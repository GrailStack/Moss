import { DatePicker } from 'antd';
import { RangePickerProps } from 'antd/lib/date-picker/interface';
import moment from 'moment';
import React from 'react';

const { RangePicker } = DatePicker;

class MyRangerPicker extends React.PureComponent<RangePickerProps> {
  public render() {
    const { value = [] } = this.props;

    (value as []).forEach((dateString, idx) => {
      if (!moment.isMoment(dateString)) {
        value[idx] = moment(dateString * 1);
      }
    });
    return <RangePicker {...this.props} />;
  }
}

export default MyRangerPicker;
