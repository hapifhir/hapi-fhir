package ca.uhn.fhir.model.dstu2.resource;

/*
 * #%L
 * HAPI FHIR Structures - DSTU2 (FHIR v0.4.0)
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hl7.fhir.instance.model.api.IIdType;

import ca.uhn.fhir.model.api.BaseElement;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.base.resource.ResourceMetadataMap;
import ca.uhn.fhir.model.dstu2.composite.ContainedDt;
import ca.uhn.fhir.model.dstu2.composite.NarrativeDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.gclient.StringClientParam;
import ca.uhn.fhir.util.ElementUtil;

public abstract class BaseResource extends BaseElement implements IResource {

	/**
	 * Search parameter constant for <b>_language</b>
	 */
	@SearchParamDefinition(name="_language", path="", description="The language of the resource", type="string"  )
	public static final String SP_RES_LANGUAGE = "_language";


	/**
	 * Search parameter constant for <b>_id</b>
	 */
	@SearchParamDefinition(name="_id", path="", description="The ID of the resource", type="string"  )
	public static final String SP_RES_ID = "_id";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>_id</b>
	 * <p>
	 * Description: <b>the _id of a resource</b><br>
	 * Type: <b>string</b><br>
	 * Path: <b>Resource._id</b><br>
	 * </p>
	 */
	public static final StringClientParam RES_ID = new StringClientParam(BaseResource.SP_RES_ID);


	@Child(name = "contained", order = 2, min = 0, max = 1)
	private ContainedDt myContained;

	private IdDt myId;

	@Child(name = "language", order = 0, min = 0, max = Child.MAX_UNLIMITED)
	private CodeDt myLanguage;

	private ResourceMetadataMap myResourceMetadata;

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

	@Override
	public IIdType getIdElement() {
		return getId();
	}

	@Override
	public CodeDt getLanguage() {
		if (myLanguage == null) {
			myLanguage = new CodeDt();
		}
		return myLanguage;
	}

	@Override
	public ResourceMetadataMap getResourceMetadata() {
		if (myResourceMetadata == null) {
			myResourceMetadata = new ResourceMetadataMap();
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
	
	public BaseResource setId(IIdType theId) {
		if (theId instanceof IdDt) {
			myId = (IdDt) theId;
		} else if (theId != null) {
			myId = new IdDt(theId.getValue());
		}
		return this;
	}

	public BaseResource setId(String theId) {
		if (theId == null) {
			myId = null;
		} else {
			myId = new IdDt(theId);
		}
		return this;
	}

	@Override
	public void setLanguage(CodeDt theLanguage) {
		myLanguage = theLanguage;
	}

	@Override
	public void setResourceMetadata(ResourceMetadataMap theMap) {
		Validate.notNull(theMap, "The Map must not be null");
		myResourceMetadata = theMap;
	}

	public void setText(NarrativeDt theText) {
		myText = theText;
	}

	@Override
	public String toString() {
		ToStringBuilder b = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		b.append("id", getId().toUnqualified());
		return b.toString();
	}

	/**
	 * Intended to be called by extending classes {@link #isEmpty()} implementations, returns <code>true</code> if all
	 * content in this superclass instance is empty per the semantics of {@link #isEmpty()}.
	 */
	@Override
	protected boolean isBaseEmpty() {
		return super.isBaseEmpty() && ElementUtil.isEmpty(myLanguage, myText, myId);
	}

}
