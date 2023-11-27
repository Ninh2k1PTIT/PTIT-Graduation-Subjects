import { Component, OnInit } from "@angular/core";
import {
  debounceTime,
  distinctUntilChanged,
  first,
  switchMap,
} from "rxjs/operators";

import { CoreSidebarService } from "@core/components/core-sidebar/core-sidebar.service";
import { ChatService } from "../../chat.service";
import { AuthenticationService } from "app/auth/service";
import { Message } from "app/model/Message";
import { Room } from "app/model/Room";
import { User } from "app/model/User";
import { FormControl } from "@angular/forms";
import { of } from "rxjs";

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

  /**
   * Constructor
   *
   * @param {ChatService} _chatService
   * @param {CoreSidebarService} _coreSidebarService
   */
  constructor(
    private _chatService: ChatService,
    private _coreSidebarService: CoreSidebarService,
    private _authService: AuthenticationService
  ) {}

  // Public Methods
  // -----------------------------------------------------------------------------------------------------

  /**
   * Open Chat
   *
   * @param id
   * @param newChat
   */
  openChat(id) {
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

  /**
   * Toggle Sidebar
   *
   * @param name
   */
  toggleSidebar(name) {
    this._coreSidebarService.getSidebarRegistry(name).toggleOpen();
  }

  /**
   * Set Index
   *
   * @param index
   */
  setIndex(index: number) {
    this.selectedIndex = index;
  }

  // Lifecycle Hooks
  // -----------------------------------------------------------------------------------------------------

  /**
   * On init
   */
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

    let skipFirst = 0;

    // Subscribe to chat users
    this._chatService.onChatUsersChange.subscribe((res) => {
      this.rooms = res;

      // Skip setIndex first time when initialized
      if (skipFirst >= 1) {
        this.setIndex(this.rooms.length - 1);
      }
      skipFirst++;
    });

    // Subscribe to selected Chats
    this._chatService.onSelectedChatChange.subscribe((res) => {
      this.chats = res;
    });

    // Add Unseen Message To Chat User
    this._chatService.onChatsChange.pipe(first()).subscribe((rooms) => {
      console.log(rooms);
      
      this.rooms = rooms;

      // chats.map(chat => {
      //   this.chatUsers.map(user => {
      //     if (user.id === chat.userId) {
      //       user.unseenMsgs = chat.unseenMsgs;
      //     }
      //   });
      // });
    });
  }

  formatLastMessage(room: Room) {
    if (room.lastMessage) {
      return (
        (room.lastMessage?.userId == this.currentUser.id
          ? "Tôi: "
          : room.users[0].id == this.currentUser.id
          ? room.users[1].username
          : room.users[0].username + ": ") + room.lastMessage.content
      );
    } else return null;
  }
}
