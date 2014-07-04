package ca.uhn.fhir.model.api;

/*
 * #%L
 * HAPI FHIR Library
 * %%
 * Copyright (C) 2014 University Health Network
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

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.Validate;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.dstu.composite.ContainedDt;
import ca.uhn.fhir.model.dstu.composite.NarrativeDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.util.ElementUtil;

public abstract class BaseResource extends BaseElement implements IResource {

	@Child(name = "contained", order = 2, min = 0, max = 1)
	private ContainedDt myContained;

	private IdDt myId;

	@Child(name = "language", order = 0, min = 0, max = Child.MAX_UNLIMITED)
	private CodeDt myLanguage;

	private Map<ResourceMetadataKeyEnum<?>, Object> myResourceMetadata;

	@Child(name = "text", order = 1, min = 0, max = 1)
	private NarrativeDt myText;

	@Override
	public ContainedDt getContained() {
		if (myContained == null) {
			myContained = new ContainedDt();
		}
		return myContained;
	}

	public IdDt getId() {
		if (myId == null) {
			myId = new IdDt();
		}
		return myId;
	}

	public CodeDt getLanguage() {
		return myLanguage;
	}

	@Override
	public Map<ResourceMetadataKeyEnum<?>, Object> getResourceMetadata() {
		if (myResourceMetadata == null) {
			myResourceMetadata = new HashMap<ResourceMetadataKeyEnum<?>, Object>();
		}
		return myResourceMetadata;
	}

	@Override
	public NarrativeDt getText() {
		if (myText == null) {
			myText = new NarrativeDt();
		}
		return myText;
	}

	public void setContained(ContainedDt theContained) {
		myContained = theContained;
	}

	public void setId(IdDt theId) {
		myId = theId;
	}

	public void setId(String theId) {
		if (theId == null) {
			myId = null;
		} else {
			myId = new IdDt(theId);
		}
	}

	public void setLanguage(CodeDt theLanguage) {
		myLanguage = theLanguage;
	}

	@Override
	public void setResourceMetadata(Map<ResourceMetadataKeyEnum<?>, Object> theMap) {
		Validate.notNull(theMap, "The Map must not be null");
		myResourceMetadata = theMap;
	}

	public void setText(NarrativeDt theText) {
		myText = theText;
	}

	/**
	 * Intended to be called by extending classes {@link #isEmpty()} implementations, returns <code>true</code> if all
	 * content in this superclass instance is empty per the semantics of {@link #isEmpty()}.
	 */
	@Override
	protected boolean isBaseEmpty() {
		return super.isBaseEmpty() && ElementUtil.isEmpty(myLanguage, myText);
	}

}
