package ca.uhn.fhir.jpa.migrate.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@EnableJpaRepositories(basePackages = {"ca.uhn.fhir.jpa.migrate.dao"})
@EnableTransactionManagement
public class TestMigrationConfig {
	private static final Logger ourLog = LoggerFactory.getLogger(TestMigrationConfig.class);

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource theDataSource) {

		LocalContainerEntityManagerFactoryBean retVal = new LocalContainerEntityManagerFactoryBean();
		retVal.setPackagesToScan("ca.uhn.fhir.jpa.migrate.entity");
		retVal.setPersistenceProvider(new HibernatePersistenceProvider());
		retVal.setPersistenceUnitName("PU_Migration");
		retVal.setDataSource(theDataSource);
		retVal.setJpaProperties(jpaProperties());
		return retVal;
	}

	@Bean
	public DataSource dataSource() {
		BasicDataSource retVal = new BasicDataSource();
		retVal.setDriver(new org.h2.Driver());
		retVal.setUrl("jdbc:h2:mem:test_migration");
		retVal.setMaxWaitMillis(30000);
		retVal.setUsername("");
		retVal.setPassword("");
		retVal.setMaxTotal(5);

		return retVal;
	}

	private Properties jpaProperties() {
		Properties extraProperties = new Properties();
		extraProperties.put("hibernate.search.enabled", "false");
		extraProperties.put("hibernate.format_sql", "false");
		extraProperties.put("hibernate.show_sql", "false");
		extraProperties.put("hibernate.hbm2ddl.auto", "update");
		extraProperties.put("hibernate.dialect", H2Dialect.class.getName());

		ourLog.info("jpaProperties: {}", extraProperties);

		return extraProperties;
	}

	@Bean
	public PlatformTransactionManager transactionManager(LocalContainerEntityManagerFactoryBean theEntityManagerFactory) {
		JpaTransactionManager retVal = new JpaTransactionManager();
		retVal.setEntityManagerFactory(theEntityManagerFactory.getObject());
		return retVal;
	}
}
