export class Game {
  constructor(
    readonly id: number | undefined,
    readonly name: String,
    readonly description: String,
    readonly imageUrl: String,
    readonly thumbnailUrl: String
  ) {}
}
