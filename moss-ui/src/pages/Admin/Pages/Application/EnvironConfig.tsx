import PageTitle from '@/components/PageTitle'
import applicationModel from '@/models/application/model'
import applicationService from '@/models/application/service'
import { connect } from '@/util/store'
import '@/style/masonry.less'
import { Button, Col, Input, Layout, Row, Tooltip } from 'antd'
import React from 'react'
import { RouteComponentProps } from 'react-router'
import AppTbs from '@/components/appTbs'

interface IEnvironConfigProps {
  envConfig: any
}
interface IEnvironConfigState {
  selectMenuIndex: number
  envSearch: string
}
class EnvironConfig extends React.Component<
  RouteComponentProps<{ id: string }> & IEnvironConfigProps,
  IEnvironConfigState
> {
  public state: IEnvironConfigState = {
    envSearch: '',
    selectMenuIndex: 0,
  }

  public componentDidMount() {
    applicationService.fetchApplicationEnv(this.props.match.params.id)
  }

  public render() {
    const { Content } = Layout
    const Search = Input.Search
    const {
      envConfig: {
        data: { propertySources: properties },
      },
    } = this.props
    const { selectMenuIndex } = this.state
    const selectedProp = properties && properties[selectMenuIndex]

    return (
      <React.Fragment>
        <Row>
          <Col span={24}>
            <AppTbs MenuData={this.props} />
          </Col>
        </Row>
        <Layout className="page EnvironConfig">
          <Row>
            <Col span={24}>
              <PageTitle name="环境配置" info="查看当前应用的环境配置信息" />
            </Col>
          </Row>

          <Content style={{ position: 'relative' }}>
            <div className="left">
              <i>配置</i>
              {properties &&
                properties.map((item: any, index: number) => {
                  const name =
                    item.name.indexOf(':') !== -1
                      ? item.name.substring(0, item.name.indexOf(':'))
                      : item.name
                  return (
                    <Button
                      key={index}
                      className={index === selectMenuIndex ? 'btnLeft active' : 'btnLeft'}
                      onClick={this.handleMenuSelect(index)}>
                      {name}
                    </Button>
                  )
                })}
            </div>
            <div className="right">
              <i className="title">SEARCH</i>
              <Search
                placeholder="input search text"
                onChange={this.envSearch}
                style={{ marginBottom: '40px', color: '#000' }}
              />
              <i className="title">CONFIG</i>
              <Row className="configList">
                <Col span={24}>{selectedProp && selectedProp.name}</Col>
              </Row>
              {selectedProp &&
                Object.keys(selectedProp && selectedProp.properties)
                  .filter(this.envSearchFilter)
                  .map((item: any, index: number) => (
                    <Row key={index} className="configList">
                      <Tooltip title={item}>
                        <Col className="config-list-item" span={11} push={1}>
                          {item}
                        </Col>
                      </Tooltip>
                      {String(selectedProp.properties[item].value).length > 50 ? (
                        <Tooltip title={String(selectedProp.properties[item].value)}>
                          <Col span={11} push={2}>
                            {String(selectedProp.properties[item].value)}
                          </Col>
                        </Tooltip>
                      ) : (
                        <Col span={11} push={2}>
                          {String(selectedProp.properties[item].value)}
                        </Col>
                      )}
                    </Row>
                  ))}
            </div>
          </Content>
        </Layout>
      </React.Fragment>
    )
  }

  private envSearchFilter = (prop: any) => {
    const { envSearch } = this.state
    return prop.indexOf(envSearch) !== -1
  }

  private envSearch = (e: any) => {
    this.setState({
      envSearch: e.currentTarget.value,
    })
  }

  private handleMenuSelect = (index: number) => {
    return () => {
      this.setState({
        selectMenuIndex: index,
      })
    }
  }
}

export default connect({ envConfig: applicationModel.EnvironConfig })(EnvironConfig)
