import { connect, wholeModel } from '@/util/store'
import React from 'react'
import { Redirect } from 'react-router-dom'

export const withAuth = (BaseComponent: React.ComponentType) => {
  class AuthRoute extends React.Component<{
    login: LoginData
  }> {
    public render() {
      const { login } = this.props
      return login.token ? (
        <BaseComponent />
      ) : (
        <Redirect
          to={{
            pathname: '/login',
          }}
        />
      )
    }
  }
  return connect({ login: wholeModel.login })(AuthRoute)
}

export default withAuth
