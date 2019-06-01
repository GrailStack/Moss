import { referToState, wholeModel } from '@/util/store'

let currentCount = 0
const notification = {
  addNotice: (source: { message: string; type: string; count?: number }) => {
    const state = referToState(wholeModel.notification.notices)
    return wholeModel.notification.notices([...state, { ...source, count: ++currentCount }])
  },
  loading: (data: number) => wholeModel.notification.loading(data),
  removeNotice: (source: { count?: number }) => {
    const state = referToState(wholeModel.notification.notices)
    if (state.length) {
      const newNotices = [...state]

      state.forEach((notice: INotice, index: number) => {
        if (notice.count === source.count) {
          newNotices.splice(index, 1)
        }
      })
      return wholeModel.notification.notices(newNotices)
    }
  },
}
export default notification
