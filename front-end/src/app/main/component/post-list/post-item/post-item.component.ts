import { Component, Input, OnInit } from '@angular/core';
import { AuthenticationService } from 'app/auth/service';
import { Post } from 'app/model/Post';
import { User } from 'app/model/User';

@Component({
  selector: 'app-post-item',
  templateUrl: './post-item.component.html',
  styleUrls: ['./post-item.component.scss']
})
export class PostItemComponent implements OnInit {
  @Input('post') post: Post
  public currentUser: User

  constructor(private _authService: AuthenticationService) { }

  ngOnInit(): void {
    this.currentUser = this._authService.currentUserValue
  }

  convertCoordinates(imageId: string, left: number, top: number) {
    const img = document.getElementById(imageId) as HTMLImageElement;
    const ratioX = img.naturalWidth / img.offsetWidth;
    const ratioY = img.naturalHeight / img.offsetHeight;

    return {
      left: left / ratioX + "px",
      top: top / ratioY + "px",
    };
  }

}
