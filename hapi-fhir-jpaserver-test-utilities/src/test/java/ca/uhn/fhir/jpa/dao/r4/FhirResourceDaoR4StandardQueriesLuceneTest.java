package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.TestDaoSearch;
import ca.uhn.fhir.jpa.search.BaseSourceSearchParameterTestCases;
import ca.uhn.fhir.jpa.search.CompositeSearchParameterTestCases;
import ca.uhn.fhir.jpa.search.QuantitySearchParameterTestCases;
import ca.uhn.fhir.jpa.test.BaseJpaTest;
import ca.uhn.fhir.jpa.test.config.TestR4Config;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.storage.test.BaseDateSearchDaoTests;
import ca.uhn.fhir.storage.test.DaoTestDataBuilder;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.PractitionerRole;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

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
	@Autowired
	IFhirResourceDao<Practitioner> myPractitionerDao;
	@Autowired
	IFhirResourceDao<PractitionerRole> myPractitionerRoleDao;

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

	@Nested
	class ChainedSort {
		private IdType myPraId1;
		private IdType myPraId2;
		private IdType myPraId3;
		private IdType myPraRoleId1;
		private IdType myPraRoleId2;
		private IdType myPraRoleId3;

		@BeforeEach
		void beforeEach() {
			myPraId1 = createPractitioner("pra1", "C_Family");
			myPraId2 = createPractitioner("pra2", "A_Family");
			myPraId3 = createPractitioner("pra3", "B_Family");

			myPraRoleId1 = createPractitionerRole("praRole1", myPraId1);
			myPraRoleId2 = createPractitionerRole("praRole2", myPraId2);
			myPraRoleId3 = createPractitionerRole("praRole3", myPraId3);
		}

		@Test
		void testRegularSortAscendingWorks() {
			myTestDaoSearch.assertSearchFindsInOrder("direct ascending sort works", "Practitioner?_sort=family", myPraId2.getIdPart(), myPraId3.getIdPart(), myPraId1.getIdPart());
		}

		@Test
		void testRegularSortDescendingWorks() {
			myTestDaoSearch.assertSearchFindsInOrder("direct descending sort works", "Practitioner?_sort=-family", myPraId1.getIdPart(), myPraId3.getIdPart(), myPraId2.getIdPart());
		}

		@Test
		void testChainedSortWorks() {
			myTestDaoSearch.assertSearchFindsInOrder("chain works", "PractitionerRole?_sort=practitioner.family", myPraRoleId2.getIdPart(), myPraRoleId3.getIdPart(), myPraRoleId1.getIdPart());
		}

		// TestDaoSearch doesn't seem to work when using "_text:

		private IdType createPractitioner(String theId, String theFamilyName) {
			final Practitioner practitioner = (Practitioner) new Practitioner()
				.setActive(true)
				.setName(List.of(new HumanName().setFamily(theFamilyName)))
				.setId(theId);

			myPractitionerDao.update(practitioner, new SystemRequestDetails());

			return practitioner.getIdElement().toUnqualifiedVersionless();
		}

		private IdType createPractitionerRole(String theId, IdType thePractitionerId) {
			final PractitionerRole practitionerRole = (PractitionerRole) new PractitionerRole()
				.setActive(true)
				.setPractitioner(new Reference(thePractitionerId.asStringValue()))
				.setId(theId);

			myPractitionerRoleDao.update(practitionerRole, new SystemRequestDetails());

			return practitionerRole.getIdElement().toUnqualifiedVersionless();
		}
	}
}
