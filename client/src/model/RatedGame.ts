import type { Game } from './Game'

export type RatedGame = Game & {
  rating: number | undefined
}
