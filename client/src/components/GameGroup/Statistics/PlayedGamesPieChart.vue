<script setup lang="ts">
import type { GameGroupStatistics } from '@/model/GameGroupStatistics'
import { init } from 'echarts'
import { onMounted, ref, useTemplateRef, type PropType, type Ref } from 'vue'

const chart = useTemplateRef('chart')

const props = defineProps({
  gameGroupStatistics: {
    type: Object as PropType<GameGroupStatistics>,
    required: true
  }
})


onMounted(async () => {
  var myChart = init(chart.value)

  const games = props.gameGroupStatistics.events
    .flatMap(event => event.games)
    .filter(game => game.gameStatus !== 'REJECTED')
    .map(game => game.game.name)


  const gameCounts: Record<string, number> = {}
  games.forEach(game => {
    gameCounts[game] = (gameCounts[game] || 0) + 1
  })

  const data = Object.keys(gameCounts).map(game => {
    return { value: gameCounts[game], name: game }
  })
  const option = {
    series: [{
      type: 'pie',
      id: 'pie',
      emphasis: {
        focus: 'self'
      },
      label: {
        formatter: '{b}: \n({d}%)'
      },
      data: data

    }
    ],
    title: {
      text: "Played games",
      left: 'center',
      top: 'top',
    },
  }

  option && myChart.setOption(option)
})


</script>

<template>
  <div style="max-width: 100%; height: 300px" ref="chart"></div>
</template>