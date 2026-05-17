import type { GameGroup } from '@/model/GameGroup'
import { authorizedFetch } from './ApiService'
import { wrapEmptySuccessResponse, wrapResponse, type ResponseWrapper } from '@/model/api/Response'
import type { RatedGame } from '@/model/Game'
import { getCurrentPlayerId } from '../LoginService'
import type { GameGroupStatistics } from '@/model/GameGroupStatistics'
import type { GameGroupMember, GameGroupMemberType } from '@/model/GameGroupMember'

export async function addGameGroup(gameGroup: GameGroup): Promise<ResponseWrapper<GameGroup>> {
  const response = await authorizedFetch('/api/gameGroups', {
    method: 'POST',
    body: JSON.stringify({
      name: gameGroup.name,
      type: gameGroup.type,
      openForNewPlayers: gameGroup.openForNewPlayers
    })
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

export async function fetchPlayersInGroup(gameGroupId: Number): Promise<ResponseWrapper<GameGroupMember[]>> {
  const response = await authorizedFetch(`/api/gameGroups/${gameGroupId}/players`)
  return await wrapResponse(response)
}

export async function fetchGamesInGroup(gameGroupId: Number): Promise<RatedGame[]> {
  const response = await authorizedFetch(`/api/gameGroups/${gameGroupId}/games`)
  return await response.json()
}

export async function fetchGameGrouptStatistics(
  gameGroupId: Number,
  startDate: Date | undefined,
  endDate: Date | undefined
): Promise<ResponseWrapper<GameGroupStatistics>> {
  const params = new URLSearchParams()
  if (startDate !== undefined) params.append('startDate', startDate.toISOString())
  if (endDate !== undefined) params.append('endDate', endDate.toISOString())

  const url = `/api/gameGroups/${gameGroupId}/statistics?${params.toString()}`
  const response = await authorizedFetch(url)
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
    body: JSON.stringify({ id: playerId })
  })
}

export async function addGameToGroup(
  gameGroupId: number,
  gameId: number
): Promise<ResponseWrapper<boolean>> {
  const response = await authorizedFetch(`/api/gameGroups/${gameGroupId}/games`, {
    method: 'POST',
    body: JSON.stringify({ id: gameId })
  })
  return wrapEmptySuccessResponse(response)
}

export async function removeGameFromGroup(
  gameGroupId: number,
  gameId: number
): Promise<ResponseWrapper<boolean>> {
  const response = await authorizedFetch(`/api/gameGroups/${gameGroupId}/games/${gameId}`, {
    method: 'DELETE',
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
    }
  )
  return wrapEmptySuccessResponse(response)
}

export async function updatePlayerMembershipType(
  gameGroupId: number,
  playerId: number,
  type: GameGroupMemberType
): Promise<ResponseWrapper<boolean>> {
  const response = await authorizedFetch(`/api/gameGroups/${gameGroupId}/${playerId}/${type}`, {
    method: 'PUT',
  })
  return wrapEmptySuccessResponse(response)
}

export async function updateGameGroup(gameGroup: GameGroup): Promise<ResponseWrapper<GameGroup>> {
  const response = await authorizedFetch(`/api/gameGroups/${gameGroup.id}`, {
    method: 'PUT',
    body: JSON.stringify({
      name: gameGroup.name,
      type: gameGroup.type,
      openForNewPlayers: gameGroup.openForNewPlayers
    })
  })
  return await wrapResponse(response)
}
