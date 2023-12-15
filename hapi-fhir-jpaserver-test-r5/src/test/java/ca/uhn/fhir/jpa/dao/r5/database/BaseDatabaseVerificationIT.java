package ca.uhn.fhir.jpa.dao.r5.database;

import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.embedded.JpaEmbeddedDatabase;
import ca.uhn.fhir.jpa.migrate.HapiMigrationStorageSvc;
import ca.uhn.fhir.jpa.migrate.MigrationTaskList;
import ca.uhn.fhir.jpa.migrate.SchemaMigrator;
import ca.uhn.fhir.jpa.migrate.dao.HapiMigrationDao;
import ca.uhn.fhir.jpa.migrate.tasks.HapiFhirJpaMigrationTasks;
import ca.uhn.fhir.jpa.test.config.TestR5Config;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.util.VersionEnum;
import jakarta.persistence.EntityManagerFactory;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r5.model.Patient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.util.Properties;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@EnableJpaRepositories(repositoryFactoryBeanClass = EnversRevisionRepositoryFactoryBean.class)
@ContextConfiguration(classes = {BaseDatabaseVerificationIT.TestConfig.class})
public abstract class BaseDatabaseVerificationIT {
	private static final Logger ourLog = LoggerFactory.getLogger(BaseDatabaseVerificationIT.class);
	private static final String MIGRATION_TABLENAME = "MIGRATIONS";

	@Autowired
	EntityManagerFactory myEntityManagerFactory;

	@Autowired
	JpaEmbeddedDatabase myJpaEmbeddedDatabase;

	@Autowired
	IFhirResourceDao<Patient> myPatientDao;


	@ParameterizedTest
	@ValueSource(ints = {10, 100000})
	public void testCreateRead(int theSize) {
		String name = StringUtils.leftPad("", theSize, "a");

		Patient patient = new Patient();
		patient.setActive(true);
		patient.addName().setFamily(name);
		IIdType id = myPatientDao.create(patient, new SystemRequestDetails()).getId();

		Patient actual = myPatientDao.read(id, new SystemRequestDetails());
		assertEquals(name, actual.getName().get(0).getFamily());
	}


	@Test
	public void testDelete() {
		Patient patient = new Patient();
		patient.setActive(true);
		IIdType id = myPatientDao.create(patient, new SystemRequestDetails()).getId().toUnqualifiedVersionless();

		myPatientDao.delete(id, new SystemRequestDetails());

		assertThrows(ResourceGoneException.class, () -> myPatientDao.read(id, new SystemRequestDetails()));
	}


	@Configuration
	public static class TestConfig extends TestR5Config {

		@Autowired
		private JpaDatabaseContextConfigParamObject myJpaDatabaseContextConfigParamObject;

		@Override
		@Bean
		public DataSource dataSource() {
			DataSource dataSource = myJpaDatabaseContextConfigParamObject.getJpaEmbeddedDatabase().getDataSource();

			HapiMigrationDao hapiMigrationDao = new HapiMigrationDao(dataSource, myJpaDatabaseContextConfigParamObject.getJpaEmbeddedDatabase().getDriverType(), MIGRATION_TABLENAME);
			HapiMigrationStorageSvc hapiMigrationStorageSvc = new HapiMigrationStorageSvc(hapiMigrationDao);

			MigrationTaskList tasks = new HapiFhirJpaMigrationTasks(Set.of()).getAllTasks(VersionEnum.values());

			SchemaMigrator schemaMigrator = new SchemaMigrator(
				"HAPI FHIR", MIGRATION_TABLENAME, dataSource, new Properties(), tasks, hapiMigrationStorageSvc);
			schemaMigrator.setDriverType(myJpaDatabaseContextConfigParamObject.getJpaEmbeddedDatabase().getDriverType());

			ourLog.info("About to run migration...");
			schemaMigrator.createMigrationTableIfRequired();
			schemaMigrator.migrate();
			ourLog.info("Migration complete");


			return dataSource;
		}

		@Bean
		public JpaEmbeddedDatabase jpaEmbeddedDatabase(JpaDatabaseContextConfigParamObject theJpaDatabaseContextConfigParamObject) {
			return theJpaDatabaseContextConfigParamObject.getJpaEmbeddedDatabase();
		}

		@Override
		protected Properties jpaProperties() {
			Properties retVal = super.jpaProperties();
			retVal.put("hibernate.hbm2ddl.auto", "none");
			retVal.put("hibernate.dialect", myJpaDatabaseContextConfigParamObject.getDialect());
			return retVal;
		}

	}

	public static class JpaDatabaseContextConfigParamObject {
		private JpaEmbeddedDatabase myJpaEmbeddedDatabase;
		private String myDialect;

		public JpaDatabaseContextConfigParamObject(JpaEmbeddedDatabase theJpaEmbeddedDatabase, String theDialect) {
			myJpaEmbeddedDatabase = theJpaEmbeddedDatabase;
			myDialect = theDialect;
		}

		public JpaEmbeddedDatabase getJpaEmbeddedDatabase() {
			return myJpaEmbeddedDatabase;
		}

		public String getDialect() {
			return myDialect;
		}
	}


}


