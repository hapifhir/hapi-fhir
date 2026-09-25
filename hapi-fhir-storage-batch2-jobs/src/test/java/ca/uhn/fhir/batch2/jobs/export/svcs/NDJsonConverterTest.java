package ca.uhn.fhir.batch2.jobs.export.svcs;

import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportResourceList;
import ca.uhn.fhir.rest.api.server.bulk.ConvertedFile;
import ca.uhn.fhir.rest.api.server.bulk.ConvertedFiles;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class NDJsonConverterTest {
	private static final org.slf4j.Logger ourLog = LoggerFactory.getLogger(NDJsonConverterTest.class);


	private static class TestNDJsonConverter extends NDJsonConverter {
		private OutputStreamWriter myWriter;

		TestNDJsonConverter(FhirContext theCtx, JpaStorageSettings theSettings) {
			super(theCtx, theSettings);
		}

		public void setStreamWriter(OutputStreamWriter theStreamWriter) {
			myWriter = theStreamWriter;
		}

		@Override
		protected OutputStreamWriter getStreamWriter(ByteArrayOutputStream theOutputStream) {
			if (myWriter != null) {
				return myWriter;
			}
			return super.getStreamWriter(theOutputStream);
		}
	}

	private FhirContext myFhirContext = FhirContext.forR4Cached();

	private JpaStorageSettings myJpaStorageSettings;

	private TestNDJsonConverter myNDJsonConverter;

	@BeforeEach
	public void before() {
		myJpaStorageSettings = new JpaStorageSettings();
		myNDJsonConverter = new TestNDJsonConverter(myFhirContext, myJpaStorageSettings);
	}

	@Test
	public void consume_blankOutput_fillsInAsNDJSON() {
		// setup
		BulkExportJobParameters parameters = createParameters();
		// normally this would be blocked by validators; but we'll test it for defensiveness
		parameters.setOutputFormat(null);

		BulkExportResourceList list = createResourceList(5);

		// test
		ConvertedFiles files = myNDJsonConverter.consume(list, parameters);

		// verify
		assertNotNull(files);
		assertThat(files.getFiles())
			.isNotEmpty();
		assertThat(files.getFiles())
			.allMatch(file -> {
				// despite null input we have ndjson output
				return file.getMimeType().equalsIgnoreCase(Constants.CT_FHIR_NDJSON);
			});
	}

	@Test
	public void consume_moreThanBulkMaxFileSize_splitsOutput() {
		// setup
		int resourceCount = 5;
		BulkExportJobParameters parameters = createParameters();
		BulkExportResourceList list = createResourceList(resourceCount);

		/*
		 * We will set the file size to
		 * ~2 resources length
		 */
		int sizeOfFile = 0;
		IParser parser = myFhirContext.newJsonParser();
		for (int i = 0; i < 2; i++) {
			IBaseResource resource = list.getResources()
				.get(i);
			String stringified = parser.encodeResourceToString(resource);
			sizeOfFile += stringified.length();
			sizeOfFile += 1; // new line characters; one per each resource in the file
		}

		myJpaStorageSettings.setBulkExportFileMaximumSize(sizeOfFile);

		// test
		ConvertedFiles files = myNDJsonConverter.consume(list, parameters);

		// validate
		assertNotNull(files);
		// we set max file size to ~2 resources. but created 5
		// so we expect 3 files
		assertThat(files.getFiles()).hasSize(3);
		for (ConvertedFile file : files.getFiles()) {
			byte[] bytes = file.getBytes();
			String content = new String(bytes, StandardCharsets.UTF_8);
			ourLog.info("{} <= {}", content.length(), sizeOfFile);
			assertTrue(content.length() <= sizeOfFile);
		}
	}

	@Test
	public void consume_withIOException_throws() throws IOException {
		// setup
		BulkExportJobParameters parameters = createParameters();
		String testException = "I am an exceptional exception.";

		BulkExportResourceList list = createResourceList(5);
		ListAppender<ILoggingEvent> appender = mock(ListAppender.class);

		Logger ndJsonLogger = (Logger) LoggerFactory.getLogger(NDJsonConverter.class);

		ndJsonLogger.addAppender(appender);

		// set a throwing output writer
		OutputStreamWriter writer = mock(OutputStreamWriter.class);
		when(writer.append(anyString())).thenThrow(new IOException(testException));
		myNDJsonConverter.setStreamWriter(writer);

		// test
		try {
			myNDJsonConverter.consume(list, parameters);
			fail("Consume should've thrown a failure");
		} catch (JobExecutionFailedException ex) {
			assertThat(ex.getMessage()).contains("Failure to process resource of type");

			// verify
			ArgumentCaptor<ILoggingEvent> logCaptor = ArgumentCaptor.forClass(ILoggingEvent.class);
			verify(appender).doAppend(logCaptor.capture());
			assertThat(logCaptor.getValue().getFormattedMessage()).contains("Failure to process resource of type "
				+ "Patient"
				+ " : "
				+ testException);
		} finally {
			ndJsonLogger.detachAppender(appender);
		}
	}

	private BulkExportResourceList createResourceList(int theCount) {
		ArrayList<IBaseResource> resources = new ArrayList<>();
		for (int i = 0; i < theCount; i++) {
			Patient patient = new Patient();
			patient.addName()
				.setFamily("Simpson")
				.addGiven("H_" + i);
			patient.setActive(true);
			resources.add(patient);
		}
		BulkExportResourceList list = new BulkExportResourceList();
		list.setResources(resources);
		return list;
	}

	private BulkExportJobParameters createParameters() {
		BulkExportJobParameters parameters = new BulkExportJobParameters();
		parameters.setOutputFormat(Constants.CT_FHIR_NDJSON);
		return parameters;
	}
}
