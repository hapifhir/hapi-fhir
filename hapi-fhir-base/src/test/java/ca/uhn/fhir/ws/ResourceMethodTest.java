package ca.uhn.fhir.ws;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

public class ResourceMethodTest {

	@Test
	public void testRequiredParamsMissing() {
		ResourceMethod rm = new ResourceMethod();
		List<Parameter> methodParams = new ArrayList<Parameter>();

		methodParams.add(new Parameter("firstName", false));
		methodParams.add(new Parameter("lastName", false));
		methodParams.add(new Parameter("mrn", true));

		rm.setParameters(methodParams);

		Set<String> inputParams = new HashSet<String>();
		inputParams.add("firstName");
		inputParams.add("lastName");

		assertEquals(false, rm.matches(inputParams)); // False
	}

	@Test
	public void testRequiredParamsOnly() {
		ResourceMethod rm = new ResourceMethod();
		List<Parameter> methodParams = new ArrayList<Parameter>();

		methodParams.add(new Parameter("firstName", false));
		methodParams.add(new Parameter("lastName", false));
		methodParams.add(new Parameter("mrn", true));

		rm.setParameters(methodParams);

		Set<String> inputParams = new HashSet<String>();
		inputParams.add("mrn");
		assertEquals(true, rm.matches(inputParams)); // True
	}

	@Test
	public void testMixedParams() {
		ResourceMethod rm = new ResourceMethod();
		List<Parameter> methodParams = new ArrayList<Parameter>();

		methodParams.add(new Parameter("firstName", false));
		methodParams.add(new Parameter("lastName", false));
		methodParams.add(new Parameter("mrn", true));

		rm.setParameters(methodParams);

		Set<String> inputParams = new HashSet<String>();
		inputParams.add("firstName");
		inputParams.add("mrn");

		assertEquals(true, rm.matches(inputParams)); // True
	}

	@Test
	public void testAllParams() {
		ResourceMethod rm = new ResourceMethod();
		List<Parameter> methodParams = new ArrayList<Parameter>();

		methodParams.add(new Parameter("firstName", false));
		methodParams.add(new Parameter("lastName", false));
		methodParams.add(new Parameter("mrn", true));

		rm.setParameters(methodParams);

		Set<String> inputParams = new HashSet<String>();
		inputParams.add("firstName");
		inputParams.add("lastName");
		inputParams.add("mrn");

		assertEquals(true, rm.matches(inputParams)); // True
	}

	@Test
	public void testAllParamsWithExtra() {
		ResourceMethod rm = new ResourceMethod();
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

		assertEquals(false, rm.matches(inputParams)); // False
	}
}
