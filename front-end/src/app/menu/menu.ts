import { CoreMenu } from '@core/types'

export const menu: CoreMenu[] = [
  {
    id: 'home',
    title: 'Trang chủ',
    type: 'item',
    icon: 'home',
    url: 'home'
  },
  {
    id: 'friend',
    title: 'Bạn bè',
    type: 'item',
    icon: 'users',
    url: 'friend'
  },
  {
    id: 'chat',
    title: 'Tin nhắn',
    type: 'item',
    icon: 'send',
    url: 'chat'
  },
  {
    id: 'log',
    title: 'Nhật kí hoạt động',
    type: 'item',
    icon: 'list',
    url: 'log'
  }
]
