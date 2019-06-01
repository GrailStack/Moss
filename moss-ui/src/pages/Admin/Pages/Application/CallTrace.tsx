import Empty from '@/components/Empty'
import PageTitle from '@/components/PageTitle'
import StateFul, { StateFulRenderPropsType, ViewState } from '@/components/StateFul'
import applicationModel from '@/models/application/model'
import applicationService from '@/models/application/service'
import { CONSTANTS } from '@/util/common'
import { connect } from '@/util/store'
import DataSet from '@antv/data-set'

import AppTbs from '@/components/appTbs'
import { Button, Col, Divider, Dropdown, Icon, Layout, Menu, Row, Tooltip, Typography } from 'antd'
import moment from 'moment'
import React from 'react'
import { RouteComponentProps, withRouter } from 'react-router'
import { Axis, Chart, Legend, Line, Point, StackBar, Tooltip as G2ToolTip } from 'viser-react'

const { Title } = Typography

const scale = [
  {
    dataKey: 'tag',
  },
]

const TIME_SELECTION_MAP = [
  {
    name: '10分钟',
    groupBy: '1m',
    timeAgo: '10m',
    time: () => moment().subtract(10, 'minutes'),
  },
  {
    name: '1小时',
    groupBy: '10m',
    timeAgo: '1h',
    time: () => moment().subtract(1, 'hour'),
  },
  {
    name: '12小时',
    groupBy: '1h',
    timeAgo: '12h',
    time: () => moment().subtract(12, 'hour'),
  },
  {
    name: '1天',
    groupBy: '2h',
    timeAgo: '1d',
    time: () => moment().subtract(1, 'day'),
  },
  {
    name: '7天',
    groupBy: '1d',
    timeAgo: '7d',
    time: () => moment().subtract(7, 'day'),
  },
]

const { Content } = Layout

interface ICallTraceProps {}

interface ICallTraceState {
  totalRequestNum: any
  requestCostTime: any
  timeText: string
}

class CallTrace extends React.Component<
  ICallTraceProps & RouteComponentProps<{ id: string }>,
  ICallTraceState
