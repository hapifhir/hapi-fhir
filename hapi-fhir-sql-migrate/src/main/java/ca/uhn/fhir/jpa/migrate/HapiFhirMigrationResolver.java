package ca.uhn.fhir.jpa.migrate;

import ca.uhn.fhir.jpa.migrate.taskdef.BaseTask;
import org.flywaydb.core.api.resolver.MigrationResolver;
import org.flywaydb.core.api.resolver.ResolvedMigration;
import org.flywaydb.core.extensibility.MigrationType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HapiFhirMigrationResolver implements MigrationResolver {
	private final MigrationTaskList myTaskList;

	public HapiFhirMigrationResolver(MigrationTaskList theTaskList) {
		myTaskList = theTaskList;
	}

	@Override
	public Collection<ResolvedMigration> resolveMigrations(Context context) {
		return toResolvedMigrationList(myTaskList);
	}

	private List<ResolvedMigration> toResolvedMigrationList(MigrationTaskList theTaskList) {
		List<ResolvedMigration> retval = new ArrayList<>();
		for (BaseTask task : theTaskList) {
			ResolvedMigration resolvedMigration = new HapiFhirResolvedMigration(task);
		}
		return retval;
	}

	@Override
	public String getPrefix() {
		return MigrationResolver.super.getPrefix();
	}

	@Override
	public MigrationType getDefaultMigrationType() {
		return MigrationResolver.super.getDefaultMigrationType();
	}
}
