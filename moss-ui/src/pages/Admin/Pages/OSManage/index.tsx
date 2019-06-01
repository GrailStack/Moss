import React from 'react'
import { Route, Switch } from 'react-router'
import menuEdit from './menuEdit'
import menuManage from './menuManage'
import menuNew from './menuNew'
import modifyExample from './modifyExample'
class OSManage extends React.Component {
  public render() {
    return (
      <React.Fragment>
        <Switch>
          <Route exact={true} path="/menuManage" component={menuManage} />
          <Route exact={true} path="/menuManage/new" component={menuNew} />
          <Route exact={true} path="/menuManage/edit" component={menuEdit} />
          
          <Route exact={true} path="/menuManage/modifyExample" component={modifyExample} />
        </Switch>
      </React.Fragment>
    )
  }
}
export default OSManage
