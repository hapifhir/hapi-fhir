/*-
 * #%L
 * hapi-fhir-storage-batch2-jobs
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
package ca.uhn.fhir.batch2.jobs.reindex.v1;

import ca.uhn.fhir.batch2.api.IJobParametersValidator;
import ca.uhn.fhir.batch2.jobs.parameters.IUrlListValidator;
import ca.uhn.fhir.batch2.jobs.reindex.ReindexJobParameters;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

@Deprecated(forRemoval = true, since = "7.6.0")
public class ReindexJobParametersValidatorV1 implements IJobParametersValidator<ReindexJobParameters> {

	private final IUrlListValidator myUrlListValidator;

	public ReindexJobParametersValidatorV1(IUrlListValidator theUrlListValidator) {
		myUrlListValidator = theUrlListValidator;
	}

	@Nullable
	@Override
	public List<String> validate(RequestDetails theRequestDetails, @Nonnull ReindexJobParameters theParameters) {
		List<String> errors = myUrlListValidator.validateUrls(theParameters.getUrls());

		if (errors == null || errors.isEmpty()) {
			// only check if there's no other errors (new list to fix immutable issues)
			errors = new ArrayList<>();
			for (String url : theParameters.getUrls()) {
				if (url.contains(" ") || url.contains("\n") || url.contains("\t")) {
					errors.add("Invalid URL. URL cannot contain spaces : " + url);
				}
			}
		}
		return errors;
	}
}
