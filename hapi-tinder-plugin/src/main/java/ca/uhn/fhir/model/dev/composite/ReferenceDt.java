















package ca.uhn.fhir.model.dev.composite;

import java.net.URI;
import java.math.BigDecimal;
import org.apache.commons.lang3.StringUtils;
import java.util.*;
import ca.uhn.fhir.model.api.*;
import ca.uhn.fhir.model.primitive.*;
import ca.uhn.fhir.model.api.annotation.*;
import ca.uhn.fhir.model.base.composite.*;

import ca.uhn.fhir.model.dev.valueset.AddressUseEnum;
import ca.uhn.fhir.model.dev.valueset.AggregationModeEnum;
import ca.uhn.fhir.model.dev.valueset.BindingConformanceEnum;
import ca.uhn.fhir.model.dev.composite.CodingDt;
import ca.uhn.fhir.model.dev.valueset.ConstraintSeverityEnum;
import ca.uhn.fhir.model.dev.valueset.ContactPointSystemEnum;
import ca.uhn.fhir.model.dev.valueset.ContactPointUseEnum;
import ca.uhn.fhir.model.dev.valueset.DataTypeEnum;
import ca.uhn.fhir.model.dev.valueset.EventTimingEnum;
import ca.uhn.fhir.model.dev.valueset.IdentifierUseEnum;
import ca.uhn.fhir.model.dev.valueset.NameUseEnum;
import ca.uhn.fhir.model.dev.resource.Organization;
import ca.uhn.fhir.model.dev.composite.PeriodDt;
import ca.uhn.fhir.model.dev.valueset.PropertyRepresentationEnum;
import ca.uhn.fhir.model.dev.valueset.QuantityComparatorEnum;
import ca.uhn.fhir.model.dev.composite.QuantityDt;
import ca.uhn.fhir.model.dev.valueset.SlicingRulesEnum;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.dev.valueset.UnitsOfTimeEnum;
import ca.uhn.fhir.model.dev.resource.ValueSet;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.primitive.Base64BinaryDt;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.DecimalDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.UriDt;

/**
 * HAPI/FHIR <b>ReferenceDt</b> Datatype
 * (Reference)
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
@DatatypeDef(name="ReferenceDt") 
public class ReferenceDt
        extends  BaseIdentifiableElement         implements ICompositeDatatype
{

	/**
	 * Constructor
	 */
	public ReferenceDt() {
		// nothing
	}


	@Child(name="reference", type=StringDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="Reference.reference",
		formalDefinition="A reference to a location at which the other resource is found. The reference may a relative reference, in which case it is relative to the service base URL, or an absolute URL that resolves to the location where the resource is found. The reference may be version specific or not. If the reference is not to a FHIR RESTful server, then it should be assumed to be version specific. Internal fragment references (start with '#') refer to contained resources"
	)
	private StringDt myReference;
	
	@Child(name="display", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Reference.display",
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
	 * Gets the value(s) for <b>reference</b> (Reference.reference).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A reference to a location at which the other resource is found. The reference may a relative reference, in which case it is relative to the service base URL, or an absolute URL that resolves to the location where the resource is found. The reference may be version specific or not. If the reference is not to a FHIR RESTful server, then it should be assumed to be version specific. Internal fragment references (start with '#') refer to contained resources
     * </p> 
	 */
	public StringDt getReferenceElement() {  
		if (myReference == null) {
			myReference = new StringDt();
		}
		return myReference;
	}

	
	/**
	 * Gets the value(s) for <b>reference</b> (Reference.reference).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A reference to a location at which the other resource is found. The reference may a relative reference, in which case it is relative to the service base URL, or an absolute URL that resolves to the location where the resource is found. The reference may be version specific or not. If the reference is not to a FHIR RESTful server, then it should be assumed to be version specific. Internal fragment references (start with '#') refer to contained resources
     * </p> 
	 */
	public String getReference() {  
		return getReferenceElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>reference</b> (Reference.reference)
	 *
     * <p>
     * <b>Definition:</b>
     * A reference to a location at which the other resource is found. The reference may a relative reference, in which case it is relative to the service base URL, or an absolute URL that resolves to the location where the resource is found. The reference may be version specific or not. If the reference is not to a FHIR RESTful server, then it should be assumed to be version specific. Internal fragment references (start with '#') refer to contained resources
     * </p> 
	 */
	public ReferenceDt setReference(StringDt theValue) {
		myReference = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>reference</b> (Reference.reference)
	 *
     * <p>
     * <b>Definition:</b>
     * A reference to a location at which the other resource is found. The reference may a relative reference, in which case it is relative to the service base URL, or an absolute URL that resolves to the location where the resource is found. The reference may be version specific or not. If the reference is not to a FHIR RESTful server, then it should be assumed to be version specific. Internal fragment references (start with '#') refer to contained resources
     * </p> 
	 */
	public ReferenceDt setReference( String theString) {
		myReference = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>display</b> (Reference.display).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Plain text narrative that identifies the resource in addition to the resource reference
     * </p> 
	 */
	public StringDt getDisplayElement() {  
		if (myDisplay == null) {
			myDisplay = new StringDt();
		}
		return myDisplay;
	}

	
	/**
	 * Gets the value(s) for <b>display</b> (Reference.display).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Plain text narrative that identifies the resource in addition to the resource reference
     * </p> 
	 */
	public String getDisplay() {  
		return getDisplayElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>display</b> (Reference.display)
	 *
     * <p>
     * <b>Definition:</b>
     * Plain text narrative that identifies the resource in addition to the resource reference
     * </p> 
	 */
	public ReferenceDt setDisplay(StringDt theValue) {
		myDisplay = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>display</b> (Reference.display)
	 *
     * <p>
     * <b>Definition:</b>
     * Plain text narrative that identifies the resource in addition to the resource reference
     * </p> 
	 */
	public ReferenceDt setDisplay( String theString) {
		myDisplay = new StringDt(theString); 
		return this; 
	}

 


}