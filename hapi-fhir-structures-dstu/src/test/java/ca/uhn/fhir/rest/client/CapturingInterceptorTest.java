package ca.uhn.fhir.rest.client;

import ca.uhn.fhir.rest.client.apache.ApacheHttpResponse;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.client.api.IHttpResponse;
import ca.uhn.fhir.rest.client.interceptor.CapturingInterceptor;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.message.BasicHttpResponse;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Spy;

import java.io.IOException;
import java.nio.charset.Charset;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;


public class CapturingInterceptorTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();


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
    public void testResponseException() throws Exception {
        IHttpResponse response = mock(IHttpResponse.class);
        IOException expectedCause = new IOException();
        doThrow(expectedCause).when(response).bufferEntity();

        thrown.expect(InternalErrorException.class);
        thrown.expectMessage("Unable to buffer the entity for capturing");
        thrown.expectCause(equalTo(expectedCause));

        CapturingInterceptor interceptor = new CapturingInterceptor();
        interceptor.interceptResponse(response);
    }

    @Test
    public void testResponseBufferApache() throws Exception{
        HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK");
        response.setEntity(new InputStreamEntity(IOUtils.toInputStream("Some content", Charset.defaultCharset())));
        IHttpResponse expectedResponse = spy(new ApacheHttpResponse(response));

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
    public void testResponseRepeatable() throws Exception{
        HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK");
        response.setEntity(new StringEntity("Some content"));
        IHttpResponse expectedResponse = spy(new ApacheHttpResponse(response));

        CapturingInterceptor interceptor = new CapturingInterceptor();
        interceptor.interceptResponse(expectedResponse);
        IHttpResponse actualResponse = interceptor.getLastResponse();

        assertEquals(expectedResponse, actualResponse);
        assertThat("Some content", equalTo(IOUtils.toString(actualResponse.createReader())));
        verify(expectedResponse, times(0)).bufferEntity();

        //A second call should not throw an exception (StringEntity is repeatable)
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
    public void testClear(){
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

}
