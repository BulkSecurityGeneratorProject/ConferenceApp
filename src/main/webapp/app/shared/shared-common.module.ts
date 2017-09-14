import { NgModule, LOCALE_ID } from '@angular/core';
import { Title } from '@angular/platform-browser';

import {
    ConferenceappSharedLibsModule,
    JhiLanguageHelper,
    FindLanguageFromKeyPipe,
    FishAlertComponent,
    FishAlertErrorComponent
} from './';

@NgModule({
    imports: [
        ConferenceappSharedLibsModule
    ],
    declarations: [
        FindLanguageFromKeyPipe,
        FishAlertComponent,
        FishAlertErrorComponent
    ],
    providers: [
        FindLanguageFromKeyPipe,
        JhiLanguageHelper,
        Title,
        {
            provide: LOCALE_ID,
            useValue: 'en'
        },
    ],
    exports: [
        ConferenceappSharedLibsModule,
        FindLanguageFromKeyPipe,
        FishAlertComponent,
        FishAlertErrorComponent
    ]
})
export class ConferenceappSharedCommonModule {}
