import type { GameGroup } from '@/model/GameGroup'
import { authorizedFetch } from './ApiService'
import { wrapResponse, type ResponseWrapper } from '@/model/api/Response'
import type { Player } from '@/model/Player/Player'
import type { Game } from '@/model/Game'

export async function addGameGroup(gameGroup: GameGroup): Promise<ResponseWrapper<GameGroup>> {
  const response = await authorizedFetch('/api/gameGroups', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({ name: gameGroup.name })
  })
  return await wrapResponse(response)
}

export async function fetchGameGroups(): Promise<GameGroup[]> {
  const response = await authorizedFetch('/api/gameGroups')
  return await response.json()
}

export async function deleteGameGroup(id: Number) {
  await authorizedFetch(`/api/gameGroups/${id}`, { method: 'DELETE' })
}
// Gamegroups
export async function loadGameGroup(gameGroupId: number): Promise<GameGroup> {
  const response = await authorizedFetch(`/api/gameGroups/${gameGroupId}`)
  return (await response.json()) as GameGroup
}

export async function fetchPlayersInGroup(gameGroupId: Number): Promise<Player[]> {
  const response = await authorizedFetch(`/api/gameGroups/${gameGroupId}/players`)
  return (await response.json()) as Player[]
}

export async function fetchGamesInGroup(gameGroupId: Number): Promise<Game[]> {
  const response = await authorizedFetch(`/api/gameGroups/${gameGroupId}/games`)
  return (await response.json()) as Game[]
}

export async function removePlayerFromGroup(gameGroupId: Number, playerId: Number) {
  await authorizedFetch(`/api/gameGroups/${gameGroupId}/players/${playerId}`, {
    method: 'DELETE'
  })
}

export async function addPlayerToGroup(gameGroupId: number, playerId: number) {
  await authorizedFetch(`/api/gameGroups/${gameGroupId}/players`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({ id: playerId })
  })
}

export async function addGameToGroup(gameGroupId: number, gameId: number): Promise<boolean> {
  const response = await authorizedFetch(`/api/gameGroups/${gameGroupId}/games`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({ id: gameId })
  })
  return response.status < 300
}
