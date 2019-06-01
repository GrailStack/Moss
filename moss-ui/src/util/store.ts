import React from 'react'
import { destruct } from 'react-glux'
import { combineReducers, createStore } from 'redux'
import { devToolsEnhancer } from 'redux-devtools-extension'

import application from '@/models/application/model'
import dashboard from '@/models/dashboard/model'
import login from '@/models/login/model'
import menu from '@/models/menu/model'
import user from '@/models/user/model'

import notification from '@/models/notification/model'
import selectNode from '@/models/selectNode/model'
import tab from '@/models/tab/model'
import globalConf from '@/models/global/model'

const wholeModel = {
  dashboard,
  application,
  menu,
  notification,
  tab,
  login,
  selectNode,
  globalConf,
  user,
}

const store =
  ENV === 'production' ? createStore(() => ({})) : createStore(() => ({}), devToolsEnhancer({}))
window.Store = store
const { reducers, connect, referToState } = destruct(store)(wholeModel)
store.replaceReducer(combineReducers(reducers))

if (ENV !== 'production') {
  console.log('store状态结构：', referToState(wholeModel))
  window.React = React
}
export { connect, wholeModel, referToState }
