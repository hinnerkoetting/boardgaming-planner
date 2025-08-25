export class Rating {
  constructor(
    public myRating: number | undefined,
    readonly averageRating: number | undefined,
    readonly numberOfVotes: number,
    readonly existsVeto: boolean
  ) { }
}
