export class TagModel {
  constructor(
    readonly id: string,
    readonly description: string,
    readonly order: number,
    readonly type: string
  ) {}
}
