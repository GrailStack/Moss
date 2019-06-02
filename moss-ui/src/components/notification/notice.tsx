import classnames from 'classnames'
import React from 'react'

class Notice extends React.Component<
  INotice,
  {
    show: boolean
  }
> {
  public state = {
    show: false,
  }
  private timer?: NodeJS.Timer
  public render() {
    const { message, type } = this.props

    return (
      <div
        className={classnames('notice', {
          hide: this.state.show === false,
          show: this.state.show,
        })}
      >
        <i className={`iconfont icon-${type}`} />
        <span>{message}</span>
      </div>
    )
  }

  public componentDidMount() {
    const { duration = 2000 } = this.props
    setTimeout(() => {
      this.setState({ show: true }, () => {
        this.timer = setTimeout(() => {
          if (this.timer) {
            clearTimeout(this.timer)
          }
          this.removeNotice()
        }, duration)
      })
    }, 0)
  }

  public componentWillUnmount() {
    if (this.timer) {
      clearTimeout(this.timer)
    }
  }

  private removeNotice() {
    this.setState({ show: false }, () => {
      this.timer = setTimeout(() => {
        if (this.timer) {
          clearTimeout(this.timer)
        }

        if (this.props.removeNotice) {
          this.props.removeNotice(this.props.count as number)
        }
      }, 200)
    })
  }
}

export default Notice
