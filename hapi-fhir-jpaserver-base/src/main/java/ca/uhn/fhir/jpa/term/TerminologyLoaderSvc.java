package ca.uhn.fhir.jpa.term;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Charsets;

import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink.RelationshipTypeEnum;
import ca.uhn.fhir.jpa.util.Counter;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public class TerminologyLoaderSvc implements IHapiTerminologyLoaderSvc {
	private static final int LOG_INCREMENT = 100000;

	public static final String LOINC_FILE = "loinc.csv";

	public static final String LOINC_HIERARCHY_FILE = "MULTI-AXIAL_HIERARCHY.CSV";
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TerminologyLoaderSvc.class);

	public static final String SCT_FILE_CONCEPT = "Terminology/sct2_Concept_Full_";
	public static final String SCT_FILE_DESCRIPTION = "Terminology/sct2_Description_Full-en";
	public static final String SCT_FILE_RELATIONSHIP = "Terminology/sct2_Relationship_Full";
	@Autowired
	private IHapiTerminologySvc myTermSvc;

	private void dropCircularRefs(TermConcept theConcept, ArrayList<String> theChain, Map<String, TermConcept> theCode2concept, Counter theCircularCounter) {
		
		theChain.add(theConcept.getCode());
		for (Iterator<TermConceptParentChildLink> childIter = theConcept.getChildren().iterator(); childIter.hasNext();) {
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

	private void extractFiles(List<byte[]> theZipBytes, List<String> theExpectedFilenameFragments) {
		Set<String> foundFragments = new HashSet<String>();

		for (byte[] nextZipBytes : theZipBytes) {
			ZipInputStream zis = new ZipInputStream(new BufferedInputStream(new ByteArrayInputStream(nextZipBytes)));
			try {
				for (ZipEntry nextEntry; (nextEntry = zis.getNextEntry()) != null;) {
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

	public String firstNonBlank(String... theStrings) {
		String retVal = "";
		for (String nextString : theStrings) {
			if (isNotBlank(nextString)) {
				retVal = nextString;
				break;
			}
		}
		return retVal;
	}

	private TermConcept getOrCreateConcept(TermCodeSystemVersion codeSystemVersion, Map<String, TermConcept> id2concept, String id) {
		TermConcept concept = id2concept.get(id);
		if (concept == null) {
			concept = new TermConcept();
			id2concept.put(id, concept);
			concept.setCodeSystem(codeSystemVersion);
		}
		return concept;
	}

	private void iterateOverZipFile(List<byte[]> theZipBytes, String fileNamePart, IRecordHandler handler, char theDelimiter, QuoteMode theQuoteMode) {
		boolean found = false;

		for (byte[] nextZipBytes : theZipBytes) {
			ZipInputStream zis = new ZipInputStream(new BufferedInputStream(new ByteArrayInputStream(nextZipBytes)));
			try {
				for (ZipEntry nextEntry; (nextEntry = zis.getNextEntry()) != null;) {
					ZippedFileInputStream inputStream = new ZippedFileInputStream(zis);

					String nextFilename = nextEntry.getName();
					if (nextFilename.contains(fileNamePart)) {
						ourLog.info("Processing file {}", nextFilename);
						found = true;

						Reader reader = null;
						CSVParser parsed = null;
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
		List<String> expectedFilenameFragments = Arrays.asList(LOINC_FILE, LOINC_HIERARCHY_FILE);

		extractFiles(theZipBytes, expectedFilenameFragments);

		ourLog.info("Beginning LOINC processing");

		return processLoincFiles(theZipBytes, theRequestDetails);
	}

	@Override
	public UploadStatistics loadSnomedCt(List<byte[]> theZipBytes, RequestDetails theRequestDetails) {
		List<String> expectedFilenameFragments = Arrays.asList(SCT_FILE_DESCRIPTION, SCT_FILE_RELATIONSHIP, SCT_FILE_CONCEPT);

		extractFiles(theZipBytes, expectedFilenameFragments);

		ourLog.info("Beginning SNOMED CT processing");

		return processSnomedCtFiles(theZipBytes, theRequestDetails);
	}

	UploadStatistics processLoincFiles(List<byte[]> theZipBytes, RequestDetails theRequestDetails) {
		final TermCodeSystemVersion codeSystemVersion = new TermCodeSystemVersion();
		final Map<String, TermConcept> code2concept = new HashMap<String, TermConcept>();

		IRecordHandler handler = new LoincHandler(codeSystemVersion, code2concept);
		iterateOverZipFile(theZipBytes, LOINC_FILE, handler, ',', QuoteMode.NON_NUMERIC);

		handler = new LoincHierarchyHandler(codeSystemVersion, code2concept);
		iterateOverZipFile(theZipBytes, LOINC_HIERARCHY_FILE, handler, ',', QuoteMode.NON_NUMERIC);

		theZipBytes.clear();
		
		for (Iterator<Entry<String, TermConcept>> iter = code2concept.entrySet().iterator(); iter.hasNext();) {
			Entry<String, TermConcept> next = iter.next();
			// if (isBlank(next.getKey())) {
			// ourLog.info("Removing concept with blankc code[{}] and display [{}", next.getValue().getCode(), next.getValue().getDisplay());
			// iter.remove();
			// continue;
			// }
			TermConcept nextConcept = next.getValue();
			if (nextConcept.getParents().isEmpty()) {
				codeSystemVersion.getConcepts().add(nextConcept);
			}
		}

		ourLog.info("Have {} total concepts, {} root concepts", code2concept.size(), codeSystemVersion.getConcepts().size());

		String url = LOINC_URL;
		storeCodeSystem(theRequestDetails, codeSystemVersion, url);

		return new UploadStatistics(code2concept.size());
	}

	private void storeCodeSystem(RequestDetails theRequestDetails, final TermCodeSystemVersion codeSystemVersion, String url) {
		myTermSvc.setProcessDeferred(false);
		myTermSvc.storeNewCodeSystemVersion(url, codeSystemVersion, theRequestDetails);
		myTermSvc.setProcessDeferred(true);
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
			float pct = ((float)count / rootConcepts.size()) * 100.0f;
			ourLog.info(" * Scanning for circular refs - have scanned {} / {} codes ({}%)", count, rootConcepts.size(), pct);
			dropCircularRefs(next, new ArrayList<String>(), code2concept, circularCounter);
		}

		codeSystemVersion.getConcepts().addAll(rootConcepts.values());
		String url = SCT_URL;
		storeCodeSystem(theRequestDetails, codeSystemVersion, url);

		return new UploadStatistics(code2concept.size());
	}

	@VisibleForTesting
	void setTermSvcForUnitTests(IHapiTerminologySvc theTermSvc) {
		myTermSvc = theTermSvc;
	}

	private interface IRecordHandler {
		void accept(CSVRecord theRecord);
	}

	public class LoincHandler implements IRecordHandler {

		private final Map<String, TermConcept> myCode2Concept;
		private final TermCodeSystemVersion myCodeSystemVersion;

		public LoincHandler(TermCodeSystemVersion theCodeSystemVersion, Map<String, TermConcept> theCode2concept) {
			myCodeSystemVersion = theCodeSystemVersion;
			myCode2Concept = theCode2concept;
		}

		@Override
		public void accept(CSVRecord theRecord) {
			String code = theRecord.get("LOINC_NUM");
			if (isNotBlank(code)) {
				String longCommonName = theRecord.get("LONG_COMMON_NAME");
				String shortName = theRecord.get("SHORTNAME");
				String consumerName = theRecord.get("CONSUMER_NAME");
				String display = firstNonBlank(longCommonName, shortName, consumerName);

				TermConcept concept = new TermConcept(myCodeSystemVersion, code);
				concept.setDisplay(display);

				Validate.isTrue(!myCode2Concept.containsKey(code));
				myCode2Concept.put(code, concept);
			}
		}

	}

	public class LoincHierarchyHandler implements IRecordHandler {

		private Map<String, TermConcept> myCode2Concept;
		private TermCodeSystemVersion myCodeSystemVersion;

		public LoincHierarchyHandler(TermCodeSystemVersion theCodeSystemVersion, Map<String, TermConcept> theCode2concept) {
			myCodeSystemVersion = theCodeSystemVersion;
			myCode2Concept = theCode2concept;
		}

		@Override
		public void accept(CSVRecord theRecord) {
			String parentCode = theRecord.get("IMMEDIATE_PARENT");
			String childCode = theRecord.get("CODE");
			String childCodeText = theRecord.get("CODE_TEXT");

			if (isNotBlank(parentCode) && isNotBlank(childCode)) {
				TermConcept parent = getOrCreate(parentCode, "(unknown)");
				TermConcept child = getOrCreate(childCode, childCodeText);

				parent.addChild(child, RelationshipTypeEnum.ISA);
			}
		}

		private TermConcept getOrCreate(String theCode, String theDisplay) {
			TermConcept retVal = myCode2Concept.get(theCode);
			if (retVal == null) {
				retVal = new TermConcept();
				retVal.setCodeSystem(myCodeSystemVersion);
				retVal.setCode(theCode);
				retVal.setDisplay(theDisplay);
				myCode2Concept.put(theCode, retVal);
			}
			return retVal;
		}

	}

	private final class SctHandlerConcept implements IRecordHandler {

		private Set<String> myValidConceptIds;
		private Map<String, String> myConceptIdToMostRecentDate = new HashMap<String, String>();

		public SctHandlerConcept(Set<String> theValidConceptIds) {
			myValidConceptIds = theValidConceptIds;
		}

		@Override
		public void accept(CSVRecord theRecord) {
			String id = theRecord.get("id");
			String date = theRecord.get("effectiveTime");

			if (!myConceptIdToMostRecentDate.containsKey(id) || myConceptIdToMostRecentDate.get(id).compareTo(date) < 0) {
				boolean active = "1".equals(theRecord.get("active"));
				if (active) {
					myValidConceptIds.add(id);
				} else {
					myValidConceptIds.remove(id);
				}
				myConceptIdToMostRecentDate.put(id, date);
			}

		}
	}

	private final class SctHandlerDescription implements IRecordHandler {
		private final Map<String, TermConcept> myCode2concept;
		private final TermCodeSystemVersion myCodeSystemVersion;
		private final Map<String, TermConcept> myId2concept;
		private Set<String> myValidConceptIds;

		private SctHandlerDescription(Set<String> theValidConceptIds, Map<String, TermConcept> theCode2concept, Map<String, TermConcept> theId2concept, TermCodeSystemVersion theCodeSystemVersion) {
			myCode2concept = theCode2concept;
			myId2concept = theId2concept;
			myCodeSystemVersion = theCodeSystemVersion;
			myValidConceptIds = theValidConceptIds;
		}

		@Override
		public void accept(CSVRecord theRecord) {
			String id = theRecord.get("id");
			boolean active = "1".equals(theRecord.get("active"));
			if (!active) {
				return;
			}
			String conceptId = theRecord.get("conceptId");
			if (!myValidConceptIds.contains(conceptId)) {
				return;
			}

			String term = theRecord.get("term");

			TermConcept concept = getOrCreateConcept(myCodeSystemVersion, myId2concept, id);
			concept.setCode(conceptId);
			concept.setDisplay(term);
			myCode2concept.put(conceptId, concept);
		}
	}

	private final class SctHandlerRelationship implements IRecordHandler {
		private final Map<String, TermConcept> myCode2concept;
		private final TermCodeSystemVersion myCodeSystemVersion;
		private final Map<String, TermConcept> myRootConcepts;

		private SctHandlerRelationship(TermCodeSystemVersion theCodeSystemVersion, HashMap<String, TermConcept> theRootConcepts, Map<String, TermConcept> theCode2concept) {
			myCodeSystemVersion = theCodeSystemVersion;
			myRootConcepts = theRootConcepts;
			myCode2concept = theCode2concept;
		}

		@Override
		public void accept(CSVRecord theRecord) {
			Set<String> ignoredTypes = new HashSet<String>();
			ignoredTypes.add("Method (attribute)");
			ignoredTypes.add("Direct device (attribute)");
			ignoredTypes.add("Has focus (attribute)");
			ignoredTypes.add("Access instrument");
			ignoredTypes.add("Procedure site (attribute)");
			ignoredTypes.add("Causative agent (attribute)");
			ignoredTypes.add("Course (attribute)");
			ignoredTypes.add("Finding site (attribute)");
			ignoredTypes.add("Has definitional manifestation (attribute)");

			String sourceId = theRecord.get("sourceId");
			String destinationId = theRecord.get("destinationId");
			String typeId = theRecord.get("typeId");
			boolean active = "1".equals(theRecord.get("active"));
			
			TermConcept typeConcept = myCode2concept.get(typeId);
			TermConcept sourceConcept = myCode2concept.get(sourceId);
			TermConcept targetConcept = myCode2concept.get(destinationId);
			if (sourceConcept != null && targetConcept != null && typeConcept != null) {
				if (typeConcept.getDisplay().equals("Is a (attribute)")) {
					RelationshipTypeEnum relationshipType = RelationshipTypeEnum.ISA;
					if (!sourceId.equals(destinationId)) {
						if (active) {
							TermConceptParentChildLink link = new TermConceptParentChildLink();
							link.setChild(sourceConcept);
							link.setParent(targetConcept);
							link.setRelationshipType(relationshipType);
							link.setCodeSystem(myCodeSystemVersion);
	
							targetConcept.addChild(sourceConcept, relationshipType);
						} else {
							// not active, so we're removing any existing links
							for (TermConceptParentChildLink next : new ArrayList<TermConceptParentChildLink>(targetConcept.getChildren())) {
								if (next.getRelationshipType() == relationshipType) {
									if (next.getChild().getCode().equals(sourceConcept.getCode())) {
										next.getParent().getChildren().remove(next);
										next.getChild().getParents().remove(next);
									}
								}
							}
						}
					}
				} else if (ignoredTypes.contains(typeConcept.getDisplay())) {
					// ignore
				} else {
					// ourLog.warn("Unknown relationship type: {}/{}", typeId, typeConcept.getDisplay());
				}
			}
		}

	}

	private static class ZippedFileInputStream extends InputStream {

		private ZipInputStream is;

		public ZippedFileInputStream(ZipInputStream is) {
			this.is = is;
		}

		@Override
		public void close() throws IOException {
			is.closeEntry();
		}

		@Override
		public int read() throws IOException {
			return is.read();
		}
	}

}
