<script setup lang="ts">
import { RatedGame } from '@/model/Game';
import * as echarts from 'echarts';
import { onMounted, ref, useTemplateRef, type PropType, type Ref } from 'vue';

const chart = useTemplateRef("chart")

const props = defineProps({
  games: {
    type: Array as PropType<RatedGame[]>,
    required: true
  }
}
);

const average: Ref<Record<number, number>> = ref({});
const myRatings: Ref<Record<number, number>> = ref({});


function groupGamesByRating(games: RatedGame[]): void {  
  Array.from(Array(10).keys()).forEach(i => {
    average.value[i] = 0;
    myRatings.value[i] = 0;
  });

  games.forEach(game => {
    const rating = Math.floor(game.rating.averageRating)
   
    average.value[rating] = average.value[rating] + 1;
    if (game.rating.myRating) {
      const myRating = Math.floor(game.rating.myRating)
      myRatings.value[rating] = myRatings.value[myRating] + 1;
    }
    
  });
}

onMounted(() => {

  groupGamesByRating(props.games)
  var myChart = echarts.init(chart.value);
  var option;

  option = {
    xAxis: {
      type: 'category',
      data: Object.keys(average.value)
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        data: Object.values(average.value),
        type: 'bar',
        name: "Average rating",
        label: {
          show: true,
          position: 'top',
          formatter: '{c}',
        }
      },
      {
        data: Object.values(myRatings.value),
        type: 'bar',
        name: "My rating",
        label: {
          show: true,
          position: 'top',
          formatter: '{c}',
        }
      }
    ],
    title: {
      show: true,
      text: 'Games rating',
    }, legend: {      
      orient: 'vertical',
      right: 10,
      top: 'center'
    },

  };

  option && myChart.setOption(option);
});



</script>

<template>
  <div style="width: 600px; height: 400px;" ref="chart"></div>

</template>