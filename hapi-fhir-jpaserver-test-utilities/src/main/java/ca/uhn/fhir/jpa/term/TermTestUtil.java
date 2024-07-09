/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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

import ca.uhn.fhir.jpa.entity.TermConceptDesignation;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;

import java.io.IOException;
import java.util.Collection;

import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_ANSWERLIST_DUPLICATE_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_ANSWERLIST_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_ANSWERLIST_LINK_DUPLICATE_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_ANSWERLIST_LINK_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_CONSUMER_NAME_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_DOCUMENT_ONTOLOGY_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_DUPLICATE_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_GROUP_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_GROUP_TERMS_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_HIERARCHY_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_IEEE_MEDICAL_DEVICE_CODE_MAPPING_TABLE_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_IMAGING_DOCUMENT_CODES_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_LINGUISTIC_VARIANTS_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_LINGUISTIC_VARIANTS_PATH_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_PARENT_GROUP_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_PART_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_PART_LINK_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_PART_LINK_FILE_PRIMARY_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_PART_LINK_FILE_SUPPLEMENTARY_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_PART_RELATED_CODE_MAPPING_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_RSNA_PLAYBOOK_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_TOP2000_COMMON_LAB_RESULTS_SI_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_TOP2000_COMMON_LAB_RESULTS_US_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_UNIVERSAL_LAB_ORDER_VALUESET_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_UPLOAD_PROPERTIES_FILE;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_XML_FILE;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class TermTestUtil {
	public static final String URL_MY_CODE_SYSTEM = "http://example.com/my_code_system";
	public static final String URL_MY_VALUE_SET = "http://example.com/my_value_set";

	private TermTestUtil() {}

	public static void addLoincMandatoryFilesAndSinglePartLinkToZip(ZipCollectionBuilder theFiles) throws IOException {
		addBaseLoincMandatoryFilesToZip(theFiles, true);
		theFiles.addFileZip("/loinc/", "loincupload_singlepartlink.properties");
		theFiles.addFileZip("/loinc/", LOINC_PART_LINK_FILE_DEFAULT.getCode());
	}

	public static void addLoincMandatoryFilesAndConsumerNameAndLinguisticVariants(ZipCollectionBuilder theFiles)
			throws IOException {
		addBaseLoincMandatoryFilesToZip(theFiles, true);
		theFiles.addFileZip("/loinc/", "loincupload_singlepartlink.properties");
		theFiles.addFileZip("/loinc/", LOINC_PART_LINK_FILE_DEFAULT.getCode());
		theFiles.addFileZip("/loinc/", LOINC_CONSUMER_NAME_FILE_DEFAULT.getCode());
		theFiles.addFileZip("/loinc/", LOINC_LINGUISTIC_VARIANTS_FILE_DEFAULT.getCode());
		theFiles.addFileZip("/loinc/", LOINC_LINGUISTIC_VARIANTS_PATH_DEFAULT.getCode() + "zhCN5LinguisticVariant.csv");
		theFiles.addFileZip(
				"/loinc/", LOINC_LINGUISTIC_VARIANTS_PATH_DEFAULT.getCode() + "deAT24LinguisticVariant.csv");
		theFiles.addFileZip("/loinc/", LOINC_LINGUISTIC_VARIANTS_PATH_DEFAULT.getCode() + "frCA8LinguisticVariant.csv");
	}

	public static void addLoincMandatoryFilesToZip(ZipCollectionBuilder theFiles) throws IOException {
		addBaseLoincMandatoryFilesToZip(theFiles, true);
		theFiles.addFileZip("/loinc/", LOINC_UPLOAD_PROPERTIES_FILE.getCode());
		theFiles.addFileZip("/loinc/", LOINC_PART_LINK_FILE_PRIMARY_DEFAULT.getCode());
		theFiles.addFileZip("/loinc/", LOINC_PART_LINK_FILE_SUPPLEMENTARY_DEFAULT.getCode());
	}

	public static void addLoincMandatoryFilesWithoutTop2000ToZip(ZipCollectionBuilder theFiles) throws IOException {
		addBaseLoincMandatoryFilesToZip(theFiles, false);
		theFiles.addFileZip("/loinc/", LOINC_UPLOAD_PROPERTIES_FILE.getCode());
		theFiles.addFileZip("/loinc/", LOINC_PART_LINK_FILE_PRIMARY_DEFAULT.getCode());
		theFiles.addFileZip("/loinc/", LOINC_PART_LINK_FILE_SUPPLEMENTARY_DEFAULT.getCode());
	}

	public static void addLoincMandatoryFilesWithPropertiesFileToZip(
			ZipCollectionBuilder theFiles, String thePropertiesFile) throws IOException {
		if (thePropertiesFile != null) {
			theFiles.addFileZip("/loinc/", thePropertiesFile);
		}
		theFiles.addFileZip("/loinc/", LOINC_PART_LINK_FILE_PRIMARY_DEFAULT.getCode());
		theFiles.addFileZip("/loinc/", LOINC_PART_LINK_FILE_SUPPLEMENTARY_DEFAULT.getCode());
		addBaseLoincMandatoryFilesToZip(theFiles, true);
	}

	static void addBaseLoincMandatoryFilesToZip(ZipCollectionBuilder theFiles, Boolean theIncludeTop2000)
			throws IOException {
		theFiles.addFileZip("/loinc/", LOINC_XML_FILE.getCode());
		theFiles.addFileZip("/loinc/", LOINC_GROUP_FILE_DEFAULT.getCode());
		theFiles.addFileZip("/loinc/", LOINC_GROUP_TERMS_FILE_DEFAULT.getCode());
		theFiles.addFileZip("/loinc/", LOINC_PARENT_GROUP_FILE_DEFAULT.getCode());
		theFiles.addFileZip("/loinc/", LOINC_FILE_DEFAULT.getCode());
		theFiles.addFileZip("/loinc/", LOINC_DUPLICATE_FILE_DEFAULT.getCode());
		theFiles.addFileZip("/loinc/", LOINC_HIERARCHY_FILE_DEFAULT.getCode());
		theFiles.addFileZip("/loinc/", LOINC_ANSWERLIST_FILE_DEFAULT.getCode());
		theFiles.addFileZip("/loinc/", LOINC_ANSWERLIST_DUPLICATE_FILE_DEFAULT.getCode());
		theFiles.addFileZip("/loinc/", LOINC_ANSWERLIST_LINK_FILE_DEFAULT.getCode());
		theFiles.addFileZip("/loinc/", LOINC_ANSWERLIST_LINK_DUPLICATE_FILE_DEFAULT.getCode());
		theFiles.addFileZip("/loinc/", LOINC_PART_FILE_DEFAULT.getCode());
		theFiles.addFileZip("/loinc/", LOINC_PART_RELATED_CODE_MAPPING_FILE_DEFAULT.getCode());
		theFiles.addFileZip("/loinc/", LOINC_DOCUMENT_ONTOLOGY_FILE_DEFAULT.getCode());
		theFiles.addFileZip("/loinc/", LOINC_RSNA_PLAYBOOK_FILE_DEFAULT.getCode());
		theFiles.addFileZip("/loinc/", LOINC_UNIVERSAL_LAB_ORDER_VALUESET_FILE_DEFAULT.getCode());
		theFiles.addFileZip("/loinc/", LOINC_IEEE_MEDICAL_DEVICE_CODE_MAPPING_TABLE_FILE_DEFAULT.getCode());
		theFiles.addFileZip("/loinc/", LOINC_IMAGING_DOCUMENT_CODES_FILE_DEFAULT.getCode());
		if (theIncludeTop2000) {
			theFiles.addFileZip("/loinc/", LOINC_TOP2000_COMMON_LAB_RESULTS_SI_FILE_DEFAULT.getCode());
			theFiles.addFileZip("/loinc/", LOINC_TOP2000_COMMON_LAB_RESULTS_US_FILE_DEFAULT.getCode());
		}
	}

	static void verifyConsumerName(Collection<TermConceptDesignation> designationList, String theConsumerName) {

		TermConceptDesignation consumerNameDesignation = null;
		for (TermConceptDesignation designation : designationList) {
			if ("ConsumerName".equals(designation.getUseDisplay())) {
				consumerNameDesignation = designation;
			}
		}
		assertEquals(theConsumerName, consumerNameDesignation.getValue());
	}

	static void verifyLinguisticVariant(
			Collection<TermConceptDesignation> designationList,
			String theLanguage,
			String theComponent,
			String theProperty,
			String theTimeAspct,
			String theSystem,
			String theScaleTyp,
			String methodType,
			String theClass,
			String theShortName,
			String theLongCommonName,
			String theRelatedName2,
			String theLinguisticVariantDisplayName) {

		TermConceptDesignation formalNameDes = null;
		TermConceptDesignation shortNameDes = null;
		TermConceptDesignation longCommonNameDes = null;
		TermConceptDesignation linguisticVariantDisplayNameDes = null;

		for (TermConceptDesignation designation : designationList) {
			if (theLanguage.equals(designation.getLanguage())) {

				if ("FullySpecifiedName".equals(designation.getUseDisplay())) formalNameDes = designation;

				if ("SHORTNAME".equals(designation.getUseDisplay())) shortNameDes = designation;
				if ("LONG_COMMON_NAME".equals(designation.getUseDisplay())) longCommonNameDes = designation;
				if ("LinguisticVariantDisplayName".equals(designation.getUseDisplay()))
					linguisticVariantDisplayNameDes = designation;
			}
		}

		verifyDesignation(
				formalNameDes,
				ITermLoaderSvc.LOINC_URI,
				"FullySpecifiedName",
				theComponent + ":" + theProperty + ":" + theTimeAspct + ":" + theSystem + ":" + theScaleTyp + ":"
						+ methodType);
		verifyDesignation(shortNameDes, ITermLoaderSvc.LOINC_URI, "SHORTNAME", theShortName);
		verifyDesignation(longCommonNameDes, ITermLoaderSvc.LOINC_URI, "LONG_COMMON_NAME", theLongCommonName);
		verifyDesignation(
				linguisticVariantDisplayNameDes,
				ITermLoaderSvc.LOINC_URI,
				"LinguisticVariantDisplayName",
				theLinguisticVariantDisplayName);
	}

	private static void verifyDesignation(
			TermConceptDesignation theDesignation, String theUseSystem, String theUseCode, String theValue) {
		if (theDesignation == null) return;
		assertEquals(theUseSystem, theDesignation.getUseSystem());
		assertEquals(theUseCode, theDesignation.getUseCode());
		assertEquals(theValue, theDesignation.getValue());
	}
}
