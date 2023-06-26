package ca.uhn.fhir.parser.i391;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.parser.CustomTypeDstu2Test;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.util.TestUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * See #391
 */
public class TestOutcomeTest {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(CustomTypeDstu2Test.class);

	@Test
	public void testCustomDataTypeBugN2_UnknownElement() {
		CustomBlock nameDt = new CustomBlock();
		nameDt.ourValue = new StringDt("testText");

		CustomOperationOutcome outcome = new CustomOperationOutcome();
		outcome.element2 = nameDt;

		IParser parser = FhirContext.forDstu2().newXmlParser();
		String outcomeString = parser.setPrettyPrint(true).encodeResourceToString(outcome);		
		ourLog.info(outcomeString);
		
		assertEquals("<OperationOutcome xmlns=\"http://hl7.org/fhir\">" + 
				"<meta>" + 
				"<profile value=\"http://hl7.org/fhir/profiles/custom-operation-outcome\"/>" + 
				"</meta>" + 
				"<extension url=\"#someElement2\">" + 
				"<valueString value=\"testText\"/>" + 
				"</extension>" + 
				"</OperationOutcome>", parser.setPrettyPrint(false).encodeResourceToString(outcome));
		
		CustomOperationOutcome parsedOutcome = parser.parseResource(CustomOperationOutcome.class, outcomeString);
		ourLog.info(outcomeString);

//		assertNotNull(parsedOutcome.element2);
//		assertNotNull(parsedOutcome.element2.ourValue);
	}

	@Test
	public void testParseBoundCodeDtJson() {
		IParser jsonParser = FhirContext.forDstu2().newJsonParser();

		TestOutcome outcome = new TestOutcome();
		outcome.setElement(new BoundCodeDt<OutcomeEnum>(new OutcomeBinder(), OutcomeEnum.ITEM1));

		String xmlResource = jsonParser.encodeResourceToString(outcome);
		TestOutcome operationOutcome = jsonParser.parseResource(TestOutcome.class, xmlResource);

		assertNotNull(operationOutcome.getElement());
		assertTrue(operationOutcome.getElement() instanceof BoundCodeDt);
		assertEquals(outcome.getElement(), operationOutcome.getElement());
	}

	@Test
	public void testParseBoundCodeDtXml() {
		IParser xmlParser = FhirContext.forDstu2().newXmlParser();

		TestOutcome outcome = new TestOutcome();
		outcome.setElement(new BoundCodeDt<OutcomeEnum>(new OutcomeBinder(), OutcomeEnum.ITEM1));

		String xmlResource = xmlParser.encodeResourceToString(outcome);
		TestOutcome operationOutcome = xmlParser.parseResource(TestOutcome.class, xmlResource);

		assertNotNull(operationOutcome.getElement());
		assertTrue(operationOutcome.getElement() instanceof BoundCodeDt);
		assertEquals(outcome.getElement(), operationOutcome.getElement());
	}

	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}

}
