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
package ca.uhn.fhir.jpa.batch2.jobs.term.base;

public class TerminologyConstants {

	public static final String IMGTHLA_URI = "http://www.ebi.ac.uk/ipd/imgt/hla";
	public static final String LOINC_URI = "http://loinc.org";
	public static final String SCT_URI = "http://snomed.info/sct";
	public static final String ICD10_URI = "http://hl7.org/fhir/sid/icd-10";
	public static final String ICD10CM_URI = "http://hl7.org/fhir/sid/icd-10-cm";
	public static final String IEEE_11073_10101_URI = "urn:iso:std:iso:11073:10101";

	/** Non-instantiable */
	private TerminologyConstants() {}

	public static final String FILENAME_LOINC_UPLOAD_PROPERTIES_FILE = "loincupload.properties";
	public static final String FILENAME_LOINC_DISTRIBUTION_FILE = "loinc.zip";
	public static final String FILENAME_SNOMED_CT_DISTRIBUTION_FILE = "snomedct.zip";
	public static final String FILENAME_ICD10_DISTRIBUTION_FILE = "icd10.zip";
	public static final String FILENAME_ICD10CM_DISTRIBUTION_FILE = "icd10cm.zip";

	public static final String FILENAME_CUSTOM_DISTRIBUTION_FILE = "custom.zip";

	/**
	 * Used by the
	 * {@link ca.uhn.fhir.jpa.batch2.jobs.term.custom.ImportCustomTerminologyJobAppCtx Import Custom Terminology}
	 * job
	 */
	public static final String CUSTOM_CODESYSTEM_JSON = "codesystem.json";
	/**
	 * Used by the
	 * {@link ca.uhn.fhir.jpa.batch2.jobs.term.custom.ImportCustomTerminologyJobAppCtx Import Custom Terminology}
	 * job
	 */
	public static final String CUSTOM_CODESYSTEM_XML = "codesystem.xml";
	/**
	 * Used by the
	 * {@link ca.uhn.fhir.jpa.batch2.jobs.term.custom.ImportCustomTerminologyJobAppCtx Import Custom Terminology}
	 * job
	 */
	public static final String CUSTOM_CONCEPTS_FILE = "concepts.csv";
	/**
	 * Used by the
	 * {@link ca.uhn.fhir.jpa.batch2.jobs.term.custom.ImportCustomTerminologyJobAppCtx Import Custom Terminology}
	 * job
	 */
	public static final String CUSTOM_HIERARCHY_FILE = "hierarchy.csv";
	/**
	 * Used by the
	 * {@link ca.uhn.fhir.jpa.batch2.jobs.term.custom.ImportCustomTerminologyJobAppCtx Import Custom Terminology}
	 * job
	 */
	public static final String CUSTOM_PROPERTIES_FILE = "properties.csv";
}
