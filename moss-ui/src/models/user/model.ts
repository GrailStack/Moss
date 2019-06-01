import { gluer } from 'glue-redux'

const User = gluer((data: UserData): UserData => {
  return data
}, [])

const userMgmt = {
  User,
}

export default userMgmt
