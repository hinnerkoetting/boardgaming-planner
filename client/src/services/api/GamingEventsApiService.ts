import type { GamingEvent } from "@/model/GamingEvent"
import { authorizedFetch } from "./ApiService"

export async function fetchGamingEvents(gameGroupId: Number): Promise<GamingEvent[]> {
  const response = await authorizedFetch(`/api/gameGroup/${gameGroupId}/gamingEvents`)
  return await response.json()
}

export async function fetchNextGamingEvents(gameGroupId: Number): Promise<GamingEvent[]> {
  const response = await authorizedFetch(`/api/gameGroup/${gameGroupId}/gamingEvents?` + new URLSearchParams({
    onlyNext: "true",
    number: "5"
  }))
  return await response.json()
}
