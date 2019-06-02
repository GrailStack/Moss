import noticeService from '@/models/notification/service'
import { history } from '@/pages/App'
import { message } from 'antd'
import axios, { AxiosError, AxiosRequestConfig, AxiosResponse } from 'axios'

axios.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
  const registerCenter = localStorage.getItem('registerCenter')
  if (token) {
    config.headers.token = token
  }
  if (registerCenter) {
    config.headers.registerSource = registerCenter
  }

  return config
})

axios.interceptors.response.use(response => {
  if (response.data && response.data.msgCode === '401') {
    history.push('/login')
  }
  return response
})

const IGNORE_ERROR = ['actuator/metricsInfo', 'actuator/infologfile', 'actuator/errorlogfile']
axios.interceptors.response.use(
  config => config,
  error => {
    if (error.config && error.config.method === 'options') {
      // do noting
    } else if (error.response && error.response.status >= 500) {
      if (IGNORE_ERROR.some(path => error.config.url.indexOf(path) !== -1)) {
        return
      }

      console.log(error)
      message.error(
        `${error.response.statusText} (${error.response.status}) : ${error.request.responseURL} `
      )
    } else if ((error.response && error.response.status === 408) || error.code === 'ECONNABORTED') {
      message.error(`请求超时 : ${error.config.url}`)
    } else if (error.response && error.response.status === 404) {
      // 日志收集到哪 !
    }
    return Promise.reject(error)
  }
)

const dataFormat = ['code', 'data', 'msgCode', 'msgContent']

const formatResponseData = (data: { [index: string]: any }) => {
  const dataType = typeof data
  if (dataType === 'undefined' || data === null) {
    data = {}
  } else if (dataType === 'object') {
    const keys = Object.keys(data)
    const tempArr = keys.filter(key => dataFormat.indexOf(key) === -1)
    if (keys.length === dataFormat.length && tempArr.length === 0) {
      return data
    }
  } else if (dataType === 'string' && !data) {
    data = {}
  }

  return {
    code: 200,
    data,
    msgCode: null,
    msgContent: 'success',
  }
}

export default <T>(
  options: AxiosRequestConfig,
  option: {
    showSuccess?: boolean
    showFail?: boolean
    showLoading?: boolean
    fullResponse?: boolean
  } = {}
): Promise<T> => {
  const { showSuccess = false, showFail = false, showLoading = true, fullResponse = false } = option
  const defaultConfig = {
    baseURL: apiHost,
    headers: {
      'Content-Type': 'application/json',
      'X-Requested-With': 'XMLHttpRequest',
    },
    responseType: 'json',
    timeout: 10000,
    validateStatus: (status: number) => status >= 200 && status < 300,
  }
  if (showLoading) {
    noticeService.loading(1)
  }
  return (
    axios({
      ...defaultConfig,
      ...options,
    })
      // @ts-ignore
      .finally(() => {
        if (showLoading) {
          noticeService.loading(-1)
        }
      })
      .then((response: AxiosResponse) => {
        const responseData = formatResponseData(response.data)
        const { code, data, msgContent, msgCode } = responseData

        if (code === 200) {
          if (showSuccess) {
            noticeService.addNotice({
              message: msgContent,
              type: 'success',
            })
          }
          if (msgCode) {
            return Promise.reject({
              response: {
                data: msgContent,
                status: code || msgCode,
              },
            })
          }
          if (fullResponse) {
            return Promise.resolve(response)
          } else {
            return Promise.resolve(data)
          }
        } else {
          return Promise.reject({
            response: {
              data: msgContent,
              status: code || msgCode,
            },
          })
        }
      })
      .catch((error: AxiosError) => {
        const finalError: {
          [index: string]: any
        } = {}

        if (error.response) {
          finalError.errorCode = error.response.status
          finalError.errorMsg = error.response.data
        } else if (error.request) {
          finalError.errorCode = 'none'
          finalError.errorMsg = error.request
        } else {
          finalError.errorCode = 'none'
          finalError.errorMsg = error.message
        }
        if (showFail) {
          const { errorMessage, errorCode } = finalError
          noticeService.addNotice({
            message: `${errorMessage}(${errorCode})`,
            type: 'error',
          })
        }
        return Promise.reject(finalError)
      })
  )
}
