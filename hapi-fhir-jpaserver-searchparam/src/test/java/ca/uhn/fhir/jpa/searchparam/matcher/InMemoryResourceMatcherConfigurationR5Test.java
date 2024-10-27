package ca.uhn.fhir.jpa.searchparam.matcher;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.extractor.ResourceIndexedSearchParams;
import ca.uhn.fhir.jpa.searchparam.extractor.SearchParamExtractorService;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.param.TokenParamModifier;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import org.hl7.fhir.r5.model.CodeableConcept;
import org.hl7.fhir.r5.model.Coding;
import org.hl7.fhir.r5.model.Observation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static ca.uhn.fhir.jpa.searchparam.matcher.InMemoryResourceMatcherR5Test.newRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class InMemoryResourceMatcherConfigurationR5Test {
	public static final String OBSERVATION_CODE = "MATCH";
	public static final String OBSERVATION_CODE_SYSTEM = "http://hl7.org/some-cs";
	public static final String OBSERVATION_CODE_DISPLAY = "Match";
	public static final String OBSERVATION_CODE_VALUE_SET_URI = "http://hl7.org/some-vs";

	@MockBean
	ISearchParamRegistry mySearchParamRegistry;
	@Autowired
	private InMemoryResourceMatcher myInMemoryResourceMatcher;
	@MockBean
	private SearchParamExtractorService mySearchParamExtractorService;
	@MockBean
	private IndexedSearchParamExtractor myIndexedSearchParamExtractor;
	private Observation myObservation;
	private ResourceIndexedSearchParams mySearchParams;

	@BeforeEach
	public void before() {
		RuntimeSearchParam codeSearchParam = new RuntimeSearchParam(null, null, null, null, "Observation.code", RestSearchParameterTypeEnum.TOKEN, null, null, RuntimeSearchParam.RuntimeSearchParamStatusEnum.ACTIVE, null, null, null);
		when(mySearchParamRegistry.getActiveSearchParam("Observation", "code")).thenReturn(codeSearchParam);

		myObservation = new Observation();
		CodeableConcept codeableConcept = new CodeableConcept();
		codeableConcept.addCoding().setCode(OBSERVATION_CODE)
			.setSystem(OBSERVATION_CODE_SYSTEM).setDisplay(OBSERVATION_CODE_DISPLAY);
		myObservation.setCode(codeableConcept);
		mySearchParams = extractSearchParams(myObservation);
	}

	@Test
	@Order(1)	// We have to do this one first, because the InMemoryResourceMatcher is stateful regarding whether it has been initialized yet
	public void testValidationSupportInitializedOnlyOnce() {
		ApplicationContext applicationContext = mock(ApplicationContext.class);
		when(applicationContext.getBean(IValidationSupport.class)).thenThrow(new ConfigurationException());
		myInMemoryResourceMatcher.myApplicationContext = applicationContext;

		for (int i = 0; i < 10; i++) {
			myInMemoryResourceMatcher.match("code" + TokenParamModifier.IN.getValue() + "=" + OBSERVATION_CODE_VALUE_SET_URI, myObservation, mySearchParams, newRequest());
		}

		verify(applicationContext, times(1)).getBean(IValidationSupport.class);
	}

	@Test
	@Order(2)
	/*
		Tests the case where the :in qualifier can not be supported because no bean implementing IValidationSupport was registered
	 */
	public void testUnsupportedIn() {
		InMemoryMatchResult result = myInMemoryResourceMatcher.match("code" + TokenParamModifier.IN.getValue() + "=" + OBSERVATION_CODE_VALUE_SET_URI, myObservation, mySearchParams, newRequest());
		assertFalse(result.supported());
		assertEquals("Parameter: <code:in> Reason: Qualified parameter not supported", result.getUnsupportedReason());
	}

	@Test
	@Order(3)
	public void testUnsupportedNotIn() {
		InMemoryMatchResult result = myInMemoryResourceMatcher.match("code" + TokenParamModifier.NOT_IN.getValue() + "=" + OBSERVATION_CODE_VALUE_SET_URI, myObservation, mySearchParams, newRequest());
		assertFalse(result.supported());
		assertEquals("Parameter: <code:not-in> Reason: Qualified parameter not supported", result.getUnsupportedReason());
	}

	private ResourceIndexedSearchParams extractSearchParams(Observation theObservation) {
		ResourceIndexedSearchParams retval = ResourceIndexedSearchParams.withSets();
		retval.myTokenParams.add(extractCodeTokenParam(theObservation));
		return retval;
	}

	private ResourceIndexedSearchParamToken extractCodeTokenParam(Observation theObservation) {
		Coding coding = theObservation.getCode().getCodingFirstRep();
		return new ResourceIndexedSearchParamToken(new PartitionSettings(), "Observation", "code", coding.getSystem(), coding.getCode());
	}

	@Configuration
	public static class SpringConfig {
		@Bean
		InMemoryResourceMatcher inMemoryResourceMatcher() {
			return new InMemoryResourceMatcher();
		}

		@Bean
		MatchUrlService matchUrlService() {
			return new MatchUrlService();
		}

		@Bean
		FhirContext fhirContext() {
			return FhirContext.forR5();
		}

		@Bean
        StorageSettings storageSettings() {
			return new StorageSettings();
		}
	}

}
