/*-
 * #%L
 * HAPI FHIR Server - SQL Migration
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
package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.util.ClasspathUtil;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.intellij.lang.annotations.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.text.MessageFormat;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class PopulateSearchParameterIdentityTableTask extends BaseTask {
	private static final Logger ourLog = LoggerFactory.getLogger(PopulateSearchParameterIdentityTableTask.class);

	public static final String POPULATE_SPIDX_IDENTITY_POSTGRES =
			ClasspathUtil.loadResource("ca/uhn/fhir/jpa/migrate/taskdef/populate_spidx_identity/postgres.sql");
	public static final String POPULATE_SPIDX_IDENTITY_ORACLE =
			ClasspathUtil.loadResource("ca/uhn/fhir/jpa/migrate/taskdef/populate_spidx_identity/oracle.sql");
	public static final String POPULATE_SPIDX_IDENTITY_MSSQL =
			ClasspathUtil.loadResource("ca/uhn/fhir/jpa/migrate/taskdef/populate_spidx_identity/mssql.sql");

	private final SearchParameterTableName mySearchParameterTableName;

	public PopulateSearchParameterIdentityTableTask(
			String theProductVersion, String theSchemaVersion, SearchParameterTableName theSearchParameterTableName) {
		super(theProductVersion, theSchemaVersion);
		mySearchParameterTableName = theSearchParameterTableName;
	}

	@Override
	public void validate() {
		String description = String.format(
				"Populating HFJ_SPIDX_IDENTITY table with hash_identity, res_type, sp_name from %s table.",
				mySearchParameterTableName);
		setDescription(description);
	}

	@Override
	public void doExecute() throws SQLException {
		@Language("SQL")
		String sql = null;

		switch (getDriverType()) {
			case H2_EMBEDDED:
				// sql = POPULATE_SPIDX_IDENTITY_POSTGRES;
				break;
			case POSTGRES_9_4:
			case COCKROACHDB_21_1:
				sql = MessageFormat.format(POPULATE_SPIDX_IDENTITY_POSTGRES, mySearchParameterTableName);
				break;
			case ORACLE_12C:
				sql = MessageFormat.format(POPULATE_SPIDX_IDENTITY_ORACLE, mySearchParameterTableName);
				break;
			case MSSQL_2012:
				sql = MessageFormat.format(POPULATE_SPIDX_IDENTITY_MSSQL, mySearchParameterTableName);
				break;
			case MARIADB_10_1:
			case MYSQL_5_7:
			case DERBY_EMBEDDED:
			default:
				// MARIADB_10_1, MYSQL_5_7, DERBY_EMBEDDED are not supported by this task
				throw new IllegalStateException(Msg.code(64));
		}

		if (isNotBlank(sql)) {
			executeSql(mySearchParameterTableName.name(), sql);
		}
	}

	@Override
	protected void generateEquals(EqualsBuilder theBuilder, BaseTask theOtherObject) {
		PopulateSearchParameterIdentityTableTask otherObject =
				(PopulateSearchParameterIdentityTableTask) theOtherObject;
		theBuilder.append(mySearchParameterTableName, otherObject.mySearchParameterTableName);
	}

	@Override
	protected void generateHashCode(HashCodeBuilder theBuilder) {
		theBuilder.append(mySearchParameterTableName);
	}
}
