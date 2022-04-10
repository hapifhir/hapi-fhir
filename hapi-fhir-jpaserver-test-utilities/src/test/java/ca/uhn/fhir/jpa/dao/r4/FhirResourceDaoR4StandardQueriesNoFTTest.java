package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.TestDaoSearch;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.test.BaseJpaTest;
import ca.uhn.fhir.jpa.test.config.TestHibernateSearchAddInConfig;
import ca.uhn.fhir.jpa.test.config.TestR4Config;
import ca.uhn.fhir.storage.test.BaseDateSearchDaoTests;
import ca.uhn.fhir.storage.test.DaoTestDataBuilder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Observation;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;
import java.util.function.Consumer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.not;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
	TestR4Config.class,
	TestHibernateSearchAddInConfig.NoFT.class,
	DaoTestDataBuilder.Config.class,
	TestDaoSearch.Config.class
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class FhirResourceDaoR4StandardQueriesNoFTTest extends BaseJpaTest {
	@Autowired
	protected DaoRegistry myDaoRegistry;
	@Autowired
	PlatformTransactionManager myTxManager;
	@Autowired
	FhirContext myFhirCtx;
	@Autowired
	@Qualifier("myObservationDaoR4")
	IFhirResourceDao<Observation> myObservationDao;
	@Autowired
	MatchUrlService myMatchUrlService;
	@RegisterExtension
	@Autowired
	DaoTestDataBuilder myDataBuilder;
	@Autowired
	TestDaoSearch myTestDaoSearch;

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
			return new TestDataBuilderFixture<>(myDataBuilder, myObservationDao);
		}
	}

	@Nested
	public class TokenSearch {

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

			@SafeVarargs
			private IIdType withObservation(Consumer<IBaseResource>... theBuilder) {
				myObservationId = myDataBuilder.createObservation(theBuilder);
				return myObservationId;
			}

			private void assertFind(String theMessage, String theUrl) {
				List<String> resourceIds = myTestDaoSearch.searchForIds(theUrl);
				assertThat(theMessage, resourceIds, hasItem(equalTo(myObservationId.getIdPart())));
			}

			private void assertNotFind(String theMessage, String theUrl) {
				List<String> resourceIds = myTestDaoSearch.searchForIds(theUrl);
				assertThat(theMessage, resourceIds, not(hasItem(equalTo(myObservationId.getIdPart()))));
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

					List<String> allIds = myTestDaoSearch.searchForIds("/Observation?_sort=code");
					assertThat(allIds, hasItems(idAlphaA, idAlphaM, idAlphaZ, idExA, idExD, idExM));

					allIds = myTestDaoSearch.searchForIds("/Observation?_sort=code&code=http://example.org|");
					assertThat(allIds, hasItems(idExA, idExD, idExM));
				}
			}
		}
	}

	@Nested
	public class NumericSearch {
		IIdType myResourceId;

		private IIdType withRiskAssessmentWithProbabilty(double theValue) {
			myResourceId = myDataBuilder.createResource("RiskAssessment", myDataBuilder.withPrimitiveAttribute("prediction.probabilityDecimal", theValue));
			return myResourceId;
		}

		@Nested
		public class Queries {

			@Test
			public void eq() {
				withRiskAssessmentWithProbabilty(0.6);

				assertNotFind("when gt", "/RiskAssessment?probability=0.5");
				// TODO we break the spec here.  Default search should be approx
				// assertFind("when a little gt - default is approx", "/RiskAssessment?probability=0.599");
				// assertFind("when a little lt - default is approx", "/RiskAssessment?probability=0.601");
				assertFind("when eq", "/RiskAssessment?probability=0.6");
				assertNotFind("when lt", "/RiskAssessment?probability=0.7");
			}

			@Test
			public void ne() {
				withRiskAssessmentWithProbabilty(0.6);

				assertFind("when gt", "/RiskAssessment?probability=ne0.5");
				assertNotFind("when eq", "/RiskAssessment?probability=ne0.6");
				assertFind("when lt", "/RiskAssessment?probability=ne0.7");
			}

			@Test
			public void ap() {
				withRiskAssessmentWithProbabilty(0.6);

				assertNotFind("when gt", "/RiskAssessment?probability=ap0.5");
				assertFind("when a little gt", "/RiskAssessment?probability=ap0.58");
				assertFind("when eq", "/RiskAssessment?probability=ap0.6");
				assertFind("when a little lt", "/RiskAssessment?probability=ap0.62");
				assertNotFind("when lt", "/RiskAssessment?probability=ap0.7");
			}

			@Test
			public void gt() {
				withRiskAssessmentWithProbabilty(0.6);

				assertFind("when gt", "/RiskAssessment?probability=gt0.5");
				assertNotFind("when eq", "/RiskAssessment?probability=gt0.6");
				assertNotFind("when lt", "/RiskAssessment?probability=gt0.7");

			}

			@Test
			public void ge() {
				withRiskAssessmentWithProbabilty(0.6);

				assertFind("when gt", "/RiskAssessment?probability=ge0.5");
				assertFind("when eq", "/RiskAssessment?probability=ge0.6");
				assertNotFind("when lt", "/RiskAssessment?probability=ge0.7");
			}

			@Test
			public void lt() {
				withRiskAssessmentWithProbabilty(0.6);

				assertNotFind("when gt", "/RiskAssessment?probability=lt0.5");
				assertNotFind("when eq", "/RiskAssessment?probability=lt0.6");
				assertFind("when lt", "/RiskAssessment?probability=lt0.7");

			}

			@Test
			public void le() {
				withRiskAssessmentWithProbabilty(0.6);

				assertNotFind("when gt", "/RiskAssessment?probability=le0.5");
				assertFind("when eq", "/RiskAssessment?probability=le0.6");
				assertFind("when lt", "/RiskAssessment?probability=le0.7");
			}


			private void assertFind(String theMessage, String theUrl) {
				List<String> resourceIds = myTestDaoSearch.searchForIds(theUrl);
				assertThat(theMessage, resourceIds, hasItem(equalTo(myResourceId.getIdPart())));
			}

			private void assertNotFind(String theMessage, String theUrl) {
				List<String> resourceIds = myTestDaoSearch.searchForIds(theUrl);
				assertThat(theMessage, resourceIds, not(hasItem(equalTo(myResourceId.getIdPart()))));
			}
		}

		@Nested
		public class Sorting {
			@Test
			public void sortByNumeric() {
				String idAlpha7 = withRiskAssessmentWithProbabilty(0.7).getIdPart();
				String idAlpha2 = withRiskAssessmentWithProbabilty(0.2).getIdPart();
				String idAlpha5 = withRiskAssessmentWithProbabilty(0.5).getIdPart();

				List<String> allIds = myTestDaoSearch.searchForIds("/RiskAssessment?_sort=probability");
				assertThat(allIds, hasItems(idAlpha2, idAlpha5, idAlpha7));
			}

		}

	}

	@Nested
	public class QuantitySearch {
		IIdType myResourceId;

		private IIdType withObservationWithValueQuantity(double theValue) {
//			IBase quantity = myDataBuilder.withElementOfType("Quantity",
//				myDataBuilder.withPrimitiveAttribute("value", theValue),
//				myDataBuilder.withPrimitiveAttribute("unit", "mmHg"),
//				myDataBuilder.withPrimitiveAttribute("system", "http://unitsofmeasure.org"));
			myResourceId = myDataBuilder.createObservation(myDataBuilder.withElementAt("valueQuantity",
				myDataBuilder.withPrimitiveAttribute("value", theValue),
				myDataBuilder.withPrimitiveAttribute("unit", "mmHg"),
				myDataBuilder.withPrimitiveAttribute("system", "http://unitsofmeasure.org"),
				myDataBuilder.withPrimitiveAttribute("code", "mm[Hg]")
			));
			return myResourceId;
		}

		@Nested
		public class Queries {

			@Test
			public void eq() {
				withObservationWithValueQuantity(0.6);

				assertNotFind("when gt", "/Observation?value-quantity=0.5||mmHg");
				assertNotFind("when gt unitless", "/Observation?value-quantity=0.5");
				// TODO we break the spec here.  Default search should be approx
				// assertFind("when a little gt - default is approx", "/Observation?value-quantity=0.599");
				// assertFind("when a little lt - default is approx", "/Observation?value-quantity=0.601");
				// TODO we don't seem to support "units", only "code".
				assertFind("when eq with units", "/Observation?value-quantity=0.6||mm[Hg]");
				assertFind("when eq unitless", "/Observation?value-quantity=0.6");
				assertNotFind("when lt", "/Observation?value-quantity=0.7||mmHg");
				assertNotFind("when lt", "/Observation?value-quantity=0.7");
			}

			@Test
			public void ne() {
				withObservationWithValueQuantity(0.6);

				assertFind("when gt", "/Observation?value-quantity=ne0.5");
				assertNotFind("when eq", "/Observation?value-quantity=ne0.6");
				assertFind("when lt", "/Observation?value-quantity=ne0.7");
			}

			@Test
			public void ap() {
				withObservationWithValueQuantity(0.6);

				assertNotFind("when gt", "/Observation?value-quantity=ap0.5");
				assertFind("when a little gt", "/Observation?value-quantity=ap0.58");
				assertFind("when eq", "/Observation?value-quantity=ap0.6");
				assertFind("when a little lt", "/Observation?value-quantity=ap0.62");
				assertNotFind("when lt", "/Observation?value-quantity=ap0.7");
			}

			@Test
			public void gt() {
				withObservationWithValueQuantity(0.6);

				assertFind("when gt", "/Observation?value-quantity=gt0.5");
				assertNotFind("when eq", "/Observation?value-quantity=gt0.6");
				assertNotFind("when lt", "/Observation?value-quantity=gt0.7");

			}

			@Test
			public void ge() {
				withObservationWithValueQuantity(0.6);

				assertFind("when gt", "/Observation?value-quantity=ge0.5");
				assertFind("when eq", "/Observation?value-quantity=ge0.6");
				assertNotFind("when lt", "/Observation?value-quantity=ge0.7");
			}

			@Test
			public void lt() {
				withObservationWithValueQuantity(0.6);

				assertNotFind("when gt", "/Observation?value-quantity=lt0.5");
				assertNotFind("when eq", "/Observation?value-quantity=lt0.6");
				assertFind("when lt", "/Observation?value-quantity=lt0.7");

			}

			@Test
			public void le() {
				withObservationWithValueQuantity(0.6);

				assertNotFind("when gt", "/Observation?value-quantity=le0.5");
				assertFind("when eq", "/Observation?value-quantity=le0.6");
				assertFind("when lt", "/Observation?value-quantity=le0.7");
			}


			private void assertFind(String theMessage, String theUrl) {
				List<String> resourceIds = myTestDaoSearch.searchForIds(theUrl);
				assertThat(theMessage, resourceIds, hasItem(equalTo(myResourceId.getIdPart())));
			}

			private void assertNotFind(String theMessage, String theUrl) {
				List<String> resourceIds = myTestDaoSearch.searchForIds(theUrl);
				assertThat(theMessage, resourceIds, not(hasItem(equalTo(myResourceId.getIdPart()))));
			}
		}

		@Nested
		public class Sorting {
			@Test
			public void sortByNumeric() {
				String idAlpha7 = withObservationWithValueQuantity(0.7).getIdPart();
				String idAlpha2 = withObservationWithValueQuantity(0.2).getIdPart();
				String idAlpha5 = withObservationWithValueQuantity(0.5).getIdPart();

				List<String> allIds = myTestDaoSearch.searchForIds("/Observation?_sort=value-quantity");
				assertThat(allIds, hasItems(idAlpha2, idAlpha5, idAlpha7));
			}
		}

	}

}
