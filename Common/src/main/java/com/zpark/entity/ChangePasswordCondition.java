package com.zpark.entity;


import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;

import java.io.Serializable;

@Data
//更改密码所需参数
@Schema(name = "ChangePasswordCondition请求实体", description = "更改密码所需参数")
public class ChangePasswordCondition implements Serializable {
    @Schema(description = "旧密码", example = "12345678", requiredMode = Schema.RequiredMode.REQUIRED)
    private String oldPassword;
    @Schema(description = "新密码", example = "12312312", requiredMode = Schema.RequiredMode.REQUIRED)
    private String newPassword;
    @Schema(description = "确认新密码", example = "12312312", requiredMode = Schema.RequiredMode.REQUIRED)
    private String confirmNewPassword;
}
