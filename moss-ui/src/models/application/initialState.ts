import { string } from 'prop-types'

export const BASE = {
  name: 'loading...',
}

export const BUILD = {}

export const EVENT = []

export const HEALTH = {}

export const DEPINFO = {
  summerframeworkVersion: '',
  using: [],
  appName: '',
  springBootVersion: '',
  springCloudVersion: '',
}

export const LOG = {
  levels: [],
  loggers: {},
}

export const LOGDEL = {
  data: '',
}

export const APPSEARCHLIST = {
  data: {},
}

export const SEARCHTOOBAR = {
  data: {},
}

export const JVM = {
  data: {},
}

export const ENVIRONCONFIG = {
  data: {},
}

export const JMX = {
  data: {},
}

export const TRACE = []

export const THREAD = {}

export const JAR = {}

export const MODIFY = {}

export const SERVICE: ServiceData = {
  currentPage: 0,
  totalCount: 0,
  totalPage: 0,
  list: [],
}

export const GCLOG: GCLogData = {
  page: 0,
  size: 0,
  log: [],
  total: 0,
}

export const EVENTLOG: EventLogData = {
  currentPage: 0,
  totalCount: 0,
  totalPage: 0,
  list: [],
}

export const PROJECT: ProjectModel = {} as ProjectModel

export const USERLIST: UserModel[] = []

export const REMOTECONFIG: RemoteConfigModel[] = []

export const TRAJECTORY: TrajectoryData = { log: [] as Trajectory[] } as TrajectoryData
export const TOTALREQUESTNUM: TotalRequestNumData = {}

export const REQUESTCONSTTIME: RequestCostTImeData = {}
