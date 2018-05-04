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

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.ConceptMap.ConceptMapGroupComponent;
import org.hl7.fhir.r4.model.UriType;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class ImportCsvToConceptMapCommand extends AbstractImportExportCsvConceptMapCommand {
	// Don't use qualified names for loggers in HAPI CLI.
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ImportCsvToConceptMapCommand.class.getSimpleName());

	@Override
	public String getCommandDescription() {
		return "Imports a CSV file to a ConceptMap resource.";
	}

	@Override
	public String getCommandName() {
		return "import-csv-to-conceptmap";
	}

	@Override
	public Options getOptions() {
		Options options = new Options();

		this.addFhirVersionOption(options);
		addBaseUrlOption(options);
		addRequiredOption(options, CONCEPTMAP_URL_PARAM, "url", true, "The URL of the ConceptMap resource to be imported ( i.e. ConceptMap.url )");
		addRequiredOption(options, FILENAME_PARAM, "filename", true, "The name of the imported CSV file ( e.g. input.csv )");
		addOptionalOption(options, PATH_PARAM, "path", true, "The target directory of the imported CSV file ( e.g. /home/user/Downloads/ )");
		addBasicAuthOption(options);

		return options;
	}

	@Override
	protected void process() throws ParseException {
		searchForConceptMapByUrl();
	}

	private void searchForConceptMapByUrl() throws ParseException {
		if (fhirVersion == FhirVersionEnum.DSTU3) {
			org.hl7.fhir.dstu3.model.ConceptMap conceptMap = convertCsvToConceptMapDstu3();

			ourLog.info("Searching for existing ConceptMap with specified ConceptMap.url: {}", conceptMapUrl);
			MethodOutcome methodOutcome = client
				.update()
				.resource(conceptMap)
				.conditional()
				.where(org.hl7.fhir.dstu3.model.ConceptMap.URL.matches().value(conceptMapUrl))
				.execute();

			if (methodOutcome.getCreated()) {
				ourLog.info("Created new ConceptMap: {}", methodOutcome.getId().getValue());
			} else {
				ourLog.info("Updated existing ConceptMap: {}", methodOutcome.getId().getValue());
			}
		} else if (fhirVersion == FhirVersionEnum.R4) {
			ConceptMap conceptMap = convertCsvToConceptMapR4();

			ourLog.info("Searching for existing ConceptMap with specified ConceptMap.url: {}", conceptMapUrl);
			MethodOutcome methodOutcome = client
				.update()
				.resource(conceptMap)
				.conditional()
				.where(ConceptMap.URL.matches().value(conceptMapUrl))
				.execute();

			if (methodOutcome.getCreated()) {
				ourLog.info("Created new ConceptMap: {}", methodOutcome.getId().getValue());
			} else {
				ourLog.info("Updated existing ConceptMap: {}", methodOutcome.getId().getValue());
			}
		}
	}

	private org.hl7.fhir.dstu3.model.ConceptMap convertCsvToConceptMapDstu3() throws ParseException {
		ourLog.info("Converting CSV to ConceptMap...");
		org.hl7.fhir.dstu3.model.ConceptMap retVal = new org.hl7.fhir.dstu3.model.ConceptMap();

		ourLog.info("Finished converting CSV to ConceptMap.");
		return retVal;
	}

	private ConceptMap convertCsvToConceptMapR4() throws ParseException {
		ourLog.info("Converting CSV to ConceptMap...");
		ConceptMap retVal = new ConceptMap();
		Reader reader = null;
		CSVParser csvParser = null;
		try {
			reader = Files.newBufferedReader(Paths.get(path.concat(filename)));
			csvParser = new CSVParser(
				reader,
				CSVFormat
					.DEFAULT
					.withRecordSeparator("\n")
					.withHeader(Header.class)
					.withIgnoreHeaderCase()
					.withTrim());

			retVal.setUrl(CONCEPTMAP_URL_PARAM);

			Set<String> distinctSourceValueSet = new HashSet<>();
			Set<String> distinctTargetValueSet = new HashSet<>();
			Set<String> distinctSourceCodeSystemsAndVersions = new HashSet<>();

			String sourceValueSet;
			String targetValueSet;
			String sourceCodeSystem;
			String sourceCodeSystemVersion;
			String sourceCodeSystemAndVersion;
			ConceptMapGroupComponent group;
			for (CSVRecord csvRecord : csvParser) {

				// <editor-fold desc="ConceptMap.source">
				sourceValueSet = StringUtils.defaultString(csvRecord.get(Header.SOURCE_VALUE_SET));

				if (isNotBlank(sourceValueSet)) {
					if (distinctSourceValueSet.add(sourceValueSet)) {
						retVal.setSource(new UriType(sourceValueSet));
					} else {
						throw new ParseException("CSV identifies multiple distinct source value sets ([" + distinctSourceValueSet.iterator().next() + "] and [" + sourceValueSet + "]); however, ConceptMap.source has a cardinality of 0..1.");
					}
				}
				// </editor-fold>

				// <editor-fold desc="ConceptMap.target">
				targetValueSet = StringUtils.defaultString(csvRecord.get(Header.TARGET_VALUE_SET));

				if (isNotBlank(targetValueSet)) {
					if (distinctTargetValueSet.add(targetValueSet)) {
						retVal.setSource(new UriType(targetValueSet));
					} else {
						throw new ParseException("CSV identifies multiple distinct target value sets ([" + distinctTargetValueSet.iterator().next() + "] and [" + targetValueSet + "]); however, ConceptMap.target has a cardinality of 0..1.");
					}
				}
				// </editor-fold>

				// <editor-fold desc="ConceptMap.group.source and ConceptMap.group.sourceVersion">
				sourceCodeSystem = StringUtils.defaultString(csvRecord.get(Header.SOURCE_CODE_SYSTEM));
				sourceCodeSystemVersion = StringUtils.defaultString(csvRecord.get(Header.SOURCE_CODE_SYSTEM_VERSION));
				sourceCodeSystemAndVersion = sourceCodeSystem.concat(sourceCodeSystemVersion);

				if (isNotBlank(sourceCodeSystemAndVersion)) {
					if (distinctSourceCodeSystemsAndVersions.add(sourceCodeSystemAndVersion)); {

						group = new ConceptMapGroupComponent();

						if (isNotBlank(sourceCodeSystem)) {
							group.setSource(sourceCodeSystem);
						}

						if (isNotBlank(sourceCodeSystemVersion)) {
							group.setSourceVersion(sourceCodeSystemVersion);
						}

						retVal.addGroup(group);
					}
				}
				// </editor-fold>
			}
		} catch (IOException e) {
			throw new InternalErrorException(e);
		} finally {
			IOUtils.closeQuietly(csvParser);
			IOUtils.closeQuietly(reader);
		}

		ourLog.info("Finished converting CSV to ConceptMap.");
		return retVal;
	}
}
