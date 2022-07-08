package ca.uhn.hapi.fhir.docs.interceptor;

/*-
 * #%L
 * HAPI FHIR - Docs
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

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import org.hl7.fhir.instance.model.api.IBaseResource;

// START SNIPPET: TagTrimmingInterceptor
/**
 * This is a simple interceptor for the JPA server that trims all tags, profiles, and security labels from
 * resources before they are saved.
 */
@Interceptor
public class TagTrimmingInterceptor {

	/** Handle creates */
	@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED)
	public void insert(IBaseResource theResource) {
		theResource.getMeta().getTag().clear();
		theResource.getMeta().getProfile().clear();
		theResource.getMeta().getSecurity().clear();
	}

	/** Handle updates */
	@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED)
	public void update(IBaseResource theOldResource, IBaseResource theResource) {
		theResource.getMeta().getTag().clear();
		theResource.getMeta().getProfile().clear();
		theResource.getMeta().getSecurity().clear();
	}

}
// END SNIPPET: TagTrimmingInterceptor
