import * as d3 from 'd3'
import React from 'react'

interface IProp {
  width: number
  calls: any
  nodes: any
}

interface IState {
  images: any
  width: number
}

class AppTopo extends React.PureComponent<IProp, IState> {
  public static getDerivedStateFromProps(props: IProp, state: IState) {
    if (props.width !== state.width) {
      return {
        width: props.width,
      }
    }
    return null
  }

  public barChartRef: any = React.createRef()

  public state = {
    images: {
      USER: require('@/assets/servermap/USER.png'),
      UNKNOWN: require('@/assets/servermap/UNKNOWN.png'),
      UNKNOWN_CLOUD: require('@/assets/servermap/UNKNOWN_CLOUD.png'),
      USER1: require('@/assets/servermap/USER1.png'),
      UNDEFINED: require('@/assets/servermap/UNDEFINED.png'),
      KAFKACONSUMER: require('@/assets/servermap/kafka.png'),
      KAFKA: require('@/assets/servermap/kafka.png'),
      H2: require('@/assets/servermap/DATABASE.png'),
      REDIS: require('@/assets/servermap/REDIS.png'),
      TOMCAT: require('@/assets/servermap/TOMCAT.png'),
      HTTPCLIENT: require('@/assets/servermap/www(1).png'),
      DUBBO: require('@/assets/servermap/DUBBO_PROVIDER.png'),
      MOTAN: require('@/assets/servermap/DATABASE.png'),
      RESIN: require('@/assets/servermap/RESIN.png'),
      FEIGN: require('@/assets/servermap/www(1).png'),
      OKHTTP: require('@/assets/servermap/www(1).png'),
      SPRINGRESTTEMPLATE: require('@/assets/servermap/www(1).png'),
      SPRINGMVC: require('@/assets/servermap/SPRING_BOOT.png'),
      STRUTS2: require('@/assets/servermap/DATABASE.png'),
      NUTZMVC: require('@/assets/servermap/SPRING_BOOT.png'),
      NUTZHTTP: require('@/assets/servermap/www(1).png'),
      JETTYCLIENT: require('@/assets/servermap/www(1).png'),
      JETTYSERVER: require('@/assets/servermap/SPRING_BOOT.png'),
      SHARDINGJDBC: require('@/assets/servermap/ShardingJDBC.png'),
      GRPC: require('@/assets/servermap/GRPC.png'),
      ELASTICJOB: require('@/assets/servermap/ElasticJob.png'),
      HTTPASYNCCLIENT: require('@/assets/servermap/www(1).png'),
      DUBBO_PROVIDER: require('@/assets/servermap/DUBBO_PROVIDER.png'),
      DUBBO_PROVIDER_GROUP: require('@/assets/servermap/DUBBO_PROVIDER_GROUP.png'),
      ServiceComb: require('@/assets/servermap/ORACLE_GROUP.png'),
      ORACLE: require('@/assets/servermap/ORACLE.png'),
      NG: require('@/assets/servermap/ng.png'),
      NBASE: require('@/assets/servermap/NBASE.png'),
      NBASE_T: require('@/assets/servermap/NBASE_T.png'),
      NBASE_ARC: require('@/assets/servermap/NBASE_ARC.png'),
      NBASE_ARC_GROUP: require('@/assets/servermap/NBASE_ARC_GROUP.png'),
      MYSQL: require('@/assets/servermap/MYSQL.png'),
      MYSQL_GROUP: require('@/assets/servermap/MYSQL.png'),
      MSSQLSERVER: require('@/assets/servermap/MSSQLSERVER.png'),
      MSSQLSERVER_GROUP: require('@/assets/servermap/MSSQLSERVER_GROUP.png'),
      MONGODB: require('@/assets/servermap/MONGODB.png'),
      MONGODB_GROUP: require('@/assets/servermap/MONGODB_GROUP.png'),
      MEMCACHED: require('@/assets/servermap/MEMCACHED.png'),
      MARIADB: require('@/assets/servermap/MARIADB.png'),
      MARIADB_GROUP: require('@/assets/servermap/MARIADB_GROUP.png'),
      JETTY: require('@/assets/servermap/JETTY.png'),
      JBOSS: require('@/assets/servermap/JBOSS.png'),
      FILTER: require('@/assets/servermap/FILTER.png'),
      ETC: require('@/assets/servermap/ETC.png'),
      CUBRID: require('@/assets/servermap/CUBRID.png'),
      CUBRID_GROUP: require('@/assets/servermap/CUBRID_GROUP.png'),
      CLIENT: require('@/assets/servermap/CLIENT.png'),
      CASSANDRA: require('@/assets/servermap/CASSANDRA.png'),
      BLOC: require('@/assets/servermap/BLOC.png'),
      BACKEND: require('@/assets/servermap/BACKEND.png'),
      ARCUS: require('@/assets/servermap/ARCUS.png'),
      APACHE: require('@/assets/servermap/APACHE.png'),
      ACTIVEMQ_CLIENT: require('@/assets/servermap/ACTIVEMQ_CLIENT.png'),
      ACTIVEMQ_CLIENT_GROUP: require('@/assets/servermap/ACTIVEMQ_CLIENT_GROUP.png'),
    },
    width: 0,
  }

