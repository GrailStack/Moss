import { CONSTANTS } from '@/util/common'
import AppTbs from '@/components/appTbs'
import PageTitle from '@/components/PageTitle'
import applicationModels from '@/models/application/model'
import applicationService from '@/models/application/service'
import { connect } from '@/util/store'
import '@/style/masonry.less'
import DataSet from '@antv/data-set/lib/data-set'
import { Col, Layout, Row } from 'antd'
import * as d3 from 'd3'
import _ from 'lodash'
import moment from 'moment'
import React from 'react'
import { RouteComponentProps, withRouter } from 'react-router'
import { CSSTransition, TransitionGroup } from 'react-transition-group'

import { Axis, Chart, Legend, Line, Point, Tooltip } from 'viser-react'

const { Content } = Layout
const THREAD_CHART_WIDTH = CONSTANTS.CHART_CONTENT_WIDTH

const THREAD_DUMP_DATA_UPDATE_INTERVAL = 1000 * 5
const HEIGHT_PER_LINE = 40
const TICK_COUNT = 16
const LEGEND_WIDTH = 250.0

const THREAD_STATE_COLOR_MAP = {
  RUNNABLE: CONSTANTS.CHART_COLOR.GREEN,
  WAITING: CONSTANTS.CHART_COLOR.YELLOW,
  TIMED_WAITING: CONSTANTS.CHART_COLOR.SEA_GREEN,
} as any

const THREAD_STATE_SHORT_MAP = {
  RUNNABLE: 'R',
  WAITING: 'W',
  TIMED_WAITING: 'T',
} as any

interface IThreadState {
  threadStateDatas: any[]
  timestamps: any[]
  yAxisData: any[]
  domain: any[]
}

class Thread extends React.Component<RouteComponentProps<{ id: string }>, IThreadState> {
  public state: IThreadState = {
    threadStateDatas: [],
    timestamps: [],
    yAxisData: [],
    domain: [],
  }
  private timer: any
  private dataSource: any[] = []
  private lineChartDataSource: any[] = []
  private svg: any
  private domain: any[] = []

  public componentDidMount() {
    const { match } = this.props
    this.fetchDumpInfo(match.params.id, true)

    this.timer = setInterval(() => {
      this.fetchDumpInfo(match.params.id, false)
    }, THREAD_DUMP_DATA_UPDATE_INTERVAL)
  }

  public componentWillUnmount() {
    clearInterval(this.timer)
  }

  public render() {
    return (
      <>
        <Row>
          <Col span={24}>
            <AppTbs MenuData={this.props} />
          </Col>
        </Row>
        <Layout className="page">
          <Row>
            <Col span={24}>
              <PageTitle
                name="查看线程"
                info="查看当前实例的线程情况"
                // titleExtra={<Button type="primary" ghost={true} size="small" icon="download"> Heap Dump </Button>}
              />
            </Col>
          </Row>
          <Content
            style={{
              background: '#fff',
            }}>
            {this.renderLineChart()}
            {this.renderBarChart()}
          </Content>
        </Layout>
      </>
    )
  }

  private fetchDumpInfo(id: string, firstRequest: boolean) {
    // 定时循环获取最新 dump 数据
    this.fetchThreadInfo(id, firstRequest).then((dumps: any) => {
      // dumps.splice(0, dumps.length - 20)
      const timestamp = moment().valueOf()
      const newDump = { timestamp, dumps }
      const lineNewState = this.prepareLineChartData(newDump)
      const barNewState = this.prepareBarCharData(newDump, firstRequest)

      this.setState({ ...lineNewState, ...barNewState })
    })
  }

