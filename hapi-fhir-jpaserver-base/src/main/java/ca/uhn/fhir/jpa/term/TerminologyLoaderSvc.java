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
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.annotations.VisibleForTesting;

import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

public class TerminologyLoaderSvc {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TerminologyLoaderSvc.class);

	@Autowired
	private IHapiTerminologySvc myTermSvc;

	@VisibleForTesting
	void setTermSvcForUnitTests(IHapiTerminologySvc theTermSvc) {
		myTermSvc = theTermSvc;
	}

	public void loadSnomedCt(byte[] theZipBytes) {
		String filenameDescription = "Terminology/sct2_Description_Full";
		String filenameRelationship = "Terminology/sct2_Relationship_Full";
		List<String> allFilenames = Arrays.asList(filenameDescription, filenameRelationship);

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
					IOUtils.copy(inputStream, new SinkOutputStream());
					continue;
				}
				
				ourLog.debug("Streaming ZIP entry {} into temporary file", nextEntry.getName());

				File nextOutFile = File.createTempFile("hapi_fhir", ".csv");
				nextOutFile.deleteOnExit();
				OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(nextOutFile, false));
				try {
					IOUtils.copy(inputStream, outputStream);
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

		final TermCodeSystemVersion codeSystemVersion = new TermCodeSystemVersion();
		final Map<String, TermConcept> id2concept = new HashMap<String, TermConcept>();
		final Map<String, TermConcept> code2concept = new HashMap<String, TermConcept>();
		final List<TermConceptParentChildLink> links = new ArrayList<TermConceptParentChildLink>();

		IRecordHandler handler = new IRecordHandler() {
			@Override
			public void accept(CSVRecord theRecord) {
				String id = theRecord.get("id");
				boolean active = "1".equals(theRecord.get("active"));
				if (!active) {
					return;
				}
				String conceptId = theRecord.get("conceptId");
				String term = theRecord.get("term");

				TermConcept concept = getOrCreateConcept(codeSystemVersion, id2concept, id);
				concept.setCode(conceptId);
				concept.setDisplay(term);
				code2concept.put(conceptId, concept);
			}
		};
		iterateOverZipFile(filenameToFile, filenameDescription, handler);

		final HashSet<TermConcept> rootConcepts = new HashSet<TermConcept>();
		rootConcepts.addAll(code2concept.values());

		handler = new IRecordHandler() {
			@Override
			public void accept(CSVRecord theRecord) {
				String sourceId = theRecord.get("sourceId");
				String destinationId = theRecord.get("destinationId");
				String typeId = theRecord.get("typeId");
				boolean active = "1".equals(theRecord.get("active"));
				if (!active) {
					return;
				}
				TermConcept typeConcept = findConcept(code2concept, typeId);
				TermConcept sourceConcept = findConcept(code2concept, sourceId);
				TermConcept targetConcept = findConcept(code2concept, destinationId);
				if (typeConcept.getDisplay().equals("Is a")) {
					TermConceptParentChildLink link = new TermConceptParentChildLink();
					link.setChild(sourceConcept);
					link.setParent(targetConcept);
					link.setCodeSystem(codeSystemVersion);
					rootConcepts.remove(link.getChild());
				} else {
					ourLog.warn("Unknown relationship type: {}/{}", typeId, typeConcept.getDisplay());
				}
			}

			private TermConcept findConcept(final Map<String, TermConcept> code2concept, String typeId) {
				TermConcept typeConcept = code2concept.get(typeId);
				if (typeConcept == null) {
					throw new InternalErrorException("Unknown type ID: " + typeId);
				}
				return typeConcept;
			}
		};
		iterateOverZipFile(filenameToFile, filenameRelationship, handler);

		ourLog.info("Done loading SNOMED CT files - {} root codes, {} total codes", rootConcepts.size(), code2concept.size());

		codeSystemVersion.getConcepts().addAll(rootConcepts);
		myTermSvc.storeNewCodeSystemVersion("http://snomed.info/sct", codeSystemVersion);
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

					while (iter.hasNext()) {
						CSVRecord nextRecord = iter.next();
						handler.accept(nextRecord);
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

	private TermConcept getOrCreateConcept(TermCodeSystemVersion codeSystemVersion, Map<String, TermConcept> id2concept, String id) {
		TermConcept concept = id2concept.get(id);
		if (concept == null) {
			concept = new TermConcept();
			id2concept.put(id, concept);
			concept.setCodeSystem(codeSystemVersion);
		}
		return concept;
	}

	private static class ZippedFileInputStream extends InputStream {

		private ZipInputStream is;

		public ZippedFileInputStream(ZipInputStream is) {
			this.is = is;
		}

		@Override
		public int read() throws IOException {
			return is.read();
		}

		@Override
		public void close() throws IOException {
			is.closeEntry();
		}
	}

	private interface IRecordHandler {
		void accept(CSVRecord theRecord);
	}

	public static void main(String[] args) throws Exception {
		byte[] bytes = IOUtils.toByteArray(new FileInputStream("/Users/james/Downloads/SnomedCT_Release_INT_20160131_Full.zip"));
		TerminologyLoaderSvc svc = new TerminologyLoaderSvc();
		svc.loadSnomedCt(bytes);
	}

	private static class SinkOutputStream extends OutputStream {

		@Override
		public void write(int theB) throws IOException {
			// ignore
		}

		@Override
		public void write(byte[] theB) throws IOException {
			// ignore
		}

		@Override
		public void write(byte[] theB, int theOff, int theLen) throws IOException {
			// ignore
		}
		
	}
	
}
