<template>
  <div>
    <div v-if="isLoggedInRef">
      <Menubar :model="items" breakpoint="5000px">

        <template #start>
          <div style="width: 100%;">
            <template v-if="selectedGameGroup">
              <RouterLink :to="{ name: 'gameGroup', params: { gameGroupId: selectedGameGroup?.id } }">Games</RouterLink>
              &#183;
              <RouterLink :to="{ name: 'groupGamingEventsOverview', params: { gameGroupId: selectedGameGroup?.id } }">
                Calendar</RouterLink>
              &#183;
              <RouterLink :to="{ name: 'gameGroupStatistics', params: { gameGroupId: selectedGameGroup?.id } }">
                Stats</RouterLink>
              &#183;
            </template>
            <template v-if="!selectedGameGroup">
              <RouterLink to="/gameGroups">Select group</RouterLink>
              &#183;
            </template>
            <RouterLink to="/personalCollection">Me</RouterLink>
          </div>
        </template>
      </Menubar>
    </div>
  </div>
</template>


<script setup lang="ts">
import { doesCurrentPlayerHaveRole, isLoggedIn, logout } from '@/services/LoginService'
import router from '@/router'
import { Role } from '@/model/api/JwtPayload'
import { ref, type Ref } from 'vue'
import EventBus, { EventBusEvent } from '@/services/EventBus'
import type { GameGroup } from '@/model/GameGroup'
import { getStoredCurrentGameGroup, removeCurrentGameGroup, storeCurrentGameGroup } from '@/services/GameGroupService'
import { Menubar } from 'primevue'
import type { MenuItem } from 'primevue/menuitem'
import { RouterLink } from 'vue-router'

const isLoggedInRef = ref(isLoggedIn())
const selectedGameGroup: Ref<GameGroup | undefined> = ref(getStoredCurrentGameGroup())

EventBus.addEventListener('login-status', () => {
  isLoggedInRef.value = isLoggedIn()
})

EventBus.addEventListener('gaming-group-opened', (event: Event) => {
  const eventBusEvent = event as EventBusEvent
  selectedGameGroup.value = eventBusEvent.data
  storeCurrentGameGroup(eventBusEvent.data)
})

EventBus.addEventListener('gaming-group-removed', () => {
  selectedGameGroup.value = undefined
  removeCurrentGameGroup()
})

function onClickLogout() {
  logout()
  router.push({ name: 'home' })
}


const items: Ref<MenuItem[]> = ref([
  {
    label: 'Select group',
    icon: 'pi pi-users',
    command: () => {
      router.push({ name: 'gameGroups' })
    }
  },
  {
    label: 'Settings',
    icon: 'pi pi-cog',
    command: () => {
      router.push({ name: 'account' })
    }
  },
  {
    label: 'Logout',
    icon: 'pi pi-sign-out',
    command: () => {
      onClickLogout();
    }
  },
  {
    label: 'Admin',
    icon: 'pi pi-star',
    visible: doesCurrentPlayerHaveRole(Role.ADMIN),
    items: [
      {
        label: 'Players',

        command: () => {
          router.push({ name: 'adminPlayers' })
        }
      },
      {
        label: 'Games',
        command: () => {
          router.push({ name: 'adminGames' })
        }
      },
      {
        label: 'Groups',
        command: () => {
          router.push({ name: 'adminGameGroups' })
        }
      },
      {
        label: 'Tags',
        command: () => {
          router.push({ name: 'adminTags' })
        }
      }
    ]
  }
]);
</script>
