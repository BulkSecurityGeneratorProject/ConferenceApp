import { Injectable } from '@angular/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';

import { FishLoginModalComponent } from './login.component';

@Injectable()
export class LoginModalService {
    private isOpen = false;
    constructor(
        private modalService: NgbModal,
    ) {}

    open(): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;
        const modalRef = this.modalService.open(FishLoginModalComponent, {
            container: 'nav'
        });
        modalRef.result.then((result) => {
            this.isOpen = false;
        }, (reason) => {
            this.isOpen = false;
        });
        return modalRef;
    }
}
