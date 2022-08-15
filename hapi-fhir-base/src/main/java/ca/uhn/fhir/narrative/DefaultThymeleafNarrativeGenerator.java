package ca.uhn.fhir.narrative;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.ArrayList;
import java.util.List;

public class DefaultThymeleafNarrativeGenerator extends BaseThymeleafNarrativeGenerator implements INarrativeGenerator {

	public static final String NARRATIVES_PROPERTIES = "classpath:ca/uhn/fhir/narrative/narratives.properties";
	static final String HAPISERVER_NARRATIVES_PROPERTIES = "classpath:ca/uhn/fhir/narrative/narratives-hapiserver.properties";

	private boolean myUseHapiServerConformanceNarrative;

	public DefaultThymeleafNarrativeGenerator() {
		super();
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
