// ref: https://umijs.org/config/
export default {
  define: {
    theme: {
      'primary-color': '#68BD45',
      'layout-body-background': '#f5f8f9',
    },
  },
  lessLoaderOptions: {
    modifyVars: {
      'primary-color': '#68BD45',
      'layout-body-background': '#f5f8f9',
    },
  },
  disableCSSModules: true,
  routes: [{ path: '/', component: './index' }],
  history: 'hash',
  treeShaking: false,
  chainWebpack(config, { webpack }) {
    config.resolve.extensions.add('tsx')
    config.resolve.extensions.add('less')
  },
  plugins: [
    // ref: https://umijs.org/plugin/umi-plugin-react.html
    [
      'umi-plugin-react',
      {
        antd: true,
        dynamicImport: false,
        title: 'Moss 莫斯',
        dll: true,
        locale: {
          enable: false,
          default: 'zh-CN',
        },
        routes: {
          exclude: [
            /models\//,
            /services\//,
            /model\.(t|j)sx?$/,
            /service\.(t|j)sx?$/,
            /components\//,
          ],
        },
      },
    ],
  ],
}
