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

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.databind.ObjectMapper;

public interface ICdsMethod {
	Object invoke(ObjectMapper theObjectMapper, IModelJson theJson, String theServiceId);
}
