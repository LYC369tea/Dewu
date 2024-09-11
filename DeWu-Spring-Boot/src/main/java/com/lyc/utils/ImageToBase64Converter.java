package com.lyc.utils;

import org.springframework.web.multipart.MultipartFile;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class ImageToBase64Converter {

    private static final int MAX_WIDTH = 512;
    private static final int MAX_HEIGHT = 512;
    private static final float QUALITY = 0.6f;

    public static String convertToBase64(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        BufferedImage originalImage = ImageIO.read(file.getInputStream());
        BufferedImage compressedImage = compressImage(originalImage);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String formatName = getFormatName(file.getContentType());
        ImageIO.write(compressedImage, formatName, outputStream);

        byte[] imageBytes = outputStream.toByteArray();
        String base64Encoded = Base64.getEncoder().encodeToString(imageBytes);

        String contentType = file.getContentType();
        if (contentType == null) {
            contentType = "image/jpeg";
        }

        return "data:" + contentType + ";base64," + base64Encoded;
    }

    private static BufferedImage compressImage(BufferedImage originalImage) {
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();

        if (originalWidth > MAX_WIDTH || originalHeight > MAX_HEIGHT) {
            float aspectRatio = (float) originalWidth / originalHeight;
            int newWidth, newHeight;

            if (aspectRatio > 1) {
                newWidth = MAX_WIDTH;
                newHeight = (int) (MAX_WIDTH / aspectRatio);
            } else {
                newHeight = MAX_HEIGHT;
                newWidth = (int) (MAX_HEIGHT * aspectRatio);
            }

            BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = resizedImage.createGraphics();
            g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
            g.dispose();

            return resizedImage;
        }

        return originalImage;
    }

    private static String getFormatName(String contentType) {
        if (contentType == null) {
            return "JPEG";
        }
        switch (contentType.toLowerCase()) {
            case "image/png":
                return "PNG";
            case "image/gif":
                return "GIF";
            default:
                return "JPEG";
        }
    }

    public static boolean isWithinSizeLimit(String base64Image) {
        String base64Data = base64Image.substring(base64Image.indexOf(",") + 1);
        int decodedLength = Base64.getDecoder().decode(base64Data).length;
        int sizeLimit = 20 * 512 * 512; // 20MB in bytes
        return decodedLength <= sizeLimit;
    }
}