  private diagonal = d3
    .linkHorizontal()
    .source((d: any) => {
      d.sx = d.source.x
      d.sy = d.source.y
      if (Math.abs(d.source.x - d.target.x) < 40) {
        return [d.source.x, d.source.y]
      }
      if (d.source.x > d.target.x) {
        return [d.source.x, d.source.y]
      }
      d.sx = d.source.x + 100
      return [d.source.x + 100, d.source.y]
    })
    .target((d: any) => {
      d.tx = d.target.x
      d.ty = d.target.y
      if (Math.abs(d.source.x - d.target.x) < 40) {
        return [d.target.x, d.target.y]
      }
      if (d.source.x < d.target.x) {
        return [d.target.x, d.target.y]
      }
      d.tx = d.target.x + 100
      return [d.target.x + 100, d.target.y]
    })

  private diagonalvertical = d3
    .linkVertical()
    .source((d: any) => {
      d.sx = d.source.x
      d.sy = d.source.y
      if (Math.abs(d.source.x - d.target.x) < 40) {
        return [d.source.x, d.source.y]
      }
      if (d.source.x > d.target.x) {
        return [d.source.x, d.source.y]
      }
      d.sx = d.source.x + 100
      return [d.source.x + 100, d.source.y]
    })
    .target((d: any) => {
      d.tx = d.target.x
      d.ty = d.target.y
      if (Math.abs(d.source.x - d.target.x) < 40) {
        return [d.target.x, d.target.y]
      }
      if (d.source.x < d.target.x) {
        return [d.target.x, d.target.y]
      }
      d.tx = d.target.x + 100
      return [d.target.x + 100, d.target.y]
    })

  private line: any = {}
  private linkText: any = {}
  private node: any = {}
  private force: any = {}

  public componentDidMount() {
    this.createBarChart(this.props.width)
  }

  public componentDidUpdate(prevProps: IProp) {
    if (prevProps.width !== this.state.width) {
      this.createBarChart(this.props.width)
    }
  }

