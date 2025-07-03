import type { LoginResponse } from '@/model/LoginResponse'
import { loginRequest } from './ApiService'

export function isLoggedIn() {
  return !!localStorage.getItem('access-token')
}

export async function login(name: string, password: string) {
  const response = await loginRequest(name, password)
  if (response as LoginResponse) {
    localStorage.setItem('access-token', response?.token || '')
  }
}

export function logout() {
  localStorage.removeItem('access-token')
}
