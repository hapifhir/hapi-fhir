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
package ca.uhn.fhir.jpa.util;

import org.hibernate.dialect.DerbyDialect;
import org.hibernate.exception.spi.TemplatedViolatedConstraintNameExtractor;
import org.hibernate.exception.spi.ViolatedConstraintNameExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.function.Function;

import static org.hibernate.exception.spi.TemplatedViolatedConstraintNameExtractor.extractUsingTemplate;

public class DerbyTenSevenHapiFhirDialect extends DerbyDialect {

	private static final Logger ourLog = LoggerFactory.getLogger(DerbyTenSevenHapiFhirDialect.class);

	@Override
	public ViolatedConstraintNameExtractor getViolatedConstraintNameExtractor() {
		Function<SQLException, String> extractor = e -> {
			switch (e.getSQLState()) {
				case "23505":
					return extractUsingTemplate(
							"unique or primary key constraint or unique index identified by '", "'", e.getMessage());
				default:
					return null;
			}
		};
		return new TemplatedViolatedConstraintNameExtractor(extractor);
	}
}
