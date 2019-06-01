import { Empty } from 'antd'
import React, { ReactNode } from 'react'
import './style.less'

interface IEmptyProps {
  description?: string
  children?: ReactNode
}

const E = (props: IEmptyProps) => {
  const { description, children, ...rest } = props
  return (
    <div className="component-empty">
      <Empty description={description || '暂无数据'} {...rest}>
        {children}
      </Empty>
    </div>
  )
}
export default E
