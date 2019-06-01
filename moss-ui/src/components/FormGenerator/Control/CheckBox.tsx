import { Checkbox } from 'antd';
import { CheckboxGroupProps } from 'antd/lib/checkbox/Group';
import React from 'react';

const { Group } = Checkbox;

class MyCheckbox extends React.Component<CheckboxGroupProps> {
  public render() {
    return <Group {...this.props} />;
  }
}

export default MyCheckbox;
