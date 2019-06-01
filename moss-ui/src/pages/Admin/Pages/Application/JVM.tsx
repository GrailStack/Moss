import '@/style/masonry.less'
import { CONSTANTS } from '@/util/common'

import PageTitle from '@/components/PageTitle'
import applicationService from '@/models/application/service'
import { getTime } from '@/util/timestamp'
import DataSet from '@antv/data-set/lib/data-set'
import '@antv/data-set/lib/transform/fold'
import '@antv/data-set/lib/transform/percent'
import { Button, Col, Dropdown, Icon, Layout, Menu, Row, Tooltip as AntTooltip } from 'antd'
import _ from 'lodash'
import React from 'react'
import { RouteComponentProps } from 'react-router'
import { Axis, Bar, Chart, Coord, Legend, Line, Pie, Point, Tooltip } from 'viser-react'

import AppTbs from '@/components/appTbs'

interface ICUPRate {
  item: string
  count: number
}

interface IMemoryRate {
  heap: number
  nonheap: number
}

interface IGarbageGC {
  marksweepCount: string
  marksweepTime: string
  scavengeCount: string
  scavengeTime: string
  timeText: string
}

interface IMetric {
  timeText: string
  committed: string
  used: string
  init: string
  max: string
}

interface IJvmState {
  metrics: IMetric[]
  more: boolean
  timer?: NodeJS.Timeout
  garbageGc: IGarbageGC[]
  statusGc: boolean
  memoryRate: IMemoryRate[]
  cupRate: ICUPRate[]
  timeText: string
  frequency: number
}

class Jvm extends React.Component<RouteComponentProps<{ id: string }>, IJvmState> {
  public state: IJvmState = {
    metrics: [],
    garbageGc: [],
    memoryRate: [],
    cupRate: [],
    more: true,
    statusGc: true,
    timeText: '1分钟',
    frequency: 12,
  }

  private timer?: NodeJS.Timeout
  private fieldsGc: string[] = ['marksweepCount', 'scavengeCount']
  public menu = () => (
    <Menu>
      <Menu.Item onClick={this.changTime('1分钟', 12)}>1分钟</Menu.Item>
      <Menu.Item onClick={this.changTime('5分钟', 60)}>5分钟</Menu.Item>
      <Menu.Item onClick={this.changTime('10分钟', 120)}>10分钟</Menu.Item>
      <Menu.Item onClick={this.changTime('30分钟', 360)}>30分钟</Menu.Item>
      <Menu.Item onClick={this.changTime('1小时', 720)}>1小时</Menu.Item>
    </Menu>
  )

  public componentWillUnmount = () => {
    if (this.timer) {
      clearTimeout(this.timer)
      this.updateMetrics = () => null
    }
  }

  public componentDidMount() {
    this.updateMetrics(this.props.match.params.id)
    this.timer = setInterval(() => {
      this.updateMetrics(this.props.match.params.id)
    }, 5000)
  }