  private renderLineChart() {
    const { threadStateDatas, timestamps } = this.state

    const dv = new DataSet.View().source(threadStateDatas)
    dv.transform({
      type: 'fold',
      fields: ['blockedCount', 'waitedCount'],
      key: 'threadStateType',
      value: 'count',
    })

    const threadStateByCountScale = [
      {
        dataKey: 'count',
        type: 'log',
        base: '10',
      },
      {
        dataKey: 'timestamp',
        type: 'cat',
        values: timestamps,
      },
    ]

    const threadStateChartOpts = {
      height: 256,
      width: THREAD_CHART_WIDTH,
      padding: [30, 30, 50, LEGEND_WIDTH],
      data: dv.rows,
      scale: threadStateByCountScale,
    }

    const threadStateSeriesOpts = {
      color: ['threadStateType', [CONSTANTS.CHART_COLOR.YELLOW, CONSTANTS.CHART_COLOR.RED]],
      position: 'timestamp*count',
    }

    const threadStateLegendOpts = {
      position: 'left-center',
      offsetX: -30,
      textStyle: {
        textAlign: 'left',
        fontSize: '12',
        fontWeight: 'bold',
      },
    }
    const threadStatePointOpts = {
      position: 'timestamp*count',
      color: ['threadStateType', [CONSTANTS.CHART_COLOR.YELLOW, CONSTANTS.CHART_COLOR.RED]],
      size: 4,
      shape: 'hollowCircle',
    }

    return (
      <Row style={{ marginBottom: '-55px' }}>
        <Chart {...threadStateChartOpts}>
          <Tooltip />
          <Axis dataKey="count" />
          {/*
          // @ts-ignore */}
          <Axis dataKey="timestamp" label={null} line={null} tickLine={null} />
          <Line {...threadStateSeriesOpts} />
          <Legend {...threadStateLegendOpts} />
          <Point {...threadStatePointOpts} />
        </Chart>
      </Row>
    )
  }

  private prepareLineChartData(newDump: any) {
    this.lineChartDataSource.push({
      timestamp: moment(newDump.timestamp).format('HH:mm:ss'),
      dumps: newDump.dumps,
    })

    // 折线图
    const timestamps: any = []
    const threadStateDatas = this.lineChartDataSource.reduce((accumulator: any, dump: any) => {
      timestamps.push(dump.timestamp)
      const threadState = {
        waitedCount: 0,
        blockedCount: 0,
        timestamp: dump.timestamp,
        paddingData: dump.paddingData,
      }
      dump.dumps = dump.dumps.map((d: any) => {
        d.timestamp = dump.timestamp
        threadState.waitedCount += d.waitedCount
        threadState.blockedCount += d.blockedCount
        return d
      })
      accumulator.push(threadState)
      return accumulator
    }, [])

    // 保证 data === TICK_COUNT, 这样条形图和折线图可以共用一个坐标轴 ~
    while (timestamps.length !== TICK_COUNT) {
      if (timestamps.length > TICK_COUNT) {
        timestamps.splice(0, 1)
      } else {
        let t = timestamps[timestamps.length - 1]
        t = moment(t, 'HH:mm:ss')
          .add(THREAD_DUMP_DATA_UPDATE_INTERVAL / 1000, 'second')
          .format('HH:mm:ss')
        timestamps.push(t)
      }
    }

    return { threadStateDatas, timestamps }
  }

