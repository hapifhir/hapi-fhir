package ca.uhn.fhir.android;

/*-
 * #%L
 * HAPI FHIR - Android
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.okhttp.client.OkHttpRestfulClientFactory;

/**
 * This class exists in order to ensure that 
 */
public class AndroidMarker {

	public static void configureContext(FhirContext theContext) {
		theContext.setRestfulClientFactory(new OkHttpRestfulClientFactory(theContext));
	}
	
	
}
