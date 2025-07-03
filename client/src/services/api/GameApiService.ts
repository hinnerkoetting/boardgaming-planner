import type { Game } from '@/model/Game'
import { authorizedFetch } from './ApiService'
import { wrapEmptySuccessResponse, wrapResponse, type ResponseWrapper } from '@/model/api/Response'

export async function fetchGames(): Promise<Game[]> {
  const response = await authorizedFetch('/api/games')
  return await response.json()
}

export async function deleteGame(id: Number) {
  await authorizedFetch(`/api/games/${id}`, { method: 'DELETE' })
}

export async function addGame(game: Game): Promise<ResponseWrapper<Game>> {
  const response = await authorizedFetch(`/api/games`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(game)
  })
  return await wrapResponse<Game>(response)
}

export async function updateGame(game: Game): Promise<ResponseWrapper<boolean>> {
  const response = await authorizedFetch(`/api/games/${game.id}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(game)
  })
  return await wrapEmptySuccessResponse(response)
}
