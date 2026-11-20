const nodemailer = require('nodemailer');

const transporter = nodemailer.createTransport({
    service: 'gmail',
    auth: {
        user: process.env.EMAIL_USER,
        pass: process.env.EMAIL_PASSWORD
    }
});

const sendInvoiceEmail = async (email, invoice, pdfPath) => {
    const mailOptions = {
        from: process.env.EMAIL_USER,
        to: email,
        subject: `Votre facture ${invoice.invoiceNumber}`,
        text: `Bonjour,

Veuillez trouver ci-joint votre facture ${invoice.invoiceNumber}.

Montant total : ${invoice.totalTTC} TND.

Merci pour votre confiance.`,
        attachments: [
            {
                filename: `${invoice.invoiceNumber}.pdf`,
                path: pdfPath
            }
        ]
    };

    return await transporter.sendMail(mailOptions);
};

module.exports = { sendInvoiceEmail };