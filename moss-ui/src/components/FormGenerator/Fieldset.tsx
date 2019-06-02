import { Form, Icon, Tooltip } from 'antd'
import { FormLayout, GetFieldDecoratorOptions, WrappedFormUtils } from 'antd/lib/form/Form'
import React from 'react'

import { checkDependencies, interpolate } from '../util'
import Field from './Field'

const formItemLayout = {
  labelCol: { span: 6 },
  wrapperCol: { span: 10 },
}

class Fieldset extends React.Component<
  FieldSetData & {
    form: WrappedFormUtils
    visible: boolean
    layout?: FormLayout
  }
> {
  public render() {
    const { visible, fields = [], dependencies = [], form } = this.props
    const checked = checkDependencies(form.getFieldValue, dependencies)
    return checked ? (
      <fieldset style={{ display: visible ? '' : 'none' }}>
        {fields.map(field => this.getField(field))}
      </fieldset>
    ) : null
  }

  private getDecoratorOptions(option: GetFieldDecoratorOptions) {
    const { form } = this.props
    let { initialValue } = option

    if (typeof initialValue === 'string') {
      initialValue = interpolate(initialValue, form.getFieldsValue())
    } else if (typeof initialValue === 'function') {
      initialValue = initialValue(form.getFieldsValue())
    }
    return {
      ...option,
      initialValue,
    }
  }

  private getField = (fieldData: FieldData) => {
    const { fieldName, option = {}, control, dependencies = [], hidden } = fieldData
    const { form, layout } = this.props
    const props = { form, ...control }
    const layoutSet = !layout || layout === 'horizontal' ? formItemLayout : {}

    return checkDependencies(form.getFieldValue, dependencies) ? (
      <Form.Item
        style={{ display: hidden ? 'none' : '' }}
        label={this.getLabel(fieldData) || fieldName}
        key={fieldName}
        {...layoutSet}
      >
        {form.getFieldDecorator(fieldName, this.getDecoratorOptions(option))(<Field {...props} />)}
      </Form.Item>
    ) : null
  }

  private getLabel = ({ tip, label, fieldName }: FieldData) => {
    const title = label || fieldName

    if (!tip) {
      return title
    } else {
      return (
        <span>
          {title}&nbsp;
          <Tooltip title={tip}>
            <Icon className="primary-color" type="question-circle-o" />
          </Tooltip>
        </span>
      )
    }
  }
}

export default Fieldset
