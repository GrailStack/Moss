# Moss-服务治理管控平台

Moss 服务治理平台

# 介绍

每一个 Spring Boot 应用通过 Eureka 注册到 admin server, Halo 管控平台上对 应用状态、JVM 信息、内存、线程、堆栈、日志等等可视化展示，可以比较全面的监控了 Spring Boot 应用的整个生命周期。

# 基本

基础框架 : [UmiJS](https://umijs.org/zh/)

UI : [React](https://reactjs.org/) [Less](http://lesscss.org/) [Ant design](https://ant.design/)

图表 : [AntV g2](https://antv.alipay.com/zh-cn/g2/3.x/index.html) 的 React 封装 [Viser](https://viserjs.github.io/) + [D3](https://d3js.org/)

状态管理 : [react-glux](https://github.com/ZhouYK/react-glux) (基于 Redux 的类似 dva 的封装)

# Getting Started

`npm run 6`

# 相关


// 项目列表
  fetchProjects: (
    name: string = '',
    projectName: string = '',
    pageNo: number = 1,
    pageSize: number = 10,
  ) => {
    return fetch({
      method: 'POST',
      url: `admin/project/list`,
      data: { name, projectName,pageNo, pageSize},
    }).then((data: any) => {
      data.list = data.list.map((d: any) => {
        d.isDeleted = d.isDeleted === 1 ? true : false;
        return d;
      });
      return model.Project.AppSearchList(data);
    });
  },
