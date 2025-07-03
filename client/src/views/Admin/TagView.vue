<template>
  <div>
    <h1>All tags</h1>
    <div>
      <TagsTable :tags="tags" />
      <CreateTagComponent v-on:tag-added="onTagAdded" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, type Ref } from 'vue'
import { ref } from 'vue'
import { fetchTags } from '@/services/api/TagApiService'
import type { TagModel } from '@/model/TagModel'
import TagsTable from '@/components/tags/admin/TagsTable.vue'
import CreateTagComponent from '@/components/tags/admin/CreateTagDialogComponent.vue'

const tags: Ref<TagModel[]> = ref([] as TagModel[])

onMounted(async () => {
  tags.value = await fetchTags()
})

function onTagAdded(tag: TagModel) {
  tags.value.push(tag)
}
</script>
