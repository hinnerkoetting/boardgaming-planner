import type { LoginResponse } from '@/model/LoginResponse'
import { getInformationAboutMe, loginRequest, registerRequest } from './ApiService'
import EventBus from './EventBus'
import type { Me } from '@/model/Me'
import type { ErrorResponse } from '@/model/ErrorResponse'

let userInfo: Me

export function isLoggedIn() {
  return !!localStorage.getItem('access-token')
}

export async function login(name: string, password: string): Promise<undefined | string> {
  const response = await loginRequest(name, password)
  switch (response.responseType) {
    case 'login': {
      localStorage.setItem('access-token', response?.token || '')
      EventBus.emit('login-status')
      loadMe()
      return undefined
    }
    case 'error': {
      console.info(`Login failed ${response}`)
      return response.detail
    }
  }
}

export async function register(name: string, password: string): Promise<string | undefined> {
  const response = await registerRequest(name, password)
  switch (response) {
    case response as LoginResponse: {
      localStorage.setItem('access-token', response?.token || '')
      EventBus.emit('login-status')
      loadMe()
      return undefined
    }
    case response as ErrorResponse: {
      console.info(`Login failed ${response}`)
      return response.message
    }
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