  public createBarChart = (width: number) => {
    d3.select(this.barChartRef.current)
      .selectAll('svg')
      .remove()
    const svg: any = d3
      .select(this.barChartRef.current)
      .append('svg')
      .attr('width', width - 40)
      .attr('height', '440')
    const codeId = this.props.nodes.map((i: any) => i.id)
    this.props.calls.map((item: any, index: number) => {
      if (codeId.indexOf(item.target) === -1) {
        this.props.calls[index].target = this.props.calls[index].source
      }
    })
    svg.select('.graph').remove()
    this.force = d3
      .forceSimulation(this.props.nodes)
      .force('collide', d3.forceCollide().radius(() => 90))
      .force('yPos', d3.forceY().strength(1))
      .force('xPos', d3.forceX().strength(1))
      .force('charge', d3.forceManyBody().strength(-500))
      .force('link', d3.forceLink(this.props.calls).id((d: any) => d.id))
      .force('center', d3.forceCenter((width - 40) / 2, 440 / 2))
      .on('tick', this.tick)
      .stop()
    const graph = svg.append('g').attr('class', 'graph')
    svg.call(this.getZoomBehavior(graph))
    svg.on('click', (d: any) => {
      this.toggleNode(this.node, d, false)
      this.toggleLine(this.line, d, false)
      this.toggleLineText(this.linkText, d, false)
    })
    const defs = graph.append('defs')
    const arrowMarker = defs
      .append('marker')
      .attr('id', 'arrow')
      .attr('markerUnits', 'strokeWidth')
      .attr('markerWidth', '12')
      .attr('markerHeight', '12')
      .attr('viewBox', '0 0 12 12')
      .attr('refX', '11')
      .attr('refY', '6')
      .attr('orient', 'auto')
    const arrowPath = 'M2,2 L10,6 L2,10 L5,6 L2,2'
    const glink = graph.append('g').selectAll('.link')
    const link = glink
      .data(this.props.calls)
      .enter()
      .append('g')
    this.line = link
      .append('path')
      .attr('class', 'link')
      .attr('stroke-dasharray', (d: any) => (d.cpm ? '30 3' : '0'))
      .attr('stroke', (d: any) => (d.cpm ? 'rgba(24, 144, 255, 0.8)' : 'rgba(199, 199, 210, 0.6)'))
      .attr('marker-end', 'url(#arrow)')
    this.linkText = link.append('g')
    this.linkText
      .append('rect')
      .attr('rx', 10)
      .attr('ry', 10)
      .attr('width', 20)
      .attr('height', 20)
      .attr('x', -10)
      .attr('y', -10)
      .attr('fill', '#292d34')
    this.linkText
      .append('text')
      .attr('font-size', 10)
      .attr('class', 'linkText')
      .attr('text-anchor', 'middle')
      .attr('y', 3)
      .text((d: any) => d.cpm)
    arrowMarker
      .append('path')
      .attr('d', arrowPath)
      .attr('fill', 'rgba(24, 144, 255, 0.8)')
    const gnode = graph.append('g').selectAll('.node')
    this.node = gnode
      .data(this.props.nodes)
      .enter()
      .append('g')
      .call(
        d3
          .drag()
          .on('start', this.dragstart)
          .on('drag', this.dragged)
          .on('end', this.dragended)
      )
      .on('click', (d: any, index: number, allnode: any) => {
        d3.event.stopPropagation()
        this.node.attr('class', '')
        d3.select(allnode[index]).attr('class', 'node-active')
        const copyD = JSON.parse(JSON.stringify(d))
        delete copyD.x
        delete copyD.y
        delete copyD.vx
        delete copyD.vy
        delete copyD.fx
        delete copyD.fy
        delete copyD.index
        this.toggleNode(this.node, d, true)
        this.toggleLine(this.line, d, true)
        this.toggleLineText(this.linkText, d, true)
      })
    this.node
      .append('rect')
      .attr('class', (d: any) => {
        if (d.sla) {
          if (d.sla < 100) {
            return 'node-error'
          } else if (d.sla > 100 && d.sla < 10000) {
            return 'node-error'
          }
        }
        return 'node'
      })
      .attr('rx', 4)
      .attr('ry', 4)
      .attr('width', 100)
      .attr('height', 100)
    this.node
      .append('image')
      .attr('width', 48)
      .attr('height', 48)
      .attr('style', 'cursor: move;')
      .attr('x', 23)
      .attr('y', 10)
      .attr('xlink:href', (d: any) => {
        if (!d.type || d.type === 'N/A' || d.type === '') {
          return this.state.images.UNKNOWN
        } else {
          const name: string = d.type.toUpperCase().replace('-', '')
          // @ts-ignore
          return this.state.images[name]
        }
      })

    this.node
      .append('text')
      .attr('class', 'node-name')
      .attr('font-size', 13)
      .attr('y', 80)
      .attr('fill', '#333333')
      .text((d: any) => d.name)

    const wrap = (textSelections: any, maxWidth: number, padding: number, maxLine = 2) => {
      textSelections.each(function() {
        // @ts-ignore
        const text = d3.select(this)
        const words = text
          .text()
          .split('')
          .reverse()
        let word
        let line: any[] = []
        let lineNumber = 0
        const x = text.attr('x') || 0
        const y = text.attr('y')
        const dy = 0
        let tspan = text
          .text(null)
          .append('tspan')
          .attr('x', x)
          .attr('y', y)
          .attr('dy', dy + 'em') as any
        // prettier-ignore
        while ((word = words.pop())) {          // tslint:disable-line
          line.push(word);
          tspan.text(line.join(''));
          if (tspan.node().getComputedTextLength() > maxWidth - padding) {
            line.pop();
            tspan.text(line.join(''));
            line = [word];
            tspan = text
              .append('tspan')
              .attr('x', x)
              .attr('y', y)
              .attr('dy', ++lineNumber + dy + 'em')
              .text(word);
          }
        }

        if (lineNumber > maxLine) {
          text
            .selectAll('tspan')
            .filter((_, index) => {
              return index >= maxLine
            })
            .remove()
          // @ts-ignore
          const lastTspan = text.selectAll('tspan').last()
          let truncatedText = lastTspan.text()
          if (lastTspan.text() > 3) {
            truncatedText = lastTspan.text().slice(0, lastTspan.text().length - 3)
          }
          lastTspan.text(truncatedText + '...')
        }
        text.attr('x', (maxWidth - text.node().getBBox().width) / 2)
        text.selectAll('tspan').attr('x', (maxWidth - text.node().getBBox().width) / 2)
      })
    }
    d3.selectAll('.node-name').call(wrap, 100, 20)

    d3.timeout(() => {
      for (
        let i = 0,
          n = Math.ceil(Math.log(this.force.alphaMin()) / Math.log(1 - this.force.alphaDecay()));
        i < n;
        i += 1
      ) {
        this.force.tick()
        this.tick()
      }
    })
  }

