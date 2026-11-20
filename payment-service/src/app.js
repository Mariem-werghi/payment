
const express = require('express');
const cors = require('cors');

const app = express();
const dashboardRoutes = require('./routes/dashboardRoutes');
// Middlewares
app.use(cors());
app.use(express.json());
app.use('/api/dashboard', dashboardRoutes);
// Import des routes
const paymentRoutes = require('./routes/paymentRoutes');
// Routes générales
app.get('/', (req, res) => {
    res.status(200).json({
        success: true,
        message: 'ShopSphere Payment Service is running'
    });
});

app.get('/health', (req, res) => {
    res.status(200).json({
        success: true,
        service: 'Payment Service',
        status: 'UP'
    });
});

// Routes Payment
app.use('/api/payments', paymentRoutes);
const invoiceRoutes = require('./routes/invoiceRoutes');
app.use('/api/invoices', invoiceRoutes);
module.exports = app;