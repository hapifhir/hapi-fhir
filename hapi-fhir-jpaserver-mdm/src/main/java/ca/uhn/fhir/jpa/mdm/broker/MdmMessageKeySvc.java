package ca.uhn.fhir.jpa.mdm.broker;

/*-
 * #%L
 * HAPI FHIR JPA Server - Master Data Management
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.jpa.subscription.api.ISubscriptionMessageKeySvc;
import ca.uhn.fhir.mdm.model.CanonicalEID;
import ca.uhn.fhir.mdm.util.EIDHelper;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.util.List;

@Service
public class MdmMessageKeySvc implements ISubscriptionMessageKeySvc {
	@Autowired
	private EIDHelper myEIDHelper;

	@Nullable
	@Override
	public String getMessageKeyOrNull(IBaseResource theTargetResource) {
		List<CanonicalEID> eidList = myEIDHelper.getExternalEid(theTargetResource);
		if (eidList.isEmpty()) {
			return null;
		}
		return eidList.get(0).getValue();
	}
}
