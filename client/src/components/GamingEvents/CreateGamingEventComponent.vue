<template>
  <div>
    <div class="grid">
      <div>
        Start*
      </div>
      <div>
         <DatePicker id="datepicker-24h" v-model="start" showTime hourFormat="24" fluid />
      </div>
      <div>
        End
      </div>
      <div>
        <DatePicker id="datepicker-24h" v-model="end" showTime hourFormat="24" fluid />
      </div>
      <div>
        Comment
      </div>
      <div>
        <Textarea v-model:model-value="comment"/> 
      </div>
      <div/>
      <div>
        
      </div>
    </div>
    <Message v-if="errorMessage" severity="error">{{ errorMessage }}</Message>
    <Button label="Create event" @click="onClickCreateEvent"/>
  </div>
</template>

<script lang="ts" setup>
import type { GamingEvent } from '@/model/GamingEvent';
import { addAllGroupMembersToGamingEvent, createGamingEvent } from '@/services/api/GamingEventsApiService';
import { Button, DatePicker, Message, Textarea } from 'primevue';
import { ref } from 'vue';

const start = ref(null);
const end = ref(null);
const comment = ref('');
const errorMessage = ref('');

const props = defineProps({
  gameGroupId: {
    type: Number,
    required: true
  }
})

const emit = defineEmits<{
  (e: 'event-created', gamingEvent: GamingEvent): void
}>();

async function onClickCreateEvent() {
  errorMessage.value = '';
  if (start.value) {
    const result = await createGamingEvent(props.gameGroupId, start.value, end.value, comment.value)
    if (result.success) {
      const addParticipantsResult = await addAllGroupMembersToGamingEvent(result.success.id);
      if (addParticipantsResult.success) {
         emit('event-created', addParticipantsResult.success);
      } else {
        errorMessage.value = addParticipantsResult.error?.detail ?? "Error";      
      }
    } else {
      errorMessage.value = result.error?.detail ?? "Error";      
    }
  } else {
    errorMessage.value = 'Please enter a start date.';
  }
}

</script>

<style lang="css" scoped>

input {
  line-height: 28px;
  margin: 4px;
}

.grid {
  display: grid;
  grid-template-columns: 1fr 2fr;
  gap: 0px;  
  grid-template-rows: repeat(5, 1fr);
  grid-column-gap: 0px;
  grid-row-gap: 4px; 
}


</style>