/*-
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
package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.jpa.batch2.jobs.term.base.BaseImportTerminologyFileCsvStep;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyJobParameters;

public abstract class BaseImportLoincStep<CT extends BaseImportLoincStep.MyBaseContext>
		extends BaseImportTerminologyFileCsvStep<ImportTerminologyJobParameters, CT> {
	/**
	 * This is <b>NOT</b> the LOINC CodeSystem URI! It is just
	 * the website URL to LOINC.
	 */
	public static final String LOINC_WEBSITE_URL = "https://loinc.org";

	public static final String REGENSTRIEF_INSTITUTE_INC = "Regenstrief Institute, Inc.";
	public static final String LOINC_IEEE_CM_ID = "loinc-to-ieee-11073-10101";
	public static final String LOINC_IEEE_CM_URI = "http://loinc.org/cm/loinc-to-ieee-11073-10101";
	public static final String LOINC_IEEE_CM_NAME = "LOINC/IEEE Device Code Mappings";
	public static final String CM_COPYRIGHT =
			"The LOINC/IEEE Medical Device Code Mapping Table contains content from IEEE (http://ieee.org), copyright © 2017 IEEE.";
	public static final String CM_RSNA_COPYRIGHT =
			"The LOINC/RSNA Radiology Playbook and the LOINC Part File contain content from RadLex® (http://rsna.org/RadLex.aspx), copyright © 2005-2017, The Radiological Society of North America, Inc., available at no cost under the license at http://www.rsna.org/uploadedFiles/RSNA/Content/Informatics/RadLex_License_Agreement_and_Terms_of_Use_V2_Final.pdf.";
	public static final String LOINC_SCT_PART_MAP_ID = "loinc-parts-to-snomed-ct";
	public static final String LOINC_SCT_PART_MAP_URI = "http://loinc.org/cm/loinc-parts-to-snomed-ct";
	public static final String LOINC_TERM_TO_RPID_PART_MAP_ID = "loinc-to-radlex";
	public static final String LOINC_TERM_TO_RPID_PART_MAP_URI = "http://loinc.org/cm/loinc-to-radlex";
	public static final String LOINC_TERM_TO_RPID_PART_MAP_NAME = "LOINC Terms to RadLex RPIDs";
	public static final String LOINC_PART_TO_RID_PART_MAP_ID = "loinc-parts-to-radlex";
	public static final String LOINC_PART_TO_RID_PART_MAP_URI = "http://loinc.org/cm/loinc-parts-to-radlex";
	public static final String LOINC_PART_TO_RID_PART_MAP_NAME = "LOINC Parts to RadLex RIDs";
	public static final String LOINC_SCT_PART_MAP_NAME = "LOINC Part Map to SNOMED CT";
	public static final String LOINC_RXNORM_PART_MAP_ID = "loinc-parts-to-rxnorm";
	public static final String LOINC_RXNORM_PART_MAP_URI = "http://loinc.org/cm/loinc-parts-to-rxnorm";
	public static final String LOINC_RXNORM_PART_MAP_NAME = "LOINC Part Map to RxNORM";
	public static final String LOINC_PUBCHEM_PART_MAP_ID = "loinc-parts-to-pubchem";
	public static final String LOINC_PUBCHEM_PART_MAP_URI = "http://loinc.org/cm/loinc-parts-to-pubchem";
	public static final String LOINC_PUBCHEM_PART_MAP_NAME = "LOINC Part Map to PubChem";
	public static final String CM_SCT_COPYRIGHT =
			"The LOINC Part File, LOINC/SNOMED CT Expression Association and Map Sets File, RELMA database and associated search index files include SNOMED Clinical Terms (SNOMED CT®) which is used by permission of the International Health Terminology Standards Development Organisation (IHTSDO) under license. All rights are reserved. SNOMED CT® was originally created by The College of American Pathologists. “SNOMED” and “SNOMED CT” are registered trademarks of the IHTSDO. Use of SNOMED CT content is subject to the terms and conditions set forth in the SNOMED CT Affiliate License Agreement.  It is the responsibility of those implementing this product to ensure they are appropriately licensed and for more information on the license, including how to register as an Affiliate Licensee, please refer to http://www.snomed.org/snomed-ct/get-snomed-ct or info@snomed.org. Under the terms of the Affiliate License, use of SNOMED CT in countries that are not IHTSDO Members is subject to reporting and fee payment obligations. However, IHTSDO agrees to waive the requirements to report and pay fees for use of SNOMED CT content included in the LOINC Part Mapping and LOINC Term Associations for purposes that support or enable more effective use of LOINC. This material includes content from the US Edition to SNOMED CT, which is developed and maintained by the U.S. National Library of Medicine and is available to authorized UMLS Metathesaurus Licensees from the UTS Downloads site at https://uts.nlm.nih.gov.";
	public static final String RSNA_CODES_VS_ID = "loinc-rsna-radiology-playbook";
	public static final String RSNA_CODES_VS_URI = "http://loinc.org/vs/loinc-rsna-radiology-playbook";
	public static final String RSNA_CODES_VS_NAME = "LOINC/RSNA Radiology Playbook";
	public static final String RID_CS_URI = "http://www.radlex.org";
	/**
	 * About these being the same - Per Dan Vreeman:
	 * We had some discussion about this, and both
	 * RIDs (RadLex clinical terms) and RPIDs (Radlex Playbook Ids)
	 * belong to the same "code system" since they will never collide.
	 * The codesystem uri is "http://www.radlex.org". FYI, that's
	 * now listed on the FHIR page:
	 * https://www.hl7.org/fhir/terminologies-systems.html
	 * -ja
	 */
	public static final String RPID_CS_URI = RID_CS_URI;

	public static final String DOCUMENT_ONTOLOGY_CODES_VS_ID = "loinc-document-ontology";
	public static final String DOCUMENT_ONTOLOGY_CODES_VS_URI = "http://loinc.org/vs/loinc-document-ontology";
	public static final String DOCUMENT_ONTOLOGY_CODES_VS_NAME = "LOINC Document Ontology Codes";
	public static final String ASK_AT_ORDER_ENTRY_PROP_NAME = "AskAtOrderEntry";
	public static final String ASSOCIATED_OBSERVATIONS_PROP_NAME = "AssociatedObservations";
}
