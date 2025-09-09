import type { GamingEvent } from "./GamingEvent";

export class GameGroupStatistics {
  constructor(readonly events: GamingEvent[]) { }
}
