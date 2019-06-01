import '@/style/masonry.less'

import { CONSTANTS } from '@/util/common'
import Empty from '@/components/Empty'
import PageTitle from '@/components/PageTitle'
import applicationModel from '@/models/application/model'
import applicationService from '@/models/application/service'
import { connect } from '@/util/store'
import { Col, Layout, Row } from 'antd'
import React from 'react'
import Highlighter from 'react-highlight-words'
import { RouteComponentProps } from 'react-router'

import AppTbs from '@/components/appTbs'

interface IGCLogProps {
  GCLog: GCLogData
}

interface IGCLogState {
  fetchingMore: boolean
  currentPage: number
}

class GCLog extends React.Component<
  RouteComponentProps<{ id: string }> & IGCLogProps,
  IGCLogState
> {
  public state: IGCLogState = {
    fetchingMore: false,
    currentPage: 1,
  }

  public componentDidMount() {
    applicationService.fetchApplicationGCLog(this.props.match.params.id)
    window.addEventListener('scroll', this.windowScroll)
  }

  public componentWillUnmount() {
    window.removeEventListener('scroll', this.windowScroll)
  }

  public render() {
    const { Content } = Layout
    const logs = this.props.GCLog.log.join('\n')

    return (
      <React.Fragment>
        <Row>
          <Col span={24}>
            <AppTbs MenuData={this.props} />
          </Col>
        </Row>
        <Layout className="page logDel">
          <Row>
            <Col>
              <PageTitle name="GC Log" info="实时获取应用的 GC Log" />
            </Col>
          </Row>

          <Content>
            {logs.length ? (
              <Highlighter
                className="log-code"
                highlightStyle={{ color: '#68BD45' }}
                searchWords={CONSTANTS.LOG_HIGHLIGHT_REGEX}
                textToHighlight={logs}
                style={{
                  background: 'rgba(0,0,0,0.85)',
                  color: '#f7f9fb',
                  padding: 50,
                  whiteSpace: 'nowrap',
                  overflow: 'scroll',
                  maxWidth: CONSTANTS.CHART_CONTENT_WIDTH,
                }}
              />
            ) : (
              <Empty />
            )}
          </Content>
        </Layout>
      </React.Fragment>
    )
  }

  private windowScroll = () => {
    const { fetchingMore, currentPage } = this.state
    const { GCLog: gclog } = this.props
    if (fetchingMore) {
      return
    }
    if (currentPage * gclog.size > gclog.total) {
      return
    }

    const scrollYPercent =
      ((document.documentElement.scrollTop + document.body.scrollTop) /
        (document.documentElement.scrollHeight - document.documentElement.clientHeight)) *
      100
    if (scrollYPercent >= 80) {
      this.setState({
        fetchingMore: true,
      })
      applicationService.fetchApplicationGCLog(this.props.match.params.id, currentPage + 1)
    }
  }
}

export default connect({ GCLog: applicationModel.GCLog })(GCLog)
