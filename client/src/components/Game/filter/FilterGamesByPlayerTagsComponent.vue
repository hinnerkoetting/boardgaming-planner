<template>
  <div class="filterContent tagFilterOption">
    <div v-for="tag in playerTags" :key="tag.id" class="one-filter">
      <Button :severity="tag.selected === 'FILTER_EVERYONE' ? 'primary' : 'secondary'"
        @click="onClickFilterEveryone(tag)" class="filterButton">Everyone {{ tag.description }}</Button>
      <Button :severity="tag.selected === 'FILTER_ANYONE' ? 'primary' : 'secondary'" @click="onClickFilterAnyone(tag)"
        class="filterButton">Anyone {{ tag.description }}</Button>
      <Button :severity="tag.selected === 'FILTER_NOBODY' ? 'primary' : 'secondary'" @click="onClickFilterNobody(tag)"
        class="filterButton">Nobody {{ tag.description }}</Button>
    </div>
  </div>
</template>


<script setup lang="ts">
import type { PlayerTagSelection } from '@/services/FilterService';
import { Button } from 'primevue';


const playerTags = defineModel('playerTags', {
  type: Array as () => PlayerTagSelection[],
  default: () => []
})

const emits = defineEmits<{
  (e: 'change'): void
}>()


async function onClickFilterEveryone(tag: PlayerTagSelection) {
  if (tag.selected === 'FILTER_EVERYONE') {
    tag.selected = 'DO_NOT_FILTER'
  } else {
    tag.selected = 'FILTER_EVERYONE'
  }
  emits('change')
}

async function onClickFilterAnyone(tag: PlayerTagSelection) {
  if (tag.selected === 'FILTER_ANYONE') {
    tag.selected = 'DO_NOT_FILTER'
  } else {
    tag.selected = 'FILTER_ANYONE'
  }
  emits('change')
}

async function onClickFilterNobody(tag: PlayerTagSelection) {
  if (tag.selected === 'FILTER_NOBODY') {
    tag.selected = 'DO_NOT_FILTER'
  } else {
    tag.selected = 'FILTER_NOBODY'
  }
  emits('change')
}
</script>
