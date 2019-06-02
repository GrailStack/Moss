import { gluer } from 'glue-redux'

const globalConf = gluer((data?: GlobalConf) => {
  if (data && data.registerCenter && Array.isArray(data.registerCenter)) {
    const localRegisterCenter = localStorage.getItem('registerCenter')
    const localRegisterCenterInList = data.registerCenter.find(c => {
      return c.value === localRegisterCenter
    })
    if (!localRegisterCenterInList) {
      localStorage.setItem('registerCenter', '')
    }
  }
  return { ...data }
}, {})

export default globalConf
