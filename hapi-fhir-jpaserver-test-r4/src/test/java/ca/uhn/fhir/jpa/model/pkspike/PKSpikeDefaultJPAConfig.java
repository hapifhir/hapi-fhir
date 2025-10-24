package ca.uhn.fhir.jpa.model.pkspike;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.config.HapiFhirHibernateJpaDialect;
import ca.uhn.fhir.jpa.config.HapiFhirLocalContainerEntityManagerFactoryBean;
import ca.uhn.fhir.jpa.model.dialect.HapiFhirH2Dialect;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManagerFactory;
import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.persistenceunit.PersistenceManagedTypes;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.time.Duration;
import java.util.Properties;
import java.util.function.Consumer;

@Configuration
public class PKSpikeDefaultJPAConfig {

	@Inject
	FhirContext myFhirContext;

	@Bean
	DataSource datasource() {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriver(new org.h2.Driver());
		dataSource.setUrl("jdbc:h2:mem:testdb_r4" + System.currentTimeMillis());
		dataSource.setMaxWait(Duration.ofMillis(30000));
		dataSource.setUsername("");
		dataSource.setPassword("");
		dataSource.setMaxTotal(10);

		SchemaInit.initSchema(dataSource);

		return dataSource;
	}


	@Bean
	public HapiFhirLocalContainerEntityManagerFactoryBean entityManagerFactory(
//			ModuleMigrationMetadata theModuleMigrationMetadata,
		ConfigurableListableBeanFactory theConfigurableListableBeanFactory,
		DataSource theDataSource,
		PersistenceManagedTypes theManagedTypes,
		@Autowired(required = false) @Nullable Consumer<HapiFhirLocalContainerEntityManagerFactoryBean> theEntityManagerFactoryCustomizer) {

		HapiFhirLocalContainerEntityManagerFactoryBean entityManagerFactoryBean =
			new HapiFhirLocalContainerEntityManagerFactoryBean(theConfigurableListableBeanFactory);

		entityManagerFactoryBean.setJpaDialect(new HapiFhirHibernateJpaDialect(myFhirContext.getLocalizer()));
		HibernatePersistenceProvider persistenceProvider = new HibernatePersistenceProvider();
		entityManagerFactoryBean.setPersistenceProvider(persistenceProvider);
		Properties jpaProperties = new Properties();
		jpaProperties.put("hibernate.search.enabled", "false");
		jpaProperties.put("hibernate.format_sql", "false");
		jpaProperties.put("hibernate.show_sql", "false");
		jpaProperties.put("hibernate.integration.envers.enabled=false", "false");
		jpaProperties.put("hibernate.hbm2ddl.auto", "none");
		jpaProperties.put("hibernate.dialect", HapiFhirH2Dialect.class.getName());
		entityManagerFactoryBean.setJpaProperties(jpaProperties);

		entityManagerFactoryBean.setPersistenceUnitName("HapiPU");
		entityManagerFactoryBean.setDataSource(theDataSource);
		entityManagerFactoryBean.setManagedTypes(theManagedTypes);

		if (theEntityManagerFactoryCustomizer != null) {
			theEntityManagerFactoryCustomizer.accept(entityManagerFactoryBean);
		}

		return entityManagerFactoryBean;
	}

	@Bean
	public PlatformTransactionManager transactionManager(EntityManagerFactory theEntityManagerFactory) {
		JpaTransactionManager retVal = new JpaTransactionManager();
		retVal.setEntityManagerFactory(theEntityManagerFactory);
		return retVal;
	}


	@Bean
	public TransactionTemplate transactionTemplate(PlatformTransactionManager theTransactionManager) {
		return new TransactionTemplate(theTransactionManager);
	}

	@Bean
	public JdbcTemplate jdbcTemplate(DataSource theDataSource) {
		return new JdbcTemplate(theDataSource);
	}

	@Bean
	@Nonnull
	JpaStorageSettings storageSettings() {
		JpaStorageSettings jpaStorageSettings = new JpaStorageSettings();
		return jpaStorageSettings;
	}
}
