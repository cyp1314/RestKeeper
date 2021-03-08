package com.restkeeper.operator.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.restkeeper.operator.entity.OperatorUser;
import com.restkeeper.operator.mapper.OperatorUserMapper;
import com.restkeeper.utils.JWTUtil;
import com.restkeeper.utils.MD5CryptUtil;
import com.restkeeper.utils.Result;
import com.restkeeper.utils.ResultCode;
import com.restkeeper.utils.ResultConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RefreshScope
@DubboService(version = "1.0.0",protocol = "dubbo")
public class OperatorUserServiceImpl extends ServiceImpl<OperatorUserMapper, OperatorUser> implements IOperatorUserService{

    @Value("${gateway.secret}")
    private String secret;

    /**
     * @description: 管理员登录
     * @param: * @param: loginName
     * @param: loginPass
     * @return: com.restkeeper.utils.Result
     * @author Mr-CHEN
     * @date: 2021-03-08 17:16
     */
    @Override
    public Result login(String loginName, String loginPass) {
        Result result = new Result();
        if (StringUtils.isEmpty(loginName)){
            result.setStatus(ResultCode.error);
            result.setDesc(ResultConstant.USERNAME_ENPTY);
            return result;
        }
        if (StringUtils.isEmpty(loginPass)){
            result.setStatus(ResultCode.error);
            result.setDesc(ResultConstant.USERPASS_ENPTY);
            return result;
        }

        QueryWrapper<OperatorUser> wrapper = new QueryWrapper<>();
        wrapper.eq("loginname",loginName);
        OperatorUser operatorUser = this.baseMapper.selectOne(wrapper);
        if (operatorUser ==null ){
            result.setStatus(ResultCode.error);
            result.setDesc(ResultConstant.USER_NOT_EXISTS);
            return result;
        }

        // 比对密码
        String salts = MD5CryptUtil.getSalts(operatorUser.getLoginpass());
        if (!Md5Crypt.md5Crypt(loginPass.getBytes(),salts).equals(operatorUser.getLoginpass())){
            result.setStatus(ResultCode.error);
            result.setDesc(ResultConstant.NOT_PASS);
            return result;
        }
        // 生产 jwt 令牌
        Map<String,Object> tokenInfo = new HashMap<>();
        tokenInfo.put("loginName",loginName);
        String token = null;

        try {
            token = JWTUtil.createJWTByObj(tokenInfo,secret);
        } catch (IOException e) {
            log.error("加密失败",e.getMessage());
            result.setStatus(ResultCode.error);
            result.setDesc(ResultConstant.TOKEN_ERROR);
            return result;
        }

        // 返回结果
        result.setStatus(ResultCode.success);
        result.setDesc(ResultConstant.SUCCESS);
        result.setData(operatorUser);
        result.setToken(token);
        return result;
    }

    @Override
    public IPage<OperatorUser> queryPageByName(int page, int pageSize, String name) {
        QueryWrapper<OperatorUser> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(name)){
            wrapper.like("loginname",name);
        }
        return this.baseMapper.selectPage(new Page<>(page, pageSize), wrapper);
    }
}
