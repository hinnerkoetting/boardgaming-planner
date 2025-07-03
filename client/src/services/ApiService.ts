import type { Game } from '@/model/Game'
import type { Player } from '@/model/Player'

export async function fetchPlayers(): Promise<Player[]> {
  const response = await fetch('/api/players')
  const json = await response.json()
  return json._embedded.player as Player[]
}

export async function fetchGames(): Promise<Game[]> {
  const response = await fetch('/api/games')
  const json = await response.json()
  return json._embedded.game as Game[]
}

export async function deletePlayer(id: Number) {
  await fetch(`/api/players/${id}`, { method: 'DELETE' })
}

export async function deleteGame(id: Number) {
  await fetch(`/api/games/${id}`, { method: 'DELETE' })
}
