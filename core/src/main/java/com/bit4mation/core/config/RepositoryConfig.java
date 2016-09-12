package com.bit4mation.core.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = {"com.bit4mation"})
@EntityScan(basePackages = {"com.bit4mation"})
@ComponentScan(basePackages = {"com.bit4mation"})
@EnableTransactionManagement
public class RepositoryConfig {
}
