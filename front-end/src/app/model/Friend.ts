import { User } from "./User";

export class Friend {
  id: number;
  sender: User;
  receiver: User;
  isFriend: boolean;
}
