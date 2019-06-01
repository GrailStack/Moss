import { getMenuInfo } from '@/models/menu/service'
import { capitalizeFirstLetter } from '@/util'
import classnames from 'classnames'
import React from 'react'

import Pages from './Pages'
import Application from './Pages/Application'

function getPageName(src: string): string {
  return capitalizeFirstLetter(src.split('/')[1])
}

class Stage extends React.Component<
  {
    current: string
    tab: string[]
  },
  {}
> {
  public render() {
    const { current, tab } = this.props
    return (
      <div id="stage" className="ant-layout-content">
        {tab.map((name: string) => (
          <div
            key={name}
            style={{ width: '100%' }}
            className={classnames({
              active: name === current,
            })}
          >
            {this.getChildren({ active: name === current, name })}
          </div>
        ))}
      </div>
    )
  }

  private getChildren = ({ active, name }: { active: boolean; name: string }) => {
    if (name === 'dashboard') {
      return active ? <Pages.Dashboard /> : null
    }

    const { src, isExternal } = getMenuInfo(name)

    if (!src) {
      return <Application />
    }
    if (isExternal) {
      return <iframe src={src} />
    } else {
      const Page = Pages[getPageName(src as string)]
      return Page ? <Page /> : <Application />
    }
  }
}

export default Stage
