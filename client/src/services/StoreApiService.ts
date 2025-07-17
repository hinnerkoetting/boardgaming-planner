import { useStore } from '@/main'
import { fetchTags } from './api/TagApiService'
import type { TagModel } from '@/model/TagModel'

export async function loadTags(force?: boolean): Promise<TagModel[]> {
  const store = useStore()
  if (store.state.allTags.length > 0 && !force) {
    return store.state.allTags
  }

  const loadedTags = await fetchTags()
  store.commit('updateTags', loadedTags)
  return loadedTags
}
