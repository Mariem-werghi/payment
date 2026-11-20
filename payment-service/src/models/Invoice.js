
const mongoose = require('mongoose');

// Sous-entité : lignes de facture
const invoiceLineSchema = new mongoose.Schema({

    productId: {
        type: String,
        required: true
    },

    description: {
        type: String,
        required: true,
        trim: true
    },

    quantity: {
        type: Number,
        required: true,
        min: 1
    },

    unitPriceHT: {
        type: Number,
        required: true,
        min: 0
    },

    taxRate: {
        type: Number,
        required: true,
        min: 0,
        max: 100
    },

    lineTotalHT: {
        type: Number,
        required: true,
        min: 0
    }

}, { _id: false });


const invoiceSchema = new mongoose.Schema({

    // Numéro unique de facture
    invoiceNumber: {
        type: String,
        required: true,
        unique: true,
        trim: true
    },

    // Référence de la commande
    orderId: {
        type: String,
        required: true,
        unique: true
    },

    // Référence du paiement
    paymentId: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'Payment',
        required: true,
        unique: true
    },

    // Produits et services facturés
    lines: {
        type: [invoiceLineSchema],
        required: true,
        validate: {
            validator: lines => lines.length > 0,
            message: 'La facture doit contenir au moins une ligne'
        }
    },

    // Total hors taxes
    subtotalHT: {
        type: Number,
        required: true,
        min: 0
    },

    // Montant total de la TVA
    taxAmount: {
        type: Number,
        required: true,
        min: 0
    },

    // Total toutes taxes comprises
    totalTTC: {
        type: Number,
        required: true,
        min: 0
    },

    // Date de création
    createdAt: {
        type: Date,
        default: Date.now
    }

}, {
    timestamps: true
});

// Index pour les recherches
invoiceSchema.index({ createdAt: -1 });

module.exports = mongoose.model('Invoice', invoiceSchema);
