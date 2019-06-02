import { gluer } from 'glue-redux'

const dashboard = gluer((data: DashboardBasicData): DashboardBasicData => {
  return data
}, {})

const report = gluer((data: ReportData): ReportData => {
  return data
}, {})

export default { dashboard, report }
