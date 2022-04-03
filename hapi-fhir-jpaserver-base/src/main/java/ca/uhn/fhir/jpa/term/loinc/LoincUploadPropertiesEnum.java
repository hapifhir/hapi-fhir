package ca.uhn.fhir.jpa.term.loinc;

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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This enum is used to facilitate configurable filenames when uploading LOINC.
 */
public enum LoincUploadPropertiesEnum {
	/**
	 * Sorting agnostic.
	 */

	LOINC_UPLOAD_PROPERTIES_FILE("loincupload.properties"),
	LOINC_XML_FILE("loinc.xml"),

	/*
	 * MANDATORY
	 */
	// Answer lists (ValueSets of potential answers/values for LOINC "questions")
	LOINC_ANSWERLIST_FILE("loinc.answerlist.file"),
	LOINC_ANSWERLIST_FILE_DEFAULT("AccessoryFiles/AnswerFile/AnswerList.csv"),
	// Answer list links (connects LOINC observation codes to answer list codes)
	LOINC_ANSWERLIST_LINK_FILE("loinc.answerlist.link.file"),
	LOINC_ANSWERLIST_LINK_FILE_DEFAULT("AccessoryFiles/AnswerFile/LoincAnswerListLink.csv"),

	// Document ontology
	LOINC_DOCUMENT_ONTOLOGY_FILE("loinc.document.ontology.file"),
	LOINC_DOCUMENT_ONTOLOGY_FILE_DEFAULT("AccessoryFiles/DocumentOntology/DocumentOntology.csv"),

	// LOINC codes
	LOINC_FILE("loinc.file"),
	LOINC_FILE_DEFAULT("LoincTable/Loinc.csv"),

	// LOINC hierarchy
	LOINC_HIERARCHY_FILE("loinc.hierarchy.file"),
	LOINC_HIERARCHY_FILE_DEFAULT("AccessoryFiles/MultiAxialHierarchy/MultiAxialHierarchy.csv"),

	// IEEE medical device codes
	LOINC_IEEE_MEDICAL_DEVICE_CODE_MAPPING_TABLE_FILE("loinc.ieee.medical.device.code.mapping.table.file"),
	LOINC_IEEE_MEDICAL_DEVICE_CODE_MAPPING_TABLE_FILE_DEFAULT("AccessoryFiles/LoincIeeeMedicalDeviceCodeMappingTable/LoincIeeeMedicalDeviceCodeMappingTable.csv"),

	// Imaging document codes
	LOINC_IMAGING_DOCUMENT_CODES_FILE("loinc.imaging.document.codes.file"),
	LOINC_IMAGING_DOCUMENT_CODES_FILE_DEFAULT("AccessoryFiles/ImagingDocuments/ImagingDocumentCodes.csv"),

	// Part
	LOINC_PART_FILE("loinc.part.file"),
	LOINC_PART_FILE_DEFAULT("AccessoryFiles/PartFile/Part.csv"),

	// Part link
	LOINC_PART_LINK_FILE("loinc.part.link.file"),
	LOINC_PART_LINK_FILE_DEFAULT("AccessoryFiles/PartFile/LoincPartLink.csv"),
	LOINC_PART_LINK_FILE_PRIMARY("loinc.part.link.primary.file"),
	LOINC_PART_LINK_FILE_PRIMARY_DEFAULT("AccessoryFiles/PartFile/LoincPartLink_Primary.csv"),
	LOINC_PART_LINK_FILE_SUPPLEMENTARY("loinc.part.link.supplementary.file"),
	LOINC_PART_LINK_FILE_SUPPLEMENTARY_DEFAULT("AccessoryFiles/PartFile/LoincPartLink_Supplementary.csv"),

	// Part related code mapping
	LOINC_PART_RELATED_CODE_MAPPING_FILE("loinc.part.related.code.mapping.file"),
	LOINC_PART_RELATED_CODE_MAPPING_FILE_DEFAULT("AccessoryFiles/PartFile/PartRelatedCodeMapping.csv"),

	// RSNA playbook
	LOINC_RSNA_PLAYBOOK_FILE("loinc.rsna.playbook.file"),
	LOINC_RSNA_PLAYBOOK_FILE_DEFAULT("AccessoryFiles/LoincRsnaRadiologyPlaybook/LoincRsnaRadiologyPlaybook.csv"),

