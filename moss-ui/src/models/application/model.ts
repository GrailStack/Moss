import { gluer } from 'glue-redux'

import {
  APPSEARCHLIST,
  BASE,
  BUILD,
  DEPINFO,
  ENVIRONCONFIG,
  EVENT,
  EVENTLOG,
  GCLOG,
  HEALTH,
  JAR,
  JMX,
  JVM,
  LOG,
  LOGDEL,
  MODIFY,
  REMOTECONFIG,
  REQUESTCONSTTIME,
  SEARCHTOOBAR,
  SERVICE,
  THREAD,
  TOTALREQUESTNUM,
  TRACE,
  TRAJECTORY,
  USERLIST,
} from './initialState'

function mapApplicationData(app: InstanceItem) {
  return {
    id: app.id,
    group: app.registration.metadata.GROUP,
    name: app.registration.name,
    ownerName: app.ownerName,
    attachType: 1,
    projectKey: app.projectKey,
    status: app.statusInfo.status,
    url: app.registration.serviceUrl,
    version: app.version,
    takeOver: app.takeOver,
    ...app,
  }
}

// 应用信息-首页
const index = gluer((data: InstanceData) => {
  const { currentPage, totalCount, totalPage, list = [] } = data
  return {
    list: list.map(mapApplicationData),
    currentPage,
    pageSize: 8,
    totalPage,
    totalCount,
  }
}, {})

// 应用信息-详情
const detail = {
  base: gluer(BASE),
  build: gluer(BUILD),
  event: gluer(EVENT),
  health: gluer(HEALTH),
  depInfo: gluer(DEPINFO),
}

// 应用信息-日志
const log = gluer((data: any) => {
  const { levels, loggers } = data
  return {
    levels,
    loggers,
  }
}, LOG)

// 日志详情
const LogDel = gluer((data: any, state: any) => {
  return {
    data: data + state.data,
  }
}, LOGDEL)

// appSearch
const AppSearch = {
  AppSearchList: gluer(APPSEARCHLIST),
  SearchToobar: gluer(SEARCHTOOBAR),
}

// GCLog
const GCLog = gluer((data: GCLogData, state: GCLogData) => {
  const newLog = { log: state.log.concat(data.log) }
  return { ...data, ...newLog }
}, GCLOG)
// JVM
const Jvm = gluer((data: any) => {
  return {
    data,
  }
}, JVM)

// EnvironConfig
const EnvironConfig = gluer((data: any) => {
  return {
    data,
  }
}, ENVIRONCONFIG)

// JMX
const Jmx = gluer((data: any) => {
  return {
    data,
  }
}, JMX)

// 接入管理
const modifyExample = gluer((data: any) => {
  return { data }
}, MODIFY)

// Thread
const Thread = gluer((data: []) => {
  return { data }
}, THREAD)

// Trace
const Trace = gluer((data: any) => {
  return {
    data,
  }
}, TRACE)

// Jar
const Jar = gluer((data: JardepsData) => {
  return { data }
}, JAR)

// serviceManage 服务管理
const Service = gluer((data: ServiceData): ServiceData => {
  return data
}, SERVICE)

// 事件日志
const EventLog = gluer((data: EventLogData): EventLogData => {
  return data
}, EVENTLOG)

const Project = gluer((data: ProjectData): ProjectData => {
  return data
}, [])


const RegisterCenter = gluer((data: RegisterCenterData): RegisterCenterData => {
  return data
}, [])


const UserList = gluer((data: UserModel[]): UserModel[] => {
  return data
}, USERLIST)

const RemoteConfigs = gluer((data: RemoteConfigModel[]): RemoteConfigModel[] => {
  return data
}, REMOTECONFIG)

const Trajectory = gluer((data: TrajectoryData): TrajectoryData => {
  return data
}, TRAJECTORY)

const TotalRequestNum = gluer((data: TotalRequestNumData): TotalRequestNumData => {
  return data
}, TOTALREQUESTNUM)

const RequestCostTime = gluer((data: RequestCostTImeData): RequestCostTImeData => {
  return data
}, REQUESTCONSTTIME)

const application = {
  log,
  LogDel,
  detail,
  GCLog,
  index,
  AppSearch,
  Jvm,
  EnvironConfig,
  Jmx,
  modifyExample,
  Trace,
  Thread,
  Jar,
  Service,
  EventLog,
  Project,
  RegisterCenter,
  UserList,
  RemoteConfigs,
  Trajectory,
  TotalRequestNum,
  RequestCostTime,
}

export default application
