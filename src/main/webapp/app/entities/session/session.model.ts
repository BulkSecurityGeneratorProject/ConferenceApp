import { BaseEntity } from './../../shared';

export const enum SessionType {
    'BOF',
    'CONFERENCE',
    'HANDS_ON_LAB',
    'IGNITE',
    'KEYNOTE',
    'TUTORIAL'
}

export class Session implements BaseEntity {
    constructor(
        public id?: number,
        public title?: string,
        public detail?: string,
        public type?: SessionType,
        public date?: any,
        public location?: BaseEntity,
        public speakers?: BaseEntity[],
    ) {
    }
}
