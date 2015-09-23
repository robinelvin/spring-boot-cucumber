package com.robinelvin.sbc.config;

import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.server.InProcessServer;
import org.springframework.data.neo4j.server.Neo4jServer;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.data.neo4j.config.Neo4jConfiguration;

/**
 * @author Robin Elvin
 */
@Configuration
@EnableNeo4jRepositories(basePackages = "com.robinelvin.sbc.repositories")
@EnableTransactionManagement
@ComponentScan("com.robinelvin.sbc.models")
public class Neo4jTestConfiguration extends Neo4jConfiguration {
    @Override
    public SessionFactory getSessionFactory() {
        return new SessionFactory("com.robinelvin.sbc");
    }

    @Bean
    @Override
    public Neo4jServer neo4jServer() {
        return new InProcessServer();
    }

    @Override
    @Bean
    public Session getSession() throws Exception {
        return super.getSession();
    }
}