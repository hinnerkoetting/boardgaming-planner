<script setup lang="ts">
import { RatedGame } from '@/model/Game';
import { init } from 'echarts';
import { onMounted, ref, useTemplateRef, type PropType, type Ref } from 'vue';
import { loadTags } from '../../services/StoreApiService';

const chart = useTemplateRef("chart")

const props = defineProps({
  games: {
    type: Array as PropType<RatedGame[]>,
    required: true
  }
});

const numberOfGames = props.games.length

const gamesWithTag: Ref<Record<string, number>> = ref({});
const gamesWithoutTag: Ref<Record<string, number>> = ref({});


async function groupGamesByTag(games: RatedGame[]): Promise<void> {
  const allTags = await loadTags()
  allTags.filter(tag => tag.type !== 'PLAYER').forEach(tag => {
    gamesWithTag.value[tag.description] = 0;
  });

  games.forEach(game => {

    game.tags.global.forEach(tag => {
      gamesWithTag.value[tag.description] = gamesWithTag.value[tag.description] + 1;
    });
    game.tags.group.forEach(tag => {
      gamesWithTag.value[tag.description] = gamesWithTag.value[tag.description] + 1;
    });

  });

  Object.entries(gamesWithTag.value).forEach((value, key) => {
    gamesWithoutTag.value[key] = numberOfGames - value[1]
  });
}

onMounted(async () => {

  await groupGamesByTag(props.games)
  var myChart = init(chart.value);

  new ResizeObserver(() => myChart.resize()).observe(chart.value!);


  const option = {
    xAxis: {
      type: 'category',
      data: Object.keys(gamesWithTag.value),
      axisLabel: {
        interval: 0,        // Show all labels
        overflow: 'none',
        rotate: 30
      }
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        data: Object.values(gamesWithTag.value),
        type: 'bar',
        name: "With category",
        label: {
          show: true,
          position: 'top',
          formatter: '{c}',
        }
      },
      {
        data: Object.values(gamesWithoutTag.value),
        type: 'bar',
        itemStyle: { color: 'red' },
        name: "Without category",
        label: {
          show: true,
          position: 'top',
          formatter: '{c}',
        }
      }
    ],
    title: {
      show: true,
      text: 'Games by category',
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
