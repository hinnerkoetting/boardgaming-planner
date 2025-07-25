<template>
  <div>
    <h1>{{ gameGroup?.name || '' }}</h1>

    <div>
      <router-link :to="{ name: 'groupGamingEventsOverview', params: { gameGroupId: gameGroupId } }">Next
        events</router-link>

      &#183;

      <router-link :to="{ name: 'gameGroupStatistics', params: { gameGroupId: gameGroupId } }">Statistics</router-link>
    </div>

    <h2>
      Games
      <FilterGamesDialog :allTags="tags" :allGames="allGames" :visibleGames="displayedGames" :allPlayers="players"
        :numberOfPlayersInGroup="players.length" @updatedFilter="onUpdatedFilters" @opened="onDialogOpened" />
    </h2>

    <GamesCollection v-if="displayedGames.length > 0" :games="displayedGames" :game-group-id="gameGroupId"
      :withRateButton="isPartOfGroup" :withTagButton="isPartOfGroup" @ratingOpened="onDialogOpened"
      @game-rated="filterAndSort" :players="players" :lastVisitedGameGroup="lastVisitedGameGroup" />

    <template v-if="isPartOfGroup">
      <h2 class="green">Add game</h2>
      <AddGameToGroupComponent @game-added="onGameAdded" ref="addGameComponent" />
    </template>

    <div v-if="gameGroup?.type === GameGroupType.PERSONAL">
      <ImportCollectionFromBggComponent @imported="gamesImported" />
    </div>

    <div v-if="gameGroup?.type !== GameGroupType.PERSONAL">
      <h2>Players</h2>
      <DataTable :value="players" tableStyle="min-width: 20rem" class="players">
        <Column field="name" header="Name"></Column>
      </DataTable>
    </div>

    <Button v-if="isPartOfGroup && gameGroup?.type != GameGroupType.PERSONAL" severity="danger"
      @click="onClickLeaveButton">Leave
      group</Button>
  </div>
</template>

<script setup lang="ts">
import DataTable from 'primevue/datatable'
import Column from 'primevue/column'
import { onMounted, type Ref } from 'vue'
import { ref } from 'vue'
import { GameGroup, GameGroupType } from '@/model/GameGroup'
import type { Player } from '@/model/Player/Player'
import type { Game, RatedGame } from '@/model/Game'
import GamesCollection from '@/components/Game/GamesCollection.vue'
import router from '@/router'
import {
  addGameToGroup,
  fetchGamesInGroup,
  fetchPlayersInGroup,
  leaveGroup,
  loadGameGroup
} from '@/services/api/GameGroupApiService'
import Button from 'primevue/button'
import EventBus from '@/services/EventBus'
import { getCurrentPlayerId } from '@/services/LoginService'
import AddGameToGroupComponent from '@/components/Game/AddGameToGroupComponent.vue'
import { EventMessage } from '@/model/internal/EventMessage'
import type { TagModel } from '@/model/TagModel'
import FilterGamesDialog from '@/components/Game/filter/FilterGamesDialog.vue'
import { FilterService } from '@/services/FilterService'
import { subscribeToEventsOnGameGroup } from '@/services/ServerSentEvents'
import { useToast } from 'primevue/usetoast'
import type { ToastMessageOptions } from 'primevue'
import type { GameGroupEvent } from '@/services/GameGroupEvent'
import { loadTags } from '@/services/StoreApiService'
import { importCollectionFromBgg } from '@/services/api/BggApiService'
import ImportCollectionFromBggComponent from './ImportCollectionFromBggComponent.vue'

const gameGroup: Ref<GameGroup | null> = ref(null)
const players: Ref<Player[]> = ref([])
const allGames: Ref<RatedGame[]> = ref([])
const displayedGames: Ref<RatedGame[]> = ref([])
const tags: Ref<TagModel[]> = ref([])
const isPartOfGroup = ref(false)

const props = defineProps<{
  gameGroupId: number
}>()

const addGameComponent: Ref<typeof AddGameToGroupComponent | undefined> = ref(undefined)
const toast = useToast()
const lastVisitedGameGroupStored = localStorage.getItem('gameGroup_visited_' + props.gameGroupId)
const lastVisitedGameGroup = ref(
  (lastVisitedGameGroupStored && Number(lastVisitedGameGroupStored)) || 0
)

onMounted(async () => {
  localStorage.setItem('gameGroup_visited_' + props.gameGroupId, new Date().getTime().toString())
  loadGameGroup(props.gameGroupId).then((result) => {
    gameGroup.value = result
  })
  fetchPlayersInGroup(props.gameGroupId).then((result) => {
    players.value = result
    isPartOfGroup.value = players.value.some((player) => player.id === getCurrentPlayerId())
    if (isPartOfGroup.value) {
      subscribeToEvents()
    }
    filterAndSort()
  })
  fetchGamesInGroup(props.gameGroupId).then((result) => {
    allGames.value = result
    filterAndSort()
  })
  loadTags().then((response) => {
    tags.value = response
  })
})

async function subscribeToEvents() {
  subscribeToEventsOnGameGroup(props.gameGroupId, (data) => {
    const newToast = createToast(data)
    if (newToast) {
      toast.add(newToast)
    }
  })
}

function createToast(data: GameGroupEvent): ToastMessageOptions | undefined {
  if (data.sourcePlayerId === getCurrentPlayerId()) {
    return
  }
  if (data.eventType === 'GAME_ADDED') {
    return {
      severity: 'info',
      summary: `Game added by ${data.sourcePlayerName}`,
      detail: data.description,
      life: 3000
    }
  } else if (data.eventType === 'PLAYER_ADDED') {
    return {
      severity: 'info',
      summary: `Player added by ${data.sourcePlayerName}`,
      detail: data.description,
      life: 3000
    }
  } else if (data.eventType === 'RATING') {
    return {
      severity: 'info',
      summary: `Game rated by ${data.sourcePlayerName}`,
      detail: data.description,
      life: 3000
    }
  }
}

async function onGameAdded(game: Game, callback: (message: EventMessage) => void) {
  if (!game.id) {
    callback(new EventMessage('Game has no id', false))
    return
  }
  if (allGames.value.find((existingGame) => existingGame.id == game.id)) {
    callback(new EventMessage('Game already exists', false))
    return
  }
  const response = await addGameToGroup(props.gameGroupId, game.id!)
  if (response.error) {
    callback(new EventMessage(response.error.detail, false))
    return
  }
  const gameGroupGame: RatedGame = {
    ...game,
    rating: {
      myRating: undefined,
      numberOfVotes: 0,
      averageRating: 0,
      existsVeto: false
    },
    tags: {
      global: [],
      group: [],
      player: []
    }
  }
  displayedGames.value.push(gameGroupGame)
  allGames.value.push(gameGroupGame)

  callback(new EventMessage('Game added', true))
}

function sortGames() {
  displayedGames.value.sort((game1, game2) => {
    const difference = game2.rating.averageRating - game1.rating.averageRating
    if (difference !== 0) {
      return difference
    }
    // if both games are rated differently, number of votes decided sorting.
    // If above 5 more votes are better, if below 5, less votes are better.
    if (game1.rating.averageRating > 5) {
      return game2.rating.numberOfVotes - game1.rating.numberOfVotes
    } else {
      return game1.rating.numberOfVotes - game2.rating.numberOfVotes
    }
  })
}

async function onClickLeaveButton() {
  await leaveGroup(props.gameGroupId)
  EventBus.emit('gaming-group-removed')
  await router.push({ name: 'gameGroups' })
  router.go(0) // not sure why this is necessary, otherwise the page will not be displayed
}

function onUpdatedFilters(filteredGames: RatedGame[]) {
  displayedGames.value = filteredGames
  filterAndSort()
}

function onDialogOpened() {
  addGameComponent.value?.reset()
}

function filterAndSort() {
  filterGames()
  sortGames()
}

function filterGames() {
  displayedGames.value = new FilterService().filterWithStoredSettings(allGames.value, players.value)
}

function gamesImported() {
  fetchGamesInGroup(props.gameGroupId).then((result) => {
    allGames.value = result
    filterAndSort()
  })
}

</script>

<style lang="css" scoped>
h2 {
  margin-top: 16px;
  margin-bottom: 8px;
}

.filter {
  margin: 16x 0 16px 0;
}

.players:v-deep(tr) {
  --p-datatable-header-cell-background: var(--color-background);
  --p-datatable-row-background: var(--color-background);
}
</style>
