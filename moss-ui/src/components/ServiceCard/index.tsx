import springIcon from '@/assets/icon-spring-framework.svg'
import springIconWarn from '@/assets/warning.gif'
import applicationService from '@/models/application/service'
import { HINT } from '@/util/common'
import { Col, Icon, message, Rate, Row } from 'antd'
import classnames from 'classnames'

import React from 'react'
import './style.less'

interface IServiceCardProps {
  user: string
  data: ServiceItem
  reFetchDatas: () => void
  onClick: (id: string) => void
}

interface IServiceCardState {
  more: boolean
}

class ServiceCard extends React.Component<IServiceCardProps, IServiceCardState> {
  public state: IServiceCardState = {
    more: false,
  }

  public render() {
    const { data } = this.props
    const STATUS = data.instances
      .filter(ins => {
        return ins.statusInfo && ins.statusInfo.status
      })
      .reduce(
        (stats, currentInstance) => {
          const stat = currentInstance.statusInfo.status.toLowerCase()
          stats[stat] += 1
          return stats
        },
        {
          up: 0,
          down: 0,
          offline: 0,
        } as {
          [key: string]: number
        }
      )
    return (
      <div className={classnames('service-card', 'masonry-item', data.takeOver ? '' : 'disable')}>
        <Icon
          type={data.attachType !== 2 ? 'plus-circle' : 'minus-circle'}
          title={data.attachType !== 2 ? '点我收藏' : '点我取消收藏'}
          className={classnames('icon-card', data.twinkle ? 'red' : '')}
          onClick={this.isNeedCollect(data.name, data.attachType)}
        />
        <div className="meta" onClick={this.openDetail}>
          <img className="icon" src={data.twinkle ? springIconWarn : springIcon} />
          <div className="meta-infos">
            <div className="name">{data.name}</div>
            <div className="instance-count">实例数: {data.instanceNum}</div>
            <Rate className="rate" value={data.starsNum} disabled={true} />
          </div>
        </div>

        <Row className="status">
          <Col
            className="col"
            span={8}
            style={
              data.status === 'DOWN' || data.status === 'OFFLINE' ? { color: 'red' } : undefined
            }>
            <span>Status</span>
            <span className="status-value">{data.status}</span>
          </Col>
          <Col span={8} className="col">
            <span>Project</span>
            <span>{data.projectKey}</span>
          </Col>
          <Col span={8} className="col">
            <span>Owner</span>
            <span className="owner-value">{data.ownerName}</span>
          </Col>
        </Row>
        <Row className="status bottom">
          <Col span={8} className="col">
            <span>UP</span>
            <span>{STATUS.up}</span>
          </Col>
          <Col span={8} className="col">
            <span>DOWN</span>
            <span>{STATUS.down}</span>
          </Col>
          <Col span={8} className="col">
            <span>OFFLINE</span>
            <span>{STATUS.offline}</span>
          </Col>
        </Row>

        {/* <Icon
          style={{ color: '#C1BEBE' }}
          className="switch"
          type={this.state.more ? 'up' : 'down'}
          theme="outlined"
          onClick={this.switch}
        /> */}
      </div>
    )
  }

  private openDetail = () => {
    const { data } = this.props
    if (ENV === 'production' && !data.takeOver) {
      message.info(HINT.TAKEOVER)

      return
    }
    this.props.onClick(this.props.data.name)
  }

  private isNeedCollect = (appName: string, attachType?: number) => () => {
    const { reFetchDatas } = this.props
    const isCollect = attachType === 2 ? false : true
    applicationService
      .fetchApplicationIsNeedCollect(this.props.user, isCollect, appName.toLowerCase())
      .then(() => {
        reFetchDatas()
      })
  }
}

export default ServiceCard
