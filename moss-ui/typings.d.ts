declare module '*.css'
declare module '*.png'
declare module '*.svg'
declare module '*.gif'
declare module '*.less'
declare module '@antv/g6'
declare module 'react-json-tree'
declare module '@antv/g6/plugins/layout.dagre'
declare module '@antv/data-set'
declare module 'filesize'
declare module 'react-transition-group'
declare module '@antv/data-set/lib/data-set'
declare module 'pretty-bytes'
declare module 'react-countup'
declare module 'react-highlight-words'
declare module 'moment/locale/zh-cn'
declare module 'qs'

declare const ENV: 'development' | 'production'
declare const theme: any
declare const apiHost: string

// Global
interface Window {
  Store: any
  React: any
  Promise: Promise<any> | null
}

interface Promise<T> {
  prototype: any
}

// Common

interface KVType {
  [key: string]: string | number | boolean
}

// Common API Return data structure
interface APIData<T> {
  code: number
  data: T
  msgCode: null | number
  msgContent: string
}

// Pagination
interface PaginationData {
  currentPage: number
  totalCount: number
  totalPage: number
}

// Login
interface LoginData {
  token: string
  tokenExpired: number
  userName: string
}

// STATUS
type STATUS = 'UP' | 'DOWN' | 'OFFLINE' | 'UNKONW'

// enum interface
type NoticeType = 'info' | 'error' | 'success'

// fetch Request CustomError
interface RequestError {
  errorCode: number
  errorMsg: {
    error: string
    message: string
    path: string
    status: number
    timestamp: number
  }
}
// /admin/dashboard/basic
interface DashboardBasicData {
  appNum: number
  downNum: number
  instanceNum: number
  myAppNum: number
  myInstanNum: number
  projectNum: number
  myProjectNum: number
  department?: string
  role: string
}

// admin/instances/events?pageNum=1&pageSize=8
interface EventLogData extends PaginationData {
  list: EventLogItem[]
}

interface EventLogItem {
  instance: string
  registration: {
    healthUrl: string
    managementUrl: string
    metadata: {
      'gray.enable': string
      'hazelcast.host': string
      'hazelcast.port': string
      'hazelcast.version': string
      'management.port': string
      'management.url': string
    }
    name: string
    serviceUrl: string
    source: string
  }
  timestamp: number
  type: string
  version: number
}

// Jar admin/instances/e9a9ea7cc0b7/actuator/jardeps
interface JardepsData {
  pomInfos: PomInfo[]
  springBootVersion: string
  springCloudVersion: string
  summerframeworkVersion: string
}

interface PomInfo {
  artifactId: string
  dependencies: Dependence[]
  groupId: string
  location: string
  size: number
  version: string
}

interface Dependence {
  artifactId: string
  groupId: string
  scope?: string
  version: string
}

interface ActionDataIsObject {
  type: string
  data: {
    [index: string]: any
  }
}

// action map
interface AsyncActionMap {
  ACTION: string
  SUCCESS: string
  FAILED: string
}

// Base callback function
interface BCF {
  (err?: any, result?: any): void
}

// Base Action Shape
interface BAS<T> {
  cb?: BCF
  data: T
  type: string
}

// Action Input
declare namespace AI {}

// Action Shape
declare namespace AS {}

// Base Action Function
interface BAF<I, R> {
  (input: I, cb?: BCF): R
}

// action function
declare namespace ActionFunction {
  interface Notice {
    (type: NoticeType, message: string, duration?: number): {
      data: {
        type: NoticeType
        message: string
        duration?: number
      }
      type: string
    }
  }
}

interface INotification {
  notices: INotice[]
  loading: number
  dialog?: DialogProps
}

interface INotice {
  type?: string
  message: string
  duration?: number
  count?: number
  removeNotice?: Function
}

interface DialogProps {
  type?: string
  message: string
  duration?: number
}

// admin/applications?appName=HALO-ADMIN-2.0&pageNum=1&pageSize=8
interface ServiceData extends PaginationData {
  list: ServiceItem[]
}

