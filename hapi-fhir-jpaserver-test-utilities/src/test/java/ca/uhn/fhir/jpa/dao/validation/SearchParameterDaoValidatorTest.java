package ca.uhn.fhir.jpa.dao.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.hapi.converters.canonical.VersionCanonicalizer;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.SearchParameter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class SearchParameterDaoValidatorTest {

	@Spy
	private FhirContext ourCtx = FhirContext.forR4Cached();
	@Mock
	private ISearchParamRegistry mySearchParamRegistry;
	@Spy
	private JpaStorageSettings myStorageSettings = new JpaStorageSettings();
	@InjectMocks
	private SearchParameterDaoValidator mySvc;

	private VersionCanonicalizer myVersionCanonicalizer = new VersionCanonicalizer(ourCtx);

	@Test
	public void testValidateSubscription() {
		SearchParameter sp = new SearchParameter();
		sp.setId("SearchParameter/patient-eyecolour");
		sp.setUrl("http://example.org/SearchParameter/patient-eyecolour");
		sp.addBase("Patient");
		sp.setCode("eyecolour");
		sp.setType(Enumerations.SearchParamType.TOKEN);
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.setExpression("Patient.extension('http://foo')");
		sp.addTarget("Patient");

		org.hl7.fhir.r5.model.SearchParameter canonicalSp = myVersionCanonicalizer.searchParameterToCanonical(sp);
		mySvc.validate(canonicalSp);
	}

	@Test
	public void testValidateSubscriptionWithCustomType() {
		SearchParameter sp = new SearchParameter();
		sp.setId("SearchParameter/meal-chef");
		sp.setUrl("http://example.org/SearchParameter/meal-chef");
		sp.addBase("Meal");
		sp.setCode("chef");
		sp.setType(Enumerations.SearchParamType.REFERENCE);
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.setExpression("Meal.chef");
		sp.addTarget("Chef");

		org.hl7.fhir.r5.model.SearchParameter canonicalSp = myVersionCanonicalizer.searchParameterToCanonical(sp);
		mySvc.validate(canonicalSp);
	}

}
