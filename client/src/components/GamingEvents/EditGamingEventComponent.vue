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
        <label for="regular">Regular event</label>
               
      </div>
      <div>
        <Checkbox  v-model="regular" label="Regular event" binary input-id="regular" style="margin-bottom: 4px"/>
        <span v-if="regular" style="padding-top: 8px; margin-left: 16px;">
          <RadioButtonGroup name="schedule" v-model="schedule">
            <RadioButton value="WEEKLY" label="Weekly" input-id="weekly" class="radio" size="large"/>
             <label for="weekly">Weekly</label>
            <RadioButton value="MONTHLY" label="Monthly" input-id="monthly" class="radio" size="large"/>
            <label for="monthly">Monthly</label>
          </RadioButtonGroup>

        </span>
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
    <Button label="Update event" @click="onClickUpdateEvent"/>
  </div>
</template>

<script lang="ts" setup>
import { GamingEvent, type Schedule } from '@/model/GamingEvent';
import { addAllGroupMembersToGamingEvent, createGamingEvent, fetchGamingEvent, updateGamingEvent } from '@/services/api/GamingEventsApiService';
import { Button, Checkbox, DatePicker, Message, RadioButton, RadioButtonGroup, Textarea } from 'primevue';
import { onMounted, ref, type Ref } from 'vue';

const start: Ref<Date | null> = ref(null);
const regular = ref(false);
const schedule = ref('WEEKLY');
const comment = ref('');
const errorMessage = ref('');

const props = defineProps({
  gameGroupId: {
    type: Number,
    required: true
  },
  gamingEventId: {
    type: Number,
    required: true
  }
})

onMounted(async () => {
  errorMessage.value = '';
  const result = await fetchGamingEvent(props.gamingEventId)
  
  if (result.success) {
    const event = result.success
    start.value = new Date(event.start);
    regular.value = event.schedule !== 'ONCE';
    schedule.value = event.schedule;
    comment.value = event.description ?? '';    
  } else {
    errorMessage.value = result.error?.detail ?? "Error loading event";
  }

});

const emit = defineEmits<{
  (e: 'event-updated', gamingEvent: GamingEvent): void
}>();

async function onClickUpdateEvent() {
  errorMessage.value = '';
  if (start.value) {
    const scheduleValue = regular.value ? schedule.value : 'ONCE';
    const result = await updateGamingEvent(props.gameGroupId, props.gamingEventId, start.value, scheduleValue as Schedule, comment.value)
    if (result.success) {
      emit('event-updated', result.success);            
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
  grid-template-rows: repeat(5, 0.75fr);
  grid-column-gap: 0px;
  grid-row-gap: 4px; 
}

.radio {
  margin-right: 4px;
  margin-left: 8px;
}


</style>