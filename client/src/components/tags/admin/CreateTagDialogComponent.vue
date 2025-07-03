<template>
  <div>
    <Button @click="onClickCreateTag"> Create tag</Button>
    <Dialog v-model:visible="showDialog" modal header="Create Tag"
      ><div>
        <EditOrCreateTagComponent v-on:tag-added="onTagAdded" mode="CREATE" />
      </div>
    </Dialog>
  </div>
</template>

<script setup lang="ts">
import Button from 'primevue/button'
import Dialog from 'primevue/dialog'
import { ref } from 'vue'
import EditOrCreateTagComponent from './EditOrCreateTagComponent.vue'
import type { TagModel } from '@/model/TagModel'

const emit = defineEmits(['tag-added'])
const showDialog = ref(false)

function onClickCreateTag() {
  showDialog.value = true
}
function onTagAdded(tag: TagModel) {
  emit('tag-added', tag)
  showDialog.value = false
}
</script>
