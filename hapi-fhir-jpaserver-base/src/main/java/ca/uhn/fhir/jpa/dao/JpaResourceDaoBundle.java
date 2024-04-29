/*
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
package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Bundle.Entry;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;

public class JpaResourceDaoBundle<T extends IBaseBundle> extends BaseHapiFhirResourceDao<T> {

	@Override
	protected void preProcessResourceForStorage(
			IBaseResource theResource,
			RequestDetails theRequestDetails,
			TransactionDetails theTransactionDetails,
			boolean thePerformIndexing) {
		super.preProcessResourceForStorage(theResource, theRequestDetails, theTransactionDetails, thePerformIndexing);

		if (getContext().getVersion().getVersion() == FhirVersionEnum.DSTU2) {
			for (Entry next : ((Bundle) theResource).getEntry()) {
				next.setFullUrl((String) null);
			}
		}
	}
}
