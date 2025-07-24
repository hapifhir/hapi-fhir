package ca.uhn.fhir.batch2.jobs.export;

import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.jpa.api.model.BulkExportJobResults;
import ca.uhn.fhir.jpa.bulk.export.model.BulkExportResponseJson;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.JsonUtil;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.StringType;
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
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

	@Test
	void parsePatientParamsWhenPatientIsIIdType() {
		// given
		IIdType idType = new IdType("Patient/123");
		List<IBase> input = List.of(idType);
		// when
		List<IPrimitiveType<String>> result = BulkDataExportProvider.parsePatientList(input);
		// then
		assertEquals(1, result.size());
		assertSame(idType, result.get(0));
	}

	@Test
	void parsePatientListWhenParameterValueIsStringType() {
		// given
		StringType stringType = new StringType("abc");
		Parameters.ParametersParameterComponent param = new Parameters.ParametersParameterComponent();
		param.setValue(stringType);
		List<IBase> input = List.of(param);
		// when
		List<IPrimitiveType<String>> result = BulkDataExportProvider.parsePatientList(input);
		// then
		assertEquals(1, result.size());
		assertSame(stringType, result.get(0));
	}

	@Test
	void parsePatientListWhenParameterValueIsReference() {
		// given
		Reference reference = new Reference("Patient/456");
		Parameters.ParametersParameterComponent param = new Parameters.ParametersParameterComponent();
		param.setValue(reference);
		List<IBase> input = List.of(param);
		// when
		List<IPrimitiveType<String>> result = BulkDataExportProvider.parsePatientList(input);
		// then
		assertEquals(1, result.size());
		assertEquals("Patient/456", result.get(0).getValue());
	}

	@Test
	void parsePatientListThrowsExceptionForInvalidParameterType() {
		// given
		Parameters.ParametersParameterComponent param = new Parameters.ParametersParameterComponent();
		param.setValue(new DateType());
		List<IBase> input = List.of(param);
		// when/then
		assertThrows(InvalidRequestException.class, () -> BulkDataExportProvider.parsePatientList(input));
	}


}
