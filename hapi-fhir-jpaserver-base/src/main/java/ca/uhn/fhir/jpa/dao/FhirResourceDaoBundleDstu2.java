package ca.uhn.fhir.jpa.dao;

/*
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

import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Bundle.Entry;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;

import java.util.Set;

import static org.apache.commons.lang3.StringUtils.defaultString;

public class FhirResourceDaoBundleDstu2 extends FhirResourceDaoDstu2<Bundle> {

	@Override
	protected void preProcessResourceForStorage(Bundle theResource) {
		super.preProcessResourceForStorage(theResource);

		Set<String> allowedBundleTypes = getConfig().getBundleTypesAllowedForStorage();
		if (!allowedBundleTypes.contains(defaultString(theResource.getType()))) {
			String message = "Unable to store a Bundle resource on this server with a Bundle.type value of: " + (theResource.getType() != null ? theResource.getType() : "(missing)");
			throw new UnprocessableEntityException(message);
		}

		for (Entry next : theResource.getEntry()) {
			next.setFullUrl((String) null);
		}
	}


}
