import App from '@/pages/App'
import { polyfill } from 'es6-promise'
import React from 'react'

if (window.Promise && !window.Promise.prototype.finally) {
  window.Promise = null
  polyfill()
}

export default function() {
  return <App />
}
