package com.example.motorcyclepick.utils;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Component
public class ImageConverterUtil {
    private static final int MAX_WIDTH = 1920;
    private static final int MAX_HEIGHT = 1080;
    private static final long MAX_PROCESSING_TIME = 30000; // 30초

    public MultipartFile convertToJpg(MultipartFile originalFile) throws IOException {
        long startTime = System.currentTimeMillis();

        try {
            BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(originalFile.getBytes()));
            if (originalImage == null) {
                throw new IOException("Invalid image file");
            }

            // 이미지 크기 검증
            if (originalImage.getWidth() > MAX_WIDTH || originalImage.getHeight() > MAX_HEIGHT) {
                throw new IllegalArgumentException("Image dimensions exceed maximum allowed size");
            }

            // 메모리 사용량 모니터링
            Runtime runtime = Runtime.getRuntime();
            if (runtime.freeMemory() < originalFile.getSize() * 3) {
                throw new OutOfMemoryError("Insufficient memory for image processing");
            }

            BufferedImage jpgImage = new BufferedImage(
                    originalImage.getWidth(),
                    originalImage.getHeight(),
                    BufferedImage.TYPE_INT_RGB
            );

            jpgImage.createGraphics().drawImage(originalImage, 0, 0, null);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            if (System.currentTimeMillis() - startTime > MAX_PROCESSING_TIME) {
                throw new TimeoutException("Image processing exceeded maximum allowed time");
            }

            if (!ImageIO.write(jpgImage, "jpg", outputStream)) {
                throw new IOException("Failed to convert image to JPG format");
            }

            return new MockMultipartFile(
                    "converted.jpg",
                    "converted.jpg",
                    "image/jpeg",
                    outputStream.toByteArray()
            );
        } catch (OutOfMemoryError e) {
            throw new IOException("Memory limit exceeded during image processing", e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}