import type { GameGroupEvent } from './GameGroupEvent'
import { fetchEventSource } from '@microsoft/fetch-event-source'

const controller = new AbortController()
let existingSubscription = false

export function subscribeToEventsOnGameGroup(
  gameGroupId: number,
  listener: (data: GameGroupEvent) => void
) {
  if (existingSubscription) {
    controller.abort()
  }

  fetchEventSource(`/api/sse/gameGroup/${gameGroupId}`, {
    headers: {
      Authorization: 'Bearer ' + localStorage.getItem('access-token')
    },
    onmessage(event) {
      const data = JSON.parse(event.data) as GameGroupEvent
      listener(data)
    },
    signal: controller.signal
  })
  existingSubscription = true
}
