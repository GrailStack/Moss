import PageTitle from '@/components/PageTitle';
import { Col, Layout, Row } from 'antd';
import React from 'react';

const SwitchPushLog = React.memo(() => {
  return (
    <React.Fragment>
      <Layout className="page">
        <Row>
          <Col span={24}>
            <PageTitle name="推送记录" info="推送记录" />
          </Col>
        </Row>
      </Layout>
    </React.Fragment>
  );
});

export default SwitchPushLog;
