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
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.apache.commons.cli.Options;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import org.hl7.fhir.convertors.factory.VersionConvertorFactory_30_40;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.ConceptMap.ConceptMapGroupComponent;
import org.hl7.fhir.r4.model.ConceptMap.SourceElementComponent;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.apache.commons.lang3.StringUtils.defaultString;

public class ExportConceptMapToCsvCommand extends AbstractImportExportCsvConceptMapCommand {
	// TODO: Don't use qualified names for loggers in HAPI CLI.
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ExportConceptMapToCsvCommand.class);

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
		Options options = super.getOptions();

		addRequiredOption(options, CONCEPTMAP_URL_PARAM, CONCEPTMAP_URL_PARAM_LONGOPT, CONCEPTMAP_URL_PARAM_NAME, CONCEPTMAP_URL_PARAM_DESC);
		addRequiredOption(options, FILE_PARAM, FILE_PARAM_LONGOPT, FILE_PARAM_NAME, FILE_PARAM_DESC);

		return options;
	}

	@Override
	protected void process() throws ExecutionException {
		searchForConceptMapByUrl();
	}

	private void searchForConceptMapByUrl() throws ExecutionException {
		ourLog.info("Searching for ConceptMap with specified URL (i.e. ConceptMap.url): {}", conceptMapUrl);
		if (fhirVersion == FhirVersionEnum.DSTU3) {
			org.hl7.fhir.dstu3.model.Bundle response = client
				.search()
				.forResource(org.hl7.fhir.dstu3.model.ConceptMap.class)
				.where(org.hl7.fhir.dstu3.model.ConceptMap.URL.matches().value(conceptMapUrl))
				.returnBundle(org.hl7.fhir.dstu3.model.Bundle.class)
				.execute();

			if (response.hasEntry()) {
				ourLog.info("Found ConceptMap with specified URL (i.e. ConceptMap.url): {}", conceptMapUrl);
				org.hl7.fhir.dstu3.model.ConceptMap conceptMap = (org.hl7.fhir.dstu3.model.ConceptMap) response.getEntryFirstRep().getResource();
				convertConceptMapToCsv(conceptMap);
			} else {
				ourLog.info("No ConceptMap exists with specified URL (i.e. ConceptMap.url): {}", conceptMapUrl);
			}
		} else if (fhirVersion == FhirVersionEnum.R4) {
			Bundle response = client
				.search()
				.forResource(ConceptMap.class)
				.where(ConceptMap.URL.matches().value(conceptMapUrl))
				.returnBundle(Bundle.class)
				.execute();

			if (response.hasEntry()) {
				ourLog.info("Found ConceptMap with specified URL (i.e. ConceptMap.url): {}", conceptMapUrl);
				ConceptMap conceptMap = (ConceptMap) response.getEntryFirstRep().getResource();
				convertConceptMapToCsv(conceptMap);
			} else {
				ourLog.info("No ConceptMap exists with specified URL (i.e. ConceptMap.url): {}", conceptMapUrl);
			}
		}
	}

	private void convertConceptMapToCsv(org.hl7.fhir.dstu3.model.ConceptMap theConceptMap) throws ExecutionException {
		try {
			convertConceptMapToCsv((ConceptMap) VersionConvertorFactory_30_40.convertResource(theConceptMap));
		} catch (FHIRException fe) {
			throw new ExecutionException(Msg.code(1563), fe);
		}
	}

	private void convertConceptMapToCsv(ConceptMap theConceptMap) {
		Path path = Paths.get(file);
		ourLog.info("Exporting ConceptMap to CSV: {}", path);
		try (Writer writer = Files.newBufferedWriter(path)) {

			CSVFormat format = CSVFormat.DEFAULT
				.withRecordSeparator("\n")
				.withHeader(Header.class)
				.withQuoteMode(QuoteMode.ALL);
			try (CSVPrinter csvPrinter = new CSVPrinter(writer, format)) {
				for (ConceptMapGroupComponent group : theConceptMap.getGroup()) {
					for (SourceElementComponent element : group.getElement()) {
						for (ConceptMap.TargetElementComponent target : element.getTarget()) {

							List<String> columns = new ArrayList<>();
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

							csvPrinter.printRecord(columns);
						}
					}
				}
				csvPrinter.flush();
			}
		} catch (IOException ioe) {
			throw new InternalErrorException(Msg.code(1564) + ioe);
		}

		ourLog.info("Finished exporting to {}", file);
	}
}
