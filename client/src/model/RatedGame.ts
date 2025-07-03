import type { Game } from './Game'

export type RatedGame = Game & {
  rating: {
    myRating: number | undefined
    averageRating: number
    existsVeto: boolean
  }
}
