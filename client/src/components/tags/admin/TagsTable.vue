<template>
  <DataTable :value="allTags" tableStyle="min-width: 50rem">
    <Column field="id" header="ID"></Column>
    <Column field="description" header="Description"> </Column>
    <Column field="order" header="Order"> </Column>
    <Column field="type" header="Type"> </Column>
    <Column header="Actions">
      <template #body="slotProps">
        <Button @click="onClickDelete(slotProps.data)" severity="danger"> Delete </Button>
      </template>
    </Column>
  </DataTable>
</template>

<script setup lang="ts">
import type { TagModel } from '@/model/TagModel'
import { deleteTag } from '@/services/api/TagApiService'
import Button from 'primevue/button'
import Column from 'primevue/column'
import DataTable from 'primevue/datatable'
import { ref, watch, type PropType, type Ref } from 'vue'

const props = defineProps({
  tags: {
    type: Array as PropType<TagModel[]>,
    required: true
  }
})

watch(
  () => props.tags,
  (tags: TagModel[]) => {
    allTags.value = tags as TagModel[]
  }
)

const allTags: Ref<TagModel[]> = ref(props.tags as TagModel[])

async function onClickDelete(tag: TagModel) {
  await deleteTag(tag)
  allTags.value = allTags.value.filter((existingTag) => existingTag.id == tag.id)
}
</script>
