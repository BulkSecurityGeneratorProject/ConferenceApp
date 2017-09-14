import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('Location e2e test', () => {

    let navBarPage: NavBarPage;
    let locationDialogPage: LocationDialogPage;
    let locationComponentsPage: LocationComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Locations', () => {
        navBarPage.goToEntity('location');
        locationComponentsPage = new LocationComponentsPage();
        expect(locationComponentsPage.getTitle()).toMatch(/conferenceappApp.location.home.title/);

    });

    it('should load create Location dialog', () => {
        locationComponentsPage.clickOnCreateButton();
        locationDialogPage = new LocationDialogPage();
        expect(locationDialogPage.getModalTitle()).toMatch(/conferenceappApp.location.home.createOrEditLabel/);
        locationDialogPage.close();
    });

    it('should create and save Locations', () => {
        locationComponentsPage.clickOnCreateButton();
        locationDialogPage.setNameInput('name');
        expect(locationDialogPage.getNameInput()).toMatch('name');
        locationDialogPage.setHallNumberInput('hallNumber');
        expect(locationDialogPage.getHallNumberInput()).toMatch('hallNumber');
        locationDialogPage.save();
        expect(locationDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class LocationComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('fish-location div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class LocationDialogPage {
    modalTitle = element(by.css('h4#myLocationLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    nameInput = element(by.css('input#field_name'));
    hallNumberInput = element(by.css('input#field_hallNumber'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setNameInput = function (name) {
        this.nameInput.sendKeys(name);
    }

    getNameInput = function () {
        return this.nameInput.getAttribute('value');
    }

    setHallNumberInput = function (hallNumber) {
        this.hallNumberInput.sendKeys(hallNumber);
    }

    getHallNumberInput = function () {
        return this.hallNumberInput.getAttribute('value');
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
