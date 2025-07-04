import type { Game } from "./Game";
import type { Player } from "./Player/Player";

export class GamingEvent {
  constructor(
    readonly id: number,
    public description: string,
    public start: number,
    public end: number,
    public participants: Participation[],
    public games: Game[]
  ) {}
}

export class Participation {
  constructor(
    readonly participant: Player,
    readonly participationStatus: 'ACCEPTED' | 'DECLINED' | 'MAYBE' | 'NOT_RESPONDED',
    readonly comment: string | null = null
  ) {}
}

export class EventGame {
  constructor(
    readonly game: Game,
    readonly gameStatus: 'SUGGESTED' | 'REJECTED' | 'PLAYED',
    readonly comment: string | null = null
  ) {}
}