import FormGenerator from '@/components/FormGenerator'
import PageTitle from '@/components/PageTitle'
import fetch from '@/util/fetch'
import React from 'react'

import data from './data'

const { fields, action, method, confirm } = data

class CodeGenerate extends React.Component {
  public state = {
    current: 0,
  }

  public render() {
    return (
      <div className="page">
        <PageTitle name="代码生成" info="快速生成最佳实践的代码工程，提高开发效率" />
        <FormGenerator onSubmit={this.submit} confirm={confirm} data={fields} />
      </div>
    )
  }

  private submit = (fieldValues: any) => {
    if (fieldValues.builderProperties && !fieldValues.builderProperties.dbType) {
      delete fieldValues.builderProperties
    }
    const dbUrl =
      fieldValues && fieldValues.builderProperties && fieldValues.builderProperties.dbUrl
    if (dbUrl) {
      if (!dbUrl.startsWith('jdbc')) {
        fieldValues.builderProperties.dbUrl = `jdbc:mysql://${fieldValues.builderProperties.dbUrl}`
      }
    }

    fetch({
      method,
      url: `${action}${encodeURIComponent(JSON.stringify(fieldValues))}`,
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
      responseType: 'blob',
    }).then((response: any) => {
      const url = window.URL.createObjectURL(new Blob([response]))
      const link = document.createElement('a')
      link.href = url
      link.setAttribute('download', 'init-project.zip')
      document.body.appendChild(link)
      link.click()
    })
  }
}

export default CodeGenerate
