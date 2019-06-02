import React from 'react';
import { Route, Switch } from 'react-router';

import switchManage from './switchManage';
import switchPushLog from './switchPushLog';

class SwitchManage extends React.Component {
  public render() {
    return (
      <React.Fragment>
        <Switch>
          <Route exact={true} path="/switchManage" component={switchManage} />
          <Route exact={true} path="/switchPushLog" component={switchPushLog} />
        </Switch>
      </React.Fragment>
    );
  }
}

export default SwitchManage;
