package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.config.TestHibernateSearchAddInConfig;
import ca.uhn.fhir.jpa.config.TestR4Config;
import ca.uhn.fhir.jpa.dao.BaseDateSearchDaoTests;
import ca.uhn.fhir.jpa.dao.BaseJpaTest;
import ca.uhn.fhir.jpa.dao.DaoTestDataBuilder;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.ResourceSearch;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.server.method.SortParameter;
import ca.uhn.fhir.test.utilities.ITestDataBuilder;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Observation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.not;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestR4Config.class, TestHibernateSearchAddInConfig.NoFT.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class FhirResourceDaoR4StandardQueriesNoFT extends BaseJpaTest {
	private static final Logger ourLog = LoggerFactory.getLogger(FhirResourceDaoR4StandardQueriesNoFT.class);
	@Autowired
	PlatformTransactionManager myTxManager;
	@Autowired
	FhirContext myFhirCtx;
	@Autowired
	@Qualifier("myObservationDaoR4")
	IFhirResourceDao<Observation> myObservationDao;
	@Autowired
	protected DaoRegistry myDaoRegistry;
	@Autowired
	MatchUrlService myMatchUrlService;

	@Override
	protected PlatformTransactionManager getTxManager() {
		return myTxManager;
	}

	@Override
	protected FhirContext getFhirContext() {
		return myFhirCtx;
	}

	@Nested
	public class DateSearchTests extends BaseDateSearchDaoTests {
		@Override
		protected Fixture constructFixture() {
			DaoTestDataBuilder testDataBuilder = new DaoTestDataBuilder(myFhirCtx, myDaoRegistry, new SystemRequestDetails());
			return new TestDataBuilderFixture<>(testDataBuilder, myObservationDao);
		}
	}

	public static class TokenTestCase {

		private Consumer<IBaseResource>[] myBuilders;
		private List<ImmutableTriple<Boolean, String, String>> mySearchCases = new ArrayList<>();

		public static TokenTestCase onObservation(Consumer<IBaseResource>... theBuilders) {
			TokenTestCase result = new TokenTestCase();
			result.myBuilders = theBuilders;
			return result;
		}

		public TokenTestCase finds(String theMessage, String theQuery) {
			mySearchCases.add(new ImmutableTriple(true,theMessage, theQuery));
			return this;
		}
		public TokenTestCase doesNotFind(String theMessage, String theQuery) {
			mySearchCases.add(new ImmutableTriple(false,theMessage, theQuery));
			return this;
		}
	}

	@Nested
	public class TokenSearch  {
		// wipmb make this generic and share with ES, and Mongo.
		/*
		String criteria = "_has:Condition:subject:code=http://snomed.info/sct|55822003,http://snomed.info/sct|55822005&" +
			"_has:Condition:asserter:code=http://snomed.info/sct|55822003,http://snomed.info/sct|55822004";
		 */

		ITestDataBuilder myDataBuilder = new DaoTestDataBuilder(myFhirCtx, myDaoRegistry, new SystemRequestDetails());
		Set<IIdType> myCreatedIds = new HashSet<>();

		@AfterEach
		public void cleanup() {
			ourLog.info("cleanup {}", myCreatedIds);
			myCreatedIds.forEach(myObservationDao::delete);
		}


		@Nested
		public class Queries {
			IIdType myObservationId;

			@Test
			public void systemAndCode() {
				withObservation(myDataBuilder.withObservationCode("http://example.com", "value"));

				assertFind("by system and code", "/Observation?code=http://example.com|value");
				assertFind("by system, any code", "/Observation?code=http://example.com|");
				assertFind("by code, any system", "/Observation?code=value");
				assertNotFind("by same system, different code", "/Observation?code=http://example.com|other");
				assertNotFind("by same code, different system", "/Observation?code=http://example2.com|value");
				assertNotFind("by different code, different system", "/Observation?code=http://example2.com|otherValue");
			}

			@Test
			public void emptySystem() {
				withObservation(myDataBuilder.withObservationCode("", "value"));

				assertFind("by system and code", "/Observation?code=|value");
				assertFind("by system, any code", "/Observation?code=|");
				assertFind("by code, any system", "/Observation?code=value");
			}

			@Nested
			public class NotModifier {
				@Test
				public void simple() {
					withObservation(myDataBuilder.withObservationCode("http://example.com", "value"));

					assertFind("by same system, different code", "/Observation?code:not=http://example.com|other");
					assertFind("by same code, different system", "/Observation?code:not=http://example2.com|value");
					assertFind("by different code, different system", "/Observation?code:not=http://example2.com|otherValue");
					assertNotFind("by system and code", "/Observation?code:not=http://example.com|value");
					assertNotFind("by system, any code", "/Observation?code:not=http://example.com|");
					assertNotFind("by code, any system", "/Observation?code:not=value");
				}

				@Test
				public void findsEmpty() {
					withObservation();

					assertFind("by system and code", "/Observation?code:not=http://example.com|value");
					assertFind("by system, any code", "/Observation?code:not=http://example.com|");
					assertFind("by code, any system", "/Observation?code:not=value");

				}
			}

			@Nested
			public class TextModifier {
				@Test
				public void systemAndCode() {
					withObservation(myDataBuilder.withObservationCode("http://example.com", "value", "the display text"));
					assertFind("by code display", "/Observation?code:text=the%20display%20text");
				}
			}

			@Nested
			public class Sorting {
				@Test
				public void sortBySystemThenValue() {
					String idAlphaM = withObservation(myDataBuilder.withObservationCode("http://alpha.org", "Mvalue")).getIdPart();
					String idAlphaA = withObservation(myDataBuilder.withObservationCode("http://alpha.org", "Avalue")).getIdPart();
					String idAlphaZ = withObservation(myDataBuilder.withObservationCode("http://alpha.org", "Zvalue")).getIdPart();

					String idExD = withObservation(myDataBuilder.withObservationCode("http://example.org", "DValue")).getIdPart();
					String idExA = withObservation(myDataBuilder.withObservationCode("http://example.org", "AValue")).getIdPart();
					String idExM = withObservation(myDataBuilder.withObservationCode("http://example.org", "MValue")).getIdPart();

					List<String> allIds = searchForIds("/Observation?_sort=code");
					assertThat(allIds, hasItems(idAlphaA, idAlphaM, idAlphaZ, idExA, idExD, idExM));

					allIds = searchForIds("/Observation?_sort=code&code=http://example.org|");
					assertThat(allIds, hasItems(idExA, idExD, idExM));

				}
			}

			private IIdType withObservation(Consumer<IBaseResource>... theBuilder) {
				myObservationId = myDataBuilder.createObservation(theBuilder);
				myCreatedIds.add(myObservationId);
				return myObservationId;
			}

			private void assertFind(String theMessage, String theUrl) {
				List<String> resourceIds = searchForIds(theUrl);
				assertThat(theMessage, resourceIds, hasItem(equalTo(myObservationId.getIdPart())));
			}

			private void assertNotFind(String theMessage, String theUrl) {
				List<String> resourceIds = searchForIds(theUrl);
				assertThat(theMessage, resourceIds, not(hasItem(equalTo(myObservationId.getIdPart()))));
			}
		}

		private List<String> searchForIds(String theQueryUrl) {
			// fake out the server url parsing
			ResourceSearch search = myMatchUrlService.getResourceSearch(theQueryUrl);
			SearchParameterMap map = search.getSearchParameterMap();
			map.setLoadSynchronous(true);
			SystemRequestDetails request = fakeRequestDetailsFromUrl(theQueryUrl);
			SortSpec sort = (SortSpec) new SortParameter(myFhirCtx).translateQueryParametersIntoServerArgument(request, null);
			if (sort != null) {
				map.setSort(sort);
			}

			IBundleProvider result = myObservationDao.search(map);

			List<String> resourceIds = result.getAllResourceIds();
			return resourceIds;
		}

	}

	@Nonnull
	private SystemRequestDetails fakeRequestDetailsFromUrl(String theQueryUrl) {
		SystemRequestDetails request = new SystemRequestDetails();
		UriComponents uriComponents = UriComponentsBuilder.fromUriString(theQueryUrl).build();
		uriComponents.getQueryParams().entrySet().forEach(nextEntry -> {
			request.addParameter(nextEntry.getKey(), nextEntry.getValue().toArray(new String[0]));
		});
		return request;
	}

}
