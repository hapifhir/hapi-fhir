package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.StringAndListParam;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import com.google.common.collect.Lists;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SearchMethodPriorityTest {

	@RegisterExtension
	public static RestfulServerExtension ourServerRule = new RestfulServerExtension(FhirVersionEnum.R4);

	private String myLastMethod;
	private IGenericClient myClient;

	@BeforeEach
	public void before() {
		myLastMethod = null;
		myClient = ourServerRule.getFhirClient();
	}

	@AfterEach
	public void after() {
		ourServerRule.getRestfulServer().unregisterAllProviders();
	}

	@Test
	public void testDateRangeSelectedWhenMultipleParametersProvided() {
		ourServerRule.getRestfulServer().registerProviders(new DateStrengthsWithRequiredResourceProvider());

		myClient
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
		ourServerRule.getRestfulServer().registerProviders(new DateStrengthsWithRequiredResourceProvider());

		myClient
			.search()
			.forResource("Patient")
			.where(Patient.BIRTHDATE.after().day("2001-01-01"))
			.returnBundle(Bundle.class)
			.execute();

		assertEquals("findDateParam", myLastMethod);
	}

	@Test
	public void testEmptyDateSearchProvidedWithNoParameters() {
		ourServerRule.getRestfulServer().registerProviders(new DateStrengthsWithRequiredResourceProvider());

		myClient
			.search()
			.forResource("Patient")
			.returnBundle(Bundle.class)
			.execute();

		assertEquals("find", myLastMethod);
	}

	@Test
	public void testStringAndListSelectedWhenMultipleParametersProvided() {
		ourServerRule.getRestfulServer().registerProviders(new StringStrengthsWithOptionalResourceProvider());

		myClient
			.search()
			.forResource("Patient")
			.where(Patient.NAME.matches().value("hello"))
			.and(Patient.NAME.matches().value("goodbye"))
			.returnBundle(Bundle.class)
			.execute();

		assertEquals("findStringAndListParam", myLastMethod);
	}

	@Test
	public void testStringAndListNotSelectedWhenSingleParameterProvided() {
		ourServerRule.getRestfulServer().registerProviders(new StringStrengthsWithOptionalResourceProvider());

		myClient
			.search()
			.forResource("Patient")
			.where(Patient.NAME.matches().value("hello"))
			.returnBundle(Bundle.class)
			.execute();

		assertEquals("findString", myLastMethod);
	}

	@Test
	public void testEmptyStringSearchProvidedWithNoParameters() {
		ourServerRule.getRestfulServer().registerProviders(new StringStrengthsWithOptionalResourceProvider());

		myClient
			.search()
			.forResource("Patient")
			.returnBundle(Bundle.class)
			.execute();

		assertEquals("find", myLastMethod);
	}

	@Test
	public void testEmptyStringSearchProvidedWithNoParameters2() {
		ourServerRule.getRestfulServer().registerProviders(new StringStrengthsWithOptionalResourceProviderReverseOrder());

		myClient
			.search()
			.forResource("Patient")
			.returnBundle(Bundle.class)
			.execute();

		assertEquals("find", myLastMethod);
	}

	public class DateStrengthsWithRequiredResourceProvider implements IResourceProvider {

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

	public class StringStrengthsWithOptionalResourceProvider implements IResourceProvider {

		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}

		@Search()
		public List<Patient> findString(
			@OptionalParam(name = Patient.SP_NAME) String theDate) {
			myLastMethod = "findString";
			return Lists.newArrayList();
		}

		@Search()
		public List<Patient> findStringAndListParam(
			@OptionalParam(name = Patient.SP_NAME) StringAndListParam theRange) {
			myLastMethod = "findStringAndListParam";
			return Lists.newArrayList();
		}

		@Search
		public List<Patient> find() {
			myLastMethod = "find";
			return Lists.newArrayList();
		}

	}


	public class StringStrengthsWithOptionalResourceProviderReverseOrder implements IResourceProvider {

		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}

		@Search()
		public List<Patient> findA(
			@OptionalParam(name = Patient.SP_NAME) String theDate) {
			myLastMethod = "findString";
			return Lists.newArrayList();
		}

		@Search
		public List<Patient> findB() {
			myLastMethod = "find";
			return Lists.newArrayList();
		}

	}

}
