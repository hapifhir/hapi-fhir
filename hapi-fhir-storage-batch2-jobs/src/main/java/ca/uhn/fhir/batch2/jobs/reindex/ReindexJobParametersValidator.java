package ca.uhn.fhir.batch2.jobs.reindex;

/*-
 * #%L
 * hapi-fhir-storage-batch2-jobs
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

import ca.uhn.fhir.batch2.api.IJobParametersValidator;
import ca.uhn.fhir.jpa.api.svc.IResourceReindexSvc;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class ReindexJobParametersValidator implements IJobParametersValidator<ReindexJobParameters> {

	@Autowired
	private IResourceReindexSvc myResourceReindexSvc;

	@Nullable
	@Override
	public List<String> validate(@Nonnull ReindexJobParameters theParameters) {
		if (theParameters.getUrl().isEmpty()) {
			if (!myResourceReindexSvc.isAllResourceTypeSupported()) {
				return Collections.singletonList("At least one type-specific search URL must be provided for " + ProviderConstants.OPERATION_REINDEX + " on this server");
			}
		}

		return Collections.emptyList();
	}

}
