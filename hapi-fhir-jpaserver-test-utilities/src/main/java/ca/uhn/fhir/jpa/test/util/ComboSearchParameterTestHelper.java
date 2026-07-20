/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ComboSearchParameterTestHelper {
	private static final Logger ourLog = LoggerFactory.getLogger(ComboSearchParameterTestHelper.class);

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


	public void createPatientIdentifierUnique() {

		SearchParameter sp = new SearchParameter();
		sp.setId("SearchParameter/patient-identifier");
		sp.setCode("identifier");
		sp.setExpression("Patient.identifier");
		sp.setType(Enumerations.SearchParamType.TOKEN);
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.addBase(Enumerations.VersionIndependentResourceTypesAll.PATIENT);
		storeSearchParameter(sp);

		sp = new SearchParameter();
		sp.setId("SearchParameter/patient-uniq-identifier");
		sp.setCode("patient-uniq-identifier");
		sp.setExpression("Patient");
		sp.setType(Enumerations.SearchParamType.COMPOSITE);
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.addBase(Enumerations.VersionIndependentResourceTypesAll.PATIENT);
		sp.addComponent()
			.setExpression("Patient")
			.setDefinition("/SearchParameter/patient-identifier");
		sp.addExtension()
			.setUrl(HapiExtensions.EXT_SP_UNIQUE)
			.setValue(new BooleanType(true));
		storeSearchParameter(sp);
		mySearchParamRegistry.forceRefresh();
	}

	public void createBirthdateAndGenderSps(boolean theUnique, ISearchParamCustomizer... theSearchParamCustomizer) {
		SearchParameter sp = new SearchParameter();
		sp.setId("SearchParameter/patient-gender");
		sp.setType(Enumerations.SearchParamType.TOKEN);
		sp.setCode("gender");
		sp.setExpression("Patient.gender");
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.addBase(Enumerations.VersionIndependentResourceTypesAll.PATIENT);
		storeSearchParameter(sp);

		sp = new SearchParameter();
		sp.setId("SearchParameter/patient-birthdate");
		sp.setType(Enumerations.SearchParamType.DATE);
		sp.setCode("birthdate");
		sp.setExpression("Patient.birthDate");
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.addBase(Enumerations.VersionIndependentResourceTypesAll.PATIENT);
		storeSearchParameter(sp);

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

		storeSearchParameter(sp);

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
		storeSearchParameter(sp);

		sp = new SearchParameter();
		sp.setId("SearchParameter/patient-gender");
		sp.setType(Enumerations.SearchParamType.TOKEN);
		sp.setCode("gender");
		sp.setExpression("Patient.gender");
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.addBase(Enumerations.VersionIndependentResourceTypesAll.PATIENT);
		storeSearchParameter(sp);

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

		storeSearchParameter(sp);

		mySearchParamRegistry.forceRefresh();
	}

	public IBaseResource createObservationSubjectCodeAndRangedEffective() {
		return createObservationSubjectCodeEffective(false, true);
	}

	public IBaseResource createObservationSubjectCodeEffective(boolean theUnique, boolean theDateComponentRanged) {
		SearchParameter sp = new SearchParameter();
		sp.setId("SearchParameter/observation-subject");
		sp.setType(Enumerations.SearchParamType.REFERENCE);
		sp.setCode("subject");
		sp.setExpression("Observation.subject");
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.addBase(Enumerations.VersionIndependentResourceTypesAll.OBSERVATION);
		storeSearchParameter(sp);

		sp = new SearchParameter();
		sp.setId("SearchParameter/observation-code");
		sp.setType(Enumerations.SearchParamType.TOKEN);
		sp.setCode("code");
		sp.setExpression("Observation.code");
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.addBase(Enumerations.VersionIndependentResourceTypesAll.OBSERVATION);
		storeSearchParameter(sp);

		sp = new SearchParameter();
		sp.setId("SearchParameter/observation-date");
		sp.setType(Enumerations.SearchParamType.DATE);
		sp.setCode("date");
		sp.setExpression("Observation.effective.ofType(dateTime) | Observation.effective.ofType(Period) | Observation.effective.ofType(Timing) | Observation.effective.ofType(instant)");
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.addBase(Enumerations.VersionIndependentResourceTypesAll.OBSERVATION);
		storeSearchParameter(sp);

		sp = new SearchParameter();
		sp.setId("SearchParameter/observation-subject-code-ranged-date");
		sp.setType(Enumerations.SearchParamType.COMPOSITE);
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.addBase(Enumerations.VersionIndependentResourceTypesAll.OBSERVATION);
		sp.addComponent()
			.setExpression("Observation")
			.setDefinition("SearchParameter/observation-subject");
		sp.addComponent()
			.setExpression("Observation")
			.setDefinition("SearchParameter/observation-code");
		SearchParameter.SearchParameterComponentComponent dateComponent = sp.addComponent()
			.setExpression("Observation")
			.setDefinition("SearchParameter/observation-date");
		if (theDateComponentRanged) {
			dateComponent
				.addExtension()
				.setUrl(HapiExtensions.EXT_SP_COMBO_DATE_RANGED)
				.setValue(new BooleanType(true));
		}

		sp.addExtension()
			.setUrl(HapiExtensions.EXT_SP_UNIQUE)
			.setValue(new BooleanType(theUnique));
		IBaseResource retVal = storeSearchParameter(sp);

		mySearchParamRegistry.forceRefresh();
		return retVal;
	}

	private IBaseResource storeSearchParameter(SearchParameter theSearchParameter) {
		assertTrue(theSearchParameter.getIdElement().hasIdPart());
		ourLog.atInfo()
			.setMessage("Storing SP:\n{}")
			.addArgument(() -> FhirContext.forR5Cached().newJsonParser().setPrettyPrint(true).encodeResourceToString(theSearchParameter))
			.log();
		IBaseResource nonCanonicalResource = fromCanonical(theSearchParameter);
		mySearchParameterDao.update(nonCanonicalResource, new SystemRequestDetails());
		return nonCanonicalResource;
	}

	private IBaseResource fromCanonical(SearchParameter theSearchParameter) {
		return myVersionCanonicalizer.searchParameterFromCanonical(theSearchParameter);
	}

	public void createDocumentSubjectAndRangedDateSp() {
		SearchParameter sp = new SearchParameter();
		sp.setId("Bundle-composition-subject");
		sp.setUrl("http://example.org/SearchParameter/Bundle-composition-subject");
		sp.setName("composition.subject");
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.setCode("composition.subject");
		sp.addBase(Enumerations.VersionIndependentResourceTypesAll.BUNDLE);
		sp.setType(Enumerations.SearchParamType.REFERENCE);
		sp.setExpression("Bundle.where(type = 'document').entry[0].resource.as(Composition).subject");
		storeSearchParameter(sp);

		sp = new SearchParameter();
		sp.setId("Bundle-composition-date");
		sp.setUrl("http://example.org/SearchParameter/Bundle-composition-date");
		sp.setName("composition.date");
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.setCode("composition.date");
		sp.addBase(Enumerations.VersionIndependentResourceTypesAll.BUNDLE);
		sp.setType(Enumerations.SearchParamType.DATE);
		sp.setExpression("Bundle.where(type = 'document').entry[0].resource.as(Composition).date");
		storeSearchParameter(sp);

		sp = new SearchParameter();
		sp.setId("SearchParameter/Bundle-composition-subject-and-date");
		sp.addExtension().setUrl("http://hapifhir.io/fhir/StructureDefinition/sp-unique").setValue(new BooleanType(false));
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.setCode("bundle-composition-subject-and-type");
		sp.addBase(Enumerations.VersionIndependentResourceTypesAll.BUNDLE);
		sp.setType(Enumerations.SearchParamType.COMPOSITE);
		sp.setExpression("Bundle");
		sp.addComponent()
			.setDefinition("SearchParameter/Bundle-composition-subject")
			.setExpression("Bundle");
		sp.addComponent()
			.setDefinition("SearchParameter/Bundle-composition-date")
			.setExpression("Bundle")
			.addExtension()
			.setUrl(HapiExtensions.EXT_SP_COMBO_DATE_RANGED)
			.setValue(new BooleanType(true));
		storeSearchParameter(sp);

		mySearchParamRegistry.forceRefresh();

	}


	@FunctionalInterface
	public interface ISearchParamCustomizer {

		void accept(SearchParameter theSearchParameter);

	}

}
