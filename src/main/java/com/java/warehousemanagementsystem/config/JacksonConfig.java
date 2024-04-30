package com.java.warehousemanagementsystem.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> {
            builder.simpleDateFormat("yyyy-MM-dd HH:mm:ss");
            builder.serializationInclusion(JsonInclude.Include.NON_NULL); // 仅序列化非空字段
            builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // 禁用日期时间戳模式
            // 添加其他需要的配置
        };
    }
}
