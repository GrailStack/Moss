import userService from '@/models/user/service'
import { Button, Checkbox, Radio, Form, Input, Layout, Select } from 'antd'
import { FormComponentProps } from 'antd/lib/form'
import React from 'react'
import { RouteComponentProps, withRouter } from 'react-router'

const { Content } = Layout
const RadioGroup = Radio.Group

interface ICreateUserState {
  keyErrorMsg: string | null
}
class CreateUser extends React.Component<
  FormComponentProps & RouteComponentProps,
  ICreateUserState
  > {
  public state: ICreateUserState = { keyErrorMsg: null }
  public render() {
    const { location } = this.props
    const { initialValue = {}, type = 'CREATE' } = location.state || {}
    const { getFieldDecorator } = this.props.form
    const { keyErrorMsg } = this.state
    return (
      <Layout className="page page-create-project">
        <Content className="content">
          <Form
            labelCol={{ span: 6 }}
            wrapperCol={{ span: 14 }}
            className="creat-project-form"
            onSubmit={this.handleSubmit}>
            <Form.Item label="姓名">
              {getFieldDecorator('name', {
                initialValue: initialValue.name,
                rules: [
                  {
                    type: 'string',
                    min: 1,
                    message: '名称过短',
                  },
                  {
                    required: true,
                    message: '请输入名称',
                  },
                ],
              })(<Input />)}
            </Form.Item>

            <Form.Item
              label="用户名"
              help={keyErrorMsg}
              validateStatus={keyErrorMsg ? 'error' : 'success'}>
              {getFieldDecorator('username', {
                initialValue: initialValue.username,
                rules: [
                  {
                    type: 'string',
                    min: 1,
                    message: ' 用户名过短',
                  },
                  {
                    required: true,
                    message: '请输入用户名',
                  },
                  {
                    type: 'string',
                    pattern: /[A-Za-z0-9]+/,
                    message: '只能输入数字或字母',
                  },
                ],
              })(<Input disabled={type === 'CREATE' ? false : true} />)}
            </Form.Item>

            <Form.Item label="email">
              {getFieldDecorator('email', {
                initialValue: initialValue.email,
                rules: [
                  {
                    type: 'string',
                    min: 3,
                    message: '邮箱',
                  },
                  {
                    required: true,
                    message: '请输入邮箱',
                  },
                ],
              })(<Input />)}
            </Form.Item>

            <Form.Item label="状态">
              {getFieldDecorator('status', {
                initialValue: type === 'CREATE' ? 0 : initialValue.status,
                rules: [
                  {
                    required: true,
                    message: '是否禁用',
                  },
                ],
              })(
                <RadioGroup name="radiogroup">
                  <Radio value={0}>启用</Radio>
                  <Radio value={1}>禁用</Radio>
                </RadioGroup>
              )}
            </Form.Item>
            {type == 'CREATE' && (
            <Form.Item label="初始化密码">
              {getFieldDecorator('password', {
                initialValue: initialValue.password,
                rules: [
                  {
                    required: true,
                    message: '请输入密码',
                    whitespace: true,
                  }
                ],
              })(<Input.Password />)}
            </Form.Item>
            )}
            <Form.Item wrapperCol={{ span: 14, offset: 6 }}>
              <Button type="primary" htmlType="submit">
                {type === 'CREATE' ? '保存' : '更新'}
              </Button>
            </Form.Item>
          </Form>
        </Content>
      </Layout>
    )
  }
  private handleSubmit = (e: any) => {
    e.preventDefault()
    const { location } = this.props
    const { type = 'CREATE', initialValue } = location.state || ({} as any)
    this.props.form.validateFieldsAndScroll((err, values) => {
      debugger
      if (!err) {
        values.code = values.code
        values.isDeleted = values.isDeleted ? 1 : 0
        this.setState({
          keyErrorMsg: null,
        })
        if (type === 'CREATE') {
          userService.createUser(values)
            .then(() => {
              this.props.history.push(`/userMgmt`)
            })
            .catch(e => {
              this.setState({
                keyErrorMsg: e.errorMsg,
              })
            })
        } else {
          values.id = initialValue.id
          userService.updateUser(values)
            .then(() => {
              this.props.history.push(`/userMgmt`)
            })
        }
      }
    })
  }
}

export default Form.create({ name: 'create_user' })(withRouter(CreateUser))
