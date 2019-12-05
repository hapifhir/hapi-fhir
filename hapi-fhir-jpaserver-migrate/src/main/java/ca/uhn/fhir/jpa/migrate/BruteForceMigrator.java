package ca.uhn.fhir.jpa.migrate;

import ca.uhn.fhir.jpa.migrate.taskdef.BaseTask;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.flywaydb.core.api.MigrationInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This class is an alternative to {@link FlywayMigrator). It doesn't use Flyway, but instead just
 * executes all tasks.
 */
public class BruteForceMigrator extends BaseMigrator {

	private static final Logger ourLog = LoggerFactory.getLogger(BruteForceMigrator.class);
	private List<BaseTask<?>> myTasks = new ArrayList<>();

	@Override
	public void migrate() {
		for (BaseTask<?> next : myTasks) {
			next.setDriverType(getDriverType());
			next.setDryRun(isDryRun());
			next.setNoColumnShrink(isNoColumnShrink());
			DriverTypeEnum.ConnectionProperties connectionProperties = getDriverType().newConnectionProperties(getConnectionUrl(), getUsername(), getPassword());
			next.setConnectionProperties(connectionProperties);

			try {
				ourLog.info("Executing task of type: {}", next.getClass().getSimpleName());
				next.execute();
			} catch (SQLException e) {
				throw new InternalErrorException(e);
			}
		}
	}

	@Override
	public Optional<MigrationInfoService> getMigrationInfo() {
		return Optional.empty();
	}

	@Override
	public void addTasks(List<BaseTask<?>> theMigrationTasks) {
		myTasks.addAll(theMigrationTasks);
	}
}
