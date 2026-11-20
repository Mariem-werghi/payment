
const Payment = require('../models/Payment');
const Invoice = require('../models/Invoice');

function getDateFilter(period) {
    const now = new Date();
    const start = new Date(now);

    if (period === 'today') {
        start.setHours(0, 0, 0, 0);
    } else if (period === 'week') {
        const day = start.getDay();
        const difference = day === 0 ? 6 : day - 1;
        start.setDate(start.getDate() - difference);
        start.setHours(0, 0, 0, 0);
    } else if (period === 'month') {
        start.setDate(1);
        start.setHours(0, 0, 0, 0);
    } else {
        return null;
    }

    return { $gte: start, $lte: now };
}

async function getDashboardStats(period = 'all') {
    const dateFilter = getDateFilter(period);

    const paymentQuery = dateFilter
        ? { date: dateFilter }
        : {};

    const invoiceQuery = dateFilter
        ? { createdAt: dateFilter }
        : {};

    const [payments, totalInvoices] = await Promise.all([
        Payment.find(paymentQuery).sort({ date: -1 }),
        Invoice.countDocuments(invoiceQuery)
    ]);

    const successfulStatuses = [
        'SUCCESS',
        'PARTIALLY_REFUNDED',
        'REFUNDED'
    ];

    const successful = payments.filter(payment =>
        successfulStatuses.includes(payment.status)
    );

    const failed = payments.filter(payment =>
        payment.status === 'FAILED'
    );

    // Montant net après déduction des remboursements
    const totalCollected = successful.reduce((total, payment) => {
        const amount = Number(payment.amount) || 0;
        const refunded = Number(payment.refundedAmount) || 0;

        return total + Math.max(0, amount - refunded);
    }, 0);

    // Répartition des paiements par mode
    const methods = {};

    successful.forEach(payment => {
        const mode = payment.mode || 'UNKNOWN';
        methods[mode] = (methods[mode] || 0) + 1;
    });

    const paymentMethods = Object.entries(methods).map(
        ([mode, count]) => ({ mode, count })
    );

    // Regroupement des revenus par jour
    const revenueMap = {};

    successful.forEach(payment => {
        const paymentDate = payment.date || payment.createdAt;

        if (!paymentDate) return;

        const key = new Date(paymentDate).toISOString().slice(0, 10);

        const amount = Math.max(
            0,
            (Number(payment.amount) || 0) -
            (Number(payment.refundedAmount) || 0)
        );

        revenueMap[key] = (revenueMap[key] || 0) + amount;
    });

    const revenue = Object.entries(revenueMap)
        .sort(([dateA], [dateB]) => dateA.localeCompare(dateB))
        .map(([date, amount]) => ({
            label: new Date(date + 'T00:00:00').toLocaleDateString('fr-FR'),
            amount: Number(amount.toFixed(2))
        }));

    // Dernières transactions
    const recentTransactions = payments.slice(0, 10).map(payment => ({
        orderId: payment.orderId,
        amount: payment.amount,
        mode: payment.mode,
        status: payment.status,
        date: payment.date
    }));

    return {
        totalCollected: Number(totalCollected.toFixed(2)),
        successfulPayments: successful.length,
        failedPayments: failed.length,
        totalInvoices,
        paymentMethods,
        revenue,
        recentTransactions
    };
}

module.exports = { getDashboardStats };
