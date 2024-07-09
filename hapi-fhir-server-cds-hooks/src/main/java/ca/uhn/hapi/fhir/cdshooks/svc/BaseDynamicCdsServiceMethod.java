/*-
 * #%L
 * HAPI FHIR - CDS Hooks
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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

import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.hapi.fhir.cdshooks.api.ICdsMethod;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceRequestJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceResponseJson;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nonnull;

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
