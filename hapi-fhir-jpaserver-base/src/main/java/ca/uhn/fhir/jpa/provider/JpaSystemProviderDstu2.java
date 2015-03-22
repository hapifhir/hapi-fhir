package ca.uhn.fhir.jpa.provider;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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

import javax.servlet.http.HttpServletRequest;

import ca.uhn.fhir.model.dstu2.composite.MetaDt;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Parameters;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.annotation.Transaction;
import ca.uhn.fhir.rest.annotation.TransactionParam;

public class JpaSystemProviderDstu2 extends BaseJpaSystemProvider<Bundle> {

	//@formatter:off
	@Operation(name="$meta", idempotent=true, returnParameters= {
		@OperationParam(name="return", type=MetaDt.class)
	})
	//@formatter:on
	public Parameters operation() {
		Parameters parameters = new Parameters();
		parameters.addParameter().setName("return").setValue(getDao().metaGetOperation());
		return parameters;
	}
	
	@Transaction
	public Bundle transaction(HttpServletRequest theRequest, @TransactionParam Bundle theResources) {
		startRequest(theRequest);
		try {
			return getDao().transaction(theResources);
		} finally {
			endRequest(theRequest);
		}
	}

}
