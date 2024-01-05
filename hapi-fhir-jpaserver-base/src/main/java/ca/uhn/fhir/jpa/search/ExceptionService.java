/*-
 * #%L
 * HAPI FHIR JPA Server
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
package ca.uhn.fhir.jpa.search;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.method.PageMethodBinding;
import jakarta.annotation.Nonnull;

public class ExceptionService {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchCoordinatorSvcImpl.class);

	private final FhirContext myContext;

	public ExceptionService(FhirContext theContext) {
		myContext = theContext;
	}

	@Nonnull
	public ResourceGoneException newUnknownSearchException(String theUuid) {
		ourLog.trace("Client requested unknown paging ID[{}]", theUuid);
		String msg = myContext.getLocalizer().getMessage(PageMethodBinding.class, "unknownSearchId", theUuid);
		return new ResourceGoneException(msg);
	}
}
