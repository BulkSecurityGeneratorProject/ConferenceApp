import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { ConferenceappSessionModule } from './session/session.module';
import { ConferenceappSpeakerModule } from './speaker/speaker.module';
import { ConferenceappLocationModule } from './location/location.module';
/* needle-add-entity-module-import - add entity modules imports here */

@NgModule({
    imports: [
        ConferenceappSessionModule,
        ConferenceappSpeakerModule,
        ConferenceappLocationModule,
        /* needle-add-entity-module - add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ConferenceappEntityModule {}
