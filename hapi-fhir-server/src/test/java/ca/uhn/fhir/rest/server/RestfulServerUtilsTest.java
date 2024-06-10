package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.DeleteCascadeModeEnum;
import ca.uhn.fhir.rest.api.PreferHandlingEnum;
import ca.uhn.fhir.rest.api.PreferHeader;
import ca.uhn.fhir.rest.api.PreferReturnEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static ca.uhn.fhir.rest.api.RequestTypeEnum.GET;
import static ca.uhn.fhir.rest.api.RequestTypeEnum.POST;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
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
		assertNull(header.getReturn());
		assertTrue(header.getRespondAsync());
	}

	@Test
	public void testParseHandlingLenient() {
		PreferHeader header = RestfulServerUtils.parsePreferHeader(null, "handling=lenient");
		assertNull(header.getReturn());
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
				fail();			} catch (InvalidRequestException e) {
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
		assertThat(linkSelf).contains("http://localhost:8000/$my-operation?");
		assertThat(linkSelf).contains("_format=json");
		assertThat(linkSelf).contains("_count=10");
		assertThat(linkSelf).contains("_offset=100");


		//When
		String linkSelfWithoutGivenParameters = RestfulServerUtils.createLinkSelfWithoutGivenParameters(baseUrl, servletRequestDetails, paramsToRemove);
		//Then
		assertThat(linkSelfWithoutGivenParameters).contains("http://localhost:8000/$my-operation?");
		assertThat(linkSelfWithoutGivenParameters).contains("_format=json");
	}

	@ParameterizedTest
	@MethodSource("testParameters")
	public void testCreateSelfLinks_withDifferentResourcePathAndTenantId(String theServerBaseUrl, String theRequestPath,
	String theTenantId, String theExpectedUrl) {
		//When
		ServletRequestDetails servletRequestDetails = new ServletRequestDetails();
		servletRequestDetails.setRequestType(POST);
		servletRequestDetails.setTenantId(StringUtils.defaultString(theTenantId));
		servletRequestDetails.setRequestPath(StringUtils.defaultString(theRequestPath));

		//Then
		String linkSelfWithoutGivenParameters = RestfulServerUtils.createLinkSelfWithoutGivenParameters(theServerBaseUrl, servletRequestDetails, null);
		//Test
		assertEquals(theExpectedUrl, linkSelfWithoutGivenParameters);
	}
	static Stream<Arguments> testParameters(){
		return Stream.of(
			Arguments.of("http://localhost:8000/Partition-B","" ,"Partition-B","http://localhost:8000/Partition-B"),
			Arguments.of("http://localhost:8000/Partition-B","Partition-B" ,"Partition-B","http://localhost:8000/Partition-B"),
			Arguments.of("http://localhost:8000/Partition-B","Partition-B/Patient" ,"Partition-B","http://localhost:8000/Partition-B/Patient"),
			Arguments.of("http://localhost:8000/Partition-B","Partition-B/$my-operation" ,"Partition-B","http://localhost:8000/Partition-B/$my-operation"),
			Arguments.of("http://localhost:8000","","","http://localhost:8000"),
			Arguments.of("", "","",""),
			Arguments.of("http://localhost:8000","Patient","","http://localhost:8000/Patient"),
			Arguments.of("http://localhost:8000/Patient","","","http://localhost:8000/Patient")
		);
	}
}
