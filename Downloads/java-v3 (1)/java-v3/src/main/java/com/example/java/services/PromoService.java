package com.example.java.services;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * Service de gestion des codes promo + génération de QR codes.
 *
 * Fonctionnement :
 *  1. Un code promo est généré aléatoirement (format : ARTERA-XXXXXX)
 *  2. Il donne 30% de réduction sur le total
 *  3. Il dispose d'un nombre d'essais aléatoire entre 1 et 100
 *  4. Un QR code est généré contenant ce code — l'utilisateur le scanne
 *     avec son téléphone pour révéler le code, puis le saisit dans le panier
 */
public class PromoService {

    // ─── Singleton ────────────────────────────────────────────────────────────────
    private static PromoService instance;

    private PromoService() {
        generateNewPromo();
    }

    public static PromoService getInstance() {
        if (instance == null) instance = new PromoService();
        return instance;
    }

    // ─── État ─────────────────────────────────────────────────────────────────────
    private String  currentCode;
    private int     remainingUses;
    private boolean codeUsedThisSession = false;

    private final Random random = new Random();

    // Codes valides déjà générés dans cette session (code → usages restants)
    private final Map<String, Integer> validCodes = new HashMap<>();

    // ─── Génération d'un nouveau code ────────────────────────────────────────────

    public void generateNewPromo() {
        String suffix = UUID.randomUUID().toString()
                            .replace("-", "")
                            .substring(0, 6)
                            .toUpperCase();
        currentCode   = "ARTERA-" + suffix;
        remainingUses = 1 + random.nextInt(100);   // 1 à 100 essais
        validCodes.put(currentCode, remainingUses);
        codeUsedThisSession = false;
        System.out.println("[Promo] Nouveau code : " + currentCode
                           + " — " + remainingUses + " essai(s) disponible(s)");
    }

    // ─── Getters ─────────────────────────────────────────────────────────────────

    public String  getCurrentCode()    { return currentCode; }
    public int     getRemainingUses()  { return remainingUses; }

    // ─── Validation d'un code saisi ──────────────────────────────────────────────

    /**
     * Valide le code saisi par l'utilisateur.
     * @return true si le code est valide ET qu'il reste des essais
     */
    public boolean validateCode(String code) {
        if (code == null || code.isBlank()) return false;
        String upper = code.trim().toUpperCase();
        Integer uses = validCodes.get(upper);
        if (uses == null || uses <= 0) return false;
        // Décrémenter
        validCodes.put(upper, uses - 1);
        if (upper.equals(currentCode)) remainingUses = uses - 1;
        return true;
    }

    /**
     * Applique 30% de réduction.
     * @param originalTotal montant original
     * @return montant après réduction (× 0.70)
     */
    public double applyDiscount(double originalTotal) {
        return originalTotal * 0.70;
    }

    // ─── Génération du QR code ───────────────────────────────────────────────────

    /**
     * Génère une image JavaFX du QR code pour le code promo courant.
     * Le QR code encode directement le code promo (ex. : ARTERA-AB12CD)
     * L'utilisateur scanne → voit le code → le saisit dans le champ promo.
     *
     * @param size taille en pixels (ex. 200)
     * @return Image JavaFX du QR code
     */
    public Image generateQRCodeImage(int size) {
        return generateQRCodeImage(currentCode, size);
    }

    public Image generateQRCodeImage(String content, int size) {
        try {
            BufferedImage bufferedImage = generateQRBufferedImage(content, size);
            return bufferedImage != null ? SwingFXUtils.toFXImage(bufferedImage, null) : null;
        } catch (Exception e) {
            System.err.println("[PromoService] Erreur génération QR Image : " + e.getMessage());
            return null;
        }
    }

    /**
     * Génère le QR code courant sous forme de fichier PNG temporaire.
     * Utilisé pour l'envoi par email (pièce jointe inline).
     *
     * @param size taille en pixels
     * @return File PNG temporaire, ou null si erreur
     */
    public File generateQRCodeFile(int size) {
        return generateQRCodeFile(currentCode, size);
    }

    public File generateQRCodeFile(String content, int size) {
        try {
            BufferedImage img = generateQRBufferedImage(content, size);
            if (img == null) return null;
            File tmp = File.createTempFile("artera-qr-", ".png");
            ImageIO.write(img, "PNG", tmp);
            System.out.println("[PromoService] QR file : " + tmp.getAbsolutePath());
            return tmp;
        } catch (Exception e) {
            System.err.println("[PromoService] Erreur génération QR File : " + e.getMessage());
            return null;
        }
    }

    private BufferedImage generateQRBufferedImage(String content, int size) {
        try {
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.MARGIN, 1);
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix matrix = writer.encode(content, BarcodeFormat.QR_CODE, size, size, hints);
            return MatrixToImageWriter.toBufferedImage(matrix);
        } catch (WriterException e) {
            System.err.println("[PromoService] Erreur QR BufferedImage : " + e.getMessage());
            return null;
        }
    }
}
