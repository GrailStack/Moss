import '@/style/masonry.less'

import PageTitle from '@/components/PageTitle'
import applicationModel from '@/models/application/model'
import applicationService from '@/models/application/service'
import { CONSTANTS } from '@/util/common'
import { connect } from '@/util/store'
import { Button, Col, Icon, Layout, Row, Select } from 'antd'
import Highlighter from 'react-highlight-words'

import React from 'react'
import { RouteComponentProps } from 'react-router'

import AppTbs from '@/components/appTbs'

const Option = Select.Option

interface ILogDelProps {
  LogDel: {
    [index: string]: any
  }
}

interface ILogDelState {
  defaultValue: string,
  selectValue: string,
  logRequestOffset: number,
  fileTypes: []
}
interface ILogfileType {
  names: [];
}

class LogDel extends React.Component<
  RouteComponentProps<{ id: string }> & ILogDelProps,
  ILogDelState
> {
  public state: ILogDelState = {
    defaultValue: '',
    selectValue: '',
    logRequestOffset: 0,
    fileTypes: []
  }

  private timer?: NodeJS.Timer
  public componentDidMount() {
    applicationService
      .fetchApplicationLogfileType(this.props.match.params.id).then(response => {
        const fileType: ILogfileType = (response.data);
        this.setState({
          fileTypes: fileType.names
        });
      });
  }

  public componentWillUnmount() {
    if (this.timer) {
      clearTimeout(this.timer)
    }
  }

  public render() {
    const { Content } = Layout
    let LogDelData = this.props.LogDel.data
    if (typeof LogDelData !== 'string' || !LogDelData.length) {
      LogDelData = '暂无数据'
    }

    return (
      <React.Fragment>
        <Row>
          <Col span={24}>
            <AppTbs MenuData={this.props} />
          </Col>
        </Row>
        <Layout className="page logDel">
          <Row>
            <Col span={24}>
              <PageTitle
                name="日志"
                info="实时获取应用的日志"
                titleExtra={
                  <Select defaultValue={this.state.defaultValue} style={{ width: 160 }} onChange={this.handleChange}>
                    {this.state.fileTypes.map(type => <Option key={type} value={type}>查看{type}日志</Option>)}
                  </Select>
                }
                rightPanelExtra={
                  <Button
                    type="primary"
                    onClick={this.getLogDel(this.props.match.params.id, this.state.selectValue)}
                    style={{ float: 'right', marginTop: '5px', marginLeft: '5px' }}>
                    <Icon type="loading-3-quarters" /> 刷新
                </Button>
                }
              />
            </Col>
          </Row>

          <Content>
            <Highlighter
              className="log-code"
              highlightStyle={{ color: '#68BD45' }}
              searchWords={CONSTANTS.LOG_HIGHLIGHT_REGEX}
              textToHighlight={LogDelData}
              style={{
                background: 'rgba(0,0,0,0.85)',
                color: '#f7f9fb',
                padding: 50,
                whiteSpace: 'nowrap',
                overflow: 'scroll',
                maxWidth: CONSTANTS.CHART_CONTENT_WIDTH,
              }}
            />
            ,
          </Content>
        </Layout>
      </React.Fragment>
    )
  }

  public handleChange = (value: any) => {
    this.getLogDel(this.props.match.params.id, value)()
    this.setState({
      selectValue: value,
    })
  }

  private getLogDel = (id: any, value: string) => () => {
    const { logRequestOffset } = this.state
    return applicationService
      .fetchApplicationModifyLogDel(id, value, logRequestOffset)
      .then(resp => {
        this.setState({
          logRequestOffset: logRequestOffset + resp.request.response.length,
        })
      })
  }
}

export default connect({ LogDel: applicationModel.LogDel })(LogDel)
