
const Invoice = require('../models/Invoice');
const Payment = require('../models/Payment');
const Counter = require('../models/Counter');

// Fonction pour arrondir à deux décimales
const roundMoney = (value) => {
    return Math.round((value + Number.EPSILON) * 100) / 100;
};

// 1. CREATE : créer une facture
const createInvoice = async (data) => {

    // Vérifier le paiement
    const payment = await Payment.findById(data.paymentId);

    if (!payment) {
        throw new Error('Paiement introuvable');
    }

    // Vérifier que le paiement est validé
    if (payment.status !== 'SUCCESS') {
        throw new Error(
            'Impossible de créer une facture : paiement non validé'
        );
    }

    // Vérifier la correspondance avec la commande
    if (payment.orderId !== data.orderId) {
        throw new Error('La commande ne correspond pas au paiement');
    }

    // Empêcher plusieurs factures pour le même paiement
    const existingInvoice = await Invoice.findOne({
        paymentId: data.paymentId
    });

    if (existingInvoice) {
        throw new Error('Une facture existe déjà pour ce paiement');
    }

    // Vérifier les lignes de facture
    if (!Array.isArray(data.lines) || data.lines.length === 0) {
        throw new Error('La facture doit contenir au moins une ligne');
    }

    // Calculer les lignes et les totaux côté serveur
    const lines = data.lines.map(line => {

        const quantity = Number(line.quantity);
        const unitPriceHT = Number(line.unitPriceHT);
        const taxRate = Number(line.taxRate);

        if (
            !Number.isFinite(quantity) ||
            !Number.isFinite(unitPriceHT) ||
            !Number.isFinite(taxRate) ||
            quantity < 1 ||
            unitPriceHT < 0 ||
            taxRate < 0 ||
            taxRate > 100
        ) {
            throw new Error('Données de ligne invalides');
        }

        const lineTotalHT = roundMoney(quantity * unitPriceHT);

        return {
            productId: line.productId,
            description: line.description,
            quantity,
            unitPriceHT,
            taxRate,
            lineTotalHT
        };
    });

    const subtotalHT = roundMoney(
        lines.reduce((sum, line) => sum + line.lineTotalHT, 0)
    );

    const taxAmount = roundMoney(
        lines.reduce(
            (sum, line) =>
                sum + line.lineTotalHT * line.taxRate / 100,
            0
        )
    );

    const totalTTC = roundMoney(subtotalHT + taxAmount);

    // Vérifier que le montant correspond au paiement
    if (totalTTC !== roundMoney(payment.amount)) {
        throw new Error(
            'Le total TTC ne correspond pas au montant du paiement'
        );
    }

    // Générer le numéro séquentiel de facture
    const year = new Date().getFullYear();

    const counter = await Counter.findOneAndUpdate(
        { _id: `invoice_${year}` },
        { $inc: { sequence: 1 } },
        {
            new: true,
            upsert: true,
            setDefaultsOnInsert: true
        }
    );

    const invoiceNumber =
        `FAC-${year}-${String(counter.sequence).padStart(4, '0')}`;

    // Enregistrer la facture
    const invoice = new Invoice({
        invoiceNumber,
        orderId: data.orderId,
        paymentId: payment._id,
        lines,
        subtotalHT,
        taxAmount,
        totalTTC
    });

    return await invoice.save();
};


// 2. READ ALL : afficher toutes les factures
const getAllInvoices = async () => {

    return await Invoice.find()
        .populate('paymentId')
        .sort({ createdAt: -1 });
};


// 3. READ BY ID
const getInvoiceById = async (id) => {

    const invoice = await Invoice.findById(id)
        .populate('paymentId');

    if (!invoice) {
        throw new Error('Facture introuvable');
    }

    return invoice;
};


// 4. READ BY NUMBER
const getInvoiceByNumber = async (number) => {

    const invoice = await Invoice.findOne({
        invoiceNumber: number
    }).populate('paymentId');

    if (!invoice) {
        throw new Error('Facture introuvable');
    }

    return invoice;
};


// 5. READ BY ORDER
const getInvoicesByOrder = async (orderId) => {

    return await Invoice.find({ orderId })
        .populate('paymentId');
};


module.exports = {
    createInvoice,
    getAllInvoices,
    getInvoiceById,
    getInvoiceByNumber,
    getInvoicesByOrder
};
