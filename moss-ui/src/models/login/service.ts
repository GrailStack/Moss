import { wholeModel } from '@/util/store'
import fetch from '@/util/fetch'

export default {
  login: (body: any): Promise<LoginData> => {
    return fetch<LoginData>({
      method: 'POST',
      url: `/admin/login`,
      data: body,
    }).then(data => {
      return wholeModel.login(data)
    })
  },
}
