/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.patch;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import org.hl7.fhir.instance.model.api.IBase;

/**
 * A helper object to hold the child definitions along a provided fhir path
 */
public class FhirPathChildDefinition {
	// our parent element; if null, this is the top level element
	private IBase myBase;

	private BaseRuntimeChildDefinition myBaseRuntimeDefinition;

	private BaseRuntimeElementDefinition<?> myElementDefinition;

	private String myFhirPath;

	private FhirPathChildDefinition myChild;

	private FhirPathChildDefinition myParent;

	public IBase getBase() {
		return myBase;
	}

	public void setBase(IBase theBase) {
		myBase = theBase;
	}

	public BaseRuntimeChildDefinition getBaseRuntimeDefinition() {
		return myBaseRuntimeDefinition;
	}

	public void setBaseRuntimeDefinition(BaseRuntimeChildDefinition theChildDefinition) {
		myBaseRuntimeDefinition = theChildDefinition;
	}

	public BaseRuntimeElementDefinition<?> getElementDefinition() {
		return myElementDefinition;
	}

	public void setElementDefinition(BaseRuntimeElementDefinition<?> theElementDefinition) {
		myElementDefinition = theElementDefinition;
	}

	public String getFhirPath() {
		return myFhirPath;
	}

	public void setFhirPath(String theFhirPath) {
		myFhirPath = theFhirPath;
	}

	public FhirPathChildDefinition getChild() {
		return myChild;
	}

	public void setChild(FhirPathChildDefinition theChild) {
		myChild = theChild;
		if (myChild != null) {
			myChild.setParent(this);
		}
	}

	public FhirPathChildDefinition getParent() {
		return myParent;
	}

	public void setParent(FhirPathChildDefinition theParent) {
		myParent = theParent;
	}
}
