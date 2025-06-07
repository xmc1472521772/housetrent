package com.zpark.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zpark.entity.House;
import com.zpark.exception.FileStorageException;
import com.zpark.exception.InvalidFileException;
import com.zpark.service.FileStorageService;
import com.zpark.service.IHouseService;
import com.zpark.utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/files")
@Tag(name = "提交房源接口", description = "提交房源接口")
public class FileController {

    private final FileStorageService fileStorageService;

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private IHouseService houseService;

    @Autowired
    public FileController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }



    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "上传房源信息及媒体文件",
            description = "支持同时上传房源JSON数据、图片和视频文件",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @Parameters({
            @Parameter(
                    name = "Authorization",
                    description = "认证令牌",
                    required = true,
                    in = ParameterIn.HEADER,
                    schema = @Schema(type = "string", example = "Bearer xxx.yyy.zzz")
            ),
            @Parameter(
                    name = "imageFile",
                    description = "房源图片文件",
                    required = false,
                    in = ParameterIn.QUERY,
                    schema = @Schema(type = "string", format = "binary")
            ),
            @Parameter(
                    name = "videoFile",
                    description = "房源视频文件",
                    required = false,
                    in = ParameterIn.QUERY,
                    schema = @Schema(type = "string", format = "binary")
            )
    })
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "上传成功",
                    content = @Content(schema = @Schema(example = """
            {
              "message": "房屋信息和媒体文件上传成功",
              "image": "ok",
              "video": "ok"
            }"""))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "参数错误（JSON解析失败/文件格式错误）",
                    content = @Content(schema = @Schema(example = """
            {
              "error": "房屋信息JSON解析失败",
              "details": "Invalid JSON format"
            }"""))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "未授权（Token无效或过期）"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "服务器内部错误（文件存储失败等）"
            )
    })
    public ResponseEntity<Map<String, String>> uploadHouseWithMedia(
            @Parameter(description = "房源JSON数据", required = true,
                    schema = @Schema(implementation = House.class ))
            @RequestPart("house") String houseJson,  // 接收JSON字符串
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile,
            @RequestPart(value = "videoFile", required = false) MultipartFile videoFile,
            HttpServletRequest request) {

        try {
            // 解析JSON到House对象
            ObjectMapper objectMapper = new ObjectMapper();
            House house = objectMapper.readValue(houseJson, House.class);

            // 处理JWT token
            String requestTokenHeader = request.getHeader("Authorization");
            String token = null;
            if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
                token = requestTokenHeader.substring(7);
            }
            house.setOwnerId(jwtUtil.getUserFromToken(token).getId());

            // 处理图片上传
            if (imageFile != null && !imageFile.isEmpty()) {
                String imageFileName = fileStorageService.storeFile(imageFile);
                String imageUrl = generateFileUrl(request, imageFileName);
                house.setImg(imageUrl);
            }

            // 处理视频上传
            if (videoFile != null && !videoFile.isEmpty()) {
                String videoFileName = fileStorageService.storeFile(videoFile);
                String videoUrl = generateFileUrl(request, videoFileName);
                house.setVideo(videoUrl);
            }

            // 保存房屋信息
            houseService.housePublish(house);

            Map<String, String> response = new HashMap<>();
            response.put("message", "房屋信息和媒体文件上传成功");
//            response.put("houseId", house.getHouseId().toString());
            if (house.getImg() != null) {
//                response.put("imageUrl", house.getImg());
                response.put("image", "ok");
            }
            if (house.getVideo() != null) {
//                response.put("videoUrl", house.getVideo());
                response.put("video", "ok");
            }

            return ResponseEntity.ok(response);
        } catch (JsonProcessingException e) {
            throw new InvalidFileException("房屋信息JSON解析失败");
        } catch (InvalidFileException e) {
            throw e;
        } catch (Exception e) {
            throw new FileStorageException("上传失败: " + e.getMessage(), e);
        }
    }

    // 生成完整的文件URL
    private String generateFileUrl(HttpServletRequest request, String fileName) {
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();

        //待修改
//        return baseUrl + "D:\\aaaimage" + fileName;
        return "D:\\aaaimage" + fileName;
    }



//    /**
//     * 下载文件
//     * @param fileName 文件名
//     * @param request HTTP请求
//     * @return 文件资源响应
//     */
//    @GetMapping("/download/{fileName:.+}")
//    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
//        try {
//            // 加载文件作为资源
//            Resource resource = fileStorageService.loadFileAsResource(fileName);
//
//            // 尝试确定内容类型
//            String contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
//            if (contentType == null) {
//                contentType = "application/octet-stream";
//            }
//
//            return ResponseEntity.ok()
//                    .contentType(MediaType.parseMediaType(contentType))
//                    .header(HttpHeaders.CONTENT_DISPOSITION,
//                            "attachment; filename=\"" + resource.getFilename() + "\"")
//                    .body(resource);
//        } catch (IOException ex) {
//            throw new FileStorageException("无法确定文件类型", ex);
//        }
//    }
//
//    /**
//     * 生成缩略图
//     * @param fileName 原始文件名
//     * @return 包含缩略图文件名的响应
//     */
//    @GetMapping("/thumbnail/{fileName:.+}")
//    public ResponseEntity<Map<String, String>> generateThumbnail(@PathVariable String fileName) {
//        try {
//            // 生成缩略图(默认200x200)
//            String thumbnailName = fileStorageService.generateThumbnail(fileName, 200, 200);
//
//            Map<String, String> response = new HashMap<>();
//            response.put("thumbnailName", thumbnailName);
//            response.put("downloadUri", "/api/files/download/" + thumbnailName);
//
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            throw new FileStorageException("无法生成缩略图", e);
//        }
//    }
}
