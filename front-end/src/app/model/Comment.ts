import { CommentPhoto } from "./CommentPhoto";
import { User } from "./User";

export class Comment {
  id: number;
  content: string;
  totalReact: number;
  isReact: boolean;
  createdAt: Date;
  updatedAt: Date;
  createdBy: User;
  postId: number;
  photos: CommentPhoto[];
}
