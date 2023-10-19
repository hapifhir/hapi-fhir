package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.rest.api.*;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ca.uhn.fhir.rest.api.RequestTypeEnum.GET;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RestfulServerUtilsTest {

	@Mock
	private RequestDetails myRequestDetails;

	@Test
	public void testParsePreferReturn() {
		PreferHeader header = RestfulServerUtils.parsePreferHeader(null, "return=representation");
		assertEquals(PreferReturnEnum.REPRESENTATION, header.getReturn());
		assertFalse(header.getRespondAsync());
	}

	@Test
	public void testParsePreferReturnAndAsync() {
		PreferHeader header = RestfulServerUtils.parsePreferHeader(null, "return=OperationOutcome; respond-async");
		assertEquals(PreferReturnEnum.OPERATION_OUTCOME, header.getReturn());
		assertTrue(header.getRespondAsync());
	}

	@Test
	public void testParsePreferAsync() {
		PreferHeader header = RestfulServerUtils.parsePreferHeader(null, "respond-async");
		assertEquals(null, header.getReturn());
		assertTrue(header.getRespondAsync());
	}

	@Test
	public void testParseHandlingLenient() {
		PreferHeader header = RestfulServerUtils.parsePreferHeader(null, "handling=lenient");
		assertEquals(null, header.getReturn());
		assertFalse(header.getRespondAsync());
		assertEquals(PreferHandlingEnum.LENIENT, header.getHanding());
	}

	@Test
	public void testParseHandlingLenientAndReturnRepresentation_CommaSeparatd() {
		PreferHeader header = RestfulServerUtils.parsePreferHeader(null, "handling=lenient, return=representation");
		assertEquals(PreferReturnEnum.REPRESENTATION, header.getReturn());
		assertFalse(header.getRespondAsync());
		assertEquals(PreferHandlingEnum.LENIENT, header.getHanding());
	}

	@Test
	public void testParseHandlingLenientAndReturnRepresentation_SemicolonSeparatd() {
		PreferHeader header = RestfulServerUtils.parsePreferHeader(null, "handling=lenient; return=representation");
		assertEquals(PreferReturnEnum.REPRESENTATION, header.getReturn());
		assertFalse(header.getRespondAsync());
		assertEquals(PreferHandlingEnum.LENIENT, header.getHanding());
	}

	@ParameterizedTest
	@CsvSource({
		"         ,     ,                       , NONE    ,",
		"foo      ,     ,                       , NONE  ,  ",
		"delete   ,     ,                       , DELETE  ,",
		"delete   , 10  ,                       , DELETE  , 10",
		"delete   , abc ,                       , DELETE  , -1", // -1 means exception
		"         ,     , delete                , DELETE  ,",
		"         ,     , delete;               , DELETE  ,",
		"         ,     , delete; max-rounds=   , DELETE  , ",
		"         ,     , delete; max-rounds    , DELETE  , ",
		"         ,     , delete; max-rounds=10 , DELETE  , 10",
		"         ,     , delete; max-rounds=10 , DELETE  , 10",
	})
	public void testParseCascade(String theCascadeParam, String theCascadeMaxRoundsParam, String theCascadeHeader, DeleteCascadeModeEnum theExpectedMode, Integer theExpectedMaxRounds) {
		HashMap<String, String[]> params = new HashMap<>();
		when(myRequestDetails.getParameters()).thenReturn(params);

		if (isNotBlank(theCascadeParam)) {
			params.put(Constants.PARAMETER_CASCADE_DELETE, new String[]{theCascadeParam.trim()});
		}
		if (isNotBlank(theCascadeMaxRoundsParam)) {
			params.put(Constants.PARAMETER_CASCADE_DELETE_MAX_ROUNDS, new String[]{theCascadeMaxRoundsParam.trim()});
		}

		if (isNotBlank(theCascadeHeader)) {
			when(myRequestDetails.getHeader(Constants.HEADER_CASCADE)).thenReturn(theCascadeHeader);
		}

		if (theExpectedMaxRounds != null && theExpectedMaxRounds == -1) {
			try {
				RestfulServerUtils.extractDeleteCascadeParameter(myRequestDetails);
				fail();
			} catch (InvalidRequestException e) {
				// good
			}
		} else {
			RestfulServerUtils.DeleteCascadeDetails outcome = RestfulServerUtils.extractDeleteCascadeParameter(myRequestDetails);
			assertEquals(theExpectedMode, outcome.getMode());
			assertEquals(theExpectedMaxRounds, outcome.getMaxRounds());
		}
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
