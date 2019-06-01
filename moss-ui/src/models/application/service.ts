import fetch from '@/util/fetch'
import qs from 'qs'

import { BASE, BUILD, DEPINFO, EVENT, HEALTH, TRACE } from './initialState'
import model from './model'

const application = {
  heapDownloadUrl: (id: string) => `${apiHost}/admin/instances/${id}/actuator/heapdump`,
  fetchServiceList: (
    findType = 0,
    pageNum = 1,
    appName = '',
    pageSize = 8
  ): Promise<ServiceData> => {
    const appNameParam = (appName && appName.length) > 0 ? `appName=${appName}&` : ''
    const findTypeParam = findType ? `&findType=${findType}` : ''
    return fetch({
      method: 'get',
      url: `/admin/applications?${appNameParam}pageNum=${pageNum}&pageSize=${pageSize}${findTypeParam}`,
    }).then((data: any) => {
      return model.Service(data)
    })
  },
  fetchApplicationList: (
    data: {
      appName?: string
      pageNum?: number
      pageSize?: number
    } = {}
  ): Promise<InstanceData> => {
    return fetch({
      method: 'get',
      url: `admin/instances?${qs.stringify(data)}`,
    }).then((resp: any) => {
      return model.index(resp)
    })
  },
  clearApplicationList: () => {
    model.index({}) // reset data
  },
  fetchApplicationMetrics: (id: string): Promise<{ id: string; metrics: MetricsData }> => {
    return fetch<MetricsData>(
      {
        method: 'get',
        url: `admin/instances/${id}/actuator/metricsInfo`,
      },
      { showLoading: false }
    ).then(data => {
      return {
        id,
        metrics: data,
      }
    })
  },

  fetchApplicationDetail: (id: string): Promise<InstanceItem> => {
    model.detail.base(BASE)
    return fetch({
      method: 'get',
      url: `admin/instances/${id}`,
    })
      .then((data: any) => {
        return model.detail.base(data)
      })
      .catch(() => {
        return model.detail.base(BASE)
      })
  },

  fetchApplicationInfo: (id: string): Promise<Info> => {
    model.detail.build(BUILD)
    return fetch({
      method: 'get',
      url: `admin/instances/${id}/actuator/info`,
    })
      .then(data => {
        return model.detail.build(data)
      })
      .catch(() => {
        return model.detail.build(BUILD)
      })
  },

  fetchApplicationEvent: (id: string): Promise<InstanceEvent[]> => {
    model.detail.event(EVENT)
    return fetch({
      method: 'get',
      url: `admin/instances/events/${id}`,
    })
      .then(data => {
        return model.detail.event(data)
      })
      .catch(() => {
        return model.detail.event(EVENT)
      })
  },

  fetchApplicationHealth: (id: string): Promise<HealthData> => {
    model.detail.health(HEALTH)

    return fetch({
      method: 'get',
      url: `admin/instances/${id}/actuator/health`,
    })
      .then(data => {
        return model.detail.health(data)
      })
      .catch(() => {
        return model.detail.health(HEALTH)
      })
  },

  fetchApplicationDepInfo: (id: string): Promise<AppDepInfo> => {
    model.detail.depInfo(DEPINFO)

    return fetch({
      url: `admin/instances/${id}/actuator/appinfo`,
    })
      .then(data => {
        return model.detail.depInfo(data)
      })
      .catch(() => {
        return model.detail.depInfo(DEPINFO)
      })
  },

  // topo
  fetchApplicationTopo: (name: string) => {
    return fetch({
      method: 'get',
      url: `admin/app/trace/${name}`,
    })
  },

  /* 日志列表 */
  fetchApplicationLoggers: (id: string) => {
    return fetch({
      method: 'get',
      url: `admin/instances/${id}/actuator/loggers`,
    }).then((data: any) => {
      return Promise.resolve(model.log(data))
    })
  },

  fetchApplicationGCLog: (id: string, page = 1, size = 100): Promise<GCLogData> => {
    return fetch({
      url: `admin/instances/${id}/actuator/gc?page=${page}&size=${size}`,
    }).then(data => {
      return model.GCLog(data)
    })
  },

  /* 修改日志级别 */
  fetchApplicationModifyLogLevel: (id: string, level: string, options: object) => {
    return fetch({
      data: options,
      method: 'post',
      url: `admin/instances/${id}/actuator/loggers/${level}`,
    }).then(() => {
      application.fetchApplicationLoggers(id)
    })
  },

  /* 获取日志详情 */
  fetchApplicationModifyLogDel: (
    id: string,
    value: string,
    offset?: number,
    showLoading?: boolean
  ) => {
    const requestConf: any = {
      method: 'get',
      responseType: 'Arraybuffer',
      url: `admin/instances/${id}/actuator/${value}logfile`,
    }

    if (offset && offset > 0) {
      requestConf['headers'] = { Range: `bytes=${offset}-` }
    }

    return fetch(requestConf, { fullResponse: true, showLoading }).then((resp: any) => {
      model.LogDel(resp.data)
      return resp
    })
  },

  // 新实例列表
  fetchApplicationExampleList: (
    name: string = '',
    projectName: string = '',
    status: string = '',
    pageNo: number = 1,
    pageSize: number = 10,
    isAccept: boolean = false,
    takeOver: number = -1
  ) => {
    const params: any = { name, projectName, status, pageNo, pageSize, isAccept }
    if (takeOver > -1) {
      params['takeOver'] = takeOver
    }
    return fetch({
      method: 'POST',
      url: `admin/app/searchByPage`,
      data: params,
    }).then((data: any) => {
      data.list = data.list.map((d: any) => {
        if (!d.springApplicationName) {
          d.springApplicationName = ''
        }
        d.isDeleted = d.isDeleted === 1 ? true : false
        d.takeOver = d.takeOver === 1 ? true : false
        return d
      })
      return model.AppSearch.AppSearchList(data)
    })
  },

  // 归属项目
  fetchApplicationSearchToobar: () => {
    return fetch({
      method: 'get',
      url: `admin/app/queryCriteria`,
    }).then((data: any) => {
      return model.AppSearch.SearchToobar(data)
    })
  },

  // 环境配置
  fetchApplicationEnv: (id: any) => {
    return fetch({
      method: 'get',
      headers: {
        Accept: '*/*',
      },
      url: `admin/instances/${id}/actuator/env`,
    }).then((data: any) => {
      return Promise.resolve(model.EnvironConfig(data))
    })
  },

  //  获取JMX菜单列表
  fetchApplicationJmx: (id: string) => {
    return fetch({
      method: 'post',
      url: `admin/instances/${id}/actuator/jolokia/`,
      data: { type: 'list', config: { ignoreErrors: true } },
      headers: {
        Accept: '*/*',
      },
    }).then((data: any) => {
      return Promise.resolve(model.Jmx(data.value))
    })
  },

  // 应用实例同步
  fetchApplicationsynch: () => {
    return fetch({
      method: 'get',
      url: `admin/synch/synch`,
    })
  },

  // Trace
  fetchApplicationTrace: (id: string) => {
    model.Trace(TRACE)

    return fetch({
      method: 'get',
      headers: {
        Accept: '*/*',
      },
      url: `admin/instances/${id}/actuator/httptrace`,
    })
      .then((data: any) => {
        return Promise.resolve(model.Trace(data.traces))
      })
      .catch(() => {
        return model.Trace(TRACE)
      })
  },

  // Thread
  fetchApplicationThread: (id: string, showLoading: boolean) => {
    return fetch(
      {
        url: `admin/instances/${id}/actuator/threaddump`,
        headers: {
          Accept: '*/*',
        },
      },
      { showLoading }
    ).then((data: any) => {
      return Promise.resolve(model.Thread(data.threads))
    })
  },

  // 收藏操作
  fetchApplicationIsNeedCollect: (mailNickname: string, isCollect: boolean, appId: string) => {
    return fetch({
      method: 'get',
      url: `admin/user/isNeedCollect?mailNickname=${mailNickname}&appId=${appId}&isCollect=${isCollect}`,
    })
  },
  // Jar
  fetchApplicationJar: (id: string): Promise<JardepsData> => {
    return fetch<JardepsData>({
      url: `admin/instances/${id}/actuator/jardeps`,
      timeout: 1000 * 100, // 100s : backend api slow in V1 api
    }).then(data => {
      return model.Jar(data)
    })
  },

  // EventLog 事件日志
  fetchEventLogs: (pageNum = 1, pageSize = 8): Promise<EventLogData> => {
    return fetch<EventLogData>({
      url: `admin/instances/events?pageNum=${pageNum}&pageSize=${pageSize}`,
    }).then(data => {
      return model.EventLog(data)
    })
  },

  //注册中心管理
  fetchRegisterCenters: (code: string = '', pageNo: number = 1, pageSize: number = 10) => {
    return fetch({
      method: 'POST',
      url: `admin/registerCenter/list`,
      data: { code, pageNo, pageSize },
    }).then((data: any) => {
      data.list = data.list.map((d: any) => {
        d.isDeleted = d.isDeleted === 1 ? true : false
        return d
      })
      return model.RegisterCenter(data)
    })
  },
  deleteRegisterCenter: (registerCenterId: number) => {
    return fetch({
      url: `admin/registerCenter/delete/${registerCenterId}`,
    })
  },
  createRegisterCenter: (registerCenterData: any) => {
    return fetch({
      method: 'POST',
      url: `admin/registerCenter/add`,
      data: registerCenterData,
    })
  },

  updateRegisterCenter: (registerCenterData: any) => {
    return fetch({
      method: 'POST',
      url: `admin/registerCenter/update`,
      data: registerCenterData,
    })
  },


  // 项目列表
  fetchProjects: (
    name: string = '',
    pageNo: number = 1,
    pageSize: number = 10
  ): Promise<ProjectData> => {
    return fetch<ProjectData>({
      method: 'POST',
      url: `admin/project/list`,
      data: { name, pageNo, pageSize },
    }).then(data => {
      data.list = data.list.map((d: any) => {
        d.isDeleted = d.isDeleted === 1 ? true : false
        return d
      })
      return model.Project(data)
    })
  },
  deleteProject: (projectId: number) => {
    return fetch({
      url: `admin/project/delete/${projectId}`,
    })
  },
  createProject: (projectData: any) => {
    return fetch({
      method: 'POST',
      url: `admin/project/add`,
      data: projectData,
    })
  },
  updateProject: (projectData: any) => {
    return fetch({
      method: 'POST',
      url: `admin/project/update`,
      data: projectData,
    })
  },
  // 作为 owner 的选项
  fetchUserList: () => {
    return fetch({
      url: '/admin/user/list',
    }).then(data => {
      return model.UserList(data)
    })
  },
  fetchRemoteConfigs: (search: string = '', page: number = 1): Promise<RemoteConfigData> => {
    return fetch<RemoteConfigData>({
      method: 'POST',
      url: 'admin/metadata/pageList',
      data: {
        dictName: search,
        pageNo: page,
        pageSize: 8,
      },
    }).then(data => {
      return model.RemoteConfigs(data)
    })
  },
  updateRemoteConfig: (conf: RemoteConfigModel) => {
    return fetch({
      method: 'POST',
      url: 'admin/metadata/updateDictType',
      data: conf,
    })
  },
  updateRemoteConfigOption: (conf: RemoteConfigModel) => {
    return fetch({
      method: 'POST',
      url: 'admin/metadata/updateDictData',
      data: conf,
    })
  },
  createRemoteConfig: (conf: RemoteConfigModel) => {
    return fetch({
      method: 'POST',
      url: 'admin/metadata/addDictType',
      data: conf,
    })
  },
  createRemoteConfigOption: (conf: RemoteConfigModel) => {
    return fetch({
      method: 'POST',
      url: 'admin/metadata/addDictData',
      data: conf,
    })
  },
  deleteRemoteConfig: (id: number) => {
    return fetch({
      url: `/admin/metadata/deleteDictType/${id}`,
    })
  },
  deleteRemoteConfigOption: (id: number) => {
    return fetch({
      url: `/admin/metadata/deleteDictData/${id}`,
    })
  },
  fetchTrajectory: (data?: any) => {
    const defaultData = { pageNo: 1, pageSize: 10 }
    const mergedData = { ...defaultData, ...data }

    return fetch({
      method: 'POST',
      url: `/admin/app/mqTrace`,
      data: mergedData,
    }).then(data => {
      return model.Trajectory(data)
    })
  },
  fetchTotalRequestNum: (instanceId: string, time: string, groupByTime: string) => {
    return fetch({
      url: `admin/app/totalRequestNum?instanceId=${instanceId}&time=${time}&groupByTime=${groupByTime}`,
    }).then(data => {
      return model.TotalRequestNum(data)
    })
  },
  fetchRequestCostTime: (id: string, time: string, groupByTime: string) => {
    return fetch({
      url: `admin/app/requestCostTime?time=${time}&instanceId=${id}&groupByTime=${groupByTime}`,
    }).then(data => {
      return model.RequestCostTime(data)
    })
  },
}

export default application