interface ServiceItem {
  starsNum: number
  twinkle: boolean
  attachType?: number
  buildVersion?: string
  instanceNum: number
  instances: InstanceItem[]
  name: string
  ownerName?: string
  projectKey?: string
  projectName?: string
  status: STATUS
  statusTimestamp: number
  takeOver: boolean
}

interface InstanceData extends PaginationData {
  list: InstanceItem[]
  pageSize?: number
}

interface InstanceItem {
  buildVersion?: string
  endpoints?: EndPoint[]
  id: string
  info: { [key: string]: string }
  registered: boolean
  registration: {
    healthUrl: string
    managementUrl: string
    metadata: { [key: string]: string }
    name: string
    serviceUrl: string
    source: string
    icon: string
  }
  statusInfo: {
    status: STATUS
    details: { [key: string]: string }
  }
  url?: string
  group?: string
  name: string
  ownerName?: string
  projectKey?: string
  instanceNum: string
  status: STATUS
  statusTimestamp: number
  takeOver: boolean
  tags?: any
  unsavedEvents?: any
  attachType: number
  version: number
}

interface EndPoint {
  id: string
  url: string
}

// admin/instances/965ce52d71e1/actuator/metricsInfo
interface MetricsData {
  gcPsMarksweepCount: string
  gcPsMarksweepTime: string
  gcPsScavengeCount: string
  gcPsScavengeTime: string
  heapCommitted: string
  heapInit: string
  heapMax: string
  jvmMemoryUsedHeap: string
  jvmMemoryUsedNonHeap: string
  jvmThreadslive: string
  nonheapCommitted: string
  processors: string
  systemloadAverage: string
}

// admin/instances/965ce52d71e1/actuator/health
interface HealthData {
  details: {
    db?: { status: STATUS; details: HealthDetail }
    description?: string
    discoveryComposite?: { status: STATUS; details: HealthDetail }
    diskSpace?: { status: STATUS; details: HealthDetail }
    hystrix?: { status: STATUS }
  }
  status: STATUS
}

interface HealthDetail {
  [key: string]: string
}

// admin/instances/965ce52d71e1/actuator/info
interface Info {
  artifactId: string
  git: Git
  groupId: string
  version: string
}

interface Git {
  branch: string
  build: {
    host: string
    time: number
    user: { name: string; email: string }
    version: string
  }
  closest: { tag: { commit: { count: string }; name: string } }
  commit: any
  dirty: string
  remote: { origin: { url: string } }
  tags: string
}

// admin/instances/7e44bfad832e/actuator/appinfo

interface AppDepInfo {
  summerframeworkVersion: string
  using: [{ [key: string]: string }]
  appName: string
  springBootVersion: string
  springCloudVersion: string
}

// admin/instances/events/965ce52d71e1
interface InstanceEvent {
  instance: string
  registration: Registration
  timestamp: number
  type: string
  version: number
  statusInfo?: {
    status: STATUS
  }
}

interface Registration {
  healthUrl: string
  managementUrl: string
  metadata: { [key: string]: string }
  name: string
  serviceUrl: string
  source: string
}

// admin/instances/00e8c3185557/actuator/gc?page=1&size=100

interface GCLogData {
  page: number
  size: number
  log: string[]
  total: number
  error?: any
}

// admin/user/menu/

interface MenuData {
  gmtCreate?: string
  gmtModified?: string
  icon: string
  id?: string
  key: string
  name?: string
  parentId?: string
  parentIds?: string
  roles?: string
  sort?: string
  src?: string
  subMenu: MenuData[]
  title: string
  url?: string
  isExternal: boolean
}

// RegisterCenter
interface RegisterCenterData extends PaginationData {
  list: RegisterCenterModel[]
  id: number
}

interface RegisterCenterModel {
  code: string
  desc: string
  gmtModified: string
  id: number
  isDeleted: number
  url: string
}

// Project
interface ProjectData extends PaginationData {
  list: ProjectModel[]
  type?: string
  id: number
}

interface ProjectModel {
  cname: string
  description: string
  gmtModified: string
  id: number
  isDeleted: number
  key: string
  name: string
  ownerId: string
  ownerName: string
}

// UserList
interface UserModel {
  id: number
  username: string
  name: string
  email: string
}

