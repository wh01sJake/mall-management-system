<script setup>
import * as echarts from 'echarts'
import {onMounted, ref, reactive} from "vue";
import chartApi from "@/api/chart.js";
import orderApi from "@/api/order.js";

const orderStatusChartRef = ref()
const revenueChartRef = ref()
const paymentMethodChartRef = ref()
const dailyOrdersChartRef = ref()

const stats = reactive({
    totalOrders: 0,
    totalRevenue: 0,
    pendingOrders: 0,
    completedOrders: 0
})

const loading = ref(true)

// Initialize all charts
onMounted(() => {
    initOrderStatusChart()
    initRevenueChart()
    initPaymentMethodChart()
    initDailyOrdersChart()
    loadDashboardStats()
    
    // Handle window resize
    window.addEventListener('resize', handleResize)
})

const handleResize = () => {
    const charts = [orderStatusChartRef, revenueChartRef, paymentMethodChartRef, dailyOrdersChartRef]
    charts.forEach(chartRef => {
        if (chartRef.value && chartRef.value.chart) {
            chartRef.value.chart.resize()
        }
    })
}

// Load dashboard statistics
const loadDashboardStats = () => {
    chartApi.getDashboardStats().then(result => {
        if (result.code === 0) {
            Object.assign(stats, result.data)
        }
        loading.value = false
    }).catch(() => {
        loading.value = false
    })
}

// Order Status Distribution Chart
const initOrderStatusChart = () => {
    const chart = echarts.init(orderStatusChartRef.value)
    orderStatusChartRef.value.chart = chart
    
    chartApi.getOrderStatusDistribution().then(result => {
        if (result.code === 0) {
            const option = {
                title: {
                    text: 'Order Status Distribution',
                    left: 'center',
                    textStyle: {
                        fontSize: 16,
                        fontWeight: 'bold'
                    }
                },
                tooltip: {
                    trigger: 'item',
                    formatter: '{a} <br/>{b}: {c} ({d}%)'
                },
                legend: {
                    orient: 'vertical',
                    left: 'left',
                    top: 'middle'
                },
                series: [{
                    name: 'Order Status',
                    type: 'pie',
                    radius: ['40%', '70%'],
                    center: ['60%', '50%'],
                    data: result.data,
                    emphasis: {
                        itemStyle: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        }
                    },
                    itemStyle: {
                        borderRadius: 8,
                        borderColor: '#fff',
                        borderWidth: 2
                    }
                }]
            }
            chart.setOption(option)
        } else {
            console.error('Failed to load order status data:', result.msg)
        }
    }).catch(error => {
        console.error('Error loading order status chart:', error)
    })
}

// Revenue Trends Chart
const initRevenueChart = () => {
    const chart = echarts.init(revenueChartRef.value)
    revenueChartRef.value.chart = chart
    
    chartApi.getRevenueTrends('30d').then(result => {
        if (result.code === 0) {
            const option = {
                title: {
                    text: 'Revenue Trends (Last 30 Days)',
                    left: 'center',
                    textStyle: {
                        fontSize: 16,
                        fontWeight: 'bold'
                    }
                },
                tooltip: {
                    trigger: 'axis',
                    formatter: function(params) {
                        return `${params[0].name}<br/>Revenue: €${params[0].value.toFixed(2)}`
                    }
                },
                xAxis: {
                    type: 'category',
                    data: result.data.dates,
                    axisLabel: {
                        rotate: 45
                    }
                },
                yAxis: {
                    type: 'value',
                    axisLabel: {
                        formatter: '€{value}'
                    }
                },
                series: [{
                    name: 'Revenue',
                    data: result.data.revenues,
                    type: 'line',
                    smooth: true,
                    areaStyle: {
                        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                            { offset: 0, color: 'rgba(24, 144, 255, 0.6)' },
                            { offset: 1, color: 'rgba(24, 144, 255, 0.1)' }
                        ])
                    },
                    lineStyle: {
                        color: '#1890ff',
                        width: 3
                    },
                    itemStyle: {
                        color: '#1890ff'
                    }
                }]
            }
            chart.setOption(option)
        }
    })
}

// Payment Method Distribution Chart
const initPaymentMethodChart = () => {
    const chart = echarts.init(paymentMethodChartRef.value)
    paymentMethodChartRef.value.chart = chart
    
    chartApi.getPaymentMethodStats().then(result => {
        if (result.code === 0) {
            const option = {
                title: {
                    text: 'Payment Methods',
                    left: 'center',
                    textStyle: {
                        fontSize: 16,
                        fontWeight: 'bold'
                    }
                },
                tooltip: {
                    trigger: 'item'
                },
                series: [{
                    name: 'Payment Method',
                    type: 'pie',
                    radius: '60%',
                    data: result.data,
                    emphasis: {
                        itemStyle: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        }
                    },
                    label: {
                        show: true,
                        formatter: '{b}: {d}%'
                    }
                }]
            }
            chart.setOption(option)
        }
    })
}

