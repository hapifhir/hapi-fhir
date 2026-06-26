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

	/** Non-instantiable */
	private TerminologyConstants() {}

	public static final String IMGTHLA_URI = "http://www.ebi.ac.uk/ipd/imgt/hla";
	public static final String LOINC_URI = "http://loinc.org";
	public static final String SCT_URI = "http://snomed.info/sct";
	public static final String ICD10_URI = "http://hl7.org/fhir/sid/icd-10";
	public static final String ICD10CM_URI = "http://hl7.org/fhir/sid/icd-10-cm";
	public static final String IEEE_11073_10101_URI = "urn:iso:std:iso:11073:10101";
	public static final String STEP_ID_FINALIZE_IMPORT = "finalize-import";
	public static final String STEP_ID_CHUNK_CONCEPTS_FOR_CLOSURE_GENERATION = "chunk-concepts-for-closure-generation";
	public static final String STEP_ID_GENERATE_CONCEPT_CLOSURES = "generate-concept-closures";

	/// Fixed step weight for the closure generation step. We assign this step 30% of
	/// the total weight for two reasons:
	/// * We don't actually assign any work chunks to this step until the previous step,
	///   so unless we assign a minimum weight, our progress goes down when we get to
	///   the previous step.
	/// * The work chunks go faster for this step then they do for earlier steps. Assigning
	///   30% of the total is a good-enough rough approximation.
	public static final double STEP_WEIGHT_GENERATE_CONCEPT_CLOSURES = 0.3;

	/// Fixed step weight for the "finalize" step at the end of an import terminology
	/// job. This step is very fast so we give it a minimal weight.
	public static final double STEP_WEIGHT_FINALIZE = 0.01;

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

	public static final String EXTENSION_SOURCE_CONCEPT_DIRECT_PARENT_PIDS = "http://hapifhir.io/fhir/StructureDefinition/valueset-concept-direct-parent-pids";
}
