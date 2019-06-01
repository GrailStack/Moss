const phoneRegexp = new RegExp(/^(\d{3})\d{4}(\d{4})$/);
const passwordRegexp = new RegExp(/^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,}$/);

export function isPhone(str: string): boolean {
  return phoneRegexp.test(str);
}

export function isPassword(str: string): boolean {
  return passwordRegexp.test(str);
}
