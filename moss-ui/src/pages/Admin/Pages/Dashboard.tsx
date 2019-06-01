import { CONSTANTS } from '@/util/common'
import dashBoardModel from '@/models/dashboard/model'
import dashBoardService from '@/models/dashboard/service'
import { connect, wholeModel } from '@/util/store'
import { EventBus, EVENTS } from '@/util/eventbus'
import DataSet from '@antv/data-set/lib/data-set'
import { Col, Divider, Icon, Layout, Row, Tabs } from 'antd'
import React from 'react'
import CountUp from 'react-countup'
import { Area, Axis, Bar, Chart, Coord, Legend, Line, Pie, Tooltip } from 'viser-react'

import './dashboard.less'
const { Content } = Layout
const TabPane = Tabs.TabPane

import { RouteComponentProps, withRouter } from 'react-router'

const STATUS = [
  {
    name: 'project',
    icon: 'project',
    title: '项目总数',
    value: 'projectNum',
    color: CONSTANTS.CHART_COLOR.BLUE,
  },
  {
    name: 'service',
    icon: 'hdd',
    title: '服务总数',
    value: 'appNum',
    color: CONSTANTS.CHART_COLOR.GREEN,
    onClick: (props: IMixDashboardProps) => () => {
      const { history } = props
      history.push('/serviceManage')
    },
  },
  {
    name: 'instance',
    icon: 'tablet',
    title: '实例总数',
    value: 'instanceNum',
    color: CONSTANTS.CHART_COLOR.YELLOW,
    onClick: (props: IMixDashboardProps) => () => {
      const { history } = props
      history.push('/list')
    },
  },
  {
    name: 'down',
    icon: 'fire',
    title: '实例 Down',
    value: 'downNum',
    color: CONSTANTS.CHART_COLOR.RED,
  },
]

const areaData = [
  { year: '0221', value: 1 },
  { year: '0222', value: 3 },
  { year: '0223', value: 2 },
  { year: '0224', value: 4 },
  { year: '0225', value: 2 },
  { year: '0226', value: 5 },
  { year: '0227', value: 6 },
]

const areaScale = [
  {
    dataKey: 'value',
    min: 1,
  },
  {
    dataKey: 'year',
    min: 0,
    max: 1,
  },
]

const scale = [
  {
    dataKey: 'percent',
    min: 0,
    formatter: '.0%',
  },
]

interface IDashboardProps {
  dashboard: DashboardBasicData
  user: LoginData
  report: ReportData
}

