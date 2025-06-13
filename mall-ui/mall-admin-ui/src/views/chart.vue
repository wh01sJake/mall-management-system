<script setup>
    import * as echarts from 'echarts'
    import {onMounted} from "vue";
    import {ref} from "vue";
    import chartApi from "@/api/chart.js";

    const chartRef = ref()

    onMounted(() => {
        //echarts.init(document.getElementById('myChart'));
        const myChart = echarts.init(chartRef.value);
        chartApi.selectClassCount().then((result) => {
            if (result.code == 0) {
                const option = {
                    title: {
                        text: 'Referer of a Website',
                        subtext: 'Fake Data',
                        left: 'center'
                    },
                    tooltip: {
                        trigger: 'item'
                    },
                    legend: {
                        orient: 'vertical',
                        left: 'left'
                    },
                    series: [
                        {
                            name: 'Access From',
                            type: 'pie',
                            radius: '50%',
                            data: result.data,
                            emphasis: {
                                itemStyle: {
                                    shadowBlur: 10,
                                    shadowOffsetX: 0,
                                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                                }
                            }
                        }
                    ]
                };

                // 使用刚指定的配置项和数据显示图表。
                myChart.setOption(option);
            }
        })

        window.onresize = function () {
            myChart.resize();
        };



    })

</script>

<template>
    <div ref="chartRef" style="width: 100%;height: 100%"></div>
</template>

<style scoped>

</style>