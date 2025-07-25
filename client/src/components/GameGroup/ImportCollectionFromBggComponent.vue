<template>
  <div>
    <Button @click="openDialog">Import collection from BoardGameGeek</Button>
    <Dialog v-model:visible="dialogVisible" header="Import collection from BoardGameGeek" :modal="true">

      <Message severity="info" class="info-message">Importing will add all games that are owned by this user on
        BoardGameGeek to this
        collection. It will only add games and will not delete any games.</Message>

      <InputText v-model="name" placeholder="BGG username" v-on:keyup.enter="importFromBgg" autofocus="true" />
      <Button @click="importFromBgg" :disabled="syncing">Import</Button>

      <div style="position: relative;">
        <ProgressSpinner v-if="syncing" />
      </div>


      <Message v-if="errorMessage" severity="error">{{ errorMessage }}</Message>
    </Dialog>

  </div>
</template>

<script setup lang="ts">
import { importCollectionFromBgg } from '@/services/api/BggApiService';
import { Button, Dialog, InputText, Message, ProgressSpinner } from 'primevue';
import { ref } from 'vue';

const name = ref('');
const dialogVisible = ref(false)
const errorMessage = ref('');
const syncing = ref(false);

const emits = defineEmits(['imported']);

function openDialog() {
  dialogVisible.value = true;
}

async function importFromBgg() {
  if (!name.value) {
    errorMessage.value = 'Please enter a username';
    return;
  }
  errorMessage.value = '';
  syncing.value = true;
  const response = await importCollectionFromBgg(name.value)
  if (response.success) {
    dialogVisible.value = false;
    name.value = '';
    errorMessage.value = '';
    emits('imported');
  } else {
    errorMessage.value = response.error?.detail || 'Failed to import collection';
  }
  syncing.value = false;
}
</script>

<style lang="css" scoped>
.info-message {
  margin: 8px 0;
  max-width: 500px;
}
</style>