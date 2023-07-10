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

import java.lang.reflect.Method;

public class CdsServiceMethod extends BaseCdsMethod implements ICdsServiceMethod {
	private final CdsServiceJson myCdsServiceJson;
	private final boolean myAllowAutoFhirClientPrefetch;

	public CdsServiceMethod(CdsServiceJson theCdsServiceJson, Object theServiceBean, Method theMethod, boolean theAllowAutoFhirClientPrefetch) {
		super(theServiceBean, theMethod);
		myCdsServiceJson = theCdsServiceJson;
		myAllowAutoFhirClientPrefetch = theAllowAutoFhirClientPrefetch;
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
