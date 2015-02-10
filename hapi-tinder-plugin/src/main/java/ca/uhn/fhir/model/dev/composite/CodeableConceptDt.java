















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
 * HAPI/FHIR <b>CodeableConceptDt</b> Datatype
 * (CodeableConcept)
 *
 * <p>
 * <b>Definition:</b>
 * A concept that may be defined by a formal reference to a terminology or ontology or may be provided by text
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * This is a common pattern in healthcare - a concept that may be defined by one or more codes from formal definitions including LOINC and SNOMED CT, and/or defined by the provision of text that captures a human sense of the concept
 * </p> 
 */
@DatatypeDef(name="CodeableConceptDt") 
public class CodeableConceptDt
        extends  BaseIdentifiableElement         implements ICompositeDatatype
{

	/**
	 * Constructor
	 */
	public CodeableConceptDt() {
		// nothing
	}

	/**
	 * Constructor which creates a CodeableConceptDt with one coding repetition, containing
	 * the given system and code
	 */
	public CodeableConceptDt(String theSystem, String theCode) {
		addCoding().setSystem(theSystem).setCode(theCode);
	}

	@Child(name="coding", type=CodingDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="CodeableConcept.coding",
		formalDefinition="A reference to a code defined by a terminology system"
	)
	private java.util.List<CodingDt> myCoding;
	
	@Child(name="text", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="CodeableConcept.text",
		formalDefinition="A human language representation of the concept as seen/selected/uttered by the user who entered the data and/or which represents the intended meaning of the user"
	)
	private StringDt myText;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myCoding,  myText);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myCoding, myText);
	}

	/**
	 * Gets the value(s) for <b>coding</b> (CodeableConcept.coding).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A reference to a code defined by a terminology system
     * </p> 
	 */
	public java.util.List<CodingDt> getCoding() {  
		if (myCoding == null) {
			myCoding = new java.util.ArrayList<CodingDt>();
		}
		return myCoding;
	}

	/**
	 * Sets the value(s) for <b>coding</b> (CodeableConcept.coding)
	 *
     * <p>
     * <b>Definition:</b>
     * A reference to a code defined by a terminology system
     * </p> 
	 */
	public CodeableConceptDt setCoding(java.util.List<CodingDt> theValue) {
		myCoding = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>coding</b> (CodeableConcept.coding)
	 *
     * <p>
     * <b>Definition:</b>
     * A reference to a code defined by a terminology system
     * </p> 
	 */
	public CodingDt addCoding() {
		CodingDt newType = new CodingDt();
		getCoding().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>coding</b> (CodeableConcept.coding),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * A reference to a code defined by a terminology system
     * </p> 
	 */
	public CodingDt getCodingFirstRep() {
		if (getCoding().isEmpty()) {
			return addCoding();
		}
		return getCoding().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>text</b> (CodeableConcept.text).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A human language representation of the concept as seen/selected/uttered by the user who entered the data and/or which represents the intended meaning of the user
     * </p> 
	 */
	public StringDt getTextElement() {  
		if (myText == null) {
			myText = new StringDt();
		}
		return myText;
	}

	
	/**
	 * Gets the value(s) for <b>text</b> (CodeableConcept.text).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A human language representation of the concept as seen/selected/uttered by the user who entered the data and/or which represents the intended meaning of the user
     * </p> 
	 */
	public String getText() {  
		return getTextElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>text</b> (CodeableConcept.text)
	 *
     * <p>
     * <b>Definition:</b>
     * A human language representation of the concept as seen/selected/uttered by the user who entered the data and/or which represents the intended meaning of the user
     * </p> 
	 */
	public CodeableConceptDt setText(StringDt theValue) {
		myText = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>text</b> (CodeableConcept.text)
	 *
     * <p>
     * <b>Definition:</b>
     * A human language representation of the concept as seen/selected/uttered by the user who entered the data and/or which represents the intended meaning of the user
     * </p> 
	 */
	public CodeableConceptDt setText( String theString) {
		myText = new StringDt(theString); 
		return this; 
	}

 


}