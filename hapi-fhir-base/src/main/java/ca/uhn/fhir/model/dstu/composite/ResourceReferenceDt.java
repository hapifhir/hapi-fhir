















package ca.uhn.fhir.model.dstu.composite;

import java.util.*;
import ca.uhn.fhir.model.api.*;
import ca.uhn.fhir.model.api.annotation.*;
import ca.uhn.fhir.model.primitive.*;
import ca.uhn.fhir.model.dstu.valueset.*;
import ca.uhn.fhir.model.dstu.resource.*;

/**
 * HAPI/FHIR <b>ResourceReference</b> Datatype
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
@DatatypeDef(name="ResourceReference") 
public class ResourceReferenceDt 
        extends  BaseResourceReference         implements ICompositeDatatype  {


	@Child(name="reference", type=StringDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="Relative, internal or absolute URL reference",
		formalDefinition="A reference to a location at which the other resource is found. The reference may a relative reference, in which case it is relative to the service base URL, or an absolute URL that resolves to the location where the resource is found. The reference may be version specific or not. If the reference is not to a FHIR RESTful server, then it should be assumed to be version specific. Internal fragment references (start with '#') refer to contained resources"
	)
	private StringDt myReference;
	
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
	public StringDt getReference() {  
		if (myReference == null) {
			myReference = new StringDt();
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
	public void setReference(StringDt theValue) {
		myReference = theValue;
	}


 	/**
	 * Sets the value for <b>reference</b> (Relative, internal or absolute URL reference)
	 *
     * <p>
     * <b>Definition:</b>
     * A reference to a location at which the other resource is found. The reference may a relative reference, in which case it is relative to the service base URL, or an absolute URL that resolves to the location where the resource is found. The reference may be version specific or not. If the reference is not to a FHIR RESTful server, then it should be assumed to be version specific. Internal fragment references (start with '#') refer to contained resources
     * </p> 
	 */
	public void setReference( String theString) {
		myReference = new StringDt(theString); 
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
	public void setDisplay(StringDt theValue) {
		myDisplay = theValue;
	}


 	/**
	 * Sets the value for <b>display</b> (Text alternative for the resource)
	 *
     * <p>
     * <b>Definition:</b>
     * Plain text narrative that identifies the resource in addition to the resource reference
     * </p> 
	 */
	public void setDisplay( String theString) {
		myDisplay = new StringDt(theString); 
	}

 



}