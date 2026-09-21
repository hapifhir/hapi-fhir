package ca.uhn.fhir.jpa.model.dialect;

import ca.uhn.fhir.jpa.util.ISequenceValueMassager;
import ca.uhn.hapi.fhir.sql.hibernatesvc.HapiHibernateDialectSettingsService;
import ca.uhn.hapi.fhir.sql.hibernatesvc.OracleIOT;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.model.relational.SqlStringGenerationContext;
import org.hibernate.boot.model.relational.internal.SqlStringGenerationContextImpl;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.JdbcSettings;
import org.hibernate.cfg.SchemaToolingSettings;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.hibernate.mapping.PersistentClass;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class HapiFhirOracleTableExporterTest {

	@Test
	void getSqlCreateStrings_nonPartitionedMode_addsCompress1() {
		String ddl = generateCreateTableDdl(TestIotEntity.class, false);

		assertThat(ddl).contains("ORGANIZATION INDEX COMPRESS 1");
	}

	@Test
	void getSqlCreateStrings_partitionedMode_addsCompress2() {
		String ddl = generateCreateTableDdl(TestIotEntity.class, true);

		assertThat(ddl).contains("ORGANIZATION INDEX COMPRESS 2");
	}

	@Test
	void getSqlCreateStrings_nonIotEntity_noOrganizationIndex() {
		String ddl = generateCreateTableDdl(TestNonIotEntity.class, false);

		assertThat(ddl).doesNotContain("ORGANIZATION INDEX");
	}

	@Test
	void getSqlCreateStrings_zeroCompression_noCompressClause() {
		String ddl = generateCreateTableDdl(TestZeroCompressEntity.class, false);

		assertThat(ddl).contains("ORGANIZATION INDEX").doesNotContain("INDEX COMPRESS");
	}

	// Bootstraps a minimal Hibernate environment with a single entity to produce Oracle CREATE TABLE DDL
	private String generateCreateTableDdl(Class<?> theEntityClass, boolean thePartitionMode) {
		HapiHibernateDialectSettingsService settingsService = new HapiHibernateDialectSettingsService();
		settingsService.setDatabasePartitionMode(thePartitionMode);

		StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
				.applySetting(SchemaToolingSettings.HBM2DDL_AUTO, "create")
				.applySetting(JdbcSettings.ALLOW_METADATA_ON_BOOT, false)
				.applySetting(JdbcSettings.DIALECT, HapiFhirOracleDialect.class.getName())
				.addService(ISequenceValueMassager.class, new ISequenceValueMassager.NoopSequenceValueMassager())
				.addService(HapiHibernateDialectSettingsService.class, settingsService)
				.build();

		MetadataSources sources = new MetadataSources(registry);
		sources.addAnnotatedClass(theEntityClass);
		Metadata metadata = sources.buildMetadata();

		HapiFhirOracleDialect dialect = (HapiFhirOracleDialect) metadata
				.getDatabase()
				.getDialect();

		PersistentClass pc = metadata.getEntityBindings().iterator().next();
		org.hibernate.mapping.Table table = pc.getTable();

		JdbcEnvironment jdbcEnv = registry.getService(JdbcEnvironment.class);
		SqlStringGenerationContext context = SqlStringGenerationContextImpl.fromConfigurationMap(
				jdbcEnv, metadata.getDatabase(), Collections.emptyMap());

		String[] ddl = dialect.getTableExporter().getSqlCreateStrings(table, metadata, context);
		return ddl[0];
	}

	// Define minimal test entities to test DDL generation

	@OracleIOT(nonPartitionedCompressionLevel = 1, partitionedCompressionLevel = 2)
	@Entity
	@Table(name = "TEST_IOT_TABLE")
	static class TestIotEntity {
		@Id
		@Column(name = "PID")
		private Long myPid;
	}

	@Entity
	@Table(name = "TEST_NON_IOT_TABLE")
	static class TestNonIotEntity {
		@Id
		@Column(name = "PID")
		private Long myPid;
	}

	@OracleIOT(nonPartitionedCompressionLevel = 0, partitionedCompressionLevel = 0)
	@Entity
	@Table(name = "TEST_ZERO_COMPRESS")
	static class TestZeroCompressEntity {
		@Id
		@Column(name = "PID")
		private Long myPid;
	}
}
