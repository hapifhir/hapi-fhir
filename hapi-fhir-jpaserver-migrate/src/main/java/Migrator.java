package ca.uhn.fhir.jpa.migrate;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.Location;
import org.flywaydb.core.api.configuration.Configuration;
import org.flywaydb.core.api.configuration.FluentConfiguration;

public class Migrator {

	public static void main(String[] theArgs) {
		Configuration config = new FluentConfiguration()
			.locations(new Location());
		new Flyway(config);
	}

}
