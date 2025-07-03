<template>
  <div>
    <h1>Rate</h1>
    <div id="ratingButtons">
      <Button @click="onClickRating(10)">Very good</Button>
      <Button @click="onClickRating(7)">Good</Button>
      <Button @click="onClickRating(4)">Ok</Button>
      <Button @click="onClickRating(1)">Meh</Button>
      <Button @click="onClickRating(0)">Veto</Button>

      <Button severity="danger" @click="oncClickDeleteRating()">Forget rating</Button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { Game } from '@/model/Game'
import type { Rating } from '@/model/Rating'
import { deleteInterest, updateRating } from '@/services/ApiService'
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

const emit = defineEmits<{
  (e: 'gameRated', rating: Rating): void
  (e: 'gameRatingDeleted', rating: Rating): void
}>()

const game = ref(props.game)

async function onClickRating(rating: number) {
  const response = await updateRating({
    gameId: game.value.id!,
    playerId: getCurrentUserId(),
    gameGroupId: props.gameGroupId,
    rating: rating
  })
  if (response.success) {
    emit('gameRated', response.success)
  } else {
    console.info(` Error when rating a game ${JSON.stringify(response.error)}`)
  }
}

async function oncClickDeleteRating() {
  const response = await deleteInterest({
    gameId: game.value.id!,
    playerId: getCurrentUserId(),
    gameGroupId: props.gameGroupId,
    rating: undefined
  })
  if (response.success) {
    emit('gameRatingDeleted', response.success)
  } else {
    console.info(` Error when deleting rating for a game ${JSON.stringify(response.error)}`)
  }
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
