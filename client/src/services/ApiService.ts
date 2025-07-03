import { BggFetchItem } from '@/model/BggFetchItem'
import { BggSearchItem } from '@/model/BggSearchItem'
import type { Game } from '@/model/Game'
import type { GameGroup } from '@/model/GameGroup'
import type { Player } from '@/model/Player'

// Fetch top level groups
export async function fetchPlayers(): Promise<Player[]> {
  const response = await fetch('/api/players')
  const json = await response.json()
  return json._embedded.players as Player[]
}

export async function fetchGames(): Promise<Game[]> {
  const response = await fetch('/api/games')
  const json = await response.json()
  return json._embedded.games as Game[]
}

export async function fetchGameGroups(): Promise<GameGroup[]> {
  const response = await fetch('/api/gameGroups')
  const json = await response.json()
  return json._embedded.gameGroups
}

// Delete top-level entities
export async function deletePlayer(id: Number) {
  await fetch(`/api/players/${id}`, { method: 'DELETE' })
}

export async function deleteGame(id: Number) {
  await fetch(`/api/games/${id}`, { method: 'DELETE' })
}

export async function deleteGameGroup(id: Number) {
  await fetch(`/api/gameGroups/${id}`, { method: 'DELETE' })
}

// Add top-level entities
export async function addGame(name: String) {
  await fetch(`/api/games`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({ name })
  })
}

// Gamegroups
export async function loadGameGroup(gameGroupId: Number): Promise<GameGroup> {
  const response = await fetch(`/api/gameGroups/${gameGroupId}`)
  return (await response.json()) as GameGroup
}

export async function fetchPlayersInGroup(gameGroupId: Number): Promise<Player[]> {
  const response = await fetch(`/api/gameGroups/${gameGroupId}/players`)
  return (await response.json()) as Player[]
}

export async function removePlayerFromGroup(gameGroupId: Number, playerId: Number) {
  await fetch(`/api/gameGroups/${gameGroupId}/players/${playerId}`, {
    method: 'DELETE'
  })
}

// BGG
export async function searchBgg(searchTerm: String): Promise<BggSearchItem[]> {
  const response = await fetch(`/api/bgg/search/${searchTerm}`)
  const json = (await response.json()) as any[]
  return json.map((item) => new BggSearchItem(item.id, item.name.value, item.year.value))
}

export async function fetchFromBgg(bggId: Number): Promise<BggFetchItem | null> {
  const response = await fetch(`/api/bgg/fetch/${bggId}`)
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
