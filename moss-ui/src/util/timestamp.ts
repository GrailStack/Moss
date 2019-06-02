// 时间戳转本地时间
export function getLocalTime(str: string) {
  return new Date(parseInt(str, 10)).toLocaleString().replace(/:\d{1,2}$/, ' ');
}

// 获取当前时分秒
export function getTime(timeType: string) {
  const myDate = new Date();
  const hours = myDate.getHours(); // 获取当前小时数(0-23)
  const minutes = myDate.getMinutes(); // 获取当前分钟数(0-59)
  const seconds = myDate.getSeconds(); // 获取当前秒数(0-59)
  let returnDate = '';
  if (timeType === 'hms') {
    returnDate = `${hours}:${minutes}:${seconds}`;
  }
  return returnDate;
}

export function timestampToTime(timestamp: number) {
  const unixTimestamp = new Date(timestamp);
  const commonTime = unixTimestamp.toLocaleString();
  return commonTime;
}
