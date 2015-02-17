















package ca.uhn.fhir.model.dstu2.composite;

import java.net.URI;
import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

import ca.uhn.fhir.model.api.*;
import ca.uhn.fhir.model.primitive.*;
import ca.uhn.fhir.model.api.annotation.*;
import ca.uhn.fhir.model.base.composite.*;
import ca.uhn.fhir.model.dstu2.valueset.AddressUseEnum;
import ca.uhn.fhir.model.dstu2.valueset.AggregationModeEnum;
import ca.uhn.fhir.model.dstu2.valueset.BindingConformanceEnum;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.valueset.ConstraintSeverityEnum;
import ca.uhn.fhir.model.dstu2.valueset.ContactPointSystemEnum;
import ca.uhn.fhir.model.dstu2.valueset.ContactPointUseEnum;
import ca.uhn.fhir.model.dstu2.valueset.DataTypeEnum;
import ca.uhn.fhir.model.dstu2.valueset.EventTimingEnum;
import ca.uhn.fhir.model.dstu2.valueset.IdentifierUseEnum;
import ca.uhn.fhir.model.dstu2.valueset.NameUseEnum;
import ca.uhn.fhir.model.dstu2.resource.Organization;
import ca.uhn.fhir.model.dstu2.composite.PeriodDt;
import ca.uhn.fhir.model.dstu2.valueset.PropertyRepresentationEnum;
import ca.uhn.fhir.model.dstu2.valueset.QuantityComparatorEnum;
import ca.uhn.fhir.model.dstu2.composite.QuantityDt;
import ca.uhn.fhir.model.dstu2.valueset.SlicingRulesEnum;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.dstu2.valueset.UnitsOfTimeEnum;
import ca.uhn.fhir.model.dstu2.resource.ValueSet;
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
 * HAPI/FHIR <b>CodingDt</b> Datatype
 * (Coding)
 *
 * <p>
 * <b>Definition:</b>
 * A reference to a code defined by a terminology system
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * References to codes are very common in healthcare models
 * </p> 
 */
@DatatypeDef(name="CodingDt") 
public class CodingDt
        extends  BaseCodingDt         implements ICompositeDatatype
{

	/**
	 * Constructor
	 */
	public CodingDt() {
		// nothing
	}

	/**
	 * Creates a new Coding with the given system and code
	 */
	public CodingDt(String theSystem, String theCode) {
		setSystem(theSystem);
		setCode(theCode);
	}
	
	/**
	 * Copy constructor: Creates a new Coding with the system and code copied out of the given coding
	 */
	public CodingDt(BaseCodingDt theCoding) {
		this(theCoding.getSystemElement().getValueAsString(), theCoding.getCodeElement().getValue());
	}
	

	@Child(name="system", type=UriDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="Coding.system",
		formalDefinition="The identification of the code system that defines the meaning of the symbol in the code."
	)
	private UriDt mySystem;
	
	@Child(name="version", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Coding.version",
		formalDefinition="The version of the code system which was used when choosing this code. Note that a well-maintained code system does not need the version reported, because the meaning of codes is consistent across versions. However this cannot consistently be assured. and when the meaning is not guaranteed to be consistent, the version SHOULD be exchanged"
	)
	private StringDt myVersion;
	
	@Child(name="code", type=CodeDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="Coding.code",
		formalDefinition="A symbol in syntax defined by the system. The symbol may be a predefined code or an expression in a syntax defined by the coding system (e.g. post-coordination)"
	)
	private CodeDt myCode;
	
	@Child(name="display", type=StringDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="Coding.display",
		formalDefinition="A representation of the meaning of the code in the system, following the rules of the system."
	)
	private StringDt myDisplay;
	
	@Child(name="primary", type=BooleanDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="Coding.primary",
		formalDefinition="Indicates that this code was chosen by a user directly - i.e. off a pick list of available items (codes or displays)"
	)
	private BooleanDt myPrimary;
	
	@Child(name="valueSet", order=5, min=0, max=1, type={
		ca.uhn.fhir.model.dstu2.resource.ValueSet.class	})
	@Description(
		shortDefinition="Coding.valueSet",
		formalDefinition="The set of possible coded values this coding was chosen from or constrained by"
	)
	private ResourceReferenceDt myValueSet;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  mySystem,  myVersion,  myCode,  myDisplay,  myPrimary,  myValueSet);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, mySystem, myVersion, myCode, myDisplay, myPrimary, myValueSet);
	}

	/**
	 * Gets the value(s) for <b>system</b> (Coding.system).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The identification of the code system that defines the meaning of the symbol in the code.
     * </p> 
	 */
	public UriDt getSystemElement() {  
		if (mySystem == null) {
			mySystem = new UriDt();
		}
		return mySystem;
	}

	
	/**
	 * Gets the value(s) for <b>system</b> (Coding.system).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The identification of the code system that defines the meaning of the symbol in the code.
     * </p> 
	 */
	public String getSystem() {  
		return getSystemElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>system</b> (Coding.system)
	 *
     * <p>
     * <b>Definition:</b>
     * The identification of the code system that defines the meaning of the symbol in the code.
     * </p> 
	 */
	public CodingDt setSystem(UriDt theValue) {
		mySystem = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>system</b> (Coding.system)
	 *
     * <p>
     * <b>Definition:</b>
     * The identification of the code system that defines the meaning of the symbol in the code.
     * </p> 
	 */
	public CodingDt setSystem( String theUri) {
		mySystem = new UriDt(theUri); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>version</b> (Coding.version).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The version of the code system which was used when choosing this code. Note that a well-maintained code system does not need the version reported, because the meaning of codes is consistent across versions. However this cannot consistently be assured. and when the meaning is not guaranteed to be consistent, the version SHOULD be exchanged
     * </p> 
	 */
	public StringDt getVersionElement() {  
		if (myVersion == null) {
			myVersion = new StringDt();
		}
		return myVersion;
	}

	
	/**
	 * Gets the value(s) for <b>version</b> (Coding.version).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The version of the code system which was used when choosing this code. Note that a well-maintained code system does not need the version reported, because the meaning of codes is consistent across versions. However this cannot consistently be assured. and when the meaning is not guaranteed to be consistent, the version SHOULD be exchanged
     * </p> 
	 */
	public String getVersion() {  
		return getVersionElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>version</b> (Coding.version)
	 *
     * <p>
     * <b>Definition:</b>
     * The version of the code system which was used when choosing this code. Note that a well-maintained code system does not need the version reported, because the meaning of codes is consistent across versions. However this cannot consistently be assured. and when the meaning is not guaranteed to be consistent, the version SHOULD be exchanged
     * </p> 
	 */
	public CodingDt setVersion(StringDt theValue) {
		myVersion = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>version</b> (Coding.version)
	 *
     * <p>
     * <b>Definition:</b>
     * The version of the code system which was used when choosing this code. Note that a well-maintained code system does not need the version reported, because the meaning of codes is consistent across versions. However this cannot consistently be assured. and when the meaning is not guaranteed to be consistent, the version SHOULD be exchanged
     * </p> 
	 */
	public CodingDt setVersion( String theString) {
		myVersion = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>code</b> (Coding.code).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A symbol in syntax defined by the system. The symbol may be a predefined code or an expression in a syntax defined by the coding system (e.g. post-coordination)
     * </p> 
	 */
	public CodeDt getCodeElement() {  
		if (myCode == null) {
			myCode = new CodeDt();
		}
		return myCode;
	}

	
	/**
	 * Gets the value(s) for <b>code</b> (Coding.code).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A symbol in syntax defined by the system. The symbol may be a predefined code or an expression in a syntax defined by the coding system (e.g. post-coordination)
     * </p> 
	 */
	public String getCode() {  
		return getCodeElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>code</b> (Coding.code)
	 *
     * <p>
     * <b>Definition:</b>
     * A symbol in syntax defined by the system. The symbol may be a predefined code or an expression in a syntax defined by the coding system (e.g. post-coordination)
     * </p> 
	 */
	public CodingDt setCode(CodeDt theValue) {
		myCode = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>code</b> (Coding.code)
	 *
     * <p>
     * <b>Definition:</b>
     * A symbol in syntax defined by the system. The symbol may be a predefined code or an expression in a syntax defined by the coding system (e.g. post-coordination)
     * </p> 
	 */
	public CodingDt setCode( String theCode) {
		myCode = new CodeDt(theCode); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>display</b> (Coding.display).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A representation of the meaning of the code in the system, following the rules of the system.
     * </p> 
	 */
	public StringDt getDisplayElement() {  
		if (myDisplay == null) {
			myDisplay = new StringDt();
		}
		return myDisplay;
	}

	
	/**
	 * Gets the value(s) for <b>display</b> (Coding.display).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A representation of the meaning of the code in the system, following the rules of the system.
     * </p> 
	 */
	public String getDisplay() {  
		return getDisplayElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>display</b> (Coding.display)
	 *
     * <p>
     * <b>Definition:</b>
     * A representation of the meaning of the code in the system, following the rules of the system.
     * </p> 
	 */
	public CodingDt setDisplay(StringDt theValue) {
		myDisplay = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>display</b> (Coding.display)
	 *
     * <p>
     * <b>Definition:</b>
     * A representation of the meaning of the code in the system, following the rules of the system.
     * </p> 
	 */
	public CodingDt setDisplay( String theString) {
		myDisplay = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>primary</b> (Coding.primary).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates that this code was chosen by a user directly - i.e. off a pick list of available items (codes or displays)
     * </p> 
	 */
	public BooleanDt getPrimaryElement() {  
		if (myPrimary == null) {
			myPrimary = new BooleanDt();
		}
		return myPrimary;
	}

	
	/**
	 * Gets the value(s) for <b>primary</b> (Coding.primary).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates that this code was chosen by a user directly - i.e. off a pick list of available items (codes or displays)
     * </p> 
	 */
	public Boolean getPrimary() {  
		return getPrimaryElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>primary</b> (Coding.primary)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates that this code was chosen by a user directly - i.e. off a pick list of available items (codes or displays)
     * </p> 
	 */
	public CodingDt setPrimary(BooleanDt theValue) {
		myPrimary = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>primary</b> (Coding.primary)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates that this code was chosen by a user directly - i.e. off a pick list of available items (codes or displays)
     * </p> 
	 */
	public CodingDt setPrimary( boolean theBoolean) {
		myPrimary = new BooleanDt(theBoolean); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>valueSet</b> (Coding.valueSet).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The set of possible coded values this coding was chosen from or constrained by
     * </p> 
	 */
	public ResourceReferenceDt getValueSet() {  
		if (myValueSet == null) {
			myValueSet = new ResourceReferenceDt();
		}
		return myValueSet;
	}

	/**
	 * Sets the value(s) for <b>valueSet</b> (Coding.valueSet)
	 *
     * <p>
     * <b>Definition:</b>
     * The set of possible coded values this coding was chosen from or constrained by
     * </p> 
	 */
	public CodingDt setValueSet(ResourceReferenceDt theValue) {
		myValueSet = theValue;
		return this;
	}
	
	

  


}