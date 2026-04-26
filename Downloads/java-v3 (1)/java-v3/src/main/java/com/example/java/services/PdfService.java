package com.example.java.services;

import com.example.java.entities.CartItem;
import com.example.java.entities.Ordre;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.File;
import java.io.FileOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Génère un PDF de confirmation de commande ARTERA avec signature de l'application.
 */
public class PdfService {

    // ─── Couleurs ARTERA ──────────────────────────────────────────────────────────
    private static final BaseColor BROWN_DARK  = new BaseColor(0x2C, 0x22, 0x18);
    private static final BaseColor BROWN_MED   = new BaseColor(0x4A, 0x37, 0x28);
    private static final BaseColor BROWN_LIGHT = new BaseColor(0x6B, 0x4F, 0x3A);
    private static final BaseColor CREAM       = new BaseColor(0xFD, 0xF9, 0xF5);
    private static final BaseColor GREEN_OK    = new BaseColor(0x2D, 0x6A, 0x4F);
    private static final BaseColor GRAY_TEXT   = new BaseColor(0x7A, 0x6A, 0x5A);

    // ─── Polices ──────────────────────────────────────────────────────────────────
    private static final Font FONT_TITLE   = new Font(Font.FontFamily.HELVETICA, 22, Font.BOLD,   BROWN_DARK);
    private static final Font FONT_HEADER  = new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD,   BROWN_MED);
    private static final Font FONT_NORMAL  = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, BROWN_MED);
    private static final Font FONT_SMALL   = new Font(Font.FontFamily.HELVETICA,  9, Font.NORMAL, GRAY_TEXT);
    private static final Font FONT_PRICE   = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD,   BROWN_DARK);
    private static final Font FONT_SIGN    = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC, GRAY_TEXT);
    private static final Font FONT_SUCCESS = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD,   GREEN_OK);
    private static final Font FONT_WHITE   = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD,   BaseColor.WHITE);
    private static final Font FONT_WHITE_S = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, BaseColor.WHITE);

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    /**
     * Génère un fichier PDF pour la commande donnée.
     *
     * @param ordre       l'objet Ordre avec id, total, date, buyerId
     * @param items       les articles du panier (CartItem)
     * @param buyerEmail  email de l'acheteur
     * @param promoCode   code promo utilisé (null si aucun)
     * @param originalTotal montant avant réduction
     * @return le File PDF généré dans le dossier temp système
     */
    public File generateOrderPDF(Ordre ordre,
                                  List<CartItem> items,
                                  String buyerEmail,
                                  String promoCode,
                                  double originalTotal) throws Exception {

        File pdfFile = File.createTempFile("artera-commande-" + ordre.getId() + "-", ".pdf");

        Document doc = new Document(PageSize.A4, 50, 50, 40, 40);
        PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(pdfFile));
        doc.open();

        // ── 1. HEADER ──────────────────────────────────────────────────────────────
        addHeader(doc, writer);

        // ── 2. TITRE + STATUT ──────────────────────────────────────────────────────
        doc.add(Chunk.NEWLINE);
        Paragraph statutPara = new Paragraph("✔  Commande confirmée", FONT_SUCCESS);
        statutPara.setAlignment(Element.ALIGN_CENTER);
        doc.add(statutPara);
        doc.add(Chunk.NEWLINE);

        // ── 3. INFOS COMMANDE ──────────────────────────────────────────────────────
        addCommandeInfo(doc, ordre, buyerEmail);

        doc.add(Chunk.NEWLINE);

        // ── 4. TABLEAU ARTICLES ────────────────────────────────────────────────────
        addArticlesTable(doc, items);

        doc.add(Chunk.NEWLINE);

        // ── 5. TOTAUX ─────────────────────────────────────────────────────────────
        addTotaux(doc, originalTotal, ordre.getTotalAmount(), promoCode);

        doc.add(Chunk.NEWLINE);
        doc.add(Chunk.NEWLINE);

        // ── 6. SIGNATURE APPLICATION ───────────────────────────────────────────────
        addSignature(doc, writer, ordre);

        doc.close();
        System.out.println("[PdfService] PDF généré : " + pdfFile.getAbsolutePath());
        return pdfFile;
    }

    // ─── HEADER ──────────────────────────────────────────────────────────────────

    private void addHeader(Document doc, PdfWriter writer) throws DocumentException {
        // Bandeau coloré en haut
        PdfContentByte cb = writer.getDirectContent();
        cb.setColorFill(BROWN_DARK);
        cb.rectangle(50, doc.top() - 55, doc.right() - doc.left(), 55);
        cb.fill();

        // Logo texte dans le bandeau
        Paragraph logo = new Paragraph("ARTERA", FONT_WHITE);
        logo.setAlignment(Element.ALIGN_LEFT);
        logo.setSpacingBefore(8);

        Paragraph sub = new Paragraph("Marketplace d'Art · Facture de commande", FONT_WHITE_S);
        sub.setAlignment(Element.ALIGN_LEFT);

        doc.add(logo);
        doc.add(sub);

        // Ligne séparatrice
        cb.setColorFill(BROWN_LIGHT);
        cb.rectangle(50, doc.top() - 60, doc.right() - doc.left(), 2);
        cb.fill();
    }

    // ─── INFOS COMMANDE ───────────────────────────────────────────────────────────

    private void addCommandeInfo(Document doc, Ordre ordre, String buyerEmail) throws DocumentException {
        PdfPTable infoTable = new PdfPTable(2);
        infoTable.setWidthPercentage(100);
        infoTable.setSpacingBefore(12);
        infoTable.setSpacingAfter(8);
        infoTable.setWidths(new float[]{1f, 1f});

        // Colonne gauche
        PdfPCell leftCell = new PdfPCell();
        leftCell.setBorder(Rectangle.NO_BORDER);
        leftCell.setBackgroundColor(CREAM);
        leftCell.setPadding(16);

        leftCell.addElement(new Paragraph("DÉTAILS DE LA COMMANDE", FONT_HEADER));
        leftCell.addElement(new Paragraph("N° Commande :  #" + ordre.getId(), FONT_NORMAL));
        leftCell.addElement(new Paragraph("Date :  " +
            (ordre.getCreatedAt() != null ? ordre.getCreatedAt().format(FMT) : "—"), FONT_NORMAL));
        leftCell.addElement(new Paragraph("Acheteur ID :  " + ordre.getBuyerId(), FONT_NORMAL));

        // Colonne droite
        PdfPCell rightCell = new PdfPCell();
        rightCell.setBorder(Rectangle.NO_BORDER);
        rightCell.setBackgroundColor(CREAM);
        rightCell.setPadding(16);

        rightCell.addElement(new Paragraph("INFORMATIONS CLIENT", FONT_HEADER));
        rightCell.addElement(new Paragraph("Email :  " + (buyerEmail != null ? buyerEmail : "—"), FONT_NORMAL));
        rightCell.addElement(new Paragraph("Statut paiement :  ✔ Payé via Stripe", FONT_NORMAL));

        infoTable.addCell(leftCell);
        infoTable.addCell(rightCell);
        doc.add(infoTable);
    }

    // ─── TABLEAU ARTICLES ─────────────────────────────────────────────────────────

    private void addArticlesTable(Document doc, List<CartItem> items) throws DocumentException {
        Paragraph titre = new Paragraph("Articles commandés", FONT_HEADER);
        titre.setSpacingBefore(8);
        doc.add(titre);

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setSpacingBefore(8);
        table.setWidths(new float[]{3f, 1.2f, 1.5f, 1.5f});

        // En-têtes
        String[] headers = {"Œuvre / Type", "Quantité", "Prix unitaire", "Sous-total"};
        for (String h : headers) {
            PdfPCell hCell = new PdfPCell(new Phrase(h, new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.WHITE)));
            hCell.setBackgroundColor(BROWN_MED);
            hCell.setPadding(10);
            hCell.setBorder(Rectangle.NO_BORDER);
            table.addCell(hCell);
        }

        // Lignes articles
        boolean alternate = false;
        for (CartItem item : items) {
            BaseColor rowColor = alternate ? new BaseColor(0xF5, 0xF3, 0xEF) : BaseColor.WHITE;

            addTableCell(table, nomProduit(item.getPost().getProductType()), FONT_NORMAL, rowColor);
            addTableCell(table, String.valueOf(item.getQuantity()),          FONT_NORMAL, rowColor);
            addTableCell(table, String.format("%.2f €", item.getPost().getPrice()), FONT_NORMAL, rowColor);
            addTableCell(table, String.format("%.2f €", item.getSubtotal()), FONT_NORMAL, rowColor);

            alternate = !alternate;
        }

        doc.add(table);
    }

    private void addTableCell(PdfPTable table, String text, Font font, BaseColor bg) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(bg);
        cell.setPadding(9);
        cell.setBorder(Rectangle.BOTTOM);
        cell.setBorderColor(new BaseColor(0xE8, 0xE4, 0xDC));
        table.addCell(cell);
    }

    // ─── TOTAUX ──────────────────────────────────────────────────────────────────

    private void addTotaux(Document doc, double originalTotal, double finalTotal, String promoCode) throws DocumentException {
        PdfPTable totTable = new PdfPTable(2);
        totTable.setWidthPercentage(55);
        totTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
        totTable.setWidths(new float[]{2f, 1.5f});

        if (promoCode != null && !promoCode.isBlank()) {
            addTotalRow(totTable, "Sous-total",             String.format("%.2f €", originalTotal), false);
            addTotalRow(totTable, "Code promo (" + promoCode + ")", "-30%", false);
        }
        addTotalRow(totTable, "TOTAL TTC",  String.format("%.2f €", finalTotal), true);

        doc.add(totTable);
    }

    private void addTotalRow(PdfPTable t, String label, String value, boolean bold) {
        Font f = bold ? FONT_PRICE : FONT_NORMAL;
        BaseColor bg = bold ? CREAM : BaseColor.WHITE;

        PdfPCell lbl = new PdfPCell(new Phrase(label, f));
        lbl.setBorder(Rectangle.NO_BORDER);
        lbl.setBackgroundColor(bg);
        lbl.setPadding(8);

        PdfPCell val = new PdfPCell(new Phrase(value, f));
        val.setBorder(Rectangle.NO_BORDER);
        val.setBackgroundColor(bg);
        val.setPadding(8);
        val.setHorizontalAlignment(Element.ALIGN_RIGHT);

        t.addCell(lbl);
        t.addCell(val);
    }

    // ─── SIGNATURE APPLICATION ────────────────────────────────────────────────────

    private void addSignature(Document doc, PdfWriter writer, Ordre ordre) throws DocumentException {
        // Ligne de séparation
        PdfContentByte cb = writer.getDirectContent();
        cb.setColorStroke(new BaseColor(0xD4, 0xCF, 0xC7));
        cb.setLineWidth(0.5f);
        cb.moveTo(50,  120);
        cb.lineTo(doc.right() - 50, 120);
        cb.stroke();

        // Bloc signature
        PdfPTable sigTable = new PdfPTable(2);
        sigTable.setWidthPercentage(100);
        sigTable.setSpacingBefore(28);
        sigTable.setWidths(new float[]{1f, 1f});

        // Gauche : infos application
        PdfPCell leftSig = new PdfPCell();
        leftSig.setBorder(Rectangle.NO_BORDER);

        leftSig.addElement(new Paragraph("ARTERA Platform", FONT_HEADER));
        leftSig.addElement(new Paragraph("Marketplace d'Art · v1.0.0", FONT_SIGN));
        leftSig.addElement(new Paragraph("Document généré automatiquement", FONT_SIGN));
        leftSig.addElement(new Paragraph("Référence : ARTERA-CMD-" + ordre.getId()
            + "-" + System.currentTimeMillis(), FONT_SIGN));

        // Droite : signature numérique
        PdfPCell rightSig = new PdfPCell();
        rightSig.setBorder(Rectangle.NO_BORDER);
        rightSig.setHorizontalAlignment(Element.ALIGN_RIGHT);

        rightSig.addElement(new Paragraph("Signature de l'application", FONT_SIGN));
        rightSig.addElement(new Paragraph(" ", FONT_SIGN));

        // Ligne de signature simulée
        Paragraph sigLine = new Paragraph("___________________________", FONT_NORMAL);
        sigLine.setAlignment(Element.ALIGN_RIGHT);
        rightSig.addElement(sigLine);

        Paragraph sigName = new Paragraph("ARTERA · Système Automatisé", FONT_SIGN);
        sigName.setAlignment(Element.ALIGN_RIGHT);
        rightSig.addElement(sigName);

        sigTable.addCell(leftSig);
        sigTable.addCell(rightSig);
        doc.add(sigTable);

        // Bas de page
        Paragraph footer = new Paragraph(
            "Ce document est une confirmation officielle de votre commande sur ARTERA Marketplace. " +
            "Conservez-le pour vos archives.", FONT_SMALL);
        footer.setAlignment(Element.ALIGN_CENTER);
        footer.setSpacingBefore(12);
        doc.add(footer);
    }

    // ─── Helpers ─────────────────────────────────────────────────────────────────

    private String nomProduit(String type) {
        return switch (type.toLowerCase()) {
            case "original"    -> "Œuvre originale";
            case "print"       -> "Impression artistique";
            case "digital"     -> "Art numérique";
            case "painting"    -> "Peinture";
            case "sculpture"   -> "Sculpture";
            case "photography" -> "Photographie";
            case "drawing"     -> "Dessin";
            default            -> type.substring(0, 1).toUpperCase() + type.substring(1);
        };
    }
}
