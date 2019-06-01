import { Button, Form, Modal, Steps } from 'antd'
import { WrappedFormUtils } from 'antd/lib/form/Form'
import React from 'react'

import { checkDependencies } from '../util'
import Fieldset from './Fieldset'

const { Step } = Steps
const { Item } = Form

interface IProps {
  form: WrappedFormUtils
  data: FieldSetData[]
  confirm?: { title: string }
  onSubmit: (data: any) => any
}

interface IState {
  current: number
  confirm: boolean
  confirmContext?: JSX.Element
}

class FormGenerator extends React.Component<IProps, IState> {
  public state: IState = {
    current: 0,
    confirm: false,
  }

  public render() {
    const { current } = this.state
    const { form, data, confirm } = this.props

    return (
      <React.Fragment>
        {data.length > 1 ? (
          <Steps current={current}>
            {data.map(step => (
              <Step key={step.title} title={step.title} />
            ))}
          </Steps>
        ) : null}

        <div className="page-section">
          <Form onSubmit={this.handleSubmit} className="page-section-content" layout="horizontal">
            {data.length
              ? data.map((set, idx) =>
                  idx <= current ? (
                    <Fieldset form={form} key={set.title} {...set} visible={current === idx} />
                  ) : null
                )
              : null}

            <Item wrapperCol={{ span: 10, offset: 6 }}>
              {current > 0 && (
                <Button style={{ marginRight: 8 }} onClick={this.switchStep()}>
                  上一步
                </Button>
              )}

              {data[current].submit ? (
                <Button style={{ marginRight: 8 }} type="primary" htmlType="submit">
                  提交
                </Button>
              ) : null}

              {current < data.length - 1 && (
                <Button
                  type="primary"
                  disabled={!this.checkDependencies(data[current + 1].dependencies)}
                  onClick={this.switchStep(true)}
                >
                  下一步
                </Button>
              )}
            </Item>
          </Form>
        </div>

        {confirm ? (
          <Modal
            title={confirm.title}
            visible={this.state.confirm}
            onOk={this.handleConfirmOk}
            onCancel={this.handleConfirmCancel}
            children={this.state.confirmContext}
          />
        ) : null}
      </React.Fragment>
    )
  }

  private handleConfirmOk = () => {
    const { onSubmit, form } = this.props
    this.setState(
      {
        confirm: false,
      },
      () => {
        onSubmit(form.getFieldsValue())
      }
    )
  }

  private handleConfirmCancel = () => {
    this.setState({
      confirm: false,
    })
  }

  private showConfirm = (data: { [key: string]: string }) => {
    this.setState({
      confirm: true,
      confirmContext: (
        <React.Fragment>
          {Object.keys(data).map((key: string) => {
            let value
            try {
              value = JSON.stringify(data[key])
            } catch (e) {
              value = data[key]
            }

            return (
              <p key={key}>
                {key}: {value}
              </p>
            )
          })}
        </React.Fragment>
      ),
    })
  }

  private handleSubmit = (e: React.FormEvent) => {
    e.preventDefault()

    const { confirm, onSubmit, form } = this.props
    form.validateFields((err, values) => {
      if (!err) {
        if (confirm) {
          this.showConfirm(values)
        } else {
          onSubmit(values)
        }
      }
    })
  }

  private checkDependencies = (dependencies?: Dependencies) =>
    checkDependencies(this.props.form.getFieldValue, dependencies)

  private switchStep = (isNext?: boolean) => () => {
    this.setState({ current: this.state.current + (isNext ? 1 : -1) })
  }
}

export default Form.create()(FormGenerator)
