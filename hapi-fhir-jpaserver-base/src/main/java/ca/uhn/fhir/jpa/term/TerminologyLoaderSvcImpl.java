package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink;
import ca.uhn.fhir.jpa.term.custom.ConceptHandler;
import ca.uhn.fhir.jpa.term.custom.HierarchyHandler;
import ca.uhn.fhir.jpa.term.loinc.*;
import ca.uhn.fhir.jpa.term.snomedct.SctHandlerConcept;
import ca.uhn.fhir.jpa.term.snomedct.SctHandlerDescription;
import ca.uhn.fhir.jpa.term.snomedct.SctHandlerRelationship;
import ca.uhn.fhir.jpa.util.Counter;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.ValidateUtil;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Charsets;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.ValueSet;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.*;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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

public class TerminologyLoaderSvcImpl implements IHapiTerminologyLoaderSvc {
	public static final String SCT_FILE_CONCEPT = "Terminology/sct2_Concept_Full_";
	public static final String SCT_FILE_DESCRIPTION = "Terminology/sct2_Description_Full-en";
	public static final String SCT_FILE_RELATIONSHIP = "Terminology/sct2_Relationship_Full";

	public static final String IMGTHLA_HLA_NOM_TXT = "hla_nom.txt";
	public static final String IMGTHLA_HLA_XML = "hla.xml";

	public static final String CUSTOM_CONCEPTS_FILE = "concepts.csv";
	public static final String CUSTOM_HIERARCHY_FILE = "hierarchy.csv";
	public static final String CUSTOM_CODESYSTEM_JSON = "codesystem.json";
	public static final String CUSTOM_CODESYSTEM_XML = "codesystem.xml";

