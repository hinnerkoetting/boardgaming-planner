import type { GameGroupEvent } from "./GameGroupEvent";

export function subscribeToEventsOnGameGroup(gameGroupId: number, listener: (data: GameGroupEvent) => void) {
  new EventSource(`/api/sse/gameGroup/${gameGroupId}`).addEventListener("GROUP_CHANGED", (event) => {   
    const data = JSON.parse(event.data) as GameGroupEvent;
    listener(data);
  });
}
