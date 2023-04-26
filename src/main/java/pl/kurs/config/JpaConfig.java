package pl.kurs.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackages = "pl.kurs.dao")
@PropertySource("classpath:application.properties")
public class JpaConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer getPSPC() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Profile({"dev", "prod", "!dev & !prod"})
    @Bean
    public LocalContainerEntityManagerFactoryBean getEMF(JpaVendorAdapter adapter, DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setPackagesToScan("pl.kurs.models");
        emf.setJpaVendorAdapter(adapter);
        emf.setDataSource(dataSource);
        return emf;
    }

    @Profile({"prod", "!dev & !prod"})
    @Bean
    public HibernateJpaVendorAdapter getVendorAdapterProd() {
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setDatabase(Database.MYSQL);
        adapter.setShowSql(true);
        adapter.setGenerateDdl(true);
        return adapter;
    }

    @Profile({"prod", "!dev & !prod"})
    @Bean
    public DataSource getDataSourceProd(Environment env) {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(env.getProperty("spring.datasource.mysql.url"));
        dataSource.setUsername(env.getProperty("spring.datasource.mysql.username"));
        dataSource.setPassword(env.getProperty("spring.datasource.mysql.password"));
        dataSource.setDriverClassName(env.getProperty("spring.datasource.mysql.driverClassName"));
        dataSource.setMinIdle(5);
        dataSource.setMaxIdle(100);
        return dataSource;
    }

    @Profile("dev")
    @Bean
    public HibernateJpaVendorAdapter getVendorAdapterDev() {
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setDatabase(Database.H2);
        adapter.setShowSql(true);
        adapter.setGenerateDdl(true);
        return adapter;
    }

    @Profile("dev")
    @Bean
    public DataSource getDataSourceDev(Environment env) {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(env.getProperty("spring.datasource.h2.url"));
        dataSource.setUsername(env.getProperty("spring.datasource.h2.username"));
        dataSource.setPassword(env.getProperty("spring.datasource.h2.password"));
        dataSource.setDriverClassName(env.getProperty("spring.datasource.h2.driverClassName"));
        dataSource.setMinIdle(5);
        dataSource.setMaxIdle(100);
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Profile("dev")
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

}
