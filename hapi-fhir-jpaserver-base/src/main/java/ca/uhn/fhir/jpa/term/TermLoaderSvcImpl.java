/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink;
import ca.uhn.fhir.jpa.entity.TermConceptProperty;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermDeferredStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.jpa.term.custom.CustomTerminologySet;
import ca.uhn.fhir.jpa.term.icd10.Icd10Loader;
import ca.uhn.fhir.jpa.term.icd10cm.Icd10CmLoader;
import ca.uhn.fhir.jpa.term.snomedct.SctHandlerConcept;
import ca.uhn.fhir.jpa.term.snomedct.SctHandlerDescription;
import ca.uhn.fhir.jpa.term.snomedct.SctHandlerRelationship;
import ca.uhn.fhir.jpa.util.Counter;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.ValidateUtil;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Charsets;
import jakarta.annotation.Nonnull;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.ValueSet;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class TermLoaderSvcImpl implements ITermLoaderSvc {
	public static final String CUSTOM_CONCEPTS_FILE = "concepts.csv";
	public static final String CUSTOM_HIERARCHY_FILE = "hierarchy.csv";
	public static final String CUSTOM_PROPERTIES_FILE = "properties.csv";
	static final String IMGTHLA_HLA_NOM_TXT = "hla_nom.txt";
	static final String IMGTHLA_HLA_XML = "hla.xml";
	static final String CUSTOM_CODESYSTEM_JSON = "codesystem.json";
	private static final String SCT_FILE_CONCEPT = "Terminology/sct2_Concept_Full_";
	private static final String SCT_FILE_DESCRIPTION = "Terminology/sct2_Description_Full";
	private static final String SCT_FILE_RELATIONSHIP = "Terminology/sct2_Relationship_Full";
	private static final String CUSTOM_CODESYSTEM_XML = "codesystem.xml";

	private static final int LOG_INCREMENT = 1000;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TermLoaderSvcImpl.class);
	// FYI: Hardcoded to R4 because that's what the term svc uses internally
	private final FhirContext myCtx = FhirContext.forR4Cached();
	private final ITermDeferredStorageSvc myDeferredStorageSvc;
	private final ITermCodeSystemStorageSvc myCodeSystemStorageSvc;

	@Autowired
	public TermLoaderSvcImpl(
			ITermDeferredStorageSvc theDeferredStorageSvc, ITermCodeSystemStorageSvc theCodeSystemStorageSvc) {
		this(theDeferredStorageSvc, theCodeSystemStorageSvc, true);
	}

	private TermLoaderSvcImpl(
			ITermDeferredStorageSvc theDeferredStorageSvc,
			ITermCodeSystemStorageSvc theCodeSystemStorageSvc,
			boolean theProxyCheck) {
		if (theProxyCheck) {
			// If these validations start failing, it likely means a cyclic dependency has been introduced into the
			// Spring Application
			// Context that is preventing the Spring auto-proxy bean post-processor from being able to proxy these
			// beans.  Check
			// for recent changes to the Spring @Configuration that may have caused this.
			Validate.isTrue(
					AopUtils.isAopProxy(theDeferredStorageSvc),
					theDeferredStorageSvc.getClass().getName()
							+ " is not a proxy.  @Transactional annotations will be ignored.");
			Validate.isTrue(
					AopUtils.isAopProxy(theCodeSystemStorageSvc),
					theCodeSystemStorageSvc.getClass().getName()
							+ " is not a proxy.  @Transactional annotations will be ignored.");
		}
		myDeferredStorageSvc = theDeferredStorageSvc;
		myCodeSystemStorageSvc = theCodeSystemStorageSvc;
	}

	@VisibleForTesting
	public static TermLoaderSvcImpl withoutProxyCheck(
			ITermDeferredStorageSvc theTermDeferredStorageSvc, ITermCodeSystemStorageSvc theTermCodeSystemStorageSvc) {
		return new TermLoaderSvcImpl(theTermDeferredStorageSvc, theTermCodeSystemStorageSvc, false);
	}

	@Override
	public UploadStatistics loadImgthla(List<FileDescriptor> theFiles, RequestDetails theRequestDetails) {
		try (LoadedFileDescriptors descriptors = getLoadedFileDescriptors(theFiles)) {
			List<String> mandatoryFilenameFragments = Arrays.asList(IMGTHLA_HLA_NOM_TXT, IMGTHLA_HLA_XML);
			descriptors.verifyMandatoryFilesExist(mandatoryFilenameFragments);

			ourLog.info("Beginning IMGTHLA processing");

			return processImgthlaFiles(descriptors, theRequestDetails);
		}
	}

	@VisibleForTesting
	LoadedFileDescriptors getLoadedFileDescriptors(List<FileDescriptor> theFiles) {
		return new LoadedFileDescriptors(theFiles);
	}

	@Override
	public UploadStatistics loadSnomedCt(List<FileDescriptor> theFiles, RequestDetails theRequestDetails) {
		try (LoadedFileDescriptors descriptors = getLoadedFileDescriptors(theFiles)) {

			List<String> expectedFilenameFragments =
					Arrays.asList(SCT_FILE_DESCRIPTION, SCT_FILE_RELATIONSHIP, SCT_FILE_CONCEPT);
			descriptors.verifyMandatoryFilesExist(expectedFilenameFragments);

			ourLog.info("Beginning SNOMED CT processing");

			return processSnomedCtFiles(descriptors, theRequestDetails);
		}
	}

	@Override
	public UploadStatistics loadIcd10(List<FileDescriptor> theFiles, RequestDetails theRequestDetails) {
		ourLog.info("Beginning ICD-10 processing");

		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl(ICD10_URI);
		codeSystem.setContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);
		codeSystem.setStatus(Enumerations.PublicationStatus.ACTIVE);

		TermCodeSystemVersion codeSystemVersion = new TermCodeSystemVersion();
		int count = 0;

		try (LoadedFileDescriptors compressedDescriptors = getLoadedFileDescriptors(theFiles)) {
			for (FileDescriptor nextDescriptor : compressedDescriptors.getUncompressedFileDescriptors()) {
				if (nextDescriptor.getFilename().toLowerCase(Locale.US).endsWith(".xml")) {
					try (InputStream inputStream = nextDescriptor.getInputStream();
							InputStreamReader reader = new InputStreamReader(inputStream, Charsets.UTF_8)) {
						Icd10Loader loader = new Icd10Loader(codeSystem, codeSystemVersion);
						loader.load(reader);
						count += loader.getConceptCount();
					}
				}
			}
		} catch (IOException | SAXException e) {
			throw new InternalErrorException(Msg.code(2135) + e);
		}

		codeSystem.setVersion(codeSystemVersion.getCodeSystemVersionId());

		IIdType target = storeCodeSystem(theRequestDetails, codeSystemVersion, codeSystem, null, null);
		return new UploadStatistics(count, target);
	}

	@Override
	public UploadStatistics loadIcd10cm(List<FileDescriptor> theFiles, RequestDetails theRequestDetails) {
		ourLog.info("Beginning ICD-10-cm processing");

		CodeSystem cs = new CodeSystem();
		cs.setUrl(ICD10CM_URI);
		cs.setName("ICD-10-CM");
		cs.setContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);
		cs.setStatus(Enumerations.PublicationStatus.ACTIVE);

		TermCodeSystemVersion codeSystemVersion = new TermCodeSystemVersion();
		int count = 0;

		try (LoadedFileDescriptors compressedDescriptors = getLoadedFileDescriptors(theFiles)) {
			for (FileDescriptor nextDescriptor : compressedDescriptors.getUncompressedFileDescriptors()) {
				if (nextDescriptor.getFilename().toLowerCase(Locale.US).endsWith(".xml")) {
					try (InputStream inputStream = nextDescriptor.getInputStream();
							InputStreamReader reader = new InputStreamReader(inputStream, Charsets.UTF_8)) {
						Icd10CmLoader loader = new Icd10CmLoader(codeSystemVersion);
						loader.load(reader);
						count += loader.getConceptCount();
					}
				}
			}
		} catch (IOException | SAXException e) {
			throw new InternalErrorException(Msg.code(865) + e);
		}

		cs.setVersion(codeSystemVersion.getCodeSystemVersionId());

		IIdType target = storeCodeSystem(theRequestDetails, codeSystemVersion, cs, null, null);
		return new UploadStatistics(count, target);
	}

	@Override
	public UploadStatistics loadCustom(
			String theSystem, List<FileDescriptor> theFiles, RequestDetails theRequestDetails) {
		try (LoadedFileDescriptors descriptors = getLoadedFileDescriptors(theFiles)) {
			Optional<String> codeSystemContent = loadFile(descriptors, CUSTOM_CODESYSTEM_JSON, CUSTOM_CODESYSTEM_XML);
			CodeSystem codeSystem;
			if (codeSystemContent.isPresent()) {
				codeSystem = EncodingEnum.detectEncoding(codeSystemContent.get())
						.newParser(myCtx)
						.parseResource(CodeSystem.class, codeSystemContent.get());
				ValidateUtil.isTrueOrThrowInvalidRequest(
						theSystem.equalsIgnoreCase(codeSystem.getUrl()),
						"CodeSystem.url does not match the supplied system: %s",
						theSystem);
				ValidateUtil.isTrueOrThrowInvalidRequest(
						CodeSystem.CodeSystemContentMode.NOTPRESENT.equals(codeSystem.getContent()),
						"CodeSystem.content does not match the expected value: %s",
						CodeSystem.CodeSystemContentMode.NOTPRESENT.toCode());
			} else {
				codeSystem = new CodeSystem();
				codeSystem.setUrl(theSystem);
				codeSystem.setContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);
			}

			CustomTerminologySet terminologySet = CustomTerminologySet.load(descriptors, false);
			TermCodeSystemVersion csv = terminologySet.toCodeSystemVersion();

			IIdType target = storeCodeSystem(theRequestDetails, csv, codeSystem, null, null);
			return new UploadStatistics(terminologySet.getSize(), target);
		}
	}

	@Override
	public UploadStatistics loadDeltaAdd(
			String theSystem, List<FileDescriptor> theFiles, RequestDetails theRequestDetails) {
		ourLog.info(
				"Processing terminology delta ADD for system[{}] with files: {}",
				theSystem,
				theFiles.stream().map(FileDescriptor::getFilename).collect(Collectors.toList()));
		try (LoadedFileDescriptors descriptors = getLoadedFileDescriptors(theFiles)) {
			CustomTerminologySet terminologySet = CustomTerminologySet.load(descriptors, false);
			return myCodeSystemStorageSvc.applyDeltaCodeSystemsAdd(theSystem, terminologySet);
		}
	}

	@Override
	public UploadStatistics loadDeltaRemove(
			String theSystem, List<FileDescriptor> theFiles, RequestDetails theRequestDetails) {
		ourLog.info(
				"Processing terminology delta REMOVE for system[{}] with files: {}",
				theSystem,
				theFiles.stream().map(FileDescriptor::getFilename).collect(Collectors.toList()));
		try (LoadedFileDescriptors descriptors = getLoadedFileDescriptors(theFiles)) {
			CustomTerminologySet terminologySet = CustomTerminologySet.load(descriptors, true);
			return myCodeSystemStorageSvc.applyDeltaCodeSystemsRemove(theSystem, terminologySet);
		}
	}

	private void dropCircularRefs(
			TermConcept theConcept, ArrayList<String> theChain, Map<String, TermConcept> theCode2concept) {

		theChain.add(theConcept.getCode());
		for (Iterator<TermConceptParentChildLink> childIter =
						theConcept.getChildren().iterator();
				childIter.hasNext(); ) {
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
					b.append(StringUtils.substring(nextCode.getDisplay(), 0, 20)
							.replace("[", "")
							.replace("]", "")
							.trim());
					b.append("] ");
				}
				ourLog.info(b.toString(), theConcept.getCode());
				childIter.remove();
				nextChild.getParents().remove(next);

			} else {
				dropCircularRefs(nextChild, theChain, theCode2concept);
			}
		}
		theChain.remove(theChain.size() - 1);
	}


	private Optional<String> loadFile(LoadedFileDescriptors theDescriptors, String... theFilenames) {
		for (FileDescriptor next : theDescriptors.getUncompressedFileDescriptors()) {
			for (String nextFilename : theFilenames) {
				if (next.getFilename().endsWith(nextFilename)) {
					try {
						String contents = IOUtils.toString(next.getInputStream(), Charsets.UTF_8);
						return Optional.of(contents);
					} catch (IOException e) {
						throw new InternalErrorException(Msg.code(868) + e);
					}
				}
			}
		}
		return Optional.empty();
	}

	private UploadStatistics processImgthlaFiles(
			LoadedFileDescriptors theDescriptors, RequestDetails theRequestDetails) {
		final TermCodeSystemVersion codeSystemVersion = new TermCodeSystemVersion();
		final List<ValueSet> valueSets = new ArrayList<>();
		final List<ConceptMap> conceptMaps = new ArrayList<>();

		CodeSystem imgthlaCs;
		try {
			String imgthlaCsString = IOUtils.toString(
					TermReadSvcImpl.class.getResourceAsStream("/ca/uhn/fhir/jpa/term/imgthla/imgthla.xml"),
					Charsets.UTF_8);
			imgthlaCs = FhirContext.forR4Cached().newXmlParser().parseResource(CodeSystem.class, imgthlaCsString);
		} catch (IOException e) {
			throw new InternalErrorException(Msg.code(869) + "Failed to load imgthla.xml", e);
		}

		boolean foundHlaNom = false;
		boolean foundHlaXml = false;
		for (FileDescriptor nextZipBytes : theDescriptors.getUncompressedFileDescriptors()) {
			String nextFilename = nextZipBytes.getFilename();

			if (!IMGTHLA_HLA_NOM_TXT.equals(nextFilename)
					&& !nextFilename.endsWith("/" + IMGTHLA_HLA_NOM_TXT)
					&& !IMGTHLA_HLA_XML.equals(nextFilename)
					&& !nextFilename.endsWith("/" + IMGTHLA_HLA_XML)) {
				ourLog.info("Skipping unexpected file {}", nextFilename);
				continue;
			}

			if (IMGTHLA_HLA_NOM_TXT.equals(nextFilename) || nextFilename.endsWith("/" + IMGTHLA_HLA_NOM_TXT)) {
				// process colon-delimited hla_nom.txt file
				ourLog.info("Processing file {}", nextFilename);

				//				IRecordHandler handler = new HlaNomTxtHandler(codeSystemVersion, code2concept,
				// propertyNamesToTypes);
				//				AntigenSource antigenSource = new WmdaAntigenSource(hlaNomFilename, relSerSerFilename,
				// relDnaSerFilename);

				Reader reader = null;
				try {
					reader = new InputStreamReader(nextZipBytes.getInputStream(), Charsets.UTF_8);

					LineNumberReader lnr = new LineNumberReader(reader);
					while (lnr.readLine() != null) {}
					ourLog.warn("Lines read from {}:  {}", nextFilename, lnr.getLineNumber());

				} catch (IOException e) {
					throw new InternalErrorException(Msg.code(870) + e);
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

					LineNumberReader lnr = new LineNumberReader(reader);
					while (lnr.readLine() != null) {}
					ourLog.warn("Lines read from {}:  {}", nextFilename, lnr.getLineNumber());

				} catch (IOException e) {
					throw new InternalErrorException(Msg.code(871) + e);
				} finally {
					IOUtils.closeQuietly(reader);
				}

				foundHlaXml = true;
			}
		}

		if (!foundHlaNom) {
			throw new InvalidRequestException(Msg.code(872) + "Did not find file matching " + IMGTHLA_HLA_NOM_TXT);
		}

		if (!foundHlaXml) {
			throw new InvalidRequestException(Msg.code(873) + "Did not find file matching " + IMGTHLA_HLA_XML);
		}

		int valueSetCount = valueSets.size();
		int rootConceptCount = codeSystemVersion.getConcepts().size();
		ourLog.info(
				"Have {} total concepts, {} root concepts, {} ValueSets",
				rootConceptCount,
				rootConceptCount,
				valueSetCount);

		// remove this when fully implemented ...
		throw new InternalErrorException(
				Msg.code(874) + "HLA nomenclature terminology upload not yet fully implemented.");

		//		IIdType target = storeCodeSystem(theRequestDetails, codeSystemVersion, imgthlaCs, valueSets, conceptMaps);
		//
		//		return new UploadStatistics(conceptCount, target);
	}


	private UploadStatistics processSnomedCtFiles(
			LoadedFileDescriptors theDescriptors, RequestDetails theRequestDetails) {
		final TermCodeSystemVersion codeSystemVersion = new TermCodeSystemVersion();
		final Map<String, TermConcept> id2concept = new HashMap<>();
		final Map<String, TermConcept> code2concept = new HashMap<>();
		final Set<String> validConceptIds = new HashSet<>();

		IZipContentsHandlerCsv handler = new SctHandlerConcept(validConceptIds);
		iterateOverZipFileCsv(theDescriptors, SCT_FILE_CONCEPT, handler, '\t', null, true);

		ourLog.info("Have {} valid concept IDs", validConceptIds.size());

		handler = new SctHandlerDescription(validConceptIds, code2concept, id2concept, codeSystemVersion);
		iterateOverZipFileCsv(theDescriptors, SCT_FILE_DESCRIPTION, handler, '\t', null, true);

		ourLog.info("Got {} concepts, cloning map", code2concept.size());
		final HashMap<String, TermConcept> rootConcepts = new HashMap<>(code2concept);

		handler = new SctHandlerRelationship(codeSystemVersion, code2concept);
		iterateOverZipFileCsv(theDescriptors, SCT_FILE_RELATIONSHIP, handler, '\t', null, true);

		IOUtils.closeQuietly(theDescriptors);

		ourLog.info("Looking for root codes");
		rootConcepts
				.entrySet()
				.removeIf(theStringTermConceptEntry ->
						!theStringTermConceptEntry.getValue().getParents().isEmpty());

		ourLog.info(
				"Done loading SNOMED CT files - {} root codes, {} total codes",
				rootConcepts.size(),
				code2concept.size());

		Counter circularCounter = new Counter();
		for (TermConcept next : rootConcepts.values()) {
			long count = circularCounter.getThenAdd();
			float pct = ((float) count / rootConcepts.size()) * 100.0f;
			ourLog.info(
					" * Scanning for circular refs - have scanned {} / {} codes ({}%)",
					count, rootConcepts.size(), pct);
			dropCircularRefs(next, new ArrayList<>(), code2concept);
		}

		codeSystemVersion.getConcepts().addAll(rootConcepts.values());

		CodeSystem cs = new org.hl7.fhir.r4.model.CodeSystem();
		cs.setUrl(SCT_URI);
		cs.setName("SNOMED CT");
		cs.setContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);
		cs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		IIdType target = storeCodeSystem(theRequestDetails, codeSystemVersion, cs, null, null);

		return new UploadStatistics(code2concept.size(), target);
	}

	private IIdType storeCodeSystem(
			RequestDetails theRequestDetails,
			final TermCodeSystemVersion theCodeSystemVersion,
			CodeSystem theCodeSystem,
			List<ValueSet> theValueSets,
			List<ConceptMap> theConceptMaps) {
		Validate.isTrue(theCodeSystem.getContent() == CodeSystem.CodeSystemContentMode.NOTPRESENT);

		List<ValueSet> valueSets = ObjectUtils.defaultIfNull(theValueSets, Collections.emptyList());
		List<ConceptMap> conceptMaps = ObjectUtils.defaultIfNull(theConceptMaps, Collections.emptyList());

		IIdType retVal;
		myDeferredStorageSvc.setProcessDeferred(false);
		retVal = myCodeSystemStorageSvc.storeNewCodeSystemVersion(
				theCodeSystem, theCodeSystemVersion, theRequestDetails, valueSets, conceptMaps);
		myDeferredStorageSvc.setProcessDeferred(true);

		return retVal;
	}

	public static void iterateOverZipFileCsv(
			LoadedFileDescriptors theDescriptors,
			String theFileNamePart,
			IZipContentsHandlerCsv theHandler,
			char theDelimiter,
			QuoteMode theQuoteMode,
			boolean theIsPartialFilename) {
		iterateOverZipFileCsv(
				theDescriptors, theFileNamePart, theHandler, theDelimiter, theQuoteMode, theIsPartialFilename, true);
	}

	public static void iterateOverZipFileCsvOptional(
			LoadedFileDescriptors theDescriptors,
			String theFileNamePart,
			IZipContentsHandlerCsv theHandler,
			char theDelimiter,
			QuoteMode theQuoteMode,
			boolean theIsPartialFilename) {
		iterateOverZipFileCsv(
				theDescriptors, theFileNamePart, theHandler, theDelimiter, theQuoteMode, theIsPartialFilename, false);
	}

	private static void iterateOverZipFileCsv(
			LoadedFileDescriptors theDescriptors,
			String theFileNamePart,
			IZipContentsHandlerCsv theHandler,
			char theDelimiter,
			QuoteMode theQuoteMode,
			boolean theIsPartialFilename,
			boolean theRequireMatch) {
		IZipContentsHandler handler = (reader, filename) -> {
			CSVParser parsed = newCsvRecords(theDelimiter, theQuoteMode, reader);
			Iterator<CSVRecord> iter = parsed.iterator();
			ourLog.debug("Header map: {}", parsed.getHeaderMap());

			int count = 0;
			int nextLoggedCount = 0;
			while (iter.hasNext()) {
				CSVRecord nextRecord = iter.next();
				if (!nextRecord.isConsistent()) {
					continue;
				}
				theHandler.accept(nextRecord);
				count++;
				if (count >= nextLoggedCount) {
					ourLog.info(" * Processed {} records in {}", count, filename);
					nextLoggedCount += LOG_INCREMENT;
				}
			}
		};

		iterateOverZipFile(theDescriptors, theFileNamePart, theIsPartialFilename, theRequireMatch, handler);
	}

	private static void iterateOverZipFile(
			LoadedFileDescriptors theDescriptors,
			String theFileNamePart,
			boolean theIsPartialFilename,
			boolean theRequireMatch,
			IZipContentsHandler theHandler) {
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

				try {

					Reader reader = new InputStreamReader(nextZipBytes.getInputStream(), Charsets.UTF_8);
					theHandler.handle(reader, nextFilename);

				} catch (IOException e) {
					throw new InternalErrorException(Msg.code(877) + e);
				}
			}
		}

		if (!foundMatch && theRequireMatch) {
			throw new InvalidRequestException(Msg.code(878) + "Did not find file matching " + theFileNamePart);
		}
	}

	@Nonnull
	private static CSVParser newCsvRecords(char theDelimiter, QuoteMode theQuoteMode, Reader theReader)
			throws IOException {
		CSVParser parsed;
		CSVFormat format =
				CSVFormat.newFormat(theDelimiter).withFirstRecordAsHeader().withTrim();
		if (theQuoteMode != null) {
			format = format.withQuote('"').withQuoteMode(theQuoteMode);
		}
		parsed = new CSVParser(theReader, format);
		return parsed;
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

	public static TermConcept getOrCreateConcept(Map<String, TermConcept> id2concept, String id) {
		TermConcept concept = id2concept.get(id);
		if (concept == null) {
			concept = new TermConcept();
			id2concept.put(id, concept);
		}
		return concept;
	}

	public static TermConceptProperty getOrCreateConceptProperty(
			Map<String, List<TermConceptProperty>> code2Properties, String code, String key) {
		List<TermConceptProperty> termConceptProperties = code2Properties.get(code);
		if (termConceptProperties == null) return new TermConceptProperty();
		Optional<TermConceptProperty> termConceptProperty = termConceptProperties.stream()
				.filter(property -> key.equals(property.getKey()))
				.findFirst();
		return termConceptProperty.orElseGet(TermConceptProperty::new);
	}
}
