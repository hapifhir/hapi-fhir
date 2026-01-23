/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.embedded;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import org.testcontainers.containers.OracleContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * For testing purposes.
 * <br/><br/>
 * Embedded Oracle 23 database that uses a {@link DriverTypeEnum#ORACLE_12C} driver
 * and a dockerized Testcontainer.
 *
 */
public class Oracle23EmbeddedDatabase extends OracleEmbeddedDatabase {

	public Oracle23EmbeddedDatabase() {
		super(createOracle23Container());
	}

	private static OracleContainer createOracle23Container() {

		DockerImageName dockerImageName = DockerImageName.parse("gvenzl/oracle-free:23.4-slim-faststart")
				.asCompatibleSubstituteFor("gvenzl/oracle-xe");

		Oracle23Container oracle23Container = new Oracle23Container(dockerImageName);
		oracle23Container.withPrivilegedMode(true);

		return oracle23Container;
	}

	static class Oracle23Container extends OracleContainer {

		public Oracle23Container(DockerImageName dockerImageName) {
			super(dockerImageName);
		}

		@Override
		public String getJdbcUrl() {
			// By default, OracleContainer use db name 'xepdb1' in the JDBC URL when connecting to the instance running
			// in the docker container. that works fine with container gvenzl/oracle-xe:21-slim-faststart (see
			// Oracle21EmbeddedDatabase) since the default db name running within that containe is 'xepdb1'.
			//
			// On the other hand, container gvenzl/oracle-free:23.4-slim-faststart creates a default db called
			// 'FREEPDB1'.  as a result, we need to tell OracleContainer to use that name when attempting to connect.
			// i've tried invoking method `withDatabaseName` but that has the side effect of trying to create
			// a new db named 'FREEPDB1' which fails due to the default one already existing.
			// at last, i resolved to swapping the name in this method overwrite.
			return super.getJdbcUrl().replace("/xepdb1", "/FREEPDB1");
		}
	}
}