	private static final int LOG_INCREMENT = 1000;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TerminologyLoaderSvcImpl.class);

	@Autowired
	private IHapiTerminologySvc myTermSvc;

	// FYI: Hardcoded to R4 because that's what the term svc uses internally
	private final FhirContext myCtx = FhirContext.forR4();

	private void dropCircularRefs(TermConcept theConcept, ArrayList<String> theChain, Map<String, TermConcept> theCode2concept, Counter theCircularCounter) {

		theChain.add(theConcept.getCode());
		for (Iterator<TermConceptParentChildLink> childIter = theConcept.getChildren().iterator(); childIter.hasNext(); ) {
			TermConceptParentChildLink next = childIter.next();
			TermConcept nextChild = next.getChild();
			if (theChain.contains(nextChild.getCode())) {

				StringBuilder b = new StringBuilder();
				b.append("Removing circular reference code ");
				b.append(nextChild.getCode());
				b.append(" from parent ");
				b.append(next.getParent().getCode());
				b.append(". Chain was: ");
				for (String nextInChain : theChain) {
					TermConcept nextCode = theCode2concept.get(nextInChain);
					b.append(nextCode.getCode());
					b.append('[');
					b.append(StringUtils.substring(nextCode.getDisplay(), 0, 20).replace("[", "").replace("]", "").trim());
					b.append("] ");
				}
				ourLog.info(b.toString(), theConcept.getCode());
				childIter.remove();
				nextChild.getParents().remove(next);

			} else {
				dropCircularRefs(nextChild, theChain, theCode2concept, theCircularCounter);
			}
		}
		theChain.remove(theChain.size() - 1);

	}

	private void iterateOverZipFile(LoadedFileDescriptors theDescriptors, String theFileNamePart, IRecordHandler theHandler, char theDelimiter, QuoteMode theQuoteMode, boolean theIsPartialFilename) {

		boolean foundMatch = false;
		for (FileDescriptor nextZipBytes : theDescriptors.getUncompressedFileDescriptors()) {
			String nextFilename = nextZipBytes.getFilename();
			boolean matches;
			if (theIsPartialFilename) {
				matches = nextFilename.contains(theFileNamePart);
			} else {
				matches = nextFilename.endsWith("/" + theFileNamePart) || nextFilename.equals(theFileNamePart);
			}

			if (matches) {
				ourLog.info("Processing file {}", nextFilename);
				foundMatch = true;

				Reader reader;
				CSVParser parsed;
				try {
					reader = new InputStreamReader(nextZipBytes.getInputStream(), Charsets.UTF_8);

					if (ourLog.isTraceEnabled()) {
						String contents = IOUtils.toString(reader);
						ourLog.info("File contents for: {}\n{}", nextFilename, contents);
						reader = new StringReader(contents);
					}

					CSVFormat format = CSVFormat.newFormat(theDelimiter).withFirstRecordAsHeader();
					if (theQuoteMode != null) {
						format = format.withQuote('"').withQuoteMode(theQuoteMode);
					}
					parsed = new CSVParser(reader, format);
					Iterator<CSVRecord> iter = parsed.iterator();
					ourLog.debug("Header map: {}", parsed.getHeaderMap());

					int count = 0;
					int nextLoggedCount = 0;
					while (iter.hasNext()) {
						CSVRecord nextRecord = iter.next();
						if (nextRecord.isConsistent() == false) {
							continue;
						}
						theHandler.accept(nextRecord);
						count++;
						if (count >= nextLoggedCount) {
							ourLog.info(" * Processed {} records in {}", count, nextFilename);
							nextLoggedCount += LOG_INCREMENT;
						}
					}

				} catch (IOException e) {
					throw new InternalErrorException(e);
				}
			}

		}

		if (!foundMatch) {
			throw new InvalidRequestException("Did not find file matching " + theFileNamePart);
		}

	}

	@Override
	public UploadStatistics loadImgthla(List<FileDescriptor> theFiles, RequestDetails theRequestDetails) {
		LoadedFileDescriptors descriptors = null;
		try {
			descriptors = new LoadedFileDescriptors(theFiles);
			List<String> mandatoryFilenameFragments = Arrays.asList(
				IMGTHLA_HLA_NOM_TXT,
				IMGTHLA_HLA_XML
			);
			descriptors.verifyMandatoryFilesExist(mandatoryFilenameFragments);

			ourLog.info("Beginning IMGTHLA processing");

			return processImgthlaFiles(descriptors, theRequestDetails);
		} finally {
			IOUtils.closeQuietly(descriptors);
		}
	}

	@Override
	public UploadStatistics loadLoinc(List<FileDescriptor> theFiles, RequestDetails theRequestDetails) {
		try (LoadedFileDescriptors descriptors = new LoadedFileDescriptors(theFiles)) {
			List<String> loincUploadPropertiesFragment = Arrays.asList(
				LOINC_UPLOAD_PROPERTIES_FILE.getCode()
			);
			descriptors.verifyMandatoryFilesExist(loincUploadPropertiesFragment);

			Properties uploadProperties = getProperties(descriptors, LOINC_UPLOAD_PROPERTIES_FILE.getCode());

			List<String> mandatoryFilenameFragments = Arrays.asList(
				uploadProperties.getProperty(LOINC_ANSWERLIST_FILE.getCode(), LOINC_ANSWERLIST_FILE_DEFAULT.getCode()),
				uploadProperties.getProperty(LOINC_ANSWERLIST_LINK_FILE.getCode(), LOINC_ANSWERLIST_LINK_FILE_DEFAULT.getCode()),
				uploadProperties.getProperty(LOINC_DOCUMENT_ONTOLOGY_FILE.getCode(), LOINC_DOCUMENT_ONTOLOGY_FILE_DEFAULT.getCode()),
				uploadProperties.getProperty(LOINC_FILE.getCode(), LOINC_FILE_DEFAULT.getCode()),
				uploadProperties.getProperty(LOINC_HIERARCHY_FILE.getCode(), LOINC_HIERARCHY_FILE_DEFAULT.getCode()),
				uploadProperties.getProperty(LOINC_IEEE_MEDICAL_DEVICE_CODE_MAPPING_TABLE_FILE.getCode(), LOINC_IEEE_MEDICAL_DEVICE_CODE_MAPPING_TABLE_FILE_DEFAULT.getCode()),
				uploadProperties.getProperty(LOINC_IMAGING_DOCUMENT_CODES_FILE.getCode(), LOINC_IMAGING_DOCUMENT_CODES_FILE_DEFAULT.getCode()),
				uploadProperties.getProperty(LOINC_PART_FILE.getCode(), LOINC_PART_FILE_DEFAULT.getCode()),
				uploadProperties.getProperty(LOINC_PART_LINK_FILE.getCode(), LOINC_PART_LINK_FILE_DEFAULT.getCode()),
				uploadProperties.getProperty(LOINC_PART_RELATED_CODE_MAPPING_FILE.getCode(), LOINC_PART_RELATED_CODE_MAPPING_FILE_DEFAULT.getCode()),
				uploadProperties.getProperty(LOINC_RSNA_PLAYBOOK_FILE.getCode(), LOINC_RSNA_PLAYBOOK_FILE_DEFAULT.getCode()),
				uploadProperties.getProperty(LOINC_TOP2000_COMMON_LAB_RESULTS_SI_FILE.getCode(), LOINC_TOP2000_COMMON_LAB_RESULTS_SI_FILE_DEFAULT.getCode()),
				uploadProperties.getProperty(LOINC_TOP2000_COMMON_LAB_RESULTS_US_FILE.getCode(), LOINC_TOP2000_COMMON_LAB_RESULTS_US_FILE_DEFAULT.getCode()),
				uploadProperties.getProperty(LOINC_UNIVERSAL_LAB_ORDER_VALUESET_FILE.getCode(), LOINC_UNIVERSAL_LAB_ORDER_VALUESET_FILE_DEFAULT.getCode())
			);
			descriptors.verifyMandatoryFilesExist(mandatoryFilenameFragments);

			List<String> optionalFilenameFragments = Arrays.asList(
				uploadProperties.getProperty(LOINC_GROUP_FILE.getCode(), LOINC_GROUP_FILE_DEFAULT.getCode()),
				uploadProperties.getProperty(LOINC_GROUP_TERMS_FILE.getCode(), LOINC_GROUP_TERMS_FILE_DEFAULT.getCode()),
				uploadProperties.getProperty(LOINC_PARENT_GROUP_FILE.getCode(), LOINC_PARENT_GROUP_FILE_DEFAULT.getCode())
			);
			descriptors.verifyOptionalFilesExist(optionalFilenameFragments);

			ourLog.info("Beginning LOINC processing");

			return processLoincFiles(descriptors, theRequestDetails, uploadProperties);
		}
	}

	@NotNull
	private Properties getProperties(LoadedFileDescriptors theDescriptors, String thePropertiesFile) {
		Properties retVal = new Properties();
		for (FileDescriptor next : theDescriptors.getUncompressedFileDescriptors()) {
			if (next.getFilename().endsWith(thePropertiesFile)) {
				try {
					try (InputStream inputStream = next.getInputStream()) {
						retVal.load(inputStream);
					}
				} catch (IOException e) {
					throw new InternalErrorException("Failed to read " + thePropertiesFile, e);
				}
			}
		}
		return retVal;
	}

	@Override
	public UploadStatistics loadSnomedCt(List<FileDescriptor> theFiles, RequestDetails theRequestDetails) {
		try (LoadedFileDescriptors descriptors = new LoadedFileDescriptors(theFiles)) {

			List<String> expectedFilenameFragments = Arrays.asList(
				SCT_FILE_DESCRIPTION,
				SCT_FILE_RELATIONSHIP,
				SCT_FILE_CONCEPT);
			descriptors.verifyMandatoryFilesExist(expectedFilenameFragments);

			ourLog.info("Beginning SNOMED CT processing");

			return processSnomedCtFiles(descriptors, theRequestDetails);
		}
	}

	@Override
	public UploadStatistics loadCustom(String theSystem, List<FileDescriptor> theFiles, RequestDetails theRequestDetails) {
		try (LoadedFileDescriptors descriptors = new LoadedFileDescriptors(theFiles)) {
			final Map<String, TermConcept> code2concept = new HashMap<>();
			IRecordHandler handler;

			Optional<String> codeSystemContent = loadFile(descriptors, CUSTOM_CODESYSTEM_JSON, CUSTOM_CODESYSTEM_XML);
			CodeSystem codeSystem;
			if (codeSystemContent.isPresent()) {
				codeSystem = EncodingEnum
					.detectEncoding(codeSystemContent.get())
					.newParser(myCtx)
					.parseResource(CodeSystem.class, codeSystemContent.get());
				ValidateUtil.isTrueOrThrowInvalidRequest(theSystem.equalsIgnoreCase(codeSystem.getUrl()), "CodeSystem.url does not match the supplied system: %s", theSystem);
				ValidateUtil.isTrueOrThrowInvalidRequest(CodeSystem.CodeSystemContentMode.NOTPRESENT.equals(codeSystem.getContent()), "CodeSystem.content does not match the expected value: %s", CodeSystem.CodeSystemContentMode.NOTPRESENT.toCode());
			} else {
				codeSystem = new CodeSystem();
				codeSystem.setUrl(theSystem);
				codeSystem.setContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);
			}

			TermCodeSystemVersion csv = new TermCodeSystemVersion();

			// Concept File
			handler = new ConceptHandler(code2concept, csv);
			iterateOverZipFile(descriptors, CUSTOM_CONCEPTS_FILE, handler, ',', QuoteMode.NON_NUMERIC, false);

			// Hierarchy
			if (descriptors.hasFile(CUSTOM_HIERARCHY_FILE)) {
				handler = new HierarchyHandler(code2concept);
				iterateOverZipFile(descriptors, CUSTOM_HIERARCHY_FILE, handler, ',', QuoteMode.NON_NUMERIC, false);
			}

			// Add root concepts to CodeSystemVersion
			for (TermConcept nextConcept : code2concept.values()) {
				if (nextConcept.getParents().isEmpty()) {
					csv.getConcepts().add(nextConcept);
				}
			}

			IIdType target = storeCodeSystem(theRequestDetails, csv, codeSystem, null, null);
			return new UploadStatistics(code2concept.size(), target);
		}
	}

	private Optional<String> loadFile(LoadedFileDescriptors theDescriptors, String... theFilenames) {
		for (FileDescriptor next : theDescriptors.getUncompressedFileDescriptors()) {
			for (String nextFilename : theFilenames) {
				if (next.getFilename().endsWith(nextFilename)) {
					try {
						String contents = IOUtils.toString(next.getInputStream(), Charsets.UTF_8);
						return Optional.of(contents);
					} catch (IOException e) {
						throw new InternalErrorException(e);
					}
				}
			}
		}
		return Optional.empty();
	}

	UploadStatistics processImgthlaFiles(LoadedFileDescriptors theDescriptors, RequestDetails theRequestDetails) {
		final TermCodeSystemVersion codeSystemVersion = new TermCodeSystemVersion();
		final Map<String, TermConcept> code2concept = new HashMap<>();
		final List<ValueSet> valueSets = new ArrayList<>();
		final List<ConceptMap> conceptMaps = new ArrayList<>();

		CodeSystem imgthlaCs;
		try {
			String imgthlaCsString = IOUtils.toString(BaseHapiTerminologySvcImpl.class.getResourceAsStream("/ca/uhn/fhir/jpa/term/imgthla/imgthla.xml"), Charsets.UTF_8);
			imgthlaCs = FhirContext.forR4().newXmlParser().parseResource(CodeSystem.class, imgthlaCsString);
		} catch (IOException e) {
			throw new InternalErrorException("Failed to load imgthla.xml", e);
		}

		Map<String, CodeSystem.PropertyType> propertyNamesToTypes = new HashMap<>();
		for (CodeSystem.PropertyComponent nextProperty : imgthlaCs.getProperty()) {
			String nextPropertyCode = nextProperty.getCode();
			CodeSystem.PropertyType nextPropertyType = nextProperty.getType();
			if (isNotBlank(nextPropertyCode)) {
				propertyNamesToTypes.put(nextPropertyCode, nextPropertyType);
			}
		}

		boolean foundHlaNom = false;
		boolean foundHlaXml = false;
		for (FileDescriptor nextZipBytes : theDescriptors.getUncompressedFileDescriptors()) {
			String nextFilename = nextZipBytes.getFilename();

			if (!IMGTHLA_HLA_NOM_TXT.equals(nextFilename) && !nextFilename.endsWith("/" + IMGTHLA_HLA_NOM_TXT)
				&& !IMGTHLA_HLA_XML.equals(nextFilename) && !nextFilename.endsWith("/" + IMGTHLA_HLA_XML)) {
				ourLog.info("Skipping unexpected file {}", nextFilename);
				continue;
			}

			if (IMGTHLA_HLA_NOM_TXT.equals(nextFilename) || nextFilename.endsWith("/" + IMGTHLA_HLA_NOM_TXT)) {
				// process colon-delimited hla_nom.txt file
				ourLog.info("Processing file {}", nextFilename);

//				IRecordHandler handler = new HlaNomTxtHandler(codeSystemVersion, code2concept, propertyNamesToTypes);
//				AntigenSource antigenSource = new WmdaAntigenSource(hlaNomFilename, relSerSerFilename, relDnaSerFilename);

				Reader reader = null;
				try {
					reader = new InputStreamReader(nextZipBytes.getInputStream(), Charsets.UTF_8);

					if (ourLog.isTraceEnabled()) {
						String contents = IOUtils.toString(reader);
						ourLog.info("File contents for: {}\n{}", nextFilename, contents);
						reader = new StringReader(contents);
					}

					LineNumberReader lnr = new LineNumberReader(reader);
					while (lnr.readLine() != null) {
					}
					ourLog.warn("Lines read from {}:  {}", nextFilename, lnr.getLineNumber());

				} catch (IOException e) {
					throw new InternalErrorException(e);
				} finally {
					IOUtils.closeQuietly(reader);
				}

				foundHlaNom = true;
			}

			if (IMGTHLA_HLA_XML.equals(nextFilename) || nextFilename.endsWith("/" + IMGTHLA_HLA_XML)) {
				// process hla.xml file
				ourLog.info("Processing file {}", nextFilename);

//				IRecordHandler handler = new HlaXmlHandler(codeSystemVersion, code2concept, propertyNamesToTypes);
//				AlleleSource alleleSource = new HlaXmlAlleleSource(hlaXmlFilename);

				Reader reader = null;
				try {
					reader = new InputStreamReader(nextZipBytes.getInputStream(), Charsets.UTF_8);

					if (ourLog.isTraceEnabled()) {
						String contents = IOUtils.toString(reader);
						ourLog.info("File contents for: {}\n{}", nextFilename, contents);
						reader = new StringReader(contents);
					}

					LineNumberReader lnr = new LineNumberReader(reader);
					while (lnr.readLine() != null) {
					}
					ourLog.warn("Lines read from {}:  {}", nextFilename, lnr.getLineNumber());

				} catch (IOException e) {
					throw new InternalErrorException(e);
				} finally {
					IOUtils.closeQuietly(reader);
				}

				foundHlaXml = true;
			}

		}

		if (!foundHlaNom) {
			throw new InvalidRequestException("Did not find file matching " + IMGTHLA_HLA_NOM_TXT);
		}

		if (!foundHlaXml) {
			throw new InvalidRequestException("Did not find file matching " + IMGTHLA_HLA_XML);
		}

		int valueSetCount = valueSets.size();
		int rootConceptCount = codeSystemVersion.getConcepts().size();
		int conceptCount = code2concept.size();
		ourLog.info("Have {} total concepts, {} root concepts, {} ValueSets", conceptCount, rootConceptCount, valueSetCount);

		// remove this when fully implemented ...
		throw new InternalErrorException("HLA nomenclature terminology upload not yet fully implemented.");

//		IIdType target = storeCodeSystem(theRequestDetails, codeSystemVersion, imgthlaCs, valueSets, conceptMaps);
//
//		return new UploadStatistics(conceptCount, target);
	}

	UploadStatistics processLoincFiles(LoadedFileDescriptors theDescriptors, RequestDetails theRequestDetails, Properties theUploadProperties) {
		final TermCodeSystemVersion codeSystemVersion = new TermCodeSystemVersion();
		final Map<String, TermConcept> code2concept = new HashMap<>();
		final List<ValueSet> valueSets = new ArrayList<>();
		final List<ConceptMap> conceptMaps = new ArrayList<>();

		CodeSystem loincCs;
		try {
			String loincCsString = IOUtils.toString(BaseHapiTerminologySvcImpl.class.getResourceAsStream("/ca/uhn/fhir/jpa/term/loinc/loinc.xml"), Charsets.UTF_8);
			loincCs = FhirContext.forR4().newXmlParser().parseResource(CodeSystem.class, loincCsString);
		} catch (IOException e) {
			throw new InternalErrorException("Failed to load loinc.xml", e);
		}

		Map<String, CodeSystem.PropertyType> propertyNamesToTypes = new HashMap<>();
		for (CodeSystem.PropertyComponent nextProperty : loincCs.getProperty()) {
			String nextPropertyCode = nextProperty.getCode();
			CodeSystem.PropertyType nextPropertyType = nextProperty.getType();
			if (isNotBlank(nextPropertyCode)) {
				propertyNamesToTypes.put(nextPropertyCode, nextPropertyType);
			}
		}

		IRecordHandler handler;

		// Part
		handler = new LoincPartHandler(codeSystemVersion, code2concept);
		iterateOverZipFile(theDescriptors, theUploadProperties.getProperty(LOINC_PART_FILE.getCode(), LOINC_PART_FILE_DEFAULT.getCode()), handler, ',', QuoteMode.NON_NUMERIC, false);
		Map<PartTypeAndPartName, String> partTypeAndPartNameToPartNumber = ((LoincPartHandler) handler).getPartTypeAndPartNameToPartNumber();

		// LOINC codes
		handler = new LoincHandler(codeSystemVersion, code2concept, propertyNamesToTypes, partTypeAndPartNameToPartNumber);
		iterateOverZipFile(theDescriptors, theUploadProperties.getProperty(LOINC_FILE.getCode(), LOINC_FILE_DEFAULT.getCode()), handler, ',', QuoteMode.NON_NUMERIC, false);

		// LOINC hierarchy
		handler = new LoincHierarchyHandler(codeSystemVersion, code2concept);
		iterateOverZipFile(theDescriptors, theUploadProperties.getProperty(LOINC_HIERARCHY_FILE.getCode(), LOINC_HIERARCHY_FILE_DEFAULT.getCode()), handler, ',', QuoteMode.NON_NUMERIC, false);

		// Answer lists (ValueSets of potential answers/values for LOINC "questions")
		handler = new LoincAnswerListHandler(codeSystemVersion, code2concept, valueSets, conceptMaps, theUploadProperties);
		iterateOverZipFile(theDescriptors, theUploadProperties.getProperty(LOINC_ANSWERLIST_FILE.getCode(), LOINC_ANSWERLIST_FILE_DEFAULT.getCode()), handler, ',', QuoteMode.NON_NUMERIC, false);

		// Answer list links (connects LOINC observation codes to answer list codes)
		handler = new LoincAnswerListLinkHandler(code2concept, valueSets);
		iterateOverZipFile(theDescriptors, theUploadProperties.getProperty(LOINC_ANSWERLIST_LINK_FILE.getCode(), LOINC_ANSWERLIST_LINK_FILE_DEFAULT.getCode()), handler, ',', QuoteMode.NON_NUMERIC, false);

		// RSNA playbook
		// Note that this should come before the "Part Related Code Mapping"
		// file because there are some duplicate mappings between these
		// two files, and the RSNA Playbook file has more metadata
		handler = new LoincRsnaPlaybookHandler(code2concept, valueSets, conceptMaps, theUploadProperties);
		iterateOverZipFile(theDescriptors, theUploadProperties.getProperty(LOINC_RSNA_PLAYBOOK_FILE.getCode(), LOINC_RSNA_PLAYBOOK_FILE_DEFAULT.getCode()), handler, ',', QuoteMode.NON_NUMERIC, false);

		// Part link
		handler = new LoincPartLinkHandler(codeSystemVersion, code2concept);
		iterateOverZipFile(theDescriptors, theUploadProperties.getProperty(LOINC_PART_LINK_FILE.getCode(), LOINC_PART_LINK_FILE_DEFAULT.getCode()), handler, ',', QuoteMode.NON_NUMERIC, false);

		// Part related code mapping
		handler = new LoincPartRelatedCodeMappingHandler(code2concept, valueSets, conceptMaps, theUploadProperties);
		iterateOverZipFile(theDescriptors, theUploadProperties.getProperty(LOINC_PART_RELATED_CODE_MAPPING_FILE.getCode(), LOINC_PART_RELATED_CODE_MAPPING_FILE_DEFAULT.getCode()), handler, ',', QuoteMode.NON_NUMERIC, false);

		// Document ontology
		handler = new LoincDocumentOntologyHandler(code2concept, propertyNamesToTypes, valueSets, conceptMaps, theUploadProperties);
		iterateOverZipFile(theDescriptors, theUploadProperties.getProperty(LOINC_DOCUMENT_ONTOLOGY_FILE.getCode(), LOINC_DOCUMENT_ONTOLOGY_FILE_DEFAULT.getCode()), handler, ',', QuoteMode.NON_NUMERIC, false);

		// Top 2000 codes - US
		handler = new LoincTop2000LabResultsUsHandler(code2concept, valueSets, conceptMaps, theUploadProperties);
		iterateOverZipFile(theDescriptors, theUploadProperties.getProperty(LOINC_TOP2000_COMMON_LAB_RESULTS_US_FILE.getCode(), LOINC_TOP2000_COMMON_LAB_RESULTS_US_FILE_DEFAULT.getCode()), handler, ',', QuoteMode.NON_NUMERIC, false);

		// Top 2000 codes - SI
		handler = new LoincTop2000LabResultsSiHandler(code2concept, valueSets, conceptMaps, theUploadProperties);
		iterateOverZipFile(theDescriptors, theUploadProperties.getProperty(LOINC_TOP2000_COMMON_LAB_RESULTS_SI_FILE.getCode(), LOINC_TOP2000_COMMON_LAB_RESULTS_SI_FILE_DEFAULT.getCode()), handler, ',', QuoteMode.NON_NUMERIC, false);

		// Universal lab order ValueSet
		handler = new LoincUniversalOrderSetHandler(code2concept, valueSets, conceptMaps, theUploadProperties);
		iterateOverZipFile(theDescriptors, theUploadProperties.getProperty(LOINC_UNIVERSAL_LAB_ORDER_VALUESET_FILE.getCode(), LOINC_UNIVERSAL_LAB_ORDER_VALUESET_FILE_DEFAULT.getCode()), handler, ',', QuoteMode.NON_NUMERIC, false);

		// IEEE medical device codes
		handler = new LoincIeeeMedicalDeviceCodeHandler(code2concept, valueSets, conceptMaps, theUploadProperties);
		iterateOverZipFile(theDescriptors, theUploadProperties.getProperty(LOINC_IEEE_MEDICAL_DEVICE_CODE_MAPPING_TABLE_FILE.getCode(), LOINC_IEEE_MEDICAL_DEVICE_CODE_MAPPING_TABLE_FILE_DEFAULT.getCode()), handler, ',', QuoteMode.NON_NUMERIC, false);

		// Imaging document codes
		handler = new LoincImagingDocumentCodeHandler(code2concept, valueSets, conceptMaps, theUploadProperties);
		iterateOverZipFile(theDescriptors, theUploadProperties.getProperty(LOINC_IMAGING_DOCUMENT_CODES_FILE.getCode(), LOINC_IMAGING_DOCUMENT_CODES_FILE_DEFAULT.getCode()), handler, ',', QuoteMode.NON_NUMERIC, false);

		// Group
		handler = new LoincGroupFileHandler(code2concept, valueSets, conceptMaps, theUploadProperties);
		iterateOverZipFile(theDescriptors, theUploadProperties.getProperty(LOINC_GROUP_FILE.getCode(), LOINC_GROUP_FILE_DEFAULT.getCode()), handler, ',', QuoteMode.NON_NUMERIC, false);

		// Group terms
		handler = new LoincGroupTermsFileHandler(code2concept, valueSets, conceptMaps, theUploadProperties);
		iterateOverZipFile(theDescriptors, theUploadProperties.getProperty(LOINC_GROUP_TERMS_FILE.getCode(), LOINC_GROUP_TERMS_FILE_DEFAULT.getCode()), handler, ',', QuoteMode.NON_NUMERIC, false);

		// Parent group
		handler = new LoincParentGroupFileHandler(code2concept, valueSets, conceptMaps, theUploadProperties);
		iterateOverZipFile(theDescriptors, theUploadProperties.getProperty(LOINC_PARENT_GROUP_FILE.getCode(), LOINC_PARENT_GROUP_FILE_DEFAULT.getCode()), handler, ',', QuoteMode.NON_NUMERIC, false);

		IOUtils.closeQuietly(theDescriptors);

		valueSets.add(getValueSetLoincAll());

		for (Entry<String, TermConcept> next : code2concept.entrySet()) {
			TermConcept nextConcept = next.getValue();
			if (nextConcept.getParents().isEmpty()) {
				codeSystemVersion.getConcepts().add(nextConcept);
			}
		}

		int valueSetCount = valueSets.size();
		int rootConceptCount = codeSystemVersion.getConcepts().size();
		int conceptCount = code2concept.size();
		ourLog.info("Have {} total concepts, {} root concepts, {} ValueSets", conceptCount, rootConceptCount, valueSetCount);

		IIdType target = storeCodeSystem(theRequestDetails, codeSystemVersion, loincCs, valueSets, conceptMaps);

		return new UploadStatistics(conceptCount, target);
	}

	private ValueSet getValueSetLoincAll() {
		ValueSet retVal = new ValueSet();

		retVal.setId("loinc-all");
		retVal.setUrl("http://loinc.org/vs");
		retVal.setVersion("1.0.0");
		retVal.setName("All LOINC codes");
		retVal.setStatus(Enumerations.PublicationStatus.ACTIVE);
		retVal.setDate(new Date());
		retVal.setPublisher("Regenstrief Institute, Inc.");
		retVal.setDescription("A value set that includes all LOINC codes");
		retVal.setCopyright("This content from LOINC® is copyright © 1995 Regenstrief Institute, Inc. and the LOINC Committee, and available at no cost under the license at https://loinc.org/license/");
		retVal.getCompose().addInclude().setSystem(IHapiTerminologyLoaderSvc.LOINC_URI);

		return retVal;
	}

	private UploadStatistics processSnomedCtFiles(LoadedFileDescriptors theDescriptors, RequestDetails theRequestDetails) {
		final TermCodeSystemVersion codeSystemVersion = new TermCodeSystemVersion();
		final Map<String, TermConcept> id2concept = new HashMap<>();
		final Map<String, TermConcept> code2concept = new HashMap<>();
		final Set<String> validConceptIds = new HashSet<>();

		IRecordHandler handler = new SctHandlerConcept(validConceptIds);
		iterateOverZipFile(theDescriptors, SCT_FILE_CONCEPT, handler, '\t', null, true);

		ourLog.info("Have {} valid concept IDs", validConceptIds.size());

		handler = new SctHandlerDescription(validConceptIds, code2concept, id2concept, codeSystemVersion);
		iterateOverZipFile(theDescriptors, SCT_FILE_DESCRIPTION, handler, '\t', null, true);

		ourLog.info("Got {} concepts, cloning map", code2concept.size());
		final HashMap<String, TermConcept> rootConcepts = new HashMap<>(code2concept);

		handler = new SctHandlerRelationship(codeSystemVersion, rootConcepts, code2concept);
		iterateOverZipFile(theDescriptors, SCT_FILE_RELATIONSHIP, handler, '\t', null, true);

		IOUtils.closeQuietly(theDescriptors);

		ourLog.info("Looking for root codes");
		rootConcepts
			.entrySet()
			.removeIf(theStringTermConceptEntry -> theStringTermConceptEntry.getValue().getParents().isEmpty() == false);

		ourLog.info("Done loading SNOMED CT files - {} root codes, {} total codes", rootConcepts.size(), code2concept.size());

		Counter circularCounter = new Counter();
		for (TermConcept next : rootConcepts.values()) {
			long count = circularCounter.getThenAdd();
			float pct = ((float) count / rootConcepts.size()) * 100.0f;
			ourLog.info(" * Scanning for circular refs - have scanned {} / {} codes ({}%)", count, rootConcepts.size(), pct);
			dropCircularRefs(next, new ArrayList<>(), code2concept, circularCounter);
		}

		codeSystemVersion.getConcepts().addAll(rootConcepts.values());

		CodeSystem cs = new org.hl7.fhir.r4.model.CodeSystem();
		cs.setUrl(SCT_URI);
		cs.setName("SNOMED CT");
		cs.setContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);
		IIdType target = storeCodeSystem(theRequestDetails, codeSystemVersion, cs, null, null);

		return new UploadStatistics(code2concept.size(), target);
	}

	@VisibleForTesting
	void setTermSvcForUnitTests(IHapiTerminologySvc theTermSvc) {
		myTermSvc = theTermSvc;
	}

	private IIdType storeCodeSystem(RequestDetails theRequestDetails, final TermCodeSystemVersion theCodeSystemVersion, CodeSystem theCodeSystem, List<ValueSet> theValueSets, List<ConceptMap> theConceptMaps) {
		Validate.isTrue(theCodeSystem.getContent() == CodeSystem.CodeSystemContentMode.NOTPRESENT);

		List<ValueSet> valueSets = ObjectUtils.defaultIfNull(theValueSets, Collections.emptyList());
		List<ConceptMap> conceptMaps = ObjectUtils.defaultIfNull(theConceptMaps, Collections.emptyList());

		IIdType retVal;
		myTermSvc.setProcessDeferred(false);
		retVal = myTermSvc.storeNewCodeSystemVersion(theCodeSystem, theCodeSystemVersion, theRequestDetails, valueSets, conceptMaps);
		myTermSvc.setProcessDeferred(true);

		return retVal;
	}

	static class LoadedFileDescriptors implements Closeable {

		private List<File> myTemporaryFiles = new ArrayList<>();
		private List<IHapiTerminologyLoaderSvc.FileDescriptor> myUncompressedFileDescriptors = new ArrayList<>();

		LoadedFileDescriptors(List<IHapiTerminologyLoaderSvc.FileDescriptor> theFileDescriptors) {
			try {
				for (FileDescriptor next : theFileDescriptors) {
					if (next.getFilename().toLowerCase().endsWith(".zip")) {
						ourLog.info("Uncompressing {} into temporary files", next.getFilename());
						try (InputStream inputStream = next.getInputStream()) {
							ZipInputStream zis = new ZipInputStream(new BufferedInputStream(inputStream));
							for (ZipEntry nextEntry; (nextEntry = zis.getNextEntry()) != null; ) {
								BOMInputStream fis = new BOMInputStream(zis);
								File nextTemporaryFile = File.createTempFile("hapifhir", ".tmp");
								nextTemporaryFile.deleteOnExit();
								FileOutputStream fos = new FileOutputStream(nextTemporaryFile, false);
								IOUtils.copy(fis, fos);
								String nextEntryFileName = nextEntry.getName();
								myUncompressedFileDescriptors.add(new FileDescriptor() {
									@Override
									public String getFilename() {
										return nextEntryFileName;
									}

									@Override
									public InputStream getInputStream() {
										try {
											return new FileInputStream(nextTemporaryFile);
										} catch (FileNotFoundException e) {
											throw new InternalErrorException(e);
										}
									}
								});
								myTemporaryFiles.add(nextTemporaryFile);
							}
						}
					} else {
						myUncompressedFileDescriptors.add(next);
					}

				}
			} catch (Exception e) {
				close();
				throw new InternalErrorException(e);
			}
		}

		boolean hasFile(String theFilename) {
			return myUncompressedFileDescriptors
				.stream()
				.map(t -> t.getFilename().replaceAll(".*[\\\\/]", "")) // Strip the path from the filename
				.anyMatch(t -> t.equals(theFilename));
		}

		@Override
		public void close() {
			for (File next : myTemporaryFiles) {
				FileUtils.deleteQuietly(next);
			}
		}

		List<IHapiTerminologyLoaderSvc.FileDescriptor> getUncompressedFileDescriptors() {
			return myUncompressedFileDescriptors;
		}

		private List<String> notFound(List<String> theExpectedFilenameFragments) {
			Set<String> foundFragments = new HashSet<>();
			for (String nextExpected : theExpectedFilenameFragments) {
				for (FileDescriptor next : myUncompressedFileDescriptors) {
					if (next.getFilename().contains(nextExpected)) {
						foundFragments.add(nextExpected);
						break;
					}
				}
			}

			ArrayList<String> notFoundFileNameFragments = new ArrayList<>(theExpectedFilenameFragments);
			notFoundFileNameFragments.removeAll(foundFragments);
			return notFoundFileNameFragments;
		}

		private void verifyMandatoryFilesExist(List<String> theExpectedFilenameFragments) {
			List<String> notFound = notFound(theExpectedFilenameFragments);
			if (!notFound.isEmpty()) {
				throw new UnprocessableEntityException("Could not find the following mandatory files in input: " + notFound);
			}
		}

		private void verifyOptionalFilesExist(List<String> theExpectedFilenameFragments) {
			List<String> notFound = notFound(theExpectedFilenameFragments);
			if (!notFound.isEmpty()) {
				ourLog.warn("Could not find the following optional files: " + notFound);
			}
		}


	}

	public static String firstNonBlank(String... theStrings) {
		String retVal = "";
		for (String nextString : theStrings) {
			if (isNotBlank(nextString)) {
				retVal = nextString;
				break;
			}
		}
		return retVal;
	}

	public static TermConcept getOrCreateConcept(TermCodeSystemVersion codeSystemVersion, Map<String, TermConcept> id2concept, String id) {
		TermConcept concept = id2concept.get(id);
		if (concept == null) {
			concept = new TermConcept();
			id2concept.put(id, concept);
			concept.setCodeSystemVersion(codeSystemVersion);
		}
		return concept;
	}
}
