/*-
 * #%L
 * HAPI FHIR - Command Line Client - API
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.cli;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.migrate.HapiMigrator;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 *
 */
public abstract class BaseClearMigrationLockCommand extends BaseCommand {

	public static final String CLEAR_LOCK = "clear-migration-lock";
	private String myMigrationTableName;

	@Override
	public String getCommandDescription() {
		return "This command clears a database migration lock";
	}

	@Override
	public String getCommandName() {
		return CLEAR_LOCK;
	}

	@Override
	public Options getOptions() {
		Options retVal = new Options();
		addRequiredOption(retVal, "u", "url", "URL", "The JDBC database URL");
		addRequiredOption(retVal, "n", "username", "Username", "The JDBC database username");
		addRequiredOption(retVal, "p", "password", "Password", "The JDBC database password");
		addRequiredOption(
				retVal, "d", "driver", "Driver", "The database driver to use (Options are " + driverOptions() + ")");
		addRequiredOption(retVal, "l", "lock-uuid", "Lock UUID", "The UUID value of the lock held in the database.");
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
		String lockUUID = theCommandLine.getOptionValue("l");
		DriverTypeEnum driverType;
		String driverTypeString = theCommandLine.getOptionValue("d");
		try {
			driverType = DriverTypeEnum.valueOf(driverTypeString);
		} catch (Exception e) {
			throw new ParseException(Msg.code(2774) + "Invalid driver type \"" + driverTypeString
					+ "\". Valid values are: " + driverOptions());
		}

		DriverTypeEnum.ConnectionProperties connectionProperties =
				driverType.newConnectionProperties(url, username, password);
		HapiMigrator migrator =
				new HapiMigrator(myMigrationTableName, connectionProperties.getDataSource(), driverType);
		migrator.clearMigrationLockWithUUID(lockUUID);
	}

	protected void setMigrationTableName(String theMigrationTableName) {
		myMigrationTableName = theMigrationTableName;
	}
}
