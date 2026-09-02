/*-
 * #%L
 * HAPI FHIR JPA Server - Master Data Management
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
package ca.uhn.fhir.jpa.mdm.broker;

import ca.uhn.fhir.jpa.subscription.api.ISubscriptionMessageKeySvc;
import ca.uhn.fhir.mdm.model.CanonicalEID;
import ca.uhn.fhir.mdm.util.EIDHelper;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.stereotype.Service;

@Service
public class MdmMessageKeySvc implements ISubscriptionMessageKeySvc {
	private final EIDHelper myEIDHelper;

	public MdmMessageKeySvc(EIDHelper theEidHelper) {
		myEIDHelper = theEidHelper;
	}

	/**
	 * The broker routes messages sharing a key to the same consumer, which is what keeps changes to one
	 * patient in order when several MDM consumers are running. The key must therefore not depend on the
	 * order identifiers happen to appear in the payload. Where a resource type is identified by several
	 * EID systems, the primary one - the first configured for that resource type - decides the key.
	 */
	@Nullable
	@Override
	public String getMessageKeyOrNull(IBaseResource theTargetResource) {
		return myEIDHelper
				.getPrimaryExternalEid(theTargetResource)
				.map(CanonicalEID::getValue)
				.orElse(null);
	}
}
