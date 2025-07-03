import { BggFetchItem } from '@/model/BggFetchItem'
import { BggSearchItem } from '@/model/BggSearchItem'
import type { Game } from '@/model/Game'
import type { GameGroup } from '@/model/GameGroup'
import type { LoginResponse } from '@/model/LoginResponse'
import type { Player } from '@/model/Player'

// Fetch top level groups
export async function fetchPlayers(): Promise<Player[]> {
  const response = await authorizedFetch('/api/players')
  const json = await response.json()
  return json._embedded.players as Player[]
}

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
export async function deletePlayer(id: Number) {
  await authorizedFetch(`/api/players/${id}`, { method: 'DELETE' })
}

export async function deleteGame(id: Number) {
  await authorizedFetch(`/api/games/${id}`, { method: 'DELETE' })
}

export async function deleteGameGroup(id: Number) {
  await authorizedFetch(`/api/gameGroups/${id}`, { method: 'DELETE' })
}

// Add top-level entities
export async function addGame(game: Game) {
  await authorizedFetch(`/api/games`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(game)
  })
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

export async function removePlayerFromGroup(gameGroupId: Number, playerId: Number) {
  await authorizedFetch(`/api/gameGroups/${gameGroupId}/players/${playerId}`, {
    method: 'DELETE'
  })
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

// Auth

export async function loginRequest(
  name: String,
  password: String
): Promise<LoginResponse | undefined> {
  try {
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
    return (await response.json()) as LoginResponse
  } catch (error) {
    console.log('There was an error', error)
  }
}

async function authorizedFetch(url: string | URL | globalThis.Request, init?: RequestInit) {
  if (init?.headers) {
    return fetch(url, {
      ...init,
      ...defaultFetchOptions,
      headers: { ...init.headers, ...defaultFetchOptions.headers }
    })
  }
  return fetch(url, { ...init, ...defaultFetchOptions })
}

const defaultFetchOptions = {
  headers: {
    Authorization: 'Bearer ' + localStorage.getItem('access-token')
  }
}
