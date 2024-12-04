package ca.uhn.fhir.jpa.migrate.util;

import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class SqlUtil {

	/**
	 * Non instantiable
	 */
	private SqlUtil() {
		// nothing
	}

	@Nonnull
	public static List<String> splitSqlFileIntoStatements(String theSql) {
		String sqlWithoutComments = Arrays.stream(theSql.split("\n"))
				.filter(t -> !t.startsWith("--"))
				.collect(Collectors.joining("\n"));

		return Arrays.stream(sqlWithoutComments.split(";"))
				.filter(StringUtils::isNotBlank)
				.map(StringUtils::trim)
				.collect(Collectors.toList());
	}

	@Nonnull
	public static List<String> splitSqlFileIntoSqlStatementsUpperCase(String theSql) {
		return splitSqlFileIntoStatements(theSql).stream()
				.map(t -> t.toUpperCase(Locale.US))
				.collect(Collectors.toList());
	}
}
