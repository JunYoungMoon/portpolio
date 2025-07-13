package com.portfolio.portfolio.config;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(
        basePackages = {
                "com.portfolio.portfolio.nplus1.repository.jpa",
                "com.portfolio.portfolio.ddd.infrastructure.repository",
                "com.portfolio.portfolio.ai.infrastructure.repository",
                "com.portfolio.portfolio.giftorder.repository"
        },
        transactionManagerRef = "jpaTransactionManager"
)
@EnableR2dbcRepositories("com.portfolio.portfolio.pg.repository.r2dbc")
public class JpaConfig {

    @Value("${DB_HOST}")
    private String dbHost;

    @Value("${DB_NAME}")
    private String dbName;

    @Value("${DB_PASSWORD}")
    private String dbPassword;

    @Primary
    @Bean(name = "dataSource")
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(dbHost);
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUsername(dbName);
        dataSource.setPassword(dbPassword);
        return dataSource;
    }

    @Bean
    public EntityManagerFactoryBuilder entityManagerFactoryBuilder() {
        JpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        return new EntityManagerFactoryBuilder(
                jpaVendorAdapter,
                (dataSource) -> {
                    Map<String, Object> props = new HashMap<>();
                    props.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
                    props.put("hibernate.hbm2ddl.auto", "update");
                    props.put("hibernate.show_sql", true);
                    props.put("hibernate.format_sql", true);
                    return props;
                },
                null
        );
    }

    @Primary
    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder,
            DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages(
                        "com.portfolio.portfolio.nplus1.repository.jpa",
                        "com.portfolio.portfolio.ddd.domain.model.entity",
                        "com.portfolio.portfolio.ai.domain.model.entity",
                        "com.portfolio.portfolio.giftorder.entity"
                )
                .persistenceUnit("default")
                .build();
    }

    @Primary
    @Bean(name = "jpaTransactionManager")
    public PlatformTransactionManager jpaTransactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}