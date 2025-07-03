<template>
  <div>
    <h1>Want to play game next time?</h1>
    <Message v-if="errorMessage" severity="error" class="full-width">{{ errorMessage }}</Message>
    <div id="ratingButtons">
      <Button @click="onClickRating(10)">&#128077;&#128077;&#128077; Absolutely</Button>
      <Button @click="onClickRating(7)">&#128077;&#128077; </Button>
      <Button @click="onClickRating(4)">&#128077; </Button>
      <Button @click="onClickRating(1)" severity="warn"><div class="meh">&#128077;</div></Button>
      <Button @click="onClickRating(0)" severity="danger"
        ><div class="veto">&#128077;</div>
        Veto</Button
      >

      <Button severity="secondary" @click="oncClickDeleteRating()" class="delete"
        >Forget rating</Button
      >
    </div>
  </div>
</template>

<script setup lang="ts">
import { Game } from '@/model/Game'
import type { Rating } from '@/model/Rating'
import { deleteInterest, updateRating } from '@/services/api/RatingService'
import { getCurrentPlayerId } from '@/services/LoginService'
import Button from 'primevue/button'
import Message from 'primevue/message'
import { ref, type Ref } from 'vue'

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
const errorMessage: Ref<string> = ref('')

async function onClickRating(rating: number) {
  const response = await updateRating({
    gameId: game.value.id!,
    playerId: getCurrentPlayerId(),
    gameGroupId: props.gameGroupId,
    rating: rating
  })
  if (response.success) {
    emit('gameRated', response.success)
  } else {
    errorMessage.value = response.error?.detail || 'Error'
  }
}

async function oncClickDeleteRating() {
  const response = await deleteInterest({
    gameId: game.value.id!,
    playerId: getCurrentPlayerId(),
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
  gap: 8px;
}

Button {
  margin-top: 4px;
}

.meh {
  transform: rotate(90deg);
}

.veto {
  transform: rotate(180deg);
}

.delete {
  margin-top: 16px;
}
</style>
