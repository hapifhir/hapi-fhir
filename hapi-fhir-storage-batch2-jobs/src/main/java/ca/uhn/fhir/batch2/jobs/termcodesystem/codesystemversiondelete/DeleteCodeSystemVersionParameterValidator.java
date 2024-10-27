/*-
 * #%L
 * hapi-fhir-storage-batch2-jobs
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.batch2.jobs.termcodesystem.codesystemversiondelete;

import ca.uhn.fhir.batch2.api.IJobParametersValidator;
import ca.uhn.fhir.jpa.term.models.TermCodeSystemDeleteVersionJobParameters;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DeleteCodeSystemVersionParameterValidator
		implements IJobParametersValidator<TermCodeSystemDeleteVersionJobParameters> {

	@Nullable
	@Override
	public List<String> validate(
			RequestDetails theRequestDetails, @Nonnull TermCodeSystemDeleteVersionJobParameters theParameters) {
		ArrayList<String> errors = new ArrayList<>();
		long versionPID = theParameters.getCodeSystemVersionPid();

		if (versionPID <= 0) {
			errors.add("Invalid code system version PID " + versionPID);
		}

		return errors;
	}
}
