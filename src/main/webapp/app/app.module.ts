import './vendor.ts';

import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { Ng2Webstorage } from 'ng2-webstorage';

import { ConferenceappSharedModule, UserRouteAccessService } from './shared';
import { ConferenceappHomeModule } from './home/home.module';
import { ConferenceappAdminModule } from './admin/admin.module';
import { ConferenceappAccountModule } from './account/account.module';
import { ConferenceappEntityModule } from './entities/entity.module';

import { customHttpProvider } from './blocks/interceptor/http.provider';
import { PaginationConfig } from './blocks/config/uib-pagination.config';

import {
    FishMainComponent,
    LayoutRoutingModule,
    NavbarComponent,
    FooterComponent,
    ActiveMenuDirective,
    ErrorComponent
} from './layouts';

@NgModule({
    imports: [
        BrowserModule,
        LayoutRoutingModule,
        Ng2Webstorage.forRoot({ prefix: 'jhi', separator: '-'}),
        ConferenceappSharedModule,
        ConferenceappHomeModule,
        ConferenceappAdminModule,
        ConferenceappAccountModule,
        ConferenceappEntityModule
    ],
    declarations: [
        FishMainComponent,
        NavbarComponent,
        ErrorComponent,
        ActiveMenuDirective,
        FooterComponent
    ],
    providers: [
        customHttpProvider(),
        PaginationConfig,
        UserRouteAccessService
    ],
    bootstrap: [ FishMainComponent ]
})
export class ConferenceappAppModule {}
