package com.zpark.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "file.storage")
public class FileStorageProperties {
    
    private String uploadDir = System.getProperty("user.home") + "/uploads";
    private long maxFileSizeInBytes = 10 * 1024 * 1024; // 默认10MB

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }

    public long getMaxFileSizeInBytes() {
        return maxFileSizeInBytes;
    }

    public void setMaxFileSizeInBytes(long maxFileSizeInBytes) {
        this.maxFileSizeInBytes = maxFileSizeInBytes;
    }
}
