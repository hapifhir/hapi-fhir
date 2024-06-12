package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import com.helger.commons.collection.iterate.EmptyEnumeration;
import jakarta.annotation.Nonnull;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.collections4.iterators.IteratorEnumeration;
import org.apache.commons.lang3.RandomStringUtils;
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

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import static org.apache.commons.collections.CollectionUtils.isEmpty;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ServerConcurrencyTest {

	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	private static final Logger ourLog = LoggerFactory.getLogger(ServerConcurrencyTest.class);
	@RegisterExtension
	private static final RestfulServerExtension ourServer = new RestfulServerExtension(ourCtx)
		.registerProvider(new MyPatientProvider());
	@RegisterExtension
	private final HttpClientExtension myHttpClient = new HttpClientExtension();

	@Mock
	private HttpServletRequest myRequest;
	@Mock
	private HttpServletResponse myResponse;
	@Mock
	private PrintWriter myWriter;
	private HashMap<String, String> myHeaders;

	@Test
	public void testExceptionClosingInputStream() throws IOException {
		initRequestMocks();
		DelegatingServletInputStream inputStream = createMockPatientBodyServletInputStream();
		inputStream.setExceptionOnClose(true);
		when(myRequest.getInputStream()).thenReturn(inputStream);
		when(myResponse.getWriter()).thenReturn(myWriter);

		assertDoesNotThrow(() ->
			ourServer.getRestfulServer().handleRequest(RequestTypeEnum.POST, myRequest, myResponse)
		);
	}

	@Test
	public void testExceptionClosingOutputStream() throws IOException {
		initRequestMocks();
		when(myRequest.getInputStream()).thenReturn(createMockPatientBodyServletInputStream());
		when(myResponse.getWriter()).thenReturn(myWriter);

		// Throw an exception when the stream is closed
		doThrow(new EOFException()).when(myWriter).close();

		assertDoesNotThrow(() ->
			ourServer.getRestfulServer().handleRequest(RequestTypeEnum.POST, myRequest, myResponse)
		);
	}

	private void initRequestMocks() {
		myHeaders = new HashMap<>();
		myHeaders.put(Constants.HEADER_CONTENT_TYPE, Constants.CT_FHIR_JSON_NEW);

		when(myRequest.getRequestURI()).thenReturn("/Patient");
		when(myRequest.getRequestURL()).thenReturn(new StringBuffer(ourServer.getBaseUrl() + "/Patient"));
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
		when(myRequest.getHeaderNames()).thenAnswer(t -> {
			Set<String> headerNames = myHeaders.keySet();
			if (!isEmpty(headerNames)){
				return new IteratorEnumeration<>(headerNames.iterator());
			}
			return new EmptyEnumeration<>();
		});
	}

	/**
	 * Based on the class from Spring Test with the same name
	 */
	public static class DelegatingServletInputStream extends ServletInputStream {
		private final InputStream mySourceStream;
		private boolean myFinished = false;
		private boolean myExceptionOnClose = false;

		public DelegatingServletInputStream(InputStream sourceStream) {
			Assert.notNull(sourceStream, "Source InputStream must not be null");
			this.mySourceStream = sourceStream;
		}

		public void setExceptionOnClose(boolean theExceptionOnClose) {
			myExceptionOnClose = theExceptionOnClose;
		}

		@Override
		public int read() throws IOException {
			int data = this.mySourceStream.read();
			if (data == -1) {
				this.myFinished = true;
			}

			return data;
		}

		@Override
		public int available() throws IOException {
			return this.mySourceStream.available();
		}

		@Override
		public void close() throws IOException {
			super.close();
			this.mySourceStream.close();
			if (myExceptionOnClose) {
				throw new IOException("Failed!");
			}
		}

		@Override
		public boolean isFinished() {
			return this.myFinished;
		}

		@Override
		public boolean isReady() {
			return true;
		}

		@Override
		public void setReadListener(ReadListener readListener) {
			throw new UnsupportedOperationException();
		}
	}

	@SuppressWarnings("unused")
	public static class MyPatientProvider implements IResourceProvider {

		@Create
		public MethodOutcome create(@ResourceParam Patient thePatient) throws InterruptedException {
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
	public static DelegatingServletInputStream createMockPatientBodyServletInputStream() {
		Patient input = new Patient();
		input.addName().setFamily(RandomStringUtils.randomAlphanumeric(100000));
		String patient = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(input);
		ByteArrayInputStream bais = new ByteArrayInputStream(patient.getBytes(StandardCharsets.UTF_8));
		return new DelegatingServletInputStream(bais);
	}
}
