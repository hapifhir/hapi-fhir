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
package ca.uhn.fhir.jpa.batch2.jobs.term.snomedct;

import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.BaseImportTerminologyFileCsvStep;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyJobParameters;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyMetadataAttachmentJson;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyFileSetJson;
import jakarta.annotation.Nonnull;
import org.apache.commons.csv.CSVRecord;
import org.hl7.fhir.r4.model.CodeSystem;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * @see ImportSnomedCtJobAppCtx#importSnomedCtStep3Relationship()
 */
public class ImportSnomedCtStep3HandleRelationship
		extends BaseImportTerminologyFileCsvStep<
				ImportTerminologyJobParameters, ImportSnomedCtStep3HandleRelationship.MyBaseContext> {

	private static final String COL_ACTIVE = "active";
	private static final String COL_ACTIVE_VAL_1 = "1";
	private static final String COL_TYPE_ID = "typeId";
	private static final String COL_DESTINATION_ID = "destinationId";
	private static final String COL_SOURCE_ID = "sourceId";

	/**
	 * The concept with ID "116680003" denotes the concept "Is a (attribute)".
	 * https://docs.snomed.org/snomed-international-documents/snomed-ct-glossary/r/relationship-type
	 */
	private static final String COL_TYPE_ID_VAL_IS_A = "116680003";

	@Nonnull
	@Override
	public List<LoincFileNameSpecification> getFilesToProcess(
			StepExecutionDetails<ImportTerminologyJobParameters, ?> theStepExecutionDetails) {
		return List.of(new LoincFileNameSpecification(
				FileHandlingType.TSV_SPLIT_WITH_REPEAT_HEADER_5000_LINE_CHUNKS,
				t -> t.contains("sct2_Relationship_Full")));
	}

	@Override
	protected MyBaseContext newContextObject(
			StepExecutionDetails<ImportTerminologyJobParameters, TerminologyFileSetJson> theStepExecutionDetails) {
		return new MyBaseContext();
	}

	@Override
	protected void handleRecord(
			StepExecutionDetails<ImportTerminologyJobParameters, TerminologyFileSetJson> theStepExecutionDetails,
			ImportTerminologyMetadataAttachmentJson theJobMetadata,
			ImportTerminologyJobParameters theJobParameters,
			MyBaseContext theContext,
			CSVRecord theRecord,
			CodeSystem theCodeSystemToPopulate,
			TerminologyFileSetJson theData,
			String theSourceFilename) {

		String sourceId = theRecord.get(COL_SOURCE_ID);
		String destinationId = theRecord.get(COL_DESTINATION_ID);
		String typeId = theRecord.get(COL_TYPE_ID);
		boolean active = COL_ACTIVE_VAL_1.equals(theRecord.get(COL_ACTIVE));
		if (active) {

			if (isNotBlank(sourceId)
					&& isNotBlank(destinationId)
					&& COL_TYPE_ID_VAL_IS_A.equals(typeId)
					&& !sourceId.equals(destinationId)) {

				// In the RF2 relationships file:
				// Source = child, destination = parent

				super.getOrAddParentChildHierarchy(theContext, destinationId, sourceId);
			}
		}
	}
}
