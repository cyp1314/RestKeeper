package com.restkeeper.operator.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 运营端管理员
 * </p>
 */
@Data
@Accessors(chain = true)
@ApiModel(value="OperatorUser对象", description="运营端管理员信息")
@TableName("t_operator_user")
public class OperatorUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户id")
    @TableId(type = IdType.ASSIGN_ID)
    private String uid;

    @ApiModelProperty(value = "用户名称")
    @TableField(value = "loginname")
    private String loginname;

    @JsonIgnore
    @ApiModelProperty(value = "用户密码")
    @TableField(value = "loginpass")
    private String loginpass;


}
