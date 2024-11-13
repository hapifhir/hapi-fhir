package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.dao.TestDaoSearch;
import ca.uhn.fhir.jpa.test.BaseJpaTest;
import ca.uhn.fhir.jpa.test.config.TestHSearchAddInConfig;
import ca.uhn.fhir.jpa.test.config.TestR4Config;
import ca.uhn.fhir.jpa.util.SqlQuery;
import ca.uhn.fhir.jpa.util.SqlQueryList;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.storage.test.DaoTestDataBuilder;
import ch.qos.logback.classic.Level;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Meta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Sandbox for implementing queries.
 * This will NOT run during the build - use this class as a convenient
 * place to explore, debug, profile, and optimize.
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
	TestR4Config.class,
	TestHSearchAddInConfig.NoFT.class,
	DaoTestDataBuilder.Config.class,
	TestDaoSearch.Config.class,
	FhirResourceDaoR4QuerySandbox.FetchLoader.class
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@TestExecutionListeners(listeners = {
	DependencyInjectionTestExecutionListener.class
	, FhirResourceDaoR4QuerySandbox.TestDirtiesContextTestExecutionListener.class
})
public class FhirResourceDaoR4QuerySandbox extends BaseJpaTest {
	private static final Logger ourLog = LoggerFactory.getLogger(FhirResourceDaoR4QuerySandbox.class);

	@Configuration
	static class FetchLoader {
		@Bean
		public FetchSuppressInterceptor fetchSuppressInterceptor() {
			return new FetchSuppressInterceptor();
		}
	}

	@Autowired
	PlatformTransactionManager myTxManager;
	@Autowired
	FhirContext myFhirCtx;
	@RegisterExtension
	@Autowired
	DaoTestDataBuilder myDataBuilder;
	@Autowired
	TestDaoSearch myTestDaoSearch;
	@Autowired
	private IFhirSystemDao<Bundle, Meta> mySystemDaoR4;
	@Autowired
	FetchSuppressInterceptor myFetchSuppressInterceptor;

	@Override
	protected PlatformTransactionManager getTxManager() {
		return myTxManager;
	}

	@Override
	protected FhirContext getFhirContext() {
		return myFhirCtx;
	}

	List<String> myCapturedQueries = new ArrayList<>();
	@BeforeEach
	void registerLoggingInterceptor() {

		registerInterceptor(myFetchSuppressInterceptor);
		registerInterceptor(new Object(){
			@Hook(Pointcut.JPA_PERFTRACE_RAW_SQL)
			public void captureSql(RequestDetails theRequestDetails, SqlQueryList theQueries) {
				for (SqlQuery next : theQueries) {
					String output = next.getSql(true, true, true);
					ourLog.info("Query: {}", output);
					myCapturedQueries.add(output);
				}
			}
		});

	}

	@Test
	public void testSearches_logQueries() {
		myDataBuilder.createPatient();

		myTestDaoSearch.searchForIds("Patient?name=smith");

		assertThat(myCapturedQueries).isNotEmpty();
	}

	@Test
	void testQueryByPid() {

		// sentinel for over-match
		myDataBuilder.createPatient();

		String id = myDataBuilder.createPatient(
			myDataBuilder.withBirthdate("1971-01-01"),
			myDataBuilder.withActiveTrue(),
			myDataBuilder.withFamily("Smith")).getIdPart();

		myTestDaoSearch.assertSearchFindsOnly("search by server assigned id", "Patient?_pid=" + id, id);
	}

	@Test
	void testQueryByPid_withOtherSPAvoidsResourceTable() {
		// sentinel for over-match
		myDataBuilder.createPatient();

		String id = myDataBuilder.createPatient(
			myDataBuilder.withBirthdate("1971-01-01"),
			myDataBuilder.withActiveTrue(),
			myDataBuilder.withFamily("Smith")).getIdPart();

		myTestDaoSearch.assertSearchFindsOnly("search by server assigned id", "Patient?name=smith&_pid=" + id, id);
	}

