import {
  GamingEvent,
  type GameStatus,
  type ParticipationStatus,
  type Schedule
} from '@/model/GamingEvent'
import { authorizedFetch } from './ApiService'
import { wrapEmptySuccessResponse, wrapResponse, type ResponseWrapper } from '@/model/api/Response'

export async function fetchGamingEvents(gameGroupId: Number): Promise<GamingEvent[]> {
  const response = await authorizedFetch(`/api/gameGroup/${gameGroupId}/gamingEvents`)
  return await response.json()
}

export async function fetchGamingEvent(
  gamingEventId: Number
): Promise<ResponseWrapper<GamingEvent>> {
  const response = await authorizedFetch(`/api/gamingEvents/${gamingEventId}`)
  return wrapResponse(response)
}

export async function fetchNextGamingEvents(
  gameGroupId: Number,
  startTime: Date
): Promise<GamingEvent[]> {
  const response = await authorizedFetch(
    `/api/gameGroup/${gameGroupId}/gamingEvents?` +
    new URLSearchParams({
      startTime: startTime.getTime().toString(),
      number: '5'
    })
  )
  return await response.json()
}

export async function createGamingEvent(
  gameGroupId: Number,
  start: Date,
  schedule: Schedule,
  description: string
): Promise<ResponseWrapper<GamingEvent>> {
  const body = {
    start: start.getTime(),
    schedule,
    description,
    gameGroupId
  }
  const response = await authorizedFetch(`/api/gameGroup/${gameGroupId}/gamingEvents`, {
    method: 'POST',
    body: JSON.stringify(body),
  })
  return wrapResponse(response)
}

export async function updateGamingEvent(
  gameGroupId: Number,
  gamingEventId: number,
  start: Date,
  schedule: Schedule,
  description: string
): Promise<ResponseWrapper<GamingEvent>> {
  const body = {
    start: start.getTime(),
    schedule,
    description,
    gameGroupId,
    id: gamingEventId
  }
  const response = await authorizedFetch(
    `/api/gameGroup/${gameGroupId}/gamingEvents/${gamingEventId}`,
    {
      method: 'PUT',
      body: JSON.stringify(body),
    }
  )
  return wrapResponse(response)
}

export async function addAllGroupMembersToGamingEvent(
  gamingEventId: Number
): Promise<ResponseWrapper<GamingEvent>> {
  const response = await authorizedFetch(`/api/gamingEvents/${gamingEventId}/player/all`, {
    method: 'POST',
  })
  return wrapResponse(response)
}

export async function updateParticipationStatus(
  gamingEventId: number,
  playerId: number,
  participationStatus: ParticipationStatus
): Promise<ResponseWrapper<boolean>> {
  const body = {
    status: participationStatus
  }
  const response = await authorizedFetch(
    `/api/gamingEvents/${gamingEventId}/player/${playerId}/status`,
    {
      method: 'PUT',
      body: JSON.stringify(body),
    }
  )
  return wrapEmptySuccessResponse(response)
}

export async function addGameToEvent(
  gamingEventId: number,
  gameId: number
): Promise<ResponseWrapper<boolean>> {
  const body = {
    gameId: gameId,
    gameStatus: 'SUGGESTED',
    comment: null
  }
  const response = await authorizedFetch(`/api/gamingEvents/${gamingEventId}/game`, {
    method: 'POST',
    body: JSON.stringify(body),
  })
  return wrapEmptySuccessResponse(response)
}

export async function updateGameStatus(
  gamingEventId: number,
  gameId: number,
  gameStatus: GameStatus
): Promise<ResponseWrapper<boolean>> {
  const body = {
    status: gameStatus
  }
  const response = await authorizedFetch(
    `/api/gamingEvents/${gamingEventId}/game/${gameId}/status`,
    {
      method: 'PUT',
      body: JSON.stringify(body),
    }
  )
  return wrapEmptySuccessResponse(response)
}

export async function removeGameFromEvent(
  gamingEventId: number,
  gameId: number
): Promise<ResponseWrapper<boolean>> {
  const response = await authorizedFetch(`/api/gamingEvents/${gamingEventId}/game/${gameId}`, {
    method: 'DELETE'
  })
  return wrapEmptySuccessResponse(response)
}

export async function cloneEvent(
  gameGroupId: number,
  gamingEventId: number
): Promise<ResponseWrapper<GamingEvent>> {
  const response = await authorizedFetch(
    `/api/gameGroup/${gameGroupId}/gamingEvents/${gamingEventId}/clone`,
    {
      method: 'POST'
    }
  )
  return wrapResponse(response)
}

export async function updateEvent(
  gameGroupId: number,
  gamingEventId: number,
  start: Date,
  schedule: Schedule,
  description: string,
  parentEventId: number
): Promise<ResponseWrapper<GamingEvent>> {
  const body = {
    start: start.getTime(),
    schedule,
    description,
    gameGroupId,
    id: gamingEventId,
    parentEventId
  }
  const response = await authorizedFetch(
    `/api/gameGroup/${gameGroupId}/gamingEvents/${gamingEventId}`,
    {
      method: 'PUT',
      body: JSON.stringify(body),
    }
  )
  return wrapResponse(response)
}

export async function deleteEvent(gamingEventId: number): Promise<ResponseWrapper<boolean>> {
  const response = await authorizedFetch(`/api/gamingEvents/${gamingEventId}`, {
    method: 'DELETE',
  })
  return wrapEmptySuccessResponse(response)
}
