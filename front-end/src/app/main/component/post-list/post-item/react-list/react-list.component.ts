import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Post } from 'app/model/Post';
import { PostService } from 'app/services/post.service';

@Component({
  selector: 'app-react-list',
  templateUrl: './react-list.component.html',
  styleUrls: ['./react-list.component.scss']
})
export class ReactListComponent implements OnInit {
  @Input('post') public post: Post
  @ViewChild('modal') public modal: NgbActiveModal

  constructor(private _modalService: NgbModal, private _postService: PostService) { }

  ngOnInit(): void {
  }

  modalOpen() {
    this._modalService.open(this.modal, {
      scrollable: true
    });
  }

  updateReact() {
    console.log(this.post.id);
    this._postService.react(this.post.id).subscribe(res => {
      this.post.isReact = res.data
    })
  }
}
