import { Transfer } from 'antd';
import { TransferItem, TransferProps } from 'antd/lib/transfer';
import React from 'react';

class MyTransfer extends React.PureComponent<
  TransferProps & {
    options: FieldOptions;
  }
> {
  public render() {
    const dataSource = this.parse(this.props.options);
    const transferProps = {
      ...this.props,
      dataSource,
      render: (item: TransferItem) => item.title,
    };
    return <Transfer {...transferProps} />;
  }

  private parse(options: FieldOptions): TransferItem[] {
    return options.map(opt => {
      const { key, title } =
        typeof opt === 'string'
          ? { key: opt, title: opt }
          : { key: opt.value, title: opt.label || opt.value };
      return { key, title };
    });
  }
}

export default MyTransfer;
