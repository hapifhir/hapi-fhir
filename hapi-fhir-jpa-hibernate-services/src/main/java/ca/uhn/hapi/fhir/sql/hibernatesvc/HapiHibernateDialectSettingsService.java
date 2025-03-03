/*-
 * #%L
 * HAPI FHIR JPA Hibernate Services
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.hapi.fhir.sql.hibernatesvc;

import org.hibernate.service.Service;

/**
 * A hibernate {@link Service} which provides the HAPI FHIR Storage Settings. This
 * class is registered into the Hibernate {@link org.hibernate.service.ServiceRegistry},
 * and provides a way of passing information from the startup context (e.g.
 * configuration that comes from outside) into internal hibernate services,
 * since these are instantiated by Hibernate itself by class name and therefore
 * can't easily have config data injected in.
 */
public class HapiHibernateDialectSettingsService implements Service {

	private boolean myDatabasePartitionMode;

	/**
	 * Constructor
	 */
	public HapiHibernateDialectSettingsService() {
		super();
	}

	/**
	 * Are we in Database Partition Mode?
	 */
	public boolean isDatabasePartitionMode() {
		return myDatabasePartitionMode;
	}

	/**
	 * Are we in Database Partition Mode?
	 */
	public void setDatabasePartitionMode(boolean theDatabasePartitionMode) {
		myDatabasePartitionMode = theDatabasePartitionMode;
	}
}
