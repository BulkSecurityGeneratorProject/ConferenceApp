import { BaseEntity } from './../../shared';

export const enum DesignationType {
    'JAVEONE_ROCKSTAR',
    'JAVA_CHAMPION',
    'ORACLE_ACE',
    'ORACLE_CERTIFIED',
    'ORACLE_DEVELOPER_CHAMPION'
}

export class Speaker implements BaseEntity {
    constructor(
        public id?: number,
        public speakerName?: string,
        public email?: string,
        public designation?: DesignationType,
        public bio?: string,
        public featured?: boolean,
        public sessions?: BaseEntity[],
    ) {
        this.featured = false;
    }
}
