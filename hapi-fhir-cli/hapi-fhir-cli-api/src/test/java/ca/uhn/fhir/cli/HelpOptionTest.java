package ca.uhn.fhir.cli;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HelpOptionTest extends ConsoleOutputCapturingBaseTest {
	@Test
	public void testHelpOption() {
		App.main(new String[]{"help", "create-package"});
		assertThat(outputStreamCaptor.toString().trim()).as(outputStreamCaptor.toString().trim()).contains("Usage");
	}
}
