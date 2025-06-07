package com.zpark.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author xx
 * @since 2025-03-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("emailcode")
@ApiModel(value="Emailcode对象", description="")
public class Emailcode implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "email", type = IdType.AUTO)
    private String email;

    @TableField("code")
    private String code;


}
