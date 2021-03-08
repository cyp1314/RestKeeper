package com.restkeeper.operator.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/** 
 * @description: 用户登录实体
 * @param: * @param: null 
 * @return:  
 * @author Mr-CHEN
 * @date: 2021-03-08 17:26
 */
@ApiModel(value = "用户登录参数")
@Data
public class LoginVO implements Serializable {

    @ApiModelProperty(value = "用户名")
    private String loginName;
    @ApiModelProperty(value = "密码")
    private String loginPass;
}
