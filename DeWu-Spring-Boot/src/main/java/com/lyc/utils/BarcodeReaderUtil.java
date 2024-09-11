package com.lyc.utils;


import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.multi.GenericMultipleBarcodeReader;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

public class BarcodeReaderUtil {

    /**
     * 解码给定文件中的所有条形码。
     *
     * @param file 包含条形码的图像文件
     * @return 解码出的条形码文本列表
     * @throws IOException       如果文件无法读取
     * @throws NotFoundException 如果未找到条形码
     */
    public static List<String> decodeBarcodes(MultipartFile file)  {
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(file.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (bufferedImage == null) {
            try {
                throw new IOException("Failed to load image.");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        Map<DecodeHintType, Object> hints = new HashMap<>();
        hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
        hints.put(DecodeHintType.POSSIBLE_FORMATS, Arrays.asList(BarcodeFormat.values()));

        MultiFormatReader multiFormatReader = new MultiFormatReader();
        multiFormatReader.setHints(hints);

        GenericMultipleBarcodeReader reader = new GenericMultipleBarcodeReader(multiFormatReader);
        Result[] results = new Result[0];
        try {
            results = reader.decodeMultiple(bitmap, hints);
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        }

        List<String> decodedTexts = new ArrayList<>();
        if (results != null) {
            for (Result result : results) {
                decodedTexts.add(result.getText());
            }
        }

        return decodedTexts;
    }
}
