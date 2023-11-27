import { Message } from "./Message"
import { User } from "./User"

export class Room {
    id?: number
    users: User[]
    lastMessage?: Message
}