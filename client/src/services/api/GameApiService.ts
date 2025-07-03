import type { AdminGame, Game } from '@/model/Game'
import { authorizedFetch } from './ApiService'
import { wrapEmptySuccessResponse, wrapResponse, type ResponseWrapper } from '@/model/api/Response'

export async function fetchGames(): Promise<AdminGame[]> {
  const response = await authorizedFetch('/api/games')
  return await response.json()
}


export async function loadGame(gameId: number): Promise<Game> {
  const response = await authorizedFetch(`/api/games/${gameId}`)
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

export async function addTagToGame(game: Game, tagId: number): Promise<ResponseWrapper<boolean>> {
  const body = {
    id: tagId
  }
  const response = await authorizedFetch(`/api/games/${game.id}/globalTag`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(body)
  })
  return await wrapEmptySuccessResponse(response)
}

export async function removeTagFromGame(
  game: Game,
  tagId: number
): Promise<ResponseWrapper<boolean>> {
  const response = await authorizedFetch(`/api/games/${game.id}/globalTag/${tagId}`, {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json'
    }
  })
  return await wrapEmptySuccessResponse(response)
}
