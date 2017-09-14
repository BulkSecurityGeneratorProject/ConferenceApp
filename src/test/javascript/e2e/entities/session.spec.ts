import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('Session e2e test', () => {

    let navBarPage: NavBarPage;
    let sessionDialogPage: SessionDialogPage;
    let sessionComponentsPage: SessionComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Sessions', () => {
        navBarPage.goToEntity('session');
        sessionComponentsPage = new SessionComponentsPage();
        expect(sessionComponentsPage.getTitle()).toMatch(/conferenceappApp.session.home.title/);

    });

    it('should load create Session dialog', () => {
        sessionComponentsPage.clickOnCreateButton();
        sessionDialogPage = new SessionDialogPage();
        expect(sessionDialogPage.getModalTitle()).toMatch(/conferenceappApp.session.home.createOrEditLabel/);
        sessionDialogPage.close();
    });

    it('should create and save Sessions', () => {
        sessionComponentsPage.clickOnCreateButton();
        sessionDialogPage.setTitleInput('title');
        expect(sessionDialogPage.getTitleInput()).toMatch('title');
        sessionDialogPage.setDetailInput('detail');
        expect(sessionDialogPage.getDetailInput()).toMatch('detail');
        sessionDialogPage.typeSelectLastOption();
        sessionDialogPage.setDateInput('2000-12-31');
        expect(sessionDialogPage.getDateInput()).toMatch('2000-12-31');
        sessionDialogPage.locationSelectLastOption();
        // sessionDialogPage.speakersSelectLastOption();
        sessionDialogPage.save();
        expect(sessionDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class SessionComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('fish-session div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class SessionDialogPage {
    modalTitle = element(by.css('h4#mySessionLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    titleInput = element(by.css('input#field_title'));
    detailInput = element(by.css('input#field_detail'));
    typeSelect = element(by.css('select#field_type'));
    dateInput = element(by.css('input#field_date'));
    locationSelect = element(by.css('select#field_location'));
    speakersSelect = element(by.css('select#field_speakers'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setTitleInput = function (title) {
        this.titleInput.sendKeys(title);
    }

    getTitleInput = function () {
        return this.titleInput.getAttribute('value');
    }

    setDetailInput = function (detail) {
        this.detailInput.sendKeys(detail);
    }

    getDetailInput = function () {
        return this.detailInput.getAttribute('value');
    }

    setTypeSelect = function (type) {
        this.typeSelect.sendKeys(type);
    }

    getTypeSelect = function () {
        return this.typeSelect.element(by.css('option:checked')).getText();
    }

    typeSelectLastOption = function () {
        this.typeSelect.all(by.tagName('option')).last().click();
    }
    setDateInput = function (date) {
        this.dateInput.sendKeys(date);
    }

    getDateInput = function () {
        return this.dateInput.getAttribute('value');
    }

    locationSelectLastOption = function () {
        this.locationSelect.all(by.tagName('option')).last().click();
    }

    locationSelectOption = function (option) {
        this.locationSelect.sendKeys(option);
    }

    getLocationSelect = function () {
        return this.locationSelect;
    }

    getLocationSelectedOption = function () {
        return this.locationSelect.element(by.css('option:checked')).getText();
    }

    speakersSelectLastOption = function () {
        this.speakersSelect.all(by.tagName('option')).last().click();
    }

    speakersSelectOption = function (option) {
        this.speakersSelect.sendKeys(option);
    }

    getSpeakersSelect = function () {
        return this.speakersSelect;
    }

    getSpeakersSelectedOption = function () {
        return this.speakersSelect.element(by.css('option:checked')).getText();
    }

    save() {
        this.saveButton.click();
    }

    close() {
        this.closeButton.click();
    }

    getSaveButton() {
        return this.saveButton;
    }
}
