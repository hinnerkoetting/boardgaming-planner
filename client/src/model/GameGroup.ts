export class GameGroup {
  constructor(
    readonly id: number | undefined,
    readonly name: string,
    readonly type: 'PERSONAL' | 'PUBLIC' | 'PRIVATE',
  ) { }
}
