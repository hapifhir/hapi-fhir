package ca.uhn.fhir.jpa.dao.r4b;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoSubscription;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirResourceDao;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4b.model.Subscription;

public class FhirResourceDaoSubscriptionR4B extends BaseHapiFhirResourceDao<Subscription> implements IFhirResourceDaoSubscription<Subscription> {

	@Override
	public Long getSubscriptionTablePidForSubscriptionResource(IIdType theId, RequestDetails theRequest, TransactionDetails theTransactionDetails) {
		throw new UnsupportedOperationException(Msg.code(2150));
	}

}
