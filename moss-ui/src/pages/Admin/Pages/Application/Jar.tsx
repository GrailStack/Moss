import '@/style/masonry.less'

import { CONSTANTS } from '@/util/common'
import Exception from '@/components/Exception'
import PageTitle from '@/components/PageTitle'
import applicationModel from '@/models/application/model'
import applicationService from '@/models/application/service'
import { connect } from '@/util/store'
import filesize from 'filesize'
import _ from 'lodash'
import './style.less'

import { Col, Collapse, Icon, Input, Layout, Radio, Row, Tooltip } from 'antd'

import * as d3 from 'd3'
import React from 'react'
import { RouteComponentProps, withRouter } from 'react-router'
import AppTbs from '@/components/appTbs'

const Content = Layout.Content
const Panel = Collapse.Panel
const Search = Input.Search

const CategoryTag = (props: { versionTabData: any }) => {
  const { versionTabData } = props
  return (
    <div className="category-tag-container">
      <Tooltip title="唯一依赖">
        <div className="tag tag-normal">
          <p>normal:({versionTabData.normalCount})</p>
          <Icon type="question-circle" />
        </div>
      </Tooltip>
      <Tooltip title="初始依赖">
        <div className="tag tag-start">
          <p>start</p>
          <Icon type="question-circle" />
        </div>
      </Tooltip>
      <Tooltip title="该 jar 包被多个库依赖, 且有多个版本, 如 : spring-web: '4.3.9.RELEASE', '4.3.17.RELEASE', '4.3.20.RELEASE', 并且当前安装的依赖包版本号 < 声明的最高版本号">
        <div className="tag tag-low">
          <p>low:({versionTabData.lowCount})</p>
          <Icon type="question-circle" />
        </div>
      </Tooltip>
      <Tooltip title="该 jar 包被多个库依赖, 且有多个版本, 如 : spring-web: '4.3.9.RELEASE', '4.3.17.RELEASE', '4.3.20.RELEASE'">
        <div className="tag tag-many">
          <p>many:({versionTabData.manyCount})</p>
          <Icon type="question-circle" />
        </div>
      </Tooltip>
    </div>
  )
}

interface IJarState {
  currentNodeData: any
  currentSegment: any
  frameworkVersion: any
  jarDeps: any
  versionTabData: any
  isFetched: boolean
  searchFilter: string
  noData: boolean
}

class Jar extends React.Component<RouteComponentProps<{ id: string }>, IJarState> {
  public state: IJarState = {
    currentSegment: 'Net',
    currentNodeData: null,
    frameworkVersion: {},
    jarDeps: {},
    versionTabData: {},
    isFetched: false,
    searchFilter: '',
    noData: false,
  }
  private isDragging: boolean = false
  private dragMovedCount: number = 0 // when click, dragStart and dragEnd will trigger, use this flag to prevent fire both of the event
  private simulation: any
  private currentSelectedNodeIndex: number = -1
  private currentSimulationStopped: boolean = false

  private debouncedSearchJarDeps = _.debounce((value: any) => {
    return this.setState({ searchFilter: value })
  }, 250)

  public componentDidMount() {
    const { match } = this.props
    applicationService
      .fetchApplicationJar(match.params.id)
      .then(data => {
        data.pomInfos = data.pomInfos.filter((i: any) => {
          return i.groupId && i.artifactId
        })
        this.drawCart(data)
      })
      .catch((e: RequestError) => {
        if (e.errorCode === 404) {
          this.setState({
            noData: true,
          })
        }
      })
  }

