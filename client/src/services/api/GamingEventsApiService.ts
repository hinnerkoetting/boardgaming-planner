import type { GamingEvent } from "@/model/GamingEvent"
import { authorizedFetch } from "./ApiService"
import { wrapEmptySuccessResponse, wrapResponse, type ResponseWrapper } from "@/model/api/Response"

export async function fetchGamingEvents(gameGroupId: Number): Promise<GamingEvent[]> {
  const response = await authorizedFetch(`/api/gameGroup/${gameGroupId}/gamingEvents`)
  return await response.json()
}

export async function fetchGamingEvent(gamingEventId: Number): Promise<ResponseWrapper<GamingEvent>> {
  const response = await authorizedFetch(`/api/gamingEvents/${gamingEventId}`)
  return wrapResponse(response)
}

export async function fetchNextGamingEvents(gameGroupId: Number): Promise<GamingEvent[]> {
  const response = await authorizedFetch(`/api/gameGroup/${gameGroupId}/gamingEvents?` + new URLSearchParams({
    onlyNext: "true",
    number: "5"
  }))
  return await response.json()
}

export async function createGamingEvent(gameGroupId: Number, start: Date, end: Date | null, description: string): Promise<ResponseWrapper<GamingEvent>> {
  const body = {
    start: start.getTime(),
    end: end?.getTime(),
    description,
    gameGroupId
  }
  const response = await authorizedFetch(`/api/gameGroup/${gameGroupId}/gamingEvents`, {
    method: "POST",
    body: JSON.stringify(body),
    headers: {
      "Content-Type": "application/json"
    }
  })
  return wrapResponse(response)
}