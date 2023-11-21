import {
  Component,
  ElementRef,
  HostListener,
  OnInit,
  ViewChild,
} from "@angular/core";
import { CoreSidebarService } from "@core/components/core-sidebar/core-sidebar.service";
import { ChatService } from "../chat.service";
import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";
import { environment } from "environments/environment";
import { AuthenticationService } from "app/auth/service";
import { User } from "app/model/User";
import { Message } from "app/model/Message";
import { EmojiEvent } from "@ctrl/ngx-emoji-mart/ngx-emoji";
import { Room } from "app/model/Room";

@Component({
  selector: "app-chat-content",
  templateUrl: "./chat-content.component.html",
  styleUrls: ["./chat-content.component.scss"],
})
export class ChatContentComponent implements OnInit {
  // Decorator
  @ViewChild("scrollMe") scrollMe: ElementRef;
  scrolltop: number = null;

  // Public
  public activeChat: Boolean;
  public chats: Message[] = [];
  public chatUser: User;
  public currentUser: User;
  public chatMessage = "";
  public newChat;
  public showEmojiPicker = false;
  public room: Room;

  @HostListener("document:click", ["$event"])
  onDocumentClick(event: Event) {
    this.showEmojiPicker = false;
  }

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
   * Update Chat
   */
  updateChat() {
    if (this.chatMessage) {
      this._chatService
        .sendMessage({ roomId: this.room.id, content: this.chatMessage })
        .subscribe((res) => {
          console.log(res);
        });
    }
    console.log(this.chatMessage);

    // this.newChat = {
    //   message: this.chatMessage,
    //   time: 'Mon Dec 10 2018 07:46:43 GMT+0000 (GMT)',
    //   senderId: this.userProfile.id
    // };
    // // If chat data is available (update chat)
    // if (this.chats.chat) {
    //   if (this.newChat.message !== '') {
    //     this.chats.chat.push(this.newChat);
    //     this._chatService.updateChat(this.chats);
    //     this.chatMessage = '';
    //     setTimeout(() => {
    //       this.scrolltop = this.scrollMe?.nativeElement.scrollHeight;
    //     }, 0);
    //   }
    // }
    // // Else create new chat
    // else {
    //   this._chatService.createNewChat(this.chatUser.id, this.newChat);
    // }
    // this._chatService.send().subscribe((res) => {
    //   console.log(res);
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

  // Lifecycle Hooks
  // -----------------------------------------------------------------------------------------------------

  /**
   * On init
   */
  ngOnInit(): void {
    this.currentUser = this._authService.currentUserValue;

    this._chatService.onReceiveMessage.subscribe((res) => {
      if (this.room.id === res.roomId) {
        this.chats.push(res);
        setTimeout(() => {
          this.scrolltop = this.scrollMe?.nativeElement.scrollHeight;
        }, 0);
      }
    });

    // Subscribe to Chat Change
    this._chatService.onChatOpenChange.subscribe((res) => {
      console.log(res);

      this.chatMessage = "";
      this.activeChat = res;

    });

    // Subscribe to Selected Chat Change
    this._chatService.onSelectedChatChange.subscribe((res) => {
      this.chats = res;
      setTimeout(() => {
        this.scrolltop = this.scrollMe?.nativeElement.scrollHeight;
      }, 0);
    });

    // Subscribe to Selected Chat User Change
    this._chatService.onSelectedRoomChange.subscribe((res) => {
      console.log(res);
      this.room = res;
      if (res)
        this.chatUser =
          this._authService.currentUserValue.id == res.users[0].id
            ? res.users[1]
            : res.users[0];
    });
  }

  openEmojiPicker(event: Event) {
    event.stopPropagation();
    this.showEmojiPicker = !this.showEmojiPicker;
  }

  emojiSelect(event: EmojiEvent) {
    this.chatMessage += event.emoji.native;
  }
}
