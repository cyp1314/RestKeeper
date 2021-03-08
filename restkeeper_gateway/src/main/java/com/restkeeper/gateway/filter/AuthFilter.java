package com.restkeeper.gateway.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.restkeeper.utils.JWTUtil;
import com.restkeeper.utils.ResultConstant;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.filter.OrderedFilter;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Mr-CHEN
 * @version 1.0
 * @description: 过滤器类
 * @date 2021-03-08 17:52
 */
@Component
@RefreshScope
public class AuthFilter implements GlobalFilter, OrderedFilter {

    @Value("${gateway.secret}")
    private String secret;

    @Value("#{'${gateway.excludeUrls}'.split(',')}")
    private List<String> excludeUrls;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpResponse response = exchange.getResponse();

        // 请求路径
        String path = exchange.getRequest().getPath().toString();

        // 排除特殊不需要令牌路径
        if (excludeUrls.contains(path)){
            return chain.filter(exchange);
        }
        // 获取令牌信息
        String token = response.getHeaders().getFirst("Authorization");
        if (StringUtils.isNotEmpty(token)){
            JWTUtil.VerifyResult verifyJwt = JWTUtil.verifyJwt(token, secret);
            if (verifyJwt.isValidate()){
                // 合法
                return chain.filter(exchange);
            }else {
                // 不合法
                Map<String,Object> responseData = Maps.newHashMap();
                responseData.put("code",verifyJwt.getCode());
                responseData.put("message", ResultConstant.TOKEN_NOT_PASS);
                return responseError(response,responseData);
            }
        }else {
            //返回错误信息
            Map<String,Object> responseData = Maps.newHashMap();
            responseData.put("code", 401);
            responseData.put("message", ResultConstant.TOKEN_NOT_EXISTS);
            responseData.put("cause", ResultConstant.NOT_TOKEN);
            return responseError(response,responseData);
        }
    }

    // 返回数据
    private Mono<Void> responseError(ServerHttpResponse response, Map<String, Object> responseData) {
        //将信息转换为Json
        ObjectMapper objectMapper = new ObjectMapper();
        byte[] data = new byte[0];
        try{
            data = objectMapper.writeValueAsBytes(responseData);
        } catch (Exception e){
            e.printStackTrace();
        }
        //输出错误信息
        DataBuffer buffer = response.bufferFactory().wrap(data);
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        return response.writeWith(Mono.just(buffer));
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
