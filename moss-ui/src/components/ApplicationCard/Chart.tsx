import './style.less'

import React from 'react'
import { Area, Chart, SmoothLine, Tooltip } from 'viser-react'

class AppChart extends React.PureComponent<{
  data: ApplicationMetricsData[]
  color?: string
}> {
  public render() {
    const { color, data = [] } = this.props
    const keys = Object.keys(data[0] || {})
    return (
      <Chart
        forceFit={true}
        height={45}
        scale={[{ dataKey: keys[0], range: [0, 1], type: 'timeCat' }]}
        padding={[5, 0, 0, 0]}
        data={data}>
        <Tooltip showTitle={false} />
        <SmoothLine position={keys.join('*')} color={color} />
        <Area position={keys.join('*')} color={color} shape="smooth" />
      </Chart>
    )
  }
}

export default AppChart
