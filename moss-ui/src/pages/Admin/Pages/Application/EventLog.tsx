import PageTitle from '@/components/PageTitle'
import applicationModel from '@/models/application/model'
import applicationService from '@/models/application/service'
import { CONSTANTS } from '@/util/common'
import { connect } from '@/util/store'
import { Layout, Table } from 'antd'
import _ from 'lodash'
import moment from 'moment'
import React from 'react'
import './style.less'

interface IEventLogProps {
  eventLog: EventLogData
}

class EventLog extends React.Component<IEventLogProps, {}> {
  public componentDidMount() {
    applicationService.fetchEventLogs()
  }

  public render() {
    const { eventLog } = this.props
    const columns = [
      {
        title: 'Application',
        dataIndex: 'registration.name',
        key: 'registration.name',
        render: (text: string) => {
          return text || 'UNKNOWN'
        },
      },
      {
        title: 'Instance',
        dataIndex: 'instance',
        key: 'instance',
      },
      {
        title: 'Time',
        dataIndex: 'timestamp',
        key: 'timestamp',
        render: (time: number) => {
          return moment.unix(time).fromNow()
        },
      },
      {
        title: 'Event',
        dataIndex: 'type',
        key: 'type',
      },
    ]

    return (
      <React.Fragment>
        <Layout className="page event-list">
          <PageTitle
            name="事件日志"
            info="服务实例大盘可视化生产所有应用实例，帮助您快速找到您负责或收藏的应用并了解实例的内部详情"
          />
          <Table<EventLogItem>
            className="eventlist-table"
            columns={columns}
            dataSource={eventLog.list}
            expandRowByClick={true}
            expandedRowRender={this.renderExpandedRow}
            rowKey="timestamp"
            pagination={{
              pageSize: 8,
              current: eventLog.currentPage,
              showQuickJumper: true,
              total: eventLog.totalCount,
              onChange: this.changePage,
            }}
          />
        </Layout>
      </React.Fragment>
    )
  }

  private renderExpandedRow = (eventLog: EventLogItem) => {
    return (
      <pre
        style={{
          margin: 0,
          maxWidth: CONSTANTS.PAGE_CONTENT.MAX_WIDTH - 50 - 16,
        }}>
        {JSON.stringify(eventLog, null, 4)}
      </pre>
    )
  }

  private changePage = (pageNum: number) => {
    applicationService.fetchEventLogs(pageNum)
  }
}

export default connect({ eventLog: applicationModel.EventLog })(EventLog)
