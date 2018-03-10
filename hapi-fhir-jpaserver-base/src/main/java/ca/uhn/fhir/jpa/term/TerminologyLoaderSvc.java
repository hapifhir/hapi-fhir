package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink;
import ca.uhn.fhir.jpa.term.loinc.*;
import ca.uhn.fhir.jpa.term.snomedct.SctHandlerConcept;
import ca.uhn.fhir.jpa.term.snomedct.SctHandlerDescription;
import ca.uhn.fhir.jpa.term.snomedct.SctHandlerRelationship;
import ca.uhn.fhir.jpa.util.Counter;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Charsets;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.ValueSet;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/*
 * #%L
 * HAPI FHIR JPA Server
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

public class TerminologyLoaderSvc implements IHapiTerminologyLoaderSvc {
	public static final String LOINC_FILE = "loinc.csv";
	public static final String LOINC_HIERARCHY_FILE = "MULTI-AXIAL_HIERARCHY.CSV";
	public static final String LOINC_ANSWERLIST_FILE = "AnswerList_Beta_1.csv";
	public static final String LOINC_ANSWERLIST_LINK_FILE = "LoincAnswerListLink_Beta_1.csv";
	public static final String LOINC_PART_FILE = "Part_Beta_1.csv";
	public static final String LOINC_PART_LINK_FILE = "LoincPartLink_Beta_1.csv";
	public static final String LOINC_PART_RELATED_CODE_MAPPING_FILE = "PartRelatedCodeMapping_Beta_1.csv";
	public static final String SCT_FILE_CONCEPT = "Terminology/sct2_Concept_Full_";
	public static final String SCT_FILE_DESCRIPTION = "Terminology/sct2_Description_Full-en";
	public static final String SCT_FILE_RELATIONSHIP = "Terminology/sct2_Relationship_Full";
	private static final int LOG_INCREMENT = 100000;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TerminologyLoaderSvc.class);

	@Autowired
	private IHapiTerminologySvc myTermSvc;
	@Autowired(required = false)
	private IHapiTerminologySvcDstu3 myTermSvcDstu3;
	@Autowired(required = false)
	private IHapiTerminologySvcR4 myTermSvcR4;

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

	private void iterateOverZipFile(List<byte[]> theZipBytes, String fileNamePart, IRecordHandler handler, char theDelimiter, QuoteMode theQuoteMode) {
		boolean found = false;

		for (byte[] nextZipBytes : theZipBytes) {
			ZipInputStream zis = new ZipInputStream(new BufferedInputStream(new ByteArrayInputStream(nextZipBytes)));
			try {
				for (ZipEntry nextEntry; (nextEntry = zis.getNextEntry()) != null; ) {

					String nextFilename = nextEntry.getName();
					if (nextFilename.contains(fileNamePart)) {
						ourLog.info("Processing file {}", nextFilename);
						found = true;

						Reader reader;
						CSVParser parsed;
						try {
							reader = new InputStreamReader(new BOMInputStream(zis), Charsets.UTF_8);
							CSVFormat format = CSVFormat.newFormat(theDelimiter).withFirstRecordAsHeader();
							if (theQuoteMode != null) {
								format = format.withQuote('"').withQuoteMode(theQuoteMode);
							}
							parsed = new CSVParser(reader, format);
							Iterator<CSVRecord> iter = parsed.iterator();
							ourLog.debug("Header map: {}", parsed.getHeaderMap());

							int count = 0;
							int logIncrement = LOG_INCREMENT;
							int nextLoggedCount = 0;
							while (iter.hasNext()) {
								CSVRecord nextRecord = iter.next();
								handler.accept(nextRecord);
								count++;
								if (count >= nextLoggedCount) {
									ourLog.info(" * Processed {} records in {}", count, nextFilename);
									nextLoggedCount += logIncrement;
								}
							}

						} catch (IOException e) {
							throw new InternalErrorException(e);
						}
					}
				}
			} catch (IOException e) {
				throw new InternalErrorException(e);
			} finally {
				IOUtils.closeQuietly(zis);
			}
		}

		// This should always be true, but just in case we've introduced a bug...
		Validate.isTrue(found);
	}

	@Override
	public UploadStatistics loadLoinc(List<byte[]> theZipBytes, RequestDetails theRequestDetails) {
		List<String> expectedFilenameFragments = Arrays.asList(
			LOINC_FILE,
			LOINC_HIERARCHY_FILE);

		verifyMandatoryFilesExist(theZipBytes, expectedFilenameFragments);

		ourLog.info("Beginning LOINC processing");

		return processLoincFiles(theZipBytes, theRequestDetails);
	}

	@Override
	public UploadStatistics loadSnomedCt(List<byte[]> theZipBytes, RequestDetails theRequestDetails) {
		List<String> expectedFilenameFragments = Arrays.asList(SCT_FILE_DESCRIPTION, SCT_FILE_RELATIONSHIP, SCT_FILE_CONCEPT);

		verifyMandatoryFilesExist(theZipBytes, expectedFilenameFragments);

		ourLog.info("Beginning SNOMED CT processing");

		return processSnomedCtFiles(theZipBytes, theRequestDetails);
	}

	UploadStatistics processLoincFiles(List<byte[]> theZipBytes, RequestDetails theRequestDetails) {
		final TermCodeSystemVersion codeSystemVersion = new TermCodeSystemVersion();
		final Map<String, TermConcept> code2concept = new HashMap<>();
		final List<ValueSet> valueSets = new ArrayList<>();

		CodeSystem loincCs;
		try {
			String loincCsString = IOUtils.toString(BaseHapiTerminologySvcImpl.class.getResourceAsStream("/ca/uhn/fhir/jpa/term/loinc/loinc.xml"), Charsets.UTF_8);
			loincCs = FhirContext.forR4().newXmlParser().parseResource(CodeSystem.class, loincCsString);
		} catch (IOException e) {
			throw new InternalErrorException("Failed to load loinc.xml", e);
		}

		Set<String> propertyNames = new HashSet<>();
		for (CodeSystem.PropertyComponent nextProperty : loincCs.getProperty()) {
			if (isNotBlank(nextProperty.getCode())) {
				propertyNames.add(nextProperty.getCode());
			}
		}

		IRecordHandler handler;

		// Loinc Codes
		handler = new LoincHandler(codeSystemVersion, code2concept, propertyNames);
		iterateOverZipFile(theZipBytes, LOINC_FILE, handler, ',', QuoteMode.NON_NUMERIC);

		// Loinc Hierarchy
		handler = new LoincHierarchyHandler(codeSystemVersion, code2concept);
		iterateOverZipFile(theZipBytes, LOINC_HIERARCHY_FILE, handler, ',', QuoteMode.NON_NUMERIC);

		// Answer lists (ValueSets of potential answers/values for loinc "questions")
		handler = new LoincAnswerListHandler(codeSystemVersion, code2concept, propertyNames, valueSets);
		iterateOverZipFile(theZipBytes, LOINC_ANSWERLIST_FILE, handler, ',', QuoteMode.NON_NUMERIC);

		// Answer list links (connects loinc observation codes to answerlist codes)
		handler = new LoincAnswerListLinkHandler(code2concept, valueSets);
		iterateOverZipFile(theZipBytes, LOINC_ANSWERLIST_LINK_FILE, handler, ',', QuoteMode.NON_NUMERIC);

		// Part file
		handler = new LoincPartHandler(codeSystemVersion, code2concept);
		iterateOverZipFile(theZipBytes, LOINC_PART_FILE, handler, ',', QuoteMode.NON_NUMERIC);

		// Part link file
		handler = new LoincPartLinkHandler(codeSystemVersion, code2concept);
		iterateOverZipFile(theZipBytes, LOINC_PART_LINK_FILE, handler, ',', QuoteMode.NON_NUMERIC);

		theZipBytes.clear();

		for (Entry<String, TermConcept> next : code2concept.entrySet()) {
			TermConcept nextConcept = next.getValue();
			if (nextConcept.getParents().isEmpty()) {
				codeSystemVersion.getConcepts().add(nextConcept);
			}
		}

		ourLog.info("Have {} total concepts, {} root concepts", code2concept.size(), codeSystemVersion.getConcepts().size());

		storeCodeSystem(theRequestDetails, codeSystemVersion, loincCs, valueSets);

		return new UploadStatistics(code2concept.size());
	}

	UploadStatistics processSnomedCtFiles(List<byte[]> theZipBytes, RequestDetails theRequestDetails) {
		final TermCodeSystemVersion codeSystemVersion = new TermCodeSystemVersion();
		final Map<String, TermConcept> id2concept = new HashMap<String, TermConcept>();
		final Map<String, TermConcept> code2concept = new HashMap<String, TermConcept>();
		final Set<String> validConceptIds = new HashSet<String>();

		IRecordHandler handler = new SctHandlerConcept(validConceptIds);
		iterateOverZipFile(theZipBytes, SCT_FILE_CONCEPT, handler, '\t', null);

		ourLog.info("Have {} valid concept IDs", validConceptIds.size());

		handler = new SctHandlerDescription(validConceptIds, code2concept, id2concept, codeSystemVersion);
		iterateOverZipFile(theZipBytes, SCT_FILE_DESCRIPTION, handler, '\t', null);

		ourLog.info("Got {} concepts, cloning map", code2concept.size());
		final HashMap<String, TermConcept> rootConcepts = new HashMap<String, TermConcept>(code2concept);

		handler = new SctHandlerRelationship(codeSystemVersion, rootConcepts, code2concept);
		iterateOverZipFile(theZipBytes, SCT_FILE_RELATIONSHIP, handler, '\t', null);

		theZipBytes.clear();

		ourLog.info("Looking for root codes");
		for (Iterator<Entry<String, TermConcept>> iter = rootConcepts.entrySet().iterator(); iter.hasNext(); ) {
			if (iter.next().getValue().getParents().isEmpty() == false) {
				iter.remove();
			}
		}

		ourLog.info("Done loading SNOMED CT files - {} root codes, {} total codes", rootConcepts.size(), code2concept.size());

		Counter circularCounter = new Counter();
		for (TermConcept next : rootConcepts.values()) {
			long count = circularCounter.getThenAdd();
			float pct = ((float) count / rootConcepts.size()) * 100.0f;
			ourLog.info(" * Scanning for circular refs - have scanned {} / {} codes ({}%)", count, rootConcepts.size(), pct);
			dropCircularRefs(next, new ArrayList<String>(), code2concept, circularCounter);
		}

		codeSystemVersion.getConcepts().addAll(rootConcepts.values());

		CodeSystem cs = new org.hl7.fhir.r4.model.CodeSystem();
		cs.setUrl(SCT_URL);
		cs.setContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);
		storeCodeSystem(theRequestDetails, codeSystemVersion, cs, null);

		return new UploadStatistics(code2concept.size());
	}

	@VisibleForTesting
	void setTermSvcDstu3ForUnitTest(IHapiTerminologySvcDstu3 theTermSvcDstu3) {
		myTermSvcDstu3 = theTermSvcDstu3;
	}

	@VisibleForTesting
	void setTermSvcForUnitTests(IHapiTerminologySvc theTermSvc) {
		myTermSvc = theTermSvc;
	}

	private void storeCodeSystem(RequestDetails theRequestDetails, final TermCodeSystemVersion theCodeSystemVersion, CodeSystem theCodeSystem, List<ValueSet> theValueSets) {
		Validate.isTrue(theCodeSystem.getContent() == CodeSystem.CodeSystemContentMode.NOTPRESENT);

		List<ValueSet> valueSets = ObjectUtils.defaultIfNull(theValueSets, Collections.<ValueSet>emptyList());

		myTermSvc.setProcessDeferred(false);
		if (myTermSvcDstu3 != null) {
			myTermSvcDstu3.storeNewCodeSystemVersion(theCodeSystem, theCodeSystemVersion, theRequestDetails, valueSets);
		} else {
			myTermSvcR4.storeNewCodeSystemVersion(theCodeSystem, theCodeSystemVersion, theRequestDetails, valueSets);
		}
		myTermSvc.setProcessDeferred(true);
	}

	private void verifyMandatoryFilesExist(List<byte[]> theZipBytes, List<String> theExpectedFilenameFragments) {
		Set<String> foundFragments = new HashSet<>();

		for (byte[] nextZipBytes : theZipBytes) {
			ZipInputStream zis = new ZipInputStream(new BufferedInputStream(new ByteArrayInputStream(nextZipBytes)));
			try {
				for (ZipEntry nextEntry; (nextEntry = zis.getNextEntry()) != null; ) {
					for (String next : theExpectedFilenameFragments) {
						if (nextEntry.getName().contains(next)) {
							foundFragments.add(next);
						}
					}
				}
			} catch (IOException e) {
				throw new InternalErrorException(e);
			} finally {
				IOUtils.closeQuietly(zis);
			}
		}

		for (String next : theExpectedFilenameFragments) {
			if (!foundFragments.contains(next)) {
				throw new InvalidRequestException("Invalid input zip file, expected zip to contain the following name fragments: " + theExpectedFilenameFragments + " but found: " + foundFragments);
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
			concept.setCodeSystem(codeSystemVersion);
		}
		return concept;
	}


}
