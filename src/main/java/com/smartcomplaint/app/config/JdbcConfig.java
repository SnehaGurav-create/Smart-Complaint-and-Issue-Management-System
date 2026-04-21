package com.smartcomplaint.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;

@Configuration
public class JdbcConfig {
    // Spring Boot auto-configures DataSource, so we just wrap it with a JdbcTemplate if needed explicitly.
    // Or we simply use the auto-configured one. We will define it here just to fulfill the architectural layout.
    
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
