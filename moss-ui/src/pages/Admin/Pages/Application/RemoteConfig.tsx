import PageTitle from '@/components/PageTitle'
import SearchBar from '@/components/SearchBar'
import applicationModel from '@/models/application/model'
import applicationService from '@/models/application/service'
import { connect } from '@/util/store'
import { Button, Col, Divider, Form, Input, Layout, Popconfirm, Row, Select, Table } from 'antd'
import { FormComponentProps } from 'antd/es/form'
import { WrappedFormUtils } from 'antd/es/form/Form'
import { ColumnProps } from 'antd/es/table'
import _ from 'lodash'
import moment from 'moment'
import React from 'react'
import { RouteComponentProps } from 'react-router'
import './style.less'

const { Content } = Layout

const FormItem = Form.Item
const EditableContext = React.createContext({} as WrappedFormUtils)
const EditableRow = ({ form, index, ...props }: { form: WrappedFormUtils; index: number }) => (
  <EditableContext.Provider value={form}>
    <tr {...props} />
  </EditableContext.Provider>
)
const EditableFormRow = Form.create()(EditableRow)

interface IEditableCellProps {
  record: RemoteConfigModel
  editable: boolean
  renderEidtable: Function
  required: boolean
}

class EditableCell extends React.Component<
  IEditableCellProps & ColumnProps<IEditableCellProps> & FormComponentProps
> {
  render() {
    const {
      dataIndex = '',
      title,
      required,
      record,
      editable,
      renderEidtable,
      children,
      ...restProps
    } = this.props
    const { editing = false } = record || {}
    return (
      <EditableContext.Consumer>
        {form => {
          const { getFieldDecorator } = form

          let contentComponent
          if (editable && editing) {
            if (typeof renderEidtable === 'function') {
              contentComponent = renderEidtable(
                getFieldDecorator,
                dataIndex,
                title,
                record,
                required
              )
            } else {
              contentComponent = (
                <FormItem style={{ margin: 0 }} className="editable-cell-form">
                  {getFieldDecorator(dataIndex, {
                    rules: [
                      {
                        required,
                        message: `请输入 ${title}!`,
                      },
                    ],
                    initialValue: record && record[dataIndex],
                  })(<Input className="editablecell-input" />)}
                </FormItem>
              )
            }
          } else {
            contentComponent = record ? (
              <Input
                className="readonly editablecell-input"
                value={String(_.last(children) || '')}
                readOnly={true}
              />
            ) : (
              children
            )
          }
          return <td {...restProps}>{contentComponent}</td>
        }}
      </EditableContext.Consumer>
    )
  }
}

const columns = [
  {
    title: 'id',
    dataIndex: 'id',
    key: 'id',
    width: 100,
  },
  {
    title: '字典名称',
    dataIndex: 'dictName',
    key: 'dictName',
    editable: true,
  },
  {
    title: '字典 key',
    dataIndex: 'dictCode',
    key: 'dictCode',
    editable: true,
  },
  {
    title: '修改时间',
    dataIndex: 'gmtModified',
    key: 'gmtModified',
    render: (time: number) => {
      return moment(time).fromNow()
    },
  },
]

const optionsColumns = [
  {
    title: 'id',
    dataIndex: 'id',
    key: 'id',
    width: 80,
  },
  {
    title: '数据项名称',
    dataIndex: 'itemName',
    key: 'itemName',
    editable: true,
    width: 150,
  },
  {
    title: '数据项值',
    dataIndex: 'itemValue',
    key: 'itemValue',
    required: false,
    editable: true,
    width: 150,
  },
  {
    title: '描述',
    dataIndex: 'itemDesc',
    key: 'itemDesc',
    editable: true,
  },
  {
    title: '修改时间',
    dataIndex: 'gmtModified',
    key: 'gmtModified',
    width: 140,
    render: (time: number) => {
      return moment(time).fromNow()
    },
  },
  {
    title: '状态',
    dataIndex: 'status',
    key: 'status',
    width: 100,
    render: (status: number) => {
      return status === 1 ? '已启用' : '已禁用'
    },
    editable: true,
    renderEidtable: (
      getFieldDecorator: Function,
      dataIndex: string,
      title: string,
      record: RemoteConfigModel
    ) => {
      return (
        <FormItem style={{ margin: 0 }} className="editable-cell-form">
          {getFieldDecorator(dataIndex, {
            rules: [
              {
                required: true,
                message: `请输入 ${title}!`,
              },
            ],
            initialValue: (record && record[dataIndex]) || 0,
          })(
            <Select className="status-select">
              <Select.Option value={0}>禁用</Select.Option>
              <Select.Option value={1}>启用</Select.Option>
            </Select>
          )}
        </FormItem>
      )
    },
  },
]

