package com.zpark.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;

@Data
@ApiModel(value="注册用", description="")
public class registerDto implements Serializable {


    @ApiModelProperty(value = "用户名")
    @Schema(description = "用户名", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    @ApiModelProperty(value = "密码")
    @Schema(description = "密码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

    @ApiModelProperty(value = "邮箱")
    @Schema(description = "邮箱", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;


    @TableField(exist = false)
    @ApiModelProperty(value = "验证码")
    @Schema(description = "验证码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String code;



}
