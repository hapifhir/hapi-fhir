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
		@Test
		void testChainedSortWorks() {
			// given
			final IdType praId1 = createPractitioner("pra1", "C_Family");
			final IdType praId2 = createPractitioner("pra2", "A_Family");
			final IdType praId3 = createPractitioner("pra3", "B_Family");

			final String baseUrl = praId1.getBaseUrl();
			final String stringValue = praId1.asStringValue();
			final IdType praRoleId1 = createPractitionerRole("praRole1", praId1);
			final IdType praRoleId2 = createPractitionerRole("praRole2", praId2);
			final IdType praRoleId3 = createPractitionerRole("praRole3", praId3);

			// when
			myTestDaoSearch.assertSearchFindsInOrder("chain works", "PractitionerRole?_sort=practitioner.family", praRoleId2.getIdPart(), praRoleId3.getIdPart(), praRoleId1.getIdPart());
		}

		@Test
		void testChainedSortCombinedWithFullTextIsRefused() {
			// given
			final IdType praId1 = createPractitioner("pra1", "C_Family");
			final IdType praId2 = createPractitioner("pra2", "A_Family");
			final IdType praId3 = createPractitioner("pra3", "B_Family");

			final IdType praRoleId1 = createPractitionerRole("praRole1", praId1);
			final IdType praRoleId2 = createPractitionerRole("praRole2", praId2);
			final IdType praRoleId3 = createPractitionerRole("praRole3", praId3);

			// when
			// LUKETODO:  figure out which code SHOULD throw this Exception
			assertThrows(IllegalArgumentException.class, ()-> myTestDaoSearch.searchForResources( "PractitionerRole?_text=blahblah&_sort=practitioner.family"));
		}

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
