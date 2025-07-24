export class GameGroup {
  constructor(
    readonly id: number | undefined,
    readonly name: string,
    readonly type: GameGroupType
  ) { }
}

export enum GameGroupType { PERSONAL = 'PERSONAL', PUBLIC = 'PUBLIC', PRIVATE = 'PRIVATE' }

