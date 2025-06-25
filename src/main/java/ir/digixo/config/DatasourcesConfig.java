package ir.digixo.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DatasourcesConfig {

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties dataSourceProperties1() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("spring.uni")
    public DataSourceProperties dataSourceProperties2() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    public DataSource datasource1() {
        return dataSourceProperties1().initializeDataSourceBuilder().build();
    }

    @Bean
    public DataSource datasource2() {
        return dataSourceProperties2().initializeDataSourceBuilder().build();
    }

}
