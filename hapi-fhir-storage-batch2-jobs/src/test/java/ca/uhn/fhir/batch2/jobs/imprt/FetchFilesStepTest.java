package ca.uhn.fhir.batch2.jobs.imprt;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.test.utilities.server.HttpServletExtension;
import ca.uhn.fhir.util.BundleBuilder;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ca.uhn.fhir.rest.api.Constants.CT_APP_NDJSON;
import static ca.uhn.fhir.rest.api.Constants.CT_FHIR_JSON;
import static ca.uhn.fhir.rest.api.Constants.CT_FHIR_JSON_NEW;
import static ca.uhn.fhir.rest.api.Constants.CT_FHIR_NDJSON;
import static ca.uhn.fhir.rest.api.Constants.CT_JSON;
import static ca.uhn.fhir.rest.api.Constants.CT_TEXT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class FetchFilesStepTest {

	private static final FhirContext ourCtx = FhirContext.forR4Cached();

	public static final String INSTANCE_ID = "instance-id";
	public static final JobInstance ourTestInstance = JobInstance.fromInstanceId(INSTANCE_ID);
	public static final String CHUNK_ID = "chunk-id";

	private final ContentTypeHeaderModifiableBulkImportFileServlet myBulkImportFileServlet = new ContentTypeHeaderModifiableBulkImportFileServlet();
	@RegisterExtension
	private final HttpServletExtension myHttpServletExtension = new HttpServletExtension()
		.withServlet(myBulkImportFileServlet);
	private final FetchFilesStep mySvc = new FetchFilesStep();

	@Mock
	private IJobDataSink<NdJsonFileJson> myJobDataSink;
	@Captor
	private ArgumentCaptor<NdJsonFileJson> myFileCaptorCaptor;

	@BeforeEach
	public void beforeEach() {
		mySvc.setFhirContext(ourCtx);
	}

	@ParameterizedTest
	@ValueSource(strings = {CT_FHIR_NDJSON, CT_FHIR_JSON, CT_FHIR_JSON_NEW, CT_APP_NDJSON, CT_JSON, CT_TEXT})
	public void testFetchWithBasicAuth(String theHeaderContentType) {

		// Setup
		myBulkImportFileServlet.setHeaderContentTypeValue(theHeaderContentType);
		String index = myBulkImportFileServlet.registerFileByContents("{\"resourceType\":\"Patient\"}");

		BulkImportJobParameters parameters = new BulkImportJobParameters()
			.addNdJsonUrl(myHttpServletExtension.getBaseUrl() + "/download?index=" + index)
			.setHttpBasicCredentials("admin:password");
		StepExecutionDetails<BulkImportJobParameters, VoidModel> details = new StepExecutionDetails<>(parameters, null, ourTestInstance, new WorkChunk().setId(CHUNK_ID));

		// Test

		mySvc.run(details, myJobDataSink);

		// Verify

		assertThat(myHttpServletExtension.getRequestHeaders()).hasSize(1);

		String expectedAuthHeader = "Authorization: Basic " + Base64.getEncoder().encodeToString("admin:password".getBytes(StandardCharsets.UTF_8));
		assertThat(myHttpServletExtension.getRequestHeaders().get(0)).as(myHttpServletExtension.toString()).contains(expectedAuthHeader);
	}

	@Test
	public void testFetchWithBasicAuth_SplitIntoBatches() {
		// Setup

		StringBuilder b = new StringBuilder();
		for (int i = 0; i < 10; i++) {
			b.append("{\"resourceType\":\"Patient\"}").append("\n");
		}
		String resource = b.toString();
		String index = myBulkImportFileServlet.registerFileByContents(resource);

		BulkImportJobParameters parameters = new BulkImportJobParameters()
			.addNdJsonUrl(myHttpServletExtension.getBaseUrl() + "/download?index=" + index)
			.setMaxBatchResourceCount(3);
		StepExecutionDetails<BulkImportJobParameters, VoidModel> details = new StepExecutionDetails<>(parameters, null, ourTestInstance, new WorkChunk().setId(CHUNK_ID));

		// Test

		mySvc.run(details, myJobDataSink);

		// Verify

		verify(myJobDataSink, times(4)).accept(any(NdJsonFileJson.class));

	}

	@Test
	public void testFetchWithBasicAuth_InvalidCredential() {

		// Setup

		String index = myBulkImportFileServlet.registerFileByContents("{\"resourceType\":\"Patient\"}");

		BulkImportJobParameters parameters = new BulkImportJobParameters()
			.addNdJsonUrl(myHttpServletExtension.getBaseUrl() + "/download?index=" + index)
			.setHttpBasicCredentials("admin");
		StepExecutionDetails<BulkImportJobParameters, VoidModel> details = new StepExecutionDetails<>(parameters, null, ourTestInstance, new WorkChunk().setId(CHUNK_ID));

		// Test & Verify

		assertThatExceptionOfType(JobExecutionFailedException.class).isThrownBy(() -> mySvc.run(details, myJobDataSink));
	}

	@Test
	public void testGroupByCompartmentName() {
		// Setup
		BundleBuilder bb = new BundleBuilder(ourCtx);
		Encounter encPatientANumber0 = new Encounter();
		encPatientANumber0.setId("Encounter/encPatientANumber0");
		encPatientANumber0.setSubject(new Reference("Patient/A"));
		bb.addTransactionUpdateEntry(encPatientANumber0);
		Encounter encPatientBNumber0 = new Encounter();
		encPatientBNumber0.setId("Encounter/encPatientBNumber0");
		encPatientBNumber0.setSubject(new Reference("Patient/B"));
		bb.addTransactionUpdateEntry(encPatientBNumber0);
		Encounter encPatientANumber1 = new Encounter();
		encPatientANumber1.setId("Encounter/encPatientANumber1");
		encPatientANumber1.setSubject(new Reference("Patient/A"));
		bb.addTransactionUpdateEntry(encPatientANumber1);
		Encounter encPatientBNumber1 = new Encounter();
		encPatientBNumber1.setId("Encounter/encPatientBNumber1");
		encPatientBNumber1.setSubject(new Reference("Patient/B"));
		bb.addTransactionUpdateEntry(encPatientBNumber1);
		IBaseBundle bundle = bb.getBundle();
		String ndJson = ourCtx.newNDJsonParser().setPrettyPrint(true).encodeResourceToString(bundle);
		String id = myBulkImportFileServlet.registerFileByContents(ndJson);

		BulkImportJobParameters parameters = new BulkImportJobParameters()
			.addNdJsonUrl(myHttpServletExtension.getBaseUrl() + "/download?index=" + id)
			.setMaxBatchResourceCount(2)
			.setGroupByCompartmentName("Patient");
		StepExecutionDetails<BulkImportJobParameters, VoidModel> details = new StepExecutionDetails<>(parameters, null, ourTestInstance, new WorkChunk().setId(CHUNK_ID));

		// Test

		mySvc.run(details, myJobDataSink);

		// Verify

		verify(myJobDataSink, times(2)).accept(myFileCaptorCaptor.capture());
		List<String> idBatches = myFileCaptorCaptor.getAllValues().stream().map(t -> toIdsString(t)).toList();
		assertThat(idBatches).containsExactlyInAnyOrder(
			"Encounter/encPatientANumber0 Encounter/encPatientANumber1",
			"Encounter/encPatientBNumber0 Encounter/encPatientBNumber1"
		);
	}

	private String toIdsString(NdJsonFileJson theFile) {
		String ndJsonText = theFile.getNdJsonText();
		Bundle parsed = ourCtx.newNDJsonParser().parseResource(Bundle.class, ndJsonText);
		return parsed.getEntry().stream().map(t->t.getResource().getIdElement().toUnqualifiedVersionless().getValue()).collect(Collectors.joining(" "));
	}


	public static class ContentTypeHeaderModifiableBulkImportFileServlet extends BulkImportFileServlet{

		public String myContentTypeValue;


		public void setHeaderContentTypeValue(String theContentTypeValue) {
			myContentTypeValue = theContentTypeValue;
		}

		@Override
		public String getHeaderContentType() {
			return Objects.nonNull(myContentTypeValue) ? myContentTypeValue : super.getHeaderContentType();
		}
	}

}
