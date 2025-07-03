export class GameGroupEvent {
  constructor(
    readonly sourcePlayerId: number,
    readonly sourcePlayerName: string,
    readonly gameGroupId: number,
    readonly eventType: string,
    readonly description: string
  ) {}
}
