package ca.uhn.fhir.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BaseCommandTest {

	@Test
	public void testUserPrompt() throws ParseException {

		InputStream stdin = System.in;
		try {
			System.setIn(new ByteArrayInputStream("A VALUE\n".getBytes()));

			String value = new MyBaseCommand().read();
			assertEquals("A VALUE", value);

		} finally {
			System.setIn(stdin);
		}

	}

	private static class MyBaseCommand extends BaseCommand {
		@Override
		public String getCommandDescription() {
			return null;
		}

		String read() throws ParseException {
			return promptUser("Enter a String: ");
		}

		@Override
		public String getCommandName() {
			return null;
		}

		@Override
		public Options getOptions() {
			return null;
		}

		@Override
		public void run(CommandLine theCommandLine) {

		}
	}

	public static void main(String[] theValue) throws ParseException {
		new BaseCommandTest().testUserPrompt();
	}
}
