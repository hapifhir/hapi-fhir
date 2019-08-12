package ca.uhn.fhir.rest.server;

/*-
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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

import ca.uhn.fhir.rest.server.method.OperationMethodBinding;
import ca.uhn.fhir.rest.server.method.SearchMethodBinding;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;

public class Bindings {
	private final IdentityHashMap<SearchMethodBinding, String> myNamedSearchMethodBindingToName;
	private final HashMap<String, List<SearchMethodBinding>> mySearchNameToBindings;
	private final HashMap<String, List<OperationMethodBinding>> myOperationNameToBindings;
	private final IdentityHashMap<OperationMethodBinding, String> myOperationBindingToName;

	public Bindings(IdentityHashMap<SearchMethodBinding, String> theNamedSearchMethodBindingToName, HashMap<String, List<SearchMethodBinding>> theSearchNameToBindings, HashMap<String, List<OperationMethodBinding>> theOperationNameToBindings, IdentityHashMap<OperationMethodBinding, String> theOperationBindingToName) {
		myNamedSearchMethodBindingToName = theNamedSearchMethodBindingToName;
		mySearchNameToBindings = theSearchNameToBindings;
		myOperationNameToBindings = theOperationNameToBindings;
		myOperationBindingToName = theOperationBindingToName;
	}

	public IdentityHashMap<SearchMethodBinding, String> getNamedSearchMethodBindingToName() {
		return myNamedSearchMethodBindingToName;
	}

	public HashMap<String, List<SearchMethodBinding>> getSearchNameToBindings() {
		return mySearchNameToBindings;
	}

	public HashMap<String, List<OperationMethodBinding>> getOperationNameToBindings() {
		return myOperationNameToBindings;
	}

	public IdentityHashMap<OperationMethodBinding, String> getOperationBindingToName() {
		return myOperationBindingToName;
	}
}
