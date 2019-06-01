import './style.less';

import Application from './Application';
import CodeGenerate from './CodeGenerate';
import Dashboard from './Dashboard';
import OSManage from './OSManage';
import SwitchCenter from './SwitchCenter';

const pages: { [key: string]: any } = {
  Application,
  Dashboard,
  CodeGenerate,
  OSManage,
  SwitchCenter,
};

export default pages;
