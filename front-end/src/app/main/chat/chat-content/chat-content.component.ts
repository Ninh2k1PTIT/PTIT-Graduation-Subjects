import {
  Component,
  ElementRef,
  HostListener,
  OnInit,
  ViewChild,
} from "@angular/core";
import { CoreSidebarService } from "@core/components/core-sidebar/core-sidebar.service";
import { EmojiEvent } from "@ctrl/ngx-emoji-mart/ngx-emoji";
import { AuthenticationService } from "app/auth/service";
import { Message } from "app/model/Message";
import { Room } from "app/model/Room";
import { User } from "app/model/User";
import { ChatService } from "../chat.service";

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

  updateChat() {
    if (this.chatMessage) {
      this._chatService
        .sendMessage({
          roomId: this.room.id,
          content: this.chatMessage,
          photos: [],
        })
        .subscribe(() => {
          this.chatMessage = "";
        });
    }
  }

  toggleSidebar(name) {
    this._coreSidebarService.getSidebarRegistry(name).toggleOpen();
  }

  ngOnInit(): void {
    this.currentUser = this._authService.currentUserValue;

    this._chatService.onReceiveMessage.subscribe((res) => {
      if (this.room?.id === res.roomId) {
        this.chats.push(res);
        setTimeout(() => {
          this.scrolltop = this.scrollMe?.nativeElement.scrollHeight;
        }, 0);
      }
    });

    // Subscribe to Chat Change
    this._chatService.onChatOpenChange.subscribe((res) => {
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

  async onFileInput(event: Event) {
    const target = event.target as HTMLInputElement;
    const files = target.files;

    if (files.length > 0) {
      this._chatService
        .sendMessage({
          roomId: this.room.id,
          content: null,
          photos: [
            {
              content: await this.convertFileToUrl(files.item(0)),
            },
          ],
        })
        .subscribe();
    }
  }

  convertFileToUrl(file: File) {
    return new Promise<string>((resolve, reject) => {
      const reader = new FileReader();
      reader.onload = () => {
        resolve(reader.result.toString());
      };
      reader.readAsDataURL(file);
    });
  }
}
