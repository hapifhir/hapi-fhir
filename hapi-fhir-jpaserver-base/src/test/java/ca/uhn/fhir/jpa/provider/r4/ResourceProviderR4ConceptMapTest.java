package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent;
import org.junit.AfterClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ResourceProviderR4ConceptMapTest extends BaseResourceProviderR4Test {
	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	@Test
	public void testTranslate() {
		myTermSvc.storeNewConceptMap(createConceptMap());

		Parameters inParams = new Parameters();
		inParams.addParameter().setName("system").setValue(new UriType(CS_URL));
		inParams.addParameter().setName("targetsystem").setValue(new UriType(CS_URL_2));
		inParams.addParameter().setName("code").setValue(new CodeType("12345"));

		Parameters respParams = myClient
			.operation()
			.onType(ConceptMap.class)
			.named("translate")
			.withParameters(inParams)
			.execute();

		ParametersParameterComponent param = respParams.getParameter().get(0);
		assertEquals("result", param.getName());
		assertTrue(((BooleanType) param.getValue()).booleanValue());

		param = respParams.getParameter().get(1);
		assertEquals("message", param.getName());
		assertEquals("Matches found!", ((StringType) param.getValue()).getValueAsString());

		param = respParams.getParameter().get(2);
		assertEquals("match", param.getName());
		assertEquals(3, param.getPart().size());

		ParametersParameterComponent part = param.getPart().get(0);
		assertEquals("equivalence", part.getName());

		part = param.getPart().get(1);
		assertEquals("concept", part.getName());
		Coding coding = (Coding) part.getValue();
		assertEquals("34567", coding.getCode());
		assertEquals("Target Code 34567", coding.getDisplay());
		assertEquals(CS_URL_2, coding.getSystem());

		part = param.getPart().get(2);
		assertEquals("source", part.getName());
		assertEquals(CM_URL, ((UriType) part.getValue()).getValueAsString());
	}

	// TODO: Utility method for fetching paramByName
}
