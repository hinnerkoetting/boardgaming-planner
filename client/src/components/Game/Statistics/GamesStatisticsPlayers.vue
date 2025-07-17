<script setup lang="ts">
import type { GameStatistics } from '@/model/GameStatistics'
import { init } from 'echarts'
import { onMounted, useTemplateRef, type PropType } from 'vue'

const chart = useTemplateRef('chart')

const props = defineProps({
  gameStatistics: {
    type: Object as PropType<GameStatistics>,
    required: true
  }
})

onMounted(async () => {
  var myChart = init(chart.value)

  new ResizeObserver(() => myChart.resize()).observe(chart.value!)

  const option = {
    xAxis: {
      type: 'category',
      data: Object.entries(props.gameStatistics.numberOfTimesPlayedByPlayers).map(([key]) => key),
      axisLabel: {
        interval: 0, // Show all labels
        overflow: 'none',
        rotate: 30
      }
    },
    yAxis: {
      type: 'value',
      minInterval: 1
    },
    series: [
      {
        data: Object.entries(props.gameStatistics.numberOfTimesPlayedByPlayers).map(
          ([_, value]) => value
        ),
        type: 'bar',
        name: 'Number of times played',
        colorBy: 'data',
        label: {
          show: false,
          position: 'top',
          formatter: '{c}'
        }
      }
    ]
  }

  option && myChart.setOption(option)
})
</script>

<template>
  <div style="width: 100%">
    Played by these players:

    <div style="width: 100%; height: 400px" ref="chart"></div>
  </div>
</template>
