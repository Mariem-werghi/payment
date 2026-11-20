
const mongoose = require('mongoose');

const paymentSchema = new mongoose.Schema({

    // Référence de la commande dans le service Orders
    orderId: {
        type: String,
        required: true,
        trim: true
    },

    // Montant du paiement
    amount: {
        type: Number,
        required: true,
        min: [0.001, 'Le montant doit être positif']
    },

    // Devise
    currency: {
        type: String,
        default: 'TND',
        uppercase: true
    },

    // Mode de paiement
    mode: {
        type: String,
        required: true,
        enum: ['CARD', 'CASH_ON_DELIVERY']
    },

    // État du paiement
    status: {
        type: String,
        enum: ['PENDING', 'SUCCESS', 'FAILED', 'REFUNDED'],
        default: 'PENDING'
    },

    // Identifiant de transaction du prestataire
    transactionId: {
        type: String,
        default: null
    },

    // Motif d'échec
    failureReason: {
        type: String,
        default: null
    },

    // Date du paiement
    date: {
        type: Date,
        default: Date.now
    },

    // Date du remboursement
    refundedAt: {
        type: Date,
        default: null
    },

    // Montant remboursé
    refundedAmount: {
        type: Number,
        default: 0,
        min: 0
    }

}, {
    timestamps: true
});

// Index pour accélérer les recherches
paymentSchema.index({ orderId: 1 });
paymentSchema.index({ status: 1, mode: 1 });
paymentSchema.index({ date: -1 });

module.exports = mongoose.model('Payment', paymentSchema);
