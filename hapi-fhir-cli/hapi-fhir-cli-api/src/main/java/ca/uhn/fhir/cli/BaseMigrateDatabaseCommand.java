package ca.uhn.fhir.cli;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.migrate.Migrator;
import ca.uhn.fhir.util.VersionEnum;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public abstract class BaseMigrateDatabaseCommand extends BaseCommand {
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

		addOptionalOption(retVal, "r", "dry-run", false, "Log the SQL statements that would be executed but to not actually make any changes");

		addRequiredOption(retVal,"u", "url", "URL", "The JDBC database URL");
		addRequiredOption(retVal,"n", "username", "Username", "The JDBC database username");
		addRequiredOption(retVal,"p", "password", "Password", "The JDBC database password");
		addRequiredOption(retVal,"f", "from", "Version", "The database schema version to migrate FROM");
		addRequiredOption(retVal,"t", "to", "Version", "The database schema version to migrate TO");
		addRequiredOption(retVal,"d", "driver", "Driver", "The database driver to use (Options are " + driverOptions() + ")");

		return retVal;
	}

	private String driverOptions() {
		return Arrays.stream(DriverTypeEnum.values()).map(Enum::name).collect(Collectors.joining(", "));
	}

	@Override
	public List<String> provideUsageNotes() {
		String versions = "The following versions are supported: " +
			Arrays.stream(VersionEnum.values()).map(Enum::name).collect(Collectors.joining(", "));
		return Collections.singletonList(versions);
	}

	@Override
	public void run(CommandLine theCommandLine) throws ParseException, ExecutionException {

		String url = theCommandLine.getOptionValue("u");
		String username = theCommandLine.getOptionValue("n");
		String password = theCommandLine.getOptionValue("p");
		DriverTypeEnum driverType;
		String driverTypeString = theCommandLine.getOptionValue("d");
		try {
			driverType = DriverTypeEnum.valueOf(driverTypeString);
		} catch (Exception e) {
			throw new ParseException("Invalid driver type \"" + driverTypeString + "\". Valid values are: " + driverOptions());
		}

		VersionEnum from = parseVersion(theCommandLine, "f", "from");
		VersionEnum to = parseVersion(theCommandLine, "t", "to");

		boolean dryRun = theCommandLine.hasOption("r");

		Migrator migrator = new Migrator();
		migrator.setConnectionUrl(url);
		migrator.setDriverType(driverType);
		migrator.setUsername(username);
		migrator.setPassword(password);
		migrator.setDryRun(dryRun);
		addTasks(migrator, from, to);

		migrator.migrate();
	}

	@NotNull
	private VersionEnum parseVersion(CommandLine theCommandLine, String theOptionName, String theOptionDesc) throws ParseException {
		VersionEnum from;
		String optionValue = theCommandLine.getOptionValue(theOptionName);
		try {
			from = VersionEnum.valueOf(optionValue);
		} catch (Exception e) {
			throw new ParseException("Invalid " + theOptionDesc+ " value: " + optionValue);
		}
		return from;
	}

	protected abstract void addTasks(Migrator theMigrator, VersionEnum theFrom, VersionEnum theTo);
}
