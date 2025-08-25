<script setup lang="ts">
import { RatedGame } from '@/model/Game'
import { init } from 'echarts'
import { onMounted, ref, useTemplateRef, type PropType, type Ref } from 'vue'

const chart = useTemplateRef('chart')

const props = defineProps({
  games: {
    type: Array as PropType<RatedGame[]>,
    required: true
  }
})

const average: Ref<Record<number, number>> = ref({})

function groupGamesByRating(games: RatedGame[]): void {
  Array.from(Array(11).keys()).forEach((i) => {
    average.value[i] = 0
  })

  games.forEach((game) => {
    if (game.rating.averageRating) {
      const rating = Math.floor(game.rating.averageRating)
      average.value[rating + 1] = (average.value[rating + 1] || 0) + 1
    } else {
      // not rated yet
      average.value[0] = (average.value[0] || 0) + 1
    }

  })
}

onMounted(() => {
  groupGamesByRating(props.games)
  var myChart = init(chart.value)

  new ResizeObserver(() => myChart.resize()).observe(chart.value!)

  const option = {
    xAxis: {
      type: 'category',
      data: ['Not voted', '❌ (0)', '', '⭐ (2)', '', '⭐⭐ (4)', '', '⭐⭐⭐ (6)', '', '⭐⭐⭐⭐ (8)', '', '⭐⭐⭐⭐⭐ (10)'],
      axisTick: {
        alignWithLabel: true
      }
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        data: Object.values(average.value),
        type: 'bar',
        name: 'Average rating',
        label: {
          show: true,
          position: 'top',
          formatter: '{c}'
        }
      }
    ],
    title: {
      show: true,
      text: 'Number of games by rating'
    },
    legend: {
      orient: 'vertical',
      right: 0,
      top: 'bottom'
    }
  }

  option && myChart.setOption(option)
})
</script>

<template>
  <div style="width: 100%; height: 400px" ref="chart"></div>
</template>
