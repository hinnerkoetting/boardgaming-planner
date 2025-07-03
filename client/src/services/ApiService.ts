import type { Player } from '@/model/Player'

export async function fetchPlayers(): Promise<Player[]> {
  const response = await fetch('/api/player')
  const json = await response.json()
  return json._embedded.player as Player[]
}
