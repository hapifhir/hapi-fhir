package ca.uhn.fhir.jpa.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.ReadPartitionIdRequestDetails;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.extractor.ISearchParamExtractor;
import ca.uhn.fhir.jpa.searchparam.extractor.SearchParamExtractorR4;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.FhirContextSearchParamRegistry;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class PatientIdPartitionInterceptorTest {

	private final FhirContext myFhirContext = FhirContext.forR4Cached();
	private PatientIdPartitionInterceptor mySvc;

	@Mock
	private DaoRegistry myDaoRegistry;

	@BeforeEach
	void beforeEach() {
		ISearchParamRegistry searchParamRegistry = new FhirContextSearchParamRegistry(myFhirContext);
		StorageSettings storageSettings = new StorageSettings();
		PartitionSettings partitionSettings = new PartitionSettings();
		ISearchParamExtractor searchParamExtractor = new SearchParamExtractorR4(storageSettings, partitionSettings, myFhirContext, searchParamRegistry);
		mySvc = new PatientIdPartitionInterceptor(myFhirContext, searchParamExtractor, partitionSettings, myDaoRegistry);
	}

	@ParameterizedTest
	@ValueSource(strings = {
		"123",
		"Patient/123"
	})
	void testSearch_ValidCompartment(String theValue) {
		// Test
		SearchParameterMap params = new SearchParameterMap();
		params.add("patient", new ReferenceParam(theValue));
		ReadPartitionIdRequestDetails readDetails = ReadPartitionIdRequestDetails.forSearchType("Observation", params, null);

		RequestPartitionId actual = mySvc.identifyForRead(readDetails, new ServletRequestDetails());

		// Verify
		assertFalse(actual.isAllPartitions());
	}

	@ParameterizedTest
	@CsvSource(textBlock = """
		patient , http://foo/fhir/NamingSystem/id-pathhosp-mrn|123
		subject , http://foo/fhir/NamingSystem/id-pathhosp-mrn|123
		patient , http://patient|123
		subject , http://patient|123
		patient , 123|456
		subject , 123|456
		""")
	void testSearch_InvalidParameter(String theParamName, String theParamValue) {
		// Test
		SearchParameterMap params = new SearchParameterMap();
		params.add(theParamName, new ReferenceParam(theParamValue));
		ReadPartitionIdRequestDetails readDetails = ReadPartitionIdRequestDetails.forSearchType("Encounter", params, null);

		RequestPartitionId actual = mySvc.identifyForRead(readDetails, new ServletRequestDetails());

		// Verify
		assertTrue(actual.isAllPartitions());
	}


}
