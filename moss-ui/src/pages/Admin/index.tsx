import './style.less'

import Header from '@/components/Header'
import Sider from '@/components/Sider'
import menuService from '@/models/menu/service'
import tabService from '@/models/tab/service'
import { EventBus, EVENTS } from '@/util/eventbus'
import { connect, wholeModel } from '@/util/store'
import { Layout } from 'antd'
import _ from 'lodash'
import React from 'react'
import { RouteComponentProps, withRouter } from 'react-router'
import Stage from './Stage'

interface IAdminProps {
  notification: INotification
  menu: MenuData[]
  login: LoginData
  tab: string[]
}

interface IAdminState {
  collapsed: boolean
}

type IMixAdminProps = IAdminProps & RouteComponentProps<{ current: string; sub: string }>

class Admin extends React.Component<IMixAdminProps, IAdminState> {
  public state: IAdminState = {
    collapsed: false,
  }

  public render() {
    const { collapsed } = this.state
    const { menu, login: user, tab } = this.props
    const { current, sub } = this.props.match.params

    return (
      <Layout>
        <Sider
          data={menu}
          current={current}
          sub={sub}
          collapsed={collapsed}
          onCollapseChange={this.onCollapseChange}
        />

        <Layout id="main">
          <Header
            user={user}
            tab={tab}
            current={current}
            collapsed={collapsed}
            onCollapseChange={this.onCollapseChange}
          />
          <Stage tab={tab} current={current} />
        </Layout>
      </Layout>
    )
  }

  public componentDidMount() {
    menuService.fetchUserMenu().then(() => {
      const { current } = this.props.match.params
      tabService.addTab({ title: current })
    })
  }

  public shouldComponentUpdate(props: IMixAdminProps) {
    const next = props.match.params.current
    const { current } = this.props.match.params

    if (next !== current) {
      setTimeout(() => {
        tabService.addTab({ title: next })
      }, 0)
    }
    return true
  }

  private onCollapseChange = () => {
    EventBus.emit(EVENTS.COLLAPSE_CHANGE, !this.state.collapsed)
    this.setState({
      collapsed: !this.state.collapsed,
    })
  }
}

export default connect(wholeModel)(withRouter(Admin))
