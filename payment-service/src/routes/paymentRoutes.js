
const express = require('express');

const router = express.Router();

const paymentController = require('../controllers/paymentController');

// CREATE
router.post('/', paymentController.createPayment);

// READ ALL
router.get('/', paymentController.getAllPayments);
// Simulation de confirmation
router.post('/:id/test-confirm', paymentController.confirmPayment);

// Simulation d'échec
router.post('/:id/test-fail', paymentController.failPayment);
router.post('/:id/refund', paymentController.refundPayment);
// READ ONE
router.get('/:id', paymentController.getPaymentById);

// UPDATE
router.patch('/:id', paymentController.updatePayment);

// DELETE
router.delete('/:id', paymentController.deletePayment);

module.exports = router;

