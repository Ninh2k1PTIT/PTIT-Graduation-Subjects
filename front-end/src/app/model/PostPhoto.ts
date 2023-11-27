import { User } from "./User"

export class PostPhoto {
    id?: number
    content: string
    name: string
    type: string
    tags: Tag[]
}

export class Tag {
    offsetTop: number
    offsetLeft: number
    user: User
}