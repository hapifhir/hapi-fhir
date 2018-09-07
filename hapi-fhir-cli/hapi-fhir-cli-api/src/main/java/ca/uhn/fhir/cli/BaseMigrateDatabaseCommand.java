package ca.uhn.fhir.cli;

import ca.uhn.fhir.util.VersionEnum;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class BaseMigrateDatabaseCommand extends BaseCommand {
	@Override
	public String getCommandDescription() {
		return "This command migrates a HAPI FHIR JPA database from one version of HAPI FHIR to a newer version";
	}

	@Override
	public String getCommandName() {
		return "migrate-database";
	}

	@Override
	public Options getOptions() {
		Options retVal = new Options();

		addRequiredOption(retVal,"u", "url", "URL", "The JDBC database URL");
		addRequiredOption(retVal,"n", "username", "Username", "The JDBC database username");
		addRequiredOption(retVal,"p", "password", "Password", "The JDBC database password");
		addRequiredOption(retVal,"f", "from", "Version", "The database schema version to migrate FROM");
		addRequiredOption(retVal,"t", "to", "Version", "The database schema version to migrate TO");

		return retVal;
	}

	@Override
	public List<String> provideUsageNotes() {
		String versions = "The following versions are supported: " +
			Arrays.stream(VersionEnum.values()).map(Enum::name).collect(Collectors.joining(", "));
		return Collections.singletonList(versions);
	}

	@Override
	public void run(CommandLine theCommandLine) throws ParseException, ExecutionException {

	}
}
