package ca.uhn.fhir.jpa.util;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.jpa.config.HibernatePropertiesProvider;
import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.migrate.HapiMigrator;
import ca.uhn.fhir.jpa.migrate.tasks.HapiFhirJpaMigrationTasks;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.dialect.HapiFhirH2Dialect;
import ca.uhn.fhir.util.VersionEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PartitionedIdModeVerificationSvcTest {

	private static final String MIGRATION_TABLE_NAME = "hapi_migrator";

	private DriverTypeEnum.ConnectionProperties myConnectionProperties;

	@Mock
	private HibernatePropertiesProvider myHibernatePropertiesProvider;

	@ParameterizedTest
	@CsvSource(textBlock =
		// Partitioned Schema, Partitioned Settings, Uppercase Identifiers
		"""
			true,              true,                 true
			true,              false,                true
			false,             true,                 true
			false,             false,                true
			true,              true,                 false
			false,             false,                false
			"""
	)
	void testPartitionedIdDatabase_WantPartitionedIdDatabase(boolean thePartitionedIdModeForSchema, boolean thePartitionedIdModeForSettings, boolean theCapitalizedIdentifers) {
		myConnectionProperties = newConnection(theCapitalizedIdentifers);

		Set<String> commandLineValue = thePartitionedIdModeForSchema ? Set.of(HapiFhirJpaMigrationTasks.FlagEnum.DB_PARTITION_MODE.getCommandLineValue()) : Set.of();
		HapiFhirJpaMigrationTasks tasks = new HapiFhirJpaMigrationTasks(commandLineValue);

		HapiMigrator migrator = new HapiMigrator(MIGRATION_TABLE_NAME, myConnectionProperties.getDataSource(), DriverTypeEnum.H2_EMBEDDED);
		migrator.addTasks(tasks.getAllTasks(VersionEnum.values()));
		migrator.createMigrationTableIfRequired();
		migrator.migrate();

		PlatformTransactionManager txManager = new DataSourceTransactionManager(myConnectionProperties.getDataSource());
		when(myHibernatePropertiesProvider.getDataSource()).thenReturn(myConnectionProperties.getDataSource());
		when(myHibernatePropertiesProvider.getDialect()).thenReturn(new HapiFhirH2Dialect());

		PartitionSettings partitionedSettings = new PartitionSettings();
		partitionedSettings.setDatabasePartitionMode(thePartitionedIdModeForSettings);
		PartitionedIdModeVerificationSvc svc = new PartitionedIdModeVerificationSvc(partitionedSettings, myHibernatePropertiesProvider, txManager);

		if (thePartitionedIdModeForSchema == thePartitionedIdModeForSettings) {
			assertDoesNotThrow(svc::verifyPartitionedIdMode);
		} else {
			ConfigurationException ex = assertThrows(ConfigurationException.class, svc::verifyPartitionedIdMode);
			assertThat(ex.getMessage()).contains("System is configured in Partitioned ID mode but the database schema is not correct for this");
		}
	}

	/**
	 * Servide should not fail if there are no tables in the database yet - This means the
	 * migrator should set things up correctly.
	 */
	@Test
	void testEmptyDatabaseDoesNotFail() {
		myConnectionProperties = newConnection(true);

		PlatformTransactionManager txManager = new DataSourceTransactionManager(myConnectionProperties.getDataSource());
		when(myHibernatePropertiesProvider.getDataSource()).thenReturn(myConnectionProperties.getDataSource());
		when(myHibernatePropertiesProvider.getDialect()).thenReturn(new HapiFhirH2Dialect());

		PartitionSettings partitionedSettings = new PartitionSettings();
		PartitionedIdModeVerificationSvc svc = new PartitionedIdModeVerificationSvc(partitionedSettings, myHibernatePropertiesProvider, txManager);

		assertDoesNotThrow(svc::verifyPartitionedIdMode);
	}

	/**
	 * Create a new connection to a randomized H2 database for testing
	 */
	private DriverTypeEnum.ConnectionProperties newConnection(boolean theCapitalizedIdentifiers) {
		String url = "jdbc:h2:mem:test_migration-" + UUID.randomUUID();
		if (theCapitalizedIdentifiers) {
			url += ";CASE_INSENSITIVE_IDENTIFIERS=TRUE;DATABASE_TO_UPPER=TRUE;DATABASE_TO_LOWER=FALSE;";
		} else {
			url += ";CASE_INSENSITIVE_IDENTIFIERS=TRUE;DATABASE_TO_LOWER=TRUE;DATABASE_TO_UPPER=FALSE;";
		}

		//+ ";CASE_INSENSITIVE_IDENTIFIERS=TRUE;";
		return DriverTypeEnum.H2_EMBEDDED.newConnectionProperties(url, "SA", "SA");
	}
}
