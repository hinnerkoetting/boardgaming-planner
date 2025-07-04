import type { Game } from "./Game";
import type { Player } from "./Player/Player";

export class GamingEvent {
  constructor(
    readonly id: number,
    public description: string,
    public start: number,
    public end: number,
    public participants: Participation[],
    public games: EventGame[]
  ) {}
}

export class Participation {
  constructor(
    readonly participant: Player,
    public participationStatus: ParticipationStatus,
    readonly comment: string | null = null
  ) {}
}

export class EventGame {
  constructor(
    readonly game: Game,
    readonly gameStatus: GameStatus,
    readonly comment: string | null = null
  ) {}
}


export type ParticipationStatus = 'CONFIRMED' | 'DECLINED' | 'MAYBE' | 'NOT_RESPONDED'
export type GameStatus = 'SUGGESTED' | 'REJECTED' | 'PLAYED'