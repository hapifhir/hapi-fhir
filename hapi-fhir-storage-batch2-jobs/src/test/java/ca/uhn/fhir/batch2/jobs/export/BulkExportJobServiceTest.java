package ca.uhn.fhir.batch2.jobs.export;

import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class BulkExportJobServiceTest {

	@ParameterizedTest
	@CsvSource(
		value = {
			// requestPath | completeUrl | expectedRelativeUrl
			"$export|http://localhost:8080/fhir/$export|/$export",
			"$export|http://localhost:8080/fhir/$export?_type=Patient|/$export?_type=Patient",
			"$export|http://localhost:8080/fhir/$export?_type=Patient,Observation&_since=2026-08-24T11:41:35.845Z|/$export?_type=Patient,Observation&_since=2026-08-24T11:41:35.845Z",
			"%24export|http://localhost:8080/fhir/%24export|/%24export",
			"%24export|http://localhost:8080/fhir/%24export?_type=Patient|/%24export?_type=Patient",
			"Patient/$export|http://localhost:8080/fhir/Patient/$export|/Patient/$export",
			"Patient/$export|http://localhost:8080/fhir/Patient/$export?_type=Observation|/Patient/$export?_type=Observation",
			"Patient/123/$export|http://localhost:8080/fhir/Patient/123/$export|/Patient/123/$export",
			"Patient/123/%24export|http://localhost:8080/fhir/Patient/123/%24export?_type=Observation|/Patient/123/%24export?_type=Observation",
			"Group/abc/$export|http://localhost:8080/fhir/Group/abc/$export?_type=Patient&_elements=id|/Group/abc/$export?_type=Patient&_elements=id",
			"Group/abc/%24export|http://localhost:8080/fhir/Group/abc/%24export|/Group/abc/%24export",
			// Server address override: completeUrl host differs from the externally-facing base, but requestPath is base-independent
			"$export|https://internal.example.com:9443/fhir/$export?_type=Patient|/$export?_type=Patient",
			// Tenant / extra base path segments are excluded because requestPath already excludes the server base
			"$export|http://localhost:8080/fhir/DEFAULT/$export?_type=Patient|/$export?_type=Patient",
			// Encoded characters in the query string are preserved verbatim
			"Patient/$export|http://localhost:8080/fhir/Patient/$export?_typeFilter=Patient%3Factive%3Dtrue|/Patient/$export?_typeFilter=Patient%3Factive%3Dtrue"
		},
		delimiter = '|')
	void getRequestUrlRelativeToServerBaseReturnsOperationPathWithQuery(
			String theRequestPath, String theCompleteUrl, String theExpectedRelativeUrl) {
		// given
		ServletRequestDetails requestDetails = new ServletRequestDetails();
		requestDetails.setRequestPath(theRequestPath);
		requestDetails.setCompleteUrl(theCompleteUrl);

		// when
		String actual = BulkExportJobService.getRequestUrlRelativeToServerBase(requestDetails);

		// then
		assertThat(actual).isEqualTo(theExpectedRelativeUrl);
	}

	@Test
	void getRequestUrlRelativeToServerBaseWhenCompleteUrlNullReturnsPathWithoutQuery() {
		// given
		ServletRequestDetails requestDetails = new ServletRequestDetails();
		requestDetails.setRequestPath("$export");

		// when
		String actual = BulkExportJobService.getRequestUrlRelativeToServerBase(requestDetails);

		// then
		assertThat(actual).isEqualTo("/$export");
	}

	@Test
	void getRequestUrlRelativeToServerBaseWhenCompleteUrlHasNoQueryReturnsPathOnly() {
		// given
		ServletRequestDetails requestDetails = new ServletRequestDetails();
		requestDetails.setRequestPath("Group/abc/$export");
		requestDetails.setCompleteUrl("http://localhost:8080/fhir/Group/abc/$export");

		// when
		String actual = BulkExportJobService.getRequestUrlRelativeToServerBase(requestDetails);

		// then
		assertThat(actual).isEqualTo("/Group/abc/$export");
	}
}
