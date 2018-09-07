package ca.uhn.fhir.cli;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import com.google.common.base.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class HapiMigrateDatabaseCommandTest {

	private static final Logger ourLog = LoggerFactory.getLogger(HapiMigrateDatabaseCommandTest.class);

	static {
		System.setProperty("test", "true");
	}

	@Test
	public void testMigrate() throws IOException {

		File directory = new File("target/migrator_derby_test_340_350");
		if (directory.exists()) {
			FileUtils.deleteDirectory(directory);
		}

		String url = "jdbc:derby:directory:target/migrator_derby_test_340_350;create=true";
		DriverTypeEnum.ConnectionProperties connectionProperties = DriverTypeEnum.DERBY_EMBEDDED.newConnectionProperties(url, "", "");

		String script = IOUtils.toString(HapiMigrateDatabaseCommandTest.class.getResourceAsStream("/persistence_create_derby107_340.sql"), Charsets.UTF_8);
		List<String> scriptStatements = new ArrayList<>(Arrays.asList(script.split("\n")));
		for (int i = 0; i < scriptStatements.size(); i++) {
			String nextStatement = scriptStatements.get(i);
			if (isBlank(nextStatement)) {
				scriptStatements.remove(i);
				i--;
				continue;
			}

			nextStatement = nextStatement.trim();
			while (nextStatement.endsWith(";")) {
				nextStatement = nextStatement.substring(0, nextStatement.length() - 1);
			}
			scriptStatements.set(i, nextStatement);
		}

		connectionProperties.getTxTemplate().execute(t -> {
			for (String next : scriptStatements) {
				connectionProperties.newJdbcTemplate().execute(next);
			}
			return null;
		});

		ourLog.info("**********************************************");
		ourLog.info("Done Setup, Starting Dry Run...");
		ourLog.info("**********************************************");

		String[] args = new String[]{
			"migrate-database",
			"-d", "DERBY_EMBEDDED",
			"-u", url,
			"-n", "",
			"-p", "",
			"-r",
			"-f", "V3_4_0",
			"-t", "V3_5_0"
		};
		App.main(args);

		ourLog.info("**********************************************");
		ourLog.info("Done Setup, Starting Migration...");
		ourLog.info("**********************************************");

		args = new String[]{
			"migrate-database",
			"-d", "DERBY_EMBEDDED",
			"-u", url,
			"-n", "",
			"-p", "",
			"-f", "V3_4_0",
			"-t", "V3_5_0"
		};
		App.main(args);
	}
}
