package ca.uhn.fhir.jpa.searchparam.registry;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.hapi.converters.canonical.VersionCanonicalizer;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.SearchParameter;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class SearchParameterCanonicalizerTest {

	@ParameterizedTest
	@ValueSource(booleans = {false, true})
	public void testCanonicalizeSearchParameterWithCustomType(boolean theConvertToR5) {
		SearchParameter sp = new SearchParameter();
		sp.setId("SearchParameter/meal-chef");
		sp.setUrl("http://example.org/SearchParameter/meal-chef");
		sp.addBase("Meal");
		sp.addBase("Patient");
		sp.setCode("chef");
		sp.setType(Enumerations.SearchParamType.REFERENCE);
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.setExpression("Meal.chef | Observation.subject");
		sp.addTarget("Chef");
		sp.addTarget("Observation");

		IBaseResource searchParamToCanonicalize = sp;
		SearchParameterCanonicalizer svc;
		if (theConvertToR5) {
			VersionCanonicalizer versionCanonicalizer = new VersionCanonicalizer(FhirContext.forR4Cached());
			searchParamToCanonicalize = versionCanonicalizer.searchParameterToCanonical(sp);
			svc = new SearchParameterCanonicalizer(FhirContext.forR5Cached());
		} else {
			svc = new SearchParameterCanonicalizer(FhirContext.forR4Cached());
		}

		RuntimeSearchParam output = svc.canonicalizeSearchParameter(searchParamToCanonicalize);
		assertEquals("chef", output.getName());
		assertEquals(RestSearchParameterTypeEnum.REFERENCE, output.getParamType());
		assertEquals(RuntimeSearchParam.RuntimeSearchParamStatusEnum.ACTIVE, output.getStatus());
		assertThat(output.getPathsSplit(), containsInAnyOrder("Meal.chef", "Observation.subject"));
		assertThat(output.getBase(), containsInAnyOrder("Meal", "Patient"));
		assertThat(output.getTargets(), contains("Chef", "Observation"));

	}

}
