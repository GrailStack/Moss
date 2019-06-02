import fetch from '@/util/fetch';

import model from './model';

const selectNode = {
  /* 节点列表 */
  fetchApplicationNodelist: () => {
    return fetch({
      method: 'get',
      url: `api/applications/`,
    }).then((data: any) => {
      return Promise.resolve(model.SelectNode(data));
    });
  },
};

export default selectNode;