  public render() {
    const { currentNodeData, currentSegment, frameworkVersion, isFetched, noData } = this.state
    return (
      <div className="page-jar">
        <Row>
          <Col span={24}>
            <AppTbs MenuData={this.props} />
          </Col>
        </Row>
        <Layout className="page jardeps">
          <Row>
            <Col span={24}>
              <PageTitle name="查看依赖" info="查看当前实例的依赖情况" />
            </Col>
          </Row>
          <Content
            style={{
              background: '#fff',
              position: 'relative',
            }}>
            {noData && <Exception type="302" />}
            {!noData && <div className={currentSegment === 'Net' ? '' : 'hidden'} id="chart" />}
            {!noData && currentSegment === 'Tree' && <div>{this.renderTree()}</div>}
          </Content>
        </Layout>
        {isFetched && this.renderJarTab(currentSegment === 'Tree')}
        {isFetched && currentSegment === 'Net' && (
          <CategoryTag versionTabData={this.state.versionTabData} />
        )}
        {isFetched && currentSegment === 'Net' && this.renderVersion(frameworkVersion)}
        {currentSegment === 'Net' && this.renderDepsBoard(currentNodeData)}
      </div>
    )
  }

  private drawCart(json: any) {
    this.setState({
      isFetched: true,
      jarDeps: json,
      frameworkVersion: {
        springBootVersion: json.springBootVersion,
        springCloudVersion: json.springCloudVersion,
        summerframeworkVersion: json.summerframeworkVersion,
        totalJarCount: json.pomInfos.length,
      },
    })
    const nodes: any[] = []
    let links: any[] = []
    const versionTabData = {
      normalCount: 0,
      lowCount: 0,
      manyCount: 0,
    }
    const depsMap = new Map()
    json.pomInfos.forEach((data: any) => {
      const node = {
        group: data.groupId,
        name: data.artifactId,
        version: data.version,
      }
      nodes.push(node)
      if (Array.isArray(data.dependencies)) {
        data.dependencies = data.dependencies.map((dep: any) => {
          dep.source = dep.artifactId
          dep.target = data.artifactId

          const findData = json.pomInfos.find((rawNode: any) => {
            return rawNode.artifactId === dep.artifactId
          })

          if (findData) {
            if (!findData.usage) {
              findData.usage = []
            }
            findData.usage.push(node)
          }
          if (dep.version) {
            const versions = depsMap.get(dep.artifactId) || []
            versions.push(dep.version)
            depsMap.set(dep.artifactId, versions)
          }
          return dep
        })
        links.push(...data.dependencies)
      }
    })

    // @ts-ignore
    for (const [key, value] of depsMap) {
      const filteredValue = _.uniqWith(value, (a: any, b: any) => {
        return this.versionToNum(a) === this.versionToNum(b)
      }).sort((a: any, b: any) => {
        // @ts-ignore
        return this.versionToNum(a) - this.versionToNum(b)
      })
      depsMap.set(key, filteredValue)
    }

    links = links
      .filter((dep: any) => {
        const find = nodes.find((n: any) => {
          return n.artifactId === dep.target
        })
        return !find
      })
      .map((l: any) => {
        l.source = nodes.findIndex((n: any) => {
          return n.name === l.source
        })
        l.target = nodes.findIndex((n: any) => {
          return n.name === l.target
        })
        return l.source > 0 && l.target > 0 ? l : null
      })
      .filter((dep: any) => dep !== null)

    const width = CONSTANTS.CHART_CONTENT_WIDTH
    const height = 1000

    const simulation = d3.forceSimulation()
    const force = simulation
      .force(
        'charge',
        d3
          .forceManyBody()
          .theta(0.1)
          .strength(-700)
          .distanceMin(-120)
          .distanceMax(400)
      )
      .force(
        'link',
        d3
          .forceLink()
          .id((d: any) => d.index)
          .distance(140)
      )
      .force('center', d3.forceCenter(width / 2, height / 2))
      .force('y', d3.forceY(0.0001))
      .force('x', d3.forceX(0.0001)) as any
    this.simulation = simulation

    const svg = d3
      .select('#chart')
      .append('svg')
      .attr('width', width)
      .attr('height', height)

    const g = svg.append('g')

    const z = d3
      .zoom()
      .scaleExtent([1, 10])
      .on('zoom', () => {
        g.attr('transform', d3.event.transform)
      })
    svg
      // @ts-ignore
      .call(z)
      .on('dblclick.zoom', null)
      .on('click', () => {
        if (d3.event.path[0].tagName === 'svg') {
          // click on the black space
          debouncedHandleMouseOut()
          this.setState({
            currentNodeData: null,
          })
        }
      })

    g.append('svg:defs')
      .selectAll('marker')
      .data(['end'])
      .enter()
      .append('svg:marker')
      .attr('id', String)
      .attr('markerUnits', 'userSpaceOnUse')
      .attr('viewBox', '0 -5 10 10')
      .attr('refX', 18)
      .attr('refY', 0)
      .attr('markerWidth', 12)
      .attr('markerHeight', 12)
      .attr('orient', 'auto')
      .attr('stroke-width', 2)
      .append('svg:path')
      .attr('d', 'M2,0 L0,-3 L9,0 L0,3 M2,0 L0,-3')

    force
      .nodes(nodes)
      // @ts-ignore
      .force('link')
      .links(links)

    const link = g
      .selectAll('.link')
      .data(links)
      .enter()
      .append('svg:line')
      .attr('class', 'link')
      .attr('marker-end', 'url(#end)')

    // @ts-ignore
    const handleMouseOver = (data: any, i: number, allNode: [any]) => {
      if (this.isDragging || !allNode) {
        return
      }
      this.currentSelectedNodeIndex = i
      if (!this.currentSimulationStopped) {
        this.simulation.stop()
        nodes.forEach(node => {
          node.fixed = true
        })
        this.currentSimulationStopped = true
      }
      const hoverNodeData = json.pomInfos.find((info: any) => {
        return info.artifactId === data.name
      })
      d3.selectAll('circle').attr('opacity', (d: any) => {
        const isMouseOver = d.name === hoverNodeData.artifactId
        const isDep = hoverNodeData.dependencies.find((dep: any) => {
          return dep.artifactId === d.name
        })
        return isDep || isMouseOver ? 1 : 0.2
      })
      d3.selectAll('text').attr('opacity', (d: any) => {
        const isMouseOver = d.name === hoverNodeData.artifactId
        const isDep = hoverNodeData.dependencies.find((dep: any) => {
          return dep.artifactId === d.name
        })
        return isDep || isMouseOver ? 1 : 0.2
      })
      d3.selectAll('.link').attr('opacity', (l: any) => {
        const isDep = hoverNodeData.dependencies.find((dep: any) => {
          return dep === l
        })
        return isDep ? 1 : 0.2
      })
    }
    const debouncedHandleMouseOver = _.debounce(handleMouseOver.bind(this), 200) // start after mouse out anim finish

    const handleMouseOut = () => {
      if (this.isDragging) {
        return
      }
      this.currentSelectedNodeIndex = -1
      if (this.currentSimulationStopped) {
        this.simulation.restart()
        nodes.forEach(node => {
          node.fixed = false
        })
        this.currentSimulationStopped = false
      }
      this.resetChartAnim()
    }
    const debouncedHandleMouseOut = _.debounce(handleMouseOut.bind(this), 50)

    // @ts-ignore
    const clicked = (data: any, i: number, allNode: [any]) => {
      if (d3.event.defaultPrevented) {
        return
      }
      const currentNodeData = json.pomInfos.find((j: any) => {
        return j.artifactId === allNode[i].__data__.name
      })

      if (this.currentSelectedNodeIndex === i) {
        debouncedHandleMouseOut()
      } else {
        debouncedHandleMouseOver(data, i, allNode)
      }

      this.setState({
        currentNodeData,
      })
    }

    const dragstarted = (d: any) => {
      if (!this.currentSimulationStopped) {
        this.resetChartAnim()
      }
      if (!d3.event.active && !this.currentSimulationStopped) {
        force.alphaTarget(0.5).restart()
      }
      d.fx = d.x
      d.fy = d.y
    }

    const dragged = (d: any) => {
      this.isDragging = true
      this.dragMovedCount++
      d.fx = d3.event.x
      d.fy = d3.event.y
    }

    const dragended = (d: any) => {
      this.isDragging = false
      if (this.dragMovedCount > 8) {
        this.dragMovedCount = 0
        if (!d3.event.active && !this.currentSimulationStopped) {
          force.alphaTarget(0.5)
        }
        d.fx = null
        d.fy = null
      }
    }

    const d3Node = g
      .selectAll('.node')
      .data(nodes)
      .enter()
      .append('g')
      .attr('class', 'node')

      .call(
        // @ts-ignore
        d3
          .drag()
          .on('start', dragstarted)
          .on('drag', dragged)
          .on('end', dragended)
      )
      .on('click', clicked)

    d3Node
      .append('circle')
      .attr('r', 13)
      .attr('fill', (d: any) => {
        let fillColor = CONSTANTS.CHART_COLOR.GREEN
        d.type = 'normal'
        if (nodes.indexOf(d) === 0) {
          d.type = 'start'
          fillColor = CONSTANTS.CHART_COLOR.YELLOW
          versionTabData.normalCount++
        } else {
          const curVersion = d.version
          const versions = depsMap.get(d.name) || []
          if (versions.length > 0) {
            if (curVersion !== versions[versions.length - 1]) {
              d.type = 'low'
              fillColor = CONSTANTS.CHART_COLOR.RED
              versionTabData.lowCount++
            } else if (versions.length > 1) {
              d.type = 'many'
              fillColor = CONSTANTS.CHART_COLOR.LIGNT_YELLOW
              versionTabData.manyCount++
            }
          }
        }
        if (d.type === 'normal') {
          versionTabData.normalCount++
        }
        this.setState({
          versionTabData: { ...versionTabData },
        })
        return fillColor
      })

    d3Node
      .append('text')
      .attr('dx', -18)
      .attr('dy', 6)
      .style('font-size', '14px')
      .text((d: any) => {
        return d.name
      })

    force.on('tick', () => {
      link
        .attr('x1', (d: any) => {
          return d.source.x
        })
        .attr('y1', (d: any) => {
          return d.source.y
        })
        .attr('x2', (d: any) => {
          return d.target.x
        })
        .attr('y2', (d: any) => {
          return d.target.y
        })
      d3Node.attr('transform', (d: any) => {
        return 'translate(' + d.x + ',' + d.y + ')'
      })
    })
    return
  }

