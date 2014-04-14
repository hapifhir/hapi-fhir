package ca.uhn.fhir.narrative;

import java.util.ArrayList;
import java.util.List;

import ca.uhn.fhir.model.dstu.resource.Conformance;
import ca.uhn.fhir.rest.server.RestfulServer;


public class DefaultThymeleafNarrativeGenerator extends BaseThymeleafNarrativeGenerator implements INarrativeGenerator {

	static final String NARRATIVES_PROPERTIES = "classpath:ca/uhn/fhir/narrative/narratives.properties";
	static final String HAPISERVER_NARRATIVES_PROPERTIES = "classpath:ca/uhn/fhir/narrative/narratives-hapiserver.properties";
	
	private boolean myUseHapiServerConformanceNarrative;

	@Override
	protected List<String> getPropertyFile() {
		List<String> retVal=new ArrayList<String>();
		retVal.add(NARRATIVES_PROPERTIES);
		if (myUseHapiServerConformanceNarrative) {
			retVal.add(HAPISERVER_NARRATIVES_PROPERTIES);
		}
		return retVal;
	}

	/**
	 * If set to <code>true</code> (default is <code>false</code>) a special custom narrative for the
	 * {@link Conformance} resource will be provided, which is designed to be used with 
	 * HAPI {@link RestfulServer} instances. This narrative provides a friendly search
	 * page which can assist users of the service.
	 */
	public void setUseHapiServerConformanceNarrative(boolean theValue) {
		myUseHapiServerConformanceNarrative=theValue;
	}

	/**
	 * If set to <code>true</code> (default is <code>false</code>) a special custom narrative for the
	 * {@link Conformance} resource will be provided, which is designed to be used with 
	 * HAPI {@link RestfulServer} instances. This narrative provides a friendly search
	 * page which can assist users of the service.
	 */
	public boolean isUseHapiServerConformanceNarrative() {
		return myUseHapiServerConformanceNarrative;
	}

}
