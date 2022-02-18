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
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import com.google.common.io.Files;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.hl7.fhir.utilities.npm.NpmPackage;
import org.hl7.fhir.utilities.npm.PackageGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.concurrent.ExecutionException;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@SuppressWarnings("UnstableApiUsage")
public class CreatePackageCommand extends BaseCommand {
	private static final Logger ourLog = LoggerFactory.getLogger(CreatePackageCommand.class);
	public static final String TARGET_DIRECTORY_OPT = "target-directory";
	public static final String DEPENDENCY_OPT = "dependency";
	public static final String INCLUDE_EXAMPLE_OPT = "include-example";
	public static final String INCLUDE_PACKAGE_OPT = "include-package";
	public static final String VERSION_OPT = "version";
	public static final String NAME_OPT = "name";
	public static final String DESCRIPTION_OPT = "description";
	private File myWorkDirectory;
	private String myPackageName;
	private String myPackageVersion;
	private NpmPackage myPackage;
	private String myPackageDescription;

	@Override
	public String getCommandDescription() {
		return "Create an NPM package using the FHIR packaging format";
	}

	@Override
	public String getCommandName() {
		return "create-package";
	}

	@Override
	public Options getOptions() {
		Options options = new Options();
		addFhirVersionOption(options);

		addRequiredOption(options, null, NAME_OPT, "Package Name", "The name/id of the package, e.g. \"com.example.fhir.myapp\"");
		addRequiredOption(options, null, VERSION_OPT, "Package Version", "The package version. FHIR packages use SemVer, e.g. \"1.0.0\"");
		addOptionalOption(options, null, DESCRIPTION_OPT, "Description", "A description for this package");
		addOptionalOption(options, null, INCLUDE_PACKAGE_OPT, "File Spec", "A file spec to include in the package as a package resource/artifact");
		addOptionalOption(options, null, INCLUDE_EXAMPLE_OPT, "File Spec", "A file spec to include in the package as an example resource/artifact");
		addOptionalOption(options, null, TARGET_DIRECTORY_OPT, "Directory", "The directory in which to place the final package");
		addOptionalOption(options, null, DEPENDENCY_OPT, "name:version", "Include this dependency, in the form \"name:version\"");

		return options;
	}

	@Override
	public void cleanup() {
		try {
			if (myWorkDirectory != null) {
				FileUtils.deleteDirectory(myWorkDirectory);
			}
		} catch (IOException e) {
			throw new InternalErrorException(Msg.code(1545) + "Failed to delete temporary directory \"" + myWorkDirectory.getAbsolutePath() + "\"", e);
		}
	}

	@Override
	public void run(CommandLine theCommandLine) throws ParseException, ExecutionException {

		parseFhirContext(theCommandLine);

		myPackageName = theCommandLine.getOptionValue(NAME_OPT);
		if (isBlank(myPackageName)) {
			throw new ParseException(Msg.code(1546) + "No package name supplied (--" + NAME_OPT + ")");
		}
		if (!NpmPackage.isValidName(myPackageName)) {
			throw new ParseException(Msg.code(1547) + "Invalid package name: " + myPackageName);
		}

		myPackageVersion = theCommandLine.getOptionValue(VERSION_OPT);
		if (isBlank(myPackageVersion)) {
			throw new ParseException(Msg.code(1548) + "No package version supplied (--"+VERSION_OPT+")");
		}
		if (!NpmPackage.isValidVersion(myPackageVersion)) {
			throw new ParseException(Msg.code(1549) + "Invalid package version: " + myPackageVersion);
		}

		ourLog.info("Creating FHIR package {}#{}", myPackageName, myPackageVersion);

		PackageGenerator manifestGenerator = new PackageGenerator();
		manifestGenerator.name(myPackageName);
		manifestGenerator.version(myPackageVersion);
		manifestGenerator.description(myPackageDescription);
		injectFhirVersionsArray(manifestGenerator);
		if (isNotBlank(myPackageDescription)) {
			manifestGenerator.description(myPackageDescription);
		}

		String[] dependencies = theCommandLine.getOptionValues(DEPENDENCY_OPT);
		if (dependencies != null) {
			for (String nextDependencyString : dependencies) {
				int colonIdx = nextDependencyString.indexOf(":");
				if (colonIdx == -1) {
					throw new ParseException(Msg.code(1550) + "Invalid dependency spec: " + nextDependencyString);
				}
				String depName = nextDependencyString.substring(0, colonIdx);
				String depVersion = nextDependencyString.substring(colonIdx + 1);
				manifestGenerator.dependency(depName, depVersion);
			}
		}

		myWorkDirectory = Files.createTempDir();
		myPackage = NpmPackage.empty(manifestGenerator);

		ourLog.info("Using temporary directory: {}", myWorkDirectory.getAbsolutePath());

		// Package
		String[] packageValues = theCommandLine.getOptionValues(INCLUDE_PACKAGE_OPT);
		String folder = "package";
		addFiles(packageValues, folder);

		// Example
		packageValues = theCommandLine.getOptionValues(INCLUDE_EXAMPLE_OPT);
		folder = "example";
		addFiles(packageValues, folder);


		String targetDirectory = theCommandLine.getOptionValue(TARGET_DIRECTORY_OPT);
		if (isBlank(targetDirectory)) {
			targetDirectory = ".";
		}
		File targetFile = new File(new File(targetDirectory), myPackageName + "-" + myPackageVersion + ".tgz");

		ourLog.info("Writing NPM file: {}", targetFile.toString());

		try (FileOutputStream os = new FileOutputStream(targetFile, false)) {
			myPackage.save(os);
		} catch (IOException e) {
			throw new ExecutionException(Msg.code(1551) + "Failed to write file " + targetFile, e);
		}
	}

	private void injectFhirVersionsArray(PackageGenerator manifestGenerator) {
		JsonObject rootJsonObject = manifestGenerator.getRootJsonObject();
		JsonArray fhirVersionsArray = new JsonArray();
		fhirVersionsArray.add(myFhirCtx.getVersion().getVersion().getFhirVersionString());
		rootJsonObject.add("fhirVersions", fhirVersionsArray);
	}

	public void addFiles(String[] thePackageValues, String theFolder) throws ParseException, ExecutionException {
		if (thePackageValues != null) {
			for (String nextPackageValue : thePackageValues) {
				if (!nextPackageValue.contains("/")) {
					throw new ParseException(Msg.code(1552) + "Invalid file expression: " + nextPackageValue);
				}

				int endIndex = nextPackageValue.lastIndexOf("/");
				String path = nextPackageValue.substring(0, endIndex);
				String expression = nextPackageValue.substring(endIndex + 1);
				IOFileFilter filter = new WildcardFileFilter(expression);
				Collection<File> files = FileUtils.listFiles(new File(path), filter, FalseFileFilter.INSTANCE);

				for (File next : files) {

					byte[] contentBytes;
					String type;
					try {
						String contents = IOUtils.toString(new FileInputStream(next), StandardCharsets.UTF_8);
						contentBytes = contents.getBytes(StandardCharsets.UTF_8);
						type = EncodingEnum.detectEncoding(contents).newParser(myFhirCtx).parseResource(contents).fhirType();
					} catch (IOException | DataFormatException e) {
						throw new ExecutionException(Msg.code(1553) + "Failed to load/parse file: " + next.getName(), e);
					}

					ourLog.info("Adding {} file of type {}: {}", theFolder, type, next.getName());
					myPackage.addFile(theFolder, next.getName(), contentBytes, type);
				}
			}
		}
	}
}

