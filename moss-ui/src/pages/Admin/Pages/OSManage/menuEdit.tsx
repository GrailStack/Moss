import FormGenerator from '@/components/FormGenerator'
import PageTitle from '@/components/PageTitle'
import menuService from '@/models/menu/service'
import { Col, Layout, Row } from 'antd'
import React from 'react'
import { RouteComponentProps, withRouter } from 'react-router'

import data from './menuEditData'

const { fields, action, method } = data

class MenuEdit extends React.PureComponent<
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
              <PageTitle name="菜单修改" info="我是描述我是描述我是描述我是描述我是描述" />
            </Col>
          </Row>

          <FormGenerator onSubmit={this.submit} data={fields} />
        </Layout>
      </React.Fragment>
    )
  }

  public componentWillMount() {
    const editData: any = this.props.location.query
    if (editData) {
      fields[0].fields.forEach((item: any) => {
        item.option.initialValue = editData[item.fieldName]
      })
    }
  }

  private submit = (fieldValues: any) => {
    fieldValues.id = fieldValues.id
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

export default withRouter(MenuEdit)
