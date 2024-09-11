package com.lyc;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.multi.GenericMultipleBarcodeReader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class BarcodeReader {

    public static void main(String[] args) {
        try {
            File file = new File("src/main/resources/static/image/testimage.jpg");
            BufferedImage bufferedImage = ImageIO.read(file);

            LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            Map<DecodeHintType, Object> hints = new HashMap<>();
            hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
            // 使用 Arrays.asList 将 BarcodeFormat 数组转换为 List
            hints.put(DecodeHintType.POSSIBLE_FORMATS, Arrays.asList(BarcodeFormat.values()));

            MultiFormatReader multiFormatReader = new MultiFormatReader();
            multiFormatReader.setHints(hints);

            // 使用 GenericMultipleBarcodeReader 处理可能存在多个条形码的情况
            GenericMultipleBarcodeReader reader = new GenericMultipleBarcodeReader(multiFormatReader);
            Result[] results = reader.decodeMultiple(bitmap, hints);

            if (results != null) {
                for (Result result : results) {
                    System.out.println("Barcode text is " + result.getText());
                }
            } else {
                System.out.println("No barcode found.");
            }

        } catch (IOException e) {
            System.err.println("Failed to load image: " + e.getMessage());
        } catch (NotFoundException e) {
            System.err.println("No barcode found in image: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
