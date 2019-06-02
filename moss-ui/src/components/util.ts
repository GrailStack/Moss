import { assert } from 'chai'

import { qsParse } from '../util'

export const interpolate = (str: string, params: object) => {
  const names = Object.keys(params)
  const values = (Object as any).values(params).map((v: any) => (v === '' ? undefined : v))
  // tslint:disable-next-line
  return new Function(...names, `return \`${str}\`;`)(...values)
}

export const compileTemplateLiterals = (stringTemplate: string, props: object) => {
  try {
    const str = interpolate(stringTemplate, props)
    return str.indexOf('undefined') > -1 ? null : str
  } catch (err) {
    return null
  }
}

export const compareValue = (left: any, operator?: string, right?: any) => {
  if (operator && right) {
    try {
      ;(assert as any)[operator](left, right)
      return true
    } catch (e) {
      return false
    }
  } else {
    return !(!left || (Array.isArray(left) && !left.length))
  }
}

export const checkDependencies = (
  source: { [key: string]: any },
  dependencies?: Dependencies
): boolean => {
  if (dependencies) {
    return ![].concat(dependencies as any).some((dependency: Dependency) => {
      if (Array.isArray(dependency)) {
        const _dependency = [...dependency]

        _dependency[0] =
          typeof source === 'function' ? source(_dependency[0]) : source[_dependency[0]]
        return !compareValue(...(_dependency as [string, string?, string?]))
      } else {
        const value = typeof source === 'function' ? source(dependency) : source[dependency]

        return value ? false : compileTemplateLiterals(dependency, source) !== 'true'
      }
    })
  }
  return true
}

export function getInitialValue(searchBar: FieldSetData, querystring: string) {
  const { fields: oFields } = searchBar

  if (!oFields) {
    return searchBar
  }

  const qs = qsParse(querystring)
  const fields = oFields.map(field => {
    const initialValue = qs[field.fieldName]

    const fieldValue = {
      ...field,
    }

    if (initialValue) {
      fieldValue.option = {
        ...fieldValue.option,
        initialValue,
      }
    }
    return fieldValue
  })

  return {
    ...searchBar,
    fields,
  }
}

export const exportToJson = (objectData: any, filename: string) => {
  let contentType = 'application/json;charset=utf-8;'
  if (window.navigator && window.navigator.msSaveOrOpenBlob) {
    const blob = new Blob([decodeURIComponent(encodeURI(JSON.stringify(objectData)))], {
      type: contentType,
    })
    navigator.msSaveOrOpenBlob(blob, filename)
  } else {
    const a = document.createElement('a')
    a.download = filename
    a.href = 'data:' + contentType + ',' + encodeURIComponent(JSON.stringify(objectData))
    a.target = '_blank'
    document.body.appendChild(a)
    a.click()
    document.body.removeChild(a)
  }
}
