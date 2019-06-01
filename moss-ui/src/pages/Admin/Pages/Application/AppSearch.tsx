import '@/style/masonry.less'

import PageTitle from '@/components/PageTitle'
import applicationModel from '@/models/application/model'
import applicationService from '@/models/application/service'
import {qsParse} from '@/util'
import {connect} from '@/util/store'
import {getLocalTime} from '@/util/timestamp'
import {Button, Col, Form, Input, Layout, Row, Select, Table, Tooltip} from 'antd'
import {FormComponentProps} from 'antd/lib/form/Form'
import React from 'react'
import {RouteComponentProps, withRouter} from 'react-router'

const {Content} = Layout
const {Option} = Select

interface IAppSearchProps {
  AppSearch: {
    [index: string]: any
  }
}

interface IAppSearchState {
  lastSearch: any
}

class AppSearch extends React.PureComponent<RouteComponentProps & FormComponentProps & IAppSearchProps,
  IAppSearchState> {
  public state: IAppSearchState = {
    lastSearch: {},
  }
  public columns = [
    {
      title: 'ID',
      dataIndex: 'id',
      key: 'id',
      width: 50,
      fixed: 'left',
    },
    {
      title: '应用名称',
      dataIndex: 'springApplicationName',
      key: 'springApplicationName',
      width: 200,
      fixed: 'left',
    },
    {
      title: '负责人',
      dataIndex: 'ownerName',
      key: 'ownerName',
      width: 100,
      render: (ownerName: string, data: any) => {
        return (
          <Tooltip title={`mailto:${data.ownerId}@qq.com`}>
            <a href={`mailto:${data.ownerId}@qq.com`}>{ownerName}</a>
          </Tooltip>
        )
      },
    },
    {
      title: '归属项目',
      dataIndex: 'projectName',
      key: 'projectName',
    },
    {
      title: '仓库',
      dataIndex: 'repoUrl',
      key: 'repoUrl',
    },
    {
      title: '创建时间',
      dataIndex: 'gmtCreate',
      key: 'gmtCreate',
      render: (gmtCreate: any) => getLocalTime(gmtCreate),
    },
    {
      title: '更新时间',
      dataIndex: 'gmtModified',
      key: 'gmtModified',
      render: (gmtModified: any) => getLocalTime(gmtModified),
    },
    {
      title: '状态',
      key: 'status',
      dataIndex: 'status',
      render: (msg: string) => this.statusFilter(parseInt(msg, 10)),
    },
    {
      title: '操作',
      key: '1',
      fixed: 'right',
      width: 100,
      render: (record: any) =>
        this.props.location.pathname === '/appAccept' ? (
          record.isDeleted === 0 ? (
            <span>
              <Button
                type="primary"
                onClick={this.jumpDel(record)}
                style={{marginRight: '8px', marginBottom: '8px'}}>
                查看实例
              </Button>

              <Button type="primary" onClick={this.jumpModifyExample(record)}>
                修改
              </Button>
            </span>
          ) : (
            <Button type="primary" onClick={this.jumpModifyExample(record)}>
              修改
            </Button>
          )
        ) : (
          <Button type="primary" onClick={this.jumpDel(record)}>
            查看实例
          </Button>
        ),
    },
  ] as any

  public columnAccess: any = {
    title: '接入状态',
    key: 'takeOver',
    dataIndex: 'takeOver',
    render: (takeOver: number) => {
      return takeOver ? '已接入' : '未接入'
    },
  }

  public componentWillMount() {
    if (this.props.location.pathname === '/appAccept') {
      this.columns.splice(8, 0, this.columnAccess)
    }
  }

  public componentDidMount() {
    applicationService.fetchUserList()
    applicationService.fetchProjects();
    const paginationUrl = qsParse(this.props.location.search)
    if (!paginationUrl.pageNum) {
      paginationUrl.pageNum = 1
    }
    const nextQs = qsParse(this.props.location.search.slice(0))
    this.getExampleList(
      nextQs.appName ? nextQs.appName : '',
      nextQs.projectName ? nextQs.projectName : '',
      nextQs.status ? nextQs.status : '',
      paginationUrl.pageNum
    )
    applicationService.fetchApplicationSearchToobar()
  }

  public render() {
    const {Item} = Form
    const {getFieldDecorator} = this.props.form
    const {list, currentPage, totalCount} = this.props.AppSearch.AppSearchList

    const {projectNameOptions, statusOptions} = this.getOptions()

    return (
      <React.Fragment>
        <Layout className="page AppSearch">
          <Row>
            <Col span={24}>
              <PageTitle
                name="应用列表"
                info={
                  this.props.location.pathname === '/appAccept'
                    ? '规范化，统一化，信息化纳管每个BU的应用进行服务治理'
                    : '全方位的了解微服务应用的信息，包括归属项目，负责人以及具体某个应用实例的内部运行状态和信息'
                }
              />
            </Col>
          </Row>
          <Content>
            <Form layout="inline" className="ant-advanced-search-form" onSubmit={this.search}>
              <Item>{getFieldDecorator('appName')(<Input placeholder="请输入关键字"/>)}</Item>
              <Item>
                {getFieldDecorator('projectName')(
                  <Select style={{width: '150px'}} placeholder="项目 Key" showSearch={true}>
                    {projectNameOptions}
                  </Select>
                )}
              </Item>
              <Item>
                {getFieldDecorator('status')(
                  <Select placeholder="应用状态" style={{width: '150px'}}>
                    {statusOptions}
                  </Select>
                )}
              </Item>

              <Item>
                {getFieldDecorator('takeOver')(
                  <Select placeholder="接入状态" style={{width: '150px'}}>
                    <Option value={1}>已接入</Option>
                    <Option value={0}>未接入</Option>
                  </Select>
                )}
              </Item>

              <Item>
                <Button type="primary" icon="search" htmlType="submit">
                  搜索
                </Button>
              </Item>
              <Button
                type="primary"
                className={
                  this.props.location.pathname === '/appAccept' ? 'btnRefresh' : 'btnRefreshHide'
                }
                onClick={this.fetchApplicationsynch}>
                同步
              </Button>
            </Form>
            <Row style={{"backgroundColor":"#ffff"}}>
              <Row className={"tableTool"}>
                <Button type="primary" onClick={this.onCreateApp} className={'btnAppAdd'}>
                  创建
                </Button>
              </Row>
              <Table
                pagination={{
                  pageSize: 10,
                  total: totalCount,
                  current: currentPage,
                  showQuickJumper: true,
                  onChange: this.changePage,
                }}
                columns={this.columns}
                dataSource={list}
                bordered={true}
                rowKey="id"
                scroll={{x: 1500}}
              />
            </Row>
          </Content>
        </Layout>
      </React.Fragment>
    )
  }

  public jumpDel = (record: any) => () => {
    this.props.history.push(`list?appName=${record.appId}&pageSize=10&pageNum=1`)
  }

  public jumpModifyExample = (record: any) => () => {
    const {location} = this.props
    record.takeOver = record.takeOver ? '1' : "0";
    record.status = record.status === true ? "1" : (record.status === false ? "0" : '' + record.status)
    console.log(record)
    applicationModel.modifyExample(record)
    this.props.history.push(`${location.pathname}/modifyExample`)
  }

  private changePage = (page: number) => {
    const {lastSearch} = this.state
    this.getExampleList(
      lastSearch.appName ? lastSearch.appName : '',
      lastSearch.projectName ? lastSearch.projectName : '',
      lastSearch.status ? lastSearch.status : '',
      page
    )
  }

  private getExampleList = (
    name: string,
    projectName: string,
    status: string,
    pageNo: number,
    pageSize: number = 10,
    takeOver: number = -1
  ) => {
    const params = this.props.location.pathname
    const isAccept = params === '/appAccept' ? false : true
    applicationService.fetchApplicationExampleList(
      name,
      projectName,
      status,
      pageNo,
      pageSize,
      isAccept,
      takeOver
    )
  }

  private search = (evt: React.FormEvent) => {
    evt.preventDefault()
    this.props.form.validateFields(
      (err: null | { [index: string]: any }, values: { [index: string]: any }) => {
        if (!err) {
          this.setState({
            lastSearch: values,
          })

          this.getExampleList(
            values.appName ? values.appName : '',
            values.projectName ? values.projectName : '',
            values.status ? values.status : '',
            values.pageNum ? values.pageNum : 1,
            10,
            values.takeOver
          )
        }
      }
    )
  }

  private statusFilter = (value: any) => {
    let valueItem = '创建'
    if (value === 0) {
      valueItem = '创建'
    }
    if (value === 1) {
      valueItem = '开发中'
    }
    if (value === 2) {
      valueItem = '运行中'
    }
    if (value === 3) {
      valueItem = '已下线'
    }
    return valueItem
  }

  private getOptions = () => {
    let projectName = []
    const projectNameOptions = []
    let status = []
    const statusOptions = []
    if (this.props.AppSearch.SearchToobar.length > 0) {
      projectName = this.props.AppSearch.SearchToobar[0].options
      for (const projectName1 of projectName) {
        projectNameOptions.push(
          <Option key={projectName1.text} value={projectName1.value}>
            {projectName1.text}
          </Option>
        )
      }
      status = this.props.AppSearch.SearchToobar[1].options
      for (const status1 of status) {
        statusOptions.push(
          <Option key={status1.text} value={status1.value}>
            {status1.text}
          </Option>
        )
      }
    }

    return {projectNameOptions, statusOptions, projectName, status}
  }

  private fetchApplicationsynch = () => {
    const {lastSearch} = this.state
    const {currentPage} = this.props.AppSearch.AppSearchList
    applicationService.fetchApplicationsynch().then(() => {
      this.getExampleList(
        lastSearch.appName ? lastSearch.appName : '',
        lastSearch.projectName ? lastSearch.projectName : '',
        lastSearch.status ? lastSearch.status : '',
        currentPage
      )
    })
  }

  private onCreateApp = () => {
    const {history} = this.props
    applicationModel.modifyExample({id: null})
    history.push(`${location.pathname}/createApp`)
  }
}

const WrappedLogin = Form.create()(AppSearch)
export default connect({AppSearch: applicationModel.AppSearch})(withRouter(WrappedLogin))
