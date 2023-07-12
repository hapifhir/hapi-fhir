/*-
 * #%L
 * HAPI FHIR - CDS Hooks
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
package ca.uhn.hapi.fhir.cdshooks.svc;

import ca.uhn.hapi.fhir.cdshooks.api.ICdsServiceMethod;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceRequestJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceResponseJson;

import java.util.function.Function;

public class CdsDynamicPrefetchableServiceMethod extends BaseDynamicCdsServiceMethod implements ICdsServiceMethod {
	private final CdsServiceJson myCdsServiceJson;
	private final boolean myAllowAutoFhirClientPrefetch;

	public CdsDynamicPrefetchableServiceMethod(
			CdsServiceJson theCdsServiceJson,
			Function<CdsServiceRequestJson, CdsServiceResponseJson> theFunction,
			boolean theAllowAutoFhirClientPrefetch) {
		super(theFunction);
		myAllowAutoFhirClientPrefetch = theAllowAutoFhirClientPrefetch;
		myCdsServiceJson = theCdsServiceJson;
	}

	@Override
	public CdsServiceJson getCdsServiceJson() {
		return myCdsServiceJson;
	}

	@Override
	public boolean isAllowAutoFhirClientPrefetch() {
		return myAllowAutoFhirClientPrefetch;
	}
}
