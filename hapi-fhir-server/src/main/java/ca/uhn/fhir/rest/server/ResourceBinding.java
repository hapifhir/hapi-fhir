/*
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.method.BaseMethodBinding;
import ca.uhn.fhir.rest.server.method.MethodMatchEnum;

import java.util.LinkedList;
import java.util.List;

/**
 * Holds all method bindings for an individual resource type
 */
public class ResourceBinding {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceBinding.class);

	private String myResourceName;
	private final LinkedList<BaseMethodBinding> myMethodBindings = new LinkedList<>();

	/**
	 * Constructor
	 */
	public ResourceBinding() {
		super();
	}

	public BaseMethodBinding getMethod(RequestDetails theRequest) {
		if (myMethodBindings.isEmpty()) {
			ourLog.warn("No methods exist for resource: {}", myResourceName);
			return null;
		}

		ourLog.debug("Looking for a handler for {}", theRequest);

		/*
		 * Look for the method with the highest match strength
		 */

		BaseMethodBinding matchedMethod = null;
		MethodMatchEnum matchedMethodStrength = null;

		for (BaseMethodBinding rm : myMethodBindings) {
			MethodMatchEnum nextMethodMatch = rm.incomingServerRequestMatchesMethod(theRequest);
			if (nextMethodMatch != MethodMatchEnum.NONE) {
				if (matchedMethodStrength == null || matchedMethodStrength.ordinal() < nextMethodMatch.ordinal()) {
					matchedMethod = rm;
					matchedMethodStrength = nextMethodMatch;
				}
				if (matchedMethodStrength == MethodMatchEnum.EXACT) {
					break;
				}
			}
		}

		return matchedMethod;
	}

	public String getResourceName() {
		return myResourceName;
	}

	public void setResourceName(String resourceName) {
		this.myResourceName = resourceName;
	}

	public List<BaseMethodBinding> getMethodBindings() {
		return myMethodBindings;
	}

	public void addMethod(BaseMethodBinding method) {
		if (myMethodBindings.stream()
				.anyMatch(
						t -> t.getMethod().toString().equals(method.getMethod().toString()))) {
			ourLog.warn(
					"The following method has been registered twice against this RestfulServer: {}",
					method.getMethod());
		}
		this.myMethodBindings.push(method);
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof ResourceBinding)) return false;
		return myResourceName.equals(((ResourceBinding) o).getResourceName());
	}

	@Override
	public int hashCode() {
		return 0;
	}
}
