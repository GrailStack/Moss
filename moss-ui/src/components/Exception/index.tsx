// from : https://github.com/ant-design/ant-design-pro/blob/master/src/components/Exception/index.zh-CN.md

import { Button } from 'antd';
import classNames from 'classnames';
import React from 'react';
import './index.less';
import config from './typeConfig';

interface IProps {
  className?: string;
  backText?: string;
  linkElement?: string;
  type?: string;
  title?: string;
  desc?: string;
  img?: string;
  actions?: string | React.ReactNode;
  redirect?: string;
}

const Exception = React.memo((props: IProps) => {
  const {
    className,
    backText = '返回首页',
    linkElement = 'a',
    type = '',
    title,
    desc,
    img,
    actions,
    redirect = '/',
    ...rest
  } = props;
  const pageType = type in config ? type : '404';
  const clsString = classNames('exception', className);
  return (
    <div className={clsString} {...rest}>
      <div className={'imgBlock'}>
        <div
          className={'imgEle'}
          style={{ backgroundImage: `url(${img || config[pageType].img})` }}
        />
      </div>
      <div className={'content'}>
        <h1>{title || config[pageType].title}</h1>
        <div className={'desc'}>{desc || config[pageType].desc}</div>
        <div className={'actions'}>
          {actions ||
            React.createElement(
              linkElement,
              {
                to: redirect,
                href: redirect,
              },
              <Button type="primary">{backText}</Button>,
            )}
        </div>
      </div>
    </div>
  );
});

export default Exception;
