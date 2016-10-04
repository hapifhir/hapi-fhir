package ca.uhn.fhir.cli;

import static org.fusesource.jansi.Ansi.ansi;

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
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.Ansi.Color;
import org.fusesource.jansi.AnsiConsole;
import org.slf4j.LoggerFactory;

import com.phloc.commons.io.file.FileUtils;

import ca.uhn.fhir.util.VersionUtil;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;

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
		ourCommands.add(new WebsocketSubscribeCommand());
		ourCommands.add(new UploadTerminologyCommand());

		Collections.sort(ourCommands);
	}

	private static void logCommandUsage(BaseCommand theCommand) {
		logAppHeader();

		logCommandUsageNoHeader(theCommand);
	}

	private static void logCommandUsageNoHeader(BaseCommand theCommand) {
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
		System.out.println("Max configured JVM memory (Xmx): " + FileUtils.getFileSizeDisplay(Runtime.getRuntime().maxMemory(), 1));
		System.out.println("Detected Java version: " + System.getProperty("java.version"));
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

		if (command == null) {
			System.out.println("Unrecognized command: " + ansi().bold().fg(Color.RED) + theArgs[0] + ansi().boldOff().fg(Ansi.Color.WHITE));
			System.out.println();
			logUsage();
			return;
		}

		Options options = command.getOptions();
		DefaultParser parser = new DefaultParser();
		CommandLine parsedOptions;
		
		logAppHeader();
		validateJavaVersion();
		loggingConfigOn();
		
		try {
			String[] args = Arrays.asList(theArgs).subList(1, theArgs.length).toArray(new String[theArgs.length - 1]);
			parsedOptions = parser.parse(options, args, true);
			if (parsedOptions.getArgList().isEmpty()==false) {
				throw new ParseException("Unrecognized argument: " + parsedOptions.getArgList().get(0).toString());
			}
			
			// Actually execute the command
			command.run(parsedOptions);

		} catch (ParseException e) {
			loggingConfigOff();
			System.err.println("Invalid command options for command: " + command.getCommandName());
			System.err.println("  " + ansi().fg(Color.RED).bold() + e.getMessage());
			System.err.println("" + ansi().fg(Color.WHITE).boldOff());
			logCommandUsageNoHeader(command);
			System.exit(1);
		} catch (CommandFailureException e) {
			ourLog.error(e.getMessage());
			System.exit(1);
		} catch (Exception e) {
			ourLog.error("Error during execution: ", e);
			System.exit(1);
		}

	}

	private static void validateJavaVersion() {
		String specVersion = System.getProperty("java.specification.version");
		double version = Double.parseDouble(specVersion);
		if (version < 1.8) {
			System.err.flush();
			System.err.println("HAPI-CLI requires Java 1.8+ to run (detected " + specVersion + ")");
			System.err.println("Note that the HAPI library requires only Java 1.6, but you must install");
			System.err.println("a newer JVM in order to use the HAPI CLI tool.");
			System.exit(1);
		}
	}

}
