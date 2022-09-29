package ca.uhn.fhir.jpa.migrate;

import ca.uhn.fhir.jpa.migrate.taskdef.BaseTask;
import org.flywaydb.core.api.MigrationVersion;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class MigrationTaskList implements Iterable<BaseTask>{
	private final List<BaseTask> myTasks;

	public MigrationTaskList() {
		myTasks = new ArrayList<>();
	}

	public MigrationTaskList(List<BaseTask> theTasks) {
		myTasks = theTasks;
	}

	public void addAll(Collection<BaseTask> theTasks) {
		myTasks.addAll(theTasks);
	}

	public void setDoNothingOnSkippedTasks(String theSkipVersions) {
		MigrationTaskSkipper.setDoNothingOnSkippedTasks(myTasks, theSkipVersions);
	}

	public int size() {
		return myTasks.size();
	}

	public MigrationTaskList diff(Set<MigrationVersion> theAppliedMigrationVersions) {
		List<BaseTask> unappliedTasks = myTasks.stream()
			.filter(task -> !theAppliedMigrationVersions.contains(MigrationVersion.fromVersion(task.getMigrationVersion())))
			.collect(Collectors.toList());
		return new MigrationTaskList(unappliedTasks);
	}

	public void append(Iterable<BaseTask> theMigrationTasks) {
		for (BaseTask next : theMigrationTasks) {
			myTasks.add(next);
		}
	}

	public void add(BaseTask theTask) {
		myTasks.add(theTask);
	}

	public void clear() {
		myTasks.clear();
	}

	@Nonnull
	@Override
	public Iterator<BaseTask> iterator() {
		return myTasks.iterator();
	}

	public void forEach(Consumer<? super BaseTask> theAction) {
		myTasks.forEach(theAction);
	}

	public String getLastVersion() {
		return myTasks.stream()
			.map(BaseTask::getMigrationVersion)
			.map(MigrationVersion::fromVersion)
			.sorted()
			.map(MigrationVersion::toString)
			.reduce((first, second) -> second)
			.orElse(null);
	}
}
