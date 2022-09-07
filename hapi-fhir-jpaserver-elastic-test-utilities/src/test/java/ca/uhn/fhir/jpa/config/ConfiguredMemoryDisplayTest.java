package ca.uhn.fhir.jpa.config;

import ca.uhn.fhir.i18n.HapiLocalizer;
import org.apache.jena.sparql.function.library.leviathan.log;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Disabled("Utility not intended to run automatically")
public class ConfiguredMemoryDisplayTest {
	private static final Logger ourLog = LoggerFactory.getLogger(ConfiguredMemoryDisplayTest.class);


	@Test
	void displayMemory() {
		/* This will return Long.MAX_VALUE if there is no preset limit */
		long maxMemory = Runtime.getRuntime().maxMemory();
		/* Maximum amount of memory the JVM will attempt to use */
		ourLog.error("Maximum memory (Mb): " +
			(maxMemory == Long.MAX_VALUE ? "no limit" : bytesToMb(maxMemory)));

		/* Total memory currently available to the JVM */
		ourLog.error(String.format("Total memory available to JVM (Mb): %d",
			bytesToMb(Runtime.getRuntime().totalMemory()) ));

		/* Total amount of free memory available to the JVM */
		ourLog.error("Free memory (Mb): " +
			bytesToMb( Runtime.getRuntime().freeMemory()) );

	}

	private int bytesToMb(long num) {
		return (int) (num / (1_024 * 1_024) );
	}
}

