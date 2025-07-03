import type { Game } from '@/model/Game'
import type { Player } from '@/model/Player'

export async function fetchPlayers(): Promise<Player[]> {
  const response = await fetch('/api/player')
  const json = await response.json()
  return json._embedded.player as Player[]
}

export async function fetchGames(): Promise<Game[]> {
  const response = await fetch('/api/game')
  const json = await response.json()
  return json._embedded.game as Game[]
}
