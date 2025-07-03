import type { Game } from './Game'
import type { Rating } from './Rating'

export type RatedGame = Game & {
  rating: Rating
}
