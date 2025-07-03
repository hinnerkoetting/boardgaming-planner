import type { TagModel } from './TagModel'

export class Game {
  constructor(
    readonly id: number | undefined,
    readonly name: string,
    readonly description: string,
    readonly imageUrl: string,
    readonly thumbnailUrl: string,
    readonly globalTags: TagModel[]
  ) {}
}
