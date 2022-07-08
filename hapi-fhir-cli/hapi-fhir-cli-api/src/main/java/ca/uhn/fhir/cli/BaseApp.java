package ca.uhn.fhir.cli;

/*-
 * #%L
 * HAPI FHIR - Command Line Client - API
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.util.VersionUtil;
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

import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
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
		LogbackUtil.loggingConfigOff();

		// We don't use qualified names for loggers in CLI
		ourLog = LoggerFactory.getLogger(App.class);
	}

	private MyShutdownHook myShutdownHook;
	private boolean myShutdownHookHasNotRun;

	private void logAppHeader() {
		System.out.flush();
		String msg = "------------------------------------------------------------";
		printMessageToStdout(msg);
		logProductName();
		printMessageToStdout("------------------------------------------------------------");
		printMessageToStdout("Process ID                      : " + ManagementFactory.getRuntimeMXBean().getName());
		printMessageToStdout("Max configured JVM memory (Xmx) : " + FileHelper.getFileSizeDisplay(Runtime.getRuntime().maxMemory(), 1));
		printMessageToStdout("Detected Java version           : " + System.getProperty("java.version"));
		printMessageToStdout("------------------------------------------------------------");
	}

	private void printMessageToStdout(String theMsg) {
		PrintStream out = System.out;
		if (isNotBlank(theMsg)) {
			out.println(theMsg);
		} else {
			out.println();
		}
	}

	protected void logProductName() {
		printMessageToStdout("\ud83d\udd25 " + ansi().bold() + " " + provideProductName() + ansi().boldOff() + " " + provideProductVersion() + " - Command Line Tool");
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
		printMessageToStdout("Usage:");
		printMessageToStdout("  " + provideCommandName() + " " + theCommand.getCommandName() + " [options]");
		printMessageToStdout("");

		// Description
		String wrapped = WordUtils.wrap(theCommand.getCommandDescription(), columns);
		printMessageToStdout(wrapped);
		printMessageToStdout("");

		// Usage Notes
		List<String> usageNotes = theCommand.provideUsageNotes();
		for (String next : usageNotes) {
			wrapped = WordUtils.wrap(next, columns);
			printMessageToStdout(wrapped);
			printMessageToStdout("");
		}

		// Options
		printMessageToStdout("Options:");
		HelpFormatter fmt = new HelpFormatter();
		PrintWriter pw = new PrintWriter(System.out);
		fmt.printOptions(pw, columns, getOptions(theCommand), 2, 2);
		pw.flush();

		// That's it!
		printMessageToStdout("");
	}

	private Options getOptions(BaseCommand theCommand) {
		Options options = theCommand.getOptions();
		options.addOption(null, "debug", false, "Enable debug mode");
		return options;
	}

	private void logUsage() {
		logAppHeader();
		printMessageToStdout("Usage:");
		printMessageToStdout("  " + provideCommandName() + " {command} [options]");
		printMessageToStdout("");
		printMessageToStdout("Commands:");

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
			printMessageToStdout(ansi().bold().fg(Ansi.Color.GREEN) + left + ansi().boldOff().fg(Ansi.Color.WHITE) + " - " + ansi().bold() + StringUtils.join(rightParts, LINESEP));
		}
		printMessageToStdout("");
		printMessageToStdout(ansi().boldOff().fg(Ansi.Color.WHITE) + "See what options are available:");
		printMessageToStdout("  " + provideCommandName() + " help {command}");
		printMessageToStdout("");
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
		commands.add(new ExportConceptMapToCsvCommand());
		commands.add(new ImportCsvToConceptMapCommand());
		commands.add(new HapiFlywayMigrateDatabaseCommand());
		commands.add(new CreatePackageCommand());
		commands.add(new BulkImportCommand());
		commands.add(new ReindexTerminologyCommand());
		return commands;
	}

	protected abstract String provideProductName();

	protected abstract String provideProductVersion();

	@SuppressWarnings("ResultOfMethodCallIgnored")
	public void run(String[] theArgs) {
		LogbackUtil.loggingConfigOff();
		validateJavaVersion();

		if (System.getProperty("unit_test") != null) {
			AnsiConsole.systemInstall();
		}

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
			processHelp(theArgs);
			return;
		}

		Optional<BaseCommand> commandOpt = parseCommand(theArgs);
		if (commandOpt.isEmpty())  return;

		BaseCommand command = commandOpt.get();

		myShutdownHook = new MyShutdownHook(command);
		Runtime.getRuntime().addShutdownHook(myShutdownHook);

		Options options = getOptions(command);
		DefaultParser parser = new DefaultParser();
		CommandLine parsedOptions;

		logAppHeader();
		validateJavaVersion();

		if (System.console() == null) {
			// Probably redirecting stdout to a file
			LogbackUtil.loggingConfigOnWithoutColour();
		} else {
			// Use colours if we're logging to a console
			LogbackUtil.loggingConfigOnWithColour();
		}

		try {
			String[] args = Arrays.copyOfRange(theArgs, 1, theArgs.length);
			parsedOptions = parser.parse(options, args, true);
			if (!parsedOptions.getArgList().isEmpty()) {
				throw new ParseException(Msg.code(1555) + "Unrecognized argument: " + parsedOptions.getArgList().get(0));
			}

			if (parsedOptions.hasOption("debug")) {
				LogbackUtil.loggingConfigOnDebug();
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
				LogbackUtil.loggingConfigOff();
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
			exitDueToException(new CommandFailureException("Error: " + t, t));
		}

	}

	private Optional<BaseCommand> parseCommand(String[] theArgs) {
		Optional<BaseCommand> commandOpt = getNextCommand(theArgs, 0);

		if (commandOpt.isEmpty()) {
			String message = "Unrecognized command: " + ansi().bold().fg(Ansi.Color.RED) + theArgs[0] + ansi().boldOff().fg(Ansi.Color.WHITE);
			printMessageToStdout(message);
			printMessageToStdout("");
			logUsage();
			exitDueToProblem(message);
		}
		return commandOpt;
	}

	private Optional<BaseCommand> getNextCommand(String[] theArgs, int thePosition) {
		return ourCommands.stream().filter(cmd -> cmd.getCommandName().equals(theArgs[thePosition])).findFirst();
	}

	private void processHelp(String[] theArgs) {
		if (theArgs.length < 2) {
			logUsage();
			return;
		}
		Optional<BaseCommand> commandOpt = getNextCommand(theArgs, 1);
		if (commandOpt.isEmpty()) {
			String message = "Unknown command: " + theArgs[1];
			System.err.println(message);
			exitDueToProblem(message);
			return;
		}
		logCommandUsage(commandOpt.get());
	}


	private void exitDueToProblem(String theDescription) {
		if ("true".equals(System.getProperty("test"))) {
			throw new Error(Msg.code(1556) + theDescription);
		} else {
			System.exit(1);
		}
	}

	private void exitDueToException(Throwable e) {
		if ("true".equals(System.getProperty("test"))) {
			if (e instanceof CommandFailureException) {
				throw (CommandFailureException) e;
			}
			throw new Error(Msg.code(1557) + e);
		} else {
			System.exit(1);
		}
	}

	private void runCleanupHookAndUnregister() {
		if (myShutdownHookHasNotRun) {
			Runtime.getRuntime().removeShutdownHook(myShutdownHook);
			myShutdownHook.start();
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
}
