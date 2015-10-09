package ca.uhn.fhir.cli;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.fusesource.jansi.AnsiConsole;
import org.slf4j.LoggerFactory;

import ca.uhn.fhir.util.VersionUtil;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import static org.fusesource.jansi.Ansi.*;
import static org.fusesource.jansi.Ansi.Color.*;
import static org.fusesource.jansi.Ansi.*;
import static org.fusesource.jansi.Ansi.Color.*;
public class App {
	private static List<BaseCommand> ourCommands;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(App.class);

	public static final String LINESEP = System.getProperty("line.separator");
	
	static {
		ourCommands = new ArrayList<BaseCommand>();
		ourCommands.add(new RunServerCommand());
		ourCommands.add(new ExampleDataUploader());
		ourCommands.add(new ValidateCommand());
		ourCommands.add(new ValidationDataUploader());

		Collections.sort(ourCommands);
	}

	private static void logCommandUsage(BaseCommand theCommand) {
		logAppHeader();

		System.out.println("Usage:");
		System.out.println("  hapi-fhir-cli " + theCommand.getCommandName() + " [options]");
		System.out.println();
		System.out.println("Options:");

		HelpFormatter fmt = new HelpFormatter();
		PrintWriter pw = new PrintWriter(System.out);
		fmt.printOptions(pw, 80, theCommand.getOptions(), 2, 2);
		pw.flush();
		pw.close();
	}

	private static void loggingConfigOff() {
		try {
			JoranConfigurator configurator = new JoranConfigurator();
			configurator.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
			((LoggerContext) LoggerFactory.getILoggerFactory()).reset();
			configurator.doConfigure(App.class.getResourceAsStream("/logback-cli-off.xml"));
		} catch (JoranException e) {
			e.printStackTrace();
		}
	}

	private static void loggingConfigOn() {
		try {
			JoranConfigurator configurator = new JoranConfigurator();
			configurator.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
			((LoggerContext) LoggerFactory.getILoggerFactory()).reset();
			configurator.doConfigure(App.class.getResourceAsStream("/logback-cli-on.xml"));
		} catch (JoranException e) {
			e.printStackTrace();
		}
	}

	private static void logUsage() {
		logAppHeader();
		System.out.println("Usage:");
		System.out.println("  hapi-fhir-cli {command} [options]");
		System.out.println();
		System.out.println("Commands:");

		int longestCommandLength = 0;
		for (BaseCommand next : ourCommands) {
			longestCommandLength = Math.max(longestCommandLength, next.getCommandName().length());
		}
		
		for (BaseCommand next : ourCommands) {
			String left = "  " + StringUtils.rightPad(next.getCommandName(), longestCommandLength);
			String[] rightParts = WordUtils.wrap(next.getCommandDescription(), 80 - (left.length() + 3)).split("\\n");
			for (int i = 1; i < rightParts.length; i++) {
				rightParts[i] = StringUtils.leftPad("", left.length() + 3) + rightParts[i];
			}
			System.out.println(ansi().bold().fg(Color.GREEN) + left + ansi().boldOff().fg(Color.WHITE) + " - " + ansi().bold() + StringUtils.join(rightParts, LINESEP));
		}
		System.out.println();
		System.out.println(ansi().boldOff().fg(Color.WHITE) + "See what options are available:");
		System.out.println("  hapi-fhir-cli help {command}");
		System.out.println();
	}

	private static void logAppHeader() {
		System.out.flush();
		System.out.println("------------------------------------------------------------");
		System.out.println("\ud83d\udd25 " + ansi().bold() + "HAPI FHIR" + ansi().boldOff() + " " + VersionUtil.getVersion() + " - Command Line Tool");
		System.out.println("------------------------------------------------------------");
	}

	public static void main(String[] theArgs) {
		loggingConfigOff();
		AnsiConsole.systemInstall();

		// log version while the logging is off
		VersionUtil.getVersion(); 
		
		if (theArgs.length == 0) {
			logUsage();
			return;
		}

		if (theArgs[0].equals("help")) {
			if (theArgs.length < 2) {
				logUsage();
				return;
			}
			BaseCommand command = null;
			for (BaseCommand nextCommand : ourCommands) {
				if (nextCommand.getCommandName().equals(theArgs[1])) {
					command = nextCommand;
					break;
				}
			}
			if (command == null) {
				System.err.println("Unknown command: " + theArgs[1]);
				return;
			}
			logCommandUsage(command);
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
		
		logAppHeader();
		loggingConfigOn();
		
		try {
			String[] args = Arrays.asList(theArgs).subList(1, theArgs.length).toArray(new String[theArgs.length - 1]);
			parsedOptions = parser.parse(options, args, true);

			
			// Actually execute the command
			command.run(parsedOptions);

		} catch (ParseException e) {
			ourLog.error("Invalid command options for command: " + command.getCommandName());
			ourLog.error(e.getMessage());
			ourLog.error("Aborting!");
			return;
		} catch (CommandFailureException e) {
			ourLog.error(e.getMessage());
		} catch (Exception e) {
			ourLog.error("Error during execution: ", e);
		}

	}

}
