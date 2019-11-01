package ca.uhn.fhir.jpa.migrate.tasks;

import org.jetbrains.annotations.NotNull;
import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.migrate.tasks.api.ISchemaInitializationProvider;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseSchemaInitializationProvider implements ISchemaInitializationProvider {
	@Override
	public List<String> getSqlStatements(DriverTypeEnum theDriverType) {
		List<String> retval = new ArrayList<>();

		String initScript;
		initScript = getSchemaInitializationPath(theDriverType);
		try {
			InputStream sqlFileInputStream = HapiFhirSchemaInitializationProvider.class.getResourceAsStream(initScript);
			if (sqlFileInputStream == null) {
				throw new ConfigurationException("Schema initialization script " + initScript + " not found on classpath");
			}
			LineIterator iterator = IOUtils.lineIterator(sqlFileInputStream, Charsets.UTF_8);
			while (iterator.hasNext()) {
				retval.add(iterator.nextLine());
			}
		} catch (IOException e) {
			throw new ConfigurationException("Error reading schema initialization script " + initScript, e);
		}
		return retval;
	}

	@Nonnull
	protected abstract String getSchemaInitializationPath(DriverTypeEnum theDriverType);
}
