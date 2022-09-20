package ca.uhn.fhir.jpa.searchparam.extractor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.FhirContextSearchParamRegistry;
import ca.uhn.fhir.test.utilities.ITestDataBuilder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchParamExtractorServiceTest implements ITestDataBuilder.WithSupport {

	final ModelConfig myModelConfig = new ModelConfig();
	SearchParamExtractorService mySvc;
	@Mock
	IInterceptorBroadcaster myRequestInterceptorBroadcaster;
	@Mock
	IInterceptorBroadcaster myJpaInterceptorBroadcaster;
	final FhirContext myFhirContext = FhirContext.forR4Cached();
	final FhirContextSearchParamRegistry mySearchParamRegistry = new FhirContextSearchParamRegistry(myFhirContext);

	@BeforeEach
	void before() {
		mySvc = new SearchParamExtractorService();
		mySvc.setInterceptorBroadcasterForUnitTest(myJpaInterceptorBroadcaster);
		mySvc.setSearchParamExtractor(new SearchParamExtractorR4(myModelConfig, new PartitionSettings(), myFhirContext, mySearchParamRegistry));
		mySvc.setModelConfig(myModelConfig);
		mySvc.setContext(myFhirContext);
	}

	@Test
	void testHandleWarnings() {
		ISearchParamExtractor.SearchParamSet<Object> searchParamSet = new ISearchParamExtractor.SearchParamSet<>();
		searchParamSet.addWarning("help i'm a bug");
		searchParamSet.addWarning("Spiff");

		when(myJpaInterceptorBroadcaster.callHooks(any(), any())).thenReturn(true);

		SearchParamExtractorService.handleWarnings(new ServletRequestDetails(myRequestInterceptorBroadcaster), myJpaInterceptorBroadcaster, searchParamSet);

		verify(myJpaInterceptorBroadcaster, times(2)).callHooks(eq(Pointcut.JPA_PERFTRACE_WARNING), any());
		verify(myRequestInterceptorBroadcaster, times(2)).callHooks(eq(Pointcut.JPA_PERFTRACE_WARNING), any());
	}

	@Test
	void testExtraction_composite_populates() {
		ResourceIndexedSearchParams extracted = new ResourceIndexedSearchParams();
		IBaseResource resource = buildResource("Observation",
			withObservationComponent(
				withCodingAt("code.coding", "http://example.com", "8480-6", null),
				withQuantityAtPath("valueQuantity", 60, null, "mmHg")),
			withObservationComponent(
				withCodingAt("code.coding", "http://example.com", "3421-5", null),
				withQuantityAtPath("valueQuantity", 100, null, "mmHg"))
		);
		mySvc.extractSearchIndexParameters(new ServletRequestDetails(), extracted, resource, null);
		assertThat(extracted.myCompositeParams, notNullValue());
		assertThat(extracted.myCompositeParams, not(empty()));
		// wipmb head
	}

	@Override
	public Support getSupport() {
		return new Support() {
			@Override
			public FhirContext getFhirContext() {
				return myFhirContext;
			}

			@Override
			public IIdType createResource(IBaseResource theResource) {
				return null;
			}

			@Override
			public IIdType updateResource(IBaseResource theResource) {
				return null;
			}
		};
	}
}
