/*-
 * #%L
 * HAPI FHIR Server - SQL Migration
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
package ca.uhn.fhir.jpa.migrate;

import ca.uhn.fhir.jpa.migrate.taskdef.BaseTask;
import jakarta.annotation.Nonnull;
import org.flywaydb.core.api.MigrationVersion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MigrationTaskList implements Iterable<BaseTask> {
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
				.filter(task ->
						!theAppliedMigrationVersions.contains(MigrationVersion.fromVersion(task.getMigrationVersion())))
				.collect(Collectors.toList());
		return new MigrationTaskList(unappliedTasks);
	}

	public MigrationTaskList getUnskippableTasks() {
		List<BaseTask> tasks =
				myTasks.stream().filter(t -> !t.isHeavyweightSkippableTask()).collect(Collectors.toList());
		return new MigrationTaskList(tasks);
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

	public void removeIf(Predicate<BaseTask> theFilter) {
		myTasks.removeIf(theFilter);
	}

	public BaseTask[] toTaskArray() {
		return myTasks.toArray(new BaseTask[0]);
	}
}
