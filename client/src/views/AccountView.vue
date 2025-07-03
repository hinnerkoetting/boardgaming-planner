<template>
  <div>
    <RouterLink to="/">back</RouterLink>
    <h1>Account</h1>
    <div class="meWrapper">
      <div>Name: <InputText v-model="nameModel" disabled /></div>
      <Button severity="secondary" @click="onClickChangeName">Update name </Button>
      <Button severity="secondary" @click="onClickUpdatePassword">Update password </Button>
      <Button severity="danger" @click="onClickDeleteMe">Delete your account</Button>
    </div>
    <Dialog v-model:visible="nameDialogVisible" modal header="Change name">
      <div class="dialogContent">
        <div class="row">
          <div>Name</div>
          <InputText
            v-model="nameModel"
            class="input"
            v-on:keyup.enter="onClickSubmitNameChange"
            @focus="($event.target as any).select()"
          />
        </div>
        <Message v-if="nameErrorMessage" severity="error">{{ nameErrorMessage }}</Message>
        <Button severity="primary" @click="onClickSubmitNameChange">Change </Button>
      </div>
    </Dialog>

    <Dialog v-model:visible="passwordDialogVisible" modal header="Update password">
      <div class="dialogContent">
        <div class="row">
          <div>Old</div>
          <InputText
            type="password"
            v-model="oldPasswordModel"
            autocomplete="current-password"
            class="input"
            v-on:keyup.enter="onClickSubmitNameChange"
          />
        </div>
        <div class="row">
          New
          <InputText
            type="password"
            v-model="newPasswordModel"
            class="input"
            autocomplete="new-password"
            v-on:keyup.enter="onClickSubmitPasswordChange"
          />
        </div>
        <Message v-if="passwordErrorMessage" severity="error">{{ passwordErrorMessage }}</Message>
        <Button severity="primary" @click="onClickSubmitPasswordChange">Change </Button>
      </div>
    </Dialog>
    <Dialog
      v-model:visible="deleteDialogVisible"
      modal
      header="Are you sure to delete your account?"
    >
      <div class="dialogContent">
        <Message v-if="deleteErrorMessage" severity="error">{{ deleteErrorMessage }}</Message>
        <Button severity="secondary" @click="onClickCancelDeleteMe">Cancel (keep account) </Button>
        <br />
        <Button severity="danger" @click="onClickConfirmDeleteMe"
          >Confirm deletion (cannot be undone)
        </Button>
      </div>
    </Dialog>
  </div>
</template>

<script setup lang="ts">
import Button from 'primevue/button'
import InputText from 'primevue/inputtext'
import { onMounted, ref } from 'vue'

import { getInformationAboutMe } from '@/services/api/ApiService'
import Dialog from 'primevue/dialog'
import { deleteMe, updateName, updatePassword } from '@/services/api/AccountService'
import { logout, updateToken } from '@/services/LoginService'
import Message from 'primevue/message'
import router from '@/router'

const nameModel = ref('')
const nameDialogVisible = ref(false)
const nameErrorMessage = ref('')

const oldPasswordModel = ref('')
const newPasswordModel = ref('')
const passwordDialogVisible = ref(false)
const passwordErrorMessage = ref('')

const deleteErrorMessage = ref('')
const deleteDialogVisible = ref(false)

onMounted(async () => {
  const me = await getInformationAboutMe()
  nameModel.value = me.name
})

function onClickChangeName() {
  nameDialogVisible.value = true
  nameErrorMessage.value = ''
}

async function onClickSubmitNameChange() {
  const response = await updateName(nameModel.value)
  if (response.success) {
    if (response.success.token) {
      updateToken(response.success.token)
    }
    nameDialogVisible.value = false
  } else {
    nameErrorMessage.value = response.error?.detail || 'Error'
  }
}

function onClickUpdatePassword() {
  passwordDialogVisible.value = true
  passwordErrorMessage.value = ''
  oldPasswordModel.value = ''
  newPasswordModel.value = ''
}

async function onClickSubmitPasswordChange() {
  const response = await updatePassword(oldPasswordModel.value, newPasswordModel.value)
  if (response.success) {
    passwordDialogVisible.value = false
  } else {
    passwordErrorMessage.value = response.error?.detail || 'Error'
  }
}

function onClickDeleteMe() {
  deleteDialogVisible.value = true
}

function onClickCancelDeleteMe() {
  deleteDialogVisible.value = false
}

async function onClickConfirmDeleteMe() {
  const response = await deleteMe()
  if (response.success) {
    deleteDialogVisible.value = false
    logout()
    router.push('home')
  } else {
    deleteErrorMessage.value = response.error?.detail || 'Error'
  }
}
</script>

<style lang="css" scoped>
.meWrapper {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.dialogContent {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 4px;
}

.input {
  width: 150px;
}
</style>
