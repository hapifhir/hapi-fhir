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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.IOUtils;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.ConceptMap.ConceptMapGroupComponent;
import org.hl7.fhir.r4.model.ConceptMap.SourceElementComponent;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class ExportConceptMapToCsvCommand extends BaseCommand {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ExportConceptMapToCsvCommand.class);
	private static final String DEFAULT_OUTPUT_PATH = "./";
	private static final String CONCEPTMAP_URL_PARAM = "u";
	private static final String FILENAME_PARAM = "f";
	private static final String PATH_PARAM = "p";

	private IGenericClient client;
	private String conceptMapUrl;
	private FhirVersionEnum fhirVersion;
	private String filename;
	private String path;

	@Override
	protected void addFhirVersionOption(Options theOptions) {
		String versions = Arrays.stream(FhirVersionEnum.values())
			.filter(t -> t != FhirVersionEnum.DSTU2_1 && t != FhirVersionEnum.DSTU2_HL7ORG && t != FhirVersionEnum.DSTU2)
			.map(t -> t.name().toLowerCase())
			.sorted()
			.collect(Collectors.joining(", "));
		addRequiredOption(theOptions, FHIR_VERSION_OPTION, "fhir-version", "version", "The FHIR version being used. Valid values: " + versions);
	}

	@Override
	public String getCommandDescription() {
		return "Exports a specific ConceptMap resource to a CSV file.";
	}

	@Override
	public String getCommandName() {
		return "export-conceptmap-to-csv";
	}

	@Override
	public Options getOptions() {
		Options options = new Options();

		addFhirVersionOption(options);
		addBaseUrlOption(options);
		addRequiredOption(options, CONCEPTMAP_URL_PARAM, "url", true, "The URL of the ConceptMap resource to be exported ( i.e. ConceptMap.url )");
		addRequiredOption(options, FILENAME_PARAM, "filename", true, "The name of the exported CSV file ( e.g. output.csv )");
		addOptionalOption(options, PATH_PARAM, "path", true, "The target directory of the exported CSV file ( e.g. /home/user/Downloads )");
		addBasicAuthOption(options);

		return options;
	}

	@Override
	public void run(CommandLine theCommandLine) throws ParseException {
		parseFhirContext(theCommandLine);
		FhirContext ctx = getFhirContext();

		String targetServer = theCommandLine.getOptionValue(BASE_URL_PARAM);
		if (isBlank(targetServer)) {
			throw new ParseException("No target server (-" + BASE_URL_PARAM + ") specified");
		} else if (!targetServer.startsWith("http") && !targetServer.startsWith("file")) {
			throw new ParseException("Invalid target server specified, must begin with 'http' or 'file'");
		}

		conceptMapUrl = theCommandLine.getOptionValue(CONCEPTMAP_URL_PARAM);
		if (isBlank(conceptMapUrl)) {
			throw new ParseException("No ConceptMap URL (" + CONCEPTMAP_URL_PARAM + ") specified");
		}

		filename = theCommandLine.getOptionValue(FILENAME_PARAM);
		if (isBlank(filename)) {
			throw new ParseException("No filename (" + FILENAME_PARAM + ") specified");
		}
		if (!filename.endsWith(".csv")) {
			filename = filename.concat(".csv");
		}

		path = theCommandLine.getOptionValue(PATH_PARAM);
		if (isBlank(path)) {
			path = DEFAULT_OUTPUT_PATH;
		}

		client = super.newClient(theCommandLine);
		fhirVersion = ctx.getVersion().getVersion();
		if (fhirVersion != FhirVersionEnum.DSTU3
			&& fhirVersion != FhirVersionEnum.R4) {
			throw new ParseException("This command does not support FHIR version " + fhirVersion);
		}

		if (theCommandLine.hasOption('v')) {
			client.registerInterceptor(new LoggingInterceptor(true));
		}

		searchByConceptMapUrl();
	}

	private void searchByConceptMapUrl() {
		ourLog.info("Searching for ConceptMap with specified ConceptMap.url: {}", conceptMapUrl);
		if (fhirVersion == FhirVersionEnum.DSTU3) {
			org.hl7.fhir.dstu3.model.Bundle response = client
				.search()
				.forResource(org.hl7.fhir.dstu3.model.ConceptMap.class)
				.where(org.hl7.fhir.dstu3.model.ConceptMap.URL.matches().value(conceptMapUrl))
				.returnBundle(org.hl7.fhir.dstu3.model.Bundle.class)
				.execute();

			if (response.hasEntry()) {
				ourLog.info("Found ConceptMap with specified ConceptMap.url: {}", conceptMapUrl);
				org.hl7.fhir.dstu3.model.ConceptMap conceptMap = (org.hl7.fhir.dstu3.model.ConceptMap) response.getEntryFirstRep().getResource();
				convertConceptMapToCsv(conceptMap);
			} else {
				ourLog.info("No ConceptMap exists with specified ConceptMap.url: {}", conceptMapUrl);
			}
		} else if (fhirVersion == FhirVersionEnum.R4) {
			Bundle response = client
				.search()
				.forResource(ConceptMap.class)
				.where(ConceptMap.URL.matches().value(conceptMapUrl))
				.returnBundle(Bundle.class)
				.execute();

			if (response.hasEntry()) {
				ourLog.info("Found ConceptMap with specified ConceptMap.url: {}", conceptMapUrl);
				ConceptMap conceptMap = (ConceptMap) response.getEntryFirstRep().getResource();
				convertConceptMapToCsv(conceptMap);
			} else {
				ourLog.info("No ConceptMap exists with specified ConceptMap.url: {}", conceptMapUrl);
			}
		}
	}

	private void convertConceptMapToCsv(org.hl7.fhir.dstu3.model.ConceptMap theConceptMap) {
		ourLog.info("Exporting ConceptMap to CSV...");
		BufferedWriter bufferedWriter = null;
		CSVPrinter csvPrinter = null;
		try {
			bufferedWriter = Files.newBufferedWriter(Paths.get(path.concat(filename)));
			csvPrinter = new CSVPrinter(
				bufferedWriter,
				CSVFormat
					.DEFAULT
					.withRecordSeparator("\n")
					.withHeader(Headers.class));

			for (org.hl7.fhir.dstu3.model.ConceptMap.ConceptMapGroupComponent group : theConceptMap.getGroup()) {
				for (org.hl7.fhir.dstu3.model.ConceptMap.SourceElementComponent element : group.getElement()) {
					for (org.hl7.fhir.dstu3.model.ConceptMap.TargetElementComponent target : element.getTarget()) {

						List<String> columns = new ArrayList<>();
						columns.add(defaultString(theConceptMap.getUrl()));
						columns.add(defaultString(theConceptMap.getSourceUriType().getValueAsString()));
						columns.add(defaultString(theConceptMap.getTargetUriType().getValueAsString()));
						columns.add(defaultString(group.getSource()));
						columns.add(defaultString(group.getSourceVersion()));
						columns.add(defaultString(group.getTarget()));
						columns.add(defaultString(group.getTargetVersion()));
						columns.add(defaultString(element.getCode()));
						columns.add(defaultString(element.getDisplay()));
						columns.add(defaultString(target.getCode()));
						columns.add(defaultString(target.getDisplay()));
						columns.add(defaultString(target.getEquivalence().toCode()));
						columns.add(defaultString(target.getComment()));

						csvPrinter.print(columns);
					}
				}
			}
		} catch (IOException | FHIRException e) {
			throw new InternalErrorException(e);
		} finally {
			IOUtils.closeQuietly(csvPrinter);
			IOUtils.closeQuietly(bufferedWriter);
		}
		ourLog.info("Finished to exporting to {}" + path.concat(filename));
	}

	private void convertConceptMapToCsv(ConceptMap theConceptMap) {
		ourLog.info("Exporting ConceptMap to CSV...");
		BufferedWriter bufferedWriter = null;
		CSVPrinter csvPrinter = null;
		try {
			bufferedWriter = Files.newBufferedWriter(Paths.get(path.concat(filename)));
			csvPrinter = new CSVPrinter(
				bufferedWriter,
				CSVFormat
					.DEFAULT
					.withRecordSeparator("\n")
					.withHeader(Headers.class));

			for (ConceptMapGroupComponent group : theConceptMap.getGroup()) {
				for (SourceElementComponent element : group.getElement()) {
					for (ConceptMap.TargetElementComponent target : element.getTarget()) {

						List<String> columns = new ArrayList<>();
						columns.add(defaultString(theConceptMap.getUrl()));
						columns.add(defaultString(theConceptMap.getSourceUriType().getValueAsString()));
						columns.add(defaultString(theConceptMap.getTargetUriType().getValueAsString()));
						columns.add(defaultString(group.getSource()));
						columns.add(defaultString(group.getSourceVersion()));
						columns.add(defaultString(group.getTarget()));
						columns.add(defaultString(group.getTargetVersion()));
						columns.add(defaultString(element.getCode()));
						columns.add(defaultString(element.getDisplay()));
						columns.add(defaultString(target.getCode()));
						columns.add(defaultString(target.getDisplay()));
						columns.add(defaultString(target.getEquivalence().toCode()));
						columns.add(defaultString(target.getComment()));

						csvPrinter.print(columns);
					}
				}
			}
		} catch (IOException | FHIRException e) {
			throw new InternalErrorException(e);
		} finally {
			IOUtils.closeQuietly(csvPrinter);
			IOUtils.closeQuietly(bufferedWriter);
		}
		ourLog.info("Finished to exporting to {}" + path.concat(filename));
	}

	private enum Headers {
		CONCEPTMAP_URL,
		SOURCE_VALUE_SET,
		TARGET_VALUE_SET,
		SOURCE_CODE_SYSTEM,
		SOURCE_CODE_SYSTEM_VERSION,
		TARGET_CODE_SYSTEM,
		TARGET_CODE_SYSTEM_VERSION,
		SOURCE_CODE,
		SOURCE_DISPLAY,
		TARGET_CODE,
		TARGET_DISPLAY,
		EQUIVALENCE,
		COMMENT
	}
}
