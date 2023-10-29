import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-react-list',
  templateUrl: './react-list.component.html',
  styleUrls: ['./react-list.component.scss']
})
export class ReactListComponent implements OnInit {
  @Input('reacts') public reacts
  @ViewChild('modal') public modal: NgbActiveModal

  constructor(private _modalService: NgbModal) { }

  ngOnInit(): void {
  }

  modalOpen() {
    this._modalService.open(this.modal, {
      scrollable: true
    });
  }
}
