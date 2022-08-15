package ca.uhn.fhir.jpa.util;

/*-
 * #%L
 * hapi-fhir-jpa
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

import org.hibernate.dialect.DerbyTenSevenDialect;
import org.hibernate.exception.spi.TemplatedViolatedConstraintNameExtracter;
import org.hibernate.exception.spi.ViolatedConstraintNameExtracter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class DerbyTenSevenHapiFhirDialect extends DerbyTenSevenDialect {

	private static final Logger ourLog = LoggerFactory.getLogger(DerbyTenSevenHapiFhirDialect.class);

	@Override
	public ViolatedConstraintNameExtracter getViolatedConstraintNameExtracter() {
		return new TemplatedViolatedConstraintNameExtracter() {
			@Override
			protected String doExtractConstraintName(SQLException theSqlException) throws NumberFormatException {
				switch (theSqlException.getSQLState()) {
					case "23505":
						return this.extractUsingTemplate("unique or primary key constraint or unique index identified by '", "'", theSqlException.getMessage());
					default:
						return null;
				}
			}
		};
	}

}
