import { Radio } from 'antd';
import { RadioGroupProps } from 'antd/lib/radio';
import React from 'react';

class MyRadio extends React.PureComponent<RadioGroupProps> {
  public render() {
    return <Radio.Group {...this.props} />;
  }
}

export default MyRadio;
