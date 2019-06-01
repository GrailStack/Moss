import PageTitle from '@/components/PageTitle'
import SearchBar from '@/components/SearchBar'
import applicationModel from '@/models/application/model'
import applicationService from '@/models/application/service'
import { connect } from '@/util/store'
import { Button, Layout, Modal, Row, Table, Tooltip } from 'antd'
import moment from 'moment'
import React from 'react'
import { RouteComponentProps, withRouter } from 'react-router'

const confirm = Modal.confirm

const { Content } = Layout

interface IProjectProps {
  project: ProjectData
}

class Project extends React.Component<IProjectProps & RouteComponentProps, {}> {
  private lastFetchOptions: any = {}

  public componentDidMount() {
    applicationService.fetchProjects()
  }

  public render() {
    const { project } = this.props
    const { list, currentPage, totalCount } = project

    const columns = [
      {
        title: '项目 id',
        dataIndex: 'id',
        key: 'id',
      },
      {
        title: '项目名称',
        dataIndex: 'cname',
        key: 'cname',
      },
      {
        title: '项目 owner',
        dataIndex: 'ownerName',
        key: 'ownerName',
        render: (ownerName: string, data: any) => {
          return (
            <Tooltip title={`mailto:${data.ownerId}@qq.com`}>
              <a href={`mailto:${data.ownerId}@qq.com`}>{ownerName}</a>
            </Tooltip>
          )
        },
      },
      {
        title: '创建时间',
        dataIndex: 'gmtCreate',
        key: 'gmtCreate',
        render: (date: number) => {
          return moment(date).fromNow()
        },
      },
      {
        title: '修改时间',
        dataIndex: 'gmtModified',
        key: 'gmtModified',
        render: (date: number) => {
          return moment(date).fromNow()
        },
      },
      {
        title: '描述',
        dataIndex: 'description',
        key: 'description',
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
            <Button type="danger" onClick={this.onDeleteProject(record)}>
              删除
            </Button>
          </Row>
        ),
      },
    ]

    return (
      <Layout className="page page-project">
        <Content>
          <PageTitle name="项目列表" info="项目管理" />
          <Row>
            <SearchBar onSubmit={this.onSearch} optionConf={{ hideMoreSearch: true }} />
          </Row>
          <Row className="project-container">
            <Row className="create-project">
              <Button type="primary" onClick={this.onCreateProject}>
                创建项目
              </Button>
            </Row>
            <Table
              onRow={this.onRowClick}
              className="project-table"
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

  private onDeleteProject = (project: ProjectModel) => (e: React.MouseEvent) => {
    e.stopPropagation()
    confirm({
      title: '删除',
      content: '确认删除项目 ?',
      onOk() {
        applicationService.deleteProject(project.id).then(() => {
          applicationService.fetchProjects()
        })
      },
    })
  }

  private onRowClick = (e: ProjectModel) => () => {
    const { history } = this.props
    history.push(`${location.pathname}/createProject`, { initialValue: e, type: 'EDIT' })
  }

  private onSearch = (e: any) => {
    this.lastFetchOptions = e
    const name = e.appName
    applicationService.fetchProjects(name, 1)
  }

  private onCreateProject = () => {
    const { history } = this.props
    history.push(`${location.pathname}/createProject`)
  }

  private changePage = (page: number) => {
    const { appName } = this.lastFetchOptions
    applicationService.fetchProjects(appName || '', page)
  }
}

export default connect({ project: applicationModel.Project })(withRouter(Project))
