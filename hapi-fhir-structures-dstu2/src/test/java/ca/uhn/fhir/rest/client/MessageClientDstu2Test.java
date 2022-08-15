package ca.uhn.fhir.rest.client;


import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.MessageHeader;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu2.valueset.BundleTypeEnum;
import ca.uhn.fhir.model.dstu2.valueset.ResponseTypeEnum;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.client.apache.ApacheRestfulClientFactory;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.client.impl.BaseClient;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.input.ReaderInputStream;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicStatusLine;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MessageClientDstu2Test {

    private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(MessageClientDstu2Test.class);
    private FhirContext ourCtx;
    private HttpClient myHttpClient;

    private HttpResponse myHttpResponse;

    @AfterAll
    public static void afterClassClearContext() {
        TestUtil.randomizeLocaleAndTimezone();
    }

    @BeforeEach
    public void before() {
        ourCtx = FhirContext.forDstu2();

        myHttpClient = mock(HttpClient.class, new ReturnsDeepStubs());
        ourCtx.setRestfulClientFactory(new ApacheRestfulClientFactory(ourCtx));
        ourCtx.getRestfulClientFactory().setConnectionRequestTimeout(10000);
        ourCtx.getRestfulClientFactory().setConnectTimeout(10000);
        ourCtx.getRestfulClientFactory().setPoolMaxPerRoute(100);
        ourCtx.getRestfulClientFactory().setPoolMaxTotal(100);

        ourCtx.getRestfulClientFactory().setHttpClient(myHttpClient);
        ourCtx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
        myHttpResponse = mock(HttpResponse.class, new ReturnsDeepStubs());

        System.setProperty(BaseClient.HAPI_CLIENT_KEEPRESPONSES, "true");
    }

    @Test
    public void testSendMessageAsync() throws Exception {
        OperationOutcome oo = new OperationOutcome();
        oo.addIssue().setDiagnostics("FOOBAR");
        final String msg = ourCtx.newJsonParser().encodeResourceToString(oo);

        ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
        when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
        when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
        when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
        when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<InputStream>() {
            @Override
            public InputStream answer(InvocationOnMock theInvocation) throws Throwable {
                return new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8"));
            }
        });

        IGenericClient client = ourCtx.getRestfulClientFactory().newGenericClient("http://192.168.4.93:83/fhirServer");

        client.setEncoding(EncodingEnum.JSON);

// Create the input message to pass to the server
        final Bundle msgBundle = getMessageBundle(
                "myEvent", "Test Event",
                "MySource", "http://myServer/fhir/", "MyDestination", "http://myDestinationServer/fhir/");

// Invoke $process-message
        OperationOutcome response = client
                .operation()
                .processMessage()
                .setResponseUrlParam("http://myserver/fhir")
                .setMessageBundle(msgBundle)
                .asynchronous(OperationOutcome.class)
                .execute();

        //System.out.println(response);
        assertEquals("http://192.168.4.93:83/fhirServer/$process-message?async=true&response-url=http%3A%2F%2Fmyserver%2Ffhir&_format=json", capt.getAllValues().get(0).getURI().toASCIIString());
        assertEquals("POST", capt.getAllValues().get(0).getRequestLine().getMethod());
        //assertEquals("<Parameters xmlns=\"http://hl7.org/fhir\"><parameter><name value=\"resource\"/><resource><Patient xmlns=\"http://hl7.org/fhir\"><name><given value=\"GIVEN\"/></name></Patient></resource></parameter></Parameters>", extractBody(capt, 0));
        //assertNotNull(response.getOperationOutcome());
        assertEquals("FOOBAR", ((OperationOutcome) response).getIssueFirstRep().getDiagnosticsElement().getValue());

    }

    @Test
    public void testSendMessage() throws Exception {
        final Bundle msgBundleResponse = getMessageBundle(
                "myEvent", "Test Event",
                "MySource", "http://myServer/fhir/", "MyDestination", "http://myDestinationServer/fhir/");
        ((MessageHeader) msgBundleResponse.getEntryFirstRep().getResource()).getResponse().setCode(ResponseTypeEnum.OK);
        final String msg = ourCtx.newJsonParser().encodeResourceToString(msgBundleResponse);

        ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
        when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
        when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
        when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
        when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<InputStream>() {
            @Override
            public InputStream answer(InvocationOnMock theInvocation) throws Throwable {
                return new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8"));
            }
        });

        IGenericClient client = ourCtx.getRestfulClientFactory().newGenericClient("http://192.168.4.93:83/fhirServer");

        client.setEncoding(EncodingEnum.JSON);

// Create the input message to pass to the server
        final Bundle msgBundle = getMessageBundle(
                "myEvent", "Test Event",
                "MySource", "http://myServer/fhir/", "MyDestination", "http://myDestinationServer/fhir/");

// Invoke $process-message
        Bundle response = client
                .operation()           
                .processMessage()
                .setMessageBundle(msgBundle)
                .synchronous(Bundle.class)
                .execute();

        //System.out.println(response);
        assertEquals("http://192.168.4.93:83/fhirServer/$process-message?async=false&_format=json", capt.getAllValues().get(0).getURI().toASCIIString());
        assertEquals("POST", capt.getAllValues().get(0).getRequestLine().getMethod());
        //assertEquals("<Parameters xmlns=\"http://hl7.org/fhir\"><parameter><name value=\"resource\"/><resource><Patient xmlns=\"http://hl7.org/fhir\"><name><given value=\"GIVEN\"/></name></Patient></resource></parameter></Parameters>", extractBody(capt, 0));
        //assertNotNull(response.getOperationOutcome());
        assertEquals("MessageHeader", ((Bundle) response).getEntryFirstRep().getResource().getResourceName());

    }

    /**
     * Criar um FHIR Message Bundle pre-preenchido com os parametros
     *
     * @param eventCode
     * @param eventDisplay
     * @param sourceName
     * @param sourceEnpoint
     * @param destinationName
     * @param destinationEndpoint
     * @return Message Bundle
     */
    public static Bundle getMessageBundle(String eventCode, String eventDisplay, String sourceName, String sourceEnpoint, String destinationName, String destinationEndpoint) {
        /*
         Init Bundle
         */
        Bundle msgBundle = new Bundle();
        msgBundle.getMeta().setLastUpdated(new Date());
        msgBundle.setType(BundleTypeEnum.MESSAGE); //Document Type
        msgBundle.setId(UUID.randomUUID().toString()); // Random ID
        /*
         Init MessageHeader
         */
        MessageHeader msh = new MessageHeader();
        msh.setId(UUID.randomUUID().toString());
        msh.setTimestampWithMillisPrecision(new Date());
        msh.getEvent().setSystem("http://mybServer/fhir/events");
        msh.getEvent().setCode(eventCode);
        msh.getEvent().setDisplay(eventDisplay);
        msh.getSource().setName(sourceName);
        msh.getSource().setEndpoint(sourceEnpoint);
        msh.getDestinationFirstRep().setName(destinationName);
        msh.getDestinationFirstRep().setEndpoint(destinationEndpoint);
        Bundle.Entry entry = new Bundle.Entry();
        entry.setFullUrl("http://mybase/fhirServer/Bundle/" + msh.getId().getValue());
        entry.setResource(msh);
        msgBundle.addEntry(entry);
        return msgBundle;
    }
}
