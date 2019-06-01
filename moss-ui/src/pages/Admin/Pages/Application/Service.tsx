import SearchBar from '@/components/SearchBar'
import '@/style/masonry.less'
import { CONSTANTS } from '@/util/common'

import PageTitle from '@/components/PageTitle'
import ServiceCard from '@/components/ServiceCard'
import applicationModel from '@/models/application/model'
import applicationService from '@/models/application/service'
import { connect, wholeModel } from '@/util/store'
import { Button, Icon, Layout, Pagination, Radio, Table, Tooltip } from 'antd'
import { RadioChangeEvent } from 'antd/lib/radio'
import moment from 'moment'
import React from 'react'
import { RouteComponentProps } from 'react-router'

const { Group: RadioGroup, Button: RadioButton } = Radio

interface IServiceProps {
  user: any
  service: ServiceData
}

interface IServiceState {
  dataView: 'TABLE' | 'COLUMN'
}

class Service extends React.Component<RouteComponentProps & IServiceProps, IServiceState> {
  public state: IServiceState = {
    dataView: 'COLUMN',
  }

  private instanceNumTitle = (
    <Tooltip title="UP数 / 总实例数">
      实例数 <Icon type="question-circle" />
    </Tooltip>
  )
  private columns = [
    {
      title: '服务名',
      dataIndex: 'name',
      key: 'name',
    },
    {
      title: '项目所有者',
      dataIndex: 'ownerName',
      key: 'ownerName',
    },
    {
      title: '项目标识',
      dataIndex: 'projectKey',
      key: 'projectKey',
    },
    {
      title: '项目名',
      dataIndex: 'projectName',
      key: 'projectName',
    },
    {
      title: this.instanceNumTitle,
      dataIndex: 'instanceNum',
      key: 'instanceNum',
      render: (_: any, data: ServiceItem) => {
        const upCount = data.instances.reduce((count: number, instance: InstanceItem) => {
          if (instance.statusInfo.status === 'UP') {
            return count + 1
          }
          return count
        }, 0)
        return `${upCount} / ${data.instances.length}`
      },
    },
    {
      title: '是否接入',
      dataIndex: 'takeOver',
      key: 'takeOver',
      render: (takeOver: number) => {
        return takeOver ? '是' : '否'
      },
    },
    {
      title: '状态',
      dataIndex: 'status',
      key: 'status',
    },
    {
      title: '更新时间',
      dataIndex: 'statusTimestamp',
      key: 'statusTimestamp',
      render: (time: number) => {
        return moment.unix(time).fromNow()
      },
    },
    {
      title: '操作',
      key: '操作',
      render: (item: ServiceItem) => {
        return (
          <Button type="primary" onClick={this._goDetail(item.name)}>
            查看实例
          </Button>
        )
      },
    },
  ]

  private lastFetchOptions: any = {}
  public componentDidMount() {
    applicationService.fetchServiceList()
  }

  public render() {
    const { user, service } = this.props
    const { list = [], currentPage, totalCount } = service
    const { dataView } = this.state
    let columnWidth = (window.innerWidth - CONSTANTS.SIDE_BAR_WIDTH - 30 * 2) / 4 - 30
    if (columnWidth < 280) {
      columnWidth = 280
    }

    const columnView = (
      <>
        <div className="page-content masonry" style={{ columnWidth }}>
          {list.map((serviceData: any) => (
            <ServiceCard
              reFetchDatas={this.reFetchDatas}
              user={user.userName}
              key={serviceData.name}
              data={serviceData}
              onClick={this.goDetail}
            />
          ))}
        </div>
        <Pagination
          total={totalCount}
          pageSize={8}
          current={currentPage}
          showTotal={this.showTotal}
          onChange={this.changePage}
        />
      </>
    )

    const tableView = (
      <Table
        className="table-view"
        dataSource={list}
        rowKey="name"
        columns={this.columns}
        pagination={{
          total: totalCount,
          pageSize: 8,
          current: currentPage,
          showTotal: this.showTotal,
          onChange: this.changePage,
        }}
      />
    )

    const contentView = dataView === 'TABLE' ? tableView : columnView
    return (
      <React.Fragment>
        <Layout className="page page-service">
          <PageTitle
            name="服务管理"
            info="服务管理可视化生产所有服务实例，帮助您快速找到您负责或收藏的服务并了解实例的内部详情"
          />

          <SearchBar onSubmit={this.search}/>
          <RadioGroup
            className="view-switch"
            onChange={this.changeDataView}
            defaultValue={'COLUMN'}>
            <RadioButton value="COLUMN">
              <Icon type="appstore" />
            </RadioButton>
            <RadioButton value="TABLE">
              <Icon type="ordered-list" />
            </RadioButton>
          </RadioGroup>

          {list.length ? contentView : <div className="noData">暂无数据</div>}
        </Layout>
      </React.Fragment>
    )
  }

  private changeDataView = (e: RadioChangeEvent) => {
    this.setState({
      dataView: e.target.value,
    })
  }

  private reFetchDatas = () => {
    applicationService.fetchServiceList(
      this.lastFetchOptions.findType as number,
      1,
      this.lastFetchOptions.appName as string,
      (this.lastFetchOptions.pageSize as number) || 8
    )
  }

  private search = (e: any) => {
    const filteredSearchObj = Object.keys(e).reduce((obj: any, current: any) => {
      obj[current] = e[current]
      return obj
    }, {})

    this.lastFetchOptions = filteredSearchObj

    applicationService.fetchServiceList(
      filteredSearchObj.findType,
      1,
      filteredSearchObj.appName,
      filteredSearchObj.pageSize || 8
    )
  }

  private _goDetail = (name: string) => () => {
    this.goDetail(name)
  }

  private goDetail = (name: string) => {
    this.props.history.push(`list?appName=${name}&pageSize=10&pageNo=1`)
  }

  private showTotal = () => {
    const { service } = this.props
    return `总数: ${service.totalCount}`
  }

  private changePage = (p: number) => {
    const { findType, appName, pageSize } = this.lastFetchOptions
    applicationService.fetchServiceList(
      !isNaN(findType) ? Number(findType) : 0,
      p,
      appName || '',
      !isNaN(pageSize) ? Number(pageSize) : 8
    )
  }
}

export default connect({
  service: applicationModel.Service,
  user: wholeModel.login,
})(Service)
