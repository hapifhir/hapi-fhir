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
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermDeferredStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.jpa.term.custom.CustomTerminologySet;
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
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.ValueSet;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class TermLoaderSvcImpl implements ITermLoaderSvc {
	public static final String CUSTOM_CONCEPTS_FILE = "concepts.csv";
	public static final String CUSTOM_HIERARCHY_FILE = "hierarchy.csv";
	public static final String CUSTOM_PROPERTIES_FILE = "properties.csv";
	static final String IMGTHLA_HLA_NOM_TXT = "hla_nom.txt";
	static final String IMGTHLA_HLA_XML = "hla.xml";
	static final String CUSTOM_CODESYSTEM_JSON = "codesystem.json";
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

	@VisibleForTesting
	LoadedFileDescriptors getLoadedFileDescriptors(List<FileDescriptor> theFiles) {
		return new LoadedFileDescriptors(theFiles);
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
}
