package com.restkeeper.operator.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.restkeeper.operator.entity.OperatorUser;
import com.restkeeper.operator.service.IOperatorUserService;
import com.restkeeper.operator.vo.LoginVO;
import com.restkeeper.response.vo.PageVO;
import com.restkeeper.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理员的登录接口
 */
@RestController
@RefreshScope //配置中心的自动刷新
@Slf4j
@Api(tags = {"管理员相关接口"})
public class UserController {


    @Value("${server.port}")
    private String port;

    @DubboReference(version = "1.0.0", check = false)
    private IOperatorUserService operatorUserService;

    @ApiOperation(value = "测试动态刷新配置")
    @GetMapping(value = "/echo")
    public String echo() {
        return "i am from port: " + port;
    }

    @ApiOperation(value = "登陆")
    @PostMapping("/login")
    public Result login(@RequestBody LoginVO loginVO){
        return operatorUserService.login(loginVO.getLoginName(),loginVO.getLoginPass());
    }


    @ApiOperation(value = "分页查询管理员")
    @GetMapping("/pageList/{page}/{pageSize}")
    public PageVO<OperatorUser> findListByPage(@ApiParam(name = "page", value = "当前页面", required = true) @PathVariable("page") int page,
                                 @ApiParam(name = "pageSize", value = "每页大小", required = true) @PathVariable("pageSize") int pageSize,
                                 @ApiParam(name = "name", required = false) String name) {
        IPage<OperatorUser> operatorUserIPage = operatorUserService.queryPageByName(page, page, name);
        int i = 1 / 0;
        return new PageVO<>(operatorUserIPage);
    }

}
