export class TagModel {
  constructor(
    readonly id: number,
    public description: string,
    public importedSourceName: string,
    public ranking: number,
    public type: string
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
