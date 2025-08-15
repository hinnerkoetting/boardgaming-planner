<template>
  <div>
    <h1>My games</h1>
    <div v-if="!gameGroup">
      You do not have a personal collection yet. You can create one by clicking the button below.<br />
      <Button @click="onClickCreate">
        Create
      </Button>
    </div>
    <div v-if="gameGroup">
      <GameGroupComponent :gameGroupId="gameGroup.id!" />

    </div>
    <Message v-if="errorMessage" severity="error" />
  </div>

</template>

<script lang="ts" setup>
import GameGroupComponent from '@/components/GameGroup/GameGroupComponent.vue';
import type { ResponseWrapper } from '@/model/api/Response';
import type { GameGroup } from '@/model/GameGroup';
import { createPersonalCollection, getPersonalCollection } from '@/services/api/PersonalCollection';
import { Button, Message } from 'primevue';
import { onMounted, ref, type Ref } from 'vue';

const gameGroup: Ref<GameGroup | null> = ref(null);
const errorMessage = ref<string>('');

onMounted(async () => {
  const collectionResponse = await getPersonalCollection()
  handleCollectionResponse(collectionResponse)
});

async function onClickCreate() {
  const collectionResponse = await createPersonalCollection()
  handleCollectionResponse(collectionResponse);
}

function handleCollectionResponse(collectionResponse: ResponseWrapper<GameGroup>) {
  errorMessage.value = '';
  if (collectionResponse.success) {
    gameGroup.value = collectionResponse.success;
  } else {
    if (collectionResponse.error?.status !== 404) {
      errorMessage.value = collectionResponse.error?.detail || 'An error occurred while fetching the collection.';
    }
  }
}

</script>