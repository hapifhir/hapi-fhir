package ca.uhn.fhir.r4.narrative;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.narrative.BaseNarrativeGenerator;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.INarrative;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.utils.LiquidEngine;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LiquidNarrativeGenerator extends BaseNarrativeGenerator {
	private final LiquidEngine myLiquidEngine;
	private final Map<String, LiquidEngine.LiquidDocument> myTemplateMap = new HashMap<>();
	private boolean myInitialized;

	public LiquidNarrativeGenerator(org.hl7.fhir.r4.utils.LiquidEngine theLiquidEngine) {
		myLiquidEngine = theLiquidEngine;
	}

	@Override
	public void generateNarrative(FhirContext theContext, IBaseResource theResource, INarrative theNarrative) {
		String name = getName(theContext, theResource);
		if (name == null) return;

		String testTemplate = "<div class=\"hapiHeaderText\">{{ Medication.code.text }}</div>";
		LiquidEngine.LiquidDocument doc = null;
		try {
			doc = myLiquidEngine.parse(testTemplate, "test-template");
			// TODO KHS remove this cast
			String output = myLiquidEngine.evaluate(doc, (Resource) theResource, null);
			theNarrative.setDivAsString(output);
		} catch (Exception e) {
			// FIXME KHS
			theNarrative.setStatusAsString("failed");
		}
	}

	@Override
	protected List<String> getPropertyFile() {
		return Collections.EMPTY_LIST;
	}

	@Override
	protected void initializeNarrativeEngine(FhirContext theFhirContext) {

	}
}
