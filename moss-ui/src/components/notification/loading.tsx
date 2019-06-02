import { Spin } from 'antd';
import React from 'react';

class Loading extends React.Component {
  public render() {
    return (
      <div className="loading">
        <Spin size="large" style={{ fontSize: '40px', color: '#001529' }} />
      </div>
    );
  }
}
export default Loading;
