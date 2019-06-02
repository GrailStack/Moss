import EventEmitter from 'events';

export const EventBus = new EventEmitter();
export const EVENTS = { COLLAPSE_CHANGE: 'COLLAPSE_CHANGE' };
