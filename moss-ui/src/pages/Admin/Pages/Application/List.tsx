import '@/style/masonry.less'

import ApplicationCard from '@/components/ApplicationCard'
import PageTitle from '@/components/PageTitle'
import SearchBar from '@/components/SearchBar'
import applicationModel from '@/models/application/model'
import applicationService from '@/models/application/service'
import { qsParse } from '@/util'
import { CONSTANTS } from '@/util/common'
import { connect, wholeModel } from '@/util/store'
import { Layout, Pagination } from 'antd'
import React from 'react'
import { RouteComponentProps, withRouter } from 'react-router'

interface IServicePageProps extends RouteComponentProps {
  application: InstanceData
  user: LoginData
}

interface IServicePageState {
  search: { [key: string]: string }
}

class ServicePage extends React.Component<IServicePageProps, IServicePageState> {
  private lastFetchOptions: any = {}

  public render() {
    const { application, user } = this.props
    const { list = [], totalCount, pageSize, currentPage } = application

    let columnWidth = (window.innerWidth - CONSTANTS.SIDE_BAR_WIDTH - 30 * 2) / 4 - 30
    if (columnWidth < 280) {
      columnWidth = 280
    }
    return (
      <React.Fragment>
        <Layout className="page">
          <PageTitle
            name="服务实例"
            info="服务实例大盘可视化生产所有应用实例，帮助您快速找到您负责或收藏的应用并了解实例的内部详情"
          />

          <SearchBar optionConf={{ hideMoreSearch: true }} onSubmit={this.search} />

          {list.length ? (
            <div className="page-content masonry" style={{ columnWidth }}>
              {list.map((app: InstanceItem) => (
                <ApplicationCard
                  user={user.userName}
                  update={this.updateMetrics}
                  key={app.id}
                  data={app}
                  search={this.props.location.search}
                  onClick={this.goDetail}
                />
              ))}
            </div>
          ) : (
            <div className="noData">暂无数据</div>
          )}

          {list.length ? (
            <Pagination
              total={totalCount}
              pageSize={pageSize}
              current={currentPage}
              showTotal={this.showTotal}
              onChange={this.changePage}
            />
          ) : null}
        </Layout>
      </React.Fragment>
    )
  }

  public componentDidMount() {
    applicationService.fetchApplicationList(qsParse(this.props.location.search))
  }

  public componentWillUnmount() {
    applicationService.clearApplicationList()
  }

  public updateMetrics = (id: string, fun?: (error: any, response: any) => void) => {
    applicationService.fetchApplicationMetrics(id).then(data => {
      const temp = fun ? fun : (params: any) => params
      return temp(null, data)
    })
  }

  private goDetail = (id: string) => {
    this.props.history.push(`${this.props.location.pathname}/${id}`)
  }

  private search = (values?: any) => {
    this.lastFetchOptions = values
    applicationService.fetchApplicationList(values)
  }

  private changePage = (pageNum: number) => {
    const { appName } = this.lastFetchOptions
    this.search({ appName: appName || '', pageNum })
  }

  private showTotal = () => {
    const { application } = this.props
    return `总数: ${application.totalCount}`
  }
}

export default connect({
  application: applicationModel.index,
  user: wholeModel.login,
})(withRouter(ServicePage))
