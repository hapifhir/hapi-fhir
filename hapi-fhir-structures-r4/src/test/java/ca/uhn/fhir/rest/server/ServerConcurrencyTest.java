package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.StopWatch;
import com.helger.commons.collection.iterate.EmptyEnumeration;
import org.apache.commons.collections4.iterators.IteratorEnumeration;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ServerConcurrencyTest {

	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	private static final Logger ourLog = LoggerFactory.getLogger(ServerConcurrencyTest.class);
	@RegisterExtension
	private RestfulServerExtension myServer = new RestfulServerExtension(ourCtx)
		.registerProvider(new MyPatientProvider());
	@RegisterExtension
	private HttpClientExtension myHttpClient = new HttpClientExtension();

	@Mock
	private HttpServletRequest myRequest;
	@Mock
	private HttpServletResponse myResponse;
	@Mock
	private PrintWriter myWriter;
	private HashMap<String, String> myHeaders;

	@Test
	public void testExceptionClosingStream() throws ServletException, IOException {
		myHeaders = new HashMap<>();
		myHeaders.put(Constants.HEADER_CONTENT_TYPE, Constants.CT_FHIR_JSON_NEW);

		when(myRequest.getInputStream()).thenReturn(createMockPatientBodyServletInputStream());
		when(myRequest.getRequestURI()).thenReturn("/Patient");
		when(myRequest.getRequestURL()).thenReturn(new StringBuffer(myServer.getBaseUrl() + "/Patient"));
		when(myRequest.getHeader(any())).thenAnswer(t -> {
			String header = t.getArgument(0, String.class);
			String value = myHeaders.get(header);
			ourLog.info("Request for header '{}' produced: {}", header, value);
			return value;
		});
		when(myRequest.getHeaders(any())).thenAnswer(t -> {
			String header = t.getArgument(0, String.class);
			String value = myHeaders.get(header);
			ourLog.info("Request for header '{}' produced: {}", header, value);
			if (value != null) {
				return new IteratorEnumeration<>(Collections.singleton(value).iterator());
			}
			return new EmptyEnumeration<>();
		});
		when(myResponse.getWriter()).thenReturn(myWriter);

		doThrow(new EOFException()).when(myWriter).close();

		myServer.getRestfulServer().handleRequest(RequestTypeEnum.POST, myRequest, myResponse);
	}

	@Test
	public void testStress() throws ExecutionException, InterruptedException {
		Patient input = new Patient();
		int count = 1000;

		input.addName().setFamily(RandomStringUtils.randomAlphanumeric(1000));
		String patient = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(input);

		StopWatch sw = new StopWatch();
		ExecutorService executor = Executors.newFixedThreadPool(10);
		List<Future<Integer>> futures = new ArrayList<>(count);
		for (int i = 0; i < count; i++) {

			HttpPost post = new HttpPost(myServer.getBaseUrl() + "/Patient");
			post.setEntity(new StringEntity(patient, ContentType.create(Constants.CT_FHIR_JSON_NEW, StandardCharsets.UTF_8)));
			Future<Integer> future = executor.submit(() -> {
				try {
					ourLog.info("Initiating request");
					CloseableHttpResponse outcome = myHttpClient.execute(post);
					ourLog.info("Completed request");
					int retVal = outcome.getStatusLine().getStatusCode();

					outcome.getEntity().getContent().reset();

					return retVal;
				} catch (IOException theE) {
					ourLog.error("IOException", theE);
					return 0;
				}
			});
			futures.add(future);
		}

		for (var next : futures) {
			next.get();
		}

		ourLog.info("Completed {} requests in {} -- {} req/ms", count, sw, sw.getThroughput(count, TimeUnit.MILLISECONDS));
	}

	/**
	 * Based on the class from Spring Test with the same name
	 */
	public static class DelegatingServletInputStream extends ServletInputStream {
		private final InputStream sourceStream;
		private boolean finished = false;

		public DelegatingServletInputStream(InputStream sourceStream) {
			Assert.notNull(sourceStream, "Source InputStream must not be null");
			this.sourceStream = sourceStream;
		}

		public int read() throws IOException {
			int data = this.sourceStream.read();
			if (data == -1) {
				this.finished = true;
			}

			return data;
		}

		public int available() throws IOException {
			return this.sourceStream.available();
		}

		public void close() throws IOException {
			super.close();
			this.sourceStream.close();
		}

		public boolean isFinished() {
			return this.finished;
		}

		public boolean isReady() {
			return true;
		}

		public void setReadListener(ReadListener readListener) {
			throw new UnsupportedOperationException();
		}
	}

	public static class MyPatientProvider implements IResourceProvider {

		@Create
		public MethodOutcome create(@ResourceParam Patient thePatient) throws InterruptedException {
//			Thread.sleep(RandomUtils.nextInt(0, 100));
			OperationOutcome oo = new OperationOutcome();
			oo.addIssue().setDiagnostics(RandomStringUtils.randomAlphanumeric(1000));

			return new MethodOutcome()
				.setId(new IdType("Patient/A"))
				.setOperationOutcome(oo);
		}


		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}
	}

	@Nonnull
	private static DelegatingServletInputStream createMockPatientBodyServletInputStream() {
		Patient input = new Patient();
		input.addName().setFamily(RandomStringUtils.randomAlphanumeric(100000));
		String patient = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(input);
		ByteArrayInputStream bais = new ByteArrayInputStream(patient.getBytes(StandardCharsets.UTF_8));
		DelegatingServletInputStream servletInputStream = new DelegatingServletInputStream(bais);
		return servletInputStream;
	}
}
