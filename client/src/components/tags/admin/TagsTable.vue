<template>
  <div>
    <DataTable :value="allTags" tableStyle="min-width: 50rem">
      <Column field="id" header="ID"></Column>
      <Column field="description" header="Description"> </Column>
      <Column field="ranking" header="Order"> </Column>
      <Column field="type" header="Type"> </Column>
      <Column header="Actions">
        <template #body="slotProps">
          <Button @click="onClickEdit(slotProps.data)" severity="info"> Edit </Button>
          <Button @click="onClickDelete(slotProps.data)" severity="danger"> Delete </Button>
        </template>
      </Column>
    </DataTable>
    <Dialog v-model:visible="editDialogVisible" modal header="Edit tag">
      <EditOrCreateTagComponent
        mode="EDIT"
        :prop-description="selectedTag?.description"
        :prop-id="selectedTag?.id"
        :prop-ranking="selectedTag?.ranking"
        v-bind:prop-type="selectedTag?.type"
        @tag-added="onTagEdited"
      />
    </Dialog>
  </div>
</template>

<script setup lang="ts">
import type { TagModel } from '@/model/TagModel'
import { deleteTag } from '@/services/api/TagApiService'
import Button from 'primevue/button'
import Column from 'primevue/column'
import DataTable from 'primevue/datatable'
import { ref, watch, type PropType, type Ref } from 'vue'
import EditOrCreateTagComponent from './EditOrCreateTagComponent.vue'
import Dialog from 'primevue/dialog'

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

const selectedTag: Ref<TagModel | undefined> = ref(undefined)
const editDialogVisible = ref(false)

function onTagEdited(tag: TagModel) {
  if (selectedTag.value) {
    const editingTag = selectedTag.value
    editingTag.description = tag.description
    editingTag.ranking = tag.ranking
    editingTag.type = tag.type
  }
  selectedTag.value = undefined
  editDialogVisible.value = false
}
async function onClickEdit(tag: TagModel) {
  selectedTag.value = tag
  editDialogVisible.value = true
}

async function onClickDelete(tag: TagModel) {
  await deleteTag(tag)
  allTags.value = allTags.value.filter((existingTag) => existingTag.id != tag.id)
}
</script>
