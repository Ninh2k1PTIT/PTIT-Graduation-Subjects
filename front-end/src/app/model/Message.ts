export class Message {
    content: string
    createdAt?: Date
    userId?: number
    roomId: number
    photos: MessagePhoto[]
}

export class MessagePhoto {
    content: string
}