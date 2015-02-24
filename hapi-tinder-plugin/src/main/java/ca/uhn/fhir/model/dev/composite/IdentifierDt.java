















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
 * HAPI/FHIR <b>IdentifierDt</b> Datatype
 * (Identifier)
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
		shortDefinition="Identifier.use",
		formalDefinition="The purpose of this identifier"
	)
	private BoundCodeDt<IdentifierUseEnum> myUse;
	
	@Child(name="label", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Identifier.label",
		formalDefinition="A text string for the identifier that can be displayed to a human so they can recognize the identifier"
	)
	private StringDt myLabel;
	
	@Child(name="system", type=UriDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="Identifier.system",
		formalDefinition="Establishes the namespace in which set of possible id values is unique."
	)
	private UriDt mySystem;
	
	@Child(name="value", type=StringDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="Identifier.value",
		formalDefinition="The portion of the identifier typically displayed to the user and which is unique within the context of the system."
	)
	private StringDt myValue;
	
	@Child(name="period", type=PeriodDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="Identifier.period",
		formalDefinition="Time period during which identifier is/was valid for use"
	)
	private PeriodDt myPeriod;
	
	@Child(name="assigner", order=5, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Organization.class	})
	@Description(
		shortDefinition="Identifier.assigner",
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
	 * Gets the value(s) for <b>use</b> (Identifier.use).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The purpose of this identifier
     * </p> 
	 */
	public BoundCodeDt<IdentifierUseEnum> getUseElement() {  
		if (myUse == null) {
			myUse = new BoundCodeDt<IdentifierUseEnum>(IdentifierUseEnum.VALUESET_BINDER);
		}
		return myUse;
	}

	
	/**
	 * Gets the value(s) for <b>use</b> (Identifier.use).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The purpose of this identifier
     * </p> 
	 */
	public String getUse() {  
		return getUseElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>use</b> (Identifier.use)
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
	 * Sets the value(s) for <b>use</b> (Identifier.use)
	 *
     * <p>
     * <b>Definition:</b>
     * The purpose of this identifier
     * </p> 
	 */
	public IdentifierDt setUse(IdentifierUseEnum theValue) {
		getUseElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>label</b> (Identifier.label).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A text string for the identifier that can be displayed to a human so they can recognize the identifier
     * </p> 
	 */
	public StringDt getLabelElement() {  
		if (myLabel == null) {
			myLabel = new StringDt();
		}
		return myLabel;
	}

	
	/**
	 * Gets the value(s) for <b>label</b> (Identifier.label).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A text string for the identifier that can be displayed to a human so they can recognize the identifier
     * </p> 
	 */
	public String getLabel() {  
		return getLabelElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>label</b> (Identifier.label)
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
	 * Sets the value for <b>label</b> (Identifier.label)
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
	 * Gets the value(s) for <b>system</b> (Identifier.system).
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
	 * Gets the value(s) for <b>system</b> (Identifier.system).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Establishes the namespace in which set of possible id values is unique.
     * </p> 
	 */
	public String getSystem() {  
		return getSystemElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>system</b> (Identifier.system)
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
	 * Sets the value for <b>system</b> (Identifier.system)
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
	 * Gets the value(s) for <b>value</b> (Identifier.value).
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
	 * Gets the value(s) for <b>value</b> (Identifier.value).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The portion of the identifier typically displayed to the user and which is unique within the context of the system.
     * </p> 
	 */
	public String getValue() {  
		return getValueElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>value</b> (Identifier.value)
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
	 * Sets the value for <b>value</b> (Identifier.value)
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
	 * Gets the value(s) for <b>period</b> (Identifier.period).
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
	 * Sets the value(s) for <b>period</b> (Identifier.period)
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
	 * Gets the value(s) for <b>assigner</b> (Identifier.assigner).
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
	 * Sets the value(s) for <b>assigner</b> (Identifier.assigner)
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