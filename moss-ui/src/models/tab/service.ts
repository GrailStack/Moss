import { getMenuInfo } from '@/models/menu/service'
import { referToState, wholeModel } from '@/util/store'

const model = wholeModel.tab

export default {
  addTab: (source: { title: string; force?: boolean }) => {
    const state = referToState(model)
    const { title, force } = source
    const info = getMenuInfo(title)

    if (info) {
      const isOpened = state.indexOf(title) > -1
      let final

      if (isOpened) {
        final = state
      } else if (info.isExternal || force || state.length < 2) {
        final = state.concat(title)
      } else {
        const internalInfo = getMenuInfo(state[1])

        state.splice(1, internalInfo.isExternal ? 0 : 1, title)
        final = [...state]
      }
      return model(final)
    }
    return model([...state])
  },
  removeTab: (source: { title: string; force?: boolean }) => {
    const state = referToState(model)
    const { title } = source
    return model(state.filter((tabTitle: string) => tabTitle !== title))
  },
}
