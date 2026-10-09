package ca.uhn.fhir.jpa.bulk;

import ca.uhn.fhir.jpa.interceptor.PatientIdPartitionInterceptor;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.searchparam.extractor.ISearchParamExtractor;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.test.utilities.HttpTestRequest;
import ca.uhn.fhir.test.utilities.HttpTestResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


import static org.assertj.core.api.Assertions.assertThat;

public class BulkExportWithPatientIdPartitioningTest extends BaseResourceProviderR4Test {

	@Autowired
	private ISearchParamExtractor mySearchParamExtractor;

	private PatientIdPartitionInterceptor myPatientIdPartitionInterceptor;

	@BeforeEach
	@Override
	public void before() {
		myPatientIdPartitionInterceptor = new PatientIdPartitionInterceptor(getFhirContext(), mySearchParamExtractor, myPartitionSettings, myDaoRegistry, myTransactionBundleNormalizer);
		myInterceptorRegistry.registerInterceptor(myPatientIdPartitionInterceptor);
		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionSettings.setUnnamedPartitionMode(true);
	}

	@AfterEach
	@Override
	public void after() {
		myInterceptorRegistry.unregisterInterceptor(myPatientIdPartitionInterceptor);
		myPartitionSettings.setPartitioningEnabled(new PartitionSettings().isPartitioningEnabled());
		myPartitionSettings.setUnnamedPartitionMode(new PartitionSettings().isUnnamedPartitionMode());
	}

	@Test
	public void testSystemBulkExport_withResourceType_success() {
		HttpTestResponse postResponse = myServer.fhirRequest("/" + ProviderConstants.OPERATION_EXPORT)
			.withHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC)
			.withHeader(JpaConstants.PARAM_EXPORT_TYPE, "Patient")
			.withHeader(JpaConstants.PARAM_EXPORT_TYPE_FILTER, "Patient?")
			.method("POST")
			.assertStatus(202);
		assertThat(postResponse.getReasonPhrase()).isEqualTo("Accepted");
	}

	@Test
	public void testSystemBulkExport_withResourceType_pollSuccessful() {
		HttpTestResponse postResponse = myServer.fhirRequest("/" + ProviderConstants.OPERATION_EXPORT)
			.withHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC)
			.withHeader(JpaConstants.PARAM_EXPORT_TYPE, "Patient") // ignored when computing partition
			.withHeader(JpaConstants.PARAM_EXPORT_TYPE_FILTER, "Patient?")
			.method("POST")
			.assertStatus(202);
		assertThat(postResponse.getReasonPhrase()).isEqualTo("Accepted");

		String locationUrl = postResponse.getHeader(Constants.HEADER_CONTENT_LOCATION);
		assertThat(locationUrl).isNotNull();

		HttpTestRequest.to(myServer.getHttpClient(), locationUrl).get().assertStatus(202);
	}
}
