<template>
  <DataTable :value="players" tableStyle="min-width: 20rem" class="players">
    <Column field="name" header="Name"></Column>
  </DataTable>
</template>

<script setup lang="ts">
import type { Player } from '@/model/Player/Player';
import { Column, DataTable } from 'primevue';
import type { PropType } from 'vue';


defineProps({
  players: {
    type: Object as PropType<Player[]>,
    required: true
  }
})
</script>