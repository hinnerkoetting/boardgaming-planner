export class Interest {
  constructor(
    readonly playerId: number,
    readonly gameId: number,
    readonly gameGroupId: number,
    readonly rating: number | undefined
  ) {}
}
