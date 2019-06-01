import { Button } from 'antd';
import React from 'react';

class ControllerBar extends React.PureComponent<{
  data: ControllerData[];
}> {
  public render() {
    const { data } = this.props;

    return (
      <div className="page-section">
        {data.map(item => {
          return (
            <Button key={item.title} {...item}>
              {item.title}
            </Button>
          );
        })}
      </div>
    );
  }
}

export default ControllerBar;
