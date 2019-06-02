import { qsParse } from '@/util'
import { Button, Collapse, Form, Icon, Input, Radio } from 'antd'
import { FormComponentProps } from 'antd/lib/form/Form'
import classnames from 'classnames'
import React from 'react'
import { RouteComponentProps, withRouter } from 'react-router'
import './style.less'

const { Item } = Form
const { Panel } = Collapse

type OptionConfType = {
  hideMoreSearch?: boolean
  hideCountPerPage?: boolean
}

interface IProps {
  optionConf?: OptionConfType
  onSubmit: (value: any) => void
}

interface IState {
  showMore: boolean
}

class SearchBar extends React.PureComponent<
  RouteComponentProps & FormComponentProps & IProps,
  IState
  > {
  public state = {
    showMore: false,
  }

  public render() {
    const { showMore } = this.state
    const { optionConf = {} } = this.props
    const { hideMoreSearch, hideCountPerPage } = optionConf
    const { getFieldDecorator } = this.props.form
    const qs = qsParse(this.props.location.search)

    return (
      <div className="advanced-search-form-container">
        <Form
          onSubmit={this.submit}
          layout="inline"
          className={classnames(
            'ant-advanced-search-form',
            'search-bar-form',
            hideMoreSearch ? '' : 'more-search-ops'
          )}>
          <Item>
            {getFieldDecorator('appName', {
              initialValue: qs.appName,
            })(
              <Input
                prefix={<Icon type="schedule" style={{ color: 'rgba(0,0,0,.25)' }} />}
                placeholder="关键字搜索"
              />
            )}
          </Item>
          {!hideCountPerPage && (
            <Item>
              {getFieldDecorator('pageSize', {
                initialValue: qs.pageSize,
              })(
                <Input
                  prefix={<Icon type="table" style={{ color: 'rgba(0,0,0,.25)' }} />}
                  placeholder="每页数量"
                />
              )}
            </Item>
          )}
          {!hideMoreSearch && (
            <Item className="radio-collect-app">
              {getFieldDecorator('findType', {
                initialValue: '',
              })(
                <Radio.Group onChange={this._submit}>
                  <Radio.Button value="2">我的收藏</Radio.Button>
                  <Radio.Button className="all-app" value="1">
                    我的服务
                  </Radio.Button>
                  <Radio.Button className="all-app" value="">
                    所有应用
                  </Radio.Button>
                </Radio.Group>
              )}
              {/* <Button
                className={classnames('more-btn', showMore ? 'show-more' : '')}
                onClick={this.handleMoreClick}>
                更多搜索
              </Button> */}
            </Item>
          )}
          <Item>
            <Button type="primary" icon="search" htmlType="submit">
              搜索
            </Button>
          </Item>
          {/* {!hideMoreSearch && (
            <Collapse className="more-filters" activeKey={showMore ? 'filters' : ''}>
              <Panel header="" key="filters">
                <Item>
                  {getFieldDecorator('instanceNumber', {
                    initialValue: qs.instanceNumber,
                  })(
                    <Input
                      prefix={<Icon type="poweroff" style={{ color: 'rgba(0,0,0,.25)' }} />}
                      placeholder="instance 数量"
                    />
                  )}
                </Item>
                <Item>
                  {getFieldDecorator('upNumber', {
                    initialValue: qs.upNumber,
                  })(
                    <Input
                      prefix={<Icon type="caret-up" style={{ color: 'rgba(0,0,0,.25)' }} />}
                      placeholder="Up 数量"
                    />
                  )}
                </Item>
              </Panel>
            </Collapse>
          )} */}
        </Form>
      </div>
    )
  }

  private handleMoreClick = () => {
    this.setState({
      showMore: !this.state.showMore,
    })
  }

  private _submit = () => {
    setTimeout(() => {
      this.props.form.validateFields(
        (err: null | { [index: string]: any }, values: { [index: string]: any }) => {
          if (!err) {
            this.props.onSubmit(values)
          }
        }
      )
    }, 233)
  }

  private submit = (evt: React.FormEvent) => {
    evt.preventDefault()
    this._submit()
  }
}

export default Form.create()(withRouter(SearchBar))
