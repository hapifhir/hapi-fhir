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
package ca.uhn.fhir.jpa.ips.jpa;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.ips.api.ISectionResourceSupplier;
import ca.uhn.fhir.jpa.ips.api.IpsContext;
import ca.uhn.fhir.jpa.ips.api.IpsSectionContext;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.valueset.BundleEntrySearchModeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.ReferenceParam;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Coverage;
import org.thymeleaf.util.Validate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class JpaSectionResourceSupplier implements ISectionResourceSupplier {
	public static final int CHUNK_SIZE = 10;

	private final JpaSectionSearchStrategyCollection mySectionSearchStrategyCollection;
	private final DaoRegistry myDaoRegistry;
	private final FhirContext myFhirContext;

	public JpaSectionResourceSupplier(
			@Nonnull JpaSectionSearchStrategyCollection theSectionSearchStrategyCollection,
			@Nonnull DaoRegistry theDaoRegistry,
			@Nonnull FhirContext theFhirContext) {
		Validate.notNull(theSectionSearchStrategyCollection, "theSectionSearchStrategyCollection must not be null");
		Validate.notNull(theDaoRegistry, "theDaoRegistry must not be null");
		Validate.notNull(theFhirContext, "theFhirContext must not be null");
		mySectionSearchStrategyCollection = theSectionSearchStrategyCollection;
		myDaoRegistry = theDaoRegistry;
		myFhirContext = theFhirContext;
	}

	@Nullable
	@Override
	public <T extends IBaseResource> List<ResourceEntry> fetchResourcesForSection(
			IpsContext theIpsContext, IpsSectionContext<T> theIpsSectionContext, RequestDetails theRequestDetails) {

		IJpaSectionSearchStrategy<T> searchStrategy =
				mySectionSearchStrategyCollection.getSearchStrategy(theIpsSectionContext.getResourceType());

		SearchParameterMap searchParameterMap = new SearchParameterMap();

		String subjectSp = determinePatientCompartmentSearchParameterName(theIpsSectionContext.getResourceType());
		searchParameterMap.add(subjectSp, new ReferenceParam(theIpsContext.getSubjectId()));

		searchStrategy.massageResourceSearch(theIpsSectionContext, searchParameterMap);

		IFhirResourceDao<T> dao = myDaoRegistry.getResourceDao(theIpsSectionContext.getResourceType());
		IBundleProvider searchResult = dao.search(searchParameterMap, theRequestDetails);

		List<ResourceEntry> retVal = null;
		for (int startIndex = 0; ; startIndex += CHUNK_SIZE) {
			int endIndex = startIndex + CHUNK_SIZE;
			List<IBaseResource> resources = searchResult.getResources(startIndex, endIndex);
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
