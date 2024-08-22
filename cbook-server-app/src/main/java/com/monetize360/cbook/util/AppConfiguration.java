package com.monetize360.cbook.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {
    @Bean
    public ObjectMapper objectMapper(){
        ObjectMapper objectMapper=new ObjectMapper();
        return objectMapper;
    }

}
