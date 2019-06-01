import { REGEXP } from '@/util/common'
import { wholeModel } from '@/util/store'
import fetch from '@/util/fetch'

const menuHash: { [key: string]: MenuData } = {}

export function setMenuHash(menus: MenuData[]) {
  Array.isArray(menus) &&
    menus.forEach(menu => {
      const { src } = menu

      if (src) {
        menu.isExternal = REGEXP.url.test(src)
        menuHash[menu.key] = menu
      }
      if (menu.subMenu) {
        setMenuHash(menu.subMenu)
      }
    })
}

export const getMenuInfo = (key: string): MenuData => {
  return menuHash[key] || ({} as MenuData)
}

export default {
  fetchUserMenu: (): Promise<MenuData[]> => {
    return fetch<MenuData[]>({
      method: 'get',
      url: `admin/user/menu/`,
    }).then(data => {
      setMenuHash(data)
      return wholeModel.menu(data)
    })
  },

  fetchUserMenuAll: (): Promise<MenuData[]> => {
    return fetch<MenuData[]>({
      method: 'get',
      url: `admin/menu/all`,
    }).then(data => {
      setMenuHash(data)
      return data
    })
  },

  fetchUserAppUpdate: (url: string, dataObj: object) => {
    return fetch({
      method: 'post',
      url,
      data: dataObj,
    })
  },

  fetchUserMenuAddOrUpdate: (url: string, method: string, dataObj: object) => {
    return fetch({
      url,
      method,
      data: dataObj,
    })
  },

  fetchUserMenuDelete: (id: string) => {
    return fetch({
      url: `admin/menu/delete/${id}`,
      method: 'get',
    })
  },
}
