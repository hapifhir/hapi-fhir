package ca.uhn.fhir.rest.server;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.rest.common.SearchMethodBinding;
import ca.uhn.fhir.rest.server.Parameter;

public class ResourceMethodTest {

	private SearchMethodBinding rm;

	@Before
	public void before() throws NoSuchMethodException, SecurityException {
		rm = new SearchMethodBinding(Patient.class, ResourceMethodTest.class.getMethod("before"));
	}
	
	@Test
	public void testRequiredParamsMissing() {
		List<Parameter> methodParams = new ArrayList<Parameter>();

		methodParams.add(new Parameter("firstName", false));
		methodParams.add(new Parameter("lastName", false));
		methodParams.add(new Parameter("mrn", true));

		rm.setParameters(methodParams);

		Set<String> inputParams = new HashSet<String>();
		inputParams.add("firstName");
		inputParams.add("lastName");

		assertEquals(false, rm.matches("ResName", null, null, inputParams)); // False
	}

	@Test
	public void testRequiredParamsOnly() {
		List<Parameter> methodParams = new ArrayList<Parameter>();

		methodParams.add(new Parameter("firstName", false));
		methodParams.add(new Parameter("lastName", false));
		methodParams.add(new Parameter("mrn", true));

		rm.setParameters(methodParams);

		Set<String> inputParams = new HashSet<String>();
		inputParams.add("mrn");
		assertEquals(true, rm.matches("ResName", null, null, inputParams)); // True
	}

	@Test
	public void testMixedParams() {
		List<Parameter> methodParams = new ArrayList<Parameter>();

		methodParams.add(new Parameter("firstName", false));
		methodParams.add(new Parameter("lastName", false));
		methodParams.add(new Parameter("mrn", true));

		rm.setParameters(methodParams);

		Set<String> inputParams = new HashSet<String>();
		inputParams.add("firstName");
		inputParams.add("mrn");

		assertEquals(true, rm.matches("ResName", null, null, inputParams)); // True
	}

	@Test
	public void testAllParams() {
		List<Parameter> methodParams = new ArrayList<Parameter>();

		methodParams.add(new Parameter("firstName", false));
		methodParams.add(new Parameter("lastName", false));
		methodParams.add(new Parameter("mrn", true));

		rm.setParameters(methodParams);

		Set<String> inputParams = new HashSet<String>();
		inputParams.add("firstName");
		inputParams.add("lastName");
		inputParams.add("mrn");

		assertEquals(true, rm.matches("ResName", null, null, inputParams)); // True
	}

	@Test
	public void testAllParamsWithExtra() {
		List<Parameter> methodParams = new ArrayList<Parameter>();

		methodParams.add(new Parameter("firstName", false));
		methodParams.add(new Parameter("lastName", false));
		methodParams.add(new Parameter("mrn", true));

		rm.setParameters(methodParams);

		Set<String> inputParams = new HashSet<String>();
		inputParams.add("firstName");
		inputParams.add("lastName");
		inputParams.add("mrn");
		inputParams.add("foo");

		assertEquals(false, rm.matches("ResName", null, null, inputParams)); // False
	}
}
