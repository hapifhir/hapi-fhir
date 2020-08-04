package ca.uhn.fhir.cli;

import org.junit.jupiter.api.Test;

public class OptionsTest {

	@Test
	public void testOptions() {

		UploadTerminologyCommand uploadTerminologyCommand = new UploadTerminologyCommand();
		uploadTerminologyCommand.getOptions();

		App app = new App();
		for (BaseCommand next : app.provideCommands()) {
			next.getOptions();
		}
	}

}
