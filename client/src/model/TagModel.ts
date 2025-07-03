export class TagModel {
  constructor(
    readonly id: number,
    readonly description: string,
    readonly ranking: number,
    readonly type: string
  ) {}
}

export class CreateTagModel {
  constructor(
    readonly description: string,
    readonly ranking: number | null,
    readonly type: string
  ) {}
}
