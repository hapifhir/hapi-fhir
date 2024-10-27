/*-
 * #%L
 * HAPI FHIR Server - SQL Migration
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
package ca.uhn.fhir.jpa.migrate.tasks;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.migrate.tasks.api.ISchemaInitializationProvider;
import ca.uhn.fhir.util.ClasspathUtil;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Charsets;
import jakarta.annotation.Nonnull;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.trim;

public class SchemaInitializationProvider implements ISchemaInitializationProvider {

	private final String mySchemaExistsIndicatorTable;
	private final boolean myCanInitializeSchema;
	private String mySchemaFileClassPath;
	private String mySchemaDescription;

	/**
	 * @param theSchemaFileClassPath        pathname to script used to initialize schema
	 * @param theSchemaExistsIndicatorTable a table name we can use to determine if this schema has already been initialized
	 * @param theCanInitializeSchema        this is a "root" schema initializer that creates the primary tables used by this app
	 */
	public SchemaInitializationProvider(
			String theSchemaDescription,
			String theSchemaFileClassPath,
			String theSchemaExistsIndicatorTable,
			boolean theCanInitializeSchema) {
		mySchemaDescription = theSchemaDescription;
		mySchemaFileClassPath = theSchemaFileClassPath;
		mySchemaExistsIndicatorTable = theSchemaExistsIndicatorTable;
		myCanInitializeSchema = theCanInitializeSchema;
	}

	@Override
	public List<String> getSqlStatements(DriverTypeEnum theDriverType) {
		if (!isEnabled()) {
			return Collections.emptyList();
		}

		List<String> retval = new ArrayList<>();

		String initScript = mySchemaFileClassPath + "/" + getInitScript(theDriverType);
		try {
			InputStream sqlFileInputStream = ClasspathUtil.loadResourceAsStream(initScript);
			// Assumes no escaped semicolons...
			String sqlString = IOUtils.toString(sqlFileInputStream, Charsets.UTF_8);
			parseSqlFileIntoIndividualStatements(theDriverType, retval, sqlString);
		} catch (IOException e) {
			throw new ConfigurationException(
					Msg.code(50) + "Error reading schema initialization script " + initScript, e);
		}
		return retval;
	}

	@VisibleForTesting
	void parseSqlFileIntoIndividualStatements(DriverTypeEnum theDriverType, List<String> retval, String theSqlString) {
		String sqlString = theSqlString.replaceAll("--.*", "");

		String sqlStringNoComments = preProcessSqlString(theDriverType, sqlString);
		String[] statements = sqlStringNoComments.split(";");
		for (String statement : statements) {
			String cleanedStatement = preProcessSqlStatement(theDriverType, statement);
			if (!isBlank(cleanedStatement)) {
				String next = trim(cleanedStatement);
				next = next.replace('\n', ' ');
				next = next.replace('\r', ' ');
				next = next.replaceAll(" +", " ");
				retval.add(next);
			}
		}
	}

	protected String preProcessSqlString(DriverTypeEnum theDriverType, String sqlString) {
		return sqlString;
	}

	protected String preProcessSqlStatement(DriverTypeEnum theDriverType, String sqlStatement) {
		return sqlStatement;
	}

	@Nonnull
	protected String getInitScript(DriverTypeEnum theDriverType) {
		return theDriverType.getSchemaFilename();
	}

	@Override
	public boolean equals(Object theO) {
		if (this == theO) return true;

		if (theO == null || getClass() != theO.getClass()) return false;

		SchemaInitializationProvider that = (SchemaInitializationProvider) theO;

		return this.getClass().getSimpleName() == that.getClass().getSimpleName();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				.append(this.getClass().getSimpleName())
				.toHashCode();
	}

	@Override
	public String getSchemaExistsIndicatorTable() {
		return mySchemaExistsIndicatorTable;
	}

	public SchemaInitializationProvider setSchemaFileClassPath(String theSchemaFileClassPath) {
		mySchemaFileClassPath = theSchemaFileClassPath;
		return this;
	}

	@Override
	public String getSchemaDescription() {
		return mySchemaDescription;
	}

	@Override
	public SchemaInitializationProvider setSchemaDescription(String theSchemaDescription) {
		mySchemaDescription = theSchemaDescription;
		return this;
	}

	@Override
	public boolean canInitializeSchema() {
		return myCanInitializeSchema;
	}
}
