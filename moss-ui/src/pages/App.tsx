import springIcon from '@/assets/icon-spring-framework.svg'
import Notification from '@/components/notification'
import withAuth from '@/components/withAuth'
import globalService from '@/models/global/service'
import '@/util/g2CustomShapes'
import { notification } from 'antd'
import { createBrowserHistory } from 'history'
import _ from 'lodash'
import moment from 'moment'
import React from 'react'

import ErrorBoundary, { FallbackProps } from 'react-error-boundary'
import { Redirect, Route, Router, Switch } from 'react-router-dom'
import Admin from './Admin'
import Login from './Login'

export const history = createBrowserHistory()
class App extends React.Component {
  constructor(props: any) {
    super(props)
    notification.config({
      duration: 2,
    })

    const source =
      ENV === 'development'
        ? new EventSource(`${apiHost}/applications`)
        : new EventSource(`applications`)
    source.addEventListener(
      'message',
      (e: any) => {
        try {
          this.showEventNotification(e.data)
        } catch (e) {
          console.error(e)
        }
      },
      false
    )

    source.addEventListener(
      'error',
      e => {
        source.close()
        console.error('EventSource : ', e)
      },
      false
    )
  }

  componentDidMount() {
    globalService.fetchGlobalConf()
  }

  public render() {
    const FallbackComponent = ({ componentStack, error }: FallbackProps) => (
      <div
        style={{
          display: 'felx',
          flexDirection: 'column',
          justifyContent: 'center',
          padding: '5vw',
        }}>
        <p>
          <strong>Oops! An error occured!</strong>
        </p>
        <p>
          <strong>Error:</strong> {error && error.toString()}
        </p>
        <p>
          <strong>Stacktrace:</strong>
          <pre>{componentStack}</pre>
        </p>
      </div>
    )

    return (
      <ErrorBoundary FallbackComponent={FallbackComponent}>
        <Router history={history}>
          <React.Fragment>
            <Switch>
              <Route path="/login" component={Login} />
              <Route path="/:current/:sub" component={withAuth(Admin)} />
              <Route path="/:current" component={withAuth(Admin)} />
              <Redirect to="/dashboard" />
            </Switch>
            <Notification />
          </React.Fragment>
        </Router>
      </ErrorBoundary>
    )
  }

  private showEventNotification = _.debounce((data: any) => {
    const msg = JSON.parse(data)
    notification.open({
      message: msg.name,
      description: `${moment.unix(msg.statusTimestamp).fromNow()} : ${msg.status}`,
      icon: <img className="icon" src={springIcon} />,
      onClick: () => {
        window.open(`/list?appName=${msg.name}&value=false`)
      },
    })
  }, 1000)
}

export default App
