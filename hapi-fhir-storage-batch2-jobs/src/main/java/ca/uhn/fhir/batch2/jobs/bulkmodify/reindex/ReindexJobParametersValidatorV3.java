/*-
 * #%L
 * HAPI-FHIR Storage Batch2 Jobs
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.batch2.jobs.bulkmodify.reindex;

import ca.uhn.fhir.batch2.api.IJobParametersValidator;
import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.base.BaseBulkModifyJobParametersValidator;
import ca.uhn.fhir.batch2.jobs.parameters.IUrlListValidator;
import ca.uhn.fhir.batch2.jobs.reindex.ReindexJobParameters;
import ca.uhn.fhir.jpa.api.IDaoRegistry;
import ca.uhn.fhir.jpa.api.dao.ReindexParameters;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ReindexJobParametersValidatorV3 extends BaseBulkModifyJobParametersValidator<ReindexJobParameters> implements IJobParametersValidator<ReindexJobParameters> {

	/**
	 * Constructor
	 */
	public ReindexJobParametersValidatorV3(IDaoRegistry theDaoRegistry, IUrlListValidator theUrlListValidator) {
		super(theDaoRegistry, theUrlListValidator);
	}

	@Override
	protected void validateJobSpecificParameters(ReindexJobParameters theParameters, List<String> theIssueListToPopulate) {
		if (theParameters.getCorrectCurrentVersion() == ReindexParameters.CorrectCurrentVersionModeEnum.ALL) {
			if (theParameters.getOptimisticLock()) {
				theIssueListToPopulate.add("Optimistic locking cannot be enabled when correcting current versions");
			}
		}

		if (theParameters.isDryRun()) {
			theIssueListToPopulate.add("Dry-run mode is not yet supported for reindexing");
		}
	}

	@Override
	protected boolean isEmptyUrlListAllowed() {
		return true;
	}

}
