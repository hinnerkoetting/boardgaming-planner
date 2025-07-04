export class TagModel {
  constructor(
    readonly id: number,
    public description: string,
    public importedSourceName: string,
    public ranking: number,
    public type: TagType
  ) {}
}

export class CreateTagModel {
  constructor(
    readonly description: string,
    public importedSourceName: string,
    readonly ranking: number | null,
    readonly type: string
  ) {}
}

export class PlayerTagModel {
  constructor(
    readonly id: number,
    public description: string,
    public importedSourceName: string,
    public ranking: number,
    public type: string,
    public playerId: number
  ) {}
}

export type TagType = 'GAME_GROUP' | 'PLAYER' | 'GLOBAL'