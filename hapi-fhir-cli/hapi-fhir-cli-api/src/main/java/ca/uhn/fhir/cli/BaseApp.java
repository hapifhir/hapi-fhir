package ca.uhn.fhir.cli;

/*-
 * #%L
 * HAPI FHIR - Command Line Client - API
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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
import com.phloc.commons.io.file.FileUtils;
import org.apache.commons.cli.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.fusesource.jansi.Ansi.ansi;

public abstract class BaseApp {
	public static final String STACKFILTER_PATTERN = "%xEx{full, sun.reflect, org.junit, org.eclipse, java.lang.reflect.Method, org.springframework, org.hibernate, com.sun.proxy, org.attoparser, org.thymeleaf}";
	public static final String STACKFILTER_PATTERN_PROP = "log.stackfilter.pattern";
	public static final String LINESEP = System.getProperty("line.separator");
	protected static final org.slf4j.Logger ourLog;
	private static List<BaseCommand> ourCommands;

	static {
		System.setProperty(STACKFILTER_PATTERN_PROP, STACKFILTER_PATTERN);
		loggingConfigOff();

		// We don't use qualified names for loggers in CLI
		ourLog = LoggerFactory.getLogger(App.class.getSimpleName());
	}

	private void logAppHeader() {
		System.out.flush();
		System.out.println("------------------------------------------------------------");
		System.out.println("\ud83d\udd25 " + ansi().bold() + " " + provideProductName() + ansi().boldOff() + " " + provideProductVersion() + " - Command Line Tool");
		System.out.println("------------------------------------------------------------");
		System.out.println("Max configured JVM memory (Xmx): " + FileUtils.getFileSizeDisplay(Runtime.getRuntime().maxMemory(), 1));
		System.out.println("Detected Java version: " + System.getProperty("java.version"));
		System.out.println("------------------------------------------------------------");
	}

	private void logCommandUsage(BaseCommand theCommand) {
		logAppHeader();

		logCommandUsageNoHeader(theCommand);
	}

	private void logCommandUsageNoHeader(BaseCommand theCommand) {
		System.out.println("Usage:");
		System.out.println("  " + provideCommandName() + " " + theCommand.getCommandName() + " [options]");
		System.out.println();
		System.out.println("Options:");

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

		HelpFormatter fmt = new HelpFormatter();
		PrintWriter pw = new PrintWriter(System.out);
		fmt.printOptions(pw, columns, theCommand.getOptions(), 2, 2);
		pw.flush();
		pw.close();
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

	public List<BaseCommand> provideCommands() {
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
			System.out.println("Unrecognized command: " + ansi().bold().fg(Ansi.Color.RED) + theArgs[0] + ansi().boldOff().fg(Ansi.Color.WHITE));
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
			String[] args = Arrays.copyOfRange(theArgs, 1, theArgs.length);
			parsedOptions = parser.parse(options, args, true);
			if (!parsedOptions.getArgList().isEmpty()) {
				throw new ParseException("Unrecognized argument: " + parsedOptions.getArgList().get(0));
			}

			// Actually execute the command
			command.run(parsedOptions);

			if (!"true".equals(System.getProperty("test"))) {
				System.exit(0);
			}

		} catch (ParseException e) {
			loggingConfigOff();
			System.err.println("Invalid command options for command: " + command.getCommandName());
			System.err.println("  " + ansi().fg(Ansi.Color.RED).bold() + e.getMessage());
			System.err.println("" + ansi().fg(Ansi.Color.WHITE).boldOff());
			logCommandUsageNoHeader(command);
			System.exit(1);
		} catch (CommandFailureException e) {
			ourLog.error(e.getMessage());
			if ("true".equals(System.getProperty("test"))) {
				throw e;
			} else {
				System.exit(1);
			}
		} catch (Exception e) {
			ourLog.error("Error during execution: ", e);
			if ("true".equals(System.getProperty("test"))) {
				throw new CommandFailureException("Error: " + e.toString(), e);
			} else {
				System.exit(1);
			}
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


}
