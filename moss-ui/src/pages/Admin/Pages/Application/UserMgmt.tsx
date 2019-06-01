import PageTitle from '@/components/PageTitle'
import SearchBar from '@/components/SearchBar'
import userService from '@/models/user/service'
import userModel from '@/models/user/model'
import { connect } from '@/util/store'
import { Button, Layout, Modal, Row, Table, Tooltip } from 'antd'
import moment from 'moment'
import React from 'react'
import { Icon } from 'antd'
import { RouteComponentProps, withRouter } from 'react-router'

const confirm = Modal.confirm

const { Content } = Layout

interface IUserProps {
  user: UserData
}

class User extends React.Component<IUserProps & RouteComponentProps, {}> {
  private lastFetchOptions: any = {}

  public componentDidMount() {
    userService.fetchUserList()
  }
  public render() {
    const { user } = this.props
    const { list, currentPage, totalCount } = user
    debugger
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
        title: '名称',
        dataIndex: 'username',
        key: 'username',
      },
      {
        title: '邮箱',
        dataIndex: 'email',
        key: 'email',
      },

      {
        title: '状态',
        dataIndex: 'status',
        key: 'status',
        render: (status: number) => {
          return status === 0 ? '启用' : '禁用'
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
            <Button type="primary" onClick={this.onRowClick(record)}>
            <Icon type="edit" />
              修改
            </Button>
            <Button  type="danger" onClick={this.onDeleteUser(record)}>
            <Icon type="delete" />
              删除
            </Button>
          </Row>
        ),
      },
    ]

    return (
      <Layout className="page page-project">
        <Content>
          <PageTitle name="用户管理" info="对服务治理平台的用户进行管理" />
          <Row>
            <Row>
              <SearchBar onSubmit={this.onSearch} optionConf={{ hideMoreSearch: true }} />
            </Row>
          </Row>
          <Row className="project-container">
            <Row className="create-project">
              <Button type="primary" onClick={this.onCreateRegisterCenter}>
              <Icon type="user-add" />
                增加用户
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

  private onDeleteUser = (rc: UserModel) => (e: React.MouseEvent) => {
    e.stopPropagation()
    confirm({
      title: '删除',
      content: '确认删除用户 ?',
      onOk() {
        userService.deleteUser(rc.id).then(() => {
         userService.fetchUserList()
        })
      },
    })
  }

  private onRowClick = (e: UserMgmtModel) => () => {
    const { history } = this.props
    history.push(`${location.pathname}/create`, { initialValue: e, type: 'EDIT' })
  }

  private onSearch = (e: any) => {
    this.lastFetchOptions = e
    const name = e.appName
    userService.fetchUserList(name, 1)
  }

  private onCreateRegisterCenter = () => {
    const { history } = this.props
    history.push(`${location.pathname}/create`)
  }

  private changePage = (page: number) => {
    const { appName } = this.lastFetchOptions
    userService.fetchUserList(appName || '', page)
  }
}
export default connect({ user: userModel.User })(withRouter(User))
