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
import ca.uhn.fhir.jpa.term.icd10cm.Icd10CmLoader;
import ca.uhn.fhir.jpa.term.loinc.LoincAnswerListHandler;
import ca.uhn.fhir.jpa.term.loinc.LoincAnswerListLinkHandler;
import ca.uhn.fhir.jpa.term.loinc.LoincConsumerNameHandler;
import ca.uhn.fhir.jpa.term.loinc.LoincDocumentOntologyHandler;
import ca.uhn.fhir.jpa.term.loinc.LoincGroupFileHandler;
import ca.uhn.fhir.jpa.term.loinc.LoincGroupTermsFileHandler;
import ca.uhn.fhir.jpa.term.loinc.LoincHandler;
import ca.uhn.fhir.jpa.term.loinc.LoincHierarchyHandler;
import ca.uhn.fhir.jpa.term.loinc.LoincIeeeMedicalDeviceCodeHandler;
import ca.uhn.fhir.jpa.term.loinc.LoincImagingDocumentCodeHandler;
import ca.uhn.fhir.jpa.term.loinc.LoincLinguisticVariantHandler;
import ca.uhn.fhir.jpa.term.loinc.LoincLinguisticVariantsHandler;
import ca.uhn.fhir.jpa.term.loinc.LoincParentGroupFileHandler;
import ca.uhn.fhir.jpa.term.loinc.LoincPartHandler;
import ca.uhn.fhir.jpa.term.loinc.LoincPartLinkHandler;
import ca.uhn.fhir.jpa.term.loinc.LoincPartRelatedCodeMappingHandler;
import ca.uhn.fhir.jpa.term.loinc.LoincRsnaPlaybookHandler;
import ca.uhn.fhir.jpa.term.loinc.LoincTop2000LabResultsSiHandler;
import ca.uhn.fhir.jpa.term.loinc.LoincTop2000LabResultsUsHandler;
import ca.uhn.fhir.jpa.term.loinc.LoincUniversalOrderSetHandler;
import ca.uhn.fhir.jpa.term.loinc.LoincXmlFileZipContentsHandler;
import ca.uhn.fhir.jpa.term.loinc.PartTypeAndPartName;
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

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc.MAKE_LOADING_VERSION_CURRENT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_ANSWERLIST_FILE;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_ANSWERLIST_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_ANSWERLIST_LINK_FILE;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_ANSWERLIST_LINK_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_CODESYSTEM_MAKE_CURRENT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_CODESYSTEM_VERSION;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_CONSUMER_NAME_FILE;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_CONSUMER_NAME_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_DOCUMENT_ONTOLOGY_FILE;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_DOCUMENT_ONTOLOGY_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_FILE;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_GROUP_FILE;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_GROUP_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_GROUP_TERMS_FILE;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_GROUP_TERMS_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_HIERARCHY_FILE;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_HIERARCHY_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_IEEE_MEDICAL_DEVICE_CODE_MAPPING_TABLE_FILE;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_IEEE_MEDICAL_DEVICE_CODE_MAPPING_TABLE_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_IMAGING_DOCUMENT_CODES_FILE;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_IMAGING_DOCUMENT_CODES_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_LINGUISTIC_VARIANTS_FILE;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_LINGUISTIC_VARIANTS_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_LINGUISTIC_VARIANTS_PATH;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_LINGUISTIC_VARIANTS_PATH_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_PARENT_GROUP_FILE;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_PARENT_GROUP_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_PART_FILE;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_PART_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_PART_LINK_FILE;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_PART_LINK_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_PART_LINK_FILE_PRIMARY;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_PART_LINK_FILE_PRIMARY_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_PART_LINK_FILE_SUPPLEMENTARY;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_PART_LINK_FILE_SUPPLEMENTARY_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_PART_RELATED_CODE_MAPPING_FILE;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_PART_RELATED_CODE_MAPPING_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_RSNA_PLAYBOOK_FILE;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_RSNA_PLAYBOOK_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_TOP2000_COMMON_LAB_RESULTS_SI_FILE;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_TOP2000_COMMON_LAB_RESULTS_SI_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_TOP2000_COMMON_LAB_RESULTS_US_FILE;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_TOP2000_COMMON_LAB_RESULTS_US_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_UNIVERSAL_LAB_ORDER_VALUESET_FILE;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_UNIVERSAL_LAB_ORDER_VALUESET_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_UPLOAD_PROPERTIES_FILE;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.hl7.fhir.common.hapi.validation.support.ValidationConstants.LOINC_ALL_VALUESET_ID;

