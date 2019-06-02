import { Select } from 'antd';
import { SelectProps } from 'antd/lib/select';
import React from 'react';

class MySelect extends React.PureComponent<
  SelectProps & {
    options: FieldOptions;
  }
> {
  public render() {
    const { options } = this.props;
    return <Select {...this.props}>{options.map(this.getOption)}</Select>;
  }
  private getOption = (option: FieldOption, idx: number) => {
    let props: { [key: string]: any };

    if (typeof option === 'string') {
      props = {};
      props.key = option || idx + '';
      props.value = option;
      props.label = option;
    } else {
      props = { ...option };
      props.key = option.key || option.value || idx + '';
      props.label = option.label || option.key;
    }

    return <Select.Option {...props}>{props.label}</Select.Option>;
  };
}

export default MySelect;