	@Test
	void insertSlow_EOBBundle() {
	    // given
		Bundle linkTargetBundle = myFhirCtx.newJsonParser().parseResource(Bundle.class, """
{
  "resourceType": "Bundle",
  "type": "transaction",
  "entry": [
    {
      "resource": {
        "resourceType": "Patient",
        "id": "patient1",
        "name": [
          {
            "use": "official",
            "family": "Doe",
            "given": [
              "John"
            ]
          }
        ]
      },
      "request": {
        "method": "PUT",
        "url": "Patient/patient1"
      }
    },
    {
      "resource": {
        "resourceType": "Patient",
        "id": "patient2",
        "name": [
          {
            "use": "official",
            "family": "Smith",
            "given": [
              "Jane"
            ]
          }
        ]
      },
      "request": {
        "method": "PUT",
        "url": "Patient/patient2"
      }
    },
    {
      "resource": {
        "resourceType": "Patient",
        "id": "patient3",
        "name": [
          {
            "use": "official",
            "family": "Brown",
            "given": [
              "Alex"
            ]
          }
        ]
      },
      "request": {
        "method": "PUT",
        "url": "Patient/patient3"
      }
    },
    {
      "resource": {
        "resourceType": "Organization",
        "id": "provider1",
        "name": "Provider One"
      },
      "request": {
        "method": "PUT",
        "url": "Organization/provider1"
      }
    },
    {
      "resource": {
        "resourceType": "Organization",
        "id": "provider2",
        "name": "Provider Two"
      },
      "request": {
        "method": "PUT",
        "url": "Organization/provider2"
      }
    },
    {
      "resource": {
        "resourceType": "Organization",
        "id": "provider3",
        "name": "Provider Three"
      },
      "request": {
        "method": "PUT",
        "url": "Organization/provider3"
      }
    },
    {
      "resource": {
        "resourceType": "Coverage",
        "id": "coverage1",
        "beneficiary": {
          "reference": "Patient/patient1"
        },
        "payor": [
          {
            "reference": "Organization/provider1"
          }
        ]
      },
      "request": {
        "method": "PUT",
        "url": "Coverage/coverage1"
      }
    },
    {
      "resource": {
        "resourceType": "Coverage",
        "id": "coverage2",
        "beneficiary": {
          "reference": "Patient/patient2"
        },
        "payor": [
          {
            "reference": "Organization/provider2"
          }
        ]
      },
      "request": {
        "method": "PUT",
        "url": "Coverage/coverage2"
      }
    },
    {
      "resource": {
        "resourceType": "Coverage",
        "id": "coverage3",
        "beneficiary": {
          "reference": "Patient/patient3"
        },
        "payor": [
          {
            "reference": "Organization/provider3"
          }
        ]
      },
      "request": {
        "method": "PUT",
        "url": "Coverage/coverage3"
      }
    }
  ]
}
""");

		Bundle eobBundle = myFhirCtx.newJsonParser().parseResource(Bundle.class, """
			{
			  "resourceType": "Bundle",
			  "type": "transaction",
			  "entry": [
			    {
			      "resource": {
			        "resourceType": "ExplanationOfBenefit",
			        "id": "eob1",
			        "status": "active",
			        "type": {
			          "coding": [
			            {
			              "system": "http://terminology.hl7.org/CodeSystem/claim-type",
			              "code": "professional",
			              "display": "Professional"
			            }
			          ]
			        },
			        "patient": {
			          "reference": "Patient/patient1"
			        },
			        "provider": {
			          "reference": "Organization/provider1"
			        },
			        "insurance": [
			          {
			            "coverage": {
			              "reference": "Coverage/coverage1"
			            }
			          }
			        ]
			      },
			      "request": {
			        "method": "POST",
			        "url": "ExplanationOfBenefit"
			      }
			    },
			    {
			      "resource": {
			        "resourceType": "ExplanationOfBenefit",
			        "id": "eob2",
			        "status": "active",
			        "type": {
			          "coding": [
			            {
			              "system": "http://terminology.hl7.org/CodeSystem/claim-type",
			              "code": "institutional",
			              "display": "Institutional"
			            }
			          ]
			        },
			        "patient": {
			          "reference": "Patient/patient2"
			        },
			        "provider": {
			          "reference": "Organization/provider2"
			        },
			        "insurance": [
			          {
			            "coverage": {
			              "reference": "Coverage/coverage2"
			            }
			          }
			        ]
			      },
			      "request": {
			        "method": "POST",
			        "url": "ExplanationOfBenefit"
			      }
			    },
			    {
			      "resource": {
			        "resourceType": "ExplanationOfBenefit",
			        "id": "eob3",
			        "status": "active",
			        "type": {
			          "coding": [
			            {
			              "system": "http://terminology.hl7.org/CodeSystem/claim-type",
			              "code": "oral",
			              "display": "Oral"
			            }
			          ]
			        },
			        "patient": {
			          "reference": "Patient/patient3"
			        },
			        "provider": {
			          "reference": "Organization/provider3"
			        },
			        "insurance": [
			          {
			            "coverage": {
			              "reference": "Coverage/coverage3"
			            }
			          }
			        ]
			      },
			      "request": {
			        "method": "POST",
			        "url": "ExplanationOfBenefit"
			      }
			    }
			  ]
			}
			""");
		myCaptureQueriesListener.clear();
		ourLog.info("-------- target bundle ----------");
		ch.qos.logback.classic.Logger hibernateLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("org.hibernate.SQL");
		hibernateLogger.setLevel(Level.DEBUG);
		
		mySystemDaoR4.transaction(mySrd, linkTargetBundle);
		//myCaptureQueriesListener.logAllQueries();
		myCaptureQueriesListener.clear();

	    // when
		ourLog.info("-------- eob bundle ----------");
		var result = mySystemDaoR4.transaction(mySrd, eobBundle);

	    // then
//		myCaptureQueriesListener.logAllQueries();
	}


