package ca.uhn.fhir.jpa.dao;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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

import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.StringType;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public interface IFhirResourceDaoConceptMap<T extends IBaseResource> extends IFhirResourceDao<T> {
	TranslationResult translate(IPrimitiveType<String> theSourceCodeSystem, IPrimitiveType<String> theTargetCodeSystem, IPrimitiveType<String> theSourceCode, RequestDetails theRequestDetails);

	class TranslationResult {
		private boolean myIsMatched;
		private String myMessage;

		public TranslationResult() {
			super();
		}

		public boolean isMatched() {
			return myIsMatched;
		}

		public void setMatched(boolean matched) {
			myIsMatched = matched;
		}

		public String getMessage() {
			return myMessage;
		}

		public void setMessage(String message) {
			myMessage = message;
		}

		public Parameters toParameters() {
			Parameters retVal = new Parameters();

			retVal.addParameter().setName("result").setValue(new BooleanType(isMatched()));
			if (isNotBlank(getMessage())) {
				retVal.addParameter().setName("message").setValue(new StringType(getMessage()));
			}

			return retVal;
		}
	}
}
