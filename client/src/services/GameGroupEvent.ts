export class GameGroupEvent {
  constructor(
    readonly source: string,
    readonly gameGroupId: number,
    readonly eventType: string,
    readonly description: string
  ) {}
}
