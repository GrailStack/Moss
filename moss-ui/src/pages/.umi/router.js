import React from 'react';
import { Router as DefaultRouter, Route, Switch } from 'react-router-dom';
import dynamic from 'umi/dynamic';
import renderRoutes from 'umi/_renderRoutes';
import RendererWrapper0 from '/develop/gitRep/mw/open/Moss/moss-ui/src/pages/.umi/LocaleWrapper.jsx'

let Router = DefaultRouter;

let routes = [
  {
    "path": "/",
    "component": require('../index').default,
    "exact": true,
    "_title": "Moss 莫斯",
    "_title_default": "Moss 莫斯"
  },
  {
    "component": () => React.createElement(require('/develop/gitRep/mw/open/Moss/moss-ui/node_modules/umi-build-dev/lib/plugins/404/NotFound.js').default, { pagesPath: 'src/pages', hasRoutesInConfig: true }),
    "_title": "Moss 莫斯",
    "_title_default": "Moss 莫斯"
  }
];
window.g_routes = routes;
window.g_plugins.applyForEach('patchRoutes', { initialValue: routes });

// route change handler
function routeChangeHandler(location, action) {
  window.g_plugins.applyForEach('onRouteChange', {
    initialValue: {
      routes,
      location,
      action,
    },
  });
}
window.g_history.listen(routeChangeHandler);
routeChangeHandler(window.g_history.location);

export default function RouterWrapper() {
  return (
<RendererWrapper0>
          <Router history={window.g_history}>
      { renderRoutes(routes, {}) }
    </Router>
        </RendererWrapper0>
  );
}