/*
 * #%L
 * HAPI FHIR JPA Server
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

public class TermLoaderSvcImpl implements ITermLoaderSvc {
	public static final String CUSTOM_CONCEPTS_FILE = "concepts.csv";
	public static final String CUSTOM_HIERARCHY_FILE = "hierarchy.csv";
	public static final String CUSTOM_PROPERTIES_FILE = "properties.csv";
	static final String IMGTHLA_HLA_NOM_TXT = "hla_nom.txt";
	static final String IMGTHLA_HLA_XML = "hla.xml";
	static final String CUSTOM_CODESYSTEM_JSON = "codesystem.json";
	private static final String SCT_FILE_CONCEPT = "Terminology/sct2_Concept_Full_";
	private static final String SCT_FILE_DESCRIPTION = "Terminology/sct2_Description_Full-en";
	private static final String SCT_FILE_RELATIONSHIP = "Terminology/sct2_Relationship_Full";
	private static final String CUSTOM_CODESYSTEM_XML = "codesystem.xml";

	private static final int LOG_INCREMENT = 1000;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TermLoaderSvcImpl.class);
	// FYI: Hardcoded to R4 because that's what the term svc uses internally
	private final FhirContext myCtx = FhirContext.forR4();
	private final ITermDeferredStorageSvc myDeferredStorageSvc;
	private final ITermCodeSystemStorageSvc myCodeSystemStorageSvc;

	@Autowired
	public TermLoaderSvcImpl(ITermDeferredStorageSvc theDeferredStorageSvc, ITermCodeSystemStorageSvc theCodeSystemStorageSvc) {
		this(theDeferredStorageSvc, theCodeSystemStorageSvc, true);
	}

	private TermLoaderSvcImpl(ITermDeferredStorageSvc theDeferredStorageSvc, ITermCodeSystemStorageSvc theCodeSystemStorageSvc, boolean theProxyCheck) {
		if (theProxyCheck) {
			// If these validations start failing, it likely means a cyclic dependency has been introduced into the Spring Application
			// Context that is preventing the Spring auto-proxy bean post-processor from being able to proxy these beans.  Check
			// for recent changes to the Spring @Configuration that may have caused this.
			Validate.isTrue(AopUtils.isAopProxy(theDeferredStorageSvc), theDeferredStorageSvc.getClass().getName() + " is not a proxy.  @Transactional annotations will be ignored.");
			Validate.isTrue(AopUtils.isAopProxy(theCodeSystemStorageSvc), theCodeSystemStorageSvc.getClass().getName() + " is not a proxy.  @Transactional annotations will be ignored.");
		}
		myDeferredStorageSvc = theDeferredStorageSvc;
		myCodeSystemStorageSvc = theCodeSystemStorageSvc;

	}

	@VisibleForTesting
	public static TermLoaderSvcImpl withoutProxyCheck(ITermDeferredStorageSvc theTermDeferredStorageSvc, ITermCodeSystemStorageSvc theTermCodeSystemStorageSvc) {
		return new TermLoaderSvcImpl(theTermDeferredStorageSvc, theTermCodeSystemStorageSvc, false);
	}

	@Override
	public UploadStatistics loadImgthla(List<FileDescriptor> theFiles, RequestDetails theRequestDetails) {
		try (LoadedFileDescriptors descriptors = getLoadedFileDescriptors(theFiles)) {
			List<String> mandatoryFilenameFragments = Arrays.asList(
				IMGTHLA_HLA_NOM_TXT,
				IMGTHLA_HLA_XML
			);
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
	public UploadStatistics loadLoinc(List<FileDescriptor> theFiles, RequestDetails theRequestDetails) {
		try (LoadedFileDescriptors descriptors = getLoadedFileDescriptors(theFiles)) {
			Properties uploadProperties = getProperties(descriptors, LOINC_UPLOAD_PROPERTIES_FILE.getCode());

			String codeSystemVersionId = uploadProperties.getProperty(LOINC_CODESYSTEM_VERSION.getCode());
			boolean isMakeCurrentVersion = Boolean.parseBoolean(
				uploadProperties.getProperty(LOINC_CODESYSTEM_MAKE_CURRENT.getCode(), "true"));

			if (StringUtils.isBlank(codeSystemVersionId) && ! isMakeCurrentVersion) {
				throw new InvalidRequestException(Msg.code(864) + "'" + LOINC_CODESYSTEM_VERSION.getCode() +
					"' property is required when '" + LOINC_CODESYSTEM_MAKE_CURRENT.getCode() + "' property is 'false'");
			}

			List<String> mandatoryFilenameFragments = Arrays.asList(
				uploadProperties.getProperty(LOINC_ANSWERLIST_FILE.getCode(), LOINC_ANSWERLIST_FILE_DEFAULT.getCode()),
				uploadProperties.getProperty(LOINC_ANSWERLIST_LINK_FILE.getCode(), LOINC_ANSWERLIST_LINK_FILE_DEFAULT.getCode()),
				uploadProperties.getProperty(LOINC_DOCUMENT_ONTOLOGY_FILE.getCode(), LOINC_DOCUMENT_ONTOLOGY_FILE_DEFAULT.getCode()),
				uploadProperties.getProperty(LOINC_FILE.getCode(), LOINC_FILE_DEFAULT.getCode()),
				uploadProperties.getProperty(LOINC_HIERARCHY_FILE.getCode(), LOINC_HIERARCHY_FILE_DEFAULT.getCode()),
				uploadProperties.getProperty(LOINC_IEEE_MEDICAL_DEVICE_CODE_MAPPING_TABLE_FILE.getCode(), LOINC_IEEE_MEDICAL_DEVICE_CODE_MAPPING_TABLE_FILE_DEFAULT.getCode()),
				uploadProperties.getProperty(LOINC_IMAGING_DOCUMENT_CODES_FILE.getCode(), LOINC_IMAGING_DOCUMENT_CODES_FILE_DEFAULT.getCode()),
				uploadProperties.getProperty(LOINC_PART_FILE.getCode(), LOINC_PART_FILE_DEFAULT.getCode()),
				uploadProperties.getProperty(LOINC_PART_RELATED_CODE_MAPPING_FILE.getCode(), LOINC_PART_RELATED_CODE_MAPPING_FILE_DEFAULT.getCode()),
				uploadProperties.getProperty(LOINC_RSNA_PLAYBOOK_FILE.getCode(), LOINC_RSNA_PLAYBOOK_FILE_DEFAULT.getCode()),
				uploadProperties.getProperty(LOINC_UNIVERSAL_LAB_ORDER_VALUESET_FILE.getCode(), LOINC_UNIVERSAL_LAB_ORDER_VALUESET_FILE_DEFAULT.getCode())
			);
			descriptors.verifyMandatoryFilesExist(mandatoryFilenameFragments);

			List<String> splitPartLinkFilenameFragments = Arrays.asList(
				uploadProperties.getProperty(LOINC_PART_LINK_FILE_PRIMARY.getCode(), LOINC_PART_LINK_FILE_PRIMARY_DEFAULT.getCode()),
				uploadProperties.getProperty(LOINC_PART_LINK_FILE_SUPPLEMENTARY.getCode(), LOINC_PART_LINK_FILE_SUPPLEMENTARY_DEFAULT.getCode())
			);
			descriptors.verifyPartLinkFilesExist(splitPartLinkFilenameFragments, uploadProperties.getProperty(LOINC_PART_LINK_FILE.getCode(), LOINC_PART_LINK_FILE_DEFAULT.getCode()));

			List<String> optionalFilenameFragments = Arrays.asList(
				uploadProperties.getProperty(LOINC_GROUP_FILE.getCode(), LOINC_GROUP_FILE_DEFAULT.getCode()),
				uploadProperties.getProperty(LOINC_GROUP_TERMS_FILE.getCode(), LOINC_GROUP_TERMS_FILE_DEFAULT.getCode()),
				uploadProperties.getProperty(LOINC_PARENT_GROUP_FILE.getCode(), LOINC_PARENT_GROUP_FILE_DEFAULT.getCode()),
				uploadProperties.getProperty(LOINC_TOP2000_COMMON_LAB_RESULTS_SI_FILE.getCode(), LOINC_TOP2000_COMMON_LAB_RESULTS_SI_FILE_DEFAULT.getCode()),
				uploadProperties.getProperty(LOINC_TOP2000_COMMON_LAB_RESULTS_US_FILE.getCode(), LOINC_TOP2000_COMMON_LAB_RESULTS_US_FILE_DEFAULT.getCode()),

				//-- optional consumer name
				uploadProperties.getProperty(LOINC_CONSUMER_NAME_FILE.getCode(), LOINC_CONSUMER_NAME_FILE_DEFAULT.getCode()),
				uploadProperties.getProperty(LOINC_LINGUISTIC_VARIANTS_FILE.getCode(), LOINC_LINGUISTIC_VARIANTS_FILE_DEFAULT.getCode())

			);
			descriptors.verifyOptionalFilesExist(optionalFilenameFragments);

			ourLog.info("Beginning LOINC processing");

			if (isMakeCurrentVersion) {
				if (codeSystemVersionId != null) {
					processLoincFiles(descriptors, theRequestDetails, uploadProperties, false);
					uploadProperties.remove(LOINC_CODESYSTEM_VERSION.getCode());
				}
				ourLog.info("Uploading CodeSystem and making it current version");

			} else {
				ourLog.info("Uploading CodeSystem without updating current version");
			}

			theRequestDetails.getUserData().put(MAKE_LOADING_VERSION_CURRENT, isMakeCurrentVersion);
			return processLoincFiles(descriptors, theRequestDetails, uploadProperties, true);
		}
	}

	@Override
	public UploadStatistics loadSnomedCt(List<FileDescriptor> theFiles, RequestDetails theRequestDetails) {
		try (LoadedFileDescriptors descriptors = getLoadedFileDescriptors(theFiles)) {

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
						  InputStreamReader reader = new InputStreamReader(inputStream, Charsets.UTF_8) ) {
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
	public UploadStatistics loadCustom(String theSystem, List<FileDescriptor> theFiles, RequestDetails theRequestDetails) {
		try (LoadedFileDescriptors descriptors = getLoadedFileDescriptors(theFiles)) {
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

			CustomTerminologySet terminologySet = CustomTerminologySet.load(descriptors, false);
			TermCodeSystemVersion csv = terminologySet.toCodeSystemVersion();

			IIdType target = storeCodeSystem(theRequestDetails, csv, codeSystem, null, null);
			return new UploadStatistics(terminologySet.getSize(), target);
		}
	}


	@Override
	public UploadStatistics loadDeltaAdd(String theSystem, List<FileDescriptor> theFiles, RequestDetails theRequestDetails) {
		ourLog.info("Processing terminology delta ADD for system[{}] with files: {}", theSystem, theFiles.stream().map(t -> t.getFilename()).collect(Collectors.toList()));
		try (LoadedFileDescriptors descriptors = getLoadedFileDescriptors(theFiles)) {
			CustomTerminologySet terminologySet = CustomTerminologySet.load(descriptors, false);
			return myCodeSystemStorageSvc.applyDeltaCodeSystemsAdd(theSystem, terminologySet);
		}
	}

	@Override
	public UploadStatistics loadDeltaRemove(String theSystem, List<FileDescriptor> theFiles, RequestDetails theRequestDetails) {
		ourLog.info("Processing terminology delta REMOVE for system[{}] with files: {}", theSystem, theFiles.stream().map(t -> t.getFilename()).collect(Collectors.toList()));
		try (LoadedFileDescriptors descriptors = getLoadedFileDescriptors(theFiles)) {
			CustomTerminologySet terminologySet = CustomTerminologySet.load(descriptors, true);
			return myCodeSystemStorageSvc.applyDeltaCodeSystemsRemove(theSystem, terminologySet);
		}
	}

	private void dropCircularRefs(TermConcept theConcept, ArrayList<String> theChain, Map<String, TermConcept> theCode2concept) {

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
				dropCircularRefs(nextChild, theChain, theCode2concept);
			}
		}
		theChain.remove(theChain.size() - 1);

	}

	@VisibleForTesting
	@Nonnull
	Properties getProperties(LoadedFileDescriptors theDescriptors, String thePropertiesFile) {
		Properties retVal = new Properties();

		try (InputStream propertyStream = ca.uhn.fhir.jpa.term.TermLoaderSvcImpl.class.getResourceAsStream("/ca/uhn/fhir/jpa/term/loinc/loincupload.properties")) {
			retVal.load(propertyStream);
		} catch (IOException e) {
			throw new InternalErrorException(Msg.code(866) + "Failed to process loinc.properties", e);
		}

		for (FileDescriptor next : theDescriptors.getUncompressedFileDescriptors()) {
			if (next.getFilename().endsWith(thePropertiesFile)) {
				try {
					try (InputStream inputStream = next.getInputStream()) {
						retVal.load(inputStream);
					}
				} catch (IOException e) {
					throw new InternalErrorException(Msg.code(867) + "Failed to read " + thePropertiesFile, e);
				}
			}
		}
		return retVal;
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

	private UploadStatistics processImgthlaFiles(LoadedFileDescriptors theDescriptors, RequestDetails theRequestDetails) {
		final TermCodeSystemVersion codeSystemVersion = new TermCodeSystemVersion();
		final List<ValueSet> valueSets = new ArrayList<>();
		final List<ConceptMap> conceptMaps = new ArrayList<>();

		CodeSystem imgthlaCs;
		try {
			String imgthlaCsString = IOUtils.toString(BaseTermReadSvcImpl.class.getResourceAsStream("/ca/uhn/fhir/jpa/term/imgthla/imgthla.xml"), Charsets.UTF_8);
			imgthlaCs = FhirContext.forR4().newXmlParser().parseResource(CodeSystem.class, imgthlaCsString);
		} catch (IOException e) {
			throw new InternalErrorException(Msg.code(869) + "Failed to load imgthla.xml", e);
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

					LineNumberReader lnr = new LineNumberReader(reader);
					while (lnr.readLine() != null) {
					}
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
					while (lnr.readLine() != null) {
					}
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
		int conceptCount = rootConceptCount;
		ourLog.info("Have {} total concepts, {} root concepts, {} ValueSets", conceptCount, rootConceptCount, valueSetCount);

		// remove this when fully implemented ...
		throw new InternalErrorException(Msg.code(874) + "HLA nomenclature terminology upload not yet fully implemented.");

//		IIdType target = storeCodeSystem(theRequestDetails, codeSystemVersion, imgthlaCs, valueSets, conceptMaps);
//
//		return new UploadStatistics(conceptCount, target);
	}

	UploadStatistics processLoincFiles(LoadedFileDescriptors theDescriptors, RequestDetails theRequestDetails, Properties theUploadProperties, Boolean theCloseFiles) {
		final TermCodeSystemVersion codeSystemVersion = new TermCodeSystemVersion();
		final Map<String, TermConcept> code2concept = new HashMap<>();
		final List<ValueSet> valueSets = new ArrayList<>();
		final List<ConceptMap> conceptMaps = new ArrayList<>();

		final List<LoincLinguisticVariantsHandler.LinguisticVariant> linguisticVariants = new ArrayList<>();

		LoincXmlFileZipContentsHandler loincXmlHandler = getLoincXmlFileZipContentsHandler();
		iterateOverZipFile(theDescriptors, "loinc.xml", false, false, loincXmlHandler);
		String loincCsString = loincXmlHandler.getContents();
		if (isBlank(loincCsString)) {
			throw new InvalidRequestException(Msg.code(875) + "Did not find loinc.xml in the ZIP distribution.");
		}

		CodeSystem loincCs = FhirContext.forR4().newXmlParser().parseResource(CodeSystem.class, loincCsString);
		if (isNotBlank(loincCs.getVersion())) {
			throw new InvalidRequestException(Msg.code(876) + "'loinc.xml' file must not have a version defined. To define a version use '" +
				LOINC_CODESYSTEM_VERSION.getCode() + "' property of 'loincupload.properties' file");
		}

		String codeSystemVersionId = theUploadProperties.getProperty(LOINC_CODESYSTEM_VERSION.getCode());
		if (codeSystemVersionId != null) {
			loincCs.setVersion(codeSystemVersionId);
			loincCs.setId(loincCs.getId() + "-" + codeSystemVersionId);
		}

		Map<String, CodeSystem.PropertyType> propertyNamesToTypes = new HashMap<>();
		for (CodeSystem.PropertyComponent nextProperty : loincCs.getProperty()) {
			String nextPropertyCode = nextProperty.getCode();
			CodeSystem.PropertyType nextPropertyType = nextProperty.getType();
			if (isNotBlank(nextPropertyCode)) {
				propertyNamesToTypes.put(nextPropertyCode, nextPropertyType);
			}
		}

		// TODO: DM 2019-09-13 - Manually add EXTERNAL_COPYRIGHT_NOTICE property until Regenstrief adds this to loinc.xml
		if (!propertyNamesToTypes.containsKey("EXTERNAL_COPYRIGHT_NOTICE")) {
			String externalCopyRightNoticeCode = "EXTERNAL_COPYRIGHT_NOTICE";
			CodeSystem.PropertyType externalCopyRightNoticeType = CodeSystem.PropertyType.STRING;
			propertyNamesToTypes.put(externalCopyRightNoticeCode, externalCopyRightNoticeType);
		}

		IZipContentsHandlerCsv handler;

		// Part
		handler = new LoincPartHandler(codeSystemVersion, code2concept);
		iterateOverZipFileCsv(theDescriptors, theUploadProperties.getProperty(LOINC_PART_FILE.getCode(), LOINC_PART_FILE_DEFAULT.getCode()), handler, ',', QuoteMode.NON_NUMERIC, false);
		Map<PartTypeAndPartName, String> partTypeAndPartNameToPartNumber = ((LoincPartHandler) handler).getPartTypeAndPartNameToPartNumber();

		// LOINC codes
		handler = new LoincHandler(codeSystemVersion, code2concept, propertyNamesToTypes, partTypeAndPartNameToPartNumber);
		iterateOverZipFileCsv(theDescriptors, theUploadProperties.getProperty(LOINC_FILE.getCode(), LOINC_FILE_DEFAULT.getCode()), handler, ',', QuoteMode.NON_NUMERIC, false);

		// LOINC hierarchy
		handler = new LoincHierarchyHandler(codeSystemVersion, code2concept);
		iterateOverZipFileCsv(theDescriptors, theUploadProperties.getProperty(LOINC_HIERARCHY_FILE.getCode(), LOINC_HIERARCHY_FILE_DEFAULT.getCode()), handler, ',', QuoteMode.NON_NUMERIC, false);

		// Answer lists (ValueSets of potential answers/values for LOINC "questions")
		handler = new LoincAnswerListHandler(codeSystemVersion, code2concept, valueSets, conceptMaps, theUploadProperties, loincCs.getCopyright());
		iterateOverZipFileCsv(theDescriptors, theUploadProperties.getProperty(LOINC_ANSWERLIST_FILE.getCode(), LOINC_ANSWERLIST_FILE_DEFAULT.getCode()), handler, ',', QuoteMode.NON_NUMERIC, false);

		// Answer list links (connects LOINC observation codes to answer list codes)
		handler = new LoincAnswerListLinkHandler(code2concept);
		iterateOverZipFileCsv(theDescriptors, theUploadProperties.getProperty(LOINC_ANSWERLIST_LINK_FILE.getCode(), LOINC_ANSWERLIST_LINK_FILE_DEFAULT.getCode()), handler, ',', QuoteMode.NON_NUMERIC, false);

		// RSNA playbook
		// Note that this should come before the "Part Related Code Mapping"
		// file because there are some duplicate mappings between these
		// two files, and the RSNA Playbook file has more metadata
		handler = new LoincRsnaPlaybookHandler(code2concept, valueSets, conceptMaps, theUploadProperties, loincCs.getCopyright());
		iterateOverZipFileCsv(theDescriptors, theUploadProperties.getProperty(LOINC_RSNA_PLAYBOOK_FILE.getCode(), LOINC_RSNA_PLAYBOOK_FILE_DEFAULT.getCode()), handler, ',', QuoteMode.NON_NUMERIC, false);

		// Part related code mapping
		handler = new LoincPartRelatedCodeMappingHandler(code2concept, valueSets, conceptMaps, theUploadProperties, loincCs.getCopyright());
		iterateOverZipFileCsv(theDescriptors, theUploadProperties.getProperty(LOINC_PART_RELATED_CODE_MAPPING_FILE.getCode(), LOINC_PART_RELATED_CODE_MAPPING_FILE_DEFAULT.getCode()), handler, ',', QuoteMode.NON_NUMERIC, false);

		// Document ontology
		handler = new LoincDocumentOntologyHandler(code2concept, propertyNamesToTypes, valueSets, conceptMaps, theUploadProperties, loincCs.getCopyright());
		iterateOverZipFileCsv(theDescriptors, theUploadProperties.getProperty(LOINC_DOCUMENT_ONTOLOGY_FILE.getCode(), LOINC_DOCUMENT_ONTOLOGY_FILE_DEFAULT.getCode()), handler, ',', QuoteMode.NON_NUMERIC, false);

		// Top 2000 codes - US
		handler = new LoincTop2000LabResultsUsHandler(code2concept, valueSets, conceptMaps, theUploadProperties, loincCs.getCopyright());
		iterateOverZipFileCsvOptional(theDescriptors, theUploadProperties.getProperty(LOINC_TOP2000_COMMON_LAB_RESULTS_US_FILE.getCode(), LOINC_TOP2000_COMMON_LAB_RESULTS_US_FILE_DEFAULT.getCode()), handler, ',', QuoteMode.NON_NUMERIC, false);

		// Top 2000 codes - SI
		handler = new LoincTop2000LabResultsSiHandler(code2concept, valueSets, conceptMaps, theUploadProperties, loincCs.getCopyright());
		iterateOverZipFileCsvOptional(theDescriptors, theUploadProperties.getProperty(LOINC_TOP2000_COMMON_LAB_RESULTS_SI_FILE.getCode(), LOINC_TOP2000_COMMON_LAB_RESULTS_SI_FILE_DEFAULT.getCode()), handler, ',', QuoteMode.NON_NUMERIC, false);

		// Universal lab order ValueSet
		handler = new LoincUniversalOrderSetHandler(code2concept, valueSets, conceptMaps, theUploadProperties);
		iterateOverZipFileCsv(theDescriptors, theUploadProperties.getProperty(LOINC_UNIVERSAL_LAB_ORDER_VALUESET_FILE.getCode(), LOINC_UNIVERSAL_LAB_ORDER_VALUESET_FILE_DEFAULT.getCode()), handler, ',', QuoteMode.NON_NUMERIC, false);

		// IEEE medical device codes
		handler = new LoincIeeeMedicalDeviceCodeHandler(code2concept, valueSets, conceptMaps, theUploadProperties, loincCs.getCopyright());
		iterateOverZipFileCsv(theDescriptors, theUploadProperties.getProperty(LOINC_IEEE_MEDICAL_DEVICE_CODE_MAPPING_TABLE_FILE.getCode(), LOINC_IEEE_MEDICAL_DEVICE_CODE_MAPPING_TABLE_FILE_DEFAULT.getCode()), handler, ',', QuoteMode.NON_NUMERIC, false);

		// Imaging document codes
		handler = new LoincImagingDocumentCodeHandler(code2concept, valueSets, conceptMaps, theUploadProperties);
		iterateOverZipFileCsv(theDescriptors, theUploadProperties.getProperty(LOINC_IMAGING_DOCUMENT_CODES_FILE.getCode(), LOINC_IMAGING_DOCUMENT_CODES_FILE_DEFAULT.getCode()), handler, ',', QuoteMode.NON_NUMERIC, false);

		// Group
		handler = new LoincGroupFileHandler(code2concept, valueSets, conceptMaps, theUploadProperties, loincCs.getCopyright());
		iterateOverZipFileCsv(theDescriptors, theUploadProperties.getProperty(LOINC_GROUP_FILE.getCode(), LOINC_GROUP_FILE_DEFAULT.getCode()), handler, ',', QuoteMode.NON_NUMERIC, false);

		// Group terms
		handler = new LoincGroupTermsFileHandler(code2concept, valueSets, conceptMaps, theUploadProperties);
		iterateOverZipFileCsv(theDescriptors, theUploadProperties.getProperty(LOINC_GROUP_TERMS_FILE.getCode(), LOINC_GROUP_TERMS_FILE_DEFAULT.getCode()), handler, ',', QuoteMode.NON_NUMERIC, false);

		// Parent group
		handler = new LoincParentGroupFileHandler(code2concept, valueSets, conceptMaps, theUploadProperties);
		iterateOverZipFileCsv(theDescriptors, theUploadProperties.getProperty(LOINC_PARENT_GROUP_FILE.getCode(), LOINC_PARENT_GROUP_FILE_DEFAULT.getCode()), handler, ',', QuoteMode.NON_NUMERIC, false);

		// Part link
		handler = new LoincPartLinkHandler(codeSystemVersion, code2concept, propertyNamesToTypes);
		iterateOverZipFileCsvOptional(theDescriptors, theUploadProperties.getProperty(LOINC_PART_LINK_FILE.getCode(), LOINC_PART_LINK_FILE_DEFAULT.getCode()), handler, ',', QuoteMode.NON_NUMERIC, false);
		iterateOverZipFileCsvOptional(theDescriptors, theUploadProperties.getProperty(LOINC_PART_LINK_FILE_PRIMARY.getCode(), LOINC_PART_LINK_FILE_PRIMARY_DEFAULT.getCode()), handler, ',', QuoteMode.NON_NUMERIC, false);
		iterateOverZipFileCsvOptional(theDescriptors, theUploadProperties.getProperty(LOINC_PART_LINK_FILE_SUPPLEMENTARY.getCode(), LOINC_PART_LINK_FILE_SUPPLEMENTARY_DEFAULT.getCode()), handler, ',', QuoteMode.NON_NUMERIC, false);

		// Consumer Name
		handler = new LoincConsumerNameHandler(code2concept);
		iterateOverZipFileCsvOptional(theDescriptors, theUploadProperties.getProperty(LOINC_CONSUMER_NAME_FILE.getCode(), LOINC_CONSUMER_NAME_FILE_DEFAULT.getCode()), handler, ',', QuoteMode.NON_NUMERIC, false);

		// Linguistic Variants
		handler = new LoincLinguisticVariantsHandler(linguisticVariants);
		iterateOverZipFileCsvOptional(theDescriptors, theUploadProperties.getProperty(LOINC_LINGUISTIC_VARIANTS_FILE.getCode(), LOINC_LINGUISTIC_VARIANTS_FILE_DEFAULT.getCode()), handler, ',', QuoteMode.NON_NUMERIC, false);

		String langFileName = null;
		for (LoincLinguisticVariantsHandler.LinguisticVariant linguisticVariant : linguisticVariants) {
			handler = new LoincLinguisticVariantHandler(code2concept, linguisticVariant.getLanguageCode());
			langFileName = linguisticVariant.getLinguisticVariantFileName();
			iterateOverZipFileCsvOptional(theDescriptors, theUploadProperties.getProperty(LOINC_LINGUISTIC_VARIANTS_PATH.getCode() + langFileName, LOINC_LINGUISTIC_VARIANTS_PATH_DEFAULT.getCode() + langFileName), handler, ',', QuoteMode.NON_NUMERIC, false);
		}

		if (theCloseFiles) {
			IOUtils.closeQuietly(theDescriptors);
		}

		valueSets.add(getValueSetLoincAll(theUploadProperties, loincCs.getCopyright()));

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

	@VisibleForTesting
	protected LoincXmlFileZipContentsHandler getLoincXmlFileZipContentsHandler() {
		return new LoincXmlFileZipContentsHandler();
	}


	private ValueSet getValueSetLoincAll(Properties theUploadProperties, String theCopyrightStatement) {
		ValueSet retVal = new ValueSet();

		String codeSystemVersionId = theUploadProperties.getProperty(LOINC_CODESYSTEM_VERSION.getCode());
		String valueSetId;
		if (codeSystemVersionId != null) {
			valueSetId = LOINC_ALL_VALUESET_ID + "-" + codeSystemVersionId;
		} else {
			valueSetId = LOINC_ALL_VALUESET_ID;
		}
		retVal.setId(valueSetId);
		retVal.setUrl("http://loinc.org/vs");
		retVal.setVersion(codeSystemVersionId);
		retVal.setName("All LOINC codes");
		retVal.setStatus(Enumerations.PublicationStatus.ACTIVE);
		retVal.setDate(new Date());
		retVal.setPublisher("Regenstrief Institute, Inc.");
		retVal.setDescription("A value set that includes all LOINC codes");
		retVal.setCopyright(theCopyrightStatement);
		retVal.getCompose().addInclude().setSystem(ITermLoaderSvc.LOINC_URI).setVersion(codeSystemVersionId);

		return retVal;
	}

	private UploadStatistics processSnomedCtFiles(LoadedFileDescriptors theDescriptors, RequestDetails theRequestDetails) {
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

		handler = new SctHandlerRelationship(codeSystemVersion, rootConcepts, code2concept);
		iterateOverZipFileCsv(theDescriptors, SCT_FILE_RELATIONSHIP, handler, '\t', null, true);

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

	private IIdType storeCodeSystem(RequestDetails theRequestDetails, final TermCodeSystemVersion theCodeSystemVersion, CodeSystem theCodeSystem, List<ValueSet> theValueSets, List<ConceptMap> theConceptMaps) {
		Validate.isTrue(theCodeSystem.getContent() == CodeSystem.CodeSystemContentMode.NOTPRESENT);

		List<ValueSet> valueSets = ObjectUtils.defaultIfNull(theValueSets, Collections.emptyList());
		List<ConceptMap> conceptMaps = ObjectUtils.defaultIfNull(theConceptMaps, Collections.emptyList());

		IIdType retVal;
		myDeferredStorageSvc.setProcessDeferred(false);
		retVal = myCodeSystemStorageSvc.storeNewCodeSystemVersion(theCodeSystem, theCodeSystemVersion, theRequestDetails, valueSets, conceptMaps);
		myDeferredStorageSvc.setProcessDeferred(true);

		return retVal;
	}

	public static void iterateOverZipFileCsv(LoadedFileDescriptors theDescriptors, String theFileNamePart, IZipContentsHandlerCsv theHandler, char theDelimiter, QuoteMode theQuoteMode, boolean theIsPartialFilename) {
		iterateOverZipFileCsv(theDescriptors, theFileNamePart, theHandler, theDelimiter, theQuoteMode, theIsPartialFilename, true);
	}

	public static void iterateOverZipFileCsvOptional(LoadedFileDescriptors theDescriptors, String theFileNamePart, IZipContentsHandlerCsv theHandler, char theDelimiter, QuoteMode theQuoteMode, boolean theIsPartialFilename) {
		iterateOverZipFileCsv(theDescriptors, theFileNamePart, theHandler, theDelimiter, theQuoteMode, theIsPartialFilename, false);
	}

	private static void iterateOverZipFileCsv(LoadedFileDescriptors theDescriptors, String theFileNamePart, IZipContentsHandlerCsv theHandler, char theDelimiter, QuoteMode theQuoteMode, boolean theIsPartialFilename, boolean theRequireMatch) {
		IZipContentsHandler handler = (reader, filename) -> {
			CSVParser parsed = newCsvRecords(theDelimiter, theQuoteMode, reader);
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
					ourLog.info(" * Processed {} records in {}", count, filename);
					nextLoggedCount += LOG_INCREMENT;
				}
			}
		};

		iterateOverZipFile(theDescriptors, theFileNamePart, theIsPartialFilename, theRequireMatch, handler);

	}

	private static void iterateOverZipFile(LoadedFileDescriptors theDescriptors, String theFileNamePart, boolean theIsPartialFilename, boolean theRequireMatch, IZipContentsHandler theHandler) {
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
	private static CSVParser newCsvRecords(char theDelimiter, QuoteMode theQuoteMode, Reader theReader) throws IOException {
		CSVParser parsed;
		CSVFormat format = CSVFormat
			.newFormat(theDelimiter)
			.withFirstRecordAsHeader()
			.withTrim();
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

	public static TermConceptProperty getOrCreateConceptProperty(Map<String, List<TermConceptProperty>> code2Properties, String code, String key) {
		List<TermConceptProperty> termConceptProperties = code2Properties.get(code);
		if (termConceptProperties == null)
			return new TermConceptProperty();
		Optional<TermConceptProperty> termConceptProperty = termConceptProperties.stream().filter(property -> key.equals(property.getKey())).findFirst();
		return termConceptProperty.isPresent() ? termConceptProperty.get() : new TermConceptProperty();
	}
}
