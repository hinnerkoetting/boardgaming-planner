import type { LoginResponse } from '@/model/LoginResponse'
import { getInformationAboutMe, loginRequest, registerRequest } from './ApiService'
import EventBus from './EventBus'
import type { Me } from '@/model/Me'
import type { ResponseWrapper } from '@/model/api/Response'

let userInfo: Me

export function isLoggedIn() {
  return !!localStorage.getItem('access-token')
}

export async function login(name: string, password: string): Promise<undefined | string> {
  const response: ResponseWrapper<LoginResponse> = await loginRequest(name, password)
  if (response.success) {
    localStorage.setItem('access-token', response?.success.token || '')
    EventBus.emit('login-status')
    loadMe()
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
    loadMe()
    return undefined
  } else if (response.error) {
    console.info(`Login failed ${response}`)
    return response.error.detail
  }
}

export function logout() {
  localStorage.removeItem('access-token')
  EventBus.emit('login-status')
  userInfo = null!
}

export async function loadMe() {
  userInfo = await getInformationAboutMe()
}

export function getCurrentUserId(): number {
  return userInfo.playerId
}
