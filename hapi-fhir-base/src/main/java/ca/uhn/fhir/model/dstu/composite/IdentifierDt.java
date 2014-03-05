















package ca.uhn.fhir.model.dstu.composite;

import java.util.*;
import ca.uhn.fhir.model.api.*;
import ca.uhn.fhir.model.api.annotation.*;
import ca.uhn.fhir.model.primitive.*;
import ca.uhn.fhir.model.dstu.valueset.*;
import ca.uhn.fhir.model.dstu.resource.*;

/**
 * HAPI/FHIR <b>Identifier</b> Datatype
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
@DatatypeDef(name="Identifier") 
public class IdentifierDt extends BaseElement implements ICompositeDatatype {

	@Child(name="use", type=CodeDt.class, order=0, min=0, max=1)	
	private BoundCodeDt<IdentifierUseEnum> myUse;
	
	@Child(name="label", type=StringDt.class, order=1, min=0, max=1)	
	private StringDt myLabel;
	
	@Child(name="system", type=UriDt.class, order=2, min=0, max=1)	
	private UriDt mySystem;
	
	@Child(name="value", type=StringDt.class, order=3, min=0, max=1)	
	private StringDt myValue;
	
	@Child(name="period", type=PeriodDt.class, order=4, min=0, max=1)	
	private PeriodDt myPeriod;
	
	@Child(name="assigner", order=5, min=0, max=1)
	@ChildResource(types= {
		Organization.class,
	})	
	private ResourceReference myAssigner;
	
	/**
	 * Gets the value(s) for <b>use</b> (usual | official | temp | secondary (If known)
).
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
	 * Sets the value(s) for <b>use</b> (usual | official | temp | secondary (If known)
)
	 *
     * <p>
     * <b>Definition:</b>
     * The purpose of this identifier
     * </p> 
	 */
	public void setUse(BoundCodeDt<IdentifierUseEnum> theValue) {
		myUse = theValue;
	}

	/**
	 * Sets the value(s) for <b>use</b> (usual | official | temp | secondary (If known)
)
	 *
     * <p>
     * <b>Definition:</b>
     * The purpose of this identifier
     * </p> 
	 */
	public void setUse(IdentifierUseEnum theValue) {
		getUse().setValueAsEnum(theValue);
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
	public void setLabel(StringDt theValue) {
		myLabel = theValue;
	}

 	/**
	 * Sets the value(s) for <b>label</b> (Description of identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * A text string for the identifier that can be displayed to a human so they can recognize the identifier
     * </p> 
	 */
	public void setLabel( String theString) {
		myLabel = new StringDt(theString); 
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
	public UriDt getSystem() {  
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
	public void setSystem(UriDt theValue) {
		mySystem = theValue;
	}

 	/**
	 * Sets the value(s) for <b>system</b> (The namespace for the identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * Establishes the namespace in which set of possible id values is unique.
     * </p> 
	 */
	public void setSystem( String theUri) {
		mySystem = new UriDt(theUri); 
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
	public StringDt getValue() {  
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
	public void setValue(StringDt theValue) {
		myValue = theValue;
	}

 	/**
	 * Sets the value(s) for <b>value</b> (The value that is unique)
	 *
     * <p>
     * <b>Definition:</b>
     * The portion of the identifier typically displayed to the user and which is unique within the context of the system.
     * </p> 
	 */
	public void setValue( String theString) {
		myValue = new StringDt(theString); 
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
	public void setPeriod(PeriodDt theValue) {
		myPeriod = theValue;
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
	public ResourceReference getAssigner() {  
		if (myAssigner == null) {
			myAssigner = new ResourceReference();
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
	public void setAssigner(ResourceReference theValue) {
		myAssigner = theValue;
	}

  

	/**
	 * Returns true if <code>this</code> identifier has the same {@link IdentifierDt#getValue() value}
	 * and {@link IdentifierDt#getSystem() system} (as compared by simple equals comparison).
	 * Does not compare other values (e.g. {@link IdentifierDt#getUse() use}) or any extensions. 
	 */
	public boolean matchesSystemAndValue(IdentifierDt theIdentifier) {
		if (theIdentifier == null) {
			return false;
		}
		return getValue().equals(theIdentifier.getValue()) && getSystem().equals(theIdentifier.getSystem());
	}

	/**
	 * Sets the value of this <code>IdentifierDt</code> using the <b>token</b> format. This 
	 * format is used in HTTP queries as a parameter format.
	 * 
	 * @see See FHIR specification 
	 *    <a href="http://www.hl7.org/implement/standards/fhir/search.html#ptypes">2.2.2 Search Parameter Types</a>
	 *    for information on the <b>token</b> format
	 */
	public void setValueAsQueryToken(String theParameter) {
		int barIndex = theParameter.indexOf('|');
		if (barIndex != -1) {
			setSystem(new UriDt(theParameter.substring(0, barIndex)));
			setValue(theParameter.substring(barIndex + 1));
		} else {
			setValue(theParameter);
		}
	}	


}