import React from 'react'

interface IProps {
  icon?: string
  name: string
  info?: string | JSX.Element
  titleExtra?: JSX.Element
  rightPanelExtra?: JSX.Element
}

const PageTitle = React.memo((props: IProps) => {
  const { icon, name, info, titleExtra, rightPanelExtra } = props
  return (
    <header className="page-title clearfix">
      {icon ? <img src={icon} /> : null}
      <div className="page-title-text">
        <div className="page-title-panel">
          <div className="page-title-container">
            <p className="page-title-name">{name}</p>
            {titleExtra ? <div className="page-title-extra" children={titleExtra} /> : null}
          </div>
          {rightPanelExtra ? (
            <div className="page-right-panel-extra" children={rightPanelExtra} />
          ) : null}
        </div>
        <span className="page-title-info">{info}</span>
      </div>
    </header>
  )
})

export default PageTitle
