package com.example.java.services;

import com.example.java.entities.Ordre;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * Envoie un email de confirmation de commande avec :
 *  - le PDF de facture en pièce jointe
 *  - le QR code du prochain code promo intégré dans le corps HTML (inline)
 *
 * Utilise Gmail SMTP avec App Password.
 */
public class EmailService {

    // ─── Credentials Gmail ───────────────────────────────────────────────────────
    private static final String FROM_EMAIL   = "mariemwerghi20@gmail.com";
    private static final String APP_PASSWORD = "gdkokruoibwlsmqh";
    private static final String FROM_NAME    = "ARTERA Marketplace";

    /**
     * Envoie l'email de confirmation avec PDF et QR code promo.
     *
     * @param toEmail       adresse de l'acheteur
     * @param ordre         l'objet Ordre créé
     * @param pdfFile       facture PDF (peut être null)
     * @param usedPromoCode code promo utilisé sur cette commande (null si aucun)
     * @param nextPromoCode nouveau code promo pour la prochaine commande (null si non généré)
     * @param qrFile        image PNG du QR code (null si non disponible)
     */
    public void sendOrderConfirmation(String toEmail,
                                       Ordre ordre,
                                       File pdfFile,
                                       String usedPromoCode,
                                       String nextPromoCode,
                                       File qrFile) throws MessagingException, IOException {

        // ── Configuration SMTP Gmail ──────────────────────────────────────────────
        Properties props = new Properties();
        props.put("mail.smtp.host",            "smtp.gmail.com");
        props.put("mail.smtp.port",            "587");
        props.put("mail.smtp.auth",            "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.trust",       "smtp.gmail.com");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, APP_PASSWORD);
            }
        });

        // ── Construction du message ───────────────────────────────────────────────
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(FROM_EMAIL, FROM_NAME));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
        message.setSubject("✅ Votre commande ARTERA #" + ordre.getId() + " est confirmée !");

        // Structure : multipart/mixed
        //   ├── multipart/related  (HTML + QR inline)
        //   │     ├── text/html
        //   │     └── image/png  (QR code, cid: artera_qrcode)
        //   └── application/pdf  (facture en pièce jointe)

        boolean hasQR = (qrFile != null && qrFile.exists());
        String  html  = buildHtmlBody(ordre, toEmail, usedPromoCode, nextPromoCode, hasQR);

        MimeMultipart outerMixed = new MimeMultipart("mixed");

        if (hasQR) {
            MimeMultipart related = new MimeMultipart("related");

            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(html, "text/html; charset=UTF-8");
            related.addBodyPart(htmlPart);

            MimeBodyPart qrPart = new MimeBodyPart();
            qrPart.setDataHandler(new DataHandler(new FileDataSource(qrFile)));
            qrPart.setHeader("Content-ID", "<artera_qrcode>");
            qrPart.setHeader("Content-Type", "image/png");
            qrPart.setDisposition(MimeBodyPart.INLINE);
            related.addBodyPart(qrPart);

            MimeBodyPart relatedWrapper = new MimeBodyPart();
            relatedWrapper.setContent(related);
            outerMixed.addBodyPart(relatedWrapper);
        } else {
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(html, "text/html; charset=UTF-8");
            outerMixed.addBodyPart(htmlPart);
        }

        if (pdfFile != null && pdfFile.exists()) {
            MimeBodyPart pdfPart = new MimeBodyPart();
            pdfPart.attachFile(pdfFile);
            pdfPart.setFileName("ARTERA-Commande-" + ordre.getId() + ".pdf");
            outerMixed.addBodyPart(pdfPart);
        }

        message.setContent(outerMixed);
        Transport.send(message);
        System.out.println("[EmailService] Email envoyé à : " + toEmail);
    }

    /** Surcharge rétro-compatible (sans QR, sans nextPromoCode). */
    public void sendOrderConfirmation(String toEmail, Ordre ordre,
                                       File pdfFile, String usedPromoCode)
            throws MessagingException, IOException {
        sendOrderConfirmation(toEmail, ordre, pdfFile, usedPromoCode, null, null);
    }

    // ─── Template HTML ────────────────────────────────────────────────────────────

    private String buildHtmlBody(Ordre ordre, String email,
                                  String usedPromoCode, String nextPromoCode,
                                  boolean hasQRInline) {
        String date = ordre.getCreatedAt() != null
            ? ordre.getCreatedAt().toLocalDate().toString() : "—";

        String usedPromoSection = "";
        if (usedPromoCode != null && !usedPromoCode.isBlank()) {
            usedPromoSection = """
                <tr>
                  <td style="padding:6px 0;color:#7a6a5a;">Code promo appliqué</td>
                  <td style="padding:6px 0;color:#2d6a4f;font-weight:bold;">%s  (−30%%)</td>
                </tr>
                """.formatted(usedPromoCode);
        }

        String nextPromoSection = buildNextPromoSection(nextPromoCode, hasQRInline);

        return """
            <!DOCTYPE html>
            <html lang="fr">
            <head><meta charset="UTF-8"/></head>
            <body style="margin:0;padding:0;background:#f5f3ef;font-family:Georgia,serif;">

              <table width="100%%" cellpadding="0" cellspacing="0">
                <tr>
                  <td style="background:#2c2218;padding:28px 40px;">
                    <span style="color:white;font-size:26px;font-weight:bold;letter-spacing:3px;">ARTERA</span>
                    <span style="color:rgba(255,255,255,0.55);font-size:13px;margin-left:12px;">Marketplace d'Art</span>
                  </td>
                </tr>
              </table>

              <table width="100%%" cellpadding="0" cellspacing="0">
                <tr><td style="padding:40px;">
                  <table width="600" cellpadding="0" cellspacing="0"
                         style="background:white;border-radius:12px;
                                box-shadow:0 4px 20px rgba(74,55,40,0.10);margin:auto;">
                    <tr><td style="padding:40px;">

                      <p style="font-size:28px;color:#2d6a4f;margin:0 0 8px 0;">✅ Commande confirmée !</p>
                      <p style="color:#7a6a5a;margin:0 0 28px 0;">
                        Bonjour,<br>Votre commande a bien été enregistrée sur ARTERA Marketplace.
                      </p>

                      <table width="100%%" cellpadding="0" cellspacing="0"
                             style="background:#fdf9f5;border-radius:8px;padding:24px;">
                        <tr>
                          <td colspan="2" style="padding-bottom:12px;border-bottom:1px solid #e8e4dc;">
                            <strong style="color:#4a3728;font-size:15px;">Récapitulatif de votre commande</strong>
                          </td>
                        </tr>
                        <tr>
                          <td style="padding:10px 0 0 0;color:#7a6a5a;">N° Commande</td>
                          <td style="padding:10px 0 0 0;font-weight:bold;color:#2c2218;">#%d</td>
                        </tr>
                        <tr>
                          <td style="padding:6px 0;color:#7a6a5a;">Date</td>
                          <td style="padding:6px 0;color:#4a3728;">%s</td>
                        </tr>
                        <tr>
                          <td style="padding:6px 0;color:#7a6a5a;">Email</td>
                          <td style="padding:6px 0;color:#4a3728;">%s</td>
                        </tr>
                        <tr>
                          <td style="padding:6px 0;color:#7a6a5a;">Paiement</td>
                          <td style="padding:6px 0;color:#2d6a4f;">✔ Payé via Stripe</td>
                        </tr>
                        %s
                        <tr>
                          <td style="padding:12px 0 0 0;border-top:1px solid #e8e4dc;
                                     color:#4a3728;font-size:16px;font-weight:bold;">TOTAL TTC</td>
                          <td style="padding:12px 0 0 0;border-top:1px solid #e8e4dc;
                                     color:#2c2218;font-size:18px;font-weight:bold;">%.2f €</td>
                        </tr>
                      </table>

                      <p style="margin:24px 0 8px 0;color:#4a3728;font-weight:bold;">📄 Votre facture PDF</p>
                      <p style="color:#7a6a5a;margin:0 0 28px 0;">
                        Votre facture officielle ARTERA est jointe à cet email en pièce jointe.
                      </p>

                      %s

                    </td></tr>
                  </table>
                </td></tr>
              </table>

              <table width="100%%" cellpadding="0" cellspacing="0">
                <tr>
                  <td style="padding:20px 40px;text-align:center;color:#9a8a7a;font-size:11px;">
                    ARTERA Marketplace · v1.0.0 · Document généré automatiquement<br>
                    © 2026 ARTERA. Tous droits réservés.
                  </td>
                </tr>
              </table>

            </body></html>
            """.formatted(ordre.getId(), date, email, usedPromoSection,
                          ordre.getTotalAmount(), nextPromoSection);
    }

    /**
     * Section HTML du prochain code promo.
     * - Si QR inline disponible : affiche le code texte + l'image QR
     * - Si pas de QR            : affiche uniquement le code texte
     * - Si nextPromoCode null   : retourne ""
     */
    private String buildNextPromoSection(String nextPromoCode, boolean hasQRInline) {
        if (nextPromoCode == null || nextPromoCode.isBlank()) return "";

        String qrBlock = hasQRInline
            ? """
              <p style="margin:10px 0 6px 0;color:#7a6a5a;font-size:12px;">
                Scannez ce QR code avec votre téléphone :
              </p>
              <div style="text-align:center;margin:10px 0 4px 0;">
                <img src="cid:artera_qrcode" alt="QR Code Promo ARTERA"
                     width="170" height="170"
                     style="border:4px solid #b7e4c7;border-radius:8px;"/>
              </div>
              """
            : """
              <p style="margin:8px 0;color:#7a6a5a;font-size:12px;">
                Saisissez ce code dans le champ promo de votre prochain panier ARTERA.
              </p>
              """;

        return """
            <table width="100%%" cellpadding="0" cellspacing="0"
                   style="background:#f0f9f4;border:1px solid #b7e4c7;
                          border-radius:10px;margin-top:28px;">
              <tr><td style="padding:24px;">
                <p style="margin:0 0 6px 0;font-size:16px;font-weight:bold;color:#2d6a4f;">
                  🎁  Votre cadeau ARTERA : −30%% sur votre prochaine commande !
                </p>
                <p style="margin:0 0 14px 0;color:#4a6a5a;font-size:13px;">
                  Merci pour votre achat ! Voici votre code promo exclusif :
                </p>
                <div style="text-align:center;margin:12px 0;">
                  <span style="display:inline-block;background:white;
                               border:2px dashed #2d6a4f;border-radius:8px;
                               padding:12px 28px;font-size:20px;font-weight:bold;
                               color:#2d6a4f;letter-spacing:3px;">%s</span>
                </div>
                %s
                <p style="margin:10px 0 0 0;color:#7a8a7a;font-size:11px;text-align:center;">
                  Code valable sur ARTERA Marketplace — 1 utilisation.
                </p>
              </td></tr>
            </table>
            """.formatted(nextPromoCode, qrBlock);
    }
}
