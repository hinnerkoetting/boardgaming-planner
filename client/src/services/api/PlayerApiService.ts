import type { Player } from '@/model/Player/Player'
import { authorizedFetch } from './ApiService'
import type { PlayerDetails } from '@/model/Player/PlayerDetails'

export async function fetchPlayers(): Promise<Player[]> {
  const response = await authorizedFetch('/api/players')
  return await response.json()
}

export async function deletePlayer(id: Number) {
  await authorizedFetch(`/api/players/${id}`, { method: 'DELETE' })
}

export async function fetchPlayerDetails(playerId: number): Promise<PlayerDetails> {
  const response = await authorizedFetch(`/api/players/${playerId}`)
  return await response.json()
}
