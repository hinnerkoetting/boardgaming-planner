<template>
  <div class="content">
    <Message severity="error" v-if="errorMessage"> {{ errorMessage }}</Message>
    <InputText
      name="description"
      v-model="description"
      placeholder="Description"
      @keyup.enter="onClickSubmit"
    />
    <Select v-model="type" :options="options" />
    <Button @click="onClickSubmit">Submit</Button>
  </div>
</template>

<script setup lang="ts">
import type { ResponseWrapper } from '@/model/api/Response'
import { CreateTagModel, TagModel } from '@/model/TagModel'
import { createTag, updateTag } from '@/services/api/TagApiService'
import Button from 'primevue/button'
import InputText from 'primevue/inputtext'
import Message from 'primevue/message'
import Select from 'primevue/select'
import { ref, type PropType } from 'vue'

const props = defineProps({
  mode: {
    type: String as PropType<'CREATE' | 'EDIT'>,
    required: true
  },
  propDescription: {
    type: String
  },
  propId: {
    type: Number
  },
  propRanking: {
    type: Number
  },
  propType: {
    type: String
  }
})
const emit = defineEmits<{
  (e: 'tag-added', tag: TagModel): void
}>()

const errorMessage = ref('')
const description = ref(props.propDescription || '')
const type = ref(props.propType || 'GLOBAL')
const options = ['GLOBAL', 'GAME_GROUP']

async function onClickSubmit() {
  const response = await createOrUpdate()
  if (response.success) {
    emit('tag-added', response.success)
  } else {
    errorMessage.value = response.error?.detail || 'Error'
  }
}

async function createOrUpdate(): Promise<ResponseWrapper<TagModel>> {
  const tag = new CreateTagModel(description.value, null, type.value)

  if (props.mode === 'CREATE') {
    return await createTag(tag)
  } else {
    await updateTag(tag)
    return {
      success: new TagModel(props.propId!, description.value, props.propRanking!, type.value),
      error: undefined
    }
  }
}
</script>

<style lang="css" scoped>
.content {
  display: flex;
  flex-direction: column;
  gap: 4px;
}
</style>
