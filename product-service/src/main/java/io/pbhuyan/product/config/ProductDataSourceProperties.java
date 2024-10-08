package io.pbhuyan.product.config;


import io.micrometer.common.util.StringUtils;
import io.pbhuyan.product.exception.InvalidProductConfigException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@ConfigurationProperties(prefix = "spring.datasource.product")
public class ProductDataSourceProperties
        //extends DataSourceProperties
        implements InitializingBean {

    private String driverClassName;

    /**
     * JDBC URL of the database.
     */
    private String url;

    /**
     * Login username of the database.
     */
    private String username;

    /**
     * Login password of the database.
     */
    private String password;
    @Override
    public void afterPropertiesSet() {
        if (StringUtils.isBlank(getUrl())) {
            throw new InvalidProductConfigException();
        }
    }


}
