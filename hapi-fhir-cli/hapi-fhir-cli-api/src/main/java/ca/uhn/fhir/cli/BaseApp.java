package ca.uhn.fhir.cli;

/*-
 * #%L
 * HAPI FHIR - Command Line Client - API
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.util.VersionUtil;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import com.helger.commons.io.file.FileHelper;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.fusesource.jansi.Ansi.ansi;

@SuppressWarnings("WeakerAccess")
public abstract class BaseApp {
	protected static final org.slf4j.Logger ourLog;
	static final String LINESEP = System.getProperty("line.separator");
	private static final String STACKFILTER_PATTERN = "%xEx{full, sun.reflect, org.junit, org.eclipse, java.lang.reflect.Method, org.springframework, org.hibernate, com.sun.proxy, org.attoparser, org.thymeleaf}";
	private static final String STACKFILTER_PATTERN_PROP = "log.stackfilter.pattern";
	private static List<BaseCommand> ourCommands;
	private static boolean ourDebugMode;

	static {
		System.setProperty(STACKFILTER_PATTERN_PROP, STACKFILTER_PATTERN);
		loggingConfigOff();

		// We don't use qualified names for loggers in CLI
		ourLog = LoggerFactory.getLogger(App.class);
	}

	private MyShutdownHook myShutdownHook;
	private boolean myShutdownHookHasNotRun;

	private void logAppHeader() {
		System.out.flush();
		System.out.println("------------------------------------------------------------");
		logProductName();
		System.out.println("------------------------------------------------------------");
		System.out.println("Process ID                      : " + ManagementFactory.getRuntimeMXBean().getName());
		System.out.println("Max configured JVM memory (Xmx) : " + FileHelper.getFileSizeDisplay(Runtime.getRuntime().maxMemory(), 1));
		System.out.println("Detected Java version           : " + System.getProperty("java.version"));
		System.out.println("------------------------------------------------------------");
	}

	protected void logProductName() {
		System.out.println("\ud83d\udd25 " + ansi().bold() + " " + provideProductName() + ansi().boldOff() + " " + provideProductVersion() + " - Command Line Tool");
	}

	private void logCommandUsage(BaseCommand theCommand) {
		logAppHeader();

		logCommandUsageNoHeader(theCommand);
	}

	private void logCommandUsageNoHeader(BaseCommand theCommand) {
		// This is passed in from the launch script
		String columnsString = System.getProperty("columns");
		int columns;
		try {
			columns = Integer.parseInt(columnsString);
			columns = Math.max(columns, 40);
			columns = Math.min(columns, 180);
		} catch (Exception e) {
			columns = 80;
		}

		// Usage
		System.out.println("Usage:");
		System.out.println("  " + provideCommandName() + " " + theCommand.getCommandName() + " [options]");
		System.out.println();

		// Description
		String wrapped = WordUtils.wrap(theCommand.getCommandDescription(), columns);
		System.out.println(wrapped);
		System.out.println();

		// Usage Notes
		List<String> usageNotes = theCommand.provideUsageNotes();
		for (String next : usageNotes) {
			wrapped = WordUtils.wrap(next, columns);
			System.out.println(wrapped);
			System.out.println();
		}

		// Options
		System.out.println("Options:");
		HelpFormatter fmt = new HelpFormatter();
		PrintWriter pw = new PrintWriter(System.out);
		fmt.printOptions(pw, columns, getOptions(theCommand), 2, 2);
		pw.flush();

		// That's it!
		System.out.println();
	}

	private Options getOptions(BaseCommand theCommand) {
		Options options = theCommand.getOptions();
		options.addOption(null, "debug", false, "Enable debug mode");
		return options;
	}

	private void logUsage() {
		logAppHeader();
		System.out.println("Usage:");
		System.out.println("  " + provideCommandName() + " {command} [options]");
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
			System.out.println(ansi().bold().fg(Ansi.Color.GREEN) + left + ansi().boldOff().fg(Ansi.Color.WHITE) + " - " + ansi().bold() + StringUtils.join(rightParts, LINESEP));
		}
		System.out.println();
		System.out.println(ansi().boldOff().fg(Ansi.Color.WHITE) + "See what options are available:");
		System.out.println("  " + provideCommandName() + " help {command}");
		System.out.println();
	}

	protected abstract String provideCommandName();

	protected List<BaseCommand> provideCommands() {
		ArrayList<BaseCommand> commands = new ArrayList<>();
		commands.add(new RunServerCommand());
		commands.add(new ExampleDataUploader());
		commands.add(new ValidateCommand());
		commands.add(new ValidationDataUploader());
		commands.add(new WebsocketSubscribeCommand());
		commands.add(new UploadTerminologyCommand());
		commands.add(new IgPackUploader());
		commands.add(new ExportConceptMapToCsvCommand());
		commands.add(new ImportCsvToConceptMapCommand());
		commands.add(new HapiFlywayMigrateDatabaseCommand());
		return commands;
	}

	protected abstract String provideProductName();

	protected abstract String provideProductVersion();

	@SuppressWarnings("ResultOfMethodCallIgnored")
	public void run(String[] theArgs) {
		loggingConfigOff();
		validateJavaVersion();

		AnsiConsole.systemInstall();

		// log version while the logging is off
		VersionUtil.getVersion();

		// Set up command list
		ourCommands = new ArrayList<>();
		ourCommands.addAll(provideCommands());
		Collections.sort(ourCommands);


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
				String message = "Unknown command: " + theArgs[1];
				System.err.println(message);
				exitDueToProblem(message);
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
			String message = "Unrecognized command: " + ansi().bold().fg(Ansi.Color.RED) + theArgs[0] + ansi().boldOff().fg(Ansi.Color.WHITE);
			System.out.println(message);
			System.out.println();
			logUsage();
			exitDueToProblem(message);
			return;
		}

		myShutdownHook = new MyShutdownHook(command);
		Runtime.getRuntime().addShutdownHook(myShutdownHook);

		Options options = getOptions(command);
		DefaultParser parser = new DefaultParser();
		CommandLine parsedOptions;

		logAppHeader();
		validateJavaVersion();
		loggingConfigOn();

		try {
			String[] args = Arrays.copyOfRange(theArgs, 1, theArgs.length);
			parsedOptions = parser.parse(options, args, true);
			if (!parsedOptions.getArgList().isEmpty()) {
				throw new ParseException("Unrecognized argument: " + parsedOptions.getArgList().get(0));
			}

			if (parsedOptions.hasOption("debug")) {
				loggingConfigOnDebug();
				ourDebugMode = true;
			}

			// Actually execute the command
			command.run(parsedOptions);

			myShutdownHookHasNotRun = true;
			runCleanupHookAndUnregister();

			if (!"true".equals(System.getProperty("test"))) {
				System.exit(0);
			}

		} catch (ParseException e) {
			if (!"true".equals(System.getProperty("test"))) {
				loggingConfigOff();
			}
			System.err.println("Invalid command options for command: " + command.getCommandName());
			System.err.println("  " + ansi().fg(Ansi.Color.RED).bold() + e.getMessage());
			System.err.println("" + ansi().fg(Ansi.Color.WHITE).boldOff());
			logCommandUsageNoHeader(command);
			runCleanupHookAndUnregister();
			exitDueToException(e);
		} catch (CommandFailureException e) {
			ourLog.error(e.getMessage());
			runCleanupHookAndUnregister();
			exitDueToException(e);
		} catch (Throwable t) {
			ourLog.error("Error during execution: ", t);
			runCleanupHookAndUnregister();
			exitDueToException(new CommandFailureException("Error: " + t.toString(), t));
		}

	}

	private void exitDueToProblem(String theDescription) {
		if ("true".equals(System.getProperty("test"))) {
			throw new Error(theDescription);
		} else {
			System.exit(1);
		}
	}

	private void exitDueToException(Throwable e) {
		if ("true".equals(System.getProperty("test"))) {
			if (e instanceof CommandFailureException) {
				throw (CommandFailureException) e;
			}
			throw new Error(e);
		} else {
			System.exit(1);
		}
	}

	private void runCleanupHookAndUnregister() {
		if (myShutdownHookHasNotRun) {
			Runtime.getRuntime().removeShutdownHook(myShutdownHook);
			myShutdownHook.run();
			myShutdownHookHasNotRun = false;
		}
	}

	private void validateJavaVersion() {
		String specVersion = System.getProperty("java.specification.version");
		double version = Double.parseDouble(specVersion);
		if (version < 1.8) {
			System.err.flush();
			System.err.println(provideProductName() + " requires Java 1.8+ to run (detected " + specVersion + ")");
			System.exit(1);
		}
	}

	private class MyShutdownHook extends Thread {
		private final BaseCommand myFinalCommand;

		MyShutdownHook(BaseCommand theFinalCommand) {
			myFinalCommand = theFinalCommand;
		}

		@Override
		public void run() {
			ourLog.info(provideProductName() + " is shutting down...");
			myFinalCommand.cleanup();
		}
	}

	public static boolean isDebugMode() {
		return ourDebugMode;
	}

	private static void loggingConfigOff() {
		try {
			JoranConfigurator configurator = new JoranConfigurator();
			configurator.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
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

	private static void loggingConfigOnDebug() {
		try {
			JoranConfigurator configurator = new JoranConfigurator();
			configurator.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
			((LoggerContext) LoggerFactory.getILoggerFactory()).reset();
			configurator.doConfigure(App.class.getResourceAsStream("/logback-cli-on-debug.xml"));
		} catch (JoranException e) {
			e.printStackTrace();
		}

		ourLog.info("Debug logging is enabled");
	}
}
