package org.hl7.fhir.utilities.liquid;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.narrative.BaseNarrativeGenerator;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class DefaultLiquidNarrativeGenerator extends BaseNarrativeGenerator {
	private static final Logger ourLog = LoggerFactory.getLogger(DefaultLiquidNarrativeGenerator.class);


	public static final String NARRATIVES_PROPERTIES = "classpath:ca/uhn/fhir/narrative/liquid/narratives.properties";
	static final String HAPISERVER_NARRATIVES_PROPERTIES = "classpath:ca/uhn/fhir/narrative/liquid/narratives-hapiserver.properties";

	private boolean myUseHapiServerConformanceNarrative;

	private LiquidEngine myLiquidEngine;
	private Object myHostServices;

	@Override
	protected void initializeNarrativeEngine(FhirContext theFhirContext) {
		myLiquidEngine = new LiquidEngine(theFhirContext);
		myLiquidEngine.setHostServices(myHostServices);
	}

	@Override
	protected String processNamedTemplate(FhirContext theFhirContext, String theName, IBaseResource theResource) throws Exception {
		String template = getNarrativeTemplate(theName);
		return processTemplate(theFhirContext, theName, theResource, template);
	}

	private String processTemplate(FhirContext theFhirContext, String theName, IBaseResource theResource, String theTemplate) throws Exception {
		LiquidEngine.LiquidDocument doc = myLiquidEngine.parse(theTemplate, theName);
		return myLiquidEngine.evaluate(doc, theResource, null);
	}

	@Override
	protected List<String> getPropertyFile() {
		List<String> retVal = new ArrayList<String>();
		retVal.add(NARRATIVES_PROPERTIES);
		if (myUseHapiServerConformanceNarrative) {
			retVal.add(HAPISERVER_NARRATIVES_PROPERTIES);
		}
		return retVal;
	}

	@Override
	public void setHostServices(Object theHostServices) {
		myHostServices = theHostServices;
	}

	/**
	 * If set to <code>true</code> (default is <code>false</code>) a special custom narrative for the Conformance resource will be provided, which is designed to be used with HAPI {@link RestfulServer}
	 * instances. This narrative provides a friendly search page which can assist users of the service.
	 */
	public void setUseHapiServerConformanceNarrative(boolean theValue) {
		myUseHapiServerConformanceNarrative = theValue;
	}

	/**
	 * If set to <code>true</code> (default is <code>false</code>) a special custom narrative for the Conformance resource will be provided, which is designed to be used with HAPI {@link RestfulServer}
	 * instances. This narrative provides a friendly search page which can assist users of the service.
	 */
	public boolean isUseHapiServerConformanceNarrative() {
		return myUseHapiServerConformanceNarrative;
	}
}
