package ca.uhn.fhir.jpa.migrate;

import org.flywaydb.core.api.FlywayException;
import org.flywaydb.core.api.configuration.Configuration;
import org.flywaydb.core.internal.jdbc.JdbcConnectionFactory;

import javax.sql.DataSource;
import java.sql.Connection;

public class HapiMigrationJdbcConnectionFactory extends JdbcConnectionFactory {
	private final Connection myOnlyConnection;

	public HapiMigrationJdbcConnectionFactory(DataSource theDataSource, Configuration theConfiguration, Connection theOnlyConnection) {
		super(theDataSource, theConfiguration, null);
		myOnlyConnection = theOnlyConnection;
	}

	@Override
	public Connection openConnection() throws FlywayException {
		return myOnlyConnection;
	}
}
