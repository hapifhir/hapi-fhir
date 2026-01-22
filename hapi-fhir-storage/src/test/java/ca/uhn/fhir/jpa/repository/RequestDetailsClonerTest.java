package ca.uhn.fhir.jpa.repository;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RequestDetailsClonerTest {

	/**
	 * The request details type is handled differently
	 * for System and Servlet RDs.
	 * We will use this enum so we can easily test both paths.
	 */
	public enum RequestDetailsType {
		SERVLET_REQUEST_DETAILS,
		SYSTEM_REQUEST_DETAILS
	}

	private final FhirContext myFhirContext = FhirContext.forR4Cached();

	private RequestDetails createFilledInRequestDetails(@Nonnull RequestDetailsType theReqType) {
		RequestDetails reqDetails = null;
		switch (theReqType) {
			case SERVLET_REQUEST_DETAILS -> {
				reqDetails = new ServletRequestDetails();
			}
			case SYSTEM_REQUEST_DETAILS -> {
				reqDetails = new SystemRequestDetails();
			}
		}
		assertNotNull(reqDetails);

		return reqDetails;
	}

	@ParameterizedTest
	@EnumSource(RequestDetailsType.class)
	public void startWith_withProvidedDetails_shouldCloneProvidedDetails(RequestDetailsType theType) {
		// setup
		RequestDetails requestDetails = createFilledInRequestDetails(theType);

		Patient patient = new Patient();
		requestDetails.setRequestType(RequestTypeEnum.POST);
		requestDetails.setRestOperationType(RestOperationTypeEnum.CREATE);
		requestDetails.setOperation("$operation");
		requestDetails.setResource(patient);
		requestDetails.setParameters(Map.of("test", new String[]{"hi", "there"}));
		requestDetails.setResourceName("Patient");
		requestDetails.setCompartmentName("compartmentname");

		// test
		RequestDetails clone = RequestDetailsCloner.startWith(requestDetails)
			.create();

		// validate
		assertEquals(requestDetails.getRequestType(), clone.getRequestType());
		assertEquals(requestDetails.getRestOperationType(), clone.getRestOperationType());
		assertEquals(requestDetails.getOperation(), clone.getOperation());
		assertEquals(requestDetails.getResource(), clone.getResource());
		assertEquals(requestDetails.getResourceName(), clone.getResourceName());
		assertEquals(requestDetails.getCompartmentName(), clone.getCompartmentName());

		Map<String, String[]> expected = requestDetails.getParameters();
		Map<String, String[]> actual = clone.getParameters();
		assertNotNull(actual);
		assertEquals(expected.size(), actual.size());
		for (String key : expected.keySet()) {
			assertTrue(actual.containsKey(key));
			Set<String> expectedValues = Arrays.stream(expected.get(key)).collect(Collectors.toSet());
			String[] actualValues = actual.get(key);
			assertNotNull(actual);
			for (String value : actualValues) {
				assertTrue(expectedValues.contains(value));
			}
		}
	}

	@ParameterizedTest
	@EnumSource(RequestDetailsType.class)
	public void setAction_clonedRequestDetails_hasDifferentAction(RequestDetailsType theReqType) {
		// setup
		RestOperationTypeEnum newRT = RestOperationTypeEnum.CREATE;
		RequestDetails requestDetails = createFilledInRequestDetails(theReqType);
		requestDetails.setRestOperationType(RestOperationTypeEnum.HISTORY_TYPE);

		// test
		RequestDetails cloned = RequestDetailsCloner.startWith(requestDetails)
			.setAction(newRT)
			.create();

		// validate
		assertEquals(newRT, cloned.getRestOperationType());
		assertNotEquals(newRT, requestDetails.getRestOperationType());
	}

	@ParameterizedTest
	@EnumSource(RequestDetailsType.class)
	public void addHeaders_clonedRequestDetails_setsNewHeaderValues(RequestDetailsType theReqType) {
		// setup
		String headerName = "name";
		List<String> valueArray = Arrays.asList("value1", "value2");
		String headerName2 = "name2";
		RequestDetails requestDetails = createFilledInRequestDetails(theReqType);
		if (requestDetails instanceof ServletRequestDetails srd) {
			HttpServletRequest mockServletRequest = mock(HttpServletRequest.class);
			// when
			when(mockServletRequest.getHeaderNames())
				.thenReturn(Collections.enumeration(new ArrayList<>()));
			srd.setServletRequest(mockServletRequest);
		}
		for (String value : valueArray) {
			requestDetails.addHeader(headerName, value);
		}

		// test
		RequestDetails clone = RequestDetailsCloner.startWith(requestDetails)
			.addHeaders(Map.of(headerName2, "newval1"))
			.addHeaders(Map.of(headerName, "value3"))
			.create();

		// validate
		assertEquals(2, requestDetails.getHeaders(headerName).size());

		List<String> newValues = clone.getHeaders(headerName);
		List<String> newHeaderValues = clone.getHeaders(headerName2);
		assertEquals(1, newValues.size());
		assertTrue(newValues.contains("value3"));
		assertEquals(1, newHeaderValues.size());
		assertEquals("newval1", newHeaderValues.get(0));
	}

	@ParameterizedTest
	@EnumSource(RequestDetailsType.class)
	public void setRequestBody_cloneRequestDetails_setsBodyOfRequest(RequestDetailsType theReqType) {
		// setup
		Parameters newParams = new Parameters();
		newParams.addParameter("key", "value");

		RequestDetails requestDetails = createFilledInRequestDetails(theReqType);
		requestDetails.setRequestContents("平沢 唯".getBytes(StandardCharsets.UTF_8));

		// when
		RestfulServer server = mock(RestfulServer.class);
		when(server.getFhirContext())
			.thenReturn(myFhirContext);
		if (requestDetails instanceof SystemRequestDetails srd) {
			srd.setServer(server);
		} else if (requestDetails instanceof ServletRequestDetails srd) {
			srd.setServer(server);
		}

		// test
		RequestDetails clone = RequestDetailsCloner.startWith(requestDetails)
			.setRequestContents(newParams)
			.create();
		RequestDetails clone2 = RequestDetailsCloner.startWith(requestDetails)
			.setRequestContents((IBaseParameters) null)
			.create();

		// verification
		assertNotNull(clone2.getRequestContentsIfLoaded());
		assertEquals(0, clone2.getRequestContentsIfLoaded().length);

		String clonedContents = new String(clone.getRequestContentsIfLoaded());
		IParser parser = myFhirContext.newJsonParser();
		Parameters parsed = parser.parseResource(Parameters.class, clonedContents);
		assertEquals("value", parsed.getParameters("key").get(0).getValue().toString());
	}

	@ParameterizedTest
	@EnumSource(RequestDetailsType.class)
	public void setParameters_clonedRequestDetails_overridesParameters(RequestDetailsType theReqType) {
		// setup
		Map<String, String[]> newParams = new HashMap<>();
		newParams.put("key", new String[]{"value "});

		RequestDetails requestDetails = createFilledInRequestDetails(theReqType);
		requestDetails.setParameters(Map.of("Key2", new String[]{"value2"}));

		// test
		RequestDetails clone = RequestDetailsCloner.startWith(requestDetails)
			.setParameters(newParams)
			.create();

		// validate
		assertEquals(1, requestDetails.getParameters().size());
		Map<String, String[]> clonedParams = clone.getParameters();
		assertEquals(1, clonedParams.get("key").length);
		String clonedValue = clonedParams.get("key")[0].trim();
		assertEquals("value", clonedValue);
	}

	@ParameterizedTest
	@EnumSource(RequestDetailsType.class)
	public void withRestOperationType_clonedRequestDetails_overridesParameters(RequestDetailsType theReqType) {
		// setup
		RequestTypeEnum newType = RequestTypeEnum.DELETE;

		RequestDetails requestDetails = createFilledInRequestDetails(theReqType);
		requestDetails.setRequestType(RequestTypeEnum.PUT);

		// test
		RequestDetails clone = RequestDetailsCloner.startWith(requestDetails)
			.withRequestType(newType)
			.create();

		// validate
		assertEquals(newType, clone.getRequestType());
		assertNotEquals(newType, requestDetails.getRequestType());
	}

	@ParameterizedTest
	@EnumSource(RequestDetailsType.class)
	public void setOperationType_clonedRequestDetails_setsNewOperationType(RequestDetailsType theReqType) {
		// setup
		String opType = "$everything";
		RequestDetails requestDetails = createFilledInRequestDetails(theReqType);
		requestDetails.setOperation("$reindex");

		// test
		RequestDetails clone = RequestDetailsCloner.startWith(requestDetails)
			.setOperation(opType)
			.create();

		// validate
		assertEquals(opType, clone.getOperation());
		assertNotEquals(opType, requestDetails.getOperation());
	}

	@ParameterizedTest
	@EnumSource(RequestDetailsType.class)
	public void setResourceType_clonedRequestDetails_updatesResourceName(RequestDetailsType theReqType) {
		// setup
		String resourceType = "Patient";

		RequestDetails requestDetails = createFilledInRequestDetails(theReqType);
		requestDetails.setResourceName("Observation");

		// test
		RequestDetails clone = RequestDetailsCloner.startWith(requestDetails)
			.setResourceType(resourceType)
			.create();

		// validate
		assertEquals(resourceType, clone.getResourceName());
		assertNotEquals(resourceType, requestDetails.getResourceName());
	}

	@ParameterizedTest
	@EnumSource(RequestDetailsType.class)
	public void clear_clonedRequestDetails_returnsEmpty(RequestDetailsType theRequestDetailsType) {
		// setup
		RequestDetails requestDetails = createFilledInRequestDetails(theRequestDetailsType);
		requestDetails.setRequestType(RequestTypeEnum.POST);
		requestDetails.setOperation("$everything");
		requestDetails.setResource(new Patient());
		requestDetails.setParameters(Map.of("key", new String[] { "value1", "value2" }));
		requestDetails.setResourceName("Patient");
		requestDetails.setCompartmentName("Compartment");

		// test
		RequestDetails cloned = RequestDetailsCloner.startWith(requestDetails)
			.clear()
			.create();

		// validate
		assertNull(cloned.getRequestType());
		assertNull(cloned.getOperation());
		assertNull(cloned.getResource());
		assertTrue(cloned.getParameters().isEmpty());
		assertNull(cloned.getResourceName());
		assertNull(cloned.getCompartmentName());

		assertNotNull(requestDetails.getRequestType());
		assertNotNull(requestDetails.getOperation());
		assertNotNull(requestDetails.getResource());
		assertFalse(requestDetails.getParameters().isEmpty());
		assertNotNull(requestDetails.getResourceName());
		assertNotNull(requestDetails.getCompartmentName());
	}
}