  private renderTree() {
    const { jarDeps, searchFilter } = this.state
    if (!jarDeps || !Array.isArray(jarDeps.pomInfos)) {
      return null
    }
    const filteredDeps = jarDeps.pomInfos.filter((dep: any) => {
      return dep.artifactId.indexOf(searchFilter) !== -1
    })
    return (
      <Collapse className="tree-chart-container">
        {filteredDeps.map((jar: any) => {
          return (
            <Panel
              disabled={!jar.dependencies || jar.dependencies.length === 0}
              header={this.titleForJar(jar)}
              key={jar.artifactId + jar.groupId + jar.scope}>
              <div className="panel-content-container">
                {jar.dependencies && <div className="title">Dependencies : </div>}
                {jar.dependencies &&
                  jar.dependencies.map((dep: any, i: number) => {
                    return (
                      <p className="dep" key={i}>
                        {this.titleForJar(dep)}
                      </p>
                    )
                  })}
                {jar.usage && <div className="title">Usage : </div>}
                {jar.usage &&
                  jar.usage.map((dep: any, i: number) => {
                    return (
                      <p className="dep" key={i}>{`${dep.group}:${dep.name}:${
                        !dep.version || dep.version === 'null' ? '' : dep.version
                      }`}</p>
                    )
                  })}
              </div>
            </Panel>
          )
        })}
      </Collapse>
    )
  }

