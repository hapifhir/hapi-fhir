/*-
 * #%L
 * Smile CDR - CDR
 * %%
 * Copyright (C) 2016 - 2023 Smile CDR, Inc.
 * %%
 * All rights reserved.
 * #L%
 */
package ca.uhn.hapi.fhir.cdshooks.api;

import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceJson;

public interface ICdsServiceMethod extends ICdsMethod {
	CdsServiceJson getCdsServiceJson();

	boolean isAllowAutoFhirClientPrefetch();
}