  public render() {
    const { Content } = Layout
    const { metrics } = this.state

    const dv = new DataSet.View().source(metrics)
    dv.transform({
      type: 'fold',
      fields: ['committed', 'used', 'init', 'max'],
      key: 'city',
      value: 'temperature',
    })
    const data = dv.rows
    const scale = [
      {
        dataKey: 'timeText',
        min: 0,
        max: 1,
        tickCount: 4,
      },
      {
        dataKey: 'temperature',
        type: 'linear',
      },
    ]

    // 垃圾回收(GC) memoryRate
    const garbageGcDv = new DataSet.View().source(this.state.garbageGc)
    garbageGcDv.transform({
      type: 'fold',
      fields: this.fieldsGc,
      key: 'city',
      value: 'temperature',
    })
    const garbageGcDvData = garbageGcDv.rows
    const garbageGcScale = [
      {
        dataKey: '',
        min: 0,
        max: 1,
        tickCount: 4,
      },
      {
        dataKey: 'temperature',
        type: 'linear',
      },
    ]

    // 内存使用率
    const memoryRateDv = new DataSet.View().source(this.state.memoryRate)
    memoryRateDv.transform({
      type: 'fold',
      fields: ['heap', 'nonheap'],
      key: 'name',
      value: 'memoryUsage',
    })
    const memoryRateData = memoryRateDv.rows

    // cpu使用率
    const cpuscale = [
      {
        dataKey: 'percent',
        min: 0,
        formatter: '.0%',
      },
    ]

    const cpudv = new DataSet.View().source(this.state.cupRate)
    cpudv.transform({
      type: 'percent',
      field: 'count',
      dimension: 'item',
      as: 'percent',
    })
    const cpudata = cpudv.rows

    return (
      <React.Fragment>
        <Row>
          <Col span={24}>
            <AppTbs MenuData={this.props} />
          </Col>
        </Row>
        <Layout className="page page-jvm">
          <Row>
            <Col span={24}>
              <PageTitle
                name="JVM"
                info="实时展示应用实例的内存使用情况，GC次数，以及CPU和内存使用率"
                rightPanelExtra={
                  <AntTooltip
                    title="heap dump 日志中会包含敏感信息
                  同时会占用实例的 CPU 硬盘, 需谨慎使用该功能">
                    <a href={applicationService.heapDownloadUrl(this.props.match.params.id)}>
                      <Button className="btn-download-dump" icon="download">
                        堆 Dump
                      </Button>
                    </a>
                  </AntTooltip>
                }
              />
            </Col>
          </Row>

          <Content>
            <div className="antvMain">
              <Row type="flex" align="middle" className="selectNode">
                <Col push={2} span={12}>
                  <Icon type="bars" /> 时间范围
                </Col>
                <Col push={2} span={9}>
                  <Dropdown overlay={this.menu()} placement="bottomLeft">
                    <Button size="small">{this.state.timeText}</Button>
                  </Dropdown>
                </Col>
              </Row>
              <div className="sectionBj">
                <div style={{ width: '55%', float: 'left' }}>
                  <span className="title" style={{ paddingLeft: '25px' }}>
                    内存使用(单位: MB)
                  </span>
                  <Chart forceFit={true} height={400} data={data} scale={scale}>
                    <Tooltip />
                    <Axis />
                    <Legend />
                    <Line
                      position="timeText*temperature"
                      color={['city', CONSTANTS.CHART_COLORS]}
                    />
                    <Point
                      position="timeText*temperature"
                      color={['city', CONSTANTS.CHART_COLORS]}
                      size={4}
                      style={{ stroke: '#fff', lineWidth: 1 }}
                      shape="circle"
                    />
                  </Chart>
                </div>
                <div style={{ width: '40%', float: 'right' }}>
                  <span className="title">内存使用率(单位: %)</span>
                  <Chart forceFit={true} height={400} data={memoryRateData}>
                    <Tooltip />
                    <Axis />
                    <Legend />
                    <Bar position="name*memoryUsage" color={['name', CONSTANTS.CHART_COLORS]} />
                  </Chart>
                </div>
                <div style={{ clear: 'both' }} />
              </div>
              <div className="sectionBj">
                <div style={{ width: '55%', float: 'left' }}>
                  <span className="title" style={{ paddingLeft: '25px' }}>
                    cpu使用率
                  </span>
                  <Chart forceFit={true} height={400} data={cpudata} scale={cpuscale}>
                    <Tooltip showTitle={false} />
                    <Coord type="theta" />
                    <Axis />
                    <Legend dataKey="item" />
                    <Pie
                      position="percent"
                      color={['item', [CONSTANTS.CHART_COLOR.RED, CONSTANTS.CHART_COLOR.BLUE]]}
                      style={{ stroke: '#fff', lineWidth: 1 }}
                      label={[
                        'percent',
                        {
                          formatter: (val: any, item: any) => {
                            return item.point.item + ': ' + val
                          },
                        },
                      ]}
                    />
                  </Chart>
                </div>
                <div style={{ width: '40%', float: 'right' }}>
                  <span className="title">
                    垃圾回收(GC)
                    <i
                      className={!this.state.statusGc ? 'btnI active' : 'btnI'}
                      onClick={this.timeGC}>
                      时间 ms
                    </i>
                    <i
                      className={this.state.statusGc ? 'btnI active' : 'btnI'}
                      onClick={this.countGC}>
                      次数
                    </i>
                  </span>
                  <Chart forceFit={true} height={400} data={garbageGcDvData} scale={garbageGcScale}>
                    <Tooltip />
                    <Axis />
                    <Legend />
                    <Line
                      position="timeText*temperature"
                      color={['city', CONSTANTS.CHART_COLORS]}
                    />
                    <Point
                      position="timeText*temperature"
                      color={['city', CONSTANTS.CHART_COLORS]}
                      size={4}
                      style={{ stroke: '#fff', lineWidth: 1 }}
                      shape="circle"
                    />
                  </Chart>
                </div>
                <div style={{ clear: 'both' }} />
              </div>
            </div>
          </Content>
        </Layout>
      </React.Fragment>
    )
  }

