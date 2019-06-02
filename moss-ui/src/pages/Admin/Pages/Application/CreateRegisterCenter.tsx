import applicationService from '@/models/application/service'

import { Button, Checkbox, Radio, Form, Input, Layout, Select } from 'antd'
import { FormComponentProps } from 'antd/lib/form'
import React from 'react'
import { RouteComponentProps, withRouter } from 'react-router'
import globalService from '@/models/global/service'

const { TextArea } = Input
const { Content } = Layout
const RadioGroup = Radio.Group

interface ICreateRegisterCenterState {
  keyErrorMsg: string | null
}
class CreateRegisterCenter extends React.Component<
  FormComponentProps & RouteComponentProps,
  ICreateRegisterCenterState
  > {
  public state: ICreateRegisterCenterState = { keyErrorMsg: null }
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
            <Form.Item label="注册中心名称">
              {getFieldDecorator('name', {
                initialValue: initialValue.name,
                rules: [
                  {
                    type: 'string',
                    min: 3,
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
              label="注册中心Code"
              help={keyErrorMsg}
              validateStatus={keyErrorMsg ? 'error' : 'success'}>
              {getFieldDecorator('code', {
                initialValue: initialValue.code,
                rules: [
                  {
                    type: 'string',
                    min: 1,
                    message: '项目 key 过短',
                  },
                  {
                    required: true,
                    message: '请输入注册中心 key',
                  },
                  {
                    type: 'string',
                    pattern: /[A-Za-z0-9]+/,
                    message: '只能输入数字或字母',
                  },
                ],
              })(<Input disabled={type === 'CREATE' ? false : true} />)}
            </Form.Item>

            <Form.Item label="URL">
              {getFieldDecorator('url', {
                initialValue: initialValue.url,
                rules: [
                  {
                    type: 'string',
                    min: 3,
                    message: '注册中心url',
                  },
                  {
                    required: true,
                    message: '请输入注册中心url',
                  },
                ],
              })(<Input />)}
            </Form.Item>

            <Form.Item label="状态">
              {getFieldDecorator('status', {
                initialValue: type === 'CREATE' ? 1 : initialValue.status,
                rules: [
                  {
                    required: true,
                    message: '请选择状态',
                  },
                ],
              })(
                <RadioGroup name="radiogroup">
                  <Radio value={1}>运行</Radio>
                  <Radio value={0}>停用</Radio>
                </RadioGroup>
              )}
            </Form.Item>

            <Form.Item label="注册中心描述">
              {getFieldDecorator('desc', {
                initialValue: initialValue.desc,
                rules: [
                  {
                    required: true,
                    message: '请输入注册中心描述',
                    whitespace: true,
                  },
                  {
                    min: 3,
                    message: '注册中心描述过短',
                  },
                ],
              })(<TextArea rows={4} />)}
            </Form.Item>
            {type !== 'CREATE' && (
              <Form.Item label="是否删除">
                {getFieldDecorator('isDeleted', {
                  initialValue: initialValue.isDeleted,
                  rules: [
                    {
                      required: true,
                      message: '请选择注册中心的状态',
                    },
                  ],
                })(<Checkbox />)}
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
          applicationService
            .createRegisterCenter(values)
            .then(() => {
              this.props.history.push(`/registerCenterMgmt`)
            })
            .then(() => {
              globalService.fetchGlobalConf()
            })
            .catch(e => {
              this.setState({
                keyErrorMsg: e.errorMsg,
              })
            })
        } else {
          values.id = initialValue.id
          applicationService
            .updateRegisterCenter(values)
            .then(() => {
              this.props.history.push(`/registerCenterMgmt`)
            })
            .then(() => {
              globalService.fetchGlobalConf()
            })
        }
      }
    })
  }
}

export default Form.create({ name: 'create_registerCenter' })(withRouter(CreateRegisterCenter))
