/*-
 * #%L
 * HAPI FHIR - Clinical Reasoning
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
package ca.uhn.fhir.cr.repo;

import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.HashMap;
import java.util.Map;

/**
 * This class produces partial clones of RequestDetails, the intent being to reuse the context of a
 * RequestDetails object for reentrant calls. It retains header and tenancy information while
 * scrapping everything else.
 */
class RequestDetailsCloner {

	static DetailsBuilder startWith(RequestDetails theDetails) {
		var newDetails = new SystemRequestDetails(theDetails);
		newDetails.setRequestType(RequestTypeEnum.POST);
		newDetails.setOperation(null);
		newDetails.setResource(null);
		newDetails.setParameters(new HashMap<>());
		newDetails.setResourceName(null);
		newDetails.setCompartmentName(null);
		newDetails.setResponse(theDetails.getResponse());

		return new DetailsBuilder(newDetails);
	}

	static class DetailsBuilder {
		private final SystemRequestDetails myDetails;

		DetailsBuilder(SystemRequestDetails theDetails) {
			myDetails = theDetails;
		}

		DetailsBuilder addHeaders(Map<String, String> theHeaders) {
			if (theHeaders != null) {
				for (var entry : theHeaders.entrySet()) {
					myDetails.addHeader(entry.getKey(), entry.getValue());
				}
			}

			return this;
		}

		DetailsBuilder setParameters(IBaseParameters theParameters) {
			IParser parser = myDetails.getServer().getFhirContext().newJsonParser();
			myDetails.setRequestContents(
					parser.encodeResourceToString(theParameters).getBytes());

			return this;
		}

		DetailsBuilder setParameters(Map<String, String[]> theParameters) {
			myDetails.setParameters(theParameters);

			return this;
		}

		DetailsBuilder withRestOperationType(RequestTypeEnum theType) {
			myDetails.setRequestType(theType);

			return this;
		}

		DetailsBuilder setOperation(String theOperation) {
			myDetails.setOperation(theOperation);

			return this;
		}

		DetailsBuilder setResourceType(String theResourceName) {
			myDetails.setResourceName(theResourceName);

			return this;
		}

		DetailsBuilder setId(IIdType theId) {
			myDetails.setId(theId);

			return this;
		}

		SystemRequestDetails create() {
			return myDetails;
		}
	}
}
