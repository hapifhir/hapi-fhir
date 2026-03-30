package ca.uhn.fhir.jpa.dao.r5.database;

import ca.uhn.fhir.jpa.embedded.H2EmbeddedDatabase;
import ca.uhn.fhir.jpa.embedded.MsSqlEmbeddedDatabase;
import ca.uhn.fhir.jpa.embedded.Oracle21EmbeddedDatabase;
import ca.uhn.fhir.jpa.embedded.Oracle23EmbeddedDatabase;
import ca.uhn.fhir.jpa.embedded.PostgresEmbeddedDatabase;
import ca.uhn.fhir.jpa.model.dialect.HapiFhirH2Dialect;
import ca.uhn.fhir.jpa.model.dialect.HapiFhirOracleDialect;
import ca.uhn.fhir.jpa.model.dialect.HapiFhirPostgresDialect;
import ca.uhn.fhir.jpa.model.dialect.HapiFhirSQLServerDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class EmbeddedDatabaseConfigurations {

	private EmbeddedDatabaseConfigurations(){

	}

	@Configuration
	public static class MsSql {
		@Bean
		public JpaDatabaseContextConfigParamObject jpaDatabaseParamObject() {
			return new JpaDatabaseContextConfigParamObject(
				new MsSqlEmbeddedDatabase(),
				HapiFhirSQLServerDialect.class.getName()
			);
		}
	}

	@Configuration
	public static class Postgres {
		@Bean
		public JpaDatabaseContextConfigParamObject jpaDatabaseParamObject() {
			return new JpaDatabaseContextConfigParamObject(
				new PostgresEmbeddedDatabase(),
				HapiFhirPostgresDialect.class.getName()
			);
		}
	}

	@Configuration
	public static class H2 {
		@Bean
		public JpaDatabaseContextConfigParamObject jpaDatabaseParamObject() {
			return new JpaDatabaseContextConfigParamObject(
				new H2EmbeddedDatabase(),
				HapiFhirH2Dialect.class.getName()
			);
		}
	}

	@Configuration
	public static class Oracle21 {
		@Bean
		public JpaDatabaseContextConfigParamObject jpaDatabaseParamObject() {
			return new JpaDatabaseContextConfigParamObject(
				new Oracle21EmbeddedDatabase(),
				HapiFhirOracleDialect.class.getName()
			);
		}
	}

	@Configuration
	public static class Oracle23 {
		@Bean
		public JpaDatabaseContextConfigParamObject jpaDatabaseParamObject() {
			return new JpaDatabaseContextConfigParamObject(
				new Oracle23EmbeddedDatabase(),
				HapiFhirOracleDialect.class.getName()
			);
		}
	}
}
