
const Payment = require('../models/Payment');

// 1. CREATE : créer un paiement
const createPayment = async (data) => {

    const payment = new Payment({
        orderId: data.orderId,
        amount: data.amount,
        currency: data.currency || 'TND',
        mode: data.mode,
        status: 'PENDING'
    });

    return await payment.save();
};


// 2. READ : récupérer tous les paiements
const getAllPayments = async (filters = {}) => {

    const query = {};

    if (filters.status) {
        query.status = filters.status;
    }

    if (filters.mode) {
        query.mode = filters.mode;
    }

    if (filters.orderId) {
        query.orderId = filters.orderId;
    }

    return await Payment.find(query).sort({ date: -1 });
};


// 3. READ : récupérer un paiement par ID
const getPaymentById = async (id) => {

    const payment = await Payment.findById(id);

    if (!payment) {
        throw new Error('Paiement introuvable');
    }

    return payment;
};


// 4. UPDATE : modifier un paiement en attente
const updatePayment = async (id, data) => {

    const payment = await Payment.findById(id);

    if (!payment) {
        throw new Error('Paiement introuvable');
    }

    if (payment.status !== 'PENDING') {
        throw new Error(
            'Seuls les paiements en attente peuvent être modifiés'
        );
    }

    const allowedFields = ['amount', 'mode'];

    for (const field of allowedFields) {
        if (data[field] !== undefined) {
            payment[field] = data[field];
        }
    }

    return await payment.save();
};


// 5. DELETE : supprimer un paiement en attente
const deletePayment = async (id) => {

    const payment = await Payment.findById(id);

    if (!payment) {
        throw new Error('Paiement introuvable');
    }

    if (payment.status !== 'PENDING') {
        throw new Error(
            'Impossible de supprimer un paiement déjà traité'
        );
    }

    await Payment.findByIdAndDelete(id);

    return payment;
    
};


// 6. CONFIRM : confirmer un paiement
const confirmPayment = async (id, transactionId) => {

    const payment = await Payment.findById(id);

    if (!payment) {
        throw new Error('Paiement introuvable');
    }

    // Protection contre la double confirmation
    if (payment.status === 'SUCCESS') {
        return payment;
    }

    if (payment.status !== 'PENDING') {
        throw new Error(
            'Ce paiement ne peut pas être confirmé'
        );
    }

    // Validation du paiement
    payment.status = 'SUCCESS';
    payment.transactionId = transactionId || null;
    payment.date = new Date();

    return await payment.save();
};


// 7. FAIL : enregistrer un échec de paiement
const failPayment = async (id, reason) => {

    const payment = await Payment.findById(id);

    if (!payment) {
        throw new Error('Paiement introuvable');
    }

    if (payment.status !== 'PENDING') {
        throw new Error(
            'Seul un paiement en attente peut échouer'
        );
    }

    payment.status = 'FAILED';
    payment.failureReason = reason || 'Paiement refusé';

    return await payment.save();
};


const refundPayment = async (id, refundAmount) => {

    // 1. Rechercher le paiement
    const payment = await Payment.findById(id);

    if (!payment) {
        throw new Error('Paiement introuvable');
    }

    // 2. Vérifier le statut
    if (!['SUCCESS', 'PARTIALLY_REFUNDED'].includes(payment.status)) {
        throw new Error(
            'Ce paiement ne peut pas être remboursé'
        );
    }

    // 3. Vérifier le montant
    const amount = Number(refundAmount);

    if (!Number.isFinite(amount) || amount <= 0) {
        throw new Error('Montant de remboursement invalide');
    }

    // 4. Calculer le montant restant
    const remainingAmount =
        payment.amount - payment.refundedAmount;

    if (amount > remainingAmount) {
        throw new Error(
            'Le montant dépasse le montant remboursable'
        );
    }

    // 5. Mettre à jour le montant remboursé
    payment.refundedAmount =
        Math.round((payment.refundedAmount + amount) * 100) / 100;

    payment.refundedAt = new Date();

    // 6. Déterminer le statut
    if (payment.refundedAmount === payment.amount) {
        payment.status = 'REFUNDED';
    } else {
        payment.status = 'PARTIALLY_REFUNDED';
    }

    // 7. Enregistrer
    return await payment.save();
};


module.exports = {
    createPayment,
    getAllPayments,
    getPaymentById,
    updatePayment,
    deletePayment,
    confirmPayment,
    failPayment,
    refundPayment
};