import { gluer } from 'glue-redux';

import { SELECTNODE } from './initialState';

// 选择节点
const SelectNode = gluer((data: any) => {
  return {
    data,
  };
}, SELECTNODE);

const nodeList = {
  SelectNode,
};

export default nodeList;
