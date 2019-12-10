package ca.uhn.fhir.jpa.migrate;

/*-
 * #%L
 * HAPI FHIR JPA Server - Migration
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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

import ca.uhn.fhir.jpa.migrate.taskdef.BaseTask;
import org.flywaydb.core.api.MigrationInfoService;

import java.util.List;
import java.util.Optional;

public abstract class BaseMigrator {

	private boolean myDryRun;
	private boolean myNoColumnShrink;
	private boolean myOutOfOrderPermitted;
	private DriverTypeEnum myDriverType;
	private String myConnectionUrl;
	private String myUsername;
	private String myPassword;

	public abstract void migrate();

	public boolean isDryRun() {
		return myDryRun;
	}

	public void setDryRun(boolean theDryRun) {
		myDryRun = theDryRun;
	}

	public boolean isNoColumnShrink() {
		return myNoColumnShrink;
	}

	public void setNoColumnShrink(boolean theNoColumnShrink) {
		myNoColumnShrink = theNoColumnShrink;
	}

	public abstract Optional<MigrationInfoService> getMigrationInfo();

	public abstract void addTasks(List<BaseTask<?>> theMigrationTasks);

	public DriverTypeEnum getDriverType() {
		return myDriverType;
	}

	public void setDriverType(DriverTypeEnum theDriverType) {
		myDriverType = theDriverType;
	}

	public String getConnectionUrl() {
		return myConnectionUrl;
	}

	public void setConnectionUrl(String theConnectionUrl) {
		myConnectionUrl = theConnectionUrl;
	}

	public String getUsername() {
		return myUsername;
	}

	public void setUsername(String theUsername) {
		myUsername = theUsername;
	}

	public String getPassword() {
		return myPassword;
	}

	public void setPassword(String thePassword) {
		myPassword = thePassword;
	}

	public boolean isOutOfOrderPermitted() {
		return myOutOfOrderPermitted;
	}

	public void setOutOfOrderPermitted(boolean theOutOfOrderPermitted) {
		myOutOfOrderPermitted = theOutOfOrderPermitted;
	}
}
