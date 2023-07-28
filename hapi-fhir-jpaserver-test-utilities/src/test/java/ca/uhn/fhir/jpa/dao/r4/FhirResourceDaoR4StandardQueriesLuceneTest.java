package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.TestDaoSearch;
import ca.uhn.fhir.jpa.search.CompositeSearchParameterTestCases;
import ca.uhn.fhir.jpa.search.QuantitySearchParameterTestCases;
import ca.uhn.fhir.jpa.search.BaseSourceSearchParameterTestCases;
import ca.uhn.fhir.jpa.test.BaseJpaTest;
import ca.uhn.fhir.jpa.test.config.TestR4Config;
import ca.uhn.fhir.storage.test.BaseDateSearchDaoTests;
import ca.uhn.fhir.storage.test.DaoTestDataBuilder;
import org.hl7.fhir.r4.model.Observation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
	TestR4Config.class,
	DaoTestDataBuilder.Config.class,
	TestDaoSearch.Config.class
})
public class FhirResourceDaoR4StandardQueriesLuceneTest extends BaseJpaTest {
	FhirContext myFhirContext = FhirContext.forR4Cached();
	@Autowired
	PlatformTransactionManager myTxManager;
	@RegisterExtension
	@Autowired
	DaoTestDataBuilder myDataBuilder;
	@Autowired
	TestDaoSearch myTestDaoSearch;
	@Autowired
	@Qualifier("myObservationDaoR4")
	IFhirResourceDao<Observation> myObservationDao;

	// todo mb create an extension to restore via clone or xstream + BeanUtils.copyProperties().
	@BeforeEach
	void setUp() {
		myStorageSettings.setAdvancedHSearchIndexing(true);
	}

	@AfterEach
	void tearDown() {
		JpaStorageSettings defaultConfig = new JpaStorageSettings();
		myStorageSettings.setAdvancedHSearchIndexing(defaultConfig.isAdvancedHSearchIndexing());
	}

	@Override
	protected FhirContext getFhirContext() {
		return myFhirContext;
	}

	@Override
	protected PlatformTransactionManager getTxManager() {
		return myTxManager;
	}

	@Nested
	public class DateSearchTests extends BaseDateSearchDaoTests {
		@Override
		protected Fixture constructFixture() {
			return new TestDataBuilderFixture<>(myDataBuilder, myObservationDao);
		}
	}

	@Nested
	class QuantityAndNormalizedQuantitySearch extends QuantitySearchParameterTestCases {
		QuantityAndNormalizedQuantitySearch() {
			super(myDataBuilder, myTestDaoSearch, myStorageSettings);
		}
	}

	@Nested
	class CompositeSearch extends CompositeSearchParameterTestCases {
		CompositeSearch() {
			super(myDataBuilder, myTestDaoSearch);
		}

		/** JPA doesn't know which sub-element matches */
		@Override
		protected boolean isCorrelatedSupported() {
			return true;
		}
	}

	@Nested
	class SourceSearchParameterTestCases extends BaseSourceSearchParameterTestCases {
		SourceSearchParameterTestCases() {
			super(myDataBuilder, myTestDaoSearch, myStorageSettings);
		}

		@Override
		protected boolean isRequestIdSupported() {
			return false;
		}
	}

}
