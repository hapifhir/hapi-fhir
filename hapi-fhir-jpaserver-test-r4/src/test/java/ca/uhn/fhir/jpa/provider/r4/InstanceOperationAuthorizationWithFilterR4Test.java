package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.searchparam.matcher.AuthorizationSearchParamMatcher;
import ca.uhn.fhir.jpa.searchparam.matcher.SearchParamMatcher;
import ca.uhn.fhir.jpa.util.CircularQueueCaptureQueriesListener;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor;
import ca.uhn.fhir.rest.server.interceptor.auth.IAuthResourceResolver;
import ca.uhn.fhir.rest.server.interceptor.auth.IAuthRule;
import ca.uhn.fhir.rest.server.interceptor.auth.IAuthorizationSearchParamMatcher;
import ca.uhn.fhir.rest.server.interceptor.auth.PolicyEnum;
import ca.uhn.fhir.rest.server.interceptor.auth.RuleBuilder;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mockito;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Stream;

import static ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable.HFJ_RES_VER;
import static ca.uhn.fhir.jpa.model.util.JpaConstants.OPERATION_VALIDATE;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_META;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class InstanceOperationAuthorizationWithFilterR4Test extends BaseResourceProviderR4Test {

	private static final String CATEGORY_VITAL_SIGNS = "vital-signs";
	private static final String FILTER_VITAL_SIGNS = "category=" + CATEGORY_VITAL_SIGNS;

	private static final String CATEGORY_IMAGING = "imaging";
	private static final String FILTER_IMAGING = "category=" + CATEGORY_IMAGING;

	@Autowired
	private IAuthResourceResolver myAuthResolver;

	private IAuthResourceResolver myAuthResolverSpy;

	@Autowired
	private SearchParamMatcher mySearchParamMatcher;

	private IAuthorizationSearchParamMatcher myAuthSearchParamMatcher;

	@BeforeEach
	void beforeEach() {
		myAuthResolverSpy = spy(myAuthResolver);
		myAuthSearchParamMatcher = new AuthorizationSearchParamMatcher(mySearchParamMatcher);
		((Logger) LoggerFactory.getLogger("ca.uhn.fhir.rest.server.interceptor.auth")).setLevel(Level.DEBUG);
	}

	@AfterEach
	void afterEach() {
		myServer.getInterceptorService().unregisterAllInterceptors();
	}

	@Nested
	class OnInstancesOfTypeMatchingOptionalFilterTest {

		@ParameterizedTest
		@ArgumentsSource(VitalSignsAndBlankFilters.class)
		void observationMatchesFilter_operationAllowed(String theFilter) {
			registerAuthInterceptorForAnyInstanceOfType(OPERATION_META, Observation.class, theFilter);

			Observation observation = createObservationWithCategory(CATEGORY_VITAL_SIGNS);

			Parameters result = myServer.getFhirClient()
				.operation()
				.onInstance(observation.getIdElement())
				.named(OPERATION_META)
				.withNoParameters(Parameters.class)
				.execute();

			assertThat(result).isNotNull();
		}

		@ParameterizedTest
		@ArgumentsSource(VitalSignsAndBlankFilters.class)
		void wrongOperation_operationDenied() {
			registerAuthInterceptorForAnyInstanceOfType(OPERATION_META, Observation.class, FILTER_VITAL_SIGNS);

			Observation observation = createObservationWithCategory(CATEGORY_VITAL_SIGNS);

			assertThatThrownBy(() ->
				myServer.getFhirClient()
					.operation()
					.onInstance(observation.getIdElement())
					.named(OPERATION_VALIDATE)
					.withNoParameters(Parameters.class)
					.execute()
			).isInstanceOf(ForbiddenOperationException.class);
		}

		@Test
		void observationDoesNotMatchFilter_operationDenied() {
			registerAuthInterceptorForAnyInstanceOfType(OPERATION_META, Observation.class, FILTER_VITAL_SIGNS);

			Observation observation = createObservationWithCategory(CATEGORY_IMAGING);

			assertThatThrownBy(() ->
				myServer.getFhirClient()
					.operation()
					.onInstance(observation.getIdElement())
					.named(OPERATION_META)
					.withNoParameters(Parameters.class)
					.execute()
			).isInstanceOf(ForbiddenOperationException.class);
		}

		@Test
		void wrongResourceType_operationDenied() {
			registerAuthInterceptorForAnyInstanceOfType(OPERATION_META, Observation.class, FILTER_VITAL_SIGNS);

			Patient patient = createPatient();

			assertThatThrownBy(() ->
					myServer.getFhirClient()
						.operation()
						.onInstance(patient.getIdElement())
						.named(OPERATION_META)
						.withNoParameters(Parameters.class)
						.execute()
			).isInstanceOf(ForbiddenOperationException.class);
		}
	}

	@Nested
	class OnAnyInstanceMatchingOptionalFilterTest {

		@ParameterizedTest
		@ArgumentsSource(VitalSignsAndBlankFilters.class)
		void observationMatchesFilter_operationAllowed(String theFilter) {
			registerAuthInterceptorForAnyInstance(OPERATION_META, theFilter);

			Observation observation = createObservationWithCategory(CATEGORY_VITAL_SIGNS);

			Parameters result = myServer.getFhirClient()
					.operation()
					.onInstance(observation.getIdElement())
					.named(OPERATION_META)
					.withNoParameters(Parameters.class)
					.execute();

			assertThat(result).isNotNull();
		}

		@ParameterizedTest
		@ArgumentsSource(VitalSignsAndBlankFilters.class)
		void wrongOperation_operationDenied(String theFilter) {
			registerAuthInterceptorForAnyInstance(OPERATION_META, theFilter);

			Observation observation = createObservationWithCategory(CATEGORY_VITAL_SIGNS);

			assertThatThrownBy(() ->
				myServer.getFhirClient()
					.operation()
					.onInstance(observation.getIdElement())
					.named(OPERATION_VALIDATE)
					.withNoParameters(Parameters.class)
					.execute()
			).isInstanceOf(ForbiddenOperationException.class);
		}

		@Test
		void observationDoesNotMatchFilter_operationDenied() {
			registerAuthInterceptorForAnyInstance(OPERATION_META, FILTER_VITAL_SIGNS);

			Observation observation = createObservationWithCategory(CATEGORY_IMAGING);

			assertThatThrownBy(() ->
					myServer.getFhirClient()
							.operation()
							.onInstance(observation.getIdElement())
							.named(OPERATION_META)
							.withNoParameters(Parameters.class)
							.execute()
			).isInstanceOf(ForbiddenOperationException.class);
		}
	}

	@Nested
	class AuthResourceResolverCachingTest {

		private enum AuthTypeEnum {
			ANY_INSTANCE_OF_TYPE,
			ANY_INSTANCE;
		}

		@ParameterizedTest
		@EnumSource(value = AuthTypeEnum.class)
		void multipleRulesCheckSameResource_resolverReadsResourceOnce(AuthTypeEnum theAuthType) {

			switch (theAuthType) {
				case ANY_INSTANCE_OF_TYPE -> registerAuthInterceptorForAnyInstanceOfType(OPERATION_META, Observation.class, FILTER_IMAGING, FILTER_VITAL_SIGNS);
				case ANY_INSTANCE -> registerAuthInterceptorForAnyInstance(OPERATION_META, FILTER_IMAGING, FILTER_VITAL_SIGNS);
				default -> throw new IllegalArgumentException("Cannot handle enum: %s".formatted(theAuthType));
			}

			Observation observation = createObservationWithCategory(CATEGORY_VITAL_SIGNS);

			myCaptureQueriesListener.clear();

			// FIRST REQUEST
			Parameters result  = myServer.getFhirClient()
					.operation()
					.onInstance(observation.getIdElement())
					.named(OPERATION_META)
					.withNoParameters(Parameters.class)
					.execute();

			assertThat(result).isNotNull();

			// auth resolver called twice (once for each rule), but resolved observation is cached
			verify(myAuthResolverSpy, times(2)).resolveResourceById(any(), any());
			assertHfjResVerSelectQueriesMade(myCaptureQueriesListener, 1);

			// SECOND REQUEST
			myCaptureQueriesListener.clear();
			Mockito.reset(myAuthResolverSpy);
			Parameters result2  = myServer.getFhirClient()
				.operation()
				.onInstance(observation.getIdElement())
				.named(OPERATION_META)
				.withNoParameters(Parameters.class)
				.execute();

			assertThat(result2).isNotNull();

			// caching is per request - assertions should be the same as first request
			verify(myAuthResolverSpy, times(2)).resolveResourceById(any(), any());
			assertHfjResVerSelectQueriesMade(myCaptureQueriesListener, 1);
		}
	}

	private Observation createObservationWithCategory(String theCategory) {
		Observation obs = new Observation();
		obs.setStatus(Observation.ObservationStatus.FINAL);
		obs.addCategory(new CodeableConcept()
			.addCoding(new Coding()
				.setSystem("http://terminology.hl7.org/CodeSystem/observation-category")
				.setCode(theCategory)));
		return (Observation) myObservationDao.create(obs, mySrd).getResource();
	}

	private Patient createPatient(){
		Patient patient = new Patient();
		patient.setActive(true);
		return (Patient) myPatientDao.create(patient, new SystemRequestDetails()).getResource();
	}

	private void assertHfjResVerSelectQueriesMade(CircularQueueCaptureQueriesListener theCaptureQueriesListener, int theNumExpected){
		long resVerQueries = theCaptureQueriesListener.getSelectQueries()
				.stream()
				.filter(q -> q.getSql(true, false).contains(HFJ_RES_VER))
				.count();

		assertThat(resVerQueries).isEqualTo(theNumExpected);
	}

	private void registerAuthInterceptorForAnyInstanceOfType(
		String theOperationName, Class<? extends IBaseResource> theType, String... theFilters) {
		AuthorizationInterceptor authInterceptor = new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				RuleBuilder builder = new RuleBuilder();
				for (String filter : theFilters) {
					builder.allow()
						.operation()
						.named(theOperationName)
						.onInstancesOfTypeMatchingOptionalFilter(theType, filter)
						.andAllowAllResponses()
						.andThen();
				}
				return builder.build();
			}

			@Override
			public IAuthorizationSearchParamMatcher getSearchParamMatcher() {
				return myAuthSearchParamMatcher;
			}

			@Override
			public IAuthResourceResolver getAuthResourceResolver() {
				return myAuthResolverSpy;
			}
		};

		myServer.registerInterceptor(authInterceptor);
	}

	private void registerAuthInterceptorForAnyInstance(String theOperationName, String... theFilters) {
		AuthorizationInterceptor authInterceptor = new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				RuleBuilder builder = new RuleBuilder();
				for (String filter : theFilters) {
					builder.allow()
						.operation()
						.named(theOperationName)
						.onAnyInstanceMatchingOptionalFilter(filter)
						.andAllowAllResponses()
						.andThen();
				}
				return builder.build();
			}

			@Override
			public IAuthorizationSearchParamMatcher getSearchParamMatcher() {
				return myAuthSearchParamMatcher;
			}

			@Override
			public IAuthResourceResolver getAuthResourceResolver() {
				return myAuthResolverSpy;
			}
		};

		myServer.registerInterceptor(authInterceptor);
	}

	static class VitalSignsAndBlankFilters implements ArgumentsProvider {
		@Override
		public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
			return Stream.of(
					Arguments.of(FILTER_VITAL_SIGNS),
					Arguments.of((String) null),
					Arguments.of("")
			);
		}
	}
}
