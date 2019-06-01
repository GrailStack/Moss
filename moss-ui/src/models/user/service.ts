import fetch from '@/util/fetch'

import model from './model'

export default {
  fetchUserList: (
    username: string = '',
    pageNo: number = 1,
    name: string = '',
    pageSize: number = 10
  ) => {
    debugger
    return fetch({
      method: 'POST',
      url: `admin/user/pageList`,
      data: { username, name, pageNo, pageSize },
    }).then((data: any) => {
      data.list = data.list.map((d: any) => {
        d.isDeleted = d.isDeleted === 1 ? true : false
        return d
      })
      return model.User(data)
    })
  },
  deleteUser: (userId: number) => {
    return fetch({
      url: `admin/user/delete/${userId}`,
    })
  },

  
  createUser: (userData: any) => {
    return fetch({
      method: 'POST',
      url: `admin/user/add`,
      data: userData,
    })
  },

  updateUser: (userData: any) => {
    return fetch({
      method: 'POST',
      url: `admin/user/update`,
      data: userData,
    })
  },
}
