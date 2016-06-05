package ca.uhn.fhir.jpa.term;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2016 University Health Network
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
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
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
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.annotations.VisibleForTesting;

import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink.RelationshipTypeEnum;
import ca.uhn.fhir.jpa.term.TerminologyLoaderSvc.LoincHierarchyHandler;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.CoverageIgnore;

public class TerminologyLoaderSvc implements IHapiTerminologyLoaderSvc {
	public static final String LOINC_FILE = "loinc.csv";

	public static final String LOINC_HIERARCHY_FILE = "MULTI-AXIAL_HIERARCHY.CSV";
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TerminologyLoaderSvc.class);

	public static final String SCT_FILE_CONCEPT = "Terminology/sct2_Concept_Full_";
	public static final String SCT_FILE_DESCRIPTION = "Terminology/sct2_Description_Full-en";
	public static final String SCT_FILE_RELATIONSHIP = "Terminology/sct2_Relationship_Full";
	@Autowired
	private IHapiTerminologySvc myTermSvc;

	private void cleanUpTemporaryFiles(Map<String, File> filenameToFile) {
		ourLog.info("Finished terminology file import, cleaning up temporary files");
		for (File nextFile : filenameToFile.values()) {
			nextFile.delete();
		}
	}

	private void dropCircularRefs(TermConcept theConcept, LinkedHashSet<String> theChain, Map<String, TermConcept> theCode2concept) {

		theChain.add(theConcept.getCode());
		for (Iterator<TermConceptParentChildLink> childIter = theConcept.getChildren().iterator(); childIter.hasNext();) {
			TermConceptParentChildLink next = childIter.next();
			TermConcept nextChild = next.getChild();
			if (theChain.contains(nextChild.getCode())) {

				StringBuilder b = new StringBuilder();
				b.append("Removing circular reference code ");
				b.append(nextChild.getCode());
				b.append(" from parent ");
				b.append(nextChild.getCode());
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
			} else {
				dropCircularRefs(nextChild, theChain, theCode2concept);
			}
		}
		theChain.remove(theConcept.getCode());

	}

	private Map<String, File> extractFiles(List<byte[]> theZipBytes, List<String> theExpectedFilenameFragments) {
		Map<String, File> filenameToFile = new HashMap<String, File>();

		for (byte[] nextZipBytes : theZipBytes) {
			ZipInputStream zis = new ZipInputStream(new BufferedInputStream(new ByteArrayInputStream(nextZipBytes)));
			try {
				for (ZipEntry nextEntry; (nextEntry = zis.getNextEntry()) != null;) {
					ZippedFileInputStream inputStream = new ZippedFileInputStream(zis);

					boolean want = false;
					for (String next : theExpectedFilenameFragments) {
						if (nextEntry.getName().contains(next)) {
							want = true;
						}
					}

					if (!want) {
						ourLog.info("Ignoring zip entry: {}", nextEntry.getName());
						continue;
					}

					ourLog.info("Streaming ZIP entry {} into temporary file", nextEntry.getName());

					File nextOutFile = File.createTempFile("hapi_fhir", ".csv");
					nextOutFile.deleteOnExit();
					OutputStream outputStream = new SinkOutputStream(new FileOutputStream(nextOutFile, false), nextEntry.getName());
					try {
						IOUtils.copyLarge(inputStream, outputStream);
					} finally {
						IOUtils.closeQuietly(outputStream);
					}

					filenameToFile.put(nextEntry.getName(), nextOutFile);
				}
			} catch (IOException e) {
				throw new InternalErrorException(e);
			} finally {
				IOUtils.closeQuietly(zis);
			}
		}

		if (filenameToFile.size() != theExpectedFilenameFragments.size()) {
			throw new InvalidRequestException("Invalid input zip file, expected zip to contain the following name fragments: " + theExpectedFilenameFragments + " but found: " + filenameToFile.keySet());
		}
		return filenameToFile;
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

	private void iterateOverZipFile(Map<String, File> theFilenameToFile, String fileNamePart, IRecordHandler handler, char theDelimiter, QuoteMode theQuoteMode) {
		boolean found = false;
		for (Entry<String, File> nextEntry : theFilenameToFile.entrySet()) {

			if (nextEntry.getKey().contains(fileNamePart)) {
				ourLog.info("Processing file {}", nextEntry.getKey());
				found = true;

				Reader reader = null;
				CSVParser parsed = null;
				try {
					reader = new BufferedReader(new FileReader(nextEntry.getValue()));
					CSVFormat format = CSVFormat.newFormat(theDelimiter).withFirstRecordAsHeader();
					if (theQuoteMode != null) {
						format = format.withQuote('"').withQuoteMode(theQuoteMode);
					}
					parsed = new CSVParser(reader, format);
					Iterator<CSVRecord> iter = parsed.iterator();
					ourLog.debug("Header map: {}", parsed.getHeaderMap());

					int count = 0;
					int logIncrement = 100000;
					int nextLoggedCount = logIncrement;
					while (iter.hasNext()) {
						CSVRecord nextRecord = iter.next();
						handler.accept(nextRecord);
						count++;
						if (count >= nextLoggedCount) {
							ourLog.info(" * Processed {} records in {}", count, fileNamePart);
							nextLoggedCount += logIncrement;
						}
					}
				} catch (IOException e) {
					throw new InternalErrorException(e);
				} finally {
					IOUtils.closeQuietly(parsed);
					IOUtils.closeQuietly(reader);
				}
			}
		}

		// This should always be true, but just in case we've introduced a bug...
		Validate.isTrue(found);
	}

	@Override
	public UploadStatistics loadLoinc(List<byte[]> theZipBytes, RequestDetails theRequestDetails) {
		List<String> expectedFilenameFragments = Arrays.asList(LOINC_FILE, LOINC_HIERARCHY_FILE);

		Map<String, File> filenameToFile = extractFiles(theZipBytes, expectedFilenameFragments);

		ourLog.info("Beginning LOINC processing");

		try {
			return processLoincFiles(filenameToFile, theRequestDetails);
		} finally {
			cleanUpTemporaryFiles(filenameToFile);
		}
	}

	@Override
	public UploadStatistics loadSnomedCt(List<byte[]> theZipBytes, RequestDetails theRequestDetails) {
		List<String> expectedFilenameFragments = Arrays.asList(SCT_FILE_DESCRIPTION, SCT_FILE_RELATIONSHIP, SCT_FILE_CONCEPT);

		Map<String, File> filenameToFile = extractFiles(theZipBytes, expectedFilenameFragments);

		ourLog.info("Beginning SNOMED CT processing");

		try {
			return processSnomedCtFiles(filenameToFile, theRequestDetails);
		} finally {
			cleanUpTemporaryFiles(filenameToFile);
		}
	}

	UploadStatistics processLoincFiles(Map<String, File> filenameToFile, RequestDetails theRequestDetails) {
		final TermCodeSystemVersion codeSystemVersion = new TermCodeSystemVersion();
		final Map<String, TermConcept> code2concept = new HashMap<String, TermConcept>();

		IRecordHandler handler = new LoincHandler(codeSystemVersion, code2concept);
		iterateOverZipFile(filenameToFile, LOINC_FILE, handler, ',', QuoteMode.NON_NUMERIC);

		handler = new LoincHierarchyHandler(codeSystemVersion, code2concept);
		iterateOverZipFile(filenameToFile, LOINC_HIERARCHY_FILE, handler, ',', QuoteMode.NON_NUMERIC);

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

		myTermSvc.storeNewCodeSystemVersion(LOINC_URL, codeSystemVersion, theRequestDetails);

		return new UploadStatistics(code2concept.size());
	}

	UploadStatistics processSnomedCtFiles(Map<String, File> filenameToFile, RequestDetails theRequestDetails) {
		final TermCodeSystemVersion codeSystemVersion = new TermCodeSystemVersion();
		final Map<String, TermConcept> id2concept = new HashMap<String, TermConcept>();
		final Map<String, TermConcept> code2concept = new HashMap<String, TermConcept>();
		final Set<String> validConceptIds = new HashSet<String>();

		IRecordHandler handler = new SctHandlerConcept(validConceptIds);
		iterateOverZipFile(filenameToFile, SCT_FILE_CONCEPT, handler, '\t', null);

		ourLog.info("Have {} valid concept IDs", validConceptIds.size());

		handler = new SctHandlerDescription(validConceptIds, code2concept, id2concept, codeSystemVersion);
		iterateOverZipFile(filenameToFile, SCT_FILE_DESCRIPTION, handler, '\t', null);

		ourLog.info("Got {} concepts, cloning map", code2concept.size());
		final HashMap<String, TermConcept> rootConcepts = new HashMap<String, TermConcept>(code2concept);

		handler = new SctHandlerRelationship(codeSystemVersion, rootConcepts, code2concept);
		iterateOverZipFile(filenameToFile, SCT_FILE_RELATIONSHIP, handler, '\t', null);

		ourLog.info("Done loading SNOMED CT files - {} root codes, {} total codes", rootConcepts.size(), code2concept.size());

		for (TermConcept next : rootConcepts.values()) {
			dropCircularRefs(next, new LinkedHashSet<String>(), code2concept);
		}

		codeSystemVersion.getConcepts().addAll(rootConcepts.values());
		myTermSvc.storeNewCodeSystemVersion(SCT_URL, codeSystemVersion, theRequestDetails);

		return new UploadStatistics(code2concept.size());
	}

	@VisibleForTesting
	void setTermSvcForUnitTests(IHapiTerminologySvc theTermSvc) {
		myTermSvc = theTermSvc;
	}

	@CoverageIgnore
	public static void main(String[] args) throws Exception {
		TerminologyLoaderSvc svc = new TerminologyLoaderSvc();

		// byte[] bytes = IOUtils.toByteArray(new FileInputStream("/Users/james/Downloads/SnomedCT_Release_INT_20160131_Full.zip"));
		// svc.loadSnomedCt(bytes);

		Map<String, File> files = new HashMap<String, File>();
		files.put(SCT_FILE_CONCEPT, new File("/Users/james/tmp/sct/SnomedCT_Release_INT_20160131_Full/Terminology/sct2_Concept_Full_INT_20160131.txt"));
		files.put(SCT_FILE_DESCRIPTION, new File("/Users/james/tmp/sct/SnomedCT_Release_INT_20160131_Full/Terminology/sct2_Description_Full-en_INT_20160131.txt"));
		files.put(SCT_FILE_RELATIONSHIP, new File("/Users/james/tmp/sct/SnomedCT_Release_INT_20160131_Full/Terminology/sct2_Relationship_Full_INT_20160131.txt"));
		svc.processSnomedCtFiles(files, null);
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

		public SctHandlerConcept(Set<String> theValidConceptIds) {
			myValidConceptIds = theValidConceptIds;
		}

		@Override
		public void accept(CSVRecord theRecord) {
			String id = theRecord.get("id");
			boolean active = "1".equals(theRecord.get("active"));
			if (!active) {
				return;
			}
			myValidConceptIds.add(id);
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
			if (!active) {
				return;
			}
			TermConcept typeConcept = findConcept(myCode2concept, typeId);
			TermConcept sourceConcept = findConcept(myCode2concept, sourceId);
			TermConcept targetConcept = findConcept(myCode2concept, destinationId);
			if (typeConcept.getDisplay().equals("Is a (attribute)")) {
				if (!sourceId.equals(destinationId)) {
					TermConceptParentChildLink link = new TermConceptParentChildLink();
					link.setChild(sourceConcept);
					link.setParent(targetConcept);
					link.setRelationshipType(TermConceptParentChildLink.RelationshipTypeEnum.ISA);
					link.setCodeSystem(myCodeSystemVersion);
					myRootConcepts.remove(link.getChild().getCode());

					targetConcept.addChild(sourceConcept, RelationshipTypeEnum.ISA);
				}
			} else if (ignoredTypes.contains(typeConcept.getDisplay())) {
				// ignore
			} else {
				// ourLog.warn("Unknown relationship type: {}/{}", typeId, typeConcept.getDisplay());
			}
		}

		private TermConcept findConcept(final Map<String, TermConcept> code2concept, String typeId) {
			TermConcept typeConcept = code2concept.get(typeId);
			if (typeConcept == null) {
				throw new InternalErrorException("Unknown type ID: " + typeId);
			}
			return typeConcept;
		}
	}

	private static class SinkOutputStream extends OutputStream {

		private static final long LOG_INCREMENT = 10 * FileUtils.ONE_MB;
		private int myBytes;
		private String myFilename;
		private long myNextLogCount = LOG_INCREMENT;
		private FileOutputStream myWrap;

		public SinkOutputStream(FileOutputStream theWrap, String theFilename) {
			myWrap = theWrap;
			myFilename = theFilename;
		}

		private void addCount(int theCount) {
			myBytes += theCount;
			if (myBytes > myNextLogCount) {
				ourLog.info(" * Wrote {} of {}", FileUtils.byteCountToDisplaySize(myBytes), myFilename);
				myNextLogCount = myBytes + LOG_INCREMENT;
			}
		}

		@Override
		public void close() throws IOException {
			myWrap.close();
		}

		@Override
		public void flush() throws IOException {
			myWrap.flush();
		}

		@Override
		public void write(byte[] theB) throws IOException {
			myWrap.write(theB);
			addCount(theB.length);
		}

		@Override
		public void write(byte[] theB, int theOff, int theLen) throws IOException {
			myWrap.write(theB, theOff, theLen);
			addCount(theLen);
		}

		@Override
		public void write(int theB) throws IOException {
			myWrap.write(theB);
			addCount(1);
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
