package ca.uhn.fhir.jpa.model.pkspike;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.config.HapiFhirLocalContainerEntityManagerFactoryBean;
import ca.uhn.fhir.jpa.config.r4.FhirContextR4Config;
import ca.uhn.fhir.jpa.config.util.HapiEntityManagerFactoryUtil;
import ca.uhn.fhir.jpa.model.dialect.HapiFhirH2Dialect;
import jakarta.annotation.Nonnull;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManagerFactory;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Spike to assess variable binding against a db.
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
	JpaBindingTest.Config.class, FhirContextR4Config.class
})
public class JpaBindingTest {
	private static final Logger ourLog = LoggerFactory.getLogger(JpaBindingTest.class);

	@Configuration
	static class Config {

		@Inject
		FhirContext myFhirContext;

		@Bean
		DataSource datasource() {
			BasicDataSource dataSource = new BasicDataSource();
			dataSource.setDriver(new org.h2.Driver());
			dataSource.setUrl("jdbc:h2:mem:testdb_r4");
			dataSource.setMaxWaitMillis(30000);
			dataSource.setUsername("");
			dataSource.setPassword("");
			dataSource.setMaxTotal(10);

			return dataSource;
		}


		@Bean
		public HapiFhirLocalContainerEntityManagerFactoryBean entityManagerFactory(
//			ModuleMigrationMetadata theModuleMigrationMetadata,
			ConfigurableListableBeanFactory theConfigurableListableBeanFactory,
			DataSource theDataSource) {
			HapiFhirLocalContainerEntityManagerFactoryBean retVal =
				new HapiFhirLocalContainerEntityManagerFactoryBean(theConfigurableListableBeanFactory);

			HapiEntityManagerFactoryUtil.configureEntityManagerFactory(retVal, myFhirContext, storageSettings());
			Properties jpaProperties = new Properties();
			jpaProperties.put("hibernate.search.enabled", "false");
			jpaProperties.put("hibernate.format_sql", "false");
			jpaProperties.put("hibernate.show_sql", "false");
			jpaProperties.put("hibernate.hbm2ddl.auto", "update");
			jpaProperties.put("hibernate.dialect", HapiFhirH2Dialect.class.getName());

			retVal.setPersistenceUnitName("HapiPU");
			retVal.setDataSource(theDataSource);
			//retVal.setManagedTypes(PersistenceManagedTypes.of(ResourceTable.class.getName()));

			retVal.setJpaProperties(jpaProperties);

			return retVal;
		}

		@Bean
		@Nonnull JpaStorageSettings storageSettings() {
			JpaStorageSettings jpaStorageSettings = new JpaStorageSettings();
			//jpaStorageSettings.setAdvancedHSearchIndexing();
			return jpaStorageSettings;
		}
	}

	@Inject
	DataSource myDataSource;

	@Inject
	EntityManagerFactory myEntityManagerFactory;

	@Test
	void createResourceTable_roundTrip() throws SQLException {
		ourLog.info("starting");
	    // given

		var t = new JdbcTemplate(myDataSource);

		t.execute(" create table table_a (col_a numeric, col_b varchar)");
		int rows = t.update("insert into table_a values (?, ?)", 22, "foo");
		assertEquals(1, rows);

		ourLog.info("done");

		try(var em = myEntityManagerFactory.createEntityManager()) {
			// empty
		}
	}
}
