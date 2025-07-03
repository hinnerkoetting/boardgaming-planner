import type { LoginResponse } from '@/model/LoginResponse'
import { getInformationAboutMe, loginRequest, registerRequest } from './ApiService'
import EventBus from './EventBus'
import type { Me } from '@/model/Me'

let userInfo: Me

export function isLoggedIn() {
  return !!localStorage.getItem('access-token')
}

export async function login(name: string, password: string) {
  const response = await loginRequest(name, password)
  if (response as LoginResponse) {
    localStorage.setItem('access-token', response?.token || '')
    EventBus.emit('login-status')
    loadMe()
  }
}

export async function register(name: string, password: string) {
  const response = await registerRequest(name, password)
  if (response as LoginResponse) {
    localStorage.setItem('access-token', response?.token || '')
    EventBus.emit('login-status')
    loadMe()
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
