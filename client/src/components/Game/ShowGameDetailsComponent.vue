<template>
  <div class="full-height">
    <div class="content">
      <Image :src="game.imageUrl" class="image" style="max-width: min(100%, 600px)" imageStyle="max-width: 100%" />
      <div class="row">
        <div :class="showExpandedDescription ? '' : 'summary'" @click="onClickDescription">
          {{ htmlDecode(game.description) }}
        </div>
        <Button link class="expandButton nopadding" @click="onClickDescription">{{
          showExpandedDescription ? 'Collapse' : 'Expand'
        }}</Button>
      </div>
      <div class="row" v-if="game.minPlayers && game.maxPlayers">
        <div class="title">
          {{ game.minPlayers }} - {{ game.maxPlayers }}
          <span v-if="showExpandedPlayerNumber"
            ><br />
            {{ game.bestNumberOfPlayers.join(', ') }} <br />
            {{ game.recommendedNumberOfPlayers.join(', ') }}
          </span>
        </div>
        <div>
          players
          <Button link class="expandButton nopadding" @click="onClickTogglePlayers">{{
            showExpandedPlayerNumber ? 'Collapse' : 'Expand'
          }}</Button>
          <span v-if="showExpandedPlayerNumber">
            <br />
            best
            <br />
            recommended
          </span>
        </div>
      </div>
      <div class="row" v-if="game.playingTimeMinutes">
        <div class="title">{{ game.playingTimeMinutes }}</div>
        <div>minutes playing time</div>
      </div>
      <div class="row" v-if="game.url">
        <a :href="game.url" target="_blank">Further information</a>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { Game } from '@/model/Game'
import Button from 'primevue/button'
import Image from 'primevue/image'
import { ref, type PropType } from 'vue'

const props = defineProps({
  game: {
    type: Object as PropType<Game>,
    required: true
  },
  withRateButton: {
    type: Boolean,
    default: true
  }
})

defineEmits<{
  (e: 'game', game: Game): void
}>()

const showExpandedDescription = ref(false)
const showExpandedPlayerNumber = ref(false)

const game = ref(props.game)

function htmlDecode(input: string) {
  var doc = new DOMParser().parseFromString(input, 'text/html')
  return doc.documentElement.textContent
}

function onClickDescription() {
  showExpandedDescription.value = !showExpandedDescription.value
}

function onClickTogglePlayers() {
  showExpandedPlayerNumber.value = !showExpandedPlayerNumber.value
}
</script>

<style scoped>
.title {
  display: block;
  text-overflow: ellipsis;
  overflow: hidden;
  white-space: nowrap;
  text-align: center;
  max-width: 320px;

  font-weight: 700;
  font-size: 1.2em;
  width: 150px;
  text-align: right;
  padding-right: 16px;
}

.image {
  max-width: 600px;
}

.content {
  display: flex;
  flex-direction: column;
  flex-wrap: wrap;
  align-items: center;
}

.row {
  max-width: min(100%, 600px);
  width: 100%;
  border: 0;
  border-top: 1px dashed #ccc;
  display: flex;
  flex-wrap: wrap;
  margin-top: 16px;
  line-height: 28px;
}

.summary {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  width: calc(100% - 70px);
  display: inline-block;
}

.expandButton {
  text-align: end;
}

.nopadding {
  padding: 0;
}
</style>
