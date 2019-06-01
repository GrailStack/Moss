import '@/style/masonry.less'

import Empty from '@/components/Empty'
import PageTitle from '@/components/PageTitle'
import applicationModel from '@/models/application/model'
import applicationService from '@/models/application/service'
import { connect } from '@/util/store'
import { Col, Layout, Menu, Row, Table } from 'antd'
import React from 'react'
import { RouteComponentProps, withRouter } from 'react-router'

import AppTbs from '@/components/appTbs'

const { SubMenu } = Menu

interface IJmxProps {
  Jmx: {
    [index: string]: any
  }
}

interface IJmxState {
  attrDataSource: any[]
  opDataSource: any[]
  tableName: string
  tableDesc: string
}

class Jmx extends React.Component<RouteComponentProps<{ id: string }> & IJmxProps, IJmxState> {
  public state: IJmxState = {
    attrDataSource: [],
    opDataSource: [],
    tableName: '',
    tableDesc: '',
  }
  private columns = [
    {
      title: '名称',
      dataIndex: 'name',
      key: 'name',
    },
    {
      title: '属性',
      dataIndex: 'attribute',
      key: 'attribute',
    },
  ]

  public render() {
    const newjmx: any = this.props.Jmx.data
    return (
      <React.Fragment>
        <Row>
          <Col span={24}>
            <AppTbs MenuData={this.props} />
          </Col>
        </Row>
        <Layout className="pageJmx page">
          <Row type="flex" style={{ flexDirection: 'column', flex: 1 }}>
            <Col span={24}>
              <PageTitle name="JMX" info="分类展示应用的JMX详情信息" />
            </Col>
            {Object.keys(newjmx).length ? this.renderJMXContent(newjmx) : <Empty />}
          </Row>
        </Layout>
      </React.Fragment>
    )
  }

  public componentDidMount() {
    this.getJmx(this.props.match.params.id)
  }

  private renderJMXContent(newjmx: any) {
    return (
      <div className="jmx-page page-section">
        <div className="jmx-sidebar page-section-content">
          <p className="f12">DOMAINS</p>
          <Menu onClick={this.onGetJmxDetailClick} mode="inline" className="jmx-domains">
            {Object.keys(newjmx).map(item => (
              <SubMenu key={item} title={<div>{item}</div>}>
                {Object.keys(newjmx[item]).map(subItem => (
                  <Menu.Item key={subItem}>{subItem}</Menu.Item>
                ))}
              </SubMenu>
            ))}
          </Menu>
        </div>
        <div className="jmx-content page-section-content">
          <p className="f12">MBEANS</p>
          <div className="mbeans">
            <div className="mbeans-head">
              <div className="title">application</div>
              <div className="mbeans-tabs">
                <div>Attributes</div>
              </div>
            </div>
            <div className="mbeans-body">
              <p className="f12">{this.state.tableName}</p>
              <Table
                dataSource={this.state.attrDataSource}
                columns={this.columns}
                bordered={true}
              />
              <p className="f12">{this.state.tableDesc}</p>
            </div>
          </div>
          <div className="mbeans mb20">
            <div className="mbeans-head">
              <div className="title">application</div>
              <div className="mbeans-tabs">
                <div>Operations</div>
              </div>
            </div>
            <div className="mbeans-body">
              <p className="f12">{this.state.tableName}</p>
              <Table dataSource={this.state.opDataSource} columns={this.columns} bordered={true} />
              <p className="f12">{this.state.tableDesc}</p>
            </div>
          </div>
        </div>
      </div>
    )
  }

  private getJmx = (id: string) => {
    applicationService.fetchApplicationJmx(id)
  }

  private onGetJmxDetailClick = (e: any) => {
    const firstMenuName: string = e.keyPath[1]
    const secondMenuName: string = e.keyPath[0]
    const newData: any = this.props.Jmx.data
    const tableAttrData: any = newData[firstMenuName][secondMenuName].attr
    const tableOpData: any = newData[firstMenuName][secondMenuName].op
    const tableName: string = newData[firstMenuName][secondMenuName].class
    const tableDesc: string = newData[firstMenuName][secondMenuName].desc
    const newTableAttrData: any = []
    if (tableAttrData) {
      Object.keys(tableAttrData).map((key, index) => {
        newTableAttrData.push({
          key: key + index,
          name: key,
          attribute: JSON.stringify(tableAttrData[key]),
        })
      })
    }
    const newTableOpData: any = []
    if (tableOpData) {
      Object.keys(tableOpData).map((key, index) => {
        newTableOpData.push({
          key: key + index,
          name: key,
          attribute: JSON.stringify(tableOpData[key]),
        })
      })
    }
    this.setState({
      tableName,
      tableDesc,
      attrDataSource: newTableAttrData,
      opDataSource: newTableOpData,
    })
  }
}

export default connect({ Jmx: applicationModel.Jmx })(withRouter(Jmx))
