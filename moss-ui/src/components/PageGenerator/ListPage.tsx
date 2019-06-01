import PageTitle from '@/components/PageTitle'
import { checkDependencies, compileTemplateLiterals } from '@/components/util'
import { qsParse, qsStringify } from '@/util'
import { Button, Layout, message, Table } from 'antd'
import React from 'react'
import { RouteComponentProps } from 'react-router'
import { Link } from 'react-router-dom'

import ControllerBar from './ControllerBar'
import SearchBar from './SearchBar'

interface IProps extends RouteComponentProps, PageConstructorData {
  rowKey?: any
  listData: any
  service: any
  columns: any
}
interface IState {
  search: { [key: string]: any }
  sync: boolean
}
class ListPage extends React.Component<IProps, IState> {
  public static getDerivedStateFromProps(props: IProps, state: IState) {
    const { location } = props
    const search = qsParse(location.search)

    return {
      ...state,
      search,
    }
  }

  public state = {
    sync: true,
    search: {},
  }
  private syncTimer: any = 0
  private columns?: PageConstructorData['columns']

  constructor(props: IProps) {
    super(props)

    if (props.columns) {
      this.columns = this.initialColumns(props.columns)
    }
  }

  public render() {
    const { listData = {}, pageTitle, rowKey, searchBar, controller, noExport } = this.props
    const search = this.getPureSearch()

    return (
      <Layout className="page">
        {pageTitle ? <PageTitle name={pageTitle} /> : null}

        {searchBar ? (
          <SearchBar
            data={searchBar}
            onSubmit={this.search}
            extend={
              <React.Fragment>
                {noExport || !searchBar.fields ? null : (
                  <Button
                    type="default"
                    icon="cloud-download"
                    disabled={!Object.keys(search).length || !listData.total}
                    onClick={this.download}
                    title="请先查询再导出"
                  >
                    导出
                  </Button>
                )}
                <Button disabled={!this.state.sync} type="default" icon="sync" onClick={this.sync}>
                  刷新
                </Button>
              </React.Fragment>
            }
          />
        ) : null}

        {controller ? <ControllerBar data={controller} /> : null}

        <Table
          rowKey={rowKey || 'id'}
          className="page-section"
          columns={this.columns}
          size="small"
          dataSource={listData.list}
          pagination={{
            ...listData,
            pageSize: listData.pageSize,
            current: listData.pageNum,
            size: 'small',
            showQuickJumper: true,
            onChange: this.changePage,
          }}
          scroll={{ x: '100%' }}
        />
      </Layout>
    )
  }

  public componentDidMount() {
    this.props.service.index(this.state.search)
  }

  public shouldComponentUpdate(props: IProps, state: any) {
    const searchChange = this.props.location.search !== props.location.search
    const pathnameChange = this.props.location.pathname !== props.location.pathname

    if (searchChange && !pathnameChange) {
      this.props.service.index(state.search)
    }
    return this.props.listData !== props.listData || this.state !== state
  }

  public componentWillUnmount() {
    if (this.syncTimer) {
      clearTimeout(this.syncTimer)
    }
  }

  private initialColumns = (columns: PageConstructorData['columns']) =>
    columns.map(column => {
      if (column.operates) {
        return {
          ...column,
          render: this.renderOperates(column.operates),
        }
      } else {
        return column
      }
    })

  // [TODO] table 组件 hover 时会反复执行 render 函数，
  // 需要做一些优化
  private renderOperates = (operates: ColumnOperate | ColumnOperate[]) => (
    // @ts-ignore
    state: string,
    record: any
  ) =>
    Array.isArray(operates) ? (
      <React.Fragment
        children={operates.map(operate => {
          return this.renderOperate(operate, record)
        })}
      />
    ) : (
      this.renderOperate(operates, record)
    )

  private renderOperate = (operate: ColumnOperate, record: any) => {
    if (operate.dependencies && !checkDependencies(record, operate.dependencies)) {
      return null
    }

    const { linkTo, doAction, title } = operate
    const children = compileTemplateLiterals(title, record)

    if (linkTo) {
      const target = compileTemplateLiterals(linkTo as string, record)
      return target ? <Link to={target} children={children} /> : children
    } else if (doAction) {
      const { action, params, disabled } = doAction
      const handle = () => {
        const actionParams = { ...params, ...record }

        // @ts-ignore
        if (this[action]) {
          // @ts-ignore
          this[action](actionParams)
        } else {
          this.doAction(action, actionParams)
        }
      }
      return (
        <Button
          size="small"
          disabled={
            disabled ? (compileTemplateLiterals(disabled, record) === 'true' ? true : false) : false
          }
          onClick={handle}
          children={children}
        />
      )
    } else {
      return children
    }
  }

  private sync = () => {
    if (!this.state.sync) {
      return
    }

    this.setState({
      sync: false,
    })

    this.syncTimer = setTimeout(() => {
      this.setState({
        sync: true,
      })
    }, 5000)
    this.props.service.index(this.state.search)
  }

  private doAction = (action: string, params: any) => {
    this.props.service[action](params)
  }

  private search = (search?: any, clearPageNumber?: boolean) => {
    const nextQs = {
      ...this.state.search,
      ...search,
    }

    if (clearPageNumber) {
      delete nextQs.pageNum
    }

    const qs = qsStringify(nextQs)
    this.props.history.push(`${this.props.location.pathname}${qs ? `?${qs}` : ''}`)
  }

  private changePage = (pageNum: number) => {
    this.search({ pageNum })
  }

  private getPureSearch() {
    const search: { [key: string]: any } = { ...this.state.search }
    delete search.pageNum
    delete search.pageSize
    return search
  }

  private download = () => {
    const { listData = {}, service } = this.props
    const search = this.getPureSearch()
    const pageSize = listData.total || 0

    if (!Object.keys(search).length) {
      message.error('请先查询再导出')
    } else if (!pageSize || pageSize >= 50000) {
      message.error('导出数量必须小于50000条')
    } else {
      service.download({ ...search, pageSize })
    }
  }
}

export default ListPage
