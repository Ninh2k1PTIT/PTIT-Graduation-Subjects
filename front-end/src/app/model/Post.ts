import { PostPhoto } from "./PostPhoto";
import { User } from "./User";

export class Post {
    id: number;
    content: string;
    createdAt: Date;
    updatedAt: Date;
    audience: number;
    user: User;
    photos: PostPhoto[];
    totalReact: number;
    totalComment: number;
    isReact: boolean;
}