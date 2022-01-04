package ca.uhn.test.concurrency;

/*-
 * #%L
 * HAPI FHIR Test Utilities
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

import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.function.Function;

public class FhirObjectPrinter implements Function<Object, String> {
	@Override
	public String apply(Object object) {
		if (object instanceof IBaseResource) {
			IBaseResource resource = (IBaseResource) object;
			return resource.getClass().getSimpleName() + " { " + resource.getIdElement().getValue() + " }";
		} else if (object != null) {
			return object.toString();
		} else {
			return "null";
		}
	}
}
