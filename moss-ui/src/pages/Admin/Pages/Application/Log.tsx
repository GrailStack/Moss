import '@/style/masonry.less'

import PageTitle from '@/components/PageTitle'
import applicationModel from '@/models/application/model'
import applicationService from '@/models/application/service'
import { connect } from '@/util/store'
import { qsParse, qsStringify } from '@/util'
import { Button, Radio, Col, Icon, Input, Layout, Pagination, Row } from 'antd'
import React from 'react'
import { RouteComponentProps, withRouter } from 'react-router'
import AppTbs from '@/components/appTbs'

interface ILogProps {
  log: {
    [index: string]: any
  }
}

class Log extends React.Component<RouteComponentProps<{ id: string }> & ILogProps> {
  public componentDidMount() {
    this.getLogInfo(this.props.match.params.id)()
  }

  public render() {
    const { Content, Header } = Layout
    const Search = Input.Search
    const { levels, loggers } = this.props.log
    const logKeys: any = Object.keys(loggers)
    const data = this.searchLog(logKeys, loggers)
    const total = data.newLoggers.length
    const loggersKeys = this.paging(data.newLoggers)
    const current = Number(data.current)
    return (
      <React.Fragment>
        <Row>
          <Col span={24}>
            <AppTbs MenuData={this.props} />
          </Col>
        </Row>
        <Layout className="page logList">
          <Row>
            <Col span={24}>
              <PageTitle
                name="日志级别"
                info="查看应用的日志级别,根据需要调整日志级别打印对应日志级别的信息"
              />
            </Col>
            {/* <Col span={12}>
            <SelectNode />
          </Col> */}
          </Row>

          <Content>
            <Header className="operation">
              <Row>
                <Col span={10}>
                  <Search
                    placeholder="input search text"
                    onSearch={this.logSearch()}
                  />
                </Col>

                <Col span={14}>
                  <Radio.Group onChange={e => this.getSort(e.target.value)} defaultValue='ALL'>
                    <Radio.Button value="ALL"><Icon type="appstore" /> ALL</Radio.Button>
                    <Radio.Button value="CLASS"><Icon type="border" /> CLASS</Radio.Button>
                    <Radio.Button value="Configured"><Icon type="setting" /> Configured</Radio.Button>
                  </Radio.Group>
                  <Button type="primary" onClick={this.getLogInfo(this.props.match.params.id)}>
                    <Icon type="loading-3-quarters" /> 刷新
                  </Button>
                </Col>
              </Row>
            </Header>
            <div
              className="rowList"
              style={{
                background: '#fff',
                padding: 50,
                paddingTop: 16,
                marginBottom: 50,
              }}>
              {loggersKeys.length ? (
                loggersKeys.map((item: any) => (
                  <Row key={item}>
                    <Col span={12}>{item}</Col>
                    {levels.map((itemL: any) => (
                      <Col key={itemL} span={2}>
                        <Button
                          onClick={this.modifyLogLevel(item, itemL)}
                          className={
                            itemL === loggers[item].effectiveLevel
                              ? `button-${loggers[item].effectiveLevel}`
                              : `button-WHITE button-default-${itemL}`
                          }
                          type="primary">
                          {itemL}
                        </Button>
                      </Col>
                    ))}
                  </Row>
                ))
              ) : (
                <Row>
                  <Col span={24} style={{ textAlign: 'center' }}>
                    暂无数据
                  </Col>
                </Row>
              )}
            </div>
            {loggersKeys.length ? (
              <Pagination
                defaultCurrent={1}
                pageSize={10}
                current={current}
                total={total}
                onChange={this.changePage}
              />
            ) : null}
          </Content>
        </Layout>
      </React.Fragment>
    )
  }

  private getLogInfo = (id: string) => () => {
    applicationService.fetchApplicationLoggers(id)
  }

  // 修改日志级别
  private modifyLogLevel = (logName: string, logLevel: string) => () => {
    const options = { configuredLevel: logLevel }
    applicationService.fetchApplicationModifyLogLevel(this.props.match.params.id, logName, options)
  }

  // 初始化以及根据条件返回数据
  private searchLog = (loggers: [], logList: any) => {
    let newLoggers: any = []
    /* 获取url参数 得到整个对象 */
    const params = qsParse(this.props.location.search.slice(0))

    if (!params.status) {
      params.status = 'ALL'
    }

    if (params.status === 'ALL') {
      newLoggers = loggers
    }

    if (params.status === 'CLASS') {
      loggers.forEach((item: any) => {
        const lastString = item
          .split('.')
          .pop()
          .substr(0, 1)
        if (/[A-Z]/.test(lastString)) {
          newLoggers.push(item)
        }
      })
    }

    if (params.status === 'Configured') {
      loggers.forEach(item => {
        if (logList[item].configuredLevel !== null) {
          newLoggers.push(item)
        }
      })
    }

    if (params.search) {
      newLoggers.forEach((item: any, i: any) => {
        if (item.search(params.search) === -1) {
          delete newLoggers[i]
        }
      })
      const indexArry: any = []
      newLoggers.forEach((item: any, i: number) => {
        if (item !== undefined) {
          indexArry.push(i)
        }
      })
      newLoggers = newLoggers.slice(indexArry[0], newLoggers.length)
    }

    const data: any = {
      current: params.pageNo as number,
      newLoggers,
    }
    return data
  }

  // 分页按钮
  private changePage = (pageNo: number) => {
    const { pathname } = this.props.location
    const nextQs = Object.assign(qsParse(this.props.location.search.slice(0)), {
      pageNo,
    })

    // window.location.href = `${pathname}?${qsStringify(nextQs)}`;
    this.props.history.push(`${pathname}?${qsStringify(nextQs)}`)
  }

  // 实际分页
  private paging = (data: any) => {
    const pageSize = 10
    /* 获取url参数 得到整个对象 */
    const params = qsParse(this.props.location.search.slice(0))

    if (!params.pageNo) {
      params.pageNo = 1
    }

    return data.slice((params.pageNo - 1) * pageSize, params.pageNo * pageSize - 1)
  }

  // ALL
  private getSort (num: any) {
    const { pathname } = this.props.location;
    this.props.history.push(`${pathname}?pageNo=1&status=${num}`)
  }

  // 搜索
  private logSearch = () => (value: any) => {
    const { pathname } = this.props.location
    const params = qsParse(this.props.location.search.slice(0))
    if (!params.status) {
      params.status = 'ALL'
    }
    this.props.history.push(`${pathname}?pageNo=1&status=${params.status}&search=${value}`)
  }
}

export default connect({ log: applicationModel.log })(withRouter(Log))
