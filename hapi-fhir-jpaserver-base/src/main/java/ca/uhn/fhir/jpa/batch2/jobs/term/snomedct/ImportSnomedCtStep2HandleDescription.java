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
import ca.uhn.fhir.jpa.batch2.jobs.term.base.BaseImportTerminologyFileStep;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyMetadataAttachmentJson;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyFileSetJson;
import ca.uhn.fhir.jpa.batch2.jobs.term.loinc.ImportLoincJobAppCtx;
import jakarta.annotation.Nonnull;
import org.apache.commons.csv.CSVRecord;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @see ImportLoincJobAppCtx#importLoincStep2Concepts()
 */
public class ImportSnomedCtStep2HandleDescription
		extends BaseImportSnomedCtStep<ImportSnomedCtStep2HandleDescription.MyContext> {

	@Nonnull
	@Override
	protected List<LoincFileNameSpecification> getFilesToProcess(
			StepExecutionDetails<ImportSnomedCtJobParameters, ?> theStepExecutionDetails) {
		return List.of(new LoincFileNameSpecification(
				FileHandlingType.TSV_SPLIT_WITH_REPEAT_HEADER_5000_LINE_CHUNKS,
				t -> t.contains("sct2_Description_Full")));
	}

	@Override
	protected ImportSnomedCtStep2HandleDescription.MyContext newContextObject(
			StepExecutionDetails<ImportSnomedCtJobParameters, TerminologyFileSetJson> theStepExecutionDetails) {
		return new MyContext();
	}

	@Override
	protected void handleRecord(
			StepExecutionDetails<ImportSnomedCtJobParameters, TerminologyFileSetJson> theStepExecutionDetails,
			ImportTerminologyMetadataAttachmentJson theJobMetadata,
			ImportSnomedCtJobParameters theJobParameters,
			MyContext theContext,
			CSVRecord theRecord,
			CodeSystem theCodeSystemToPopulate,
			TerminologyFileSetJson theData,
			String theSourceFilename) {

		String id = theRecord.get("id");
		boolean active = "1".equals(theRecord.get("active"));
		if (!active) {
			return;
		}

		String conceptId = theRecord.get("conceptId");
		String term = theRecord.get("term");

		if (theContext.mySeenTerms.add(term)) {
			CodeSystem.ConceptDefinitionComponent concept = getOrAddConcept(theContext, conceptId);
			concept.setDisplay(term);
			concept.addProperty().setCode("id").setValue(new CodeType(id));
		}
	}

	public static class MyContext extends BaseImportTerminologyFileStep.MyBaseContext {

		private final Set<String> mySeenTerms = new HashSet<>();
	}
}
