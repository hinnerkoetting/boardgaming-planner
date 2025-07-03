import { Game } from './Game'
import type { Rating } from './Rating'

export class RatedGame extends Game {
  constructor(
    public rating: Rating,
    readonly id: number | undefined,
    readonly name: string,
    readonly description: string,
    readonly imageUrl: string,
    readonly thumbnailUrl: string
  ) {
    super(id, name, description, imageUrl, thumbnailUrl)
  }
}
