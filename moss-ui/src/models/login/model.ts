import { gluer } from 'glue-redux'

const login = gluer(
  (data?: LoginData) => {
    localStorage.setItem('token', (data && data.token) || '')
    localStorage.setItem('userName', (data && data.userName) || '')
    return data
  },
  {
    userName: localStorage.getItem('userName') || 'anonymous',
    token: localStorage.getItem('token'),
  }
)

export default login
