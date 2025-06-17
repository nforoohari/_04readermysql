package ir.digixo.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

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

    @Bean("datasource1")
    @Primary
    public DataSource datasource1() {
        return dataSourceProperties1().initializeDataSourceBuilder().build();
    }

    @Bean("datasource2")
    public DataSource dataSource2() {
        return dataSourceProperties2().initializeDataSourceBuilder().build();
    }

    public Properties JPAProperties() {
        Properties props = new Properties();
        props.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
        props.put("hibernate.show_sql", true);
        props.put("hibernate.hbm2ddl.auto", "update");
        props.put("hibernate.format_sql", true);
        props.put("hibernate.use_sql_comments", true);
        props.put("hibernate.max_fetch_depth", 3);
        props.put("hibernate.jdbc.fetch_size", 50);
        props.put("hibernate.jdbc.batch_size", 500);
        return props;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        localContainerEntityManagerFactoryBean.setDataSource(dataSource2());
        localContainerEntityManagerFactoryBean.setPackagesToScan("p01Hibernate.entities");
        localContainerEntityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        localContainerEntityManagerFactoryBean.setJpaProperties(JPAProperties());
        return localContainerEntityManagerFactoryBean;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager(entityManagerFactory().getObject());
        transactionManager.setDataSource(dataSource2());
        transactionManager.setJpaDialect(new HibernateJpaDialect());
        return transactionManager;
    }
}
