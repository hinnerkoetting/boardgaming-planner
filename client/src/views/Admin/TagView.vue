<template>
  <div>
    <h1>All tags</h1>
    <div>
      <DataTable :value="tags" tableStyle="min-width: 50rem">
        <Column field="id" header="ID"></Column>
        <Column field="description" header="Description"> </Column>
        <Column field="order" header="Order"> </Column>
        <Column field="type" header="Type"> </Column>
      </DataTable>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, type Ref } from 'vue'
import { ref } from 'vue'
import { fetchTags } from '@/services/api/TagApiService'
import type { TagModel } from '@/model/TagModel'
import Column from 'primevue/column'
import DataTable from 'primevue/datatable'

const tags: Ref<TagModel[]> = ref([] as TagModel[])

onMounted(async () => {
  tags.value = await fetchTags()
  console.log(tags.value)
})

function onClickDelete(tag: TagModel) {}
</script>