> {
  public state: ICallTraceState = {
    totalRequestNum: [],
    requestCostTime: [],
    timeText: TIME_SELECTION_MAP[0].name,
  }

  public componentDidMount() {
    const defaultTime = TIME_SELECTION_MAP[1]

    this.fetchTotalRequestNum(
      this.props.match.params.id,
      defaultTime.time().toISOString(),
      defaultTime.groupBy
    )
    this.fetchRequestCostTime(this.props.match.params.id, defaultTime.timeAgo, defaultTime.groupBy)
  }

  public render() {
    const { totalRequestNum, requestCostTime } = this.state
    const hasContent =
      totalRequestNum &&
      Boolean(totalRequestNum.length) &&
      requestCostTime &&
      Boolean(requestCostTime.length)
    return (
      <React.Fragment>
        <Row>
          <Col span={24}>
            <AppTbs MenuData={this.props} />
          </Col>
        </Row>
        <Layout className="page page-call-trace">
          <Row>
            <Col span={24}>
              <PageTitle name="请求分析" info="对指定时间内的请求总数,以及请求耗时的分布分析" />
            </Col>
          </Row>
          <Content className="content-board">
            <StateFul viewState={hasContent ? ViewState.RENDER : ViewState.EMPTY}>
              {({ viewState }: StateFulRenderPropsType) => {
                switch (viewState) {
                  case ViewState.EMPTY:
                    return this.renderEmpty()

                  case ViewState.RENDER:
                    return this.renderContent()
                  default:
                    return null
                }
              }}
            </StateFul>
          </Content>
        </Layout>
      </React.Fragment>
    )
  }

  private renderContent = () => {
    const { totalRequestNum, requestCostTime } = this.state

    const requestCostItemTpl = `<li data-index={index}>
                      <span style="background-color:{color};width:8px;height:8px;border-radius:50%;display:inline-block;margin-right:8px;"></span>
                      {name}% : {value}ms
                    </li>`

    const requestNumItemTpl = `<li data-index={index}>
                      <span style="background-color:{color};width:8px;height:8px;border-radius:50%;display:inline-block;margin-right:8px;"></span>
                      {name} : {value} 次
                    </li>`
    return (
      <React.Fragment>
        <Row type="flex" justify="space-between" align="middle">
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
          <Tooltip
            className="chart-tip"
            title="查看选择时间范围内的 : URL 请求次数分布 和 请求耗时分布">
            <Icon type="question-circle" />
          </Tooltip>
        </Row>
        <Row type="flex" justify="center">
          <Title level={3}>URL请求次数 Top 10 </Title>
        </Row>
        <Row>
          <Chart
            padding="80"
            renderer="svg"
            width={CONSTANTS.CHART_CONTENT_WIDTH_RESIZE()}
            height={600}
            data={this.getTotalRequestNumRow(totalRequestNum)}>
            <G2ToolTip useHtml={true} itemTpl={requestNumItemTpl} />
            {/*
      // @ts-ignore */}
            <Axis dataKey="时间" title={true} />
            {/*
      // @ts-ignore */}
            <Axis dataKey="请求次数" title={true} />
            <Legend position="top" />
            <StackBar position="时间*请求次数" color={['tag', CONSTANTS.CHART_COLORS]} />
          </Chart>
        </Row>
        <Divider className="chart-divider" />
        <Row type="flex" justify="center">
          <Title level={3}>请求耗时分布</Title>
        </Row>
        <Row className="request-cost-chart">
          <Chart
            padding="80"
            renderer="svg"
            width={CONSTANTS.CHART_CONTENT_WIDTH_RESIZE()}
            height={700}
            data={this.getRequestCostTimeRows(requestCostTime)}
            scale={scale}>
            <G2ToolTip itemTpl={requestCostItemTpl} />
            <Axis dataKey="时间" title={true} />
            <Axis dataKey="耗时" title={true} />
            <Legend position="top" />
            <Line position="时间*耗时" color={['tag', CONSTANTS.CHART_COLORS]} />
            <Point
              position="时间*耗时"
              color="tag"
              size={4}
              style={{ stroke: '#fff', lineWidth: 1 }}
              shape="circle"
            />
          </Chart>
        </Row>
      </React.Fragment>
    )
  }

  private renderEmpty = () => {
    return <Empty />
  }

  private menu = () => (
    <Menu>
      {TIME_SELECTION_MAP.map(selection => {
        return (
          <Menu.Item key={selection.name} onClick={this.updateDataByTime(selection.name)}>
            {selection.name}
          </Menu.Item>
        )
      })}
    </Menu>
  )

  private updateDataByTime = (name: string) => () => {
    this.setState({
      timeText: name,
    })
    const findSelection = TIME_SELECTION_MAP.find(selection => {
      return selection.name === name
    })

    if (!findSelection) {
      return
    }

    Promise.all([
      this.fetchTotalRequestNum(
        this.props.match.params.id,
        encodeURIComponent(findSelection.time().toISOString()),
        findSelection.groupBy
      ),
      this.fetchRequestCostTime(
        this.props.match.params.id,
        findSelection.timeAgo,
        findSelection.groupBy
      ),
    ])
      .then(d => {
        debugger // tslint:disable-line
      })
      .catch(e => {
        debugger // tslint:disable-line
      })
  }

  private getTotalRequestNumRow = (totalRequestNum: any) => {
    const dv = new DataSet.View().source(totalRequestNum)
    const { tag, ...rest } =
      Array.isArray(totalRequestNum) && totalRequestNum.length && totalRequestNum[0]
    dv.transform({
      type: 'fold',
      fields: Object.keys(rest),
      key: '时间',
      value: '请求次数',
    })
    return dv.rows
  }

  private getRequestCostTimeRows = (requestCostTime: any) => {
    const dv = new DataSet.View().source(requestCostTime)
    const { tag, ...rest } =
      Array.isArray(requestCostTime) && requestCostTime.length && requestCostTime[0]
    dv.transform({
      type: 'fold',
      fields: Object.keys(rest),
      key: '时间',
      value: '耗时',
    })
    return dv.rows
  }

  private fetchTotalRequestNum = (instanceId: string, time: string, groupBy: string) => {
    applicationService
      .fetchTotalRequestNum(instanceId, time, encodeURIComponent(groupBy))
      .then(data => {
        if (!Array.isArray(data.results) || !Array.isArray(data.results[0].series)) {
          return
        }

        const processedData = data.results[0].series.reduce((current: any[], serieByUrl: any) => {
          const tag = serieByUrl.tags.uri
          if (!Array.isArray(serieByUrl.values)) {
            return current
          }
          const mappedSerieValue = serieByUrl.values.reduce(
            (currentTag: any, requestNumKVArray: any) => {
              const [time, count]: [string, number] = requestNumKVArray
              const formattedSortTime = moment
                .utc(time, 'YYYY-MM-DDTHH:mm:ss.SSS[Z]')
                .format('HH:mm')
              currentTag[formattedSortTime] = count
              currentTag.total += count
              return currentTag
            },
            { tag, total: 0 }
          )
          current.push(mappedSerieValue)
          return current
        }, [])

        this.setState({
          totalRequestNum: processedData
            .sort((a: any, b: any) => {
              return b.total - a.total
            })
            .slice(0, 10)
            .map((d: any) => {
              delete d.total
              return d
            }),
        })
      })
  }

  private fetchRequestCostTime = (instanceId: string, time: string, groupBy: string) => {
    applicationService.fetchRequestCostTime(instanceId, time, groupBy).then(data => {
      if (!Array.isArray(data.results) || !Array.isArray(data.results[0].series)) {
        return
      }

      const processedData = data.results[0].series.reduce((current: any[], serieByUrl: any) => {
        const tag = serieByUrl.tags.phi
        if (!Array.isArray(serieByUrl.values)) {
          return current
        }
        const mappedSerieValue = serieByUrl.values.reduce(
          (currentTag: any, requestNumKVArray: any) => {
            const [time, count]: [string, number] = requestNumKVArray
            const formattedSortTime = moment.utc(time, 'YYYY-MM-DDTHH:mm:ss.SSS[Z]').format('HH:mm')
            currentTag[formattedSortTime] = Number((count * 100).toFixed(2))
            return currentTag
          },
          { tag }
        )
        current.push(mappedSerieValue)
        return current
      }, [])

      this.setState({
        requestCostTime: processedData,
      })
    })
  }
}

export default connect({
  totalRequestNum: applicationModel.TotalRequestNum,
  requestCostTime: applicationModel.RequestCostTime,
})(withRouter(CallTrace))
