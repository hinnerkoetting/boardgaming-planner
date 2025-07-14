<script setup lang="ts">
import type { GameStatistics } from '@/model/GameStatistics';
import { init } from 'echarts';
import { onMounted, useTemplateRef, type PropType } from 'vue';

const chart = useTemplateRef("chart")

const props = defineProps({
  gameStatistics: {
    type: Object as PropType<GameStatistics>,
    required: true
  }
});

onMounted(async () => {

  var myChart = init(chart.value);

  new ResizeObserver(() => myChart.resize()).observe(chart.value!);

  const allDates = Array.from({ length: 12 }).map((_, i) => {
    const d = new Date();
    d.setMonth(d.getMonth() - 11 + i);
    d.setDate(1);    
    return d;
  });
  
  const playdates: Date[] = props.gameStatistics.playDates.map(pd => new Date(pd));
  const yData = allDates.map(monthDate => 
    playdates.filter(pd => 
      pd.getFullYear() === monthDate.getFullYear() && pd.getMonth() === monthDate.getMonth()
    ).length
  );

  const option = {
    xAxis: {
      type: 'category',
      data: allDates.map(date => formatDate(date)),
      axisLabel: {
        interval: 0,        // Show all labels
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
        data: yData,
        type: 'bar',
        name: "Played",
        label: {
          show: false,
          position: 'top',
          formatter: '{c}',
        }
      }
    ]

  };

  option && myChart.setOption(option);
});

function formatDate(date: Date) {
  return date.toLocaleDateString(undefined, { year: "2-digit", month: "short" });
}

</script>

<template>  
  <div style="width: 100%;">
    Last played

    <div style="width: 100%; height: 400px;" ref="chart"></div>
  </div>

</template>
