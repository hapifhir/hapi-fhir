package ca.uhn.fhir.jpa.provider;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2016 University Health Network
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

import java.util.List;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu2.composite.MetaDt;
import ca.uhn.fhir.rest.annotation.Transaction;
import ca.uhn.fhir.rest.annotation.TransactionParam;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;

public class JpaSystemProviderDstu1 extends BaseJpaSystemProvider<List<IResource>, MetaDt> {

	@Transaction
	public List<IResource> transaction(RequestDetails theRequestDetails, @TransactionParam List<IResource> theResources) {
		startRequest(((ServletRequestDetails) theRequestDetails).getServletRequest());
		try {
			return getDao().transaction(theRequestDetails, theResources);
		} finally {
			endRequest(((ServletRequestDetails) theRequestDetails).getServletRequest());
		}
	}

}
