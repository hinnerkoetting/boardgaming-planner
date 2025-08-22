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
      'Content-Type': 'application/json',
      'Accept': 'application/json'
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
      'Authorization': 'Bearer ' + localStorage.getItem('access-token'),
      'Accept': 'application/json',
      'Content-Type': 'application/json'
    }
  }
}

// Fetch top level groups

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
