const PDFDocument = require('pdfkit');
const fs = require('fs');
const path = require('path');

const generateInvoicePDF = (invoice) => {
    const invoicesDir = path.join(__dirname, '../../invoices');

    if (!fs.existsSync(invoicesDir)) {
        fs.mkdirSync(invoicesDir, { recursive: true });
    }

    const filePath = path.join(
        invoicesDir,
        `${invoice.number}.pdf`
    );

    const doc = new PDFDocument();

    doc.pipe(fs.createWriteStream(filePath));

    doc.fontSize(22).text('FACTURE', { align: 'center' });
    doc.moveDown();

    doc.fontSize(14).text(`Numero : ${invoice.number}`);
    doc.text(`Commande : ${invoice.orderId}`);
    doc.text(`Date : ${new Date(invoice.createdAt).toLocaleDateString('fr-FR')}`);

    doc.moveDown();
    doc.text('DETAILS DE LA FACTURE', { underline: true });
    doc.moveDown();

    invoice.lines.forEach((line) => {
        doc.fontSize(12).text(
            `${line.description} - Quantite : ${line.quantity} - Total HT : ${line.lineTotalHT} TND`
        );
    });

    doc.moveDown();
    doc.text(`Total HT : ${invoice.subtotalHT} TND`);
    doc.text(`TVA : ${invoice.taxAmount} TND`);
    doc.fontSize(16).text(`Total TTC : ${invoice.totalTTC} TND`);

    doc.end();

    return filePath;
};

module.exports = { generateInvoicePDF };