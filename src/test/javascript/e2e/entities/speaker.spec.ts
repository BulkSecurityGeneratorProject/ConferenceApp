import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('Speaker e2e test', () => {

    let navBarPage: NavBarPage;
    let speakerDialogPage: SpeakerDialogPage;
    let speakerComponentsPage: SpeakerComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Speakers', () => {
        navBarPage.goToEntity('speaker');
        speakerComponentsPage = new SpeakerComponentsPage();
        expect(speakerComponentsPage.getTitle()).toMatch(/conferenceappApp.speaker.home.title/);

    });

    it('should load create Speaker dialog', () => {
        speakerComponentsPage.clickOnCreateButton();
        speakerDialogPage = new SpeakerDialogPage();
        expect(speakerDialogPage.getModalTitle()).toMatch(/conferenceappApp.speaker.home.createOrEditLabel/);
        speakerDialogPage.close();
    });

    it('should create and save Speakers', () => {
        speakerComponentsPage.clickOnCreateButton();
        speakerDialogPage.setSpeakerNameInput('speakerName');
        expect(speakerDialogPage.getSpeakerNameInput()).toMatch('speakerName');
        speakerDialogPage.setEmailInput('email');
        expect(speakerDialogPage.getEmailInput()).toMatch('email');
        speakerDialogPage.designationSelectLastOption();
        speakerDialogPage.setBioInput('bio');
        expect(speakerDialogPage.getBioInput()).toMatch('bio');
        speakerDialogPage.getFeaturedInput().isSelected().then(function (selected) {
            if (selected) {
                speakerDialogPage.getFeaturedInput().click();
                expect(speakerDialogPage.getFeaturedInput().isSelected()).toBeFalsy();
            } else {
                speakerDialogPage.getFeaturedInput().click();
                expect(speakerDialogPage.getFeaturedInput().isSelected()).toBeTruthy();
            }
        });
        speakerDialogPage.save();
        expect(speakerDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class SpeakerComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('fish-speaker div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class SpeakerDialogPage {
    modalTitle = element(by.css('h4#mySpeakerLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    speakerNameInput = element(by.css('input#field_speakerName'));
    emailInput = element(by.css('input#field_email'));
    designationSelect = element(by.css('select#field_designation'));
    bioInput = element(by.css('input#field_bio'));
    featuredInput = element(by.css('input#field_featured'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setSpeakerNameInput = function (speakerName) {
        this.speakerNameInput.sendKeys(speakerName);
    }

    getSpeakerNameInput = function () {
        return this.speakerNameInput.getAttribute('value');
    }

    setEmailInput = function (email) {
        this.emailInput.sendKeys(email);
    }

    getEmailInput = function () {
        return this.emailInput.getAttribute('value');
    }

    setDesignationSelect = function (designation) {
        this.designationSelect.sendKeys(designation);
    }

    getDesignationSelect = function () {
        return this.designationSelect.element(by.css('option:checked')).getText();
    }

    designationSelectLastOption = function () {
        this.designationSelect.all(by.tagName('option')).last().click();
    }
    setBioInput = function (bio) {
        this.bioInput.sendKeys(bio);
    }

    getBioInput = function () {
        return this.bioInput.getAttribute('value');
    }

    getFeaturedInput = function () {
        return this.featuredInput;
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
