import { wholeModel } from '@/util/store'
import fetch from '@/util/fetch'

export default {
  fetchDashBoard: (): Promise<DashboardBasicData> => {
    return fetch<DashboardBasicData>({
      method: 'get',
      url: `admin/dashboard/basic`,
    }).then(data => {
      return wholeModel.dashboard.dashboard(data)
    })
  },
  fetchReports: (): Promise<ReportData> => {
    return fetch<ReportData>({
      method: 'get',
      url: `admin/dashboard/report`,
    }).then(data => {
      return wholeModel.dashboard.report(data)
    })
  },
}
