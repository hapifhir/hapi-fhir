/*-
 * #%L
 * HAPI FHIR - Server Framework
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
package ca.uhn.fhir.rest.server.method;

import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import jakarta.annotation.Nullable;

import java.util.Optional;

/**
 * This class is used to capture the details of an operation's ID parameter to be used by {@link OperationMethodBinding}
 */
class OperationIdParamDetails {

	@Nullable
	private final IdParam myIdParam;

	@Nullable
	private final Integer myIdParamIndex;

	public static final OperationIdParamDetails EMPTY = new OperationIdParamDetails(null, null);

	public OperationIdParamDetails(@Nullable IdParam theIdParam, @Nullable Integer theIdParamIndex) {
		myIdParam = theIdParam;
		myIdParamIndex = theIdParamIndex;
	}

	public boolean isFound() {
		return myIdParamIndex != null;
	}

	public boolean setOrReturnPreviousValue(boolean thePreviousValue) {
		return Optional.ofNullable(myIdParam).map(IdParam::optional).orElse(thePreviousValue);
	}

	public Object[] alterMethodParamsIfNeeded(RequestDetails theRequest, Object[] theMethodParams) {
		if (myIdParamIndex == null) {
			// no-op
			return theMethodParams;
		}

		final Object[] clonedMethodParams = theMethodParams.clone();

		clonedMethodParams[myIdParamIndex] = theRequest.getId();

		return clonedMethodParams;
	}
}
