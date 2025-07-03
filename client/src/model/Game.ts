export class Game {
  constructor(
    readonly id: Number | undefined,
    readonly name: String,
    readonly description: String,
    readonly imageUrl: String,
    readonly thumbnailUrl: String
  ) {}
}
