import { Routes } from '@angular/router';

import {
    userMgmtRoute,
    userDialogRoute
} from './';

import { UserRouteAccessService } from '../shared';

const ADMIN_ROUTES = [
    ...userMgmtRoute,
];

export const adminState: Routes = [{
    path: '',
    data: {
        authorities: ['ROLE_ADMIN']
    },
    canActivate: [UserRouteAccessService],
    children: ADMIN_ROUTES
},
    ...userDialogRoute
];
