package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.test.utilities.server.RestfulServerRule;
import com.google.common.collect.Lists;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class SearchDateParamPriorityTest {
	private static final Logger ourLog = LoggerFactory.getLogger(SearchDateParamPriorityTest.class);

	private static FhirContext ourCtx = FhirContext.forR4();

	@ClassRule
	public static RestfulServerRule ourServerRule = new RestfulServerRule(ourCtx);
	private String myLastMethod;

	@Before
	public void before() {
		myLastMethod = null;
	}

	@After
	public void after() {
		while (ourServerRule.getRestfulServer().getResourceProviders().size() > 0) {
			ourServerRule.getRestfulServer().unregisterProvider(ourServerRule.getRestfulServer().getResourceProviders().iterator().next());
		}
	}

	@Test
	public void testDateRangeSelectedWhenMultipleParametersProvided() {
		PatientResourceProvider provider = new PatientResourceProvider();
		ourServerRule.getRestfulServer().registerProviders(provider);

		IGenericClient client = ourServerRule.getFhirClient();
		client
			.search()
			.forResource("Patient")
			.where(Patient.BIRTHDATE.after().day("2001-01-01"))
			.and(Patient.BIRTHDATE.before().day("2002-01-01"))
			.returnBundle(Bundle.class)
			.execute();

		assertEquals("findDateRangeParam", myLastMethod);
	}

	@Test
	public void testDateRangeNotSelectedWhenSingleParameterProvided() {
		PatientResourceProvider provider = new PatientResourceProvider();
		ourServerRule.getRestfulServer().registerProviders(provider);

		IGenericClient client = ourServerRule.getFhirClient();
		client
			.search()
			.forResource("Patient")
			.where(Patient.BIRTHDATE.after().day("2001-01-01"))
			.returnBundle(Bundle.class)
			.execute();

		assertEquals("findDateParam", myLastMethod);
	}

	@Test
	public void testEmptySearchProvidedWithNoParameters() {
		PatientResourceProvider provider = new PatientResourceProvider();
		ourServerRule.getRestfulServer().registerProviders(provider);

		IGenericClient client = ourServerRule.getFhirClient();
		client
			.search()
			.forResource("Patient")
			.returnBundle(Bundle.class)
			.execute();

		assertEquals("find", myLastMethod);
	}

	public class PatientResourceProvider implements IResourceProvider {

		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}

		@Search
		public List<Patient> find() {
			myLastMethod = "find";
			return Lists.newArrayList();
		}

		@Search()
		public List<Patient> findDateParam(
			@RequiredParam(name = Patient.SP_BIRTHDATE) DateParam theDate) {
			myLastMethod = "findDateParam";
			return Lists.newArrayList();
		}

		@Search()
		public List<Patient> findDateRangeParam(
			@RequiredParam(name = Patient.SP_BIRTHDATE) DateRangeParam theRange) {
			myLastMethod = "findDateRangeParam";
			return Lists.newArrayList();
		}

	}

}
