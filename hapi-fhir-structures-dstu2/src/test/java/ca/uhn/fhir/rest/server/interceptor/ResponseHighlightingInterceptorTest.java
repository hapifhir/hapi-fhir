package ca.uhn.fhir.rest.server.interceptor;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;

import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome.Issue;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

public class ResponseHighlightingInterceptorTest {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResponseHighlightingInterceptorTest.class);

	@Test
	public void testHighlightNormalResponse() throws Exception {
		ResponseHighlighterInterceptor ic = new ResponseHighlighterInterceptor();

		HttpServletRequest req = mock(HttpServletRequest.class);
		when(req.getHeader(Constants.HEADER_ACCEPT)).thenReturn("text/html");

		HttpServletResponse resp = mock(HttpServletResponse.class);
		StringWriter sw = new StringWriter();
		when(resp.getWriter()).thenReturn(new PrintWriter(sw));

		Patient resource = new Patient();
		resource.addName().addFamily("FAMILY");

		RequestDetails reqDetails = new RequestDetails();
		reqDetails.setRequestType(RequestTypeEnum.GET);
		reqDetails.setParameters(new HashMap<String, String[]>());
		reqDetails.setServer(new RestfulServer());
		reqDetails.setServletRequest(req);

		assertFalse(ic.outgoingResponse(reqDetails, resource, req, resp));

		String output = sw.getBuffer().toString();
		ourLog.info(output);
		assertThat(output, containsString("<span class='hlTagName'>Patient</span>"));
	}
	
	
	@Test
	public void testHighlightException() throws Exception {
		ResponseHighlighterInterceptor ic = new ResponseHighlighterInterceptor();

		HttpServletRequest req = mock(HttpServletRequest.class);
		when(req.getHeader(Constants.HEADER_ACCEPT)).thenReturn("text/html");

		HttpServletResponse resp = mock(HttpServletResponse.class);
		StringWriter sw = new StringWriter();
		when(resp.getWriter()).thenReturn(new PrintWriter(sw));

		Patient resource = new Patient();
		resource.addName().addFamily("FAMILY");

		RequestDetails reqDetails = new RequestDetails();
		reqDetails.setRequestType(RequestTypeEnum.GET);
		reqDetails.setParameters(new HashMap<String, String[]>());
		reqDetails.setServer(new RestfulServer());
		reqDetails.setServletRequest(req);

		ResourceNotFoundException exception = new ResourceNotFoundException("Not found");
		exception.setOperationOutcome(new OperationOutcome().addIssue(new Issue().setDetails("Hello")));
		
		assertFalse(ic.handleException(reqDetails, exception, req, resp));

		String output = sw.getBuffer().toString();
		ourLog.info(output);
		assertThat(output, containsString("<span class='hlTagName'>OperationOutcome</span>"));
	}

}
