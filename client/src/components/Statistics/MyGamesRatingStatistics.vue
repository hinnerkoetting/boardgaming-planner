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

const myRatings: Ref<Record<number, number>> = ref({});


function groupGamesByRating(games: RatedGame[]): void {  
  Array.from(Array(6).keys()).forEach(i => {    
    myRatings.value[i] = 0;
  });

  games.forEach(game => {
          
    if (game.rating.myRating !== null) {
      switch (game.rating.myRating) {
        case 0:
          myRatings.value[1] = myRatings.value[1] + 1;
          break;
        case 1:
          myRatings.value[2] = myRatings.value[2] + 1;
          break;
        case 4:
          myRatings.value[3] = myRatings.value[3] + 1;
          break;
        case 7:
          myRatings.value[4] = myRatings.value[4] + 1;          
          break;
        case 10:
          myRatings.value[5] = myRatings.value[5] + 1;          
          break;
        default:
            console.error("Unknown rating: " + game.rating.myRating); 
            break;
      }      
    } else {
      myRatings.value[0] = myRatings.value[0] + 1;
    }
    
  });
}

onMounted(() => {

  groupGamesByRating(props.games)
  var myChart = init(chart.value);
  
  new ResizeObserver(() => myChart.resize()).observe(chart.value!);

  const option = {

    xAxis: {
      type: 'category',
      data: ['Not voted', '👎', '✋', '👍', '👍👍', '👍👍👍'],      
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        data: Object.values(myRatings.value),
        type: 'bar',
        name: "My ratings",
        itemStyle: {color: 'red'},
        label: {
          show: true,
          position: 'top',
          formatter: '{c}',
        }        
      }
    ],
    title: {
      show: true,
      text: 'My ratings',
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
  <div style="width: 100%; height: 400px;" ref="chart"></div>

</template>