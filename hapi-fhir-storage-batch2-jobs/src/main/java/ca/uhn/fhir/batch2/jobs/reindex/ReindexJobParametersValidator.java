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
import ca.uhn.fhir.batch2.jobs.parameters.UrlListValidator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ReindexJobParametersValidator implements IJobParametersValidator<ReindexJobParameters> {
	private final UrlListValidator myUrlListValidator;

	public ReindexJobParametersValidator(UrlListValidator theUrlListValidator) {
		myUrlListValidator = theUrlListValidator;
	}

	@Nullable
	@Override
	public List<String> validate(@NotNull ReindexJobParameters theParameters) {
		return myUrlListValidator.validatePartitionedUrls(theParameters.getPartitionedUrls());
	}
}
