package ca.uhn.fhir.jpa.provider;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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

import ca.uhn.fhir.rest.annotation.At;
import ca.uhn.fhir.rest.annotation.History;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Since;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.DateRangeParam;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IIdType;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public interface IHistory<T extends IAnyResource> extends IExtendedResourceProvider<T> {

	@History
	default IBundleProvider getHistoryForResourceInstance(
		HttpServletRequest theRequest,
		@IdParam IIdType theId,
		@Since Date theSince,
		@At DateRangeParam theAt,
		RequestDetails theRequestDetails) {

		startRequest(theRequest);
		try {
			DateRangeParam sinceOrAt = processSinceOrAt(theSince, theAt);
			return getDao().history(theId, sinceOrAt.getLowerBoundAsInstant(), sinceOrAt.getUpperBoundAsInstant(), theRequestDetails);
		} finally {
			endRequest(theRequest);
		}
	}

	DateRangeParam processSinceOrAt(Date theSince, DateRangeParam theAt);
}
