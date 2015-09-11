package ca.uhn.fhir.cli;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class App {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(App.class);
	private static List<BaseCommand> ourCommands;

	static {
		ourCommands = new ArrayList<BaseCommand>();
		ourCommands.add(new RunServerCommand());
	}

	public static void main(String[] theArgs) {
		if (theArgs.length == 0) {
			logUsage();
			return;
		}

		BaseCommand command = null;
		for (BaseCommand nextCommand : ourCommands) {
			if (nextCommand.getCommandName().equals(theArgs[0])) {
				command = nextCommand;
				break;
			}
		}

		Options options = command.getOptions();
		DefaultParser parser = new DefaultParser();
		CommandLine parsedOptions;
		try {
			String[] args = Arrays.asList(theArgs).subList(1, theArgs.length).toArray(new String[theArgs.length - 1]);
			parsedOptions = parser.parse(options, args, true);
			command.run(parsedOptions);
		} catch (ParseException e) {
			ourLog.error("Invalid command options for command: " + command.getCommandName());
			ourLog.error(e.getMessage());
			ourLog.error("Aborting!");
			return;
		}

	}

	private static void logUsage() {
		
	}

}