  public isLinkNode = (currNode: any, isnode: any) => {
    if (currNode.id === isnode.id) {
      return true
    }
    return this.props.calls.filter(
      (i: any) =>
        (i.source.id === currNode.id || i.target.id === currNode.id) &&
        (i.source.id === isnode.id || i.target.id === isnode.id)
    ).length
  }
  public toggleNode = (nodeCircle: any, currNode: any, isHover: any) => {
    if (isHover) {
      // 提升节点层级
      nodeCircle.sort((a: any) => (a.id === currNode.id ? 1 : -1))
      nodeCircle
        .style('opacity', 0.2)
        .filter((isnode: any) => this.isLinkNode(currNode, isnode))
        .style('opacity', 1)
    } else {
      nodeCircle.style('opacity', 1)
    }
  }
  public toggleLine = (linkLine: any, currNode: any, isHover: any) => {
    if (isHover) {
      linkLine
        .style('opacity', 0.05)
        .style('animation', 'none')
        .filter((link: any) => this.isLinkLine(currNode, link))
        .style('opacity', 1)
        .style('animation', 'dash 6s linear infinite')
    } else {
      linkLine.style('opacity', 1).style('animation', 'dash 6s linear infinite')
    }
  }
  public isLinkLine = (isnode: any, link: any) => {
    return link.source.id === isnode.id || link.target.id === isnode.id
  }
  public toggleLineText = (lineText: any, currNode: any, isHover: any) => {
    if (isHover) {
      lineText.style('fill-opacity', (link: any) => (this.isLinkLine(currNode, link) ? 1.0 : 0.0))
    } else {
      lineText.style('fill-opacity', '1.0')
    }
  }

  public tick = () => {
    this.line.attr('stroke-width', 1).attr('d', (d: any) => {
      if (Math.abs(d.sx - d.tx) > Math.abs(d.sy - d.ty)) {
        return this.diagonal(d)
      } else if (Math.abs(d.sx - d.tx) < Math.abs(d.sy - d.ty)) {
        return this.diagonalvertical(d)
      } else {
        return this.diagonal(d)
      }
    })
    this.linkText.attr('transform', (d: any) => {
      return `translate(${d.sx - (d.sx - d.tx) / 2}, ${d.sy - (d.sy - d.ty) / 2})`
    })
    this.node.attr('transform', (d: any) => `translate(${d.x},${d.y - 20})`)
  }

  public getZoomBehavior = (g: any) => {
    return d3
      .zoom()
      .scaleExtent([0.3, 10])
      .on('zoom', () => {
        g.attr(
          'transform',
          `translate(${d3.event.transform.x},${d3.event.transform.y})scale(${d3.event.transform.k})`
        )
      })
  }

  public dragstart = () => {
    this.node._groups[0].forEach((d: any) => {
      d.__data__.fx = d.__data__.x
      d.__data__.fy = d.__data__.y
    })
    if (!d3.event.active) {
      this.force.alphaTarget(0.01).restart()
    }
    d3.event.sourceEvent.stopPropagation()
  }
  public dragged = (d: any) => {
    d.fx = d3.event.x
    d.fy = d3.event.y
  }
  public dragended = () => {
    if (!d3.event.active) {
      this.force.alphaTarget(0)
    }
  }

  public textBreaking = (d3text: any, text: any) => {
    d3text
      .append('tspan')
      .attr('class', 'textwrap')
      .attr('x', 0)
      .attr('y', 80)
      .text(text)
  }

  public render() {
    return <div className="app-topo" ref={this.barChartRef} />
  }
}

export default AppTopo
