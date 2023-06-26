package ca.uhn.fhir.rest.client;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.client.apache.ApacheHttpResponse;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.client.api.IHttpResponse;
import ca.uhn.fhir.rest.client.interceptor.CapturingInterceptor;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.StopWatch;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHttpResponse;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.Charset;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class CapturingInterceptorTest {

	@Test
	public void testClear() {
		IHttpRequest expectedRequest = mock(IHttpRequest.class);
		IHttpResponse expectedResponse = mock(IHttpResponse.class);
		Object response = mock(Object.class);
		when(expectedResponse.getResponse()).thenReturn(response);

		CapturingInterceptor interceptor = new CapturingInterceptor();
		interceptor.interceptResponse(expectedResponse);
		interceptor.interceptRequest(expectedRequest);

		assertEquals(expectedRequest, interceptor.getLastRequest());
		assertEquals(expectedResponse, interceptor.getLastResponse());

		interceptor.clear();

		assertNull(interceptor.getLastRequest());
		assertNull(interceptor.getLastResponse());
	}

	@Test
	public void testRequest() {
		IHttpRequest expectedRequest = mock(IHttpRequest.class);

		CapturingInterceptor interceptor = new CapturingInterceptor();
		interceptor.interceptRequest(expectedRequest);

		assertEquals(expectedRequest, interceptor.getLastRequest());
	}

	@Test
	public void testResponse() throws Exception {
		IHttpResponse expectedResponse = mock(IHttpResponse.class);
		doNothing().when(expectedResponse).bufferEntity();

		CapturingInterceptor interceptor = new CapturingInterceptor();
		interceptor.interceptResponse(expectedResponse);

		assertEquals(expectedResponse, interceptor.getLastResponse());
		verify(expectedResponse).bufferEntity();
	}

	@Test
	public void testResponseBufferApache() throws Exception {
		StopWatch responseStopWatch = new StopWatch();
		HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK");
		response.setEntity(new InputStreamEntity(IOUtils.toInputStream("Some content", Charset.defaultCharset())));
		IHttpResponse expectedResponse = spy(new ApacheHttpResponse(response, responseStopWatch));

		CapturingInterceptor interceptor = new CapturingInterceptor();
		interceptor.interceptResponse(expectedResponse);
		IHttpResponse actualResponse = interceptor.getLastResponse();

		assertEquals(expectedResponse, actualResponse);
		assertThat("Some content", equalTo(IOUtils.toString(actualResponse.createReader())));
		verify(expectedResponse).bufferEntity();

		//A second call should not throw an exception (InpuStreamEntity is not repeatable)
		IOUtils.toString(actualResponse.createReader());
	}

	@Test
	public void testResponseBufferOther() throws Exception {
		Object response = mock(Object.class);
		IHttpResponse expectedResponse = mock(IHttpResponse.class);
		when(expectedResponse.getResponse()).thenReturn(response);
		doNothing().when(expectedResponse).bufferEntity();

		CapturingInterceptor interceptor = new CapturingInterceptor();
		interceptor.interceptResponse(expectedResponse);
		IHttpResponse actualResponse = interceptor.getLastResponse();

		assertEquals(expectedResponse, actualResponse);
		verify(expectedResponse).bufferEntity();
	}

	@Test
	public void testResponseException() throws Exception {
		IHttpResponse response = mock(IHttpResponse.class);
		IOException expectedCause = new IOException();
		doThrow(expectedCause).when(response).bufferEntity();

		InternalErrorException exception = assertThrows(InternalErrorException.class, () -> {
			CapturingInterceptor interceptor = new CapturingInterceptor();
			interceptor.interceptResponse(response);
		});

		assertEquals(Msg.code(1404) + "Unable to buffer the entity for capturing", exception.getMessage());
		assertEquals(expectedCause, exception.getCause());

	}

	@Test
	public void testResponseRepeatable() throws Exception {
		StopWatch responseStopWatch = new StopWatch();
		HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK");
		response.setEntity(new StringEntity("Some content"));
		IHttpResponse expectedResponse = spy(new ApacheHttpResponse(response, responseStopWatch));

		CapturingInterceptor interceptor = new CapturingInterceptor();
		interceptor.interceptResponse(expectedResponse);
		IHttpResponse actualResponse = interceptor.getLastResponse();

		assertEquals(expectedResponse, actualResponse);
		assertThat("Some content", equalTo(IOUtils.toString(actualResponse.createReader())));
		verify(expectedResponse, times(0)).bufferEntity();

		//A second call should not throw an exception (StringEntity is repeatable)
		IOUtils.toString(actualResponse.createReader());
	}

}
