package ca.uhn.fhir.jpa.term;

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
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.annotations.VisibleForTesting;

import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink.RelationshipTypeEnum;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

public class TerminologyLoaderSvc implements IHapiTerminologyLoaderSvc {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TerminologyLoaderSvc.class);
	static final String SCT_FILE_CONCEPT = "Terminology/sct2_Concept_Full";
	static final String SCT_FILE_DESCRIPTION = "Terminology/sct2_Description_Full";

	static final String SCT_FILE_RELATIONSHIP = "Terminology/sct2_Relationship_Full";
	

	@Autowired
	private IHapiTerminologySvc myTermSvc;

	private void dropCircularRefs(TermConcept theConcept, HashSet<String> theChain) {

		for (Iterator<TermConceptParentChildLink> childIter = theConcept.getChildren().iterator(); childIter.hasNext();) {
			TermConceptParentChildLink next = childIter.next();
			TermConcept nextChild = next.getChild();
			if (theChain.contains(nextChild.getCode())) {
				ourLog.info("Removing circular reference code {} from parent {}", nextChild.getCode(), theConcept.getCode());
				childIter.remove();
			} else {
				theChain.add(theConcept.getCode());
				dropCircularRefs(nextChild, theChain);
				theChain.remove(theConcept.getCode());
			}
		}
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

	private void iterateOverZipFile(Map<String, File> theFilenameToFile, String fileNamePart, IRecordHandler handler) {
		for (Entry<String, File> nextEntry : theFilenameToFile.entrySet()) {

			if (nextEntry.getKey().contains(fileNamePart)) {
				ourLog.info("Processing file {}", nextEntry.getKey());

				Reader reader = null;
				CSVParser parsed = null;
				try {
					reader = new BufferedReader(new FileReader(nextEntry.getValue()));
					parsed = new CSVParser(reader, CSVFormat.newFormat('\t').withFirstRecordAsHeader());
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
	}

	@Override
	public UploadStatistics loadSnomedCt(byte[] theZipBytes, RequestDetails theRequestDetails) {
		List<String> allFilenames = Arrays.asList(SCT_FILE_DESCRIPTION, SCT_FILE_RELATIONSHIP, SCT_FILE_CONCEPT);

		Map<String, File> filenameToFile = new HashMap<String, File>();
		ZipInputStream zis = new ZipInputStream(new BufferedInputStream(new ByteArrayInputStream(theZipBytes)));
		try {
			for (ZipEntry nextEntry; (nextEntry = zis.getNextEntry()) != null;) {
				ZippedFileInputStream inputStream = new ZippedFileInputStream(zis);

				boolean want = false;
				for (String next : allFilenames) {
					if (nextEntry.getName().contains(next)) {
						want = true;
					}
				}

				if (!want) {
					ourLog.info("Ignoring zip entry: {}", nextEntry.getName());
					continue;
				}

				ourLog.debug("Streaming ZIP entry {} into temporary file", nextEntry.getName());

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

		ourLog.info("Beginning SNOMED CT processing");

		try {
			return processSnomedCtFiles(filenameToFile, theRequestDetails);
		} finally {
			ourLog.info("Finished SNOMED CT file import, cleaning up temporary files");
			for (File nextFile : filenameToFile.values()) {
				nextFile.delete();
			}
		}
	}

	UploadStatistics processSnomedCtFiles(Map<String, File> filenameToFile, RequestDetails theRequestDetails) {
		final TermCodeSystemVersion codeSystemVersion = new TermCodeSystemVersion();
		final Map<String, TermConcept> id2concept = new HashMap<String, TermConcept>();
		final Map<String, TermConcept> code2concept = new HashMap<String, TermConcept>();
		final Set<String> validConceptIds = new HashSet<String>();

		IRecordHandler handler = new SctHandlerConcept(validConceptIds);
		iterateOverZipFile(filenameToFile, SCT_FILE_CONCEPT, handler);

		ourLog.info("Have {} valid concept IDs", validConceptIds.size());

		handler = new SctHandlerDescription(validConceptIds, code2concept, id2concept, codeSystemVersion);
		iterateOverZipFile(filenameToFile, SCT_FILE_DESCRIPTION, handler);

		ourLog.info("Got {} concepts, cloning map", code2concept.size());
		final HashMap<String, TermConcept> rootConcepts = new HashMap<String, TermConcept>(code2concept);

		handler = new SctHandlerRelationship(codeSystemVersion, rootConcepts, code2concept);
		iterateOverZipFile(filenameToFile, SCT_FILE_RELATIONSHIP, handler);

		ourLog.info("Done loading SNOMED CT files - {} root codes, {} total codes", rootConcepts.size(), code2concept.size());

		for (TermConcept next : rootConcepts.values()) {
			dropCircularRefs(next, new HashSet<String>());
		}

		codeSystemVersion.getConcepts().addAll(rootConcepts.values());
		myTermSvc.storeNewCodeSystemVersion(SCT_URL, codeSystemVersion, theRequestDetails);
		
		return new UploadStatistics().setConceptCount(code2concept.size());
	}

	@VisibleForTesting
	void setTermSvcForUnitTests(IHapiTerminologySvc theTermSvc) {
		myTermSvc = theTermSvc;
	}

	public static void main(String[] args) throws Exception {
		TerminologyLoaderSvc svc = new TerminologyLoaderSvc();

		//		byte[] bytes = IOUtils.toByteArray(new FileInputStream("/Users/james/Downloads/SnomedCT_Release_INT_20160131_Full.zip"));
		//		svc.loadSnomedCt(bytes);

		Map<String, File> files = new HashMap<String, File>();
		files.put(SCT_FILE_CONCEPT, new File("/Users/james/tmp/sct/SnomedCT_Release_INT_20160131_Full/Terminology/sct2_Concept_Full_INT_20160131.txt"));
		files.put(SCT_FILE_DESCRIPTION, new File("/Users/james/tmp/sct/SnomedCT_Release_INT_20160131_Full/Terminology/sct2_Description_Full-en_INT_20160131.txt"));
		files.put(SCT_FILE_RELATIONSHIP, new File("/Users/james/tmp/sct/SnomedCT_Release_INT_20160131_Full/Terminology/sct2_Relationship_Full_INT_20160131.txt"));
		svc.processSnomedCtFiles(files, null);
	}

	private interface IRecordHandler {
		void accept(CSVRecord theRecord);
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
				TermConceptParentChildLink link = new TermConceptParentChildLink();
				link.setChild(sourceConcept);
				link.setParent(targetConcept);
				link.setRelationshipType(TermConceptParentChildLink.RelationshipTypeEnum.ISA);
				link.setCodeSystem(myCodeSystemVersion);
				myRootConcepts.remove(link.getChild().getCode());

				targetConcept.addChild(sourceConcept, RelationshipTypeEnum.ISA);
			} else if (ignoredTypes.contains(typeConcept.getDisplay())) {
				// ignore
			} else {
				//				ourLog.warn("Unknown relationship type: {}/{}", typeId, typeConcept.getDisplay());
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
