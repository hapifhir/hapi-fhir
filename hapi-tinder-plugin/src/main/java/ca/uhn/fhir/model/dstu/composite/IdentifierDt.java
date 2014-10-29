















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

import java.util.List;

import ca.uhn.fhir.model.api.ICompositeDatatype;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.SimpleSetter;
import ca.uhn.fhir.model.base.composite.BaseIdentifierDt;
import ca.uhn.fhir.model.dstu.valueset.IdentifierUseEnum;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.UriDt;

/**
 * HAPI/FHIR <b>IdentifierDt</b> Datatype
 * (An identifier intended for computation)
 *
 * <p>
 * <b>Definition:</b>
 * A technical identifier - identifies some entity uniquely and unambiguously
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * Need to be able to identify things with confidence and be sure that the identification is not subject to misinterpretation
 * </p> 
 */
@DatatypeDef(name="IdentifierDt") 
public class IdentifierDt
        extends  BaseIdentifierDt         implements ICompositeDatatype
{

	/**
	 * Constructor
	 */
	public IdentifierDt() {
		// nothing
	}

	/**
	 * Creates a new identifier with the given system and value
	 */
	@SimpleSetter
	public IdentifierDt(@SimpleSetter.Parameter(name="theSystem") String theSystem, @SimpleSetter.Parameter(name="theValue") String theValue) {
		setSystem(theSystem);
		setValue(theValue);
	}

	/**
	 * Creates a new identifier with the given system and value
	 */
	@SimpleSetter
	public IdentifierDt(@SimpleSetter.Parameter(name="theUse") IdentifierUseEnum theUse, @SimpleSetter.Parameter(name="theSystem") String theSystem, @SimpleSetter.Parameter(name="theValue") String theValue, @SimpleSetter.Parameter(name="theLabel") String theLabel) {
		setUse(theUse);
		setSystem(theSystem);
		setValue(theValue);
		setLabel(theLabel);
	}
	
	@Override
	public String toString() {
		return "IdentifierDt[" + getValueAsQueryToken() + "]";
	}

	@Child(name="use", type=CodeDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="usual | official | temp | secondary (If known)",
		formalDefinition="The purpose of this identifier"
	)
	private BoundCodeDt<IdentifierUseEnum> myUse;
	
	@Child(name="label", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Description of identifier",
		formalDefinition="A text string for the identifier that can be displayed to a human so they can recognize the identifier"
	)
	private StringDt myLabel;
	
	@Child(name="system", type=UriDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="The namespace for the identifier",
		formalDefinition="Establishes the namespace in which set of possible id values is unique."
	)
	private UriDt mySystem;
	
	@Child(name="value", type=StringDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="The value that is unique",
		formalDefinition="The portion of the identifier typically displayed to the user and which is unique within the context of the system."
	)
	private StringDt myValue;
	
	@Child(name="period", type=PeriodDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="Time period when id is/was valid for use",
		formalDefinition="Time period during which identifier is/was valid for use"
	)
	private PeriodDt myPeriod;
	
	@Child(name="assigner", order=5, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Conformance.class	})
	@Description(
		shortDefinition="Organization that issued id (may be just text)",
		formalDefinition="Organization that issued/manages the identifier"
	)
	private ResourceReferenceDt myAssigner;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myUse,  myLabel,  mySystem,  myValue,  myPeriod,  myAssigner);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myUse, myLabel, mySystem, myValue, myPeriod, myAssigner);
	}

	/**
	 * Gets the value(s) for <b>use</b> (usual | official | temp | secondary (If known)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The purpose of this identifier
     * </p> 
	 */
	public BoundCodeDt<IdentifierUseEnum> getUse() {  
		if (myUse == null) {
			myUse = new BoundCodeDt<IdentifierUseEnum>(IdentifierUseEnum.VALUESET_BINDER);
		}
		return myUse;
	}

	/**
	 * Sets the value(s) for <b>use</b> (usual | official | temp | secondary (If known))
	 *
     * <p>
     * <b>Definition:</b>
     * The purpose of this identifier
     * </p> 
	 */
	public IdentifierDt setUse(BoundCodeDt<IdentifierUseEnum> theValue) {
		myUse = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>use</b> (usual | official | temp | secondary (If known))
	 *
     * <p>
     * <b>Definition:</b>
     * The purpose of this identifier
     * </p> 
	 */
	public IdentifierDt setUse(IdentifierUseEnum theValue) {
		getUse().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>label</b> (Description of identifier).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A text string for the identifier that can be displayed to a human so they can recognize the identifier
     * </p> 
	 */
	public StringDt getLabel() {  
		if (myLabel == null) {
			myLabel = new StringDt();
		}
		return myLabel;
	}

	/**
	 * Sets the value(s) for <b>label</b> (Description of identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * A text string for the identifier that can be displayed to a human so they can recognize the identifier
     * </p> 
	 */
	public IdentifierDt setLabel(StringDt theValue) {
		myLabel = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>label</b> (Description of identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * A text string for the identifier that can be displayed to a human so they can recognize the identifier
     * </p> 
	 */
	public IdentifierDt setLabel( String theString) {
		myLabel = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>system</b> (The namespace for the identifier).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Establishes the namespace in which set of possible id values is unique.
     * </p> 
	 */
	public UriDt getSystemElement() {  
		if (mySystem == null) {
			mySystem = new UriDt();
		}
		return mySystem;
	}

	/**
	 * Sets the value(s) for <b>system</b> (The namespace for the identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * Establishes the namespace in which set of possible id values is unique.
     * </p> 
	 */
	public IdentifierDt setSystem(UriDt theValue) {
		mySystem = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>system</b> (The namespace for the identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * Establishes the namespace in which set of possible id values is unique.
     * </p> 
	 */
	public IdentifierDt setSystem( String theUri) {
		mySystem = new UriDt(theUri); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>value</b> (The value that is unique).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The portion of the identifier typically displayed to the user and which is unique within the context of the system.
     * </p> 
	 */
	public StringDt getValueElement() {  
		if (myValue == null) {
			myValue = new StringDt();
		}
		return myValue;
	}

	/**
	 * Sets the value(s) for <b>value</b> (The value that is unique)
	 *
     * <p>
     * <b>Definition:</b>
     * The portion of the identifier typically displayed to the user and which is unique within the context of the system.
     * </p> 
	 */
	public IdentifierDt setValue(StringDt theValue) {
		myValue = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>value</b> (The value that is unique)
	 *
     * <p>
     * <b>Definition:</b>
     * The portion of the identifier typically displayed to the user and which is unique within the context of the system.
     * </p> 
	 */
	public IdentifierDt setValue( String theString) {
		myValue = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>period</b> (Time period when id is/was valid for use).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Time period during which identifier is/was valid for use
     * </p> 
	 */
	public PeriodDt getPeriod() {  
		if (myPeriod == null) {
			myPeriod = new PeriodDt();
		}
		return myPeriod;
	}

	/**
	 * Sets the value(s) for <b>period</b> (Time period when id is/was valid for use)
	 *
     * <p>
     * <b>Definition:</b>
     * Time period during which identifier is/was valid for use
     * </p> 
	 */
	public IdentifierDt setPeriod(PeriodDt theValue) {
		myPeriod = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>assigner</b> (Organization that issued id (may be just text)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Organization that issued/manages the identifier
     * </p> 
	 */
	public ResourceReferenceDt getAssigner() {  
		if (myAssigner == null) {
			myAssigner = new ResourceReferenceDt();
		}
		return myAssigner;
	}

	/**
	 * Sets the value(s) for <b>assigner</b> (Organization that issued id (may be just text))
	 *
     * <p>
     * <b>Definition:</b>
     * Organization that issued/manages the identifier
     * </p> 
	 */
	public IdentifierDt setAssigner(ResourceReferenceDt theValue) {
		myAssigner = theValue;
		return this;
	}

  


}
