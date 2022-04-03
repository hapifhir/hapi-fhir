package ca.uhn.fhir.jpa.migrate.tasks.api;

/*-
 * #%L
 * HAPI FHIR Server - SQL Migration
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
import ca.uhn.fhir.jpa.migrate.taskdef.BaseTask;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.Validate;
import org.flywaydb.core.api.MigrationVersion;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BaseMigrationTasks<T extends Enum> {
	MigrationVersion lastVersion;
	private Multimap<T, BaseTask> myTasks = MultimapBuilder.hashKeys().arrayListValues().build();

	@SuppressWarnings("unchecked")
	public List<BaseTask> getTasks(@Nonnull T theFrom, @Nonnull T theTo) {
		Validate.notNull(theFrom);
		Validate.notNull(theTo);
		Validate.isTrue(theFrom.ordinal() < theTo.ordinal(), "From version must be lower than to version");

		List<BaseTask> retVal = new ArrayList<>();
		for (Object nextVersion : EnumUtils.getEnumList(theFrom.getClass())) {
			if (((T) nextVersion).ordinal() <= theFrom.ordinal()) {
				continue;
			}
			if (((T) nextVersion).ordinal() > theTo.ordinal()) {
				continue;
			}

			Collection<BaseTask> nextValues = myTasks.get((T) nextVersion);
			if (nextValues != null) {
				retVal.addAll(nextValues);
			}
		}

		return retVal;
	}

	public Builder forVersion(T theRelease) {
		IAcceptsTasks sink = theTask -> {
			theTask.validate();
			myTasks.put(theRelease, theTask);
		};
		return new Builder(toReleaseName(theRelease), sink);
	}

	@Nonnull
	protected String toReleaseName(T theRelease) {
		return theRelease.name();
	}

	public List<BaseTask> getAllTasks(T[] theVersionEnumValues) {
		List<BaseTask> retval = new ArrayList<>();
		for (T nextVersion : theVersionEnumValues) {
			Collection<BaseTask> nextValues = myTasks.get(nextVersion);
			if (nextValues != null) {
				validate(nextValues);
				retval.addAll(nextValues);
			}
		}

		return retval;
	}

	protected BaseTask getTaskWithVersion(String theFlywayVersion) {
		return myTasks.values().stream()
			.filter(task -> theFlywayVersion.equals(task.getFlywayVersion()))
			.findFirst()
			.get();
	}

	void validate(Collection<BaseTask> theTasks) {
		for (BaseTask task : theTasks) {
			task.validateVersion();
			String version = task.getFlywayVersion();
			MigrationVersion migrationVersion = MigrationVersion.fromVersion(version);
			if (lastVersion != null) {
				if (migrationVersion.compareTo(lastVersion) <= 0) {
					throw new IllegalStateException(Msg.code(51) + "Migration version " + migrationVersion + " found after migration version " + lastVersion + ".  Migrations need to be in order by version number.");
				}
			}
			lastVersion = migrationVersion;
		}
	}

	public interface IAcceptsTasks {
		void addTask(BaseTask theTask);
	}
}
