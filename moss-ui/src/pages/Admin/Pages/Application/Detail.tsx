import '@/style/masonry.less'

import springIcon from '@/assets/icon-spring-framework.svg'
import AppTbs from '@/components/appTbs'
import AppTopo from '@/components/appTopo'
import Empty from '@/components/Empty'
import PageTitle from '@/components/PageTitle'
import applicationModel from '@/models/application/model'
import applicationService from '@/models/application/service'
import { CONSTANTS } from '@/util/common'
import { connect } from '@/util/store'
import { Col, Icon, Layout, Modal, Row, Tag, Tooltip } from 'antd'
import { History } from 'history'
import _ from 'lodash'
import moment from 'moment'
import React from 'react'
import JSONTree from 'react-json-tree'
import { RouteComponentProps } from 'react-router'

const { Content } = Layout

interface IDetailProps {
  history: History
  location: Location
  detail: ApplicationDetail
}

interface IDetailState {
  topoWidth: number
  nodes: any
  calls: any
  healthyModalVisible: boolean
  hasFetchedTopo: boolean
}

class Detail extends React.PureComponent<
  RouteComponentProps<{ id: string }> & IDetailProps,
  IDetailState
> {
  public topoWrap = React.createRef<HTMLDivElement>()
  public state: IDetailState = {
    topoWidth: 0,
    nodes: [],
    calls: [],
    healthyModalVisible: false,
    hasFetchedTopo: false,
  }
  public componentDidMount() {
    const {
      match: {
        params: { id },
      },
    } = this.props
    this.getDetailInfo(id)
    this.getTopoWidth()
  }

  public getTopoWidth = () => {
    const topoWrapWidth: number = parseInt(
      String(this.topoWrap.current && this.topoWrap.current.offsetWidth),
      10
    )
    this.setState({
      topoWidth: topoWrapWidth,
    })
  }

  public render() {
    const { base, build, event, health, depInfo } = this.props.detail
    const { healthyModalVisible } = this.state

    const tags = (
      <Row>
        <Tag color={CONSTANTS.CHART_COLOR.GREEN}>实例 Id : {base.id}</Tag>
        <Tag
          color={CONSTANTS.CHART_COLOR.GREEN}
          onClick={() => window.open(base.registration.managementUrl)}>
          {base.registration && base.registration.managementUrl}
        </Tag>
      </Row>
    )

    return (
      <Content>
        <Row>
          <Col span={24}>
            <AppTbs MenuData={this.props} />
          </Col>
        </Row>
        <Layout className="page">
          {base && base.registration && (
            <PageTitle
              icon={base.registration.icon || springIcon}
              name={base.registration.name}
              info={tags}
            />
          )}
          <Row>
            <Col span={14}>
              <div className="page-section pinpoint">
                <p>服务依赖拓扑</p>
                <div className="page-section-content page-section-list" ref={this.topoWrap}>
                  {this.state.nodes.length > 0 && (
                    <AppTopo
                      width={this.state.topoWidth}
                      calls={this.state.calls}
                      nodes={this.state.nodes}
                    />
                  )}
                  {this.state.nodes.length === 0 && this.state.hasFetchedTopo && (
                    <Empty description="暂无数据" />
                  )}
                  {this.state.nodes.length === 0 && !this.state.hasFetchedTopo && (
                    <Empty description="需要接入 skywalking">
                      <a href="#">接入指南</a>
                    </Empty>
                  )}
                </div>
              </div>

              <div className="page-section event">
                <p>Event 信息</p>
                <div className="page-section-content page-section-list">
                  <Row className="events-list-header" type="flex">
                    <Col span={12}>时间</Col>
                    <Col span={12}>事件</Col>
                  </Row>
                  <Row className="events-list">{event.map(this.getEventComponent)}</Row>
                </div>
              </div>
            </Col>

            <Col span={10}>
              <Row className="build">
                <div className="page-section">
                  <p className="list-title">Build 信息</p>
                  {this.getBuildComponent(build)}
                </div>
                <div className="page-section">
                  <p className="list-title">Git 信息</p>
                  {this.getGitComponent(build.git)}
                </div>
              </Row>
              <Row className="page-section health">
                <Row type="flex">
                  <Col span={23}>Health 信息</Col>
                  <Col span={1}>
                    <Icon type="reload" onClick={this.handleRefreshHealthy} />
                  </Col>
                </Row>

                <div className="page-section-content page-section-list">
                  {this.getHealthComponent(health)}
                </div>
              </Row>
              <div className="page-section">
                <p className="list-title">内部依赖信息</p>
                {this.getAppDepComponent(depInfo)}
              </div>
            </Col>
          </Row>
        </Layout>
        <Modal
          wrapClassName="json-tree-dialog"
          width={'40vw'}
          title="Healthy"
          visible={healthyModalVisible}
          onOk={this.toggleHealthyDialog(false)}
          onCancel={this.toggleHealthyDialog(false)}>
          <JSONTree className="json-tree" data={health} />
        </Modal>
      </Content>
    )
  }

  private handleRefreshHealthy = () => {
    const {
      match: {
        params: { id },
      },
    } = this.props
    applicationService.fetchApplicationHealth(id).then(() => {
      this.toggleHealthyDialog()()
    })
  }

  private toggleHealthyDialog = (visible = true) => () => {
    this.setState({
      healthyModalVisible: visible,
    })
  }

  private getEventComponent = (item: InstanceEvent, idx: number) => {
    const { timestamp, type } = item
    let fomattedType = type
    if (type === 'STATUS_CHANGED') {
      fomattedType = `${fomattedType} (${item.statusInfo && item.statusInfo.status})`
    }
    return (
      <Row key={idx}>
        <Col span={12}>{moment.unix(timestamp).format('LLL')}</Col>
        <Col span={12}>
          <span>{fomattedType} </span>
        </Col>
      </Row>
    )
  }

  private getAppDepComponent = (depInfo: AppDepInfo) => {
    const {using, springBootVersion, springCloudVersion } = depInfo
    return (
      <div className="page-section-content page-section-list">
        <Row>
          <Col span={12}>Spring Boot 版本</Col>
          <Col span={12}>{springBootVersion}</Col>
        </Row>
        <Row>
          <Col span={12}>Spring Cloud 版本</Col>
          <Col span={12}>{springCloudVersion}</Col>
        </Row>
        <Row>
          <Col span={8}>Using</Col>
        </Row>
        <Row>
          {using &&
            using.map(dep => {
              const k = Object.keys(dep) && Object.keys(dep)[0]
              const v = dep[k]
              return (
                <Row key={k}>
                  <Col offset={2} span={12}>
                    {k}
                  </Col>
                  <Col span={10}>{v}</Col>
                </Row>
              )
            })}
        </Row>
      </div>
    )
  }

  private getGitComponent = (git: Git = {} as Git) => {
    const { commit = {}, branch = '', remote } = git
    const repoUrl = remote && remote.origin && remote.origin.url

    return (
      <div className="page-section-content page-section-list">
        <Row>
          <Col span={8}>Repo</Col>
          <Tooltip title={repoUrl}>
            <Col span={16}>{repoUrl}</Col>
          </Tooltip>
        </Row>
        <Row>
          <Col span={8}>Branch</Col>
          <Tooltip title={branch}>
            <Col span={16}>{branch}</Col>
          </Tooltip>
        </Row>
        <Row>
          <Col span={8}>Commit</Col>
        </Row>
        <Row>
          <Col offset={2} span={6}>
            ID
          </Col>
          <Col span={16}>{commit && commit.id && (commit.id.abbrev || commit['id.abbrev'])}</Col>
        </Row>
        <Row>
          <Col offset={2} span={6}>
            Time
          </Col>
          <Col span={16}>{commit.time && moment.unix(commit.time).format('LLL')}</Col>
        </Row>
      </div>
    )
  }

  private getBuildComponent = (build: Info) => {
    const { version = '', artifactId = '', groupId = '' } = build
    return (
      <div className="page-section-content page-section-list">
        <Row>
          <Col span={8}>Group ID</Col>
          <Col span={16}>{groupId}</Col>
        </Row>
        <Row>
          <Col span={8}>Artifact ID</Col>
          <Col span={16}>{artifactId}</Col>
        </Row>
        <Row>
          <Col span={8}>Version</Col>
          <Col span={16}>{version}</Col>
        </Row>
        <Row>
          <Col span={8} style={{ color: '#ffff' }}>
            Time
          </Col>
        </Row>
      </div>
    )
  }

  private unWrapDataFromDetails = (data: HealthData): any => {
    if (data && data.details) {
      let { details, ...rest } = data // tslint:disable-line
      details = _.fromPairs(
        Object.keys(details).map((d: string) => {
          return [d, this.unWrapDataFromDetails(_.get(details, d))]
        })
      )
      return { ...rest, ...details }
    }
    return data
  }

  private getDetailInfo = (id: string) => {
    applicationService.fetchApplicationDetail(id).then(() => {
      const name = this.props.detail.base.registration.name.toLowerCase()
      applicationService
        .fetchApplicationTopo(name)
        .then((data: any) => {
          this.setState({
            nodes: data.getClusterTopology.nodes,
            calls: data.getClusterTopology.calls,
            hasFetchedTopo: true,
          })
        })
        .catch(() => {
          return null
        })
    })
    applicationService.fetchApplicationEvent(id)
    applicationService.fetchApplicationInfo(id)
    applicationService.fetchApplicationHealth(id)
    applicationService.fetchApplicationDepInfo(id)
  }

  private getHealthComponent = (health: HealthData) => {
    if (!health.details) {
      return (
        <Row className="health-content" type="flex" align="middle">
          <Col className="mt10 mb10 bold">Status: </Col>
          <Col className="health-status">{health.status}</Col>
        </Row>
      )
    }

    const { db, discoveryComposite, diskSpace, hystrix, mail } = this.unWrapDataFromDetails(health)
    if (discoveryComposite) {
      delete discoveryComposite.eureka
      delete discoveryComposite.discoveryClient
    }

    return (
      <div className="health-content">
        {db && (
          <Row>
            <Col>
              <div className="mt10 mb10 bold">db</div>
            </Col>
          </Row>
        )}
        {db && this.displayObjectAsKeyValueStringRecursive(db)}
        {discoveryComposite && Object.keys(discoveryComposite).length > 0 && (
          <Row>
            <Col>
              <div className="mt10 mb10 bold">discoveryComposite</div>
            </Col>
          </Row>
        )}
        {discoveryComposite &&
          Object.keys(discoveryComposite).map((item: string) => (
            <Row key={item}>
              <Col span={8}>{item}</Col>
              <Col span={16} className="extra">
                {item === 'status' ? (
                  <span className={discoveryComposite[item]}>{discoveryComposite[item]}</span>
                ) : (
                  JSON.stringify(discoveryComposite[item])
                )}
              </Col>
            </Row>
          ))}
        {diskSpace && (
          <Row>
            <Col>
              <div className="mt10 mb10 bold">diskSpace</div>
            </Col>
          </Row>
        )}
        {diskSpace &&
          Object.keys(diskSpace).map((item: string) => (
            <Row key={item}>
              <Col span={8}>{item}</Col>
              <Col span={16} className="extra">
                {item === 'status' ? (
                  <span className={diskSpace[item]}>{diskSpace[item]}</span>
                ) : null}
                {item === 'total' ? (diskSpace[item] / 1024 / 1024 / 1024).toFixed(1) + 'G' : null}
                {item === 'free' ? (diskSpace[item] / 1024 / 1024 / 1024).toFixed(1) + 'G' : null}
                {item === 'threshold' ? (diskSpace[item] / 1024 / 1024).toFixed(1) + 'M' : null}
              </Col>
            </Row>
          ))}

        {hystrix && (
          <Row>
            <Col>
              <div className="mt10 mb10 bold">hystrix</div>
            </Col>
          </Row>
        )}
        {hystrix &&
          Object.keys(hystrix).map((item: string) => (
            <Row key={item}>
              <Col span={8}>{item}</Col>
              <Col span={16} className="extra">
                {item === 'status' ? (
                  <span className={hystrix[item]}>{hystrix[item]}</span>
                ) : (
                  JSON.stringify(hystrix[item])
                )}
              </Col>
            </Row>
          ))}

        {mail && (
          <Row>
            <Col>
              <div className="mt10 mb10 bold">mail</div>
            </Col>
          </Row>
        )}
        {mail &&
          Object.keys(mail).map((item: string) => (
            <Row key={item}>
              <Col span={8}>{item}</Col>
              <Col span={16} className="extra">
                {item === 'status' ? (
                  <span className={mail[item]}>{mail[item]}</span>
                ) : (
                  JSON.stringify(mail[item])
                )}
              </Col>
            </Row>
          ))}
      </div>
    )
  }

  private displayObjectAsKeyValueStringRecursive(object: any) {
    if (!object) {
      return null
    }
    return Object.keys(object).map((item: string) => (
      <Row key={item}>
        <Col span={8}>{item}</Col>
        <Col span={15} offset={1} className="extra">
          {item === 'status' ? (
            <span className={object[item]}>{object[item]}</span>
          ) : typeof object[item] === 'object' ? (
            this.displayObjectAsKeyValueStringRecursive(object[item])
          ) : (
            object[item]
          )}
        </Col>
      </Row>
    ))
  }
}

export default connect({ detail: applicationModel.detail })(Detail)