  private prepareBarCharData(newDump: any, firstRequest: boolean) {
    this.dataSource.push(newDump)

    if (firstRequest) {
      const svg = d3
        .select('#d3-thread-state')
        .append('svg')
        .attr('width', THREAD_CHART_WIDTH - LEGEND_WIDTH)
      this.svg = svg
      this.domain = new Array(TICK_COUNT).fill(0).map((__, i) => {
        return newDump.timestamp + i * THREAD_DUMP_DATA_UPDATE_INTERVAL
      })
    }

    while (this.dataSource.length > TICK_COUNT) {
      // remove old data
      this.dataSource.splice(0, 1)
      this.domain.splice(0, 1)
      this.domain = this.domain.concat(
        this.domain[this.domain.length - 1] + THREAD_DUMP_DATA_UPDATE_INTERVAL
      )
    }

    const chartColumnWidth = Math.round((THREAD_CHART_WIDTH - LEGEND_WIDTH) / TICK_COUNT)
    const maxLine = this.dataSource.reduce(
      (accumulator: any, current: any, index: number) => {
        if (current.dumps.length > accumulator.maxLineCount) {
          accumulator.maxLineIndex = index
        }
        accumulator.maxLineCount = d3.max([current.dumps.length, accumulator.maxLineCount])
        return accumulator
      },
      { maxLineCount: 0, maxLineIndex: 0 }
    )
    const chartHeight = maxLine.maxLineCount * HEIGHT_PER_LINE + 40
    const newFrameTransition = d3
      .transition()
      .duration(this.dataSource.length > TICK_COUNT ? 0 : 275)
      .ease(d3.easeCubicInOut) as any

    let yAxisData = this.dataSource[maxLine.maxLineIndex].dumps
    yAxisData = yAxisData.map((d: any) => {
      const axisData = _.pick(d, ['threadState', 'threadId', 'threadName'])
      const latestDumpData = newDump.dumps.find((dump: any) => {
        return dump.threadId === axisData.threadId
      })
      if (latestDumpData) {
        axisData.threadState = latestDumpData.threadState
      }
      return axisData
    })

    this.svg.attr('height', chartHeight)
    const dumpClass = `dump_${newDump.timestamp}`
    const chartColumns = this.svg
      .selectAll('.chart-column')
      .data(this.dataSource, (d: any) => d.timestamp) // key to diff, d3 enter exit update pattern

    chartColumns
      .exit()
      .transition(
        d3
          .transition()
          .duration(275)
          .ease(d3.easeCubicInOut)
      )
      .attr('width', 0)
      .remove()
    chartColumns
      .enter()
      .append('g')
      .classed('chart-column', true)
      .classed(dumpClass, true)
      .attr('height', chartHeight)
      .attr('fill', '#ff00ff')
      .attr('transform', (_d: any, index: number) => {
        if (index === TICK_COUNT - 1) {
          return `translate(${(index + 1) * chartColumnWidth} ,${HEIGHT_PER_LINE} )`
        }
        return `translate(${index * chartColumnWidth} ,${HEIGHT_PER_LINE} )`
      })
      .merge(chartColumns)
      .transition(
        d3
          .transition()
          .duration(275)
          .ease(d3.easeCubicInOut)
      )
      .attr(
        'transform',
        (__: any, index: number) => `translate(${index * chartColumnWidth} ,${HEIGHT_PER_LINE} )`
      )

    const newFrames = d3
      .select(`.${dumpClass}`)
      .selectAll('dump-frame')
      .data(newDump.dumps)
    newFrames.exit().remove()
    newFrames
      .enter()
      .append('rect')
      .classed('dump-frame', true)
      .attr('y', (__: any, index: number) => index * HEIGHT_PER_LINE)
      .attr('height', '30px')
      .attr('fill', (d: any) => THREAD_STATE_COLOR_MAP[d.threadState])
      .attr('stroke', (d: any) => THREAD_STATE_COLOR_MAP[d.threadState])
      .attr('width', 0)
      .transition(newFrameTransition)
      .attr('width', chartColumnWidth + 1)
    return { domain: this.domain, yAxisData }
  }

  private renderBarChart() {
    const { yAxisData, domain } = this.state

    return (
      <Row className="thread-state-chart-container">
        <div className="y-axis html-legend">
          {yAxisData.map((d: any) => {
            const state = d.threadState
            const styles = {
              background: THREAD_STATE_COLOR_MAP[state],
            }
            return (
              <div className="legend" style={{ height: '30px' }} key={d.threadId}>
                <TransitionGroup>
                  <CSSTransition
                    timeout={175}
                    key={THREAD_STATE_SHORT_MAP[state]}
                    classNames="fade"
                    exit={false}>
                    <span className="stateTag" style={styles}>
                      {THREAD_STATE_SHORT_MAP[state]}
                    </span>
                  </CSSTransition>
                </TransitionGroup>
                <p className="name">{d.threadName}</p>
              </div>
            )
          })}
        </div>

        <div className="x-axis" style={{ width: (THREAD_CHART_WIDTH - LEGEND_WIDTH) / 0.8 }}>
          {' '}
          {/* CSS transform 0.8 for font-size */}
          {domain &&
            domain.map(d => {
              return (
                <p className="x-axis-item" key={d}>
                  {moment(d).format('HH:mm:ss')}
                </p>
              )
            })}
        </div>
        <Row id="d3-thread-state" />
      </Row>
    )
  }

  private fetchThreadInfo(id: string, initialized: boolean) {
    return applicationService.fetchApplicationThread(id, initialized)
  }
}

export default connect({ ThreadModel: applicationModels.Thread })(withRouter(Thread))
