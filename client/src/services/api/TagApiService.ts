import type { TagModel } from '@/model/TagModel'
import { authorizedFetch } from './ApiService'

export async function fetchTags(): Promise<TagModel[]> {
  const response = await authorizedFetch('/api/tags')
  return await response.json()
}
