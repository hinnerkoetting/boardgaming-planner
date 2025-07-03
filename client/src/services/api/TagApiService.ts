import type { TagModel } from '@/model/TagModel'
import { authorizedFetch } from './ApiService'
import { wrapEmptySuccessResponse, type ResponseWrapper } from '@/model/api/Response'

export async function fetchTags(): Promise<TagModel[]> {
  const response = await authorizedFetch('/api/tags')
  return await response.json()
}

export async function deleteTag(tag: TagModel): Promise<void> {
  await authorizedFetch(`/api/tags/${tag.id}`, {
    method: 'DELETE'
  })
}

export async function createTag(tag: TagModel): Promise<ResponseWrapper<boolean>> {
  const response = await authorizedFetch('/api/tags', {
    method: 'POST',
    body: JSON.stringify(tag)
  })
  return wrapEmptySuccessResponse(response)
}

export async function updateTag(tag: TagModel): Promise<ResponseWrapper<boolean>> {
  const response = await authorizedFetch('/api/tags/${tag.id}', {
    method: 'PUT',
    body: JSON.stringify(tag)
  })
  return wrapEmptySuccessResponse(response)
}
