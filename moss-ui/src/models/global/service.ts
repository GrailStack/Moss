import fetch from '@/util/fetch'
import { wholeModel } from '@/util/store'

export default {
  fetchGlobalConf: (): Promise<GlobalConf> => {
    return fetch<GlobalConf>(
      {
        url: `admin/metadata/list`,
      },
      { showLoading: false }
    ).then(data => {
      return wholeModel.globalConf(data)
    })
  },
}
