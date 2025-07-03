<template>
  <div class="content">
    <Message severity="error"> {{ errorMessage }}</Message>
    <InputText name="id" v-model="id" placeholder="ID" /><br />
    <InputText name="description" v-model="description" placeholder="Description" />
    <InputNumber name="order" v-model="order" placeholder="Order" />
    <InputText name="type" v-model="type" placeholder="Type" />
    <Button @click="onClickSubmit">Submit</Button>
  </div>
</template>

<script setup lang="ts">
import { TagModel } from '@/model/TagModel'
import { createTag } from '@/services/api/TagApiService'
import Button from 'primevue/button'
import InputNumber from 'primevue/inputnumber'
import InputText from 'primevue/inputtext'
import Message from 'primevue/message'
import { ref } from 'vue'

const emit = defineEmits(['tag-added'])

const errorMessage = ref('')
const id = ref('')
const description = ref('')
const order = ref(0)
const type = ref('')

async function onClickSubmit() {
  const tag = new TagModel(id.value, description.value, order.value, type.value)
  const response = await createTag(tag)
  if (response.success) {
    emit('tag-added', tag)
  } else {
    errorMessage.value = response.error?.detail || 'Error'
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
