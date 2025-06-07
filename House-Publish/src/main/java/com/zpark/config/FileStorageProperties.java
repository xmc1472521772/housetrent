package com.zpark.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.unit.DataSize;

/**
 * 文件存储配置属性类
 * 通过application.yml中的file配置项注入属性
 */

@Data
@Component
@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {
    private String uploadDir;       // 文件上传基础目录
    private String maxFileSize;       // 最大文件大小(字节)
    private String[] allowedTypes;  // 允许的文件类型

    public long getMaxFileSizeInBytes() {
        return DataSize.parse(maxFileSize).toBytes();
    }

    // Getter和Setter方法
//    public String getUploadDir() {
//        return uploadDir;
//    }
//
//    public void setUploadDir(String uploadDir) {
//        this.uploadDir = uploadDir;
//    }
//
//    public long getMaxFileSize() {
//        return maxFileSize;
//    }
//
//    public void setMaxFileSize(long maxFileSize) {
//        this.maxFileSize = maxFileSize;
//    }
//
//    public String[] getAllowedTypes() {
//        return allowedTypes;
//    }
//
//    public void setAllowedTypes(String[] allowedTypes) {
//        this.allowedTypes = allowedTypes;
//    }
}
