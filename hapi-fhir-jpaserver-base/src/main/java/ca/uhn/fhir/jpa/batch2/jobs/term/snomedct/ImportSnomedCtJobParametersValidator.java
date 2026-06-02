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

import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyJobParameters;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class ImportSnomedCtJobParametersValidator
		implements ca.uhn.fhir.batch2.api.IJobParametersValidator<ImportTerminologyJobParameters> {
	private static final Pattern VERSION_PATTERN = Pattern.compile("[^ ]+");

	@Nullable
	@Override
	public List<String> validate(RequestDetails theRequestDetails, @Nonnull ImportTerminologyJobParameters theParameters) {
		List<String> retVal = new ArrayList<>();

		if (isBlank(theParameters.getVersionId())) {
			retVal.add("Version ID is required");
		} else if (!VERSION_PATTERN.matcher(theParameters.getVersionId()).matches()) {
			retVal.add("Version ID is invalid: " + theParameters.getVersionId());
		}

		return retVal;
	}
}
