package org.hl7.fhir.r5.utils;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r5.hapi.ctx.HapiWorkerContext;
import org.hl7.fhir.r5.model.DateTimeType;
import org.hl7.fhir.r5.model.Observation;
import org.hl7.fhir.r5.model.Patient;
import org.hl7.fhir.r5.model.Period;
import org.hl7.fhir.r5.model.Quantity;
import org.hl7.fhir.r5.model.Reference;
import org.hl7.fhir.r5.model.Resource;
import org.hl7.fhir.utilities.graphql.EGraphEngine;
import org.hl7.fhir.utilities.graphql.EGraphQLException;
import org.hl7.fhir.utilities.graphql.GraphQLResponse;
import org.hl7.fhir.utilities.graphql.IGraphQLStorageServices;
import org.hl7.fhir.utilities.graphql.Parser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GraphQLEngineTest {
	private static HapiWorkerContext ourWorkerCtx;
	private static FhirContext ourCtx;
	private org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(GraphQLEngineTest.class);

	private Observation createObservation() {
		Observation obs = new Observation();
		obs.setId("http://foo.com/Patient/PATA");
		obs.setValue(new Quantity().setValue(123).setUnit("cm"));
		obs.setSubject(new Reference("Patient/123"));
		return obs;
	}

	private IGraphQLStorageServices createStorageServices() throws FHIRException {
		IGraphQLStorageServices retVal = mock(IGraphQLStorageServices.class);
		when(retVal.lookup(nullable(Object.class), nullable(Resource.class), nullable(Reference.class))).thenAnswer(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) {
				Object appInfo = invocation.getArguments()[0];
				Resource context = (Resource) invocation.getArguments()[1];
				Reference reference = (Reference) invocation.getArguments()[2];
				ourLog.info("AppInfo: {} / Context: {} / Reference: {}", appInfo, context.getId(), reference.getReference());

				if (reference.getReference().equalsIgnoreCase("Patient/123")) {
					Patient p = new Patient();
					p.getBirthDateElement().setValueAsString("2011-02-22");
					return new IGraphQLStorageServices.ReferenceResolution(context, p);
				}

				ourLog.info("Not found!");
				return null;
			}
		});

		return retVal;
	}

	@Test
	public void testGraphSimple() throws EGraphQLException, EGraphEngine, IOException, FHIRException {

		Observation obs = createObservation();

		GraphQLEngine engine = new GraphQLEngine(ourWorkerCtx);
		engine.setFocus(obs);
		engine.setGraphQL(Parser.parse("{valueQuantity{value,unit}}"));
		engine.execute();

		GraphQLResponse output = engine.getOutput();
		output.setWriteWrapper(false);
		StringBuilder outputBuilder = new StringBuilder();
		output.write(outputBuilder, 0, "\n");

		String expected = "{\n" +
			"  \"valueQuantity\":{\n" +
			"    \"value\":123,\n" +
			"    \"unit\":\"cm\"\n" +
			"  }\n" +
			"}";
		assertEquals(TestUtil.stripReturns(expected), TestUtil.stripReturns(outputBuilder.toString()));

	}


	@Test
	public void testChoiceType_SelectDifferentType() throws EGraphEngine, EGraphQLException, IOException {
		Observation obs = new Observation();
		obs.setId("http://foo.com/Patient/PATA");
		obs.setEffective(new Period().setStartElement(new DateTimeType("2022-01-01T00:00:00Z")).setEndElement(new DateTimeType("2022-01-01T05:00:00Z")));

		GraphQLEngine engine = new GraphQLEngine(ourWorkerCtx);
		engine.setFocus(obs);
		engine.setGraphQL(Parser.parse("{id, effectiveDateTime}"));
		engine.execute();

		GraphQLResponse output = engine.getOutput();
		output.setWriteWrapper(false);
		StringBuilder outputBuilder = new StringBuilder();
		output.write(outputBuilder, 0, "\n");

		String expected = "{\n" +
			"  \"id\":\"http://foo.com/Patient/PATA\"\n" +
			"}";
		assertEquals(TestUtil.stripReturns(expected), TestUtil.stripReturns(outputBuilder.toString()));
	}

	@Test
	public void testChoiceType_SelectSameType() throws EGraphEngine, EGraphQLException, IOException {
		Observation obs = new Observation();
		obs.setId("http://foo.com/Patient/PATA");
		obs.setEffective(new DateTimeType("2022-01-01T12:12:12Z"));

		GraphQLEngine engine = new GraphQLEngine(ourWorkerCtx);
		engine.setFocus(obs);
		engine.setGraphQL(Parser.parse("{id, effectiveDateTime}"));
		engine.execute();

		GraphQLResponse output = engine.getOutput();
		output.setWriteWrapper(false);
		StringBuilder outputBuilder = new StringBuilder();
		output.write(outputBuilder, 0, "\n");

		String expected = "{\n" +
			"  \"id\":\"http://foo.com/Patient/PATA\",\n" +
			"  \"effectiveDateTime\":\"2022-01-01T12:12:12Z\"\n" +
			"}";
		assertEquals(TestUtil.stripReturns(expected), TestUtil.stripReturns(outputBuilder.toString()));
	}


	@Test
	public void testReferences() throws EGraphQLException, EGraphEngine, IOException, FHIRException {

		String graph = " { \n" +
			"  id\n" +
			"  subject { \n" +
			"   reference\n" +
			"    resource(type : Patient) { birthDate }\n" +
			"    resource(type : Practioner) { practitionerRole {  speciality } }\n" +
			"  }  \n" +
			"  code {coding {system code} }\n" +
			" }\n" +
			" ";

		GraphQLEngine engine = new GraphQLEngine(ourWorkerCtx);
		engine.setFocus(createObservation());
		engine.setGraphQL(Parser.parse(graph));
		engine.setServices(createStorageServices());
		engine.execute();

		GraphQLResponse output = engine.getOutput();
		output.setWriteWrapper(false);
		StringBuilder outputBuilder = new StringBuilder();
		output.write(outputBuilder, 0, "\n");

		String expected = "{\n" +
			"  \"id\":\"http://foo.com/Patient/PATA\",\n" +
			"  \"subject\":{\n" +
			"    \"reference\":\"Patient/123\",\n" +
			"    \"resource\":{\n" +
			"      \"birthDate\":\"2011-02-22\"\n" +
			"    }\n" +
			"  }\n" +
			"}";
		assertEquals(TestUtil.stripReturns(expected), TestUtil.stripReturns(outputBuilder.toString()));

	}

	@BeforeAll
	public static void beforeClass() {
		ourCtx = FhirContext.forR5Cached();
		ourWorkerCtx = new HapiWorkerContext(ourCtx, ourCtx.getValidationSupport());
	}

}