  private renderJarTab(showSearch: boolean) {
    return (
      <div className="jar-chart-tab">
        <Radio.Group defaultValue="Net" onChange={this.handleSegmentChange}>
          <Radio.Button value="Net">Net</Radio.Button>
          <Radio.Button value="Tree">Tree</Radio.Button>
        </Radio.Group>
        {showSearch && (
          <Search
            className="search-deps"
            placeholder="搜索依赖"
            onChange={this.searchJarDeps}
            style={{ width: 200 }}
          />
        )}
      </div>
    )
  }

  private searchJarDeps = (e: any) => {
    return this.debouncedSearchJarDeps(e.currentTarget.value)
  }

  private renderDepsBoard(currentNodeData: any) {
    if (!currentNodeData) {
      return null
    }
    if (Array.isArray(currentNodeData.usage)) {
      currentNodeData.usage = _.uniqWith(currentNodeData.usage, (a: any, b: any) => {
        return a.name === b.name && a.group === b.group && a.version === b.version
      })
    }
    return (
      <div className="node-data-panel">
        <p>
          {currentNodeData.artifactId} : {currentNodeData.version}
        </p>
        <p>Location: {currentNodeData.location}</p>
        <p>Size: {filesize(currentNodeData.size)}</p>
        {currentNodeData.dependencies && currentNodeData.dependencies.length > 0 && (
          <p>Dependencies:</p>
        )}
        {currentNodeData.dependencies &&
          currentNodeData.dependencies.map((dep: any) => {
            return (
              <p className="dep" key={dep.artifactId + dep.groupId + dep.scope}>
                {this.titleForJar(dep)}
              </p>
            )
          })}
        {currentNodeData.usage && <p>Usage:</p>}
        {currentNodeData.usage &&
          currentNodeData.usage.map((u: any) => {
            return (
              <p className="dep" key={u.name + u.group}>{`${u.group}:${u.name}:${
                !u.version || u.version === 'null' ? '' : u.version
              }`}</p>
            )
          })}
      </div>
    )
  }

  private renderVersion = (version: any) => {
    const { springCloudVersion, springBootVersion, totalJarCount } = version
    return (
      <div className="page-section jar-framework-panel">
        <div className="page-section-content page-section-list">
          <Row>
            <Col span={12}>springCloudVersion</Col>
            <Col span={12}>{springCloudVersion}</Col>
          </Row>
          <Row>
            <Col span={12}>springBootVersion</Col>
            <Col span={12}>{springBootVersion}</Col>
          </Row>
          <Row>
            <Col span={12}>totalJarCount</Col>
            <Col span={12}>{totalJarCount}</Col>
          </Row>
        </div>
      </div>
    )
  }

  private titleForJar(jar: any) {
    return `${jar.artifactId}:${jar.groupId}${
      !jar.version || jar.version === 'null' ? '' : ':' + jar.version
    }`
  }

  private applyAnim(ele: any, reset: boolean = false) {
    ele
      .transition()
      .ease(d3.easeCubicInOut)
      .duration(175)
      .attr('r', reset ? 13 : 18)
      .attr('opacity', 1)
  }

  private handleSegmentChange = (e: any) => {
    this.setState({
      currentSegment: e.target.value,
    })
  }

  private resetChartAnim() {
    this.applyAnim(d3.selectAll('circle'), true)
    this.applyAnim(d3.selectAll('text'), true)
    this.applyAnim(d3.selectAll('.link'), true)
  }
  private versionToNum(v: any) {
    let matchs
    // prettier-ignore
    if (v && (matchs = v.match(/\d+\.\d+\.\d+|\d+\.\d+/g)) !== null) {  // tslint:disable-line

      const parts = matchs.shift().split('.');
      let num = 0;
      if (parts.length === 2) {
        parts.push(0);
      }

      for (let i = 0; i < parts.length; i++) {
        num += (parts[i] - 0) * Math.pow(10000, 2 - i);
      }
      return num;
    }
    return v
  }
}

export default connect({ Jar: applicationModel.Jar })(withRouter(Jar))
