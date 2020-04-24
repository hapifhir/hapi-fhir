package ca.uhn.fhir.rest.server;

/*
 * #%L
 * HAPI FHIR - Server Framework
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

import java.util.ArrayList;
import java.util.List;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.method.BaseMethodBinding;
import ca.uhn.fhir.rest.server.method.MethodMatchEnum;

/**
 * Holds all method bindings for an individual resource type
 */
public class ResourceBinding {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceBinding.class);

	private String resourceName;
	private List<BaseMethodBinding<?>> myMethodBindings = new ArrayList<>();

	/**
	 * Constructor
	 */
	public ResourceBinding() {
		super();
	}

	/**
	 * Constructor
	 */
	public ResourceBinding(String resourceName, List<BaseMethodBinding<?>> methods) {
		this.resourceName = resourceName;
		this.myMethodBindings = methods;
	}

	public BaseMethodBinding<?> getMethod(RequestDetails theRequest) {
		if (null == myMethodBindings) {
			ourLog.warn("No methods exist for resource: {}", resourceName);
			return null;
		}

		ourLog.debug("Looking for a handler for {}", theRequest);

		/*
		 * Look for the method with the highest match strength
		 */

		BaseMethodBinding<?> matchedMethod = null;
		MethodMatchEnum matchedMethodStrength = null;

		for (BaseMethodBinding<?> rm : myMethodBindings) {
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
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public List<BaseMethodBinding<?>> getMethodBindings() {
		return myMethodBindings;
	}

	public void setMethods(List<BaseMethodBinding<?>> methods) {
		this.myMethodBindings = methods;
	}

	public void addMethod(BaseMethodBinding<?> method) {
		this.myMethodBindings.add(method);
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof ResourceBinding))
			return false;
		return resourceName.equals(((ResourceBinding) o).getResourceName());
	}

	@Override
	public int hashCode() {
		return 0;
	}

}
