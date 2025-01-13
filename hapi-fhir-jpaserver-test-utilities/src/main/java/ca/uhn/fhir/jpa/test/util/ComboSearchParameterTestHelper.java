/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.test.util;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.util.HapiExtensions;
import ca.uhn.hapi.converters.canonical.VersionCanonicalizer;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.BooleanType;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.SearchParameter;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ComboSearchParameterTestHelper {


	private final IFhirResourceDao mySearchParameterDao;
	private final VersionCanonicalizer myVersionCanonicalizer;
	private final ISearchParamRegistry mySearchParamRegistry;

	public ComboSearchParameterTestHelper(IFhirResourceDao<?> theSearchParameterDao, ISearchParamRegistry theSearchParamRegistry) {
		mySearchParameterDao = theSearchParameterDao;
		mySearchParamRegistry = theSearchParamRegistry;

		FhirContext context = mySearchParameterDao.getContext();
		myVersionCanonicalizer = new VersionCanonicalizer(context);

		assertEquals("SearchParameter", context.getResourceType(mySearchParameterDao.getResourceType()));
	}


	public void createBirthdateAndGenderSps(boolean theUnique, ISearchParamCustomizer... theSearchParamCustomizer) {
		SearchParameter sp = new SearchParameter();
		sp.setId("SearchParameter/patient-gender");
		sp.setType(Enumerations.SearchParamType.TOKEN);
		sp.setCode("gender");
		sp.setExpression("Patient.gender");
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.addBase(Enumerations.VersionIndependentResourceTypesAll.PATIENT);
		mySearchParameterDao.update(fromCanonoical(sp), new SystemRequestDetails());

		sp = new SearchParameter();
		sp.setId("SearchParameter/patient-birthdate");
		sp.setType(Enumerations.SearchParamType.DATE);
		sp.setCode("birthdate");
		sp.setExpression("Patient.birthDate");
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.addBase(Enumerations.VersionIndependentResourceTypesAll.PATIENT);
		mySearchParameterDao.update(fromCanonoical(sp), new SystemRequestDetails());

		sp = new SearchParameter();
		sp.setId("SearchParameter/patient-gender-birthdate");
		sp.setType(Enumerations.SearchParamType.COMPOSITE);
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.addBase(Enumerations.VersionIndependentResourceTypesAll.PATIENT);
		sp.addComponent()
			.setExpression("Patient")
			.setDefinition("SearchParameter/patient-gender");
		sp.addComponent()
			.setExpression("Patient")
			.setDefinition("SearchParameter/patient-birthdate");
		sp.addExtension()
			.setUrl(HapiExtensions.EXT_SP_UNIQUE)
			.setValue(new BooleanType(theUnique));
		for (var next : theSearchParamCustomizer) {
			next.accept(sp);
		}

		mySearchParameterDao.update(fromCanonoical(sp), new SystemRequestDetails());

		mySearchParamRegistry.forceRefresh();
	}

	public void createFamilyAndGenderSps(boolean theUnique, ISearchParamCustomizer... theSearchParamCustomizer) {
		SearchParameter sp = new SearchParameter();
		sp.setId("SearchParameter/patient-family");
		sp.setType(Enumerations.SearchParamType.STRING);
		sp.setCode("family");
		sp.setExpression("Patient.name.family");
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.addBase(Enumerations.VersionIndependentResourceTypesAll.PATIENT);
		mySearchParameterDao.update(fromCanonoical(sp), new SystemRequestDetails());

		sp = new SearchParameter();
		sp.setId("SearchParameter/patient-gender");
		sp.setType(Enumerations.SearchParamType.TOKEN);
		sp.setCode("gender");
		sp.setExpression("Patient.gender");
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.addBase(Enumerations.VersionIndependentResourceTypesAll.PATIENT);
		mySearchParameterDao.update(fromCanonoical(sp), new SystemRequestDetails());

		sp = new SearchParameter();
		sp.setId("SearchParameter/patient-family-gender");
		sp.setType(Enumerations.SearchParamType.COMPOSITE);
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.addBase(Enumerations.VersionIndependentResourceTypesAll.PATIENT);
		sp.addComponent()
			.setExpression("Patient")
			.setDefinition("SearchParameter/patient-family");
		sp.addComponent()
			.setExpression("Patient")
			.setDefinition("SearchParameter/patient-gender");
		sp.addExtension()
			.setUrl(HapiExtensions.EXT_SP_UNIQUE)
			.setValue(new BooleanType(theUnique));
		for (var next : theSearchParamCustomizer) {
			next.accept(sp);
		}

		mySearchParameterDao.update(fromCanonoical(sp), new SystemRequestDetails());

		mySearchParamRegistry.forceRefresh();
	}

	private IBaseResource fromCanonoical(SearchParameter theSearchParameter) {
		return myVersionCanonicalizer.searchParameterFromCanonical(theSearchParameter);
	}


	@FunctionalInterface
	public interface ISearchParamCustomizer {

		void accept(SearchParameter theSearchParameter);

	}

}
