import * as d3 from 'd3';
import { parse, stringify } from 'qs';

d3.selection.prototype.last = function() {
  return d3.select(this.nodes()[this.size() - 1]);
};

import * as v from './validator';

export const validator = v;

export const capitalizeFirstLetter = (str: string) => {
  return str.charAt(0).toUpperCase() + str.slice(1);
};

export const getHostname = (url: string) => {
  try {
    return new URL(url).hostname;
  } catch (e) {
    return url;
  }
};

export const qsParse = (search: string) => {
  return search[0] === '?' ? parse(search.slice(1)) : parse(search);
};

export const qsStringify = (search: { [key: string]: any }) => {
  Object.keys(search).forEach(key => {
    const value = search[key];
    if (!value) {
      delete search[key];
    } else if (typeof value === 'string') {
      search[key] = value.trim();
    }
  });
  return Object.keys(search) ? stringify(search) : '';
};

export const download = (target: any, type: string, name: string) => {
  const a = document.createElement('a');
  const blob = new Blob([`\ufeff${target}`], {
    type: 'text/plain;charset=utf-8',
  });

  a.download = `${name || 'untitled'}.${type}`;
  a.href = URL.createObjectURL(blob);

  // for firefox
  document.body.appendChild(a);
  a.click();

  setTimeout(() => {
    window.URL.revokeObjectURL(a.href);
    document.body.removeChild(a);
  }, 10);
};
