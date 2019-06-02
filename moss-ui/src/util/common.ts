import { EventBus, EVENTS } from '@/util/eventbus'

export const ROUTER_MAP_RAW: Array<Array<RegExp | string>> = [
  ['/dashboard(\\B|\\b)', '总览'],
  ['/appAccept(\\B|\\b)', '系统管理 / 接入管理 / 实例列表'],
  ['/appAccept/createApp(\\B|\\b)', '编辑'],
  ['/appAccept/modifyExample(\\B|\\b)', '接入应用修改'],
  ['/serviceManage(\\B|\\b)', `服务治理 / 服务管理`],
  ['/eventLog(\\B|\\b)', '事件日志'],
  ['/list(\\B|\\b)', '服务治理 / 服务实例'],
  ['/list/(\\B|\\b)', '详情'],
  ['/list/.*/log(\\B|\\b)', '日志'],
  ['/list/.*/log/logDel(\\B|\\b)', '日志详情'],
  ['/list/.*/EnvironConfig(\\B|\\b)', '环境配置'],
  ['/list/.*/Jmx(\\B|\\b)', 'JMX'],
  ['/list/.*/Jvm(\\B|\\b)', 'JVM'],
  ['/list/.*/Trace(\\B|\\b)', 'Trace'],
  ['/list/.*/Thread(\\B|\\b)', '线程'],
  ['/list/.*/Jar(\\B|\\b)', '依赖'],
  ['/list/.*/gclog(\\B|\\b)', 'GC Log'],
  ['/menuManage(\\B|\\b)', '系统管理 / 菜单管理'],
  ['/menuManage/new(\\B|\\b)', '新增菜单'],
  ['/menuManage/edit(\\B|\\b)', '菜单修改'],
  ['/menuManage/modifyExample(\\B|\\b)', '接入应用修改'],
  ['/switchManage(\\B|\\b)', `开关中心 / 开关管理`],
  ['/switchPushLog(\\B|\\b)', `开关中心 / 推送记录`],
  ['/project(\\B|\\b)', `项目管理`],
  ['/project/createProject(\\B|\\b)', `编辑`],
  ['/remoteConfig(\\B|\\b)', `数据字典`],
]
  .reverse()
  .map((router: Array<RegExp | string>) => {
    router[0] = new RegExp(router[0])
    return router
  })

// @ts-ignore
export const ROUTER_MAP: Map<RegExp, string> = new Map(ROUTER_MAP_RAW)

let SIDEBAR_COLLAPSED = false
EventBus.on(EVENTS.COLLAPSE_CHANGE, (collapsed: boolean) => {
  SIDEBAR_COLLAPSED = collapsed
})

const SIDE_BAR_WIDTH_COLLAPSED = 80
const SIDE_BAR_WIDTH = 256

const CONSTANT = {
  SIDE_BAR_WIDTH_COLLAPSED,
  SIDE_BAR_WIDTH,
  SIDE_BAR_WIDTH_DYNAMIC: () => {
    return SIDEBAR_COLLAPSED ? SIDE_BAR_WIDTH_COLLAPSED : SIDE_BAR_WIDTH
  },
  ANTD_DEFAULT_ROW_PADDING: 30,
  CHART_COLOR: {
    BLUE: '#44A1FF',
    RED: '#F3667C',
    LIGNT_YELLOW: '#ffff00',
    YELLOW: '#FBD44B',
    GREEN: '#59CB74',
    SEA_GREEN: '#4DCCCB',
    PURPLE: '#8643E0',
  },
  CHART_COLORS: ['#44A1FF', '#F3667C', '#FBD44B', '#59CB74', '#4DCCCB', '#8643E0'],
  CHART_COLORS_REVERSED: [
    '#44A1FF',
    '#F3667C',
    '#FBD44B',
    '#59CB74',
    '#4DCCCB',
    '#8643E0',
  ].reverse(),
}

const CHART_CONTENT_WIDTH =
  window.innerWidth - CONSTANT.SIDE_BAR_WIDTH - CONSTANT.ANTD_DEFAULT_ROW_PADDING * 2

const CHART_CONTENT_WIDTH_RESIZE = () => {
  return (
    window.innerWidth - CONSTANT.SIDE_BAR_WIDTH_DYNAMIC() - CONSTANT.ANTD_DEFAULT_ROW_PADDING * 2
  )
}

const CODE_CONTENT_WIDTH =
  window.innerWidth - CONSTANT.SIDE_BAR_WIDTH - CONSTANT.ANTD_DEFAULT_ROW_PADDING * 2 - 50 * 2

const PAGE_CONTENT = {
  MAX_WIDTH: CHART_CONTENT_WIDTH - 12 * 2,
}

const LOG_HIGHLIGHT_REGEX = [/\[(DEBUG|INFO|WARN|ERROR|FATAL).*?\]/, /\[\d.*?\]/]

export const REGEXP = {
  url: /(http(s)?:\/\/.)?(www\.)?[-a-zA-Z0-9@:%._\+~#=]{2,256}\.[a-z]{2,6}\b([-a-zA-Z0-9@:%_\+.~#?&//=]*)/,
}

export const HINT = {
  TAKEOVER: '该应用未被接管',
}

export const CONSTANTS = {
  ...CONSTANT,
  CHART_CONTENT_WIDTH,
  CHART_CONTENT_WIDTH_RESIZE,
  CODE_CONTENT_WIDTH,
  PAGE_CONTENT,
  LOG_HIGHLIGHT_REGEX,
}
