import { Component, OnInit } from "@angular/core";
import { first } from "rxjs/operators";

import { CoreSidebarService } from "@core/components/core-sidebar/core-sidebar.service";
import { ChatService } from "../../chat.service";
import { AuthenticationService } from "app/auth/service";
import { Message } from "app/model/Message";
import { Room } from "app/model/Room";
import { User } from "app/model/User";

@Component({
  selector: "app-chat-sidebar",
  templateUrl: "./chat-sidebar.component.html",
})
export class ChatSidebarComponent implements OnInit {
  // Public
  public contacts;
  public rooms: Room[] = [];
  public searchText;
  public chats;
  public selectedIndex = null;
  public currentUser: User;

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

    // Reset unread Message to zero
    // this.chatUsers.map(user => {
    //   if (user.id === id) {
    //     user.unseenMsgs = 0;
    //   }
    // });
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
