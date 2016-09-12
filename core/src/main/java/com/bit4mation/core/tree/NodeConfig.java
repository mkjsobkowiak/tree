package com.bit4mation.core.tree;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NodeConfig {

    @Bean
    public NodeService nodeService() {
        return new NodeServiceImpl();
    }

    @Bean
    public NodeMapper nodeMapper() {
        return new NodeMapperImpl();
    }
}
