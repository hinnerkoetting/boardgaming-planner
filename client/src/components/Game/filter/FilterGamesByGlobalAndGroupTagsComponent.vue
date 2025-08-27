<template>
  <div class="filterContent tagFilterOption">
    <div v-for="tag in nonPlayerTags" :key="tag.id" class="one-filter">
      <Button :severity="tag.selected === 'FILTER_WITH' ? 'primary' : 'secondary'" @click="onClickFilterWith(tag)"
        class="filterButton">Only {{ tag.description }}</Button>
      <Button :severity="tag.selected === 'FILTER_WITHOUT' ? 'primary' : 'secondary'" @click="onClickFilterWithout(tag)"
        class="filterButton">No {{ tag.description }}</Button>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { TagSelection } from '@/services/FilterService';
import { Button } from 'primevue';
import type { PropType } from 'vue';


const nonPlayerTags = defineModel('nonPlayerTags', {
  type: Array as PropType<TagSelection[]>,
  required: true
})

const emits = defineEmits<{
  (e: 'change'): void
}>()


async function onClickFilterWith(tag: TagSelection) {
  if (tag.selected === 'FILTER_WITH') {
    tag.selected = 'DO_NOT_FILTER'
  } else {
    tag.selected = 'FILTER_WITH'
  }
  emits('change')
}

async function onClickFilterWithout(tag: TagSelection) {
  if (tag.selected === 'FILTER_WITHOUT') {
    tag.selected = 'DO_NOT_FILTER'
  } else {
    tag.selected = 'FILTER_WITHOUT'
  }
  emits('change')
}

</script>
