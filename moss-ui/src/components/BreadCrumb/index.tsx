import './style.less'

import { ROUTER_MAP } from '@/util/common'
import { Breadcrumb } from 'antd'
import { UnregisterCallback } from 'history'
import React from 'react'
import { RouteComponentProps, withRouter } from 'react-router'
import { Link } from 'react-router-dom'

interface IBreadCrumbTabsState {
  pathItems: any
}

class BreadCrumbTabs extends React.Component<RouteComponentProps & {}, IBreadCrumbTabsState> {
  public state: IBreadCrumbTabsState = {
    pathItems: {},
  }
  private unlisten!: UnregisterCallback

  public componentDidMount() {
    const { history, location } = this.props
    const that = this // tslint:disable-line
    this.unlisten = history.listen(loc => {
      that.updatePathItem(loc)
    })
    this.updatePathItem(location)
  }

  public componentWillUnmount() {
    this.unlisten()
  }

  public render() {
    const { pathItems } = this.state

    return (
      <div className="headAll">
        <Breadcrumb>
          {Object.keys(pathItems).map((item: string, index: number) => {
            return (
              <Breadcrumb.Item key={index}>
                <Link to={item}>{pathItems[item]}</Link>
              </Breadcrumb.Item>
            )
          })}
        </Breadcrumb>
      </div>
    )
  }

  private updatePathItem(location: any) {
    this.setState({
      pathItems: this.getPathItems(location),
    })
  }

  private getPathItems = (location: any) => {
    const startSlashReg = new RegExp('^/')

    let rawPath = location.pathname
    if (startSlashReg.test(rawPath)) {
      // location.pathname will always start with '/',remove it to create BreadCrumb
      rawPath = rawPath.slice(1)
    }
    const pathArray: any = rawPath.split('/')
    pathArray.forEach((item: string, index: number, currentPathArray: any) => {
      const formattedPath = index === 0 ? `/${item}` : `${currentPathArray[index - 1]}/${item}`
      currentPathArray[index] = formattedPath
    })

    const pathItems: any = {}
    pathArray.map((item: string) => {
      return (pathItems[item] = this.findRouterNameByPath(item))
    })

    return pathItems
  }

  private findRouterNameByPath = (path: string) => {
    let name = ''
    for (const [k, v] of ROUTER_MAP) {
      if (k.test(path)) {
        name = v
        break
      }
    }
    return name
  }
}

export default withRouter(BreadCrumbTabs)
