import './style.less'

import springIcon from '@/assets/icon-spring-framework.svg'
import { HINT } from '@/util/common'
import withUnmounted from '@ishawnwang/withunmounted'
import { Col, Icon, message, Row, Tooltip } from 'antd'
import classnames from 'classnames'
import prettyBytes from 'pretty-bytes'
import React from 'react'
import Chart from './Chart'

interface IApplicationCardProps {
  data: InstanceItem
  user: string
  search: string
  onClick: (id: string) => void
  update: (id: string, fun?: (error: any, response: any) => void) => void
}

interface IApplicationCardState {
  more: boolean
  metrics: { [key: string]: ApplicationMetricsData[] }
  timer?: NodeJS.Timeout
}
class ApplicationCard extends React.Component<IApplicationCardProps, IApplicationCardState> {
  public state: IApplicationCardState = {
    metrics: {},
    more: false,
  }
  private timer?: NodeJS.Timeout
  private errorCount: number = 0
  private hasUnmounted = false

  public render() {
    const { data } = this.props
    return (
      <div
        className={classnames('application-card', 'masonry-item', data.takeOver ? '' : 'disable')}>
        <div className="meta" onClick={this.openDetail}>
          <img className="icon" src={springIcon} />
          <div className="name">{data.name}</div>
          <div className="owner">
            <span>Owner: </span>
            {data.ownerName}
            <br />
            <span>ProjectKey: </span>
            {data.projectKey}
          </div>

          <Tooltip title={data.url}>
            <div className="url">{data.url}</div>
          </Tooltip>
        </div>

        <Row className="status">
          <Col
            className="col"
            span={8}
            style={
              data.status === 'DOWN' || data.status === 'OFFLINE' ? { color: 'red' } : undefined
            }>
            <span>Status</span>
            <span className="statusValue">{data.status}</span>
          </Col>
          <Col span={8} className="col">
            <span>Group</span> {data.group}
          </Col>
          <Col span={8} className="col">
            <span>Version</span>
            <span className="version">
              {(data &&
                data.registration &&
                data.registration.metadata &&
                data.registration.metadata.VERSION) ||
                ''}
            </span>
          </Col>
        </Row>

        <div className="charts">
          <li>
            <Chart data={this.state.metrics.jvmThreadslive} color="#00FF20" />
          </li>
          {this.state.more ? (
            <React.Fragment>
              <li>
                <Chart data={this.state.metrics.jvmMemoryUsedHeap} color="#00ACFF" />
              </li>
              <li>
                <Chart data={this.state.metrics.jvmMemoryUsedNonHeap} color="#989898" />
              </li>
            </React.Fragment>
          ) : null}
        </div>

        <Icon
          style={{ color: '#C1BEBE' }}
          className="switch"
          type={this.state.more ? 'up' : 'down'}
          theme="outlined"
          onClick={this.switch}
        />
      </div>
    )
  }

  public componentDidMount = () => {
    document.addEventListener('visibilitychange', this.handleVisibilityChange)
    this.startNextTimer(true)
  }

  public componentWillUnmount = () => {
    if (this.timer) {
      clearTimeout(this.timer)
      this.refreshChart = () => null
    }
    document.removeEventListener('visibilitychange', this.handleVisibilityChange)
  }

  private openDetail = () => {
    const { data } = this.props
    if (ENV === 'production' && !data.takeOver) {
      message.info(HINT.TAKEOVER)
      return
    }
    this.props.onClick(this.props.data.id)
  }

  private mapApplicationMetricsData = (
    current: { [alias: string]: ApplicationMetricsData[] } = {},
    metrics: {
      [alias: string]: any
    } = {},
    keys: string[]
  ): { [alias: string]: ApplicationMetricsData[] } => {
    const next: { [alias: string]: ApplicationMetricsData[] } = {}
    const timestamp = new Date().getTime()
    const needFormatRawBytesFields = ['jvmMemoryUsedHeap', 'jvmMemoryUsedNonHeap']
    keys.forEach(alias => {
      next[alias] = [...(current[alias] || [])]
      if (next[alias].length === 10) {
        next[alias].shift()
      }
      const value: ApplicationMetricsData = {
        timestamp,
      }
      value[alias] = needFormatRawBytesFields.includes(alias)
        ? prettyBytes(Number(metrics[alias] || 0))
        : metrics[alias]
      next[alias].push(value)
    })
    return next
  }

  private handleVisibilityChange = () => {
    if (!this.timer) {
      this.startNextTimer()
    }
  }

  private updateMetrics = () => {
    this.props.update(this.props.data.id, this.refreshChart)
  }

  private refreshChart = (error: any, response: any) => {
    if (this.hasUnmounted) {
      return
    }
    if (!error) {
      this.errorCount = 0
      const { metrics } = response

      this.setState({
        metrics: this.mapApplicationMetricsData(this.state.metrics, metrics, [
          'jvmThreadslive',
          'jvmMemoryUsedHeap',
          'jvmMemoryUsedNonHeap',
        ]),
      })
    } else {
      this.errorCount++
    }
    this.startNextTimer()
  }

  private startNextTimer = (first = false) => {
    if (first) {
      this.updateMetrics()
    }
    this.timer =
      this.errorCount < 5 && !document.hidden
        ? setTimeout(() => this.updateMetrics(), 3000)
        : undefined
  }

  private switch = () => {
    this.setState({
      more: !this.state.more,
    })
  }
}

export default withUnmounted(ApplicationCard)
