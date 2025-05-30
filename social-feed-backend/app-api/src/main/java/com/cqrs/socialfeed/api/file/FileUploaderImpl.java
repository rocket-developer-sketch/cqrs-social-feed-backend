package com.cqrs.socialfeed.api.file;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
public class FileUploaderImpl implements FileUploader {
    private final UploadProperties uploadProperties;

    public FileUploaderImpl(UploadProperties uploadProperties) {
        this.uploadProperties = uploadProperties;
    }

    @Override
    public String upload(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();

        // 1. 확장자 추출 및 검사
        String extension = getFileExtension(originalFilename);
        if (!isAllowedExtension(extension)) {
            throw new IllegalArgumentException("지원하지 않는 파일 형식입니다.");
        }

        // 2. UUID로 파일명 생성
        String encryptedFilename = UUID.randomUUID().toString() + "." + extension;

        // 3. 파일 저장
        String path = uploadProperties.getRelativePath() + encryptedFilename;
        file.transferTo(new File(path));

        // 4. 접근 가능한 URL 반환
        return uploadProperties.getHost() + encryptedFilename;
    }

    private String getFileExtension(String filename) {
        int lastDot = filename.lastIndexOf('.');
        if (lastDot == -1) throw new IllegalArgumentException("확장자가 없습니다.");
        return filename.substring(lastDot + 1).toLowerCase();
    }

    private boolean isAllowedExtension(String ext) {
        return uploadProperties.getAllowedExtensions().contains(ext);
    }

}
