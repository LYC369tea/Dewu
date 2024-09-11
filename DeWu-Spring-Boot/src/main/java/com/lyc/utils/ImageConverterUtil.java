package com.lyc.utils;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class ImageConverterUtil {

    /**
     * 将 JPG 格式的 MultipartFile 转换为 PNG 格式的 MultipartFile。
     *
     * @param jpgFile 输入的 JPG 格式 MultipartFile 对象。
     * @return 返回一个新的 MultipartFile 对象，表示转换后的 PNG 图像。
     * @throws IOException 如果文件转换或写入过程中发生 I/O 错误。
     */
    public static MultipartFile convertJpgToPng(MultipartFile jpgFile) throws IOException {
        if (!jpgFile.isEmpty() && jpgFile.getOriginalFilename().endsWith(".jpg")) {
            // 将 MultipartFile 转换为 BufferedImage
            BufferedImage image = ImageIO.read(jpgFile.getInputStream());
            if (image == null) {
                throw new IOException("无法读取图像.");
            }

            // 创建临时的 PNG 文件
            File tempPngFile = Files.createTempFile("temp-png-", ".png").toFile();
            tempPngFile.deleteOnExit(); // 确保 JVM 退出时删除临时文件

            // 将 BufferedImage 保存为 PNG 文件
            ImageIO.write(image, "PNG", tempPngFile);

            // 将临时文件转换为 MultipartFile
            return new MockMultipartFile(tempPngFile);
        } else {
            throw new IOException("上传的文件不是有效的 JPG 文件.");
        }
    }

    private static class MockMultipartFile implements MultipartFile {
        private final File file;

        public MockMultipartFile(File file) {
            this.file = file;
        }

        @Override
        public String getName() {
            return file.getName();
        }

        @Override
        public String getOriginalFilename() {
            return file.getName();
        }

        @Override
        public String getContentType() {
            return "image/png";
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public long getSize() {
            return file.length();
        }

        @Override
        public byte[] getBytes() throws IOException {
            return Files.readAllBytes(file.toPath());
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return Files.newInputStream(file.toPath());
        }

        @Override
        public void transferTo(File dest) throws IOException {
            Files.copy(file.toPath(), dest.toPath());
        }
    }

}