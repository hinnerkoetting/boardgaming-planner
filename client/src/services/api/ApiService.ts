import { BggFetchItem } from '@/model/BggFetchItem'
import { BggSearchItem } from '@/model/BggSearchItem'
import type { Game } from '@/model/Game'
import type { GameGroup } from '@/model/GameGroup'
import { LoginResponse } from '@/model/LoginResponse'
import { Me } from '@/model/Me'
import type { Player } from '@/model/Player/Player'
import { getCurrentPlayerId, isLoggedIn } from '../LoginService'
import type { Interest } from '@/model/Interest'
import { wrapResponse, type ResponseWrapper } from '@/model/api/Response'
import type { Rating } from '@/model/Rating'

// Auth

export async function loginRequest(
  name: String,
  password: String
): Promise<ResponseWrapper<LoginResponse>> {
  const response = await fetch('/api/auth/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      login: name,
      password
    })
  })
  return wrapResponse<LoginResponse>(response)
}

function defaultFetchOptions() {
  return {
    headers: {
      Authorization: 'Bearer ' + localStorage.getItem('access-token')
    }
  }
}

// Fetch top level groups

export async function fetchGames(): Promise<Game[]> {
  const response = await authorizedFetch('/api/games')
  const json = await response.json()
  return json._embedded.games as Game[]
}

export async function fetchGameGroups(): Promise<GameGroup[]> {
  const response = await authorizedFetch('/api/gameGroups')
  const json = await response.json()
  return json._embedded.gameGroups
}

// Delete top-level entities

export async function deleteGame(id: Number) {
  await authorizedFetch(`/api/games/${id}`, { method: 'DELETE' })
}

export async function deleteGameGroup(id: Number) {
  await authorizedFetch(`/api/gameGroups/${id}`, { method: 'DELETE' })
}

// Add top-level entities
export async function addGame(game: Game): Promise<Game> {
  const response = await authorizedFetch(`/api/games`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(game)
  })
  return await response.json()
}

// Gamegroups
export async function loadGameGroup(gameGroupId: Number): Promise<GameGroup> {
  const response = await authorizedFetch(`/api/gameGroups/${gameGroupId}`)
  return (await response.json()) as GameGroup
}

export async function fetchPlayersInGroup(gameGroupId: Number): Promise<Player[]> {
  const response = await authorizedFetch(`/api/gameGroups/${gameGroupId}/players`)
  return (await response.json()) as Player[]
}

export async function fetchGamesInGroup(gameGroupId: Number): Promise<Game[]> {
  const response = await authorizedFetch(`/api/gameGroups/${gameGroupId}/playedGames`)
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
  const response = await authorizedFetch(`/api/gameGroups/${gameGroupId}/playedGames`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({ id: gameId })
  })
  return response.status < 300
}

export async function addGameGroup(gameGroup: GameGroup): Promise<GameGroup> {
  const response = await authorizedFetch('/api/gameGroups', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({ name: gameGroup.name })
  })
  return await response.json()
}

export async function fetchInterests(gameGroupId: number): Promise<Interest[]> {
  const playerId = getCurrentPlayerId()
  const response = await authorizedFetch(`/api/ratings/gameGroup/${gameGroupId}/player/${playerId}`)
  return await response.json()
}

export async function updateRating(rating: Interest): Promise<ResponseWrapper<Rating>> {
  const response = await authorizedFetch('/api/ratings', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(rating)
  })
  return wrapResponse(response)
}

export async function deleteInterest(rating: Interest): Promise<ResponseWrapper<Rating>> {
  const response = await authorizedFetch('/api/ratings', {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(rating)
  })
  return wrapResponse(response)
}

// BGG
export async function searchBgg(searchTerm: String): Promise<BggSearchItem[]> {
  const response = await authorizedFetch(`/api/bgg/search/${searchTerm}`)
  const json = (await response.json()) as any[]
  return json.map((item) => new BggSearchItem(item.id, item.name?.value, item.year?.value))
}

export async function fetchFromBgg(bggId: Number): Promise<BggFetchItem | null> {
  const response = await authorizedFetch(`/api/bgg/fetch/${bggId}`)
  const json = (await response.json()) as any[]
  if (json.length > 0) {
    const firstItem = json[0]
    return new BggFetchItem(
      firstItem.id,
      firstItem.name,
      firstItem.description,
      firstItem.thumbnailUrl,
      firstItem.imageUrl
    )
  }
  return null
}

export async function registerRequest(
  name: String,
  password: String
): Promise<ResponseWrapper<LoginResponse>> {
  const response = await fetch('/api/auth/register', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      login: name,
      password
    })
  })
  return await wrapResponse<LoginResponse>(response)
}

export async function authorizedFetch(url: string | URL | globalThis.Request, init?: RequestInit) {
  if (!isLoggedIn()) {
    throw new Error('User currently not logged in')
  }
  if (init?.headers) {
    return fetch(url, {
      ...init,
      ...defaultFetchOptions,
      headers: { ...init.headers, ...defaultFetchOptions().headers }
    })
  }
  if (init) {
    return fetch(url, { ...init, ...defaultFetchOptions() })
  }
  return fetch(url, defaultFetchOptions())
}

// Search games
export async function searchExistingGames(searchTerm: string): Promise<Game[]> {
  const response = await authorizedFetch(`/api/games/search/${searchTerm}`)
  return (await response.json()) as Game[]
}

// Me

export async function getInformationAboutMe(): Promise<Me> {
  const response = await authorizedFetch('/api/me')
  return (await response.json()) as Me
}
