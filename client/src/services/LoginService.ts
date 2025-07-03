import type { LoginResponse } from '@/model/LoginResponse'
import { loginRequest, registerRequest } from './api/ApiService'
import EventBus from './EventBus'
import type { ResponseWrapper } from '@/model/api/Response'
import type { JwtPayload, Role } from '@/model/api/JwtPayload'

export function isLoggedIn() {
  return !!localStorage.getItem('access-token') && !isJwtExpired()
}

function getJwtPayload(): JwtPayload | undefined {
  if (localStorage.getItem('access-token')) {
    try {
      const splittedJwt = localStorage.getItem('access-token')!.split('.')
      return JSON.parse(atob(splittedJwt[1]))
    } catch (e) {
      console.log('Could not parse JWT ' + e)
    }
  }
}

function isJwtExpired(): boolean {
  const expiry = getJwtPayload()?.exp
  if (!expiry) {
    return true
  }
  const buffer = 30_000
  return expiry! * 1000 + buffer < new Date().getTime()
}

export async function login(name: string, password: string): Promise<undefined | string> {
  const response: ResponseWrapper<LoginResponse> = await loginRequest(name, password)
  if (response.success) {
    localStorage.setItem('access-token', response?.success.token || '')
    EventBus.emit('login-status')
    return undefined
  } else if (response.error) {
    console.info(`Login failed ${response}`)
    return response.error.detail
  }
}

export async function register(name: string, password: string): Promise<string | undefined> {
  const response = await registerRequest(name, password)
  if (response.success) {
    localStorage.setItem('access-token', response.success.token || '')
    EventBus.emit('login-status')
    return undefined
  } else if (response.error) {
    console.info(`Login failed ${response}`)
    return response.error.detail
  }
}

export function logout() {
  localStorage.removeItem('access-token')
  EventBus.emit('login-status')
}

export function getCurrentPlayerId(): number {
  const payload = getJwtPayload()
  if (!payload) {
    throw 'User is not logged in'
  }
  return payload!.player_id
}

export function doesCurrentPlayerHaveRole(role: Role): boolean {
  const payload = getJwtPayload()
  if (!payload) {
    throw 'User is not logged in'
  }
  return payload!.roles.includes(role)
}
