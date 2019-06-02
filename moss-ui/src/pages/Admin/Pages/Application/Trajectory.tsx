import AppTbs from '@/components/appTbs'
import PageTitle from '@/components/PageTitle'
import applicationModel from '@/models/application/model'
import applicationService from '@/models/application/service'
import { renderBoolean, renderDate } from '@/util/commonTableRender'
import { connect } from '@/util/store'
import { Button, Form, Input, Layout, Row, Select, Table } from 'antd'
import { FormComponentProps } from 'antd/lib/form/Form'
import { ColumnProps } from 'antd/lib/table'
import React from 'react'

import { RouteComponentProps, withRouter } from 'react-router'

const { Content } = Layout
const { Item } = Form
const { Option } = Select

interface ITrajectoryProps {
  trajectory: TrajectoryData
}

interface ITrajectoryState {
  lastSearch: any
}

class TrajectoryPage extends React.Component<
  ITrajectoryProps & FormComponentProps & RouteComponentProps<{ id: string }>,
  ITrajectoryState
> {
  public componentDidMount() {
    applicationService.fetchTrajectory()
  }

  public render() {
    const { trajectory, form } = this.props
    const { getFieldDecorator } = form
    const { list, currentPage, totalCount } = trajectory

    const columns = [
      {
        title: '应用名',
        dataIndex: 'applicationName',
        key: 'applicationName',
        width: 140,
      },
      {
        title: 'message Id',
        dataIndex: 'messageId',
        key: 'messageId',
        width: 140,
      },
      {
        title: '时间',
        dataIndex: 'timestamp',
        key: 'timestamp',
        render: date => {
          return <p style={{ marginLeft: '4px' }}>{renderDate('YYYYMMDDHHmmss')(date)}</p>
        },
      },
      {
        title: '成功',
        dataIndex: 'success',
        key: 'success',
        render: renderBoolean,
      },
      {
        title: '交换机',
        dataIndex: 'exchange',
        key: 'exchange',
      },
      {
        title: '队列',
        dataIndex: 'queue',
        key: 'queue',
      },
      {
        title: '路由 key',
        dataIndex: 'routingKeys',
        key: 'routingKeys',
      },
      {
        title: '客户端 IP',
        dataIndex: 'clientIp',
        key: 'clientIp',
      },
      {
        title: '频道',
        dataIndex: 'channel',
        key: 'channel',
      },
      {
        title: '消息类型',
        dataIndex: 'type',
        key: 'type',
      },
      {
        title: '连接',
        dataIndex: 'connection',
        key: 'connection',
      },
      {
        title: 'payload',
        dataIndex: 'payload',
        key: 'payload',
      },
    ] as ColumnProps<Trajectory>[]

    return (
      <React.Fragment>
        <Layout className="page page-trajectory">
          <Content>
            <PageTitle name="消息轨迹" info="消息轨迹" />
            <Row>
              <Form layout="inline" className="ant-advanced-search-form" onSubmit={this.search}>
                <Item>{getFieldDecorator('appName')(<Input placeholder="搜索应用名" />)}</Item>
                <Item>
                  {getFieldDecorator('messageId')(<Input placeholder="搜索 messageId" />)}
                </Item>
                <Item>
                  {getFieldDecorator('type')(
                    <Select placeholder="消息类型" style={{ width: '150px' }}>
                      <Option value={'Consumer'}>Consumer</Option>
                      <Option value={'Producer'}>Producer</Option>
                    </Select>
                  )}
                </Item>
                <Item>
                  <Button type="primary" icon="search" htmlType="submit">
                    搜索
                  </Button>
                </Item>
              </Form>
            </Row>
            <Row className="table-container">
              <Table
                className="trajectory-table"
                dataSource={list}
                columns={columns}
                scroll={{ x: 8000 }}
                pagination={{
                  total: totalCount,
                  pageSize: 10,
                  current: currentPage,
                  onChange: this.changePage,
                }}
                expandRowByClick={true}
                expandedRowRender={this.renderDetails}
              />
            </Row>
          </Content>
        </Layout>
      </React.Fragment>
    )
  }

  private renderDetails = (record: Trajectory) => {
    const content = record.properties
      ? JSON.stringify(JSON.parse(record.properties), null, 4)
      : '暂无更多数据'
    return (
      <pre>
        <code>{content}</code>
      </pre>
    )
  }

  private search = (evt: React.FormEvent) => {
    evt.preventDefault()
    this.props.form.validateFields(
      (err: null | { [index: string]: any }, values: { [index: string]: any }) => {
        if (!err) {
          const filteredValues = Object.keys(values)
            .filter(k => values[k])
            .reduce((obj: any, k: string) => {
              obj[k] = values[k]
              return obj
            }, {})
          this.setState({
            lastSearch: filteredValues,
          })
          applicationService.fetchTrajectory(filteredValues)
        }
      }
    )
  }

  private changePage = (page: number) => {
    const { lastSearch } = this.state

    applicationService.fetchTrajectory({ ...lastSearch, pageNo: page })
  }
}

export default connect({ trajectory: applicationModel.Trajectory })(
  withRouter(Form.create()(TrajectoryPage))
)
