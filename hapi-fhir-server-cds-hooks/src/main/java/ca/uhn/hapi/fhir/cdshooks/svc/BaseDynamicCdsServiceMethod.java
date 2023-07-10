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

import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.hapi.fhir.cdshooks.api.ICdsMethod;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceRequestJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceResponseJson;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.annotation.Nonnull;
import java.util.function.Function;

abstract class BaseDynamicCdsServiceMethod implements ICdsMethod {
	private final Function<CdsServiceRequestJson, CdsServiceResponseJson> myFunction;

	BaseDynamicCdsServiceMethod(Function<CdsServiceRequestJson, CdsServiceResponseJson> theFunction) {
		myFunction = theFunction;
	}

	@Override
	public Object invoke(ObjectMapper theObjectMapper, IModelJson theJson, String theServiceId) {
		return myFunction.apply((CdsServiceRequestJson) theJson);
	}

	@Nonnull
	public Function<CdsServiceRequestJson, CdsServiceResponseJson> getFunction() {
		return myFunction;
	}
}
