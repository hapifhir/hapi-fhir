package ca.uhn.fhir.jpa.migrate.tasks;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.migrate.tasks.api.ISchemaInitializationProvider;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SchemaInitializationProvider implements ISchemaInitializationProvider {
	private final String mySchemaFileClassPath;

	public SchemaInitializationProvider(String theSchemaFileClassPath) {
		mySchemaFileClassPath = theSchemaFileClassPath;
	}

	@Override
	public List<String> getSqlStatements(DriverTypeEnum theDriverType) {
		List<String> retval = new ArrayList<>();

		String initScript;
		initScript = mySchemaFileClassPath + "/" + theDriverType.getSchemaFilename();
		try {
			InputStream sqlFileInputStream = SchemaInitializationProvider.class.getResourceAsStream(initScript);
			if (sqlFileInputStream == null) {
				throw new ConfigurationException("Schema initialization script " + initScript + " not found on classpath");
			}
			// Assumes no escaped semicolons...
			String[] statements = IOUtils.toString(sqlFileInputStream, Charsets.UTF_8).split("\\;");
			for (String statement : statements) {
				if (!statement.trim().isEmpty()) {
					retval.add(statement);
				}
			}
		} catch (IOException e) {
			throw new ConfigurationException("Error reading schema initialization script " + initScript, e);
		}
		return retval;
	}
}
