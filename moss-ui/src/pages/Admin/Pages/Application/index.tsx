import './style.less'

import React from 'react'
import { Route, Switch } from 'react-router'

import modifyExample from '@/pages/Admin/Pages/OSManage/modifyExample'
import AppSearch from './AppSearch'
import CallTrace from './CallTrace'
import CreateProject from './CreateProject'
import Detail from './Detail'
import EnvironConfig from './EnvironConfig'
import EventLog from './EventLog'
import GCLog from './GCLog'
import Jar from './Jar'
import Jmx from './JMX'
import Jvm from './JVM'
import List from './List'
import Log from './Log'
import LogDel from './LogDel'
import Project from './Project'
import RemoteConfig from './RemoteConfig'
import Service from './Service'
import Thread from './Thread'
import Trace from './Trace'
import Trajectory from './Trajectory'
import RegisterCenterMgmt from './registerCenterMgmt'
import CreateRegisterCenter from './CreateRegisterCenter'
import UserMgmt from './UserMgmt'
import CreateUser from './CreateUser'


class Index extends React.Component<{}, {}> {
  public render() {
    return (
      <>
        <Switch>
          <Route exact={true} path="/project/createProject" component={CreateProject} />
          <Route exact={true} path="/project" component={Project} />
          <Route exact={true} path="/registerCenterMgmt" component={RegisterCenterMgmt} />
          <Route exact={true} path="/registerCenterMgmt/create" component={CreateRegisterCenter} />
          <Route exact={true} path="/serviceManage" component={Service} />
          <Route exact={true} path="/list" component={List} />
          <Route exact={true} path="/list/:id" component={Detail} />
          <Route exact={true} path="/list/:id/log" component={Log} />
          <Route exact={true} path="/list/:id/JVM" component={Jvm} />
          <Route exact={true} path="/list/:id/JMX" component={Jmx} />
          <Route exact={true} path="/list/:id/Trace" component={Trace} />
          <Route exact={true} path="/list/:id/EnvironConfig" component={EnvironConfig} />
          <Route exact={true} path="/list/:id/log/logDel" component={LogDel} />
          <Route exact={true} path="/list/:id/Thread" component={Thread} />
          <Route exact={true} path="/list/:id/Jar" component={Jar} />
          <Route exact={true} path="/list/:id/gclog" component={GCLog} />
          <Route exact={true} path="/list/:id/calltrace" component={CallTrace} />
          <Route exact={true} path="/appAccept/createApp" component={modifyExample} />
          <Route exact={true} path="/appAccept" component={AppSearch} />
          <Route exact={true} path="/eventLog" component={EventLog} />
          <Route exact={true} path="/appAccept/modifyExample" component={modifyExample} />
          <Route exact={true} path="/remoteConfig" component={RemoteConfig} />
          <Route exact={true} path="/mqTrace" component={Trajectory} />
          <Route exact={true} path="/userMgmt" component={UserMgmt} />
          <Route exact={true} path="/userMgmt/create" component={CreateUser} />
        </Switch>
      </>
    )
  }
}

export default Index
