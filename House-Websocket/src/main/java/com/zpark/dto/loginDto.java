package com.zpark.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


import java.io.Serializable;


@Data
@ApiModel(value="用于登录", description="")
public class loginDto implements Serializable {


    @ApiModelProperty(value = "用户名")
    @Schema(description = "用户名", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    @ApiModelProperty(value = "密码")
    @Schema(description = "密码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

}
