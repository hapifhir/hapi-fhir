package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.TranslateConceptResults;
import ca.uhn.fhir.jpa.dao.r5.BaseJpaR5Test;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r5.model.Coding;
import org.hl7.fhir.r5.model.ConceptMap;
import org.hl7.fhir.r5.model.Enumerations;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SuppressWarnings("LoggingSimilarMessage")
public class TermConceptMappingSvcImplR5Test extends BaseJpaR5Test {
	private static final Logger ourLog = LoggerFactory.getLogger(TermConceptMappingSvcImplR5Test.class);

	private IIdType myConceptMapId;


	@Test
	public void testTranslateCode() {
		ConceptMap cm = new ConceptMap();
		cm.setId("foo-to-bar");
		cm.setUrl("http://foo-to-bar");
		ConceptMap.ConceptMapGroupComponent group = cm.addGroup();
		group.setSource("http://foo");
		group.setTarget("http://bar");
		group.addElement()
			.setCode("1")
			.addTarget(new ConceptMap.TargetElementComponent().setCode("MILLION").setDisplay("One million").setRelationship(Enumerations.ConceptMapRelationship.EQUIVALENT));
		myConceptMapDao.update(cm, newSrd());

		List<IBaseCoding> codings= List.of(
			new Coding().setSystem("http://foo").setCode("1")
		);
		IValidationSupport.TranslateCodeRequest request = new IValidationSupport.TranslateCodeRequest(codings, "http://bar");
		TranslateConceptResults outcome = myValidationSupport.translateConcept(request);

		assertNotNull(outcome);
		assertEquals(1, outcome.getResults().size());
		assertEquals("MILLION", outcome.getResults().get(0).getCode());
	}


}
