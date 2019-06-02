import moment from 'moment'

export const renderBoolean = (booleanValue: boolean | number) => {
  return Boolean(Number(booleanValue)) ? '是' : '否'
}

export const renderDate = (format?: string) => (date: string | number) => {
  if (format) {
    return moment(String(date), format).fromNow()
  } else {
    return moment(String(date)).fromNow()
  }
}
