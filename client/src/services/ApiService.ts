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
