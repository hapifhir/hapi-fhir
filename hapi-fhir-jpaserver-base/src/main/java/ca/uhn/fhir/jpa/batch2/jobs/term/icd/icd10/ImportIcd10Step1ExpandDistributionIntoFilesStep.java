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
package ca.uhn.fhir.jpa.batch2.jobs.term.icd.icd10;

import ca.uhn.fhir.jpa.batch2.jobs.term.base.BaseExpandDistributionIntoFilesStep;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyJobParameters;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyConstants;
import ca.uhn.fhir.jpa.batch2.jobs.term.icd.ImportIcdJobAppCtx;
import ca.uhn.fhir.jpa.batch2.jobs.term.loinc.ImportLoincJobAppCtx;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.r4.model.CodeSystem;

/**
 * @see ImportIcdJobAppCtx#importIcd10Step1ExpandDistributionIntoFiles()
 */
public class ImportIcd10Step1ExpandDistributionIntoFilesStep
		extends BaseExpandDistributionIntoFilesStep<ImportTerminologyJobParameters, Void> {

	@Override
	protected Void newContextObject() {
		return null;
	}

	@Override
	protected boolean isIndividualFileAttachmentsSupported() {
		return true;
	}

	@Nonnull
	@Override
	protected String getDistributionFileName() {
		return TerminologyConstants.FILENAME_ICD10_DISTRIBUTION_FILE;
	}

	@Nonnull
	@Override
	protected String getCodeSystemIdRoot() {
		return "icd-10";
	}

	@Override
	protected void massageCodeSystem(CodeSystem theCodeSystem, Void theContext) {
		super.massageCodeSystem(theCodeSystem, theContext);

		theCodeSystem.setName("ICD-10");
	}
}
