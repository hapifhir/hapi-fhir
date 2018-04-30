package ca.uhn.fhir.cli;

import org.junit.Test;

public class OptionsTest {

	@Test
	public void testOptions() {
		App app = new App();
		for (BaseCommand next : app.provideCommands()) {
			next.getOptions();
		}
	}

}