type IMixDashboardProps = RouteComponentProps & IDashboardProps
class Dashboard extends React.Component<IMixDashboardProps> {
  public render() {
    const { dashboard, user, report } = this.props
    delete report.count
    const formattedChartDatas = Object.keys(report).reduce(
      (chartDatas, k) => {
        const dv = new DataSet.View().source(report[k])
        dv.transform({
          type: 'percent',
          field: 'count',
          dimension: 'name',
          as: 'percent',
        })
        const data = dv.rows
        chartDatas[k] = data
        return chartDatas
      },
      {} as any
    ) as KVType

    return (
      <Content className="page page-dashboard">
        <Row className="top-board-container">
          <Col className="board board-user" span={7}>
            <div className="user-name">{user.userName}</div>
            <div>
              <div className="info-container">
                <div className="character">角色:</div>
                <div>{dashboard.role}</div>
                <div className="department">部门:</div>
                <div>{dashboard.department}</div>
              </div>
              <Divider />
              <Row type="flex" className="bottom-container">
                <Col>我的服务 : {dashboard.myAppNum}</Col>
                <Col className="my-instance">我的实例 : {dashboard.myInstanNum}</Col>
                <Col className="my-project">我的项目 : {dashboard.myProjectNum}</Col>
              </Row>
            </div>
          </Col>
          <Col className="status-board-container">
            {STATUS.map((stat: any) => this.renderTopStatusCard(dashboard, stat))}
          </Col>
        </Row>
        <Row className="charts-container" key={this.context.collapsed}>
          <div>
            <Tabs
              className="chart-tabs"
              style={{
                width: (CONSTANTS.CHART_CONTENT_WIDTH_RESIZE() / 3) * 2 - 50 - 50,
              }}>
              <TabPane
                tab={
                  <Col className="chart-title no-pad">
                    <Icon className="icon" type="bar-chart" />
                    Spring Boot 版本使用统计
                  </Col>
                }
                key="sprintBootVersions">
                <div className="charts-layout">
                  <Chart
                    key={'sprintBootVersions'}
                    renderer="svg"
                    padding={'auto'}
                    height={CONSTANTS.CHART_CONTENT_WIDTH_RESIZE() / 6 - 45}
                    width={(CONSTANTS.CHART_CONTENT_WIDTH_RESIZE() / 3) * 2 - 50 - 50 - 24}
                    data={report.sprintBootVersions}
                    scale={{ dataKey: 'count' }}>
                    <Coord type="rect" direction="LB" />
                    <Tooltip showTitle={false} />
                    <Axis />
                    <Legend dataKey="name" />
                    <Bar
                      position="name*count"
                      color={['name', CONSTANTS.CHART_COLORS]}
                      style={{ stroke: '#fff', lineWidth: 1 }}
                    />
                  </Chart>
                </div>
              </TabPane>
              <TabPane
                tab={
                  <Col className="chart-title no-pad">
                    <Icon className="icon" type="bar-chart" />
                    Spring Cloud 版本使用统计
                  </Col>
                }
                key="springCloudVersions">
                <div className="charts-layout">
                  <Chart
                    key={'springCloudVersions'}
                    renderer="svg"
                    padding={'auto'}
                    height={CONSTANTS.CHART_CONTENT_WIDTH_RESIZE() / 6 - 45}
                    width={(CONSTANTS.CHART_CONTENT_WIDTH_RESIZE() / 3) * 2 - 50 - 50 - 24}
                    data={report.springCloudVersions}
                    scale={{ dataKey: 'count' }}>
                    <Coord type="rect" direction="LB" />
                    <Tooltip showTitle={false} />
                    <Axis />
                    <Legend dataKey="name" />
                    <Bar
                      position="name*count"
                      color={['name', CONSTANTS.CHART_COLORS_REVERSED]}
                      style={{ stroke: '#fff', lineWidth: 1 }}
                    />
                  </Chart>
                </div>
              </TabPane>
            </Tabs>
          </div>
          <div>
            <div className="charts-layout">
              <Row className="chart-title">
                <Icon className="icon" type="pie-chart" />
                Spring Cloud版本使用率
              </Row>
              <Divider className="divider" />
              <Chart
                key={'springCloudVersions'}
                renderer="svg"
                padding={'auto'}
                height={CONSTANTS.CHART_CONTENT_WIDTH_RESIZE() / 6 - 45}
                width={CONSTANTS.CHART_CONTENT_WIDTH_RESIZE() / 5}
                data={formattedChartDatas.springCloudVersions}
                scale={scale}>
                <Tooltip showTitle={false} />
                <Coord type="theta" />
                <Axis />
                <Legend dataKey="name" />
                <Pie
                  position="percent"
                  color={['name', CONSTANTS.CHART_COLORS_REVERSED]}
                  style={{ stroke: '#fff', lineWidth: 1 }}
                  label={[
                    'percent',
                    {
                      formatter: (val: string, item: any) => {
                        return `${val} (${item.point.count})`
                      },
                    },
                  ]}
                />
              </Chart>
            </div>
          </div>
          <div>
            <div className="charts-layout">
              <Row className="chart-title">
                <Icon className="icon" type="pie-chart" />
                服务治理平台接入率
              </Row>
              <Divider className="divider" />
              <Chart
                key={'takeOver'}
                renderer="svg"
                padding={'auto'}
                height={CONSTANTS.CHART_CONTENT_WIDTH_RESIZE() / 6 - 45}
                width={CONSTANTS.CHART_CONTENT_WIDTH_RESIZE() / 5.8}
                data={formattedChartDatas.takeOver}
                scale={scale}>
                <Tooltip showTitle={false} />
                <Coord type="theta" />
                <Axis />
                <Legend dataKey="name" />
                <Pie
                  position="percent"
                  color={['name', CONSTANTS.CHART_COLORS]}
                  style={{ stroke: '#fff', lineWidth: 1 }}
                  label={[
                    'percent',
                    {
                      formatter: (val: string, item: any) => {
                        return `${val} (${item.point.count})`
                      },
                    },
                  ]}
                />
              </Chart>
            </div>
          </div>
        </Row>
        <Row className="area-chart">
          <Row className="chart-title">
            <Icon className="icon" type="line-chart" />
            综合数据概览
          </Row>
          <Divider className="divider" />
          <Chart
            renderer="svg"
            height={CONSTANTS.CHART_CONTENT_WIDTH_RESIZE() / 4}
            width={CONSTANTS.CHART_CONTENT_WIDTH_RESIZE()}
            data={areaData}
            scale={areaScale}>
            <Tooltip
              crosshairs={{
                type: 'y',
                style: {},
              }}
            />
            <Axis dataKey="value" />
            <Line position="year*value" size={2} color={CONSTANTS.CHART_COLOR.BLUE} />
            <Area position="year*value" color={CONSTANTS.CHART_COLOR.BLUE} />
          </Chart>
        </Row>
      </Content>
    )
  }

  public componentDidMount() {
    dashBoardService.fetchDashBoard()
    dashBoardService.fetchReports()
    window.addEventListener('resize', this.onWindowResize)
    EventBus.on(EVENTS.COLLAPSE_CHANGE, this.onCollapsedChange)
  }

  public componentWillUnmount() {
    window.removeEventListener('resize', this.onWindowResize)
    EventBus.removeListener(EVENTS.COLLAPSE_CHANGE, this.onCollapsedChange)
  }

  private renderTopStatusCard = (dashboard: any, stat: any) => {
    return (
      <Col
        key={stat.name}
        className={`board board-${stat.name}-count`}
        onClick={stat.onClick && stat.onClick(this.props)}>
        <div className="icon-container" style={{ background: stat.color }}>
          <Icon type={stat.icon} />
        </div>
        <div className="content-container">
          <div className="number" style={{ color: stat.color }}>
            <CountUp end={dashboard[stat.value] || 0} />
          </div>
          <div className="title">{stat.title}</div>
        </div>
      </Col>
    )
  }

  private onCollapsedChange = () => {
    this.forceUpdate()
  }

  private onWindowResize = () => {
    this.forceUpdate()
  }
}

export default connect({
  dashboard: dashBoardModel.dashboard,
  report: dashBoardModel.report,
  user: wholeModel.login,
})(withRouter(Dashboard))
