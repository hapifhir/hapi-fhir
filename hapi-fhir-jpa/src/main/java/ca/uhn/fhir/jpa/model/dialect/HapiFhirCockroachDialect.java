/*-
 * #%L
 * HAPI FHIR JPA Model
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
package ca.uhn.fhir.jpa.model.dialect;

import org.hibernate.dialect.CockroachDialect;
import org.hibernate.dialect.DatabaseVersion;

/**
 * Dialect for CockroachDB database.
 * Minimum version: 21.1
 */
public class HapiFhirCockroachDialect extends CockroachDialect {

	public HapiFhirCockroachDialect() {
		super(DatabaseVersion.make(21, 1));
	}

	/**
	 * @see HapiFhirH2Dialect#supportsColumnCheck() for an explanation of why we disable this
	 */
	@Override
	public boolean supportsColumnCheck() {
		return false;
	}
}
