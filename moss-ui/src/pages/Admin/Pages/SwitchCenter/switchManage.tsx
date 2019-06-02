import PageTitle from '@/components/PageTitle'
import { Button, Col, Input, Layout, Radio, Row, Select, Table } from 'antd'
import React from 'react'
import { RouteComponentProps, withRouter } from 'react-router'

class SwitchManage extends React.PureComponent<RouteComponentProps & {}> {
  public state = {}
  public render() {
    const Option = Select.Option
    const Search = Input.Search
    const columns = [
      {
        title: '名称',
        dataIndex: 'title',
        key: 'title',
      },
      {
        title: '描述',
        dataIndex: 'desc',
        key: 'desc',
      },
      {
        title: '分组',
        dataIndex: 'group',
        key: 'group',
      },
      {
        title: '当前值',
        dataIndex: 'values',
        key: 'values',
      },
      {
        title: '操作',
        dataIndex: 'action',
        key: 'action',
        render: (text: string, record: any) => (
          <div>
            <Button type="primary">推送{text}</Button>
            <Button type="primary" className="ml10 mr10">
              查看{record.id}
            </Button>
          </div>
        ),
      },
    ]
    const expandedRowRender = () => {
      const childColumns = [
        {
          title: 'IP',
          dataIndex: 'ip',
          key: 'ip',
        },
        {
          title: 'HostName',
          dataIndex: 'hostname',
          key: 'hostname',
        },
        {
          title: 'Armory State',
          dataIndex: 'armorystate',
          key: 'armorystate',
        },
        {
          title: '当前值',
          dataIndex: 'values',
          key: 'values',
        },
      ]
      const childData = [
        {
          key: 1,
          ip: '10.178.28.250',
          hostname: 'wmsinput010178089250.et2',
          armorystate: 'working_online',
          values: 'true',
        },
        {
          key: 2,
          ip: '10.178.28.250',
          hostname: 'wmsinput010178089250.pre.et2',
          armorystate: 'working_online',
          values: 'true',
        },
      ]
      return (
        <div>
          <Row className="mb15">
            <Col span={5}>
              <Input addonBefore="IP" placeholder="机器IP" />
            </Col>
            <Col span={5}>
              <Button icon="close" className="ml10" />
              <Button type="primary" icon="search" className="ml10" />
            </Col>
          </Row>
          <Table columns={childColumns} dataSource={childData} bordered={true} pagination={false} />
        </div>
      )
    }

    const data = [
      {
        key: 1,
        title: 'isOpenWholeAndSingle',
        desc: '是否开启整零灰度',
        group: 'A',
        values: 'true',
        action: '操作',
      },
      {
        key: 2,
        title: 'WholeAndSingleGrayList',
        desc: '整合零灰度列表',
        group: 'A',
        values: '[52,28]',
        action: '操作',
      },
      {
        key: 3,
        title: 'WholeAndSingleGrayList',
        desc: '整合零灰度列表',
        group: 'A',
        values: '[52,28]',
        action: '操作',
      },
    ]

    return (
      <React.Fragment>
        <Layout className="page">
          <Row>
            <Col span={24}>
              <PageTitle name="开关管理" info="管理开关" />
            </Col>
          </Row>
          <div className="bg-white padding20">
            <Row className="mb15">
              <Col span={7}>
                <span className="mr10">选择应用:</span>
                <Select defaultValue="应用1" style={{ width: 160 }}>
                  <Option value="app1">应用1</Option>
                  <Option value="app2">应用2</Option>
                </Select>
              </Col>
              <Col span={3} className="pt5">
                应用：APP1
              </Col>
              <Col span={3} className="pt5">
                开关总数：392
              </Col>
            </Row>
            <Row className="mb15">
              <Col span={6}>
                <Search
                  placeholder="请输入关键字"
                  enterButton="搜索"
                  onSearch={this.onSearchClick}
                />
              </Col>
            </Row>
            <div className="mb15">
              <Radio.Group defaultValue="a" buttonStyle="solid">
                <Radio.Button value="a">ALL</Radio.Button>
                <Radio.Button value="b">分组A</Radio.Button>
                <Radio.Button value="c">分组B</Radio.Button>
              </Radio.Group>
            </div>
            <Table columns={columns} dataSource={data} expandedRowRender={expandedRowRender} />
          </div>
        </Layout>
      </React.Fragment>
    )
  }

  private onSearchClick = (value: any) => {
    console.log(value)
  }
}

export default withRouter(SwitchManage)
