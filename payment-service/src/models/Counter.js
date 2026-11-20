
const mongoose = require('mongoose');

const counterSchema = new mongoose.Schema({

    // Identifiant du compteur
    _id: {
        type: String,
        required: true
    },

    // Dernier numéro utilisé
    sequence: {
        type: Number,
        default: 0
    }

});

module.exports = mongoose.model('Counter', counterSchema);
