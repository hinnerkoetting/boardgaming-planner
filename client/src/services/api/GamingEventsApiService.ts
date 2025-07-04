import type { GamingEvent } from "@/model/GamingEvent"
import { authorizedFetch } from "./ApiService"
import { wrapResponse, type ResponseWrapper } from "@/model/api/Response"

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
