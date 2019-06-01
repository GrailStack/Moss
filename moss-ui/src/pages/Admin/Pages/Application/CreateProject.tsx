import applicationModel from '@/models/application/model'
import applicationService from '@/models/application/service'
import { connect } from '@/util/store'
import { Button, Checkbox, Form, Input, Layout, Select } from 'antd'
import { FormComponentProps } from 'antd/lib/form'
import React from 'react'
import { RouteComponentProps, withRouter } from 'react-router'

const { Option } = Select
const { TextArea } = Input
const { Content } = Layout

interface ICreateProjectProps {
  userList: UserModel[]
}

interface ICreateProjectState {
  keyErrorMsg: string | null
}
class CreateProject extends React.Component<
  ICreateProjectProps & FormComponentProps & RouteComponentProps,
  ICreateProjectState
> {
  public state: ICreateProjectState = { keyErrorMsg: null }
  public componentDidMount() {
    applicationService.fetchUserList()
  }

  public render() {
    const { location, userList } = this.props
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
            <Form.Item label="项目名称">
              {getFieldDecorator('cname', {
                initialValue: initialValue.cname,
                rules: [
                  {
                    type: 'string',
                    min: 3,
                    message: '项目名称过短',
                  },
                  {
                    required: true,
                    message: '请输入项目名称',
                  },
                ],
              })(<Input />)}
            </Form.Item>
            <Form.Item
              label="项目 key"
              help={keyErrorMsg}
              validateStatus={keyErrorMsg ? 'error' : 'success'}>
              {getFieldDecorator('key', {
                initialValue: initialValue.key,
                rules: [
                  {
                    type: 'string',
                    min: 1,
                    message: '项目 key 过短',
                  },
                  {
                    required: true,
                    message: '请输入项目 key',
                  },
                  {
                    type: 'string',
                    pattern: /[A-Za-z0-9]+/,
                    message: '只能输入数字或字母',
                  },
                ],
              })(<Input disabled={type === 'CREATE' ? false : true} />)}
            </Form.Item>
            <Form.Item label="项目owner">
              {getFieldDecorator('ownerId', {
                initialValue: initialValue.ownerId,
                rules: [
                  {
                    required: true,
                    message: '请选择项目 owner',
                  },
                ],
              })(
                <Select
                  showSearch={true}
                  style={{ width: 200 }}
                  placeholder="选择项目 Owner"
                  filterOption={this.filterOwner}>
                  {userList.map(u => {
                    return (
                      <Option key={u.id} value={u.id}>
                        {u.name} ({u.username})
                      </Option>
                    )
                  })}
                </Select>
              )}
            </Form.Item>
            <Form.Item label="项目描述">
              {getFieldDecorator('description', {
                initialValue: initialValue.description,
                rules: [
                  {
                    required: true,
                    message: '请输入项目描述',
                    whitespace: true,
                  },
                  {
                    min: 3,
                    message: '项目描述过短',
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
                      message: '请选择项目状态',
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

  private filterOwner = (input: string, option: any) => {
    return option.props.children.toLowerCase().indexOf(input) !== -1
  }

  private handleSubmit = (e: any) => {
    e.preventDefault()
    const { location, userList } = this.props
    const { type = 'CREATE', initialValue } = location.state || ({} as any)

    this.props.form.validateFieldsAndScroll((err, values) => {
      if (!err) {
        values.name = values.cname
        values.isDeleted = values.isDeleted ? 1 : 0

        const findOwner = userList.find(u => {
          return values.ownerId === u.id
        })

        if (findOwner) {
          values.ownerName = findOwner.username
        }
        this.setState({
          keyErrorMsg: null,
        })

        if (type === 'CREATE') {
          applicationService
            .createProject(values)
            .then(() => {
              this.props.history.push(`/project`)
            })
            .catch(e => {
              this.setState({
                keyErrorMsg: e.errorMsg,
              })
            })
        } else {
          values.id = initialValue.id
          applicationService.updateProject(values).then(() => {
            this.props.history.push(`/project`)
          })
        }
      }
    })
  }
}

export default connect({ userList: applicationModel.UserList })(
  withRouter(Form.create({ name: 'create_project' })(CreateProject))
)