// Daily Orders Chart
const initDailyOrdersChart = () => {
    const chart = echarts.init(dailyOrdersChartRef.value)
    dailyOrdersChartRef.value.chart = chart
    
    orderApi.getOrderCountByDateRange(
        new Date(Date.now() - 30 * 24 * 60 * 60 * 1000).toISOString().split('T')[0],
        new Date().toISOString().split('T')[0]
    ).then(result => {
        if (result.code === 0) {
            const option = {
                title: {
                    text: 'Daily Orders (Last 30 Days)',
                    left: 'center',
                    textStyle: {
                        fontSize: 16,
                        fontWeight: 'bold'
                    }
                },
                tooltip: {
                    trigger: 'axis'
                },
                xAxis: {
                    type: 'category',
                    data: result.data.dates,
                    axisLabel: {
                        rotate: 45
                    }
                },
                yAxis: {
                    type: 'value',
                    name: 'Orders'
                },
                series: [{
                    name: 'Orders',
                    data: result.data.counts,
                    type: 'bar',
                    itemStyle: {
                        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                            { offset: 0, color: '#52c41a' },
                            { offset: 1, color: '#389e0d' }
                        ])
                    },
                    barWidth: '60%'
                }]
            }
            chart.setOption(option)
        }
    })
}
</script>

<template>
    <div class="order-charts">
        <!-- Statistics Cards -->
        <el-row :gutter="20" class="stats-cards">
            <el-col :xs="12" :sm="6">
                <el-card class="stat-card">
                    <div class="stat-content">
                        <div class="stat-value">{{ stats.totalOrders }}</div>
                        <div class="stat-label">Total Orders</div>
                    </div>
                </el-card>
            </el-col>
            <el-col :xs="12" :sm="6">
                <el-card class="stat-card">
                    <div class="stat-content">
                        <div class="stat-value">€{{ stats.totalRevenue.toFixed(2) }}</div>
                        <div class="stat-label">Total Revenue</div>
                    </div>
                </el-card>
            </el-col>
            <el-col :xs="12" :sm="6">
                <el-card class="stat-card">
                    <div class="stat-content">
                        <div class="stat-value">{{ stats.pendingOrders }}</div>
                        <div class="stat-label">Pending Orders</div>
                    </div>
                </el-card>
            </el-col>
            <el-col :xs="12" :sm="6">
                <el-card class="stat-card">
                    <div class="stat-content">
                        <div class="stat-value">{{ stats.completedOrders }}</div>
                        <div class="stat-label">Completed Orders</div>
                    </div>
                </el-card>
            </el-col>
        </el-row>

        <!-- Charts Grid -->
        <el-row :gutter="20" class="charts-grid">
            <el-col :xs="24" :lg="12">
                <el-card class="chart-card">
                    <div ref="orderStatusChartRef" class="chart"></div>
                </el-card>
            </el-col>
            <el-col :xs="24" :lg="12">
                <el-card class="chart-card">
                    <div ref="paymentMethodChartRef" class="chart"></div>
                </el-card>
            </el-col>
        </el-row>

        <el-row :gutter="20" class="charts-grid">
            <el-col :xs="24" :lg="12">
                <el-card class="chart-card">
                    <div ref="revenueChartRef" class="chart"></div>
                </el-card>
            </el-col>
            <el-col :xs="24" :lg="12">
                <el-card class="chart-card">
                    <div ref="dailyOrdersChartRef" class="chart"></div>
                </el-card>
            </el-col>
        </el-row>
    </div>
</template>

<style scoped>
.order-charts {
    padding: 20px;
}

.stats-cards {
    margin-bottom: 20px;
}

.stat-card {
    text-align: center;
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.stat-content {
    padding: 10px;
}

.stat-value {
    font-size: 24px;
    font-weight: bold;
    color: #1890ff;
    margin-bottom: 8px;
}

.stat-label {
    font-size: 14px;
    color: #666;
}

.charts-grid {
    margin-bottom: 20px;
}

.chart-card {
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.chart {
    width: 100%;
    height: 400px;
}

/* Responsive design */
@media (max-width: 768px) {
    .order-charts {
        padding: 10px;
    }
    
    .chart {
        height: 300px;
    }
    
    .stat-value {
        font-size: 20px;
    }
    
    .charts-grid {
        margin-bottom: 15px;
    }
}

@media (max-width: 480px) {
    .chart {
        height: 250px;
    }
    
    .stat-value {
        font-size: 18px;
    }
}
</style>
