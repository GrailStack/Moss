import { gluer } from 'glue-redux';

const loading = gluer((data: number, state: number) => data + state, 0);

const notices = gluer([]);

export default {
  loading,
  notices,
};
