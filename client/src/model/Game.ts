import type { Rating } from './Rating'
import type { TagModel } from './TagModel'

export class Game {
  constructor(
    readonly id: number | undefined,
    readonly name: string,
    readonly description: string,
    readonly imageUrl: string,
    readonly thumbnailUrl: string,
    readonly minPlayers: number | undefined,
    readonly maxPlayers: number | undefined,
    readonly playingTimeMinutes: number | undefined,
    readonly url: string | undefined
  ) {}
}

export class AdminGame extends Game {
  constructor(
    readonly id: number | undefined,
    readonly name: string,
    readonly description: string,
    readonly imageUrl: string,
    readonly thumbnailUrl: string,
    readonly minPlayers: number | undefined,
    readonly maxPlayers: number | undefined,
    readonly playingTimeMinutes: number | undefined,
    readonly url: string | undefined,
    readonly globalTags: TagModel[]
  ) {
    super(
      id,
      name,
      description,
      imageUrl,
      thumbnailUrl,
      minPlayers,
      maxPlayers,
      playingTimeMinutes,
      url
    )
  }
}

export class GameGroupGame extends Game {
  constructor(
    readonly id: number | undefined,
    readonly name: string,
    readonly description: string,
    readonly imageUrl: string,
    readonly thumbnailUrl: string,
    readonly minPlayers: number | undefined,
    readonly maxPlayers: number | undefined,
    readonly playingTimeMinutes: number | undefined,
    readonly url: string | undefined,
    readonly tags: TagInGameGroup[],
    public rating: Rating
  ) {
    super(
      id,
      name,
      description,
      imageUrl,
      thumbnailUrl,
      minPlayers,
      maxPlayers,
      playingTimeMinutes,
      url
    )
  }
}

export class TagInGameGroup {
  constructor(
    readonly id: number,
    readonly description: string
  ) {}
}
