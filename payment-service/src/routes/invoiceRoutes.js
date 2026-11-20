
const express = require('express');

const router = express.Router();

const invoiceController = require('../controllers/invoiceController');

// CREATE
router.post('/', invoiceController.createInvoice);

// READ ALL
router.get('/', invoiceController.getAllInvoices);

// READ BY NUMBER
router.get('/number/:number', invoiceController.getInvoiceByNumber);

// READ BY ORDER
router.get('/order/:orderId', invoiceController.getInvoicesByOrder);
router.get('/:id/pdf', invoiceController.downloadInvoicePDF);
// READ BY ID
router.get('/:id', invoiceController.getInvoiceById);

module.exports = router;
