import { User } from "./User";

export class Photo {
  file: File;
  b64: string;
  type: string
  tags?: Tag[]
}

export class Tag {
  top: number
  left: number
  user: User
}