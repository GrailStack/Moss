import './style.less'

import { connect } from '@/util/store'
import React from 'react'
import { withRouter } from 'react-router'

import ListPage from './ListPage'

export default (data: PageConstructorData) =>
  withRouter(
    // @ts-ignore
    connect({ listData: data.model })((props: any) => (
      <ListPage
        {...{
          ...props,
          ...data,
        }}
      />
    ))
  )
