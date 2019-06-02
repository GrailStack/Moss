'use strict';
module.exports = {
  extends: ['stylelint-config-standard', 'stylelint-config-recess-order'],
  rules: { 'no-descending-specificity': null },
  ignoreFiles: ['**/*.js', '**/*.jsx', '**/*.ts', '**/*.tsx'],
};