const editableTableComponents = {
  body: {
    row: EditableFormRow,
    cell: EditableCell,
  },
}

interface IRemoteConfigProps {}

interface IRemoteCofigState {
  remoteConfigs: RemoteConfigModel[]
}
class RemoteConfig extends React.Component<
  IRemoteConfigProps & FormComponentProps & RouteComponentProps,
  IRemoteCofigState
> {
  public state: IRemoteCofigState = {
    remoteConfigs: [],
  }

  private remoteConfigData = {} as RemoteConfigData

  public componentDidMount() {
    this.refreshRemoteConfDatas()
  }

  private lastFetchOptions: any = {}

  private OperationColumn = {
    title: '操作',
    dataIndex: 'operation',
    key: 'operation',
    render: (__: any, record: RemoteConfigModel) => {
      return (
        <EditableContext.Consumer>
          {form =>
            record && record.editing ? (
              <div>
                <a type="primary" onClick={this.save(record, form)}>
                  保存
                </a>
                <Divider type="vertical" />
                <a type="primary" onClick={this.reset}>
                  取消
                </a>
              </div>
            ) : (
              <div>
                <a type="primary" onClick={this.edit(record)}>
                  编辑
                </a>
                <Divider type="vertical" />
                <Popconfirm title="确认删除 ?" onConfirm={this.delete(record)}>
                  <a type="danger">删除</a>
                </Popconfirm>
              </div>
            )
          }
        </EditableContext.Consumer>
      )
    },
  }
  public render() {
    const { currentPage, totalCount } = this.remoteConfigData
    const { remoteConfigs } = this.state
    const disableAddBtn = remoteConfigs.some(r => {
      return Boolean(r.editing)
    })

    const editableColumns: ColumnProps<RemoteConfigModel>[] = columns.map(col => ({
      ...col,
      onCell: (record: RemoteConfigModel) => ({
        record,
        dataIndex: col.dataIndex,
        title: col.title,
        editable: col.editable,
      }),
    }))
    editableColumns.push(this.OperationColumn)

    return (
      <Layout className="page page-remote-config">
        <Row>
          <Col>
            <PageTitle name="数据字典" info="在线配置参数, 规则等" />
          </Col>
        </Row>
        <Content className="content-board">
          <Row className="search-row" type="flex" align="middle">
            <Col span={3}>
              <Button
                icon="plus"
                className="btn-add-type"
                type="primary"
                onClick={this.addConfig}
                disabled={disableAddBtn}>
                添加类型
              </Button>
            </Col>
            <Col span={18}>
              <SearchBar
                optionConf={{ hideMoreSearch: true, hideCountPerPage: true }}
                onSubmit={this.search}
              />
            </Col>
          </Row>
          <Table
            className="table-types"
            components={editableTableComponents}
            dataSource={remoteConfigs}
            columns={editableColumns}
            rowKey="id"
            expandedRowRender={this.renderChildConfigs}
            pagination={{
              pageSize: 8,
              current: currentPage,
              showQuickJumper: true,
              total: totalCount,
              onChange: this.changePage,
            }}
          />
        </Content>
      </Layout>
    )
  }

  private changePage = (page: number) => {
    const { appName } = this.lastFetchOptions
    this.refreshRemoteConfDatas(appName || '', page)
  }

  private reset = () => {
    this.setState({
      remoteConfigs: _.cloneDeep(this.remoteConfigData.list),
    })
  }

  private edit = (remoteConfig: RemoteConfigModel) => () => {
    const { remoteConfigs } = this.state
    remoteConfig.editing = true

    if (remoteConfig.itemName) {
      // 编辑数据选项
      const parent = remoteConfigs.find(
        r => r.dictCode === remoteConfig.dictCode
      ) as RemoteConfigModel
      const optionIndex = parent.dictDataModelList.findIndex(
        option => option.id === remoteConfig.id
      )
      parent.dictDataModelList.splice(optionIndex, 1, remoteConfig)
    } else {
      // 编辑数据类型
      const index = remoteConfigs.findIndex(r => r.id === remoteConfig.id)
      remoteConfigs.splice(index, 1, remoteConfig)
    }
    this.setState({ remoteConfigs })
  }

  private save = (remoteConfig: RemoteConfigModel, form: WrappedFormUtils) => () => {
    form.validateFields((error, row) => {
      if (error) {
        return
      }

      const modifiedConfOrOption = { ...remoteConfig, ...row }
      Reflect.deleteProperty(modifiedConfOrOption, 'editing')
      Reflect.deleteProperty(modifiedConfOrOption, 'new')
      Reflect.deleteProperty(modifiedConfOrOption, 'gmtCreate')
      Reflect.deleteProperty(modifiedConfOrOption, 'gmtModified')

      if (row.itemName) {
        // 编辑选项数据
        applicationService[
          remoteConfig.new ? 'createRemoteConfigOption' : 'updateRemoteConfigOption'
        ](modifiedConfOrOption).then(this.refreshRemoteConfDatas)
      } else {
        // 编辑类型数据
        applicationService[remoteConfig.new ? 'createRemoteConfig' : 'updateRemoteConfig'](
          modifiedConfOrOption
        ).then(this.refreshRemoteConfDatas)
      }
    })
  }

  private delete = (remoteConfig: RemoteConfigModel) => () => {
    applicationService[remoteConfig.itemName ? 'deleteRemoteConfigOption' : 'deleteRemoteConfig'](
      remoteConfig.id!
    ).then(this.refreshRemoteConfDatas)
  }

  private renderChildConfigs = (remoteConfig: RemoteConfigModel) => {
    const { remoteConfigs } = this.state
    const disableAddBtn = remoteConfigs.some(r => {
      return Boolean(r.editing)
    })
    const editableColumns: ColumnProps<RemoteConfigModel>[] = optionsColumns.map(col => ({
      ...col,
      onCell: (record: RemoteConfigModel) => {
        return {
          record,
          required: Reflect.has(col, 'required') ? col.required : true,
          dataIndex: col.dataIndex,
          title: col.title,
          editable: col.editable,
          renderEidtable: col.renderEidtable,
        }
      },
    }))

    editableColumns.push(this.OperationColumn)

    return (
      <Row className="options-container">
        <Button
          className="btn-add-option"
          disabled={disableAddBtn}
          onClick={this.addOption(remoteConfig)}>
          添加选项
        </Button>
        <Table
          className="options-table"
          size="small"
          components={editableTableComponents}
          dataSource={remoteConfig.dictDataModelList}
          columns={editableColumns}
          rowKey="id"
          pagination={false}
        />
      </Row>
    )
  }

  private addOption = (remoteConfig: RemoteConfigModel) => () => {
    const DEFAULT_OPTION = {
      dictCode: remoteConfig.dictCode,
      isDeleted: 0,
      itemDesc: '',
      itemName: '',
      itemSort: 0,
      itemValue: '',
    } as RemoteConfigModel
    const newOption = { ...DEFAULT_OPTION, editing: true, new: true } as RemoteConfigModel
    remoteConfig.dictDataModelList.push(newOption)
    const { remoteConfigs } = this.state
    const index = remoteConfigs.findIndex(r => r.id === remoteConfig.id)
    remoteConfigs.splice(index, 1, remoteConfig)
    this.setState({ remoteConfigs: [...remoteConfigs] })
  }

  private addConfig = () => {
    const newEmptyConfig = {
      dictCode: '',
      dictName: '',
      dictDataModelList: [] as RemoteConfigModel[],
      editing: true,
      new: true,
      isDeleted: 0,
      status: 0,
    } as RemoteConfigModel
    this.setState({
      remoteConfigs: [...this.state.remoteConfigs, newEmptyConfig],
    })
  }

  private refreshRemoteConfDatas = (search?: string | any, page = 1) => {
    applicationService.fetchRemoteConfigs(search, page).then(confs => {
      this.remoteConfigData = confs
      this.setState({
        remoteConfigs: _.cloneDeep(confs.list), // for reset form function
      })
    })
  }

  private search = (values: any) => {
    this.lastFetchOptions = values
    this.refreshRemoteConfDatas(values.appName)
  }
}

export default connect({ remoteConfigs: applicationModel.RemoteConfigs })(RemoteConfig)
