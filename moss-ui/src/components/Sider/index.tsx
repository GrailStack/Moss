import './style.less'

import logo from '@/assets/logo.png'
import { connect, wholeModel } from '@/util/store'
import { Icon, Layout, Menu, Select } from 'antd'
import React from 'react'
import { Link } from 'react-router-dom'

const { SubMenu, Item } = Menu

interface IPageSiderProps {
  data: MenuData[]
  current: string
  sub: string
  collapsed: boolean
  onCollapseChange: () => void
  globalConf: GlobalConf
}

interface IPageSiderState {
  openKeys: string[]
  selectedKeys: string[]
}

class PageSider extends React.PureComponent<IPageSiderProps, IPageSiderState> {
  public static getDerivedStateFromProps(nextProps: any) {
    const { current, sub, data } = nextProps
    const key = sub ? `${current}/${sub}` : current
    const openKeys = PageSider.findOpenKeys(key, data)
    return {
      openKeys,
      selectedKeys: [key],
    }
  }

  private static findOpenKeys = (key: string, data: MenuData[]) => {
    const openKeys: string[] = []
    for (const temp of data) {
      if (key === temp.key) {
        openKeys.push(temp.key)
      } else if (temp.subMenu) {
        for (const subTemp of temp.subMenu) {
          if (subTemp.key === key) {
            openKeys.push(temp.key)
            break
          }
        }
      }
    }
    return openKeys
  }

  public state = {
    openKeys: [],
    selectedKeys: [],
  }

  public render() {
    const { selectedKeys, openKeys } = this.state
    const { onCollapseChange, globalConf, collapsed = false } = this.props
    return (
      <Layout.Sider
        id="sider"
        theme="dark"
        collapsible={true}
        collapsed={collapsed}
        onCollapse={onCollapseChange}
        width={256}>
        <div className="logo" id="logo">
          <a href="/" className="logo-container">
            <img src={logo} alt="Moss-莫斯服务治理平台" />
            <div className="title-container">
              <h1>Moss</h1>
              {globalConf && Array.isArray(globalConf.registerCenter) && (
                <Select
                  key={new Date().getTime()}
                  className="source-select"
                  size="small"
                  suffixIcon={<Icon type="appstore" />}
                  defaultValue={localStorage.getItem('registerCenter') || ''}
                  onChange={this.onSourceChange}>
                  {globalConf.registerCenter.map((entry: GlobalConfItem) => {
                    return (
                      <Select.Option key={entry.value} value={entry.value}>
                        {entry.name}
                      </Select.Option>
                    )
                  })}
                </Select>
              )}
            </div>
          </a>
        </div>

        <Menu
          mode="inline"
          inlineCollapsed={collapsed}
          theme="dark"
          selectedKeys={selectedKeys}
          defaultOpenKeys={openKeys}>
          {this.getMenu(this.props.data)}
        </Menu>
      </Layout.Sider>
    )
  }

  private onSourceChange = (selection: string) => {
    localStorage.setItem('registerCenter', selection)
    location.reload()
  }

  private renderMenu = (menu: MenuData) =>
    menu.icon ? (
      <span>
        <Icon type={menu.icon} />
        <span>{menu.title}</span>
      </span>
    ) : (
      menu.title
    )

  private renderMenuItem = (menu: MenuData) =>
    menu.subMenu && menu.subMenu.length > 0 ? (
      <SubMenu key={menu.key} title={this.renderMenu(menu)}>
        {this.getMenu(menu.subMenu as MenuData[])}
      </SubMenu>
    ) : (
      <Item key={menu.key}>
        <Link to={`/${menu.key}`}>{this.renderMenu(menu)}</Link>
      </Item>
    )

  private getMenu = (list: MenuData[]): React.ReactNode[] =>
    list.map(menu => this.renderMenuItem(menu))
}

export default connect({ globalConf: wholeModel.globalConf })(PageSider)
