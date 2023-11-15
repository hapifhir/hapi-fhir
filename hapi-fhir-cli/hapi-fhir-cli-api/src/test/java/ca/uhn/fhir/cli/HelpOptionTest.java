package ca.uhn.fhir.cli;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

public class HelpOptionTest extends ConsoleOutputCapturingBaseTest {
	@Test
	public void testHelpOption() {
		App.main(new String[]{"help", "create-package"});
		assertThat(outputStreamCaptor.toString().trim(), outputStreamCaptor.toString().trim(), containsString("Usage"));
	}
}