interface ListData<T> {
  pageNum?: number
  pageSize?: number
  total?: number
  pages?: number
  list?: T[]
  currentPage: number
  totalCount: number
}

// /admin/dashboard/report
interface ReportData {
  [key: string]: any
  count: number
  springCloudVersions: [ReportItem]
  sprintBootVersions: [ReportItem]
  summerFrameworkVersions: [ReportItem]
  takeOver: [ReportItem]
}

interface ReportItem {
  name: string
  count: number
}

interface ApplicationDetail {
  base: InstanceItem
  build: Info
  event: InstanceEvent[]
  health: HealthData
  depInfo: AppDepInfo
}

interface ApplicationMetricsData {
  timestamp: number
  [key: string]: any
}

// 数据字典
interface RemoteConfigData extends PaginationData {
  list: RemoteConfigModel[]
}

interface RemoteConfigModel {
  editing?: boolean
  new?: boolean
  dictCode: string
  dictDataModelList: Array<RemoteConfigModel>
  dictName?: string
  gmtCreate?: number
  gmtModified?: number
  id?: number
  isDeleted: number
  status?: number
  itemDesc?: string
  itemName?: string
  itemSort?: number
  itemValue?: string
  [key: string]: string
}

// Global Config - 数据字典编辑的配置项
interface GlobalConf {
  frameworkVerison: GlobalConfItem[]
  springBootVersion: GlobalConfItem[]
  springCloudVersion: GlobalConfItem[]
  registerCenter: GlobalConfItem[]
}

interface GlobalConfItem {
  name: string
  value: string
  desc: string
}

// 消息轨迹
interface TrajectoryData extends PaginationData {
  list: Trajectory[]
}

interface Trajectory {
  timestamp: string
  clientIp: string
  applicationName: string
  messageId: string
  type: string
  node: string
  connection: string
  vhost: string
  user: string
  channel: string
  exchange: string
  queue: string
  routingKeys: string
  properties: string
  payload: string
  success: string
  payload: string
}

interface TotalRequestNumData {}

// 请求耗时

interface RequestCostTImeData {}

type FieldOption =
  | {
      value: string
      key?: string
      label?: string
      disabled?: boolean
      className?: string
    }
  | string

type FieldOptions = Array<FieldOption>
type FieldOptionsInput = string | FieldOptions | Function
type Dependency = string | Array<any>
type Dependencies = string | Array<Dependency>

interface FieldControl {
  name: string
  options?: FieldOptionsInput
  optionsFilter?: any
  extend?: FieldOptions | Function
  value?: any
  [key: string]: any
}

interface FieldData {
  fieldName: string
  label?: string
  tip?: string
  option?: {
    initialValue?: string | string[] | boolean | Function
    rules?: any[]
  }
  control: FieldControl
  dependencies?: Dependencies
  hidden?: boolean
}

interface FieldSetData {
  submit?: boolean
  title?: string
  fields?: FieldData[]
  dependencies?: Dependencies
}

type OrderServiceType =
  | 'order'
  | 'orderTran'
  | 'refundOrder'
  | 'refundTran'
  | 'repeatTran'
  | 'reconciliation'
type WalletUserInfoServiceType = 'dashboard' | 'user' | 'card' | 'order' | 'feature' | 'faq'

interface SearchBarConstructorData {
  onSubmit: (value: any, clear?: boolean) => void
  data: FieldSetData
  extend?: JSX.Element
}

interface PageConstructorData {
  columns: { [key: string]: any }[]
  searchBar?: FieldSetData
  controller?: any
  pageTitle?: string
  model: any
  service: any
  noExport?: boolean
}

interface ControllerData {
  title: string
  onClick: () => void
  icon: string
}
interface ColumnOperate {
  dependencies?: string
  linkTo?: String
  doAction?: {
    disabled?: string
    action: string
    params?: any
  }
  title: string
  then?: any
}

// User
interface UserData extends PaginationData {
  list: UserMgmtModel[]
  id: number
}

interface UserMgmtModel {
  username: string
  name: string
  gmtModified: string
  id: number
  isDeleted: number
  email: string
}
