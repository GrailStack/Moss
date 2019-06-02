import './polyfills';
<<<<<<< HEAD
import '../../global.ts';
import '@tmp/initHistory';
=======
import '@tmp/initHistory';
import '../../global.ts';
>>>>>>> 91b037d... Update default config
import React from 'react';
import ReactDOM from 'react-dom';


// runtime plugins
window.g_plugins = require('umi/_runtimePlugin');
window.g_plugins.init({
  validKeys: ['patchRoutes','render','rootContainer','modifyRouteProps','onRouteChange','locale',],
});
window.g_plugins.use(require('@/app'));



// render
let oldRender = () => {
  const rootContainer = window.g_plugins.apply('rootContainer', {
    initialValue: React.createElement(require('./router').default),
  });
  ReactDOM.render(
    rootContainer,
    document.getElementById('root'),
  );
};
const render = window.g_plugins.compose('render', { initialValue: oldRender });

const moduleBeforeRendererPromises = [];

Promise.all(moduleBeforeRendererPromises).then(() => {
  render();
}).catch((err) => {
  window.console && window.console.error(err);
});

require('../../global.less');

// hot module replacement
if (module.hot) {
  module.hot.accept('./router', () => {
    oldRender();
  });
}
