import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { ActivatedRouteSnapshot, RouterStateSnapshot } from "@angular/router";
import { Client } from "@stomp/stompjs";
import { AuthenticationService } from "app/auth/service";
import { Message } from "app/model/Message";
import { Room } from "app/model/Room";
import { environment } from "environments/environment";

import { BehaviorSubject, Observable, Subject } from "rxjs";
import SockJS from "sockjs-client";

@Injectable()
export class ChatService {
  public contacts: any[];
  public rooms: Room[];

  public isChatOpen: Boolean;
  public chatUsers: any[];
  public onChatsChange: BehaviorSubject<any>;
  public onSelectedChatChange: BehaviorSubject<any>;
  public onSelectedRoomChange: BehaviorSubject<Room>;
  public onChatUsersChange: BehaviorSubject<any>;
  public onChatOpenChange: BehaviorSubject<Boolean>;
  public onUserProfileChange: BehaviorSubject<any>;
  public stompClient: Client;
  public onReceiveMessage: Subject<Message>;

  constructor(
    private _httpClient: HttpClient,
    private _authService: AuthenticationService
  ) {
    this.isChatOpen = false;
    this.onChatsChange = new BehaviorSubject([]);
    this.onSelectedChatChange = new BehaviorSubject([]);
    this.onSelectedRoomChange = new BehaviorSubject(null);
    this.onChatUsersChange = new BehaviorSubject([]);
    this.onChatOpenChange = new BehaviorSubject(false);
    this.onUserProfileChange = new BehaviorSubject([]);
    this.onReceiveMessage = new Subject();

    this.stompClient = new Client({
      webSocketFactory: () => {
        return new SockJS(`${environment.apiUrl}/socket`);
      },
      onConnect: (frame) => {
        this.stompClient.subscribe(
          `/topic/${_authService.currentUserValue.id}`,
          (message) => {
            const body: Message = JSON.parse(message.body)
            this.onReceiveMessage.next(JSON.parse(message.body))
          }
        );
      },
    });
    this.stompClient.activate();
  }

  /**
   * Resolver
   *
   * @param {ActivatedRouteSnapshot} route
   * @param {RouterStateSnapshot} state
   * @returns {Observable<any> | Promise<any> | any}
   */
  resolve(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<any> | Promise<any> | any {
    return new Promise<void>((resolve, reject) => {
      Promise.all([this.getRooms()]).then(() => {
        resolve();
      }, reject);
    });
  }

  /**
   * Get Chats
   */
  getRooms(): Promise<any[]> {
    return new Promise((resolve, reject) => {
      this._httpClient
        .get<Room[]>(`${environment.apiUrl}/rooms`)
        .subscribe((res) => {
          this.rooms = res;
          this.onChatsChange.next(this.rooms);

          resolve(this.rooms);
        }, reject);
    });
  }

  getSelectedRoom(roomId) {
    const selectRoom = this.rooms.find((room) => room.id === roomId);
    this.onSelectedRoomChange.next(selectRoom);
  }

  /**
   * Selected Chats
   *
   * @param id
   */
  selectedRooms(id: number) {
    this._httpClient
      .get<Message[]>(`${environment.apiUrl}/room/${id}/chats`)
      .subscribe((res) => {
        this.onSelectedChatChange.next(res);
        this.getSelectedRoom(id);
      });
  }

  /**
   * Create New Chat
   *
   * @param id
   * @param chat
   */
  createNewChat(id, chat) {
    const newChat = {
      userId: id,
      unseenMsgs: 0,
      chat: [chat],
    };

    if (chat.message !== "") {
      return new Promise<void>((resolve, reject) => {
        this._httpClient
          .post("api/chat-chats/", { ...newChat })
          .subscribe(() => {
            this.getRooms();
            this.getSelectedRoom(id);
            this.openChat(id);
            resolve();
          }, reject);
      });
    }
  }

  /**
   * Open Chat
   *
   * @param id
   */
  openChat(id) {
    this.isChatOpen = true;
    this.onChatOpenChange.next(this.isChatOpen);
    this.selectedRooms(id);
  }

  /**
   * Update Chat
   *
   * @param chats
   */
  updateChat(chats) {
    return new Promise<void>((resolve, reject) => {
      this._httpClient
        .post("api/chat-chats/" + chats.id, { ...chats })
        .subscribe(() => {
          this.getRooms();
          resolve();
        }, reject);
    });
  }

  sendMessage(message: Message) {
    return this._httpClient.post<Message>(
      `${environment.apiUrl}/chat`,
      message
    );
  }
}
