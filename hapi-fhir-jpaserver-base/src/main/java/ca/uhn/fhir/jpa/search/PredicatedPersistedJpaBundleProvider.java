/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.jpa.search;

import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.method.ResponsePage;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.List;
import java.util.function.Predicate;

/**
 * PersistedJpaBundleProvider accepting a resource filtering predicate
 */
public class PredicatedPersistedJpaBundleProvider extends PersistedJpaBundleProvider implements IBundleProvider {

	private final Predicate<? super IBaseResource> myPredicate;

	public PredicatedPersistedJpaBundleProvider(
			RequestDetails theRequest, Search theSearch, Predicate<? super IBaseResource> thePredicate) {
		super(theRequest, theSearch);
		myPredicate = thePredicate;
	}

	@Override
	public List<IBaseResource> getResources(
			int theFromIndex, int theToIndex, @Nonnull ResponsePage.ResponsePageBuilder theResponsePageBuilder) {

		List<IBaseResource> allResources = super.getResources(theFromIndex, theToIndex, theResponsePageBuilder);

		return allResources.stream().filter(myPredicate).toList();
	}
}
