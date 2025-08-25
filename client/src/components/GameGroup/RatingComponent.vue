<template>
  <div>
    <h2>{{ game.name }}</h2>
    <Message v-if="errorMessage" severity="error" class="full-width">{{ errorMessage }}</Message>
    <div class="ratingButtons">
      <div class="ratingStars">
        <span @click="onClickRating($event)" data-value="2" class="pi pi-star" @mouseover="onMouseOver($event)" />
        <span @click="onClickRating($event)" data-value="4" class="pi pi-star" @mouseover="onMouseOver($event)" />
        <span @click="onClickRating($event)" data-value="6" class="pi pi-star" @mouseover="onMouseOver($event)" />
        <span @click="onClickRating($event)" data-value="8" class="pi pi-star" @mouseover="onMouseOver($event)" />
        <span @click="onClickRating($event)" data-value="10" class="pi pi-star" @mouseover="onMouseOver($event)" />
      </div>

      <Button @click="onClickRating($event)" data-value="0" severity="danger" @mouseover="onMouseOver($event)">
        <div class="veto" />

        Veto
      </Button>

      <Button severity="secondary" @click="onClickDeleteRating()" class="delete">Forget rating</Button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { Game, RatedGame } from '@/model/Game'
import type { Rating } from '@/model/Rating'
import { deleteInterest, updateRating } from '@/services/api/RatingService'
import { getCurrentPlayerId } from '@/services/LoginService'
import Button from 'primevue/button'
import Message from 'primevue/message'
import { onMounted, ref, type PropType, type Ref } from 'vue'

const props = defineProps({
  game: {
    type: Object as PropType<RatedGame>,
    required: true
  },
  gameGroupId: {
    type: Number,
    required: true
  }
})

onMounted(() => {
  if (props.game.rating?.myRating)
    markStarsBasedOnRating(props.game.rating.myRating)
})

const emit = defineEmits<{
  (e: 'gameRated', rating: Rating): void
  (e: 'gameRatingDeleted', rating: Rating): void
}>()

const game = ref(props.game)
const errorMessage: Ref<string> = ref('')

async function onClickRating(event: MouseEvent) {
  const response = await updateRating({
    gameId: game.value.id!,
    playerId: getCurrentPlayerId(),
    gameGroupId: props.gameGroupId,
    rating: Number((event.target as HTMLElement).dataset.value)
  })
  if (response.success) {
    emit('gameRated', response.success)
  } else {
    errorMessage.value = response.error?.detail || 'Error'
  }
}

async function onMouseOver(event: MouseEvent) {
  const value = Number((event.target as HTMLElement).dataset.value)
  markStarsBasedOnRating(value)
}

function markStarsBasedOnRating(rating: number) {
  const stars = document.querySelectorAll('.ratingStars > span')
  for (let i = 0; i < stars.length; i++) {
    const star = stars[i] as HTMLSpanElement
    if (Number(star.dataset.value) <= rating) {
      star.style.color = 'gold'
      star.classList.add('pi-star-fill')
      star.classList.remove('pi-star')
    } else {
      star.style.color = 'black'
      star.classList.remove('pi-star-fill')
      star.classList.add('pi-star')
    }
  }
}

async function onClickDeleteRating() {
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
.ratingButtons {
  display: flex;
  flex-direction: column;
  flex-wrap: wrap;
  gap: 8px;
}

.ratingStars {

  display: flex;
  flex-direction: row;
  gap: 4px;

  span {
    font-size: 32px
  }
}

Button {
  margin-top: 4px;
}

.veto {
  transform: rotate(180deg);
}

.delete {
  margin-top: 16px;
}

.pi {
  cursor: pointer;
}
</style>
