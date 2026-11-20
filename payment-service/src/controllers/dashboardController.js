const dashboardService = require('../services/dashboardService');

const getDashboard = async (req, res) => {
    try {
        const allowedPeriods = ['all', 'today', 'week', 'month'];
        const period = req.query.period || 'all';

        if (!allowedPeriods.includes(period)) {
            return res.status(400).json({
                success: false,
                message: 'Période invalide'
            });
        }

        const data = await dashboardService.getDashboardStats(period);

        res.status(200).json({
            success: true,
            message: 'Statistiques financières récupérées',
            data
        });

    } catch (error) {
        console.error('Dashboard error:', error);

        res.status(500).json({
            success: false,
            message: error.message
        });
    }
};

module.exports = { getDashboard };