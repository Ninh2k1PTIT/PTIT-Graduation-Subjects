import { Component, OnInit, ViewEncapsulation } from '@angular/core';

@Component({
  selector: 'app-friend',
  templateUrl: './friend.component.html',
  styleUrls: ['./friend.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class FriendComponent implements OnInit {
  public suggestions = [
    {
      avatar: 'assets/images/portrait/small/avatar-s-9.jpg',
      name: 'Peter Reed',
      mutualFriend: '6 Mutual Friends'
    },
    {
      avatar: 'assets/images/portrait/small/avatar-s-6.jpg',
      name: 'Harriett Adkins',
      mutualFriend: '3 Mutual Friends'
    },
    {
      avatar: 'assets/images/portrait/small/avatar-s-7.jpg',
      name: 'Juan Weaver',
      mutualFriend: '1 Mutual Friends'
    },
    {
      avatar: 'assets/images/portrait/small/avatar-s-8.jpg',
      name: 'Claudia Chandler',
      mutualFriend: '16 Mutual Friends'
    },
    {
      avatar: 'assets/images/portrait/small/avatar-s-1.jpg',
      name: 'Earl Briggs',
      mutualFriend: '4 Mutual Friends'
    },
    {
      avatar: 'assets/images/portrait/small/avatar-s-10.jpg',
      name: 'Jonathan Lyons',
      mutualFriend: '25 Mutual Friends'
    }
  ]

  constructor() { }

  ngOnInit(): void {
  }

}
