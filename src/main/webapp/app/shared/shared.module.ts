import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { DatePipe } from '@angular/common';

import {
    ConferenceappSharedLibsModule,
    ConferenceappSharedCommonModule,
    CSRFService,
    AuthServerProvider,
    AccountService,
    UserService,
    StateStorageService,
    LoginService,
    LoginModalService,
    Principal,
    HasAnyAuthorityDirective,
    FishLoginModalComponent
} from './';

@NgModule({
    imports: [
        ConferenceappSharedLibsModule,
        ConferenceappSharedCommonModule
    ],
    declarations: [
        FishLoginModalComponent,
        HasAnyAuthorityDirective
    ],
    providers: [
        LoginService,
        LoginModalService,
        AccountService,
        StateStorageService,
        Principal,
        CSRFService,
        AuthServerProvider,
        UserService,
        DatePipe
    ],
    entryComponents: [FishLoginModalComponent],
    exports: [
        ConferenceappSharedCommonModule,
        FishLoginModalComponent,
        HasAnyAuthorityDirective,
        DatePipe
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]

})
export class ConferenceappSharedModule {}
