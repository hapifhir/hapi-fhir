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

/**
 * For testing purposes.
 * <br/><br/>
 * Embedded Oracle 21 database that uses a {@link DriverTypeEnum#ORACLE_12C} driver
 * and a dockerized Testcontainer with lazy initialization.
 *
 * @see <a href="https://www.testcontainers.org/modules/databases/oraclexe/">Oracle TestContainer</a>
 */
public class Oracle21EmbeddedDatabase extends OracleEmbeddedDatabase {

	public Oracle21EmbeddedDatabase() {
		super(new OracleContainer("gvenzl/oracle-xe:21-slim-faststart").withPrivilegedMode(true));
	}
}
