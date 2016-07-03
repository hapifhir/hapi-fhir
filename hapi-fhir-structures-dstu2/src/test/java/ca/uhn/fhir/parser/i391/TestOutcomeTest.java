package ca.uhn.fhir.parser.i391;

import static org.junit.Assert.*;

import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.parser.IParser;

/**
 * See #391
 */
public class TestOutcomeTest {

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
} 