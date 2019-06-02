import './style.less'

import React from 'react'

import service from '@/models/notification/service'
import { connect, wholeModel } from '@/util/store'
import Loading from './loading'
import Notice from './notice'

class Notification extends React.Component<{
  notification: INotification
}> {
  public state = { delayHideLoading: false }
  public componentDidUpdate(prevProps: any) {
    // loading 的最短展示时间为 375ms, 防止闪一下, 体验极差
    if (prevProps.notification.loading !== this.props.notification.loading) {
      if (this.props.notification.loading) {
        this.setState({
          delayHideLoading: true,
        })
        setTimeout(() => {
          this.setState({
            delayHideLoading: false,
          })
        }, 375)
      }
    }
  }

  public render() {
    const { notices, loading } = this.props.notification
    const { delayHideLoading } = this.state
    const shouldShowLoading = loading > 0 || delayHideLoading
    return (
      <div id="notification" className={notices.length || shouldShowLoading ? 'show' : ''}>
        {notices.length ? (
          <div className="notices">
            {notices.map(notice => (
              <Notice key={notice.count} removeNotice={this.removeNotice} {...notice} />
            ))}
          </div>
        ) : (
          ''
        )}
        {shouldShowLoading ? <Loading /> : ''}
      </div>
    )
  }

  public removeNotice = (count: number) => {
    service.removeNotice({
      count,
    })
  }
}

export default connect({ notification: wholeModel.notification })(Notification)
