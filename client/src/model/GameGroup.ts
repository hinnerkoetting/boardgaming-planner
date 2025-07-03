import type { Player } from './Player'

export class Game {
  id: Number
  name: String
  players: Player[]

  constructor(id: Number, name: String, players: Player[]) {
    this.id = id
    this.name = name
    this.players = players
  }
}
