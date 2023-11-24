package com.example.socialnetwork.service;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

public interface FirebaseImageService {
    String getImageUrl(String name);
    String save(MultipartFile file) throws IOException;
    String save(BufferedImage bufferedImage, String originalFileName) throws IOException;
    String save(byte[] bytes, String originalFileName, String type);
    void delete(String name) throws IOException;
    default String getExtension(String originalFileName) {
        return StringUtils.getFilenameExtension(originalFileName);
    }
    default String generateFileName(String originalFileName) {
        return UUID.randomUUID() + "." + getExtension(originalFileName);
    }
    default byte[] getByteArrays(BufferedImage bufferedImage, String format) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, format, baos);
            baos.flush();
            return baos.toByteArray();
        } catch (IOException e) {
            throw e;
        } finally {
            baos.close();
        }
    }
}
