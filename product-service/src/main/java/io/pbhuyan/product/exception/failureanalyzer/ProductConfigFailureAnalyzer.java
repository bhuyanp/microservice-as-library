package io.pbhuyan.product.exception.failureanalyzer;

import io.pbhuyan.product.exception.InvalidProductConfigException;
import org.springframework.boot.diagnostics.AbstractFailureAnalyzer;
import org.springframework.boot.diagnostics.FailureAnalysis;

public class ProductConfigFailureAnalyzer extends AbstractFailureAnalyzer<InvalidProductConfigException> {

    @Override
    protected FailureAnalysis analyze(Throwable rootFailure, InvalidProductConfigException cause) {
        String action = """
                Please define following entries in your application yml file.
                               
                 spring:
                   datasource:
                     product:
                       url: <url>
                       username: <username>
                       password: <password>
                       driver-class-name: com.mysql.cj.jdbc.Driver
                """;
        return new FailureAnalysis("Product datasource is not configured properly.", action, cause);
    }
}
