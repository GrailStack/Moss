import './style.less'

import nodeListModel from '@/models/selectNode/model'
import nodeListService from '@/models/selectNode/service'
import { connect } from '@/util/store'
import { Button, Col, Dropdown, Icon, Menu, Row } from 'antd'
import React from 'react'

class SelectNode extends React.Component<
  {
    SelectNode: {
      [index: string]: any
    }
  },
  {}
> {
  public componentDidMount() {
    this.getNodeList()
  }
  public render() {
    const data = this.props.SelectNode.data
    const menu = (
      <Menu>
        <Menu.Item>
          <a>1st menu item</a>
        </Menu.Item>
        <Menu.Item>
          <a>2nd menu item</a>
        </Menu.Item>
        <Menu.Item>
          <a>3rd menu item</a>
        </Menu.Item>
      </Menu>
    )

    return (
      <Row type="flex" align="middle" className="selectNode">
        <Col push={1} span={5}>
          <Icon type="bars" /> 选择节点
        </Col>
        <Col push={2} span={11}>
          <Dropdown overlay={menu} placement="bottomLeft">
            <Button size="small">
              {data ? data[0].serviceUrl : ' '} <i>等 {data ? data.length : 0} 项</i>
            </Button>
          </Dropdown>
        </Col>
      </Row>
    )
  }

  private getNodeList = () => {
    nodeListService.fetchApplicationNodelist()
  }
}

export default connect({ SelectNode: nodeListModel.SelectNode })(SelectNode)
