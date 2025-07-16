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
    readonly url: string | undefined,
    readonly recommendedNumberOfPlayers: number[],
    readonly bestNumberOfPlayers: number[],
    readonly addedToGameGroupDate: number | undefined
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
    public globalTags: TagModel[],
    readonly recommendedNumberOfPlayers: number[],
    readonly bestNumberOfPlayers: number[]
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
      url,
      recommendedNumberOfPlayers,
      bestNumberOfPlayers
    )
  }
}

export class RatedGame extends Game {
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
    readonly tags: TagWrapper,
    public rating: Rating,
    readonly recommendedNumberOfPlayers: number[],
    readonly bestNumberOfPlayers: number[]
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
      url,
      recommendedNumberOfPlayers,
      bestNumberOfPlayers
    )
  }
}

export class TagWrapper {
  constructor(
    readonly global: TagInGameGroup[],
    readonly group: TagInGameGroup[],
    readonly player: PlayerTagInGameGroup[]
  ) {}
}

export class TagInGameGroup {
  constructor(
    readonly id: number,
    readonly description: string
  ) {}
}

export class PlayerTagInGameGroup {
  constructor(
    readonly id: number,
    readonly description: string,
    readonly playerId: number,
  ) {}
}

