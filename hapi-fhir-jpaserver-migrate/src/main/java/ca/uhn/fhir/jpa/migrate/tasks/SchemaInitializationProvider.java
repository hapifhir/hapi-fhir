package ca.uhn.fhir.jpa.migrate.tasks;

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

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.migrate.tasks.api.ISchemaInitializationProvider;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class SchemaInitializationProvider implements ISchemaInitializationProvider {
	private final String mySchemaDescription;
	private final String mySchemaFileClassPath;
	private final String mySchemaExistsIndicatorTable;

	/**
	 * @param theSchemaDescription description of the schema being updated (for logging)
	 * @param theSchemaFileClassPath        pathname to script used to initialize schema
	 * @param theSchemaExistsIndicatorTable a table name we can use to determine if this schema has already been initialized
	 */
	public SchemaInitializationProvider(String theSchemaDescription, String theSchemaFileClassPath, String theSchemaExistsIndicatorTable) {
		mySchemaDescription = theSchemaDescription;
		mySchemaFileClassPath = theSchemaFileClassPath;
		mySchemaExistsIndicatorTable = theSchemaExistsIndicatorTable;
	}

	@Override
	public List<String> getSqlStatements(DriverTypeEnum theDriverType) {
		List<String> retval = new ArrayList<>();

		String initScript = mySchemaFileClassPath + "/" + getInitScript(theDriverType);
		try {
			InputStream sqlFileInputStream = SchemaInitializationProvider.class.getResourceAsStream(initScript);
			if (sqlFileInputStream == null) {
				throw new ConfigurationException("Schema initialization script " + initScript + " not found on classpath");
			}
			// Assumes no escaped semicolons...
			String[] statements = IOUtils.toString(sqlFileInputStream, Charsets.UTF_8).split("\\;");
			for (String statement : statements) {
				String cleanedStatement = clean(statement);
				if (!isBlank(cleanedStatement)) {
					retval.add(cleanedStatement + ";");
				}
			}
		} catch (IOException e) {
			throw new ConfigurationException("Error reading schema initialization script " + initScript, e);
		}
		return retval;
	}

	private static final Pattern ourTrailingCommaPattern = Pattern.compile(",(\\s+)\\)");

	static String clean(String theStatement) {
		// Remove commas before brackets.  The Quartz h2 schema has a comma before a closing bracket that fails to execute...
		Matcher matcher = ourTrailingCommaPattern.matcher(theStatement);
		return matcher.replaceAll("$1\\)");
	}

	@Override
	public String getDescription() {
		return mySchemaDescription;
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
}

