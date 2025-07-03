import { BggSearchItem } from '@/model/BggSearchItem'
import type { Game } from '@/model/Game'
import { LoginResponse } from '@/model/LoginResponse'
import { Me } from '@/model/Me'
import { isLoggedIn } from '../LoginService'
import { wrapResponse, type ResponseWrapper } from '@/model/api/Response'

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

// BGG
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

