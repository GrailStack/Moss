import './style.less'

import BreadCrumb from '@/components/BreadCrumb'
import { wholeModel } from '@/util/store'
import { Avatar, Badge, Dropdown, Icon, Layout, Menu } from 'antd'
import React from 'react'
import { RouteComponentProps, withRouter } from 'react-router'

const { Header } = Layout
const { Item, SubMenu } = Menu

interface IProps {
  user: LoginData
  tab: string[]
  collapsed: boolean
  current: string
  onCollapseChange: () => void
}

const HaloHeader = React.memo((props: IProps & RouteComponentProps) => {
  const { collapsed, onCollapseChange } = props
  const { userName } = props.user
  const hostname =
    window.location.hostname === 'localhost'
      ? '本地'
      : window.location.hostname.search('admin.dev') !== -1
      ? '开发'
      : window.location.hostname.search('admin.test') !== -1
      ? '测试'
      : '生产'

  const logOut = () => {
    wholeModel.login(null)
    props.history.push('/login')
  }

  const set = () => {
    wholeModel.login(null)
    props.history.push('/login')
  }

  const userMenu = (
    <Menu>
      {/* <Item onClick={logOut}>
        <Icon type="user" /> 个人中心
      </Item>
      <Item onClick={set}>
      <Icon type="setting" />账户设置
      </Item> */}
      <Item onClick={logOut}>
      <Icon type="logout" /> 退出登录
      </Item>
    </Menu>
  )

  return (
    <Header id="header">
      <div className="tab-container">
        <span className="collapse-icon-container" onClick={onCollapseChange}>
          <Icon type={collapsed ? 'menu-unfold' : 'menu-fold'} />
        </span>
        <BreadCrumb />
      </div>
      <Menu theme="light" mode="horizontal" selectable={false}>
        <Item className="env">当前环境：{hostname}</Item>
        <Item>
          <Badge dot={'' ? true : false}>
            <Icon type="notification" />
          </Badge>
        </Item>
        <Item className="avatar-item">
          <Dropdown overlay={userMenu}>
            
            <Avatar src="https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png" />

          </Dropdown>
        </Item>
        <SubMenu className="user-name" title={name}>
          <Item>
            <a href="/logout">退出</a>
          </Item>
        </SubMenu>
      </Menu>
    </Header>
  )
})

export default withRouter(HaloHeader)
