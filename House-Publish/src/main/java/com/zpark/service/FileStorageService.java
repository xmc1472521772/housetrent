package com.zpark.service;

import com.zpark.config.FileStorageProperties;
import com.zpark.exception.FileStorageException;
import com.zpark.exception.InvalidFileException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;  // 文件存储位置路径

    @Autowired
    private FileStorageProperties fileStorageProperties;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) throws FileStorageException {
        // 初始化文件存储位置
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            // 创建存储目录(如果不存在)
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("无法创建文件存储目录", ex);
        }
    }

    /**
     * 存储文件到系统
     * @param file 要存储的文件
     * @return 存储的文件名
     */
    public String storeFile(MultipartFile file) {
        // 验证文件
        validateFile(file);

        // 生成唯一文件名
        String fileName = generateUniqueFileName(file.getOriginalFilename());

        try {
            // 检查文件名是否包含非法字符
            if (fileName.contains("..")) {
                throw new FileStorageException("文件名包含非法路径序列: " + fileName);
            }

            // 将文件复制到目标位置(替换同名文件)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("无法存储文件 " + fileName + ". 请重试!", ex);
        }
    }

    /**
     * 异步存储文件(性能优化)
     * @param file 要存储的文件
     * @return CompletableFuture包含存储的文件名
     */
    @Async
    public CompletableFuture<String> storeFileAsync(MultipartFile file) {

        return CompletableFuture.supplyAsync(() -> storeFile(file));
    }

    /**
     * 加载文件作为资源
     * @param fileName 文件名
     * @return 文件资源
     */
    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return resource;
            } else {
                throw new FileStorageException("文件未找到: " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new FileStorageException("文件未找到: " + fileName, ex);
        }
    }

    /**
     * 生成缩略图
     * @param originalFilename 原始文件名
     * @param width 缩略图宽度
     * @param height 缩略图高度
     * @return 缩略图文件名
     */
    public String generateThumbnail(String originalFilename, int width, int height) {
        try {
            Path originalPath = this.fileStorageLocation.resolve(originalFilename);
            BufferedImage originalImage = ImageIO.read(originalPath.toFile());

            // 创建缩略图
            BufferedImage thumbnail = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            thumbnail.createGraphics().drawImage(
                    originalImage.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH),
                    0, 0, null);

            // 生成缩略图文件名
            String thumbnailName = "thumb_" + originalFilename;
            Path thumbnailPath = this.fileStorageLocation.resolve(thumbnailName);

            // 保存缩略图
            ImageIO.write(thumbnail, "jpg", thumbnailPath.toFile());

            return thumbnailName;
        } catch (IOException ex) {
            throw new FileStorageException("无法生成缩略图", ex);
        }
    }

    /**
     * 验证文件是否合法
     * @param file 要验证的文件
     */

    @Value("${spring.servlet.multipart.max-file-size}")
    String maxSize;
    private void validateFile(MultipartFile file) {


        // 检查文件是否为空
        if (file.isEmpty()) {
            throw new InvalidFileException("文件为空");
        }

        // 检查文件大小
        if (file.getSize() > fileStorageProperties.getMaxFileSizeInBytes()) {
            throw new InvalidFileException("文件大小超过限制");
        }

        // 检查文件类型
        String fileContentType = file.getContentType();
        if (fileContentType == null || !isAllowedFileType(fileContentType)) {
            throw new InvalidFileException("不支持的文件类型: " + fileContentType);
        }

        // 检查文件内容是否真实匹配其类型(防止伪造文件类型)
        try {
            if (fileContentType.startsWith("image/")) {
                // 验证图片文件
                BufferedImage image = ImageIO.read(file.getInputStream());
                if (image == null) {
                    throw new InvalidFileException("文件不是有效的图片");
                }
            }
        } catch (IOException e) {
            throw new InvalidFileException("无法验证文件内容", e);
        }
    }

    /**
     * 检查文件类型是否允许
     * @param fileType 文件MIME类型
     * @return 是否允许
     */
    private boolean isAllowedFileType(String fileType) {
        return Arrays.asList(
                "image/jpeg",
                "image/png",
                "image/gif",
                "video/mp4",
                "video/quicktime"
        ).contains(fileType);
    }

    /**
     * 生成唯一文件名
     * @param originalFileName 原始文件名
     * @return 唯一文件名
     */
    private String generateUniqueFileName(String originalFileName) {
        // 提取文件扩展名
        String fileExtension = StringUtils.getFilenameExtension(originalFileName);
        // 生成UUID作为新文件名
        String uniqueName = UUID.randomUUID().toString();

        return fileExtension != null ?
                uniqueName + "." + fileExtension.toLowerCase() :
                uniqueName;
    }
}
