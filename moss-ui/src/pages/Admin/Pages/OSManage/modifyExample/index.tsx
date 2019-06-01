import FormGenerator from '@/components/FormGenerator'
import PageTitle from '@/components/PageTitle'
import menuService from '@/models/menu/service'
import { connect, wholeModel } from '@/util/store'
import { FormComponentProps } from 'antd/lib/form/Form'
import React from 'react'
import { RouteComponentProps, withRouter } from 'react-router'

import data from './data'

const { fields,addAction, action } = data

class ModifyExample extends React.Component<
  RouteComponentProps &
  FormComponentProps & {
  globalConf: GlobalConf
  ModifyExample: {
    [index: string]: any
  }
}
  > {
  public componentWillMount() {
    if (Object.keys(this.props.ModifyExample).length) {
      fields[0].fields.forEach((item: any) => {
        if (item.fieldName === 'isDeleted') {
          item.option.initialValue = this.props.ModifyExample.data[item.fieldName]
        } else {
          item.option.initialValue = this.props.ModifyExample.data[item.fieldName] && this.props.ModifyExample.data[item.fieldName].toString()
        }
      })
    } else {
      this.props.history.push('/appAccept')
    }
  }

  public render() {
    const { globalConf,location } = this.props
    const alreadyFetchGlobalConf = Object.keys(globalConf).length > 0
    const create = location.pathname ==="/appAccept/createApp";
    return (
      <div className="page">
        {create ? <PageTitle name="新建应用" info="可以对应用进行新增" />:<PageTitle name="应用修改" info="可以对应用进行修改" />}
        {alreadyFetchGlobalConf && <FormGenerator onSubmit={this.submit} data={fields} />}
      </div>
    )
  }

  private submit = (fieldValues: any) => {
    fieldValues.status = Number(fieldValues.status)
    fieldValues.isDeleted = Number(fieldValues.isDeleted)
    fieldValues.id = this.props.ModifyExample.data.id
    if(fieldValues.id===null){
      menuService.fetchUserAppUpdate(addAction, fieldValues).then(() => {
        this.props.history.push(`/appAccept`)
      })
    }else{
      menuService.fetchUserAppUpdate(action, fieldValues).then(() => {
        this.props.history.push(`/appAccept`)
      })
    }
  }
}

export default connect({
  globalConf: wholeModel.globalConf,
  ModifyExample: wholeModel.application.modifyExample,
})(withRouter(ModifyExample))
