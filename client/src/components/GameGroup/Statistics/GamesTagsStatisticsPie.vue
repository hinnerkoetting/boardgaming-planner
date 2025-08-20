<script setup lang="ts">
import { init } from 'echarts'
import { onMounted, ref, useTemplateRef, type PropType, type Ref } from 'vue'

const chart = useTemplateRef('chart')

const props = defineProps({
  tagAggregation: {
    type: Object as PropType<TagAggregation>,
    required: true
  }
})

class TagAggregation {
  constructor(public tagId: number, public name: string, public gamesWithTag: number, public gamesWithoutTag: number) {
  }
}



onMounted(async () => {

  createPieChart(props.tagAggregation)
})

function createPieChart(tag: TagAggregation) {
  var myChart = init(chart.value)

  const name = tag.name
  const first = tag.gamesWithTag
  const second = tag.gamesWithoutTag
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
      data: [
        { value: second, name: `Without` },
        { value: first, name: `With` },
      ]

    }
    ],
    title: {
      text: name,
      left: 'center',
      top: 'center',
    },
  }

  option && myChart.setOption(option)
}
</script>

<template>
  <div style="width: 400px; max-width: 100%; height: 300px" ref="chart"></div>
</template>