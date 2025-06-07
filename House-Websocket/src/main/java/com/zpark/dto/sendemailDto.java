package com.zpark.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;

@Data
@ApiModel(value="用于发送邮件接口", description="")
public class sendemailDto implements Serializable {


    @ApiModelProperty(value = "用户名")
    @Schema(description = "用户名", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    @ApiModelProperty(value = "邮箱")
    @Schema(description = "邮箱", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

}

