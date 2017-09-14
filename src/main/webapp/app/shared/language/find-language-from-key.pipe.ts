import { Pipe, PipeTransform } from '@angular/core';

@Pipe({name: 'findLanguageFromKey'})
export class FindLanguageFromKeyPipe implements PipeTransform {
    private languages: any = {
        'en': { name: 'English' },
        'it': { name: 'Italiano' },
        'fr': { name: 'Français' },
        'es': { name: 'Español' },
        'tr': { name: 'Türkçe' },
        'de': { name: 'Deutsch' },
        'hi': { name: 'हिंदी' },
        'ar-ly': { name: 'العربية' }
        // needle-i18n-language-key-pipe - add/remove languages in this object
    };
    transform(lang: string): string {
        return this.languages[lang].name;
    }
    isRTL(lang: string): boolean {
        return this.languages[lang].rtl;
    }
}
