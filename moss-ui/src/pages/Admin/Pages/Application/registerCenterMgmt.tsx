import PageTitle from '@/components/PageTitle'
import SearchBar from '@/components/SearchBar'
import applicationModel from '@/models/application/model'
import applicationService from '@/models/application/service'
import { connect } from '@/util/store'
import { Button, Layout, Modal, Row, Table, Tooltip } from 'antd'
import moment from 'moment'
import React from 'react'
import { Icon } from 'antd'
import { RouteComponentProps, withRouter } from 'react-router'
import globalService from '@/models/global/service'

const confirm = Modal.confirm

const { Content } = Layout

interface IRegisterCenterProps {
  registerCenter: RegisterCenterData
}

class RegisterCenter extends React.Component<IRegisterCenterProps & RouteComponentProps, {}> {
  private lastFetchOptions: any = {}

  public componentDidMount() {
    applicationService.fetchRegisterCenters()
  }

  private instanceNumTitle = (
    <Tooltip title="注册中心唯一标识">
      Code <Icon type="question-circle" />
    </Tooltip>
  )

  public render() {
    const { registerCenter } = this.props
    const { list, currentPage, totalCount } = registerCenter

    const columns = [
      {
        title: 'id',
        dataIndex: 'id',
        key: 'id',
      },
      {
        title: '名称',
        dataIndex: 'name',
        key: 'name',
      },
      {
        title: this.instanceNumTitle,
        dataIndex: 'code',
        key: 'code',
      },
      {
        title: 'URL',
        dataIndex: 'url',
        key: 'url',
      },
      {
        title: '状态',
        dataIndex: 'status',
        key: 'status',
        render: (status: number) => {
          return status === 1 ? '运行' : '停用'
        },
      },
      {
        title: '创建时间',
        dataIndex: 'gmtCreate',
        key: 'gmtCreate',
        render: (date: number) => {
          return moment(date).format('YYYY-MM-DD HH:mm:ss')
        },
      },
      {
        title: '修改时间',
        dataIndex: 'gmtModified',
        key: 'gmtModified',
        render: (date: number) => {
          return moment(date).format('YYYY-MM-DD HH:mm:ss')
        },
      },
      {
        title: '是否删除',
        dataIndex: ' isDeleted',
        key: 'isDeleted',
        render: (isDeleted: boolean) => {
          return isDeleted ? '是' : '否'
        },
      },
      {
        title: '操作',
        width: 170,
        key: 'operation',
        render: (record: any) => (
          <Row>
            <Button style={{ marginRight: 8 }} type="primary" onClick={this.onRowClick(record)}>
              修改
            </Button>
            <Button type="danger" onClick={this.onDeleteRegisterCenter(record)}>
              删除
            </Button>
          </Row>
        ),
      },
    ]

    return (
      <Layout className="page page-project">
        <Content>
          <PageTitle name="注册中心管理" info="动态在线增加或删减注册中心" />
          <Row>
            <Row>
              <SearchBar onSubmit={this.onSearch} optionConf={{ hideMoreSearch: true }} />
            </Row>
          </Row>
          <Row className="project-container">
            <Row className="create-project">
              <Button type="primary" onClick={this.onCreateRegisterCenter}>
                <Icon type="plus" />
                增加注册中心
              </Button>
            </Row>
            <Table
              onRow={this.onRowClick}
              className="registerCenter-table"
              dataSource={list}
              columns={columns}
              pagination={{
                total: totalCount,
                pageSize: 10,
                current: currentPage,
                onChange: this.changePage,
              }}
            />
          </Row>
        </Content>
      </Layout>
    )
  }

  private onDeleteRegisterCenter = (rc: RegisterCenterModel) => (e: React.MouseEvent) => {
    e.stopPropagation()
    confirm({
      title: '删除',
      content: '确认删除注册中心 ?',
      onOk() {
        applicationService.deleteRegisterCenter(rc.id).then(() => {
          applicationService.fetchRegisterCenters()
          globalService.fetchGlobalConf()
        })
      },
    })
  }

  private onRowClick = (e: RegisterCenterModel) => () => {
    const { history } = this.props
    history.push(`${location.pathname}/create`, { initialValue: e, type: 'EDIT' })
  }

  private onSearch = (e: any) => {
    this.lastFetchOptions = e
    const code = e.appName
    applicationService.fetchRegisterCenters(code, 1)
  }

  private onCreateRegisterCenter = () => {
    const { history } = this.props
    history.push(`${location.pathname}/create`)
  }

  private changePage = (page: number) => {
    const { appName } = this.lastFetchOptions
    applicationService.fetchRegisterCenters(appName || '', page)
  }
}

export default connect({ registerCenter: applicationModel.RegisterCenter })(
  withRouter(RegisterCenter)
)