	@Test
	void testSortByPid() {

		String id1 = myDataBuilder.createPatient(myDataBuilder.withFamily("Smithy")).getIdPart();
		String id2 = myDataBuilder.createPatient(myDataBuilder.withFamily("Smithwick")).getIdPart();
		String id3 = myDataBuilder.createPatient(myDataBuilder.withFamily("Smith")).getIdPart();

		myTestDaoSearch.assertSearchFindsInOrder("sort by server assigned id", "Patient?family=smith&_sort=_pid", id1,id2,id3);
		myTestDaoSearch.assertSearchFindsInOrder("reverse sort by server assigned id", "Patient?family=smith&_sort=-_pid", id3,id2,id1);
	}

	@Test
	void testChainedSort() {
		final IIdType practitionerId = myDataBuilder.createPractitioner(myDataBuilder.withFamily("Jones"));

		final String id1 = myDataBuilder.createPatient(myDataBuilder.withFamily("Smithy")).getIdPart();
		final String id2 = myDataBuilder.createPatient(myDataBuilder.withFamily("Smithwick")).getIdPart();
		final String id3 = myDataBuilder.createPatient(
			myDataBuilder.withFamily("Smith"),
			myDataBuilder.withReference("generalPractitioner", practitionerId)).getIdPart();


		final IBundleProvider iBundleProvider = myTestDaoSearch.searchForBundleProvider("Patient?_total=ACCURATE&_sort=Practitioner:general-practitioner.family");
		assertEquals(3, iBundleProvider.size());

		final List<IBaseResource> allResources = iBundleProvider.getAllResources();
		assertEquals(3, iBundleProvider.size());
		assertEquals(3, allResources.size());

		final List<String> actualIds = allResources.stream().map(IBaseResource::getIdElement).map(IIdType::getIdPart).toList();
		assertTrue(actualIds.containsAll(List.of(id1, id2, id3)));
	}

	public static final class TestDirtiesContextTestExecutionListener extends DirtiesContextTestExecutionListener {

		@Override
		protected void beforeOrAfterTestClass(TestContext testContext, DirtiesContext.ClassMode requiredClassMode) throws Exception {
			if (!testContext.getTestClass().getName().contains("$")) {
				super.beforeOrAfterTestClass(testContext, requiredClassMode);
			}
		}
	}

}
