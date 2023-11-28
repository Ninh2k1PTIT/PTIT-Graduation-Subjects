import { Component, OnInit } from "@angular/core";
import {
  debounceTime,
  distinctUntilChanged,
  first,
  switchMap,
} from "rxjs/operators";

import { FormControl } from "@angular/forms";
import { CoreSidebarService } from "@core/components/core-sidebar/core-sidebar.service";
import { AuthenticationService } from "app/auth/service";
import { Room } from "app/model/Room";
import { User } from "app/model/User";
import { of } from "rxjs";
import { ChatService } from "../../chat.service";

@Component({
  selector: "app-chat-sidebar",
  templateUrl: "./chat-sidebar.component.html",
})
export class ChatSidebarComponent implements OnInit {
  // Public
  public contacts;
  public rooms: Room[] = [];
  public users: User[] = [];
  public searchText;
  public chats;
  public selectedIndex = null;
  public currentUser: User;
  public searchControl = new FormControl();

  constructor(
    private _chatService: ChatService,
    private _coreSidebarService: CoreSidebarService,
    private _authService: AuthenticationService
  ) {}

  openChat(id: number) {
    this.selectedIndex = id;
    this._chatService.openChat(id);
    this.users = [];
    this.searchControl.reset();
  }

  openUserChat(id: number) {
    const room = this.rooms.find((room) =>
      room.users.some((user) => user.id == id)
    );
    if (room) this.openChat(room.id);
    else
      this._chatService
        .createRoom({
          users: [{ id: id }, { id: this.currentUser.id }],
        })
        .subscribe((res) => {
          this._chatService.getRooms().then(() => {
            this.openChat(res.id);
          });
        });
  }


  toggleSidebar(name) {
    this._coreSidebarService.getSidebarRegistry(name).toggleOpen();
  }

  ngOnInit(): void {
    this.searchControl.valueChanges
      .pipe(
        debounceTime(500),
        distinctUntilChanged(),
        switchMap((username) =>
          username ? this._chatService.searchUser(username) : of([])
        )
      )
      .subscribe((res) => {
        this.users = res.filter((item) => item.id != this.currentUser.id);
      });

    this.currentUser = this._authService.currentUserValue;

    // Subscribe to chat users
    this._chatService.onChatUsersChange.subscribe((res) => {
      this.rooms = res;
    });

    // Subscribe to selected Chats
    this._chatService.onSelectedChatChange.subscribe((res) => {
      this.chats = res;
    });

    // Add Unseen Message To Chat User
    this._chatService.onChatsChange.pipe(first()).subscribe((rooms) => {
      this.rooms = rooms;
    });
  }

  formatLastMessage(room: Room) {
    if (room.lastMessage) {
      return (
        (room.lastMessage?.userId == this.currentUser.id
          ? "Tôi: "
          : room.users[0].id == this.currentUser.id
          ? room.users[1].username + ": "
          : room.users[0].username + ": ") +
        (room.lastMessage.photos.length > 0
          ? "Đã gửi 1 ảnh"
          : room.lastMessage.content)
      );
    } else return null;
  }

  formatTime(time: string) {
    if (time) {
      const minute = 60000;
      const hour = 3600000;
      const day = 86400000;
      const diff = new Date().getTime() - new Date(time).getTime();
      if (isNaN(diff)) return "";
      if (diff < minute) return "Vừa xong";
      if (diff < hour) return Math.floor(diff / minute) + " phút trước";
      if (diff < day) return Math.floor(diff / hour) + " giờ trước";
      return new Date(time).toLocaleDateString("en-GB");
    }
    return "";
  }
}
