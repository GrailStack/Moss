import FormGenerator from '@/components/FormGenerator'
import PageTitle from '@/components/PageTitle'
import menuService from '@/models/menu/service'
import { Col, Layout, Row } from 'antd'
import React from 'react'
import { RouteComponentProps, withRouter } from 'react-router'

import data from './menuNewData'

const { fields, action, method } = data

class MenuNew extends React.Component<
  RouteComponentProps & {
    location: {
      query: {}
    }
  }
> {
  public render() {
    return (
      <React.Fragment>
        <Layout className="page position-relative">
          <Row>
            <Col span={24}>
              <PageTitle name="菜单新增" info="我是描述我是描述我是描述我是描述我是描述" />
            </Col>
          </Row>

          <FormGenerator onSubmit={this.submit} data={fields} />
          <a
            href="https://ant.design/components/icon-cn/"
            target="_blank"
            className="icon-reference-link bold"
          >
            ANT DESIGN的 icon 标识符参考地址
          </a>
        </Layout>
      </React.Fragment>
    )
  }

  public componentWillMount() {
    const newData: any = this.props.location.query
    if (newData && Object.keys(newData).length > 0) {
      fields[0].fields[0].option.initialValue = newData.id
    }
  }

  private submit = (fieldValues: any) => {
    fieldValues.parentId = fieldValues.parentId
    fieldValues.name = fieldValues.name
    fieldValues.icon = fieldValues.icon
    fieldValues.roles = fieldValues.roles
    fieldValues.sort = fieldValues.sort
    fieldValues.key = fieldValues.key
    menuService
      .fetchUserMenuAddOrUpdate(action, method, fieldValues)
      .then(() => {
        return menuService.fetchUserMenu()
      })
      .then(() => {
        this.props.history.push(`/menuManage`)
      })
  }
}

export default withRouter(MenuNew)
