import { wrapEmptySuccessResponse, wrapResponse, type ResponseWrapper } from '@/model/api/Response'
import { authorizedFetch } from './ApiService'
import type { LoginResponse } from '@/model/LoginResponse'

export async function updateName(newName: string): Promise<ResponseWrapper<LoginResponse>> {
  const response = await authorizedFetch('/api/me/name', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      newName
    })
  })
  return wrapResponse(response)
}

export async function updatePassword(
  oldPassword: string,
  newPassword: string
): Promise<ResponseWrapper<boolean>> {
  const response = await authorizedFetch('/api/me/password', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      oldPassword,
      newPassword
    })
  })
  return wrapEmptySuccessResponse(response)
}

export async function deleteMe(): Promise<ResponseWrapper<boolean>> {
  const response = await authorizedFetch('/api/me', {
    method: 'DELETE'
  })
  return wrapEmptySuccessResponse(response)
}
