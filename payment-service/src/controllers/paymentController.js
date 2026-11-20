
const paymentService = require('../services/paymentService');

// CREATE
const createPayment = async (req, res) => {
    try {
        const payment = await paymentService.createPayment(req.body);

        res.status(201).json({
            success: true,
            message: 'Paiement créé avec succès',
            data: payment
        });

    } catch (error) {
        res.status(400).json({
            success: false,
            message: error.message
        });
    }
};


// GET ALL
const getAllPayments = async (req, res) => {
    try {
        const payments = await paymentService.getAllPayments(req.query);

        res.status(200).json({
            success: true,
            count: payments.length,
            data: payments
        });

    } catch (error) {
        res.status(500).json({
            success: false,
            message: error.message
        });
    }
};


// GET BY ID
const getPaymentById = async (req, res) => {
    try {
        const payment = await paymentService.getPaymentById(req.params.id);

        res.status(200).json({
            success: true,
            data: payment
        });

    } catch (error) {
        res.status(404).json({
            success: false,
            message: error.message
        });
    }
};


// UPDATE
const updatePayment = async (req, res) => {
    try {
        const payment = await paymentService.updatePayment(
            req.params.id,
            req.body
        );

        res.status(200).json({
            success: true,
            message: 'Paiement modifié avec succès',
            data: payment
        });

    } catch (error) {
        res.status(400).json({
            success: false,
            message: error.message
        });
    }
};


// DELETE
const deletePayment = async (req, res) => {
    try {
        const payment = await paymentService.deletePayment(req.params.id);

        res.status(200).json({
            success: true,
            message: 'Paiement supprimé avec succès',
            data: payment
        });

    } catch (error) {
        res.status(400).json({
            success: false,
            message: error.message
        });
    }
};


// CONFIRM PAYMENT (simulation de test)
const confirmPayment = async (req, res) => {
    try {

        // Simulation autorisée uniquement en développement
        if (process.env.NODE_ENV !== 'development') {
            return res.status(403).json({
                success: false,
                message: 'Fonction disponible uniquement en développement'
            });
        }

        const payment = await paymentService.confirmPayment(
            req.params.id,
            req.body.transactionId
        );

        res.status(200).json({
            success: true,
            message: 'Paiement validé avec succès',
            data: payment
        });

    } catch (error) {
        res.status(400).json({
            success: false,
            message: error.message
        });
    }
};


// FAIL PAYMENT (simulation de test)
const failPayment = async (req, res) => {
    try {

        if (process.env.NODE_ENV !== 'development') {
            return res.status(403).json({
                success: false,
                message: 'Fonction disponible uniquement en développement'
            });
        }

        const payment = await paymentService.failPayment(
            req.params.id,
            req.body.reason
        );

        res.status(200).json({
            success: true,
            message: 'Échec du paiement enregistré',
            data: payment
        });

    } catch (error) {
        res.status(400).json({
            success: false,
            message: error.message
        });
    }
};

const refundPayment = async (req, res) => {
    try {

        const payment = await paymentService.refundPayment(
            req.params.id,
            req.body.refundAmount
        );

        res.status(200).json({
            success: true,
            message: 'Remboursement enregistré avec succès',
            data: payment
        });

    } catch (error) {
        res.status(400).json({
            success: false,
            message: error.message
        });
    }
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
