package ca.uhn.fhir.cli;

/*-
 * #%L
 * HAPI FHIR - Command Line Client - API
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.migrate.BaseMigrator;
import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.migrate.FlywayMigrator;
import ca.uhn.fhir.jpa.migrate.MigrationTaskSkipper;
import ca.uhn.fhir.jpa.migrate.TaskOnlyMigrator;
import ca.uhn.fhir.jpa.migrate.taskdef.BaseTask;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.defaultString;

/**
 * NB since 2019-12-05: This class is kind of weirdly named now, since it can either use Flyway or not use Flyway
 */
public abstract class BaseFlywayMigrateDatabaseCommand<T extends Enum> extends BaseCommand {
	private static final Logger ourLog = LoggerFactory.getLogger(BaseFlywayMigrateDatabaseCommand.class);

	public static final String MIGRATE_DATABASE = "migrate-database";
	public static final String NO_COLUMN_SHRINK = "no-column-shrink";
	public static final String DONT_USE_FLYWAY = "dont-use-flyway";
	public static final String STRICT_ORDER = "strict-order";
	public static final String SKIP_VERSIONS = "skip-versions";
	private Set<String> myFlags;
	private String myMigrationTableName;

	protected Set<String> getFlags() {
		return myFlags;
	}

	@Override
	public String getCommandDescription() {
		return "This command migrates a HAPI FHIR JPA database to the current version";
	}

	protected abstract List<T> provideAllowedVersions();

	protected abstract Class<T> provideVersionEnumType();

	@Override
	public String getCommandName() {
		return MIGRATE_DATABASE;
	}

	@Override
	public Options getOptions() {
		Options retVal = new Options();

		addOptionalOption(retVal, "r", "dry-run", false, "Log the SQL statements that would be executed but to not actually make any changes");
		addRequiredOption(retVal, "u", "url", "URL", "The JDBC database URL");
		addRequiredOption(retVal, "n", "username", "Username", "The JDBC database username");
		addRequiredOption(retVal, "p", "password", "Password", "The JDBC database password");
		addRequiredOption(retVal, "d", "driver", "Driver", "The database driver to use (Options are " + driverOptions() + ")");
		addOptionalOption(retVal, "x", "flags", "Flags", "A comma-separated list of any specific migration flags (these flags are version specific, see migrator documentation for details)");
		addOptionalOption(retVal, null, DONT_USE_FLYWAY, false, "If this option is set, the migrator will not use FlywayDB for migration. This setting should only be used if you are trying to migrate a legacy database platform that is not supported by FlywayDB.");
		addOptionalOption(retVal, null, STRICT_ORDER, false, "If this option is set, the migrator will require migration tasks to be performed in order.");
		addOptionalOption(retVal, null, NO_COLUMN_SHRINK, false, "If this flag is set, the system will not attempt to reduce the length of columns. This is useful in environments with a lot of existing data, where shrinking a column can take a very long time.");
		addOptionalOption(retVal, null, SKIP_VERSIONS, "Versions", "A comma separated list of schema versions to skip.  E.g. 4_1_0.20191214.2,4_1_0.20191214.4");

		return retVal;
	}

	private String driverOptions() {
		return Arrays.stream(DriverTypeEnum.values()).map(Enum::name).collect(Collectors.joining(", "));
	}

	@Override
	public void run(CommandLine theCommandLine) throws ParseException {

		String url = theCommandLine.getOptionValue("u");
		String username = theCommandLine.getOptionValue("n");
		String password = theCommandLine.getOptionValue("p");
		DriverTypeEnum driverType;
		String driverTypeString = theCommandLine.getOptionValue("d");
		try {
			driverType = DriverTypeEnum.valueOf(driverTypeString);
		} catch (Exception e) {
			throw new ParseException(Msg.code(1535) + "Invalid driver type \"" + driverTypeString + "\". Valid values are: " + driverOptions());
		}

		boolean dryRun = theCommandLine.hasOption("r");
		boolean noColumnShrink = theCommandLine.hasOption(BaseFlywayMigrateDatabaseCommand.NO_COLUMN_SHRINK);

		String flags = theCommandLine.getOptionValue("x");
		myFlags = Arrays.stream(defaultString(flags).split(","))
			.map(String::trim)
			.filter(StringUtils::isNotBlank)
			.collect(Collectors.toSet());

		boolean dontUseFlyway = theCommandLine.hasOption(BaseFlywayMigrateDatabaseCommand.DONT_USE_FLYWAY);
		boolean strictOrder = theCommandLine.hasOption(BaseFlywayMigrateDatabaseCommand.STRICT_ORDER);

		BaseMigrator migrator;
		if (dontUseFlyway || dryRun) {
			// Flyway dryrun is not available in community edition
			migrator = new TaskOnlyMigrator();
		} else {
			migrator = new FlywayMigrator(myMigrationTableName);
		}

		DriverTypeEnum.ConnectionProperties connectionProperties = driverType.newConnectionProperties(url, username, password);

		migrator.setDataSource(connectionProperties.getDataSource());
		migrator.setDriverType(driverType);
		migrator.setDryRun(dryRun);
		migrator.setNoColumnShrink(noColumnShrink);
		migrator.setStrictOrder(strictOrder);
		String skipVersions = theCommandLine.getOptionValue(BaseFlywayMigrateDatabaseCommand.SKIP_VERSIONS);
		addTasks(migrator, skipVersions);
		migrator.migrate();
	}

	protected abstract void addTasks(BaseMigrator theMigrator, String theSkippedVersions);

	public void setMigrationTableName(String theMigrationTableName) {
		myMigrationTableName = theMigrationTableName;
	}

	protected void setDoNothingOnSkippedTasks(Collection<BaseTask> theTasks, String theSkipVersions) {
		MigrationTaskSkipper.setDoNothingOnSkippedTasks(theTasks, theSkipVersions);
	}
}