  private formatBytes = (byte: string) => {
    return String((Number(byte) / 1024 / 1024).toFixed(2))
  }
  private countGC = () => {
    this.fieldsGc = ['marksweepCount', 'scavengeCount']
    this.setState({
      statusGc: true,
    })
  }

  private timeGC = () => {
    this.fieldsGc = ['marksweepTime', 'scavengeTime']
    this.setState({
      statusGc: false,
    })
  }

  private updateMetrics = (id: string) => {
    applicationService.fetchApplicationMetrics(id).then(data => {
      data.metrics = _.fromPairs(
        Object.keys(data.metrics).map((k: string) => {
          return [k, Number(_.get(data.metrics, k))]
        })
      ) as MetricsData
      const timeText: string = getTime('hms')
      const newMetric: IMetric = {
        timeText,
        committed: '0',
        used: '0',
        init: '0',
        max: '0',
      }
      /* tslint:disable:no-string-literal */
      // 内存使用
      if (this.state.metrics.length >= this.state.frequency) {
        this.state.metrics.splice(0, this.state.metrics.length - this.state.frequency + 1)
      }
      newMetric.committed = this.formatBytes(data.metrics.heapCommitted)
      newMetric.used = this.formatBytes(data.metrics.jvmMemoryUsedHeap)
      newMetric.init = this.formatBytes(data.metrics.heapInit)
      newMetric.max = this.formatBytes(data.metrics.heapMax)
      // 垃圾回收(GC)
      if (this.state.garbageGc.length >= this.state.frequency) {
        this.state.garbageGc.splice(0, this.state.garbageGc.length - this.state.frequency + 1)
      }
      const newObjectGarbageGc: IGarbageGC = {
        timeText,
        marksweepCount: '0',
        marksweepTime: '0',
        scavengeCount: '0',
        scavengeTime: '0',
      }
      newObjectGarbageGc.marksweepCount = data.metrics.gcPsMarksweepCount
      newObjectGarbageGc.marksweepTime = data.metrics.gcPsMarksweepTime
      newObjectGarbageGc.scavengeCount = data.metrics.gcPsScavengeCount
      newObjectGarbageGc.scavengeTime = data.metrics.gcPsScavengeTime

      // 内存使用率 memoryRate
      const memoryRateData: IMemoryRate[] = [{ heap: 0, nonheap: 0 }]

      const heap = Number(data.metrics.jvmMemoryUsedHeap) / Number(data.metrics.heapCommitted)
      memoryRateData[0].heap = Number(String((heap * 100).toFixed(0)))
      const nonheap =
        Number(data.metrics.jvmMemoryUsedNonHeap) / Number(data.metrics.nonheapCommitted)
      memoryRateData[0].nonheap = Number(String((nonheap * 100).toFixed(0)))
      // cpu使用率
      const cupRateData: ICUPRate[] = [
        {
          item: '未使用cpu',
          count: Number(data.metrics.processors) - Number(data.metrics.systemloadAverage),
        },
        { item: '已使用cpu', count: Number(data.metrics.systemloadAverage) },
      ]
      this.setState({
        metrics: this.state.metrics.concat([newMetric]),
        more: true,
        memoryRate: memoryRateData,
        garbageGc: this.state.garbageGc.concat([newObjectGarbageGc]),
        cupRate: cupRateData,
      })
      /* tslint:enable:no-string-literal */
    })
  }

  private changTime = (timeText: string, frequency: number) => () => {
    this.setState({
      timeText,
      frequency,
    })
  }
}

export default Jvm
