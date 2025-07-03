export class Rating {
  constructor(
    public myRating: number | undefined,
    readonly averageRating: number,
    readonly existsVeto: boolean
  ) {}
}
