import { WrappedFormUtils } from 'antd/lib/form/Form'
import axios, { AxiosResponse } from 'axios'
import React from 'react'
import ANTD from './import'

import { compileTemplateLiterals } from '../util'
import MyControl from './Control'

// [TODO]
// getFieldDecorator 包装过的组件会导入一些 props, 需要显示声明
interface IFieldProps extends FieldControl {
  form: WrappedFormUtils
}

interface IFieldState {
  options?: FieldOptions
  url?: string
}

class Field extends React.Component<IFieldProps, IFieldState> {
  public state = { options: [], url: '' }
  private gap = 700
  private lastFetch: any

  public render() {
    const { name } = this.props
    const { options } = this.state

    const Control = MyControl[name] || (ANTD as { [key: string]: any })[name]
    const controlProps = {
      ...this.props,
      options,
    }
    delete controlProps.name
    return Control ? <Control {...controlProps} /> : null
  }

  public componentDidMount = () => {
    this.fetchOptionsData()
  }

  public shouldComponentUpdate(nextProps: IFieldProps) {
    const { form, id, options } = nextProps

    if (options && typeof options === 'string') {
      const { url } = this.state
      const nextUrl = compileTemplateLiterals(options, form.getFieldsValue())

      if (nextUrl && nextUrl !== url) {
        form.resetFields([id])
        this.fetchOptionsData()
        return false
      } else if (url && !nextUrl) {
        this.setState({
          options: [],
          url: '',
        })
      }
    }
    return true
  }

  private fetchOptionsData = () => {
    const { options, optionsFilter, extend } = this.props

    if (typeof options === 'string') {
      const url = compileTemplateLiterals(options, this.props.form.getFieldsValue())

      if (url) {
        if (this.lastFetch) {
          clearTimeout(this.lastFetch)
        }

        this.lastFetch = setTimeout(() => {
          axios.get(url, { withCredentials: true }).then(({ data }: AxiosResponse) => {
            let result

            if (optionsFilter) {
              result = optionsFilter(data)
            } else if (data.code === 200) {
              result = Array.isArray(data.data) ? data.data : data.data.list
            } else if (Array.isArray(data)) {
              result = data
            } else {
              result = []
            }

            const extendValue =
              typeof extend === 'function' ? extend(this.props.form.getFieldsValue()) : extend
            const opt = extendValue ? extendValue.concat(result) : result
            this.setState({
              url,
              options: opt,
            })
          })
        }, this.gap)
      }
    } else if (Array.isArray(options)) {
      this.setState({
        options,
      })
    } else if (typeof options === 'function') {
      this.setState({
        options: options(),
      })
    }
  }
}

export default Field