	// Top 2000 codes - SI
	LOINC_TOP2000_COMMON_LAB_RESULTS_SI_FILE("loinc.top2000.common.lab.results.si.file"),
	LOINC_TOP2000_COMMON_LAB_RESULTS_SI_FILE_DEFAULT("AccessoryFiles/Top2000Results/SI/Top2000CommonLabResultsSi.csv"),
	// Top 2000 codes - US
	LOINC_TOP2000_COMMON_LAB_RESULTS_US_FILE("loinc.top2000.common.lab.results.us.file"),
	LOINC_TOP2000_COMMON_LAB_RESULTS_US_FILE_DEFAULT("AccessoryFiles/Top2000Results/US/Top2000CommonLabResultsUs.csv"),

	// Universal lab order ValueSet
	LOINC_UNIVERSAL_LAB_ORDER_VALUESET_FILE("loinc.universal.lab.order.valueset.file"),
	LOINC_UNIVERSAL_LAB_ORDER_VALUESET_FILE_DEFAULT("AccessoryFiles/LoincUniversalLabOrdersValueSet/LoincUniversalLabOrdersValueSet.csv"),

	/*
	 * OPTIONAL
	 */
	// This is the version identifier for the LOINC code system
	LOINC_CODESYSTEM_VERSION("loinc.codesystem.version"),

	// Indicates if loading version has to become current
	LOINC_CODESYSTEM_MAKE_CURRENT("loinc.codesystem.make.current"),

	// This is the version identifier for the answer list file
	LOINC_ANSWERLIST_VERSION("loinc.answerlist.version"),

	// This is the version identifier for uploaded ConceptMap resources
	LOINC_CONCEPTMAP_VERSION("loinc.conceptmap.version"),

	// Group
	LOINC_GROUP_FILE("loinc.group.file"),
	LOINC_GROUP_FILE_DEFAULT("AccessoryFiles/GroupFile/Group.csv"),
	// Group terms
	LOINC_GROUP_TERMS_FILE("loinc.group.terms.file"),
	LOINC_GROUP_TERMS_FILE_DEFAULT("AccessoryFiles/GroupFile/GroupLoincTerms.csv"),
	// Parent group
	LOINC_PARENT_GROUP_FILE("loinc.parent.group.file"),
	LOINC_PARENT_GROUP_FILE_DEFAULT("AccessoryFiles/GroupFile/ParentGroup.csv"),

	// Consumer Name
	LOINC_CONSUMER_NAME_FILE("loinc.consumer.name.file"),
	LOINC_CONSUMER_NAME_FILE_DEFAULT("AccessoryFiles/ConsumerName/ConsumerName.csv"),

	// Linguistic Variants
	LOINC_LINGUISTIC_VARIANTS_FILE("loinc.linguistic.variants.file"),
	LOINC_LINGUISTIC_VARIANTS_FILE_DEFAULT("AccessoryFiles/LinguisticVariants/LinguisticVariants.csv"),

	// Linguistic Variants Folder Path which contains variants for different languages
	LOINC_LINGUISTIC_VARIANTS_PATH("loinc.linguistic.variants.path"),
	LOINC_LINGUISTIC_VARIANTS_PATH_DEFAULT("AccessoryFiles/LinguisticVariants/"),

	/*
	 * DUPLICATES
	 */
	LOINC_DUPLICATE_FILE_DEFAULT("AccessoryFiles/PanelsAndForms/Loinc.csv"),
	LOINC_ANSWERLIST_DUPLICATE_FILE_DEFAULT("AccessoryFiles/PanelsAndForms/AnswerList.csv"),
	LOINC_ANSWERLIST_LINK_DUPLICATE_FILE_DEFAULT("AccessoryFiles/PanelsAndForms/LoincAnswerListLink.csv");

	private static Map<String, LoincUploadPropertiesEnum> ourValues;
	private String myCode;

	LoincUploadPropertiesEnum(String theCode) {
		myCode = theCode;
	}

	public String getCode() {
		return myCode;
	}

	public static LoincUploadPropertiesEnum fromCode(String theCode) {
		if (ourValues == null) {
			HashMap<String, LoincUploadPropertiesEnum> values = new HashMap<String, LoincUploadPropertiesEnum>();
			for (LoincUploadPropertiesEnum next : values()) {
				values.put(next.getCode(), next);
			}
			ourValues = Collections.unmodifiableMap(values);
		}
		return ourValues.get(theCode);
	}

	/**
	 * Convert from Enum ordinal to Enum type.
	 *
	 * Usage:
	 *
	 * <code>LoincUploadPropertiesEnum loincUploadPropertiesEnum = LoincUploadPropertiesEnum.values[ordinal];</code>
	 */
	public static final LoincUploadPropertiesEnum values[] = values();
}
