
const API_URL = 'http://localhost:3004/api/dashboard';

let revenueChart = null;
let paymentChart = null;

// Charger les données du dashboard
async function loadDashboard(period = 'all') {
    const table = document.getElementById('transactionsTable');

    try {
        table.innerHTML = `
            <tr><td colspan="5">Chargement...</td></tr>
        `;

        const response = await fetch(
            `${API_URL}?period=${period}`
        );

        const result = await response.json();

        if (!response.ok || !result.success) {
            throw new Error(result.message || 'Erreur API');
        }

        const data = result.data;

        // Cartes financières
        document.getElementById('totalCollected').textContent =
            `${Number(data.totalCollected).toFixed(2)} TND`;

        document.getElementById('successfulPayments').textContent =
            data.successfulPayments;

        document.getElementById('failedPayments').textContent =
            data.failedPayments;

        document.getElementById('totalInvoices').textContent =
            data.totalInvoices;

        // Graphiques
        renderRevenueChart(data.revenue);
        renderPaymentChart(data.paymentMethods);

        // Transactions
        renderTransactions(data.recentTransactions);

    } catch (error) {
        console.error('Erreur dashboard :', error);

        table.innerHTML = `
            <tr>
                <td colspan="5">
                    Impossible de charger les données.
                    Vérifiez que le backend fonctionne.
                </td>
            </tr>
        `;
    }
}

// Graphique des revenus
function renderRevenueChart(revenue) {
    const ctx = document.getElementById('revenueChart');

    if (revenueChart) {
        revenueChart.destroy();
    }

    revenueChart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: revenue.map(item => item.label),
            datasets: [{
                label: 'Revenus (TND)',
                data: revenue.map(item => item.amount),
                borderColor: '#3b82f6',
                backgroundColor: 'rgba(59, 130, 246, 0.15)',
                fill: true,
                tension: 0.4,
                pointRadius: 4
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    labels: { color: '#cbd5e1' }
                }
            },
            scales: {
                x: {
                    ticks: { color: '#94a3b8' },
                    grid: { color: '#263349' }
                },
                y: {
                    beginAtZero: true,
                    ticks: { color: '#94a3b8' },
                    grid: { color: '#263349' }
                }
            }
        }
    });
}

// Graphique des moyens de paiement
function renderPaymentChart(methods) {
    const ctx = document.getElementById('paymentChart');

    if (paymentChart) {
        paymentChart.destroy();
    }

    paymentChart = new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: methods.map(item => item.mode),
            datasets: [{
                data: methods.map(item => item.count),
                backgroundColor: ['#3b82f6', '#10b981', '#8b5cf6'],
                borderWidth: 0
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    position: 'bottom',
                    labels: {
                        color: '#cbd5e1',
                        padding: 18
                    }
                }
            }
        }
    });
}

// Tableau des dernières transactions
function renderTransactions(transactions) {
    const table = document.getElementById('transactionsTable');

    if (!transactions || transactions.length === 0) {
        table.innerHTML = `
            <tr><td colspan="5">Aucune transaction trouvée.</td></tr>
        `;
        return;
    }

    table.innerHTML = transactions.map(payment => {
        const date = payment.date
            ? new Date(payment.date).toLocaleDateString('fr-FR')
            : '-';

        return `
            <tr>
                <td>${escapeHTML(payment.orderId)}</td>
                <td>${Number(payment.amount).toFixed(2)} TND</td>
                <td>${escapeHTML(payment.mode)}</td>
                <td>
                    <span class="status ${escapeHTML(payment.status)}">
                        ${escapeHTML(payment.status)}
                    </span>
                </td>
                <td>${date}</td>
            </tr>
        `;
    }).join('');
}

// Éviter d'insérer du texte non fiable directement dans le HTML
function escapeHTML(value) {
    return String(value ?? '').replace(/[&<>"']/g, character => ({
        '&': '&amp;',
        '<': '&lt;',
        '>': '&gt;',
        '"': '&quot;',
        "'": '&#039;'
    })[character]);
}

// Filtre par période
document.getElementById('periodFilter').addEventListener(
    'change',
    event => loadDashboard(event.target.value)
);

// Premier chargement
loadDashboard();
