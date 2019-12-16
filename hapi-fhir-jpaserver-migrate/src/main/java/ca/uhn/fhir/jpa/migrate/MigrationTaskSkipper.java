package ca.uhn.fhir.jpa.migrate;

import ca.uhn.fhir.jpa.migrate.taskdef.BaseTask;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class MigrationTaskSkipper {
	private static final Logger ourLog = LoggerFactory.getLogger(MigrationTaskSkipper.class);

	public static void setDoNothingOnSkippedTasks(Collection<BaseTask> theTasks, String theSkipVersions) {
		if (isBlank(theSkipVersions) || theTasks.isEmpty()) {
			return;
		}
		Set<String> skippedVersionSet = Stream.of(theSkipVersions.split(","))
			.map(String::trim)
			// TODO KHS filter out all characters that aren't numbers, periods and underscores
			.map(s -> s.replace("'", ""))
			.map(s -> s.replace("\"", ""))
			.filter(StringUtils::isNotBlank)
			.collect(Collectors.toSet());

		for (BaseTask task : theTasks) {
			if (skippedVersionSet.contains(task.getFlywayVersion())) {
				ourLog.info("Will skip {}: {}", task.getFlywayVersion(), task.getDescription());
				task.setDoNothing(true);
			}
		}
	}
}
