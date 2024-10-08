package io.pbhuyan.product.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "io.pbhuyan.product",
        entityManagerFactoryRef = "productEntityManagerFactory",
        transactionManagerRef = "productTransactionManager"
)
@RequiredArgsConstructor
@EnableConfigurationProperties(ProductDataSourceProperties.class)
public class ProductAutoConfiguration {
    private final ProductDataSourceProperties productDataSourceProperties;

    @Bean
    @ConfigurationProperties("spring.datasource.product")
    public DataSourceProperties productDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    //Unfortunately, to get EntityManagerFactoryBuilder injected,
    // we need to declare one of the data sources as @Primary.
    public DataSource productDataSource() {
        return DataSourceBuilder.create()
                .url(productDataSourceProperties.getUrl())
                .username(productDataSourceProperties.getUsername())
                .password(productDataSourceProperties.getPassword())
                .driverClassName(productDataSourceProperties.getDriverClassName()).build();
    }


    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean productEntityManagerFactory(
            @Qualifier("productDataSource") DataSource productDataSource,
            EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(productDataSource)
                .packages("io.pbhuyan.product.entity")
                .build();
    }

    @Bean
    public PlatformTransactionManager productTransactionManager(
            @Qualifier("productEntityManagerFactory") LocalContainerEntityManagerFactoryBean productEntityManagerFactory) {
        return new JpaTransactionManager(Objects.requireNonNull(productEntityManagerFactory.getObject()));
    }
}
