import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ConferenceappSharedModule } from '../shared';
import {
    adminState,
    UserMgmtComponent,
    UserDialogComponent,
    UserDeleteDialogComponent,
    UserMgmtDetailComponent,
    UserMgmtDialogComponent,
    UserMgmtDeleteDialogComponent,
    UserResolvePagingParams,
    UserResolve,
    UserModalService
} from './';

@NgModule({
    imports: [
        ConferenceappSharedModule,
        RouterModule.forRoot(adminState, { useHash: true })
    ],
    declarations: [

        UserMgmtComponent,
        UserDialogComponent,
        UserDeleteDialogComponent,
        UserMgmtDetailComponent,
        UserMgmtDialogComponent,
        UserMgmtDeleteDialogComponent
    ],
    entryComponents: [
        UserMgmtDialogComponent,
        UserMgmtDeleteDialogComponent
    ],
    providers: [
        UserResolvePagingParams,
        UserResolve,
        UserModalService
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ConferenceappAdminModule {}
