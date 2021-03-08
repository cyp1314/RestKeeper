package com.restkeeper.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author Mr-CHEN
 * @version 1.0
 * @description: 主启动类
 * @date 2021-02-28 16:06
 */
@EnableDiscoveryClient
@SpringBootApplication
public class GateWayApplication {

    public static void main(String[] args) {

        SpringApplication.run(GateWayApplication.class,args);
    }

}
