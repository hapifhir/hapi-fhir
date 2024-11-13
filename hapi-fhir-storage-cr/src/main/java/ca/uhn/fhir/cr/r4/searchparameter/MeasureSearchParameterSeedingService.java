/*-
 * #%L
 * HAPI FHIR - Clinical Reasoning
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
package ca.uhn.fhir.cr.r4.searchparameter;

import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import jakarta.annotation.PostConstruct;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.ResourceType;
import org.hl7.fhir.r4.model.SearchParameter;
import org.opencds.cqf.fhir.cr.measure.r4.utils.R4MeasureServiceUtils;

// LUKETODO:  find a better package for this
// LUKETODO:  javadoc
// LUKETODO:  unit test
public class MeasureSearchParameterSeedingService {

	private final DaoRegistry myDaoRegistry;
	private final ISearchParamRegistry mySearchParamRegistry;

	public MeasureSearchParameterSeedingService(
			DaoRegistry theDaoRegistry, ISearchParamRegistry theSearchParamRegistry) {
		myDaoRegistry = theDaoRegistry;
		mySearchParamRegistry = theSearchParamRegistry;

		// LUKETODO:  this happens if we inject this into unit tests
		/*
			Caused by: java.lang.IllegalArgumentException: HAPI-2063: Unknown job definition ID: REINDEX
		at ca.uhn.fhir.batch2.coordinator.JobCoordinatorImpl.lambda$startInstance$1(JobCoordinatorImpl.java:141)
		at java.base/java.util.Optional.orElseThrow(Optional.java:403)
		at ca.uhn.fhir.batch2.coordinator.JobCoordinatorImpl.startInstance(JobCoordinatorImpl.java:140)
			 */
		create();
	}

	// LUKETODO:  figure out how to get THIS @PostConstruct to get called AFTER the one for Batch2JobRegisterer
//	@PostConstruct
//	public void start() {
//	}

	public void create() {
		final IFhirResourceDao<SearchParameter> searchParameterDao =
				unsafeCast(myDaoRegistry.getResourceDao(ResourceType.SearchParameter.name()));

		final SystemRequestDetails systemRequestDetails = new SystemRequestDetails();
		// LUKETODO:  this works from cdr and from DqmR4IT but fails from tests?!?!??!?!?
		/*
		HAPI-2223: HAPI-2063: Unknown job definition ID: REINDEX
		*/
		// LUKETODO:  this fails because RequestDetails.UserData.SkipReindexing is set to FALSE
		searchParameterDao.update(R4MeasureServiceUtils.SUPPLEMENTAL_DATA_SEARCHPARAMETER, systemRequestDetails);
		searchParameterDao.update(createSearchParameterPeriodStart(), systemRequestDetails);
		searchParameterDao.update(createSearchParameterPeriodEnd(), systemRequestDetails);
		mySearchParamRegistry.forceRefresh();
	}

	// LUKETODO:
	/*
		{
		"resourceType": "SearchParameter",
		"id": "measureReport-periodEnd",
		"url": "http://hl7.org/fhir/SearchParameter/measureReport-billablePeriodEnd",
		"name": "measureReport-billablePeriodEnd",
		"status": "active",
		"description": "The measure reports' period end date",
		"code": "periodEnd",
		"base": [
			"MeasureReport"
		],
		"type": "date",
		"expression": "MeasureReport.period.end"
	},
	{
		"resourceType": "SearchParameter",
		"id": "measureReport-periodStart",
		"url": "http://hl7.org/fhir/SearchParameter/measureReport-billablePeriodStart",
		"name": "measureReport-billablePeriodStart",
		"status": "active",
		"description": "The measure reports' period start date",
		"code": "periodStart",
		"base": [
			"MeasureReport"
		],
		"type": "date",
		"expression": "MeasureReport.period.start"
		 */

	private SearchParameter createSearchParameterPeriodStart() {
		return (SearchParameter) new SearchParameter()
				.setStatus(Enumerations.PublicationStatus.ACTIVE)
				.setUrl("http://hl7.org/fhir/SearchParameter/measureReport-billablePeriodStart")
				.setName("measureReport-billablePeriodStart")
				.setDescription("The measure reports' period start date")
				.setCode("periodStart")
				.setType(Enumerations.SearchParamType.DATE)
				.setExpression("MeasureReport.period.start")
				.addBase("MeasureReport")
				.setId("measureReport-periodStart");
	}

	private SearchParameter createSearchParameterPeriodEnd() {
		return (SearchParameter) new SearchParameter()
				.setStatus(Enumerations.PublicationStatus.ACTIVE)
				.setUrl("http://hl7.org/fhir/SearchParameter/measureReport-billablePeriodEnd")
				.setName("measureReport-billablePeriodEnd")
				.setDescription("The measure reports' period end date")
				.setCode("periodEnd")
				.setType(Enumerations.SearchParamType.DATE)
				.setExpression("MeasureReport.period.end")
				.addBase("MeasureReport")
				.setId("measureReport-periodEnd");
	}

	@SuppressWarnings("unchecked")
	private static <T> T unsafeCast(Object theObject) {
		return (T) theObject;
	}
}
