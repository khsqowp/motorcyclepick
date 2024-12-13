package com.example.motorcycle.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mock.web.MockMultipartFile;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Component
public class ImageConverterUtil {

    /**
     * 다양한 이미지 형식을 JPG로 변환합니다.
     *
     * @param originalFile 원본 MultipartFile
     * @return 변환된 JPG 형식의 MultipartFile
     * @throws IOException 파일 처리 중 오류 발생시
     */
    public MultipartFile convertToJpg(MultipartFile originalFile) throws IOException {
        byte[] imageBytes = originalFile.getBytes();
        BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(imageBytes));

        if (originalImage == null) {
            throw new IOException("Failed to read image file");
        }

        // RGB 이미지로 변환
        BufferedImage jpgImage = new BufferedImage(
                originalImage.getWidth(),
                originalImage.getHeight(),
                BufferedImage.TYPE_INT_RGB
        );

        // 이미지 그리기 (배경색 흰색)
        jpgImage.createGraphics().drawImage(originalImage, 0, 0, null);

        // JPG로 변환
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        boolean success = ImageIO.write(jpgImage, "jpg", outputStream);

        if (!success) {
            throw new IOException("Failed to convert image to JPG format");
        }

        return new MockMultipartFile(
                "converted.jpg",
                "converted.jpg",
                "image/jpeg",
                outputStream.toByteArray()
        );
    }
}