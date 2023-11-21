import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";

import { NgbModule } from "@ng-bootstrap/ng-bootstrap";
import { PerfectScrollbarModule } from "ngx-perfect-scrollbar";

import { CoreSidebarModule } from "@core/components";
import { CoreCommonModule } from "@core/common.module";
import { ChatContentComponent } from "./chat-content/chat-content.component";
import { ChatActiveSidebarComponent } from "./chat-sidebars/chat-active-sidebar/chat-active-sidebar.component";
import { ChatSidebarComponent } from "./chat-sidebars/chat-sidebar/chat-sidebar.component";
import { ChatComponent } from "./chat.component";
import { ChatService } from "./chat.service";
import { PickerModule } from "@ctrl/ngx-emoji-mart";
import { EmojiModule } from "@ctrl/ngx-emoji-mart/ngx-emoji";

// routing
const routes: Routes = [
  {
    path: "",
    component: ChatComponent,
    resolve: {
      chatData: ChatService,
    },
  },
];

@NgModule({
  declarations: [
    ChatComponent,
    ChatContentComponent,
    ChatSidebarComponent,
    ChatActiveSidebarComponent,
  ],
  imports: [
    CommonModule,
    CoreSidebarModule,
    RouterModule.forChild(routes),
    CoreCommonModule,
    PerfectScrollbarModule,
    NgbModule,
    EmojiModule,
    PickerModule,
  ],
  providers: [ChatService],
})
export class ChatModule {}
