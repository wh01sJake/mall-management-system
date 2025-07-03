import service from '@/utils/request.js'

// Chart and statistics API endpoints
const chartApi = {
    // Get category count for pie chart
    selectClassCount() {
        return service.get('/admin/chart/categoryCount')
    },
    
    // Get order statistics for charts
    getOrderStats() {
        return service.get('/admin/chart/orderStats')
    },
    
    // Get revenue trends
    getRevenueTrends(period = '7d') {
        return service.get('/admin/chart/revenue', {
            params: { period: period }
        })
    },
    
    // Get order status distribution
    getOrderStatusDistribution() {
        return service.get('/admin/chart/orderStatus')
    },
    
    // Get payment method distribution
    getPaymentMethodStats() {
        return service.get('/admin/chart/paymentMethods')
    },
    
    // Get top selling products
    getTopProducts(limit = 10) {
        return service.get('/admin/chart/topProducts', {
            params: { limit: limit }
        })
    },
    
    // Get customer registration trends
    getCustomerTrends(period = '30d') {
        return service.get('/admin/chart/customerTrends', {
            params: { period: period }
        })
    },
    
    // Get sales by category
    getSalesByCategory() {
        return service.get('/admin/chart/salesByCategory')
    },
    
    // Get monthly sales comparison
    getMonthlySales(year) {
        return service.get('/admin/chart/monthlySales', {
            params: { year: year }
        })
    },
    
    // Get dashboard summary stats
    getDashboardStats() {
        return service.get('/admin/chart/dashboard')
    }
}

export default chartApi
