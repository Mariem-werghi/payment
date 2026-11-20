
const invoiceService = require('../services/invoiceService');
const { sendInvoiceEmail } = require('../services/emailService');
const { generateInvoicePDF } = require('../services/pdfService');
const path = require('path');
const fs = require('fs');
// CREATE
const createInvoice = async (req, res) => {
    try {
        const invoice = await invoiceService.createInvoice(req.body);
const pdfPath = generateInvoicePDF(invoice);
let emailSent = false;

if (req.body.email) {
    try {
        await sendInvoiceEmail(req.body.email, invoice, pdfPath);
        emailSent = true;
    } catch (emailError) {
        console.error('Erreur envoi email :', emailError.message);
    }
}
        res.status(201).json({
            success: true,
            message: 'Facture créée avec succès',
            data: invoice,
            emailSent: emailSent
        });

    } catch (error) {
        res.status(400).json({
            success: false,
            message: error.message
        });
    }
};


// GET ALL
const getAllInvoices = async (req, res) => {
    try {
        const invoices = await invoiceService.getAllInvoices();

        res.status(200).json({
            success: true,
            count: invoices.length,
            data: invoices
        });

    } catch (error) {
        res.status(500).json({
            success: false,
            message: error.message
        });
    }
};


// GET BY ID
const getInvoiceById = async (req, res) => {
    try {
        const invoice = await invoiceService.getInvoiceById(
            req.params.id
        );

        res.status(200).json({
            success: true,
            data: invoice
        });

    } catch (error) {
        res.status(404).json({
            success: false,
            message: error.message
        });
    }
};


// GET BY NUMBER
const getInvoiceByNumber = async (req, res) => {
    try {
        const invoice = await invoiceService.getInvoiceByNumber(
            req.params.number
        );

        res.status(200).json({
            success: true,
            data: invoice
        });

    } catch (error) {
        res.status(404).json({
            success: false,
            message: error.message
        });
    }
};


// GET BY ORDER
const getInvoicesByOrder = async (req, res) => {
    try {
        const invoices = await invoiceService.getInvoicesByOrder(
            req.params.orderId
        );

        res.status(200).json({
            success: true,
            count: invoices.length,
            data: invoices
        });

    } catch (error) {
        res.status(500).json({
            success: false,
            message: error.message
        });
    }
};

const downloadInvoicePDF = async (req, res) => {
    try {
        const invoice = await invoiceService.getInvoiceById(req.params.id);

        if (!invoice) {
            return res.status(404).json({
                success: false,
                message: 'Facture introuvable'
            });
        }

        const filePath = path.join(
            __dirname,
            '../../invoices',
            `${invoice.number}.pdf`
        );

        if (!fs.existsSync(filePath)) {
            return res.status(404).json({
                success: false,
                message: 'Fichier PDF introuvable'
            });
        }

        res.download(filePath);
    } catch (error) {
        res.status(500).json({
            success: false,
            message: error.message
        });
    }
};
module.exports = {
    createInvoice,
    getAllInvoices,
    getInvoiceById,
    getInvoiceByNumber,
    getInvoicesByOrder,
        downloadInvoicePDF
};
