package ca.uhn.fhir.rest.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.method.IParameter;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.method.SearchMethodBinding;
import ca.uhn.fhir.rest.method.SearchParameter;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.TestUtil;

public class ResourceMethodTest {

	private SearchMethodBinding rm;

	@Before
	public void before() throws NoSuchMethodException, SecurityException {
		rm = new SearchMethodBinding(Patient.class, ResourceMethodTest.class.getMethod("foo"), FhirContext.forDstu1(), null);
	}
	
	@Search
	public Bundle foo() {
		return null;
	}
	
	@Test
	public void testAllParams() {
		List<IParameter> methodParams = new ArrayList<IParameter>();

		methodParams.add(new SearchParameter("firstName", false));
		methodParams.add(new SearchParameter("lastName", false));
		methodParams.add(new SearchParameter("mrn", true));

		rm.setParameters(methodParams);

		Set<String> inputParams = new HashSet<String>();
		inputParams.add("firstName");
		inputParams.add("lastName");
		inputParams.add("mrn");

		RequestDetails params = withResourceAndParams("Patient", RequestTypeEnum.GET, inputParams);
		boolean actual = rm.incomingServerRequestMatchesMethod(params);
		assertTrue( actual); // True
	}
	
	@Test
	public void testAllParamsWithExtra() {
		List<IParameter> methodParams = new ArrayList<IParameter>();

		methodParams.add(new SearchParameter("firstName", false));
		methodParams.add(new SearchParameter("lastName", false));
		methodParams.add(new SearchParameter("mrn", true));

		rm.setParameters(methodParams);

		Set<String> inputParams = new HashSet<String>();
		inputParams.add("firstName");
		inputParams.add("lastName");
		inputParams.add("mrn");
		inputParams.add("foo");

		assertEquals(false, rm.incomingServerRequestMatchesMethod(withResourceAndParams("Patient", RequestTypeEnum.GET, inputParams))); // False
	}
	
	@Test
	public void testMixedParams() {
		List<IParameter> methodParams = new ArrayList<IParameter>();

		methodParams.add(new SearchParameter("firstName", false));
		methodParams.add(new SearchParameter("lastName", false));
		methodParams.add(new SearchParameter("mrn", true));

		rm.setParameters(methodParams);

		Set<String> inputParams = new HashSet<String>();
		inputParams.add("firstName");
		inputParams.add("mrn");

		assertEquals(true, rm.incomingServerRequestMatchesMethod(withResourceAndParams("Patient", RequestTypeEnum.GET, inputParams))); // True
	}

	@Test
	public void testRequiredParamsMissing() {
		List<IParameter> methodParams = new ArrayList<IParameter>();

		methodParams.add(new SearchParameter("firstName", false));
		methodParams.add(new SearchParameter("lastName", false));
		methodParams.add(new SearchParameter("mrn", true));

		rm.setParameters(methodParams);

		Set<String> inputParams = new HashSet<String>();
		inputParams.add("firstName");
		inputParams.add("lastName");

		assertEquals(false, rm.incomingServerRequestMatchesMethod(withResourceAndParams("Patient", RequestTypeEnum.GET, inputParams))); // False
	}

	@Test
	public void testRequiredParamsOnly() {
		List<IParameter> methodParams = new ArrayList<IParameter>();

		methodParams.add(new SearchParameter("firstName", false));
		methodParams.add(new SearchParameter("lastName", false));
		methodParams.add(new SearchParameter("mrn", true));

		rm.setParameters(methodParams);

		Set<String> inputParams = new HashSet<String>();
		inputParams.add("mrn");
		assertEquals(true, rm.incomingServerRequestMatchesMethod(withResourceAndParams("Patient", RequestTypeEnum.GET, inputParams))); // True
	}

	@Test(expected=IllegalStateException.class)
	public void testWildcardImmutable() {
		IResource.INCLUDE_ALL.setValue("AAA");
	}

	@Test
	public void testWildcardSet() {
		assertTrue(IResource.WILDCARD_ALL_SET.contains(IResource.INCLUDE_ALL));
	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	
	public static RequestDetails withResourceAndParams(String theResourceName, RequestTypeEnum theRequestType, Set<String> theParamNames) {
		ServletRequestDetails retVal = new ServletRequestDetails();
		retVal.setResourceName(theResourceName);
		retVal.setRequestType(theRequestType);
		Map<String, String[]> paramNames = new HashMap<String, String[]>();
		for (String next : theParamNames) {
			paramNames.put(next, new String[0]);
		}
		retVal.setParameters(paramNames);
		retVal.setServletRequest(mock(HttpServletRequest.class));
		return retVal;
	}

}
