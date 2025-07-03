import type { GameGroupEvent } from "./GameGroupEvent";

let existingSource: EventSource | null = null

export function subscribeToEventsOnGameGroup(gameGroupId: number, listener: (data: GameGroupEvent) => void) {
  if (existingSource) {
    existingSource.close();
  }
  const source =new EventSource(`/api/sse/gameGroup/${gameGroupId}`)
  source.addEventListener("GROUP_CHANGED", (event) => {   
    const data = JSON.parse(event.data) as GameGroupEvent;
    listener(data);
  });
  existingSource = source;
}
