package ca.uhn.fhir.empi.util;

/*-
 * #%L
 * HAPI FHIR - Enterprise Master Patient Index
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.empi.api.Constants;
import ca.uhn.fhir.empi.api.IEmpiProperties;
import com.google.common.annotations.VisibleForTesting;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public final class EIDHelper {
	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private IEmpiProperties myEmpiConfig;

	@VisibleForTesting
	EIDHelper(FhirContext theFhirContext, IEmpiProperties theEmpiConfig) {
		myFhirContext = theFhirContext;
		myEmpiConfig = theEmpiConfig;
	}

	public CanonicalEID createHapiEid() {
		return new CanonicalEID(
			Constants.HAPI_ENTERPRISE_IDENTIFIER_SYSTEM,
			UUID.randomUUID().toString(),
			null
		);
	}

	public Optional<CanonicalEID> getExternalEid(IBaseResource theResource) {
		return CanonicalEID.extractFromResource(myFhirContext, myEmpiConfig.getEmpiRules().getEnterpriseEIDSystem(), theResource);
	}

	public Optional<CanonicalEID> getHapiEid(IBaseResource theResource) {
		return CanonicalEID.extractFromResource(myFhirContext, Constants.HAPI_ENTERPRISE_IDENTIFIER_SYSTEM, theResource);
	}

    boolean eidsMatch(CanonicalEID theFirstEid, CanonicalEID theSecondEid) {
        return Objects.equals(theFirstEid.getValue(), theSecondEid.getValue()) && Objects.equals(theFirstEid.getSystem(), theSecondEid.getSystem());
    }


}
