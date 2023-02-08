package ca.uhn.fhir.cli;

import ca.uhn.fhir.jpa.migrate.SchemaMigrator;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;

public class HapiClearMigrationLockCommand extends BaseClearMigrationLockCommand {
	@Override
	public void run(CommandLine theCommandLine) throws ParseException {
		setMigrationTableName(SchemaMigrator.HAPI_FHIR_MIGRATION_TABLENAME);
		super.run(theCommandLine);
	}
}
