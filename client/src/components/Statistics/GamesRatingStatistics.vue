<script setup lang="ts">
import { RatedGame } from '@/model/Game';
import { init } from 'echarts';
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


function groupGamesByRating(games: RatedGame[]): void {  
  Array.from(Array(10).keys()).forEach(i => {
    average.value[i] = 0;
  });

  games.forEach(game => {
    const rating = Math.floor(game.rating.averageRating)
   
    average.value[rating] = average.value[rating] + 1;
    
  });
}

onMounted(() => {

  groupGamesByRating(props.games)
  var myChart = init(chart.value);
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
        name: "Average",
        label: {
          show: true,
          position: 'top',
          formatter: '{c}',
        }
      }      
    ],
    title: {
      show: true,
      text: 'Number of games by rating',
    }, legend: {      
      orient: 'vertical',
      right: 0,
      top: 'bottom'
    },

  };

  option && myChart.setOption(option);
});



</script>

<template>
  <div style="width: 100%; height: 400px;" ref="chart"></div>

</template>