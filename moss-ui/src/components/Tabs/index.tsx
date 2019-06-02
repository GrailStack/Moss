import './style.less'

import { getMenuInfo } from '@/models/menu/service'
import tabService from '@/models/tab/service'
import { Icon } from 'antd'
import classnames from 'classnames'
import React from 'react'
import { Link, RouteComponentProps, withRouter } from 'react-router-dom'

interface IProps {
  tab: string[]
  current: string
}

const Tabs = React.memo((props: IProps & RouteComponentProps) => {
  const removeTab = (title: string) => {
    return () => {
      const { current, tab } = props

      if (title === current) {
        let idx = tab.indexOf(current)
        const next = idx + 1 < tab.length ? ++idx : --idx
        props.history.replace(tab[next])
      }
      tabService.removeTab({ title })
    }
  }

  return (
    <ul className="stage-tab">
      {props.tab.map(name => (
        <li key={name} className={'active' || classnames({ active: name === props.current })}>
          <Link to={`/${name}`} title={name}>
            {getMenuInfo(name).title}
          </Link>
          {name !== 'dashboard' ? <Icon type="close" onClick={removeTab(name)} /> : null}
        </li>
      ))}
    </ul>
  )
})

export default withRouter(Tabs)
