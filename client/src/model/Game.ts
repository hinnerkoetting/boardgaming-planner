import type { Rating } from './Rating'
import type { TagModel } from './TagModel'

export class Game {
  constructor(
    readonly id: number | undefined,
    readonly name: string,
    readonly description: string,
    readonly imageUrl: string,
    readonly thumbnailUrl: string
  ) {}
}

export class AdminGame {
  constructor(
    readonly id: number | undefined,
    readonly name: string,
    readonly description: string,
    readonly imageUrl: string,
    readonly thumbnailUrl: string,
    readonly globalTags: TagModel[]
  ) {}
}

export class GameGroupGame {
  constructor(
    readonly id: number | undefined,
    readonly name: string,
    readonly description: string,
    readonly imageUrl: string,
    readonly thumbnailUrl: string,
    readonly tags: TagInGameGroup[],
    public rating: Rating
  ) {}
}

export class TagInGameGroup {
  constructor(
    readonly id: number,
    readonly description: string
  ) {}
}
