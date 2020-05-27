package ca.uhn.fhir.empi.rules.json;

/*-
 * #%L
 * HAPI FHIR - Enterprise Master Patient Index
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class VectorMatchResultMap {
	private final EmpiRulesJson myEmpiRulesJson;
	private Map<Long, EmpiMatchResultEnum> myVectorToMatchResultMap = new HashMap<>();
	private Map<Long, String> myVectorToFieldMatchNamesMap = new HashMap<>();

	VectorMatchResultMap(EmpiRulesJson theEmpiRulesJson) {
		myEmpiRulesJson = theEmpiRulesJson;
		//no reason to hold the entire empirulesjson here
		initMap();
	}

	private void initMap() {
		for (Map.Entry<String, EmpiMatchResultEnum> entry : myEmpiRulesJson.getMatchResultMap().entrySet()) {
			put(entry.getKey(), entry.getValue());
		}
	}

	public EmpiMatchResultEnum get(Long theMatchVector) {
		return myVectorToMatchResultMap.get(theMatchVector);
	}

	private void put(String theFieldMatchNames, EmpiMatchResultEnum theMatchResult) {
		long vector = getVector(theFieldMatchNames);
		myVectorToFieldMatchNamesMap.put(vector, theFieldMatchNames);
		myVectorToMatchResultMap.put(vector, theMatchResult);
	}

	public long getVector(String theFieldMatchNames) {
		long retval = 0;
		for (String fieldMatchName : splitFieldMatchNames(theFieldMatchNames)) {
			int index = getFieldMatchIndex(fieldMatchName);
			if (index == -1) {
				throw new IllegalArgumentException("There is no matchField with name " + fieldMatchName);
			}
			retval |= (1 << index);
		}
		return retval;
	}

	@Nonnull
	static String[] splitFieldMatchNames(String theFieldMatchNames) {
		return theFieldMatchNames.split(",\\s*");
	}

	private int getFieldMatchIndex(final String theFieldMatchName) {
		for (int i = 0; i < myEmpiRulesJson.size(); ++i) {
			if (myEmpiRulesJson.get(i).getName().equals(theFieldMatchName)) {
				return i;
			}
		}
		return -1;
	}

    public String getFieldMatchNames(long theVector) {
		return myVectorToFieldMatchNamesMap.get(theVector);
    }
}
