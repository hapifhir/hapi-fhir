package ca.uhn.fhir.jpa.migrate.tasks.api;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;

import java.util.List;

public interface ISchemaInitializationProvider {
	List<String> getSqlStatements(DriverTypeEnum theDriverType);
}
