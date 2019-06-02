import logo from '@/assets/logo.png'
import globalService from '@/models/global/service'
import loginService from '@/models/login/service'
import menuService from '@/models/menu/service'
import { connect, wholeModel } from '@/util/store'
import { Alert, Button, Form, Icon, Input, Layout } from 'antd'
import { FormComponentProps } from 'antd/lib/form'
import React from 'react'
import { RouteComponentProps, withRouter } from 'react-router'
import './style.less'
const { Content, Footer } = Layout

interface ILoginState {
  message: string | null
}
class Login extends React.Component<FormComponentProps & RouteComponentProps, ILoginState> {
  public state = { message: null }

  componentDidMount() {
    document.addEventListener('keydown', this.handleKeyDown, false)
  }

  componentWillUnmount() {
    document.removeEventListener('keydown', this.handleKeyDown, false)
  }

  public render() {
    const { message } = this.state
    const { getFieldDecorator } = this.props.form

    return (
      <Layout className="page-login">
        <Content className="content">
          <div className="login-title">
            <div className="title-container">
              <img className="icon" src={logo} alt="Moss" />
              <span className="title">Moss</span>
            </div>
            <div className="description">莫斯-全球首款服务治理平台</div>
          </div>

          <Form onSubmit={this.handleSubmit} className="login-form">
            {message && (
              <Alert
                style={{ marginBottom: 24 }}
                message={message}
                type="error"
                showIcon={true}
                closable={true}
              />
            )}
            <Form.Item>
              {getFieldDecorator('username', {
                rules: [{ required: true, message: '请输入用户名' }],
              })(
                <Input
                  size="large"
                  prefix={<Icon type="user" style={{ color: 'rgba(0,0,0,.25)' }} />}
                  placeholder="用户名"
                />
              )}
            </Form.Item>
            <Form.Item>
              {getFieldDecorator('password', {
                rules: [{ required: true, message: '请输入密码' }],
              })(
                <Input
                  size="large"
                  prefix={<Icon type="lock" style={{ color: 'rgba(0,0,0,.25)' }} />}
                  type="password"
                  placeholder="密码"
                />
              )}
            </Form.Item>

            <Button
              id="login"
              size="large"
              type="primary"
              htmlType="submit"
              className="login-form-button">
              登录
            </Button>
          </Form>
        </Content>
        <Footer>Copyright 2019 Moss服务治理平台 xujin.org</Footer>
      </Layout>
    )
  }

  handleKeyDown = (e: Event) => {
    // @ts-ignore
    if (e.code !== 'Enter') {
      return
    }
    // for functional component, React.createRef not works
    const loginBtn = document.getElementById('login')
    loginBtn!.click()
  }

  handleSubmit = (e: any) => {
    e.preventDefault()
    const { history } = this.props

    this.props.form.validateFields((err, values) => {
      if (!err) {
        const formData = new FormData()
        formData.append('username', values.username)
        formData.append('password', values.password)
        loginService
          .login(formData)
          .then(data => {
            if (!data) {
              this.setState({
                message: '用户名或密码错误',
              })
            } else {
              this.setState({
                message: null,
              })
              menuService
                .fetchUserMenu()
                .then(() => {
                  return globalService.fetchGlobalConf()
                })
                .then(() => {
                  history.push('/dashboard')
                })
            }
          })
          .catch(() => {
            this.setState({
              message: '用户名或密码错误',
            })
          })
      }
    })
  }
}

export default connect({ login: wholeModel.login })(
  withRouter(Form.create({ name: 'normal_login' })(Login))
)
