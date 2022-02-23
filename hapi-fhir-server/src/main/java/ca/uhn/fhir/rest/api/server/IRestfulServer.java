package ca.uhn.fhir.rest.api.server;

import ca.uhn.fhir.context.api.BundleInclusionRule;
import ca.uhn.fhir.rest.api.PreferReturnEnum;
import ca.uhn.fhir.rest.server.IPagingProvider;
import ca.uhn.fhir.rest.server.IRestfulServerDefaults;

/*
 * #%L
 * HAPI FHIR - Server Framework
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
public interface IRestfulServer<T extends RequestDetails> extends IRestfulServerDefaults {

	@Override
	IPagingProvider getPagingProvider();

	BundleInclusionRule getBundleInclusionRule();

	PreferReturnEnum getDefaultPreferReturn();

	default boolean canStoreSearchResults() {
		return getPagingProvider() != null && getPagingProvider().canStoreSearchResults();
	}
}
