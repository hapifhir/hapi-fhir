package ca.uhn.fhir.rest.server.tenant;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.HapiLocalizer;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.IRestfulServerDefaults;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.UrlPathTokenizer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UrlBaseTenantIdentificationStrategyTest {

	private final static String BASE_URL = "http://localhost:8888";

	@Mock
	private RequestDetails myRequestDetails;
	private static SystemRequestDetails ourSystemRequestDetails;
	@Mock
	private IRestfulServerDefaults myRestfulServerDefaults;
	@Mock
	private FhirContext myFHIRContext;
	@Mock
	private HapiLocalizer myHapiLocalizer;

	private static UrlBaseTenantIdentificationStrategy ourTenantStrategy;
	private UrlPathTokenizer myUrlTokenizer;

	@BeforeAll
	static void setup() {
		ourSystemRequestDetails = new SystemRequestDetails();
		ourTenantStrategy = new UrlBaseTenantIdentificationStrategy();
	}

	@Test
	void massageBaseUrl_givenBaseUrlAndTenant_shouldApplyTenant() {
		//given a tenant id of TENANT1
		when(myRequestDetails.getTenantId()).thenReturn("TENANT1");

		//when we massage the server base url
		String actual = ourTenantStrategy.massageServerBaseUrl(BASE_URL, myRequestDetails);

		//then we should see /TENANT1 in the url
		assertEquals(BASE_URL + "/TENANT1", actual);
	}

	@Test
	void massageBaseUrl_givenBaseUrlAndNullTenant_shouldReturnBaseUrl() {
		//given a null tenant id
		when(myRequestDetails.getTenantId()).thenReturn(null);

		//when we massage our base url
		String actual = ourTenantStrategy.massageServerBaseUrl(BASE_URL, myRequestDetails);

		//then nothing should happen
		assertEquals(BASE_URL, actual);
	}

	@CsvSource(value = {
			"						, 							, empty input url - empty URL should be returned",
			"TENANT1/Patient/123	, TENANT1/Patient/123		, tenant ID already exists - input URL should be returned",
			"TENANT1/Patient/$export, TENANT1/Patient/$export	, tenant ID already exists - input URL should be returned",
			"TENANT2/Patient/123	, TENANT2/Patient/123		, requestDetails contains different tenant ID - input URL should be returned",
			"TENANT2/$export		, TENANT2/$export			, requestDetails contains different tenant ID - input URL should be returned",
			"Patient/123			, TENANT1/Patient/123		, url starts with resource type - tenant ID should be added to URL",
			"Patient/$export		, TENANT1/Patient/$export	, url starts with resource type - tenant ID should be added to URL",
			"$export				, TENANT1/$export			, url starts with operation name - tenant ID should be added to URL",
	})
	@ParameterizedTest
	void resolveRelativeUrl_returnsCorrectlyResolvedUrl(String theInputUrl, String theExpectedResolvedUrl, String theMessage) {
		lenient().when(myRequestDetails.getTenantId()).thenReturn("TENANT1");
		lenient().when(myFHIRContext.getResourceTypes()).thenReturn(Collections.singleton("Patient"));
		lenient().when(myRequestDetails.getFhirContext()).thenReturn(myFHIRContext);

		String actual = ourTenantStrategy.resolveRelativeUrl(theInputUrl, myRequestDetails);

		assertThat(actual).as(theMessage).isEqualTo(theExpectedResolvedUrl);
	}

	@Test
	void extractTenant_givenNormalRequestAndExplicitTenant_shouldUseTenant() {
		//given a Patient request on MYTENANT
		myUrlTokenizer = new UrlPathTokenizer("MYTENANT/Patient");

		//when we extract the tenant identifier
		ourTenantStrategy.extractTenant(myUrlTokenizer, myRequestDetails);

		//then we should see MYTENANT
		verify(myRequestDetails, times(1)).setTenantId("MYTENANT");
	}

	@Test
	void extractTenant_givenSystemRequestWithNoTenant_shouldUseDefault() {
		//given any request that starts with $ and no given partition name
		myUrlTokenizer = new UrlPathTokenizer("$partition-management-create-partition");

		//when we try to extract the tenant id
		ourTenantStrategy.extractTenant(myUrlTokenizer, ourSystemRequestDetails);

		//then we should see that it defaulted to the DEFAULT partition
		assertEquals("DEFAULT", ourSystemRequestDetails.getTenantId());
	}

	@Test
	void extractTenant_givenSystemRequestWithExplicitTenant_shouldUseTenant() {
		//given a request that starts with $ on a named partition
		myUrlTokenizer = new UrlPathTokenizer("MYTENANT/$partition-management-create-partition");

		//when we extract the tenant from the request
		ourTenantStrategy.extractTenant(myUrlTokenizer, ourSystemRequestDetails);

		//then we should see MYTENANT
		assertEquals("MYTENANT", ourSystemRequestDetails.getTenantId());
	}

	@Test
	void extractTenant_givenMetadataRequestWithNoTenant_shouldUseDefault() {
		//given a metadata request with no specified partition name
		myUrlTokenizer = new UrlPathTokenizer("metadata");

		//when we try to extract the tenant from the request
		ourTenantStrategy.extractTenant(myUrlTokenizer, myRequestDetails);

		//then we should see that it defaulted to the DEFAULT partition
		verify(myRequestDetails, times(1)).setTenantId("DEFAULT");
	}

	@Test
	void extractTenant_givenMetadataRequestWithExplicitTenant_shouldUseTenant() {
		//given a metadata request on a named partition
		myUrlTokenizer = new UrlPathTokenizer("MYTENANT/metadata");

		//when we extract the tenant id
		ourTenantStrategy.extractTenant(myUrlTokenizer, myRequestDetails);

		//then we should see MYTENANT
		verify(myRequestDetails, times(1)).setTenantId("MYTENANT");
	}

	@Test
	void extractTenant_givenPatientRequestAndNoTenant_shouldInterpretPatientAsPartition() {
		//given a Patient request with no partition name specified
		myUrlTokenizer = new UrlPathTokenizer("Patient");

		//when we try to extract the tenant from the request
		ourTenantStrategy.extractTenant(myUrlTokenizer, myRequestDetails);

		//then we should see that it interpreted Patient as the partition name
		verify(myRequestDetails, times(1)).setTenantId("Patient");
	}

	@Test
	void extractTenant_givenEmptyURLNoPartition_shouldThrowException() {
		//given an empty URL with no partition name
		when(myRequestDetails.getServer()).thenReturn(myRestfulServerDefaults);
		when(myRestfulServerDefaults.getFhirContext()).thenReturn(myFHIRContext);
		when(myFHIRContext.getLocalizer()).thenReturn(myHapiLocalizer);
		myUrlTokenizer = new UrlPathTokenizer("");

		//when we try to extract the tenant from the request
		InvalidRequestException ire = assertThrows(InvalidRequestException.class, () -> {
			ourTenantStrategy.extractTenant(myUrlTokenizer, myRequestDetails);
		});

		//then we should see an exception thrown with HAPI-0307 in it
		verify(myHapiLocalizer, times(1)).getMessage(RestfulServer.class, "rootRequest.multitenant");
		assertThat(ire.getMessage()).contains("HAPI-0307");
	}

	@Test
	void extractTenant_givenSystemRequestWithEmptyUrl_shouldUseDefaultPartition() {
		//given a system request with a blank url (is this even a valid test case?)
		myUrlTokenizer = new UrlPathTokenizer("");

		//when we try to extract the tenant id
		ourTenantStrategy.extractTenant(myUrlTokenizer, ourSystemRequestDetails);

		//then we should see that it defaulted to the DEFAULT partition
		assertEquals("DEFAULT", ourSystemRequestDetails.getTenantId());
	}
}
