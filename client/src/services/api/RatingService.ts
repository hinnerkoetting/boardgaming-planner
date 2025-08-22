import type { Interest } from '@/model/Interest'
import { getCurrentPlayerId } from '../LoginService'
import type { Rating } from '@/model/Rating'
import { wrapResponse, type ResponseWrapper } from '@/model/api/Response'
import { authorizedFetch } from './ApiService'

export async function fetchInterests(gameGroupId: number): Promise<Interest[]> {
  const playerId = getCurrentPlayerId()
  const response = await authorizedFetch(`/api/ratings/gameGroup/${gameGroupId}/player/${playerId}`)
  return await response.json()
}

export async function updateRating(rating: Interest): Promise<ResponseWrapper<Rating>> {
  const response = await authorizedFetch('/api/ratings', {
    method: 'PUT',
    body: JSON.stringify(rating)
  })
  return wrapResponse(response)
}

export async function deleteInterest(rating: Interest): Promise<ResponseWrapper<Rating>> {
  const response = await authorizedFetch('/api/ratings', {
    method: 'DELETE',
    body: JSON.stringify(rating)
  })
  return wrapResponse(response)
}
