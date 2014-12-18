















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
 * HAPI/FHIR <b>HumanNameDt</b> Datatype
 * (HumanName)
 *
 * <p>
 * <b>Definition:</b>
 * A human's name with the ability to identify parts and usage
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * Need to be able to record names, along with notes about their use
 * </p> 
 */
@DatatypeDef(name="HumanNameDt") 
public class HumanNameDt
        extends  BaseHumanNameDt         implements ICompositeDatatype
{

	/**
	 * Constructor
	 */
	public HumanNameDt() {
		// nothing
	}


	@Child(name="use", type=CodeDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="HumanName.use",
		formalDefinition="Identifies the purpose for this name"
	)
	private BoundCodeDt<NameUseEnum> myUse;
	
	@Child(name="text", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="HumanName.text",
		formalDefinition="A full text representation of the name"
	)
	private StringDt myText;
	
	@Child(name="family", type=StringDt.class, order=2, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="HumanName.family",
		formalDefinition="The part of a name that links to the genealogy. In some cultures (e.g. Eritrea) the family name of a son is the first name of his father."
	)
	private java.util.List<StringDt> myFamily;
	
	@Child(name="given", type=StringDt.class, order=3, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="HumanName.given",
		formalDefinition="Given name"
	)
	private java.util.List<StringDt> myGiven;
	
	@Child(name="prefix", type=StringDt.class, order=4, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="HumanName.prefix",
		formalDefinition="Part of the name that is acquired as a title due to academic, legal, employment or nobility status, etc. and that appears at the start of the name"
	)
	private java.util.List<StringDt> myPrefix;
	
	@Child(name="suffix", type=StringDt.class, order=5, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="HumanName.suffix",
		formalDefinition="Part of the name that is acquired as a title due to academic, legal, employment or nobility status, etc. and that appears at the end of the name"
	)
	private java.util.List<StringDt> mySuffix;
	
	@Child(name="period", type=PeriodDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="HumanName.period",
		formalDefinition="Indicates the period of time when this name was valid for the named person."
	)
	private PeriodDt myPeriod;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myUse,  myText,  myFamily,  myGiven,  myPrefix,  mySuffix,  myPeriod);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myUse, myText, myFamily, myGiven, myPrefix, mySuffix, myPeriod);
	}

	/**
	 * Gets the value(s) for <b>use</b> (HumanName.use).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the purpose for this name
     * </p> 
	 */
	public BoundCodeDt<NameUseEnum> getUseElement() {  
		if (myUse == null) {
			myUse = new BoundCodeDt<NameUseEnum>(NameUseEnum.VALUESET_BINDER);
		}
		return myUse;
	}

	
	/**
	 * Gets the value(s) for <b>use</b> (HumanName.use).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the purpose for this name
     * </p> 
	 */
	public String getUse() {  
		return getUseElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>use</b> (HumanName.use)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the purpose for this name
     * </p> 
	 */
	public HumanNameDt setUse(BoundCodeDt<NameUseEnum> theValue) {
		myUse = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>use</b> (HumanName.use)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the purpose for this name
     * </p> 
	 */
	public HumanNameDt setUse(NameUseEnum theValue) {
		getUseElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>text</b> (HumanName.text).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A full text representation of the name
     * </p> 
	 */
	public StringDt getTextElement() {  
		if (myText == null) {
			myText = new StringDt();
		}
		return myText;
	}

	
	/**
	 * Gets the value(s) for <b>text</b> (HumanName.text).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A full text representation of the name
     * </p> 
	 */
	public String getText() {  
		return getTextElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>text</b> (HumanName.text)
	 *
     * <p>
     * <b>Definition:</b>
     * A full text representation of the name
     * </p> 
	 */
	public HumanNameDt setText(StringDt theValue) {
		myText = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>text</b> (HumanName.text)
	 *
     * <p>
     * <b>Definition:</b>
     * A full text representation of the name
     * </p> 
	 */
	public HumanNameDt setText( String theString) {
		myText = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>family</b> (HumanName.family).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The part of a name that links to the genealogy. In some cultures (e.g. Eritrea) the family name of a son is the first name of his father.
     * </p> 
	 */
	public java.util.List<StringDt> getFamily() {  
		if (myFamily == null) {
			myFamily = new java.util.ArrayList<StringDt>();
		}
		return myFamily;
	}

	/**
	 * Sets the value(s) for <b>family</b> (HumanName.family)
	 *
     * <p>
     * <b>Definition:</b>
     * The part of a name that links to the genealogy. In some cultures (e.g. Eritrea) the family name of a son is the first name of his father.
     * </p> 
	 */
	public HumanNameDt setFamily(java.util.List<StringDt> theValue) {
		myFamily = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>family</b> (HumanName.family)
	 *
     * <p>
     * <b>Definition:</b>
     * The part of a name that links to the genealogy. In some cultures (e.g. Eritrea) the family name of a son is the first name of his father.
     * </p> 
	 */
	public StringDt addFamily() {
		StringDt newType = new StringDt();
		getFamily().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>family</b> (HumanName.family),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * The part of a name that links to the genealogy. In some cultures (e.g. Eritrea) the family name of a son is the first name of his father.
     * </p> 
	 */
	public StringDt getFamilyFirstRep() {
		if (getFamily().isEmpty()) {
			return addFamily();
		}
		return getFamily().get(0); 
	}
 	/**
	 * Adds a new value for <b>family</b> (HumanName.family)
	 *
     * <p>
     * <b>Definition:</b>
     * The part of a name that links to the genealogy. In some cultures (e.g. Eritrea) the family name of a son is the first name of his father.
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public HumanNameDt addFamily( String theString) {
		if (myFamily == null) {
			myFamily = new java.util.ArrayList<StringDt>();
		}
		myFamily.add(new StringDt(theString));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>given</b> (HumanName.given).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Given name
     * </p> 
	 */
	public java.util.List<StringDt> getGiven() {  
		if (myGiven == null) {
			myGiven = new java.util.ArrayList<StringDt>();
		}
		return myGiven;
	}

	/**
	 * Sets the value(s) for <b>given</b> (HumanName.given)
	 *
     * <p>
     * <b>Definition:</b>
     * Given name
     * </p> 
	 */
	public HumanNameDt setGiven(java.util.List<StringDt> theValue) {
		myGiven = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>given</b> (HumanName.given)
	 *
     * <p>
     * <b>Definition:</b>
     * Given name
     * </p> 
	 */
	public StringDt addGiven() {
		StringDt newType = new StringDt();
		getGiven().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>given</b> (HumanName.given),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Given name
     * </p> 
	 */
	public StringDt getGivenFirstRep() {
		if (getGiven().isEmpty()) {
			return addGiven();
		}
		return getGiven().get(0); 
	}
 	/**
	 * Adds a new value for <b>given</b> (HumanName.given)
	 *
     * <p>
     * <b>Definition:</b>
     * Given name
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public HumanNameDt addGiven( String theString) {
		if (myGiven == null) {
			myGiven = new java.util.ArrayList<StringDt>();
		}
		myGiven.add(new StringDt(theString));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>prefix</b> (HumanName.prefix).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Part of the name that is acquired as a title due to academic, legal, employment or nobility status, etc. and that appears at the start of the name
     * </p> 
	 */
	public java.util.List<StringDt> getPrefix() {  
		if (myPrefix == null) {
			myPrefix = new java.util.ArrayList<StringDt>();
		}
		return myPrefix;
	}

	/**
	 * Sets the value(s) for <b>prefix</b> (HumanName.prefix)
	 *
     * <p>
     * <b>Definition:</b>
     * Part of the name that is acquired as a title due to academic, legal, employment or nobility status, etc. and that appears at the start of the name
     * </p> 
	 */
	public HumanNameDt setPrefix(java.util.List<StringDt> theValue) {
		myPrefix = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>prefix</b> (HumanName.prefix)
	 *
     * <p>
     * <b>Definition:</b>
     * Part of the name that is acquired as a title due to academic, legal, employment or nobility status, etc. and that appears at the start of the name
     * </p> 
	 */
	public StringDt addPrefix() {
		StringDt newType = new StringDt();
		getPrefix().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>prefix</b> (HumanName.prefix),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Part of the name that is acquired as a title due to academic, legal, employment or nobility status, etc. and that appears at the start of the name
     * </p> 
	 */
	public StringDt getPrefixFirstRep() {
		if (getPrefix().isEmpty()) {
			return addPrefix();
		}
		return getPrefix().get(0); 
	}
 	/**
	 * Adds a new value for <b>prefix</b> (HumanName.prefix)
	 *
     * <p>
     * <b>Definition:</b>
     * Part of the name that is acquired as a title due to academic, legal, employment or nobility status, etc. and that appears at the start of the name
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public HumanNameDt addPrefix( String theString) {
		if (myPrefix == null) {
			myPrefix = new java.util.ArrayList<StringDt>();
		}
		myPrefix.add(new StringDt(theString));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>suffix</b> (HumanName.suffix).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Part of the name that is acquired as a title due to academic, legal, employment or nobility status, etc. and that appears at the end of the name
     * </p> 
	 */
	public java.util.List<StringDt> getSuffix() {  
		if (mySuffix == null) {
			mySuffix = new java.util.ArrayList<StringDt>();
		}
		return mySuffix;
	}

	/**
	 * Sets the value(s) for <b>suffix</b> (HumanName.suffix)
	 *
     * <p>
     * <b>Definition:</b>
     * Part of the name that is acquired as a title due to academic, legal, employment or nobility status, etc. and that appears at the end of the name
     * </p> 
	 */
	public HumanNameDt setSuffix(java.util.List<StringDt> theValue) {
		mySuffix = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>suffix</b> (HumanName.suffix)
	 *
     * <p>
     * <b>Definition:</b>
     * Part of the name that is acquired as a title due to academic, legal, employment or nobility status, etc. and that appears at the end of the name
     * </p> 
	 */
	public StringDt addSuffix() {
		StringDt newType = new StringDt();
		getSuffix().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>suffix</b> (HumanName.suffix),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Part of the name that is acquired as a title due to academic, legal, employment or nobility status, etc. and that appears at the end of the name
     * </p> 
	 */
	public StringDt getSuffixFirstRep() {
		if (getSuffix().isEmpty()) {
			return addSuffix();
		}
		return getSuffix().get(0); 
	}
 	/**
	 * Adds a new value for <b>suffix</b> (HumanName.suffix)
	 *
     * <p>
     * <b>Definition:</b>
     * Part of the name that is acquired as a title due to academic, legal, employment or nobility status, etc. and that appears at the end of the name
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public HumanNameDt addSuffix( String theString) {
		if (mySuffix == null) {
			mySuffix = new java.util.ArrayList<StringDt>();
		}
		mySuffix.add(new StringDt(theString));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>period</b> (HumanName.period).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the period of time when this name was valid for the named person.
     * </p> 
	 */
	public PeriodDt getPeriod() {  
		if (myPeriod == null) {
			myPeriod = new PeriodDt();
		}
		return myPeriod;
	}

	/**
	 * Sets the value(s) for <b>period</b> (HumanName.period)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the period of time when this name was valid for the named person.
     * </p> 
	 */
	public HumanNameDt setPeriod(PeriodDt theValue) {
		myPeriod = theValue;
		return this;
	}
	
	

  


}