import type { GameGroup } from '@/model/GameGroup'
import { authorizedFetch } from './ApiService'
import { wrapEmptySuccessResponse, wrapResponse, type ResponseWrapper } from '@/model/api/Response'
import type { Player } from '@/model/Player/Player'
import type { RatedGame } from '@/model/Game'
import { getCurrentPlayerId } from '../LoginService'
import type { GameGroupStatistics } from '@/model/GameGroupStatistics'

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

export async function fetchMyGameGroups(): Promise<ResponseWrapper<GameGroup[]>> {
  const response = await authorizedFetch('/api/me/gameGroups')
  return wrapResponse(response)
}

export async function fetchGameGroups(): Promise<ResponseWrapper<GameGroup[]>> {
  const response = await authorizedFetch('/api/gameGroups')
  return wrapResponse(response)
}

export async function deleteGameGroup(id: Number) {
  await authorizedFetch(`/api/gameGroups/${id}`, { method: 'DELETE' })
}

export async function loadGameGroup(gameGroupId: number): Promise<GameGroup> {
  const response = await authorizedFetch(`/api/gameGroups/${gameGroupId}`)
  return (await response.json()) as GameGroup
}

export async function fetchPlayersInGroup(gameGroupId: Number): Promise<Player[]> {
  const response = await authorizedFetch(`/api/gameGroups/${gameGroupId}/players`)
  return (await response.json()) as Player[]
}

export async function fetchGamesInGroup(gameGroupId: Number): Promise<RatedGame[]> {
  const response = await authorizedFetch(`/api/gameGroups/${gameGroupId}/games`)
  return await response.json()
}

export async function fetchGameGrouptStatistics(
  gameGroupId: Number
): Promise<ResponseWrapper<GameGroupStatistics>> {
  const response = await authorizedFetch(`/api/gameGroups/${gameGroupId}/statistics`)
  return await wrapResponse(response)
}

export async function fetchGameInGroup(
  gameGroupId: number,
  gameId: number
): Promise<ResponseWrapper<RatedGame>> {
  const response = await authorizedFetch(`/api/gameGroups/${gameGroupId}/games/${gameId}`)
  return wrapResponse(response)
}

export async function removePlayerFromGroup(gameGroupId: Number, playerId: Number) {
  await authorizedFetch(`/api/gameGroups/${gameGroupId}/players/${playerId}`, {
    method: 'DELETE'
  })
}

export async function leaveGroup(gameGroupId: Number) {
  const playerId = getCurrentPlayerId()
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

export async function addGameToGroup(
  gameGroupId: number,
  gameId: number
): Promise<ResponseWrapper<boolean>> {
  const response = await authorizedFetch(`/api/gameGroups/${gameGroupId}/games`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({ id: gameId })
  })
  return wrapEmptySuccessResponse(response)
}

export async function addTagToGameInGroup(
  gameGroupId: number,
  gameId: number,
  tagId: number
): Promise<ResponseWrapper<boolean>> {
  const response = await authorizedFetch(`/api/gameGroups/${gameGroupId}/${gameId}/gameGroupTag`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({ id: tagId })
  })
  return wrapEmptySuccessResponse(response)
}

export async function deleteTagFromGameInGroup(
  gameGroupId: number,
  gameId: number,
  tagId: number
): Promise<ResponseWrapper<boolean>> {
  const response = await authorizedFetch(
    `/api/gameGroups/${gameGroupId}/${gameId}/gameGroupTag/${tagId}`,
    {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json'
      }
    }
  )
  return wrapEmptySuccessResponse(response)
}

export async function addTagToPlayereInGroup(
  gameGroupId: number,
  gameId: number,
  playerId: number,
  tagId: number
): Promise<ResponseWrapper<boolean>> {
  const response = await authorizedFetch(
    `/api/gameGroups/${gameGroupId}/${gameId}/${playerId}/playerTag`,
    {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({ id: tagId })
    }
  )
  return wrapEmptySuccessResponse(response)
}

export async function deleteTagFromPlayerInGroup(
  gameGroupId: number,
  gameId: number,
  playerId: number,
  tagId: number
): Promise<ResponseWrapper<boolean>> {
  const response = await authorizedFetch(
    `/api/gameGroups/${gameGroupId}/${gameId}/${playerId}/playerTag/${tagId}`,
    {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json'
      }
    }
  )
  return wrapEmptySuccessResponse(response)
}
