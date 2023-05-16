package ca.uhn.fhir.jpa.dao.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.util.HapiExtensions;
import ca.uhn.hapi.converters.canonical.VersionCanonicalizer;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.SearchParameter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;

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

	@Test
	public void testValidateCompositeSearchParameterIncorrectComponentType() {
		RuntimeSearchParam observationCodeSearchParam = new RuntimeSearchParam(new IdDt("observation-code"),
			null, null, null, "Observation.code", RestSearchParameterTypeEnum.TOKEN,
			null, null, RuntimeSearchParam.RuntimeSearchParamStatusEnum.ACTIVE, null, null, List.of("Observation"));
		RuntimeSearchParam observationPatientSearchParam = new RuntimeSearchParam(new IdDt("observation-patient"),
			null, null, null, "Observation.subject.where(resolve() is Patient)", RestSearchParameterTypeEnum.REFERENCE,
			null, null, RuntimeSearchParam.RuntimeSearchParamStatusEnum.ACTIVE, null, null, List.of("Observation"));

		lenient().when(mySearchParamRegistry.getActiveSearchParam(eq("Observation"), eq("observation-code")))
			.thenReturn(observationCodeSearchParam);
		lenient().when(mySearchParamRegistry.getActiveSearchParam(eq("Observation"), eq("observation-patient")))
			.thenReturn(observationPatientSearchParam);

		SearchParameter sp = new SearchParameter();
		sp.setId("SearchParameter/patient-code");
		sp.setUrl("http://example.org/SearchParameter/patient-code");
		sp.addBase("Observation");
		sp.setCode("patient-code");
		sp.setType(Enumerations.SearchParamType.COMPOSITE);
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.setExpression("Observation");
		sp.addExtension(new Extension(HapiExtensions.EXT_SP_UNIQUE, new BooleanType(false)));
		sp.addComponent(new SearchParameter.SearchParameterComponentComponent()
			.setDefinition("SearchParameter/observation-code").setExpression("Observation"));
		sp.addComponent(new SearchParameter.SearchParameterComponentComponent()
			.setDefinition("SearchParameter/observation-patient").setExpression("Observation"));

		org.hl7.fhir.r5.model.SearchParameter canonicalSp = myVersionCanonicalizer.searchParameterToCanonical(sp);

		UnprocessableEntityException thrown = assertThrows(UnprocessableEntityException.class, () -> mySvc.validate(canonicalSp));
		assertTrue(thrown.getMessage().contains("Incorrect component Search Parameter"));
	}

	@Test
	public void testValidateCompositeSearchParameterCorrectComponentTypes() {
		RuntimeSearchParam observationCodeSearchParam = new RuntimeSearchParam(new IdDt("observation-code"),
			null, null, null, "Observation.code", RestSearchParameterTypeEnum.TOKEN,
			null, null, RuntimeSearchParam.RuntimeSearchParamStatusEnum.ACTIVE, null, null, List.of("Observation"));
		RuntimeSearchParam observationEffectiveSearchParam = new RuntimeSearchParam(new IdDt("observation-effective"),
			null, null, null, "Observation.effective", RestSearchParameterTypeEnum.DATE,
			null, null, RuntimeSearchParam.RuntimeSearchParamStatusEnum.ACTIVE, null, null, List.of("Observation"));

		lenient().when(mySearchParamRegistry.getActiveSearchParam(eq("Observation"), eq("observation-code")))
			.thenReturn(observationCodeSearchParam);
		lenient().when(mySearchParamRegistry.getActiveSearchParam(eq("Observation"), eq("observation-effective")))
			.thenReturn(observationEffectiveSearchParam);

		SearchParameter sp = new SearchParameter();
		sp.setId("SearchParameter/code-effective");
		sp.setUrl("http://example.org/SearchParameter/patient-code");
		sp.addBase("Observation");
		sp.setCode("code-effective");
		sp.setType(Enumerations.SearchParamType.COMPOSITE);
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.setExpression("Observation");
		sp.addExtension(new Extension(HapiExtensions.EXT_SP_UNIQUE, new BooleanType(false)));
		sp.addComponent(new SearchParameter.SearchParameterComponentComponent()
			.setDefinition("SearchParameter/observation-code").setExpression("Observation"));
		sp.addComponent(new SearchParameter.SearchParameterComponentComponent()
			.setDefinition("SearchParameter/observation-effective").setExpression("Observation"));

		org.hl7.fhir.r5.model.SearchParameter canonicalSp = myVersionCanonicalizer.searchParameterToCanonical(sp);
		Assertions.assertDoesNotThrow(() -> mySvc.validate(canonicalSp));
	}

}
