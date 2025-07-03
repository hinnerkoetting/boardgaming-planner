<script setup lang="ts">
import InputText from 'primevue/inputtext'
import Button from 'primevue/button'
import { ref, type Ref } from 'vue'
import { addGame } from '@/services/ApiService'

const emit = defineEmits(['game-added'])

const name: Ref<String> = ref('')

function onClickAddGame() {
  addGame(name.value)
  emit('game-added', { name })
}
</script>

<template>
  <div>
    <InputText id="name" name="name" v-model="name" placeholder="Name"></InputText>
    <Button @click="onClickAddGame">Add</Button>
  </div>
</template>

<style scoped></style>
