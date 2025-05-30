package com.cqrs.socialfeed.api.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.cqrs.socialfeed.infrards")
@EntityScan(basePackages = "com.cqrs.socialfeed.infrards")
public class JpaConfig {
}
