package com.chick.webSocket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;


/**
 * @ClassName WebSocketConfig
 * @Author xiaokexin
 * @Date 2022-06-24 14:34
 * @Description WebSocketConfig
 * @Version 1.0
 */
@Configuration
public class WebSocketConfig {

    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }
}
