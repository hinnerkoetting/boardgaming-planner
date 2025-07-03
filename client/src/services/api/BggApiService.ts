import { wrapEmptySuccessResponse, wrapResponse, type ResponseWrapper } from '@/model/api/Response'
import { authorizedFetch } from './ApiService'
import { BggSearchItem } from '@/model/BggSearchItem'
import type { AdminGame, Game } from '@/model/Game'

export async function searchBgg(searchTerm: String): Promise<ResponseWrapper<BggSearchItem[]>> {
  const response = await authorizedFetch(`/api/bgg/search/${searchTerm}`)
  const wrapped = await wrapResponse<any[]>(response)
  if (wrapped.success) {
    const items = wrapped.success.map(
      (item) => new BggSearchItem(item.id, item.name?.value, item.year?.value)
    )
    return {
      success: items,
      error: undefined
    }
  } else {
    return wrapped
  }
}

export async function importFromBgg(bggId: Number): Promise<ResponseWrapper<Game>> {
  const response = await authorizedFetch(`/api/bgg/import/${bggId}`, { method: 'POST' })
  return wrapResponse(response)
}

export async function syncGameFromBgg(gameId: number): Promise<ResponseWrapper<AdminGame>> {
  const response = await authorizedFetch(`/api/bgg/sync/game/${gameId}`, { method: 'PUT' })
  return wrapResponse(response)
}

export async function syncAllGamesFromBgg(): Promise<ResponseWrapper<boolean>> {
  const response = await authorizedFetch(`/api/bgg/sync`, { method: 'POST' })
  return wrapEmptySuccessResponse(response)
}
