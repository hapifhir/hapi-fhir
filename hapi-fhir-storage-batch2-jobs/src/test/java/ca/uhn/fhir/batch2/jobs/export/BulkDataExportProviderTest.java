package ca.uhn.fhir.batch2.jobs.export;

import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.jpa.api.model.BulkExportJobResults;
import ca.uhn.fhir.jpa.bulk.export.model.BulkExportResponseJson;
import ca.uhn.fhir.util.JsonUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class BulkDataExportProviderTest {
	private static Stream<Arguments> fhirContexts() {
			return Stream.of(
				Arguments.arguments(FhirContext.forDstu2()),
				Arguments.arguments(FhirContext.forDstu2Cached()),
				Arguments.arguments(FhirContext.forDstu2Hl7Org()),
				Arguments.arguments(FhirContext.forDstu2Hl7OrgCached()),
				Arguments.arguments(FhirContext.forDstu3()),
				Arguments.arguments(FhirContext.forDstu3Cached()),
				Arguments.arguments(FhirContext.forR4()),
				Arguments.arguments(FhirContext.forR4Cached()),
				Arguments.arguments(FhirContext.forR4B()),
				Arguments.arguments(FhirContext.forR4BCached()),
				Arguments.arguments(FhirContext.forR5()),
				Arguments.arguments(FhirContext.forR5Cached())
			);
		}

	@ParameterizedTest
	@MethodSource("fhirContexts")
	void checkDeviceIsSupportedInPatientCompartment(FhirContext theFhirContext) {
		Set<String> resourceNames = new BulkDataExportProvider().getPatientCompartmentResources(theFhirContext);
		if (theFhirContext.getVersion().getVersion().isOlderThan(FhirVersionEnum.R5)) {
			assertThat(resourceNames).contains("Device");
		} else {
			assertThat(resourceNames).doesNotContain("Device");
		}
	}

	@Test
	void testCompleteStatusDocument() {
	    // given
		JobInstance job = new JobInstance();

		Date now = new Date();
		Date start = new Date(now.getTime() - 10000);
		Date end = new Date(now.getTime() - 2000);
		job.setStartTime(start);
		job.setEndTime(end);

		String reportMessage = "Report Message";
		BulkExportJobResults jobResults = new BulkExportJobResults();
		jobResults.setReportMsg(reportMessage);
		jobResults.setOriginalRequestUrl("http://example.com/fhir-endpoint/Group/123/$export");
		jobResults.setResourceTypeToBinaryIds(Map.of("Patient", List.of("Binary/1123", "Binary/1124")));

		job.setReport(JsonUtil.serialize(jobResults));

	    // when
		BulkExportResponseJson response = BulkDataExportProvider.buildCompleteResponseDocument("http://example.com/fhir-endpoint", job);

	    // then
		// see https://hl7.org/fhir/uv/bulkdata/export.html#response---complete-status
	    assertEquals(start, response.getTransactionTime(), "transactionTime should be useful as the _since param in the next $export request");
	    assertEquals(reportMessage, response.getMsg());
		assertEquals(true, response.getRequiresAccessToken());
		assertEquals(jobResults.getOriginalRequestUrl(), response.getRequest());
		assertEquals(2, response.getOutput().size());
		assertEquals(new BulkExportResponseJson.Output().setType("Patient").setUrl("http://example.com/fhir-endpoint/Binary/1123"), response.getOutput().get(0));

	}

}
