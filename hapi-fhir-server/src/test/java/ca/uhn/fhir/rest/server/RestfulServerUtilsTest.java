package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.rest.api.PreferHandlingEnum;
import ca.uhn.fhir.rest.api.PreferHeader;
import ca.uhn.fhir.rest.api.PreferReturnEnum;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ca.uhn.fhir.rest.api.RequestTypeEnum.GET;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

public class RestfulServerUtilsTest{

	@Test
	public void testParsePreferReturn() {
		PreferHeader header = RestfulServerUtils.parsePreferHeader(null,"return=representation");
		assertEquals(PreferReturnEnum.REPRESENTATION, header.getReturn());
		assertFalse(header.getRespondAsync());
	}

	@Test
	public void testParsePreferReturnAndAsync() {
		PreferHeader header = RestfulServerUtils.parsePreferHeader(null,"return=OperationOutcome; respond-async");
		assertEquals(PreferReturnEnum.OPERATION_OUTCOME, header.getReturn());
		assertTrue(header.getRespondAsync());
	}

	@Test
	public void testParsePreferAsync() {
		PreferHeader header = RestfulServerUtils.parsePreferHeader(null,"respond-async");
		assertEquals(null, header.getReturn());
		assertTrue(header.getRespondAsync());
	}

	@Test
	public void testParseHandlingLenient() {
		PreferHeader header = RestfulServerUtils.parsePreferHeader(null,"handling=lenient");
		assertEquals(null, header.getReturn());
		assertFalse(header.getRespondAsync());
		assertEquals(PreferHandlingEnum.LENIENT, header.getHanding());
	}

	@Test
	public void testParseHandlingLenientAndReturnRepresentation_CommaSeparatd() {
		PreferHeader header = RestfulServerUtils.parsePreferHeader(null,"handling=lenient, return=representation");
		assertEquals(PreferReturnEnum.REPRESENTATION, header.getReturn());
		assertFalse(header.getRespondAsync());
		assertEquals(PreferHandlingEnum.LENIENT, header.getHanding());
	}

	@Test
	public void testParseHandlingLenientAndReturnRepresentation_SemicolonSeparatd() {
		PreferHeader header = RestfulServerUtils.parsePreferHeader(null,"handling=lenient; return=representation");
		assertEquals(PreferReturnEnum.REPRESENTATION, header.getReturn());
		assertFalse(header.getRespondAsync());
		assertEquals(PreferHandlingEnum.LENIENT, header.getHanding());
	}

	@Test
	public void testCreateSelfLinks() {
		//Given
		String baseUrl = "http://localhost:8000";
		Map<String, String[]> parameters = new HashMap<>();
		parameters.put("_format", new String[]{"json"});
		parameters.put("_count", new String[]{"10"});
		parameters.put("_offset", new String[]{"100"});
		List<String> paramsToRemove = Arrays.asList("_count", "_offset");

		ServletRequestDetails servletRequestDetails = new ServletRequestDetails();
		servletRequestDetails.setFhirServerBase("http://localhost:8000");
		servletRequestDetails.setRequestPath("$my-operation");
		servletRequestDetails.setRequestType(GET);
		servletRequestDetails.setParameters(parameters);

		//When
		String linkSelf = RestfulServerUtils.createLinkSelf(baseUrl, servletRequestDetails);
		//Then
		assertThat(linkSelf, is(containsString("http://localhost:8000/$my-operation?")));
		assertThat(linkSelf, is(containsString("_format=json")));
		assertThat(linkSelf, is(containsString("_count=10")));
		assertThat(linkSelf, is(containsString("_offset=100")));


		//When
		String linkSelfWithoutGivenParameters = RestfulServerUtils.createLinkSelfWithoutGivenParameters(baseUrl, servletRequestDetails, paramsToRemove);
		//Then
		assertThat(linkSelfWithoutGivenParameters, is(containsString("http://localhost:8000/$my-operation?")));
		assertThat(linkSelfWithoutGivenParameters, is(containsString("_format=json")));

	}
}
