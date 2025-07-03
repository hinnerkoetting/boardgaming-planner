import type { Role } from '@/model/api/JwtPayload'
import { authorizedFetch } from './ApiService'
import { wrapEmptySuccessResponse, type ResponseWrapper } from '@/model/api/Response'

export async function removeRoleFromPlayer(
  playerId: number,
  role: Role
): Promise<ResponseWrapper<boolean>> {
  const response = await authorizedFetch(`/api/players/${playerId}/roles/${role}`, {
    method: 'DELETE'
  })
  return wrapEmptySuccessResponse(response)
}

export async function addRoleToPlayer(
  playerId: number,
  role: Role
): Promise<ResponseWrapper<boolean>> {
  const response = await authorizedFetch(`/api/players/${playerId}/roles/${role}`, {
    method: 'PUT'
  })
  return wrapEmptySuccessResponse(response)
}
