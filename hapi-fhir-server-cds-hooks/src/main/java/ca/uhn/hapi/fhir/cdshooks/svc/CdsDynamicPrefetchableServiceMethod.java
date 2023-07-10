/*-
 * #%L
 * Smile CDR - CDR
 * %%
 * Copyright (C) 2016 - 2023 Smile CDR, Inc.
 * %%
 * All rights reserved.
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
	public CdsDynamicPrefetchableServiceMethod(CdsServiceJson theCdsServiceJson, Function<CdsServiceRequestJson, CdsServiceResponseJson> theFunction, boolean theAllowAutoFhirClientPrefetch) {
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
