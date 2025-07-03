<template>
  <div>
    <h1>Rate</h1>
    <div id="ratingButtons">
      <Button @click="onClickRating(10)">Very good</Button>
      <Button @click="onClickRating(7)">Good</Button>
      <Button @click="onClickRating(4)">Ok</Button>
      <Button @click="onClickRating(1)">Meh</Button>
      <Button @click="onClickRating(0)">Veto</Button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { Game } from '@/model/Game'
import { updateInterest } from '@/services/ApiService'
import { getCurrentUserId } from '@/services/LoginService'
import Button from 'primevue/button'
import { ref } from 'vue'

const props = defineProps({
  game: {
    type: Game,
    required: true
  },
  gameGroupId: {
    type: Number,
    required: true
  }
})

const emit = defineEmits(['game-rated'])

const game = ref(props.game)

async function onClickRating(rating: number) {
  await updateInterest({
    gameId: game.value.id!,
    playerId: getCurrentUserId(),
    gameGroupId: props.gameGroupId,
    rating: rating
  })
  emit('game-rated', rating)
}
</script>

<style lang="css" scoped>
#ratingButtons {
  display: flex;
  flex-direction: column;
  flex-wrap: wrap;
}

Button {
  margin-top: 4px;
}
</style>
