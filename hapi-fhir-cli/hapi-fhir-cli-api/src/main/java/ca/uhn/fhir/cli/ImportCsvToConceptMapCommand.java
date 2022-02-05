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
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.hl7.fhir.convertors.factory.VersionConvertorFactory_30_40;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.ConceptMap.ConceptMapGroupComponent;
import org.hl7.fhir.r4.model.ConceptMap.SourceElementComponent;
import org.hl7.fhir.r4.model.ConceptMap.TargetElementComponent;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.UriType;

import java.io.IOException;
import java.io.Reader;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class ImportCsvToConceptMapCommand extends AbstractImportExportCsvConceptMapCommand {
	// TODO: Don't use qualified names for loggers in HAPI CLI.
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ImportCsvToConceptMapCommand.class);

	protected static final String SOURCE_VALUE_SET_PARAM = "i";
	protected static final String SOURCE_VALUE_SET_PARAM_LONGOPT = "input";
	protected static final String SOURCE_VALUE_SET_PARAM_NAME = "input";
	protected static final String SOURCE_VALUE_SET_PARAM_DESC = "The source value set of the ConceptMap to be imported (i.e. ConceptMap.sourceUri).";
	protected static final String TARGET_VALUE_SET_PARAM = "o";
	protected static final String TARGET_VALUE_SET_PARAM_LONGOPT = "output";
	protected static final String TARGET_VALUE_SET_PARAM_NAME = "output";
	protected static final String TARGET_VALUE_SET_PARAM_DESC = "The target value set of the ConceptMap to be imported (i.e. ConceptMap.targetUri).";

	protected String sourceValueSet;
	protected String targetValueSet;

	private boolean hasElements;
	private boolean hasTargets;

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
		Options options = super.getOptions();

		addRequiredOption(options, CONCEPTMAP_URL_PARAM, CONCEPTMAP_URL_PARAM_LONGOPT, CONCEPTMAP_URL_PARAM_NAME, CONCEPTMAP_URL_PARAM_DESC);
		// </editor-fold desc="Additional parameters.">
		addOptionalOption(options, SOURCE_VALUE_SET_PARAM, SOURCE_VALUE_SET_PARAM_LONGOPT, SOURCE_VALUE_SET_PARAM_NAME, SOURCE_VALUE_SET_PARAM_DESC);
		addOptionalOption(options, TARGET_VALUE_SET_PARAM, TARGET_VALUE_SET_PARAM_LONGOPT, TARGET_VALUE_SET_PARAM_NAME, TARGET_VALUE_SET_PARAM_DESC);
		// </editor-fold>
		addRequiredOption(options, FILE_PARAM, FILE_PARAM_LONGOPT, FILE_PARAM_NAME, FILE_PARAM_DESC);

		return options;
	}

	@Override
	protected void parseAdditionalParameters(CommandLine theCommandLine) {
		sourceValueSet = theCommandLine.getOptionValue(SOURCE_VALUE_SET_PARAM);
		if (isBlank(sourceValueSet)) {
			ourLog.info("Source value set is not specified (i.e. ConceptMap.sourceUri).");
		} else {
			ourLog.info("Specified source value set (i.e. ConceptMap.sourceUri): {}", sourceValueSet);
		}

		targetValueSet = theCommandLine.getOptionValue(TARGET_VALUE_SET_PARAM);
		if (isBlank(targetValueSet)) {
			ourLog.info("Target value set is not specified (i.e. ConceptMap.targetUri).");
		} else {
			ourLog.info("Specified target value set (i.e. ConceptMap.targetUri): {}", targetValueSet);
		}
	}

	@Override
	protected void process() throws ExecutionException {
		searchForConceptMapByUrl();
	}

	private void searchForConceptMapByUrl() throws ExecutionException {
		if (fhirVersion == FhirVersionEnum.DSTU3) {
			org.hl7.fhir.dstu3.model.ConceptMap conceptMap = convertCsvToConceptMapDstu3();

			ourLog.info("Searching for existing ConceptMap with specified URL (i.e. ConceptMap.url): {}", conceptMapUrl);
			MethodOutcome methodOutcome = client
				.update()
				.resource(conceptMap)
				.conditional()
				.where(org.hl7.fhir.dstu3.model.ConceptMap.URL.matches().value(conceptMapUrl))
				.execute();

			if (Boolean.TRUE.equals(methodOutcome.getCreated())) {
				ourLog.info("Created new ConceptMap: {}", methodOutcome.getId().getValue());
			} else {
				ourLog.info("Updated existing ConceptMap: {}", methodOutcome.getId().getValue());
			}
		} else if (fhirVersion == FhirVersionEnum.R4) {
			ConceptMap conceptMap = convertCsvToConceptMapR4();

			ourLog.info("Searching for existing ConceptMap with specified URL (i.e. ConceptMap.url): {}", conceptMapUrl);
			MethodOutcome methodOutcome = client
				.update()
				.resource(conceptMap)
				.conditional()
				.where(ConceptMap.URL.matches().value(conceptMapUrl))
				.execute();

			if (Boolean.TRUE.equals(methodOutcome.getCreated())) {
				ourLog.info("Created new ConceptMap: {}", methodOutcome.getId().getValue());
			} else {
				ourLog.info("Updated existing ConceptMap: {}", methodOutcome.getId().getValue());
			}
		}
	}

	private org.hl7.fhir.dstu3.model.ConceptMap convertCsvToConceptMapDstu3() throws ExecutionException {
		try {
			return (org.hl7.fhir.dstu3.model.ConceptMap) VersionConvertorFactory_30_40.convertResource(convertCsvToConceptMapR4());
		} catch (FHIRException fe) {
			throw new ExecutionException(Msg.code(1604), fe);
		}
	}

	private ConceptMap convertCsvToConceptMapR4() throws ExecutionException {
		ourLog.info("Converting CSV to ConceptMap...");
		ConceptMap retVal = new ConceptMap();
		try (
			Reader reader = getBufferedReader();
			CSVParser csvParser = new CSVParser(
				reader,
				CSVFormat
					.DEFAULT
					.withRecordSeparator("\n")
					.withHeader(Header.class)
					.withFirstRecordAsHeader()
					.withIgnoreHeaderCase()
					.withIgnoreEmptyLines()
					.withTrim())
		) {
			retVal.setUrl(conceptMapUrl);

			if (isNotBlank(sourceValueSet)) {
				retVal.setSource(new UriType(sourceValueSet));
			}

			if (isNotBlank(targetValueSet)) {
				retVal.setTarget(new UriType(targetValueSet));
			}

			TemporaryConceptMapGroup temporaryConceptMapGroup;
			TemporaryConceptMapGroupElement temporaryConceptMapGroupElement;
			Map<TemporaryConceptMapGroup, Map<TemporaryConceptMapGroupElement, Set<TemporaryConceptMapGroupElementTarget>>> groupMap = parseCsvRecords(csvParser);
			Map<TemporaryConceptMapGroupElement, Set<TemporaryConceptMapGroupElementTarget>> elementMap;
			Set<TemporaryConceptMapGroupElementTarget> targetSet;
			ConceptMapGroupComponent conceptMapGroupComponent;
			SourceElementComponent sourceElementComponent;
			TargetElementComponent targetElementComponent;
			for (Map.Entry<TemporaryConceptMapGroup, Map<TemporaryConceptMapGroupElement, Set<TemporaryConceptMapGroupElementTarget>>> groupEntry : groupMap.entrySet()) {

				hasElements = false;
				hasTargets = false;

				temporaryConceptMapGroup = groupEntry.getKey();
				conceptMapGroupComponent = new ConceptMapGroupComponent();

				if (temporaryConceptMapGroup.hasSource()) {
					conceptMapGroupComponent.setSource(temporaryConceptMapGroup.getSource());
				}

				if (temporaryConceptMapGroup.hasSourceVersion()) {
					conceptMapGroupComponent.setSourceVersion(temporaryConceptMapGroup.getSourceVersion());
				}

				if (temporaryConceptMapGroup.hasTarget()) {
					conceptMapGroupComponent.setTarget(temporaryConceptMapGroup.getTarget());
				}

				if (temporaryConceptMapGroup.hasTargetVersion()) {
					conceptMapGroupComponent.setTargetVersion(temporaryConceptMapGroup.getTargetVersion());
				}

				elementMap = groupEntry.getValue();
				for (Map.Entry<TemporaryConceptMapGroupElement, Set<TemporaryConceptMapGroupElementTarget>> elementEntry : elementMap.entrySet()) {

					temporaryConceptMapGroupElement = elementEntry.getKey();
					sourceElementComponent = new SourceElementComponent();

					if (temporaryConceptMapGroupElement.hasCode()) {
						sourceElementComponent.setCode(temporaryConceptMapGroupElement.getCode());
					}

					if (temporaryConceptMapGroupElement.hasDisplay()) {
						sourceElementComponent.setDisplay(temporaryConceptMapGroupElement.getDisplay());
					}

					targetSet = elementEntry.getValue();
					for (TemporaryConceptMapGroupElementTarget temporaryConceptMapGroupElementTarget : targetSet) {

						targetElementComponent = new TargetElementComponent();

						if (temporaryConceptMapGroupElementTarget.hasCode()) {
							targetElementComponent.setCode(temporaryConceptMapGroupElementTarget.getCode());
						}

						if (temporaryConceptMapGroupElementTarget.hasDisplay()) {
							targetElementComponent.setDisplay(temporaryConceptMapGroupElementTarget.getDisplay());
						}

						if (temporaryConceptMapGroupElementTarget.hasEquivalence()) {
							try {
								targetElementComponent.setEquivalence(Enumerations.ConceptMapEquivalence.fromCode(temporaryConceptMapGroupElementTarget.getEquivalence()));
							} catch (FHIRException fe) {
								throw new ExecutionException(Msg.code(1605), fe);
							}
						}

						if (temporaryConceptMapGroupElementTarget.hasComment()) {
							targetElementComponent.setComment(temporaryConceptMapGroupElementTarget.getComment());
						}

						if (temporaryConceptMapGroupElementTarget.hasValues()) {
							sourceElementComponent.addTarget(targetElementComponent);
							hasTargets = true;
						}
					}

					if (temporaryConceptMapGroupElement.hasValues() || hasTargets) {
						conceptMapGroupComponent.addElement(sourceElementComponent);
						hasElements = true;
					}
				}

				if (temporaryConceptMapGroup.hasValues() || hasElements || hasTargets) {
					retVal.addGroup(conceptMapGroupComponent);
				}
			}
		} catch (IOException e) {
			throw new InternalErrorException(Msg.code(1606) + e);
		}

		ourLog.info("Finished converting CSV to ConceptMap.");
		return retVal;
	}

	private Map<TemporaryConceptMapGroup, Map<TemporaryConceptMapGroupElement, Set<TemporaryConceptMapGroupElementTarget>>> parseCsvRecords(CSVParser theCsvParser) {
		Map<TemporaryConceptMapGroup, Map<TemporaryConceptMapGroupElement, Set<TemporaryConceptMapGroupElementTarget>>> retVal = new LinkedHashMap<>();

		TemporaryConceptMapGroup group;
		TemporaryConceptMapGroupElement element;
		TemporaryConceptMapGroupElementTarget target;
		Map<TemporaryConceptMapGroupElement, Set<TemporaryConceptMapGroupElementTarget>> elementMap;
		Set<TemporaryConceptMapGroupElementTarget> targetSet;

		for (CSVRecord csvRecord : theCsvParser) {

			group = new TemporaryConceptMapGroup(
				defaultString(csvRecord.get(Header.SOURCE_CODE_SYSTEM)),
				defaultString(csvRecord.get(Header.SOURCE_CODE_SYSTEM_VERSION)),
				defaultString(csvRecord.get(Header.TARGET_CODE_SYSTEM)),
				defaultString(csvRecord.get(Header.TARGET_CODE_SYSTEM_VERSION)));

			element = new TemporaryConceptMapGroupElement(
				defaultString(csvRecord.get(Header.SOURCE_CODE)),
				defaultString(csvRecord.get(Header.SOURCE_DISPLAY)));

			target = new TemporaryConceptMapGroupElementTarget(
				defaultString(csvRecord.get(Header.TARGET_CODE)),
				defaultString(csvRecord.get(Header.TARGET_DISPLAY)),
				defaultString(csvRecord.get(Header.EQUIVALENCE)),
				defaultString(csvRecord.get(Header.COMMENT)));

			if (!retVal.containsKey(group)) {
				targetSet = new LinkedHashSet<>();
				targetSet.add(target);

				elementMap = new LinkedHashMap<>();
				elementMap.put(element, targetSet);

				retVal.put(group, elementMap);
			} else if (!retVal.get(group).containsKey(element)) {
				targetSet = new LinkedHashSet<>();
				targetSet.add(target);

				retVal.get(group).put(element, targetSet);
			} else {
				retVal.get(group).get(element).add(target);
			}
		}

		return retVal;
	}
}
