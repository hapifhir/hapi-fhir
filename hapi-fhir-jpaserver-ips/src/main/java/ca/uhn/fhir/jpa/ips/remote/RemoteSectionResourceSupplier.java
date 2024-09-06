/*-
 * #%L
 * HAPI FHIR JPA Server - International Patient Summary (IPS)
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
package ca.uhn.fhir.jpa.ips.remote;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.ips.api.ISectionResourceSupplier;
import ca.uhn.fhir.jpa.ips.api.IpsContext;
import ca.uhn.fhir.jpa.ips.api.IpsSectionContext;
import ca.uhn.fhir.jpa.ips.strategy.section.ISectionSearchStrategy;
import ca.uhn.fhir.jpa.ips.strategy.section.SectionSearchStrategyCollection;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.valueset.BundleEntrySearchModeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.param.ReferenceParam;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Coverage;
import org.hl7.fhir.r4.model.Observation;
import org.thymeleaf.util.Validate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RemoteSectionResourceSupplier implements ISectionResourceSupplier {
	public static final int CHUNK_SIZE = 10;

	private final SectionSearchStrategyCollection mySectionSearchStrategyCollection;

	private final IGenericClient myClient;
	private final FhirContext myFhirContext;

	public RemoteSectionResourceSupplier(
			@Nonnull SectionSearchStrategyCollection theSectionSearchStrategyCollection,
			@Nonnull IGenericClient theClient,
			@Nonnull FhirContext theFhirContext) {
		Validate.notNull(theSectionSearchStrategyCollection, "theSectionSearchStrategyCollection must not be null");
		Validate.notNull(theClient, "theClient must not be null");
		Validate.notNull(theFhirContext, "theFhirContext must not be null");
		mySectionSearchStrategyCollection = theSectionSearchStrategyCollection;
		myClient = theClient;
		myFhirContext = theFhirContext;
	}

	@Nullable
	@Override
	public <T extends IBaseResource> List<ResourceEntry> fetchResourcesForSection(
			IpsContext theIpsContext, IpsSectionContext<T> theIpsSectionContext, RequestDetails theRequestDetails) {

		ISectionSearchStrategy<T> searchStrategy =
				mySectionSearchStrategyCollection.getSearchStrategy(theIpsSectionContext.getResourceType());

		SearchParameterMap searchParameterMap = new SearchParameterMap();
		searchParameterMap.add(
				determinePatientCompartmentSearchParameterName(theIpsSectionContext.getResourceType()),
				new ReferenceParam(theIpsContext.getSubjectId()));
		searchParameterMap.setCount(CHUNK_SIZE);

		searchStrategy.massageResourceSearch(theIpsSectionContext, searchParameterMap);

		// Need to search with pagination to be able to retrieve all resources
		List<ResourceEntry> retVal = null;
		for (int startIndex = 0; ; startIndex += CHUNK_SIZE) {
			searchParameterMap.setOffset(startIndex);

			List<IBaseResource> resources = myClient
					.search()
					.byUrl(theIpsSectionContext.getResourceType().getSimpleName()
							+ searchParameterMap.toNormalizedQueryString(myFhirContext))
					.returnBundle(Bundle.class)
					.execute()
					.getEntry()
					.stream()
					.map(Bundle.BundleEntryComponent::getResource)
					.collect(Collectors.toList());

			if (resources.isEmpty()) {
				break;
			}

			for (IBaseResource next : resources) {
				if (!next.getClass().isAssignableFrom(theIpsSectionContext.getResourceType())
						|| searchStrategy.shouldInclude(theIpsSectionContext, (T) next)) {
					if (retVal == null) {
						retVal = new ArrayList<>();
					}
					InclusionTypeEnum inclusionType =
							ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.get(next) == BundleEntrySearchModeEnum.INCLUDE
									? InclusionTypeEnum.SECONDARY_RESOURCE
									: InclusionTypeEnum.PRIMARY_RESOURCE;
					retVal.add(new ResourceEntry(next, inclusionType));
				}
			}
		}

		return retVal;
	}

	private String determinePatientCompartmentSearchParameterName(Class<? extends IBaseResource> theResourceType) {
		RuntimeResourceDefinition resourceDef = myFhirContext.getResourceDefinition(theResourceType);
		Set<String> searchParams = resourceDef.getSearchParamsForCompartmentName("Patient").stream()
				.map(RuntimeSearchParam::getName)
				.collect(Collectors.toSet());
		// A few we prefer
		if (searchParams.contains(Observation.SP_PATIENT)) {
			return Observation.SP_PATIENT;
		}
		if (searchParams.contains(Observation.SP_SUBJECT)) {
			return Observation.SP_SUBJECT;
		}
		if (searchParams.contains(Coverage.SP_BENEFICIARY)) {
			return Observation.SP_SUBJECT;
		}
		return searchParams.iterator().next();
	}
}
