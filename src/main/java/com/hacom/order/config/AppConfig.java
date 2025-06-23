package com.hacom.order.config;

import com.hacom.order.smpp.SmppClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public SmppClient smppClient() {
        return new SmppClient();
    }
}
