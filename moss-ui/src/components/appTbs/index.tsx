import { Menu } from 'antd'
import React from 'react'
import { RouteComponentProps } from 'react-router'
import { Link } from 'react-router-dom'
interface IProps {
  MenuData: any
}

const AppTbs = React.memo((props: IProps) => {
  const { location } = props.MenuData as (MenuData & RouteComponentProps)
  const urlArray = location.pathname.split('/')
  const pathName = `/${urlArray[1]}/${urlArray[2]}`
  const pageName = [
    'log',
    'Jvm',
    'EnvironConfig',
    'Jmx',
    'Trace',
    'logDel',
    'Thread',
    'Jar',
    'gclog',
    'calltrace',
    'trajectory',
  ]
  let CheckPage = 'Detail'
  pageName.forEach(item => {
    if (location.pathname.search(item) !== -1) {
      CheckPage = item
    }
  })

  return (
    <Menu
      mode="horizontal"
      defaultSelectedKeys={[CheckPage]}
      style={{
        lineHeight: '50px',
        marginBottom: '10px',
        backgroundColor: '#fff',
      }}>
      <Menu.Item key="Detail">
        <Link to={`${pathName}`}>详情</Link>
      </Menu.Item>
      <Menu.Item key="log">
        <Link to={`${pathName}/log?pageNo=1`}>日志级别</Link>
      </Menu.Item>
      <Menu.Item key="EnvironConfig">
        <Link to={`${pathName}/EnvironConfig`}>环境配置</Link>
      </Menu.Item>
      <Menu.Item key="Jmx">
        <Link to={`${pathName}/Jmx`}>JMX</Link>
      </Menu.Item>
      <Menu.Item key="Jvm">
        <Link to={`${pathName}/Jvm`}>JVM</Link>
      </Menu.Item>
      <Menu.Item key="logDel">
        <Link to={`${pathName}/log/logDel`}>查看日志</Link>
      </Menu.Item>
      <Menu.Item key="Thread">
        <Link to={`${pathName}/Thread`}>线程</Link>
      </Menu.Item>
      <Menu.Item key="Jar">
        <Link to={`${pathName}/Jar`}>依赖</Link>
      </Menu.Item>
      <Menu.Item key="Trace">
        <Link to={`${pathName}/Trace`}>http Trace</Link>
      </Menu.Item>
      <Menu.Item key="gclog">
        <Link to={`${pathName}/gclog`}>GC Log</Link>
      </Menu.Item>
    </Menu>
  )
})

export default AppTbs
