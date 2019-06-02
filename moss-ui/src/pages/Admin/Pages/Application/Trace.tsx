import '@/style/masonry.less'

import { CONSTANTS } from '@/util/common'
import PageTitle from '@/components/PageTitle'
import applicationModel from '@/models/application/model'
import applicationService from '@/models/application/service'
import { connect } from '@/util/store'
import DataSet from '@antv/data-set/lib/data-set'
import '@antv/data-set/lib/transform/fold'
import { Button, Checkbox, Col, Input, Layout, Modal, Row, Table } from 'antd'
import _ from 'lodash'
import moment from 'moment'
import React from 'react'
import JSONTree from 'react-json-tree'
import { RouteComponentProps } from 'react-router'
import { Area, Axis, Chart, Legend, Tooltip } from 'viser-react'

import AppTbs from '@/components/appTbs'

interface ITraceProps {
  Trace: {
    [index: string]: any
  }
}

interface ITraceState {
  visible: boolean
  record: object
  filterRules: any[]
}

class Trace extends React.Component<
  RouteComponentProps<{ id: string }> & ITraceProps,
  ITraceState
> {
  public columns = [
    {
      title: 'timestamp',
      dataIndex: 'timestamp',
      key: 'timestamp',
      width: 170,
      fixed: 'left',
      render: (time: number) =>
        typeof time === 'number' ? moment.unix(time).fromNow() : moment(time).fromNow(),
    },
    {
      title: 'path',
      dataIndex: 'request.uri',
      key: 'request.uri',
      width: 450,
      fixed: 'left',
      render: (text: string) => (text ? text : '无数据'),
    },
    {
      title: 'method',
      dataIndex: 'request.method',
      key: 'request.method',
    },
    {
      title: 'status',
      key: 'response.status',
      dataIndex: 'response.status',
      render: (text: string) =>
        text === '200' ? (
          <i className="green statusI">200</i>
        ) : text === '500' ? (
          <i className="yellow statusI">500</i>
        ) : text === '404' ? (
          <i className="red statusI">404</i>
        ) : text ? (
          text
        ) : (
          '无数据'
        ),
    },
    {
      title: 'Content-Type',
      key: 'response.headers["Content-Type"]',
      dataIndex: 'response.headers["Content-Type"]',
      render: (text: string) => (text ? text : '无数据'),
    },
    {
      title: 'timeTaken',
      key: 'timeTaken',
      dataIndex: 'timeTaken',
      render: (text: string) => (text ? text + 'ms' : '无数据'),
    },
    {
      title: '操作',
      key: '1',
      width: 100,
      fixed: 'right',
      render: (record: any) => (
        <Button type="primary" onClick={this.showDel(record)}>
          详情
        </Button>
      ),
    },
  ] as any[]

  public state: ITraceState = {
    visible: false,
    record: {},
    filterRules: [],
  }

  private checkBoxValueMap = new Map([
    ['200', { key: 'response.status', value: '200' }],
    ['500', { key: 'response.status', value: '500' }],
    ['/actuator', { type: 'URI_EXCLUDE', key: 'request.uri', value: '/actuator' }],
  ])

  private checkBoxOptions = [
    { label: 'SUCCESS', value: '200' },
    { label: 'server errors', value: '500' },
    {
      label: 'exclude/actuator/**',
      value: '/actuator',
    },
  ]

  public componentDidMount() {
    applicationService.fetchApplicationTrace(this.props.match.params.id)
  }

  public render() {
    const { Content } = Layout
    const CheckboxGroup = Checkbox.Group
    const Search = Input.Search
    const tableData = this.getTraceData()

    const { chartData, scale } = this.getChartSource(tableData)

    return (
      <React.Fragment>
        <Row>
          <Col span={24}>
            <AppTbs MenuData={this.props} />
          </Col>
        </Row>
        <Layout className="page trace">
          <Row>
            <Col span={24}>
              <PageTitle
                name="Trace"
                info="按时间展示http请求的轨迹信息，包括请求的路径，Response的状态，调用耗时等"
              />
            </Col>
          </Row>

          <Content>
            <div
              style={{
                background: '#fff',
                padding: 12,
                width: '100%',
                wordBreak: 'break-all',
              }}>
              <Search placeholder="path filter" size="large" onChange={this.search} />
              <CheckboxGroup
                options={this.checkBoxOptions}
                onChange={this.onChange()}
                style={{ marginTop: '8px', marginBottom: '12px' }}
              />
              <Row>
                <Col span={24}>单位： ms</Col>
              </Row>
              <Chart forceFit={true} height={400} data={chartData} scale={scale}>
                <Tooltip crosshairs={{ type: 'line' }} />
                <Axis dataKey="value" />
                <Axis dataKey="timestamp" />
                <Legend />
                <Area position="timestamp*value" color={['type', [theme['primary-color']]]} />
              </Chart>

              {tableData && tableData.length > 0 ? (
                <Table
                  style={{ maxWidth: CONSTANTS.PAGE_CONTENT.MAX_WIDTH }}
                  columns={this.columns}
                  dataSource={tableData}
                  rowKey={this.rowKey}
                  scroll={{ x: 1500 }}
                />
              ) : null}
            </div>
          </Content>
        </Layout>
        <Modal
          width={'40vw'}
          title="DETAIL"
          visible={this.state.visible}
          onOk={this.handleOk}
          onCancel={this.handleCancel}>
          <JSONTree data={this.state.record} />
        </Modal>
      </React.Fragment>
    )
  }

  public showDel = (text: any) => () => {
    this.setState({
      record: text,
      visible: true,
    })
  }

  public handleOk = () => {
    this.setState({
      visible: false,
    })
  }

  public handleCancel = () => {
    this.setState({
      visible: false,
    })
  }

  private rowKey = (__: any, index: number) => String(index)

  private search = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { filterRules } = this.state
    const newFilterRules = filterRules.filter((r: any) => {
      return r.type !== 'URI_INCLUDE'
    })
    newFilterRules.push({
      type: 'URI_INCLUDE',
      key: 'request.uri',
      value: e.currentTarget.value,
    })

    this.setState({
      filterRules: newFilterRules.slice(),
    })
  }

  private getChartSource = (tableData: []) => {
    if (!tableData) {
      return {}
    }
    const chartSource: any = []
    tableData &&
      tableData.forEach((element: any) => {
        if (element.timeTaken) {
          const newObject: any = {}
          newObject.timestamp =
            typeof element.timestamp === 'number'
              ? moment.unix(element.timestamp).format('lll')
              : moment(element.timestamp).format('lll')
          newObject.timeTaken = element.timeTaken
          chartSource.push(newObject)
        }
      })

    const dv = new DataSet.View().source(chartSource)
    dv.transform({
      type: 'fold',
      fields: ['timeTaken'],
      key: 'type',
      value: 'value',
    })
    const chartData = dv.rows

    const scale = [
      {
        dataKey: 'timestamp',
        min: 0,
        max: 1,
        tickCount: 4,
      },
      {
        dataKey: 'value',
        type: 'linear',
      },
    ]

    return { chartData, scale }
  }

  private getTraceData = () => {
    const traceDatas = this.props.Trace.data
    const { filterRules } = this.state

    if (!filterRules.length) {
      return traceDatas
    }

    return traceDatas.filter((traceData: any, index: number) => {
      traceData.key = index
      return filterRules.every((rule: any) => {
        if (rule.type === 'URI_EXCLUDE') {
          return String(_.get(traceData, rule.key)).indexOf(rule.value) === -1
        }
        if (rule.type === 'URI_INCLUDE') {
          return String(_.get(traceData, rule.key)).indexOf(rule.value) !== -1
        }
        return String(_.get(traceData, rule.key)) === String(rule.value)
      })
    })
  }

  private onChange = () => (checkedValues: any[]) => {
    this.setState({
      filterRules: checkedValues.map((v: string) => {
        return this.checkBoxValueMap.get(v)
      }),
    })
  }
}

export default connect({ Trace: applicationModel.Trace })(Trace)
