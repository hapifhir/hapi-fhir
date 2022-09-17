package ca.uhn.fhir.jpa.migrate;

import ca.uhn.fhir.jpa.migrate.dao.HapiMigrationDao;
import ca.uhn.fhir.jpa.migrate.entity.HapiMigrationEntity;
import ca.uhn.fhir.jpa.migrate.taskdef.BaseTask;
import org.flywaydb.core.api.MigrationVersion;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class HapiMigrationStorageSvc {
	private final HapiMigrationDao myHapiMigrationDao;

	public HapiMigrationStorageSvc(HapiMigrationDao theHapiMigrationDao) {
		myHapiMigrationDao = theHapiMigrationDao;
	}

	public List<HapiMigrationEntity> fetchAppliedMigrations() {
		return myHapiMigrationDao.findAll();
	}

	public List<BaseTask> diff(List<BaseTask> theTasks) {
		Set<MigrationVersion> appliedMigrationVersions = fetchAppliedMigrationVersions();

		return theTasks.stream()
			.filter(task -> !appliedMigrationVersions.contains(MigrationVersion.fromVersion(task.getMigrationVersion())))
			.collect(Collectors.toList());
	}

	private Set<MigrationVersion> fetchAppliedMigrationVersions() {
		return myHapiMigrationDao.fetchMigrationVersions().stream().map(MigrationVersion::fromVersion).collect(Collectors.toSet());
	}

	// FIXME KHS test
	public String getLatestAppliedVersion() {
		return fetchAppliedMigrationVersions().stream()
			.sorted()
			.map(MigrationVersion::toString)
			.reduce((first, second) -> second)
			.orElse("unknown");
	}
}
