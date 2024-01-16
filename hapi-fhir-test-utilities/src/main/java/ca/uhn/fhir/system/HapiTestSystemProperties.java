/*-
 * #%L
 * HAPI FHIR Test Utilities
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
package ca.uhn.fhir.system;

public final class HapiTestSystemProperties {
	private static final String MASS_INGESTION_MODE = "mass_ingestion_mode";
	private static final String SINGLE_DB_CONNECTION = "single_db_connection";
	private static final String UNLIMITED_DB_CONNECTION = "unlimited_db_connection";

	private HapiTestSystemProperties() {}

	/**
	 * Set the database connection pool size to 100
	 */
	public static void enableUnlimitedDbConnections() {
		System.setProperty(UNLIMITED_DB_CONNECTION, "true");
	}

	public static boolean isUnlimitedDbConnectionsEnabled() {
		return "true".equals(System.getProperty(UNLIMITED_DB_CONNECTION));
	}

	public static void disableUnlimitedDbConnections() {
		System.clearProperty(UNLIMITED_DB_CONNECTION);
	}

	/**
	 * Creates a JpaStorageSettings with setMassIngestionMode(true) at test app context startup time
	 */
	public static void enableMassIngestionMode() {
		System.setProperty(MASS_INGESTION_MODE, "true");
	}

	public static boolean isMassIngestionModeEnabled() {
		return "true".equals(System.getProperty(MASS_INGESTION_MODE));
	}

	public static void disableMassIngestionMode() {
		System.clearProperty(MASS_INGESTION_MODE);
	}

	/**
	 * Set the database connection pool size to 1
	 */
	public static boolean isSingleDbConnectionEnabled() {
		return "true".equals(System.getProperty(SINGLE_DB_CONNECTION));
	}

	public static void enableSingleDbConnection() {
		System.setProperty(SINGLE_DB_CONNECTION, "true");
	}

	public static void disableSingleDbConnection() {
		System.clearProperty(SINGLE_DB_CONNECTION);
	}
}
