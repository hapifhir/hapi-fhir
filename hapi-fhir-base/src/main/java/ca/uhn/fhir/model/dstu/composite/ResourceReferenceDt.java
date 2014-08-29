















package ca.uhn.fhir.model.dstu.composite;

/*
 * #%L
 * HAPI FHIR - Core Library
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

import java.math.BigDecimal;
import org.apache.commons.lang3.StringUtils;
import java.util.*;
import ca.uhn.fhir.model.api.*;
import ca.uhn.fhir.model.primitive.*;
import ca.uhn.fhir.model.api.annotation.*;
import ca.uhn.fhir.model.base.composite.*;

import ca.uhn.fhir.model.dstu.valueset.AddressUseEnum;
import ca.uhn.fhir.model.dstu.composite.CodingDt;
import ca.uhn.fhir.model.dstu.valueset.ContactSystemEnum;
import ca.uhn.fhir.model.dstu.valueset.ContactUseEnum;
import ca.uhn.fhir.model.dstu.valueset.EventTimingEnum;
import ca.uhn.fhir.model.dstu.valueset.IdentifierUseEnum;
import ca.uhn.fhir.model.dstu.valueset.NameUseEnum;
import ca.uhn.fhir.model.dstu.resource.Organization;
import ca.uhn.fhir.model.dstu.composite.PeriodDt;
import ca.uhn.fhir.model.dstu.valueset.QuantityCompararatorEnum;
import ca.uhn.fhir.model.dstu.composite.QuantityDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.dstu.valueset.UnitsOfTimeEnum;
import ca.uhn.fhir.model.dstu.resource.ValueSet;
import ca.uhn.fhir.model.primitive.Base64BinaryDt;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.DecimalDt;
import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.UriDt;

/**
 * HAPI/FHIR <b>ResourceReferenceDt</b> Datatype
 * (A reference from one resource to another)
 *
 * <p>
 * <b>Definition:</b>
 * A reference from one resource to another
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 */
@DatatypeDef(name="ResourceReferenceDt") 
public class ResourceReferenceDt
        extends  BaseResourceReferenceDt         implements ICompositeDatatype
{

	/**
	 * Constructor
	 */
	public ResourceReferenceDt() {
		// nothing
	}

	/**
	 * Constructor which creates a resource reference containing the actual resource in question.
	 * <p>
	 * <b> When using this in a server:</b> Generally if this is serialized, it will be serialized as a contained
	 * resource, so this should not be used if the intent is not to actually supply the referenced resource. This is not
	 * a hard-and-fast rule however, as the server can be configured to not serialized this resource, or to load an ID
	 * and contain even if this constructor is not used.
	 * </p>
	 * 
	 * @param theResource
	 *            The resource instance
	 */
	public ResourceReferenceDt(IResource theResource) {
		super(theResource);
	}

	/**
	 * Constructor which accepts a reference directly (this can be an ID, a partial/relative URL or a complete/absolute
	 * URL)
	 * 
	 * @param theId
	 *            The reference itself
	 */
	public ResourceReferenceDt(String theId) {
		setReference(new IdDt(theId));
	}

	/**
	 * Constructor which accepts a reference directly (this can be an ID, a partial/relative URL or a complete/absolute
	 * URL)
	 * 
	 * @param theId
	 *            The reference itself
	 */
	public ResourceReferenceDt(IdDt theResourceId) {
		setReference(theResourceId);
	}

	@Child(name="reference", type=IdDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="Relative, internal or absolute URL reference",
		formalDefinition="A reference to a location at which the other resource is found. The reference may a relative reference, in which case it is relative to the service base URL, or an absolute URL that resolves to the location where the resource is found. The reference may be version specific or not. If the reference is not to a FHIR RESTful server, then it should be assumed to be version specific. Internal fragment references (start with '#') refer to contained resources"
	)
	private IdDt myReference;
	
	@Child(name="display", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Text alternative for the resource",
		formalDefinition="Plain text narrative that identifies the resource in addition to the resource reference"
	)
	private StringDt myDisplay;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myReference,  myDisplay);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myReference, myDisplay);
	}

	/**
	 * Gets the value(s) for <b>reference</b> (Relative, internal or absolute URL reference).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A reference to a location at which the other resource is found. The reference may a relative reference, in which case it is relative to the service base URL, or an absolute URL that resolves to the location where the resource is found. The reference may be version specific or not. If the reference is not to a FHIR RESTful server, then it should be assumed to be version specific. Internal fragment references (start with '#') refer to contained resources
     * </p> 
	 */
	public IdDt getReference() {  
		if (myReference == null) {
			myReference = new IdDt();
		}
		return myReference;
	}

	/**
	 * Sets the value(s) for <b>reference</b> (Relative, internal or absolute URL reference)
	 *
     * <p>
     * <b>Definition:</b>
     * A reference to a location at which the other resource is found. The reference may a relative reference, in which case it is relative to the service base URL, or an absolute URL that resolves to the location where the resource is found. The reference may be version specific or not. If the reference is not to a FHIR RESTful server, then it should be assumed to be version specific. Internal fragment references (start with '#') refer to contained resources
     * </p> 
	 */
	public ResourceReferenceDt setReference(IdDt theValue) {
		myReference = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>reference</b> (Relative, internal or absolute URL reference)
	 *
     * <p>
     * <b>Definition:</b>
     * A reference to a location at which the other resource is found. The reference may a relative reference, in which case it is relative to the service base URL, or an absolute URL that resolves to the location where the resource is found. The reference may be version specific or not. If the reference is not to a FHIR RESTful server, then it should be assumed to be version specific. Internal fragment references (start with '#') refer to contained resources
     * </p> 
	 */
	public ResourceReferenceDt setReference( String theId) {
		myReference = new IdDt(theId); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>display</b> (Text alternative for the resource).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Plain text narrative that identifies the resource in addition to the resource reference
     * </p> 
	 */
	public StringDt getDisplay() {  
		if (myDisplay == null) {
			myDisplay = new StringDt();
		}
		return myDisplay;
	}

	/**
	 * Sets the value(s) for <b>display</b> (Text alternative for the resource)
	 *
     * <p>
     * <b>Definition:</b>
     * Plain text narrative that identifies the resource in addition to the resource reference
     * </p> 
	 */
	public ResourceReferenceDt setDisplay(StringDt theValue) {
		myDisplay = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>display</b> (Text alternative for the resource)
	 *
     * <p>
     * <b>Definition:</b>
     * Plain text narrative that identifies the resource in addition to the resource reference
     * </p> 
	 */
	public ResourceReferenceDt setDisplay( String theString) {
		myDisplay = new StringDt(theString); 
		return this; 
	}

 


}