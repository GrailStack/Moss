import PageTitle from '@/components/PageTitle'
import menuService from '@/models/menu/service'
import { connect, wholeModel } from '@/util/store'
import { Button, Card, Col, Icon, Layout, Popconfirm, Row, Table } from 'antd'
import React from 'react'
import { RouteComponentProps, withRouter } from 'react-router'

interface IProps {
  menu: {
    [index: string]: any
  }
  history: {}
}

interface IState {
  menuData: any
}
class MenuManage extends React.PureComponent<RouteComponentProps & IProps, IState> {
  public state = {
    menuData: [],
  }

  public render() {
    const columns = [
      {
        title: '名称',
        dataIndex: 'title',
        key: 'title',
        width: '15%',
      },
      {
        title: 'URL',
        dataIndex: 'url',
        key: 'src',
        width: '15%',
      },
      {
        title: 'KEY',
        dataIndex: 'key',
        key: 'key',
      },
      {
        title: '权限',
        dataIndex: 'roles',
        key: 'roles',
      },
      {
        title: '顺序',
        dataIndex: 'sort',
        key: 'sort',
      },
      {
        title: '图标',
        dataIndex: 'icon',
        key: 'icon',
        render: (text: string) => (
          <span>
            <Icon type={text} /> {text}
          </span>
        ),
      },
      {
        title: '操作',
        dataIndex: 'action',
        key: 'action',
        width: '30%',
        render: (text: string, record: any) => (
          <span>
            <Button size="small" icon="edit" onClick={this.onMenuEditClick.bind(this, record)}>
              修改{text}
            </Button>
            <Button
              type="primary"
              icon="plus"
              size="small"
              className="ml10 mr10"
              onClick={this.onMenuCreatClick.bind(this, record.id)}>
              添加子栏目
            </Button>
            <Popconfirm
              title="确定删除菜单吗?"
              onConfirm={this.onMenuDeleteClick.bind(this, record.id)}>
              <Button type="danger" icon="delete" size="small">
                删除
              </Button>
            </Popconfirm>
          </span>
        ),
      },
    ]

    return (
      <React.Fragment>
        <Layout className="page">
          <Row>
            <Col span={24}>
              <PageTitle name="菜单管理" info="管理服务治理平台的菜单" />
            </Col>
            {/* <Col span={12}>
              <SelectNode />
            </Col> */}
          </Row>
          <Card
            title="菜单管理"
            extra={
              <Button type="primary" onClick={this.onMenuCreatClick.bind(this, '0')}>
                添加主菜单
              </Button>
            }>
            <Table
              columns={columns}
              dataSource={this.state.menuData}
              bordered={true}
              defaultExpandAllRows={true}
              pagination={false}
            />
          </Card>
        </Layout>
      </React.Fragment>
    )
  }

  public componentDidMount() {
    this.getMenuAllFetch()
  }

  public getMenuAllFetch() {
    menuService.fetchUserMenuAll().then((data: any) => {
      const menuData: any = data
      const subkey = 'children'
      function parseJson(arr: any) {
        arr = arr.slice()
        function toParse(subArr: any) {
          subArr.forEach((item: any) => {
            if (item.subMenu && Array.isArray(item.subMenu) && item.subMenu.length > 0) {
              item[subkey] = item.subMenu
              toParse(item[subkey])
            }
            delete item.subMenu
          })
          return subArr
        }
        return toParse(arr)
      }

      this.setState({
        menuData: parseJson(menuData),
      })
    })
  }

  public onMenuCreatClick = (id: string) => {
    const data: any = { id }
    const path = {
      pathname: '/menuManage/new',
      query: data,
    }
    this.props.history.push(path)
  }

  public onMenuEditClick = (record: any) => {
    const path = {
      pathname: '/menuManage/edit',
      query: record,
    }
    this.props.history.push(path)
  }

  public onMenuDeleteClick = (id: string) => {
    menuService.fetchUserMenuDelete(id).then(() => {
      this.getMenuAllFetch()
      return menuService.fetchUserMenu()
    })
  }
}

export default connect(wholeModel)(withRouter(MenuManage))
