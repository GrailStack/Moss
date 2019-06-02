import Fieldset from '@/components/FormGenerator/Fieldset'
import { Button, Form } from 'antd'
import { FormComponentProps } from 'antd/lib/form/Form'
import { isMoment } from 'moment'
import React from 'react'
import { RouteComponentProps, withRouter } from 'react-router'

import { getInitialValue } from '../util'

const { Item } = Form

interface ISearchBarProps
  extends RouteComponentProps,
    FormComponentProps,
    SearchBarConstructorData {
  data: any
  extend: any
}

interface IState {
  searchFields: FieldSetData
}

class SearchBar extends React.PureComponent<ISearchBarProps, IState> {
  private searchFields: FieldSetData = {}

  constructor(props: ISearchBarProps) {
    super(props)

    if (props.data) {
      this.searchFields = getInitialValue(props.data, props.location.search)
    }
  }
  public render() {
    const { form, extend } = this.props

    return (
      <Form className="page-section search-bar" onSubmit={this.submit} layout="inline">
        {this.searchFields.fields ? (
          <React.Fragment>
            <Fieldset form={form} {...this.searchFields} layout="inline" visible={true} />
            <Item>
              <Button type="primary" icon="search" htmlType="submit">
                查询
              </Button>
              <Button type="default" icon="reload" onClick={this.reset}>
                重置
              </Button>
              {extend ? extend : null}
            </Item>
          </React.Fragment>
        ) : (
          <Item>{extend ? extend : null}</Item>
        )}
      </Form>
    )
  }

  private submit = (evt: React.FormEvent) => {
    evt.preventDefault()
    this.props.form.validateFields(
      (err: null | { [index: string]: any }, values: { [index: string]: any }) => {
        if (!err) {
          const keys = Object.keys(values)
          const newTime: { [key: string]: any } = {}

          keys.forEach(key => {
            const value = values[key]
            if (
              isMoment(value) ||
              (Array.isArray(value) && isMoment(value[0]) && isMoment(value[1]))
            ) {
              newTime[key] = this.formatMoment(value)
            }
          })

          this.props.onSubmit(
            {
              ...values,
              ...newTime,
            },
            true
          )
        }
      }
    )
  }

  private formatMoment = (time: any) => {
    if (Array.isArray(time)) {
      return [
        time[0]
          .startOf('day')
          .toDate()
          .getTime(),
        time[1]
          .endOf('day')
          .toDate()
          .getTime(),
      ]
    } else {
      return time
        .startOf('day')
        .toDate()
        .getTime()
    }
  }

  private reset = () => {
    const { form, onSubmit, data } = this.props

    if (data.fields) {
      // https://github.com/ant-design/ant-design/issues/8880
      const fields = data.fields.map((field: any) => field.fieldName)
      const values: { [key: string]: any } = {}
      const fValues: { [key: string]: any } = {}

      fields.forEach((fieldName: string) => {
        values[fieldName] = { value: undefined }
        fValues[fieldName] = undefined
      })
      form.setFields(values)
      onSubmit(fValues, true)
    }
  }
}

export default Form.create()(withRouter(SearchBar))
