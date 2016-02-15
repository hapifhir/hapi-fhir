















package ca.uhn.fhir.testmodel;

import java.util.List;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.BaseIdentifiableElement;
import ca.uhn.fhir.model.api.ICompositeDatatype;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.dstu.composite.PeriodDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.Organization;
import ca.uhn.fhir.model.dstu.valueset.IdentifierUseEnum;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.rest.param.StringParam;

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
public class IdentifierDt 
        extends  BaseIdentifiableElement         implements ICompositeDatatype  , IQueryParameterType {

	/**
	 * Creates a new identifier
	 */
	public IdentifierDt() {
		// nothing
	}

	/**
	 * Creates a new identifier with the given system and value
	 */
	public IdentifierDt(String theSystem, String theValue) {
		setSystem(theSystem);
		setValue(theValue);
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
		Organization.class,
	})
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
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType,  myUse,  myLabel,  mySystem,  myValue,  myPeriod,  myAssigner );
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
	public void setUse(BoundCodeDt<IdentifierUseEnum> theValue) {
		myUse = theValue;
	}


	/**
	 * Sets the value(s) for <b>use</b> (usual | official | temp | secondary (If known))
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
	 * Sets the value for <b>label</b> (Description of identifier)
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
	 * Sets the value for <b>system</b> (The namespace for the identifier)
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
	 * Sets the value for <b>value</b> (The value that is unique)
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
	public void setAssigner(ResourceReferenceDt theValue) {
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
	 * {@inheritDoc}
	 */
	@Override
	public String getValueAsQueryToken(FhirContext theContext) {
		if (org.apache.commons.lang3.StringUtils.isNotBlank(getSystem().getValueAsString())) {
			return getSystem().getValueAsString() + '|' + getValue().getValueAsString(); 
		} else {
			return getValue().getValueAsString();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setValueAsQueryToken(String theQualifier, String theValue) {
		int barIndex = theValue.indexOf('|');
		if (barIndex != -1) {
			setSystem(new UriDt(theValue.substring(0, barIndex)));
			setValue(theValue.substring(barIndex + 1));
		} else {
			setValue(theValue);
		}
	}	

	@Override
	public String getQueryParameterQualifier() {
		return null;
	}	

	/**
	 * <b>Not supported!</b>
	 * 
	 * @deprecated get/setMissing is not supported in StringDt. Use {@link StringParam} instead if you
	 * need this functionality
	 */
	@Deprecated
	@Override
	public Boolean getMissing() {
		throw new UnsupportedOperationException("get/setMissing is not supported in StringDt. Use {@link StringParam} instead if you need this functionality");
	}

	/**
	 * <b>Not supported!</b>
	 * 
	 * @deprecated get/setMissing is not supported in StringDt. Use {@link StringParam} instead if you
	 * need this functionality
	 */
	@Deprecated
	@Override
	public void setMissing(Boolean theMissing) {
		throw new UnsupportedOperationException("get/setMissing is not supported in StringDt. Use {@link StringParam} instead if you need this functionality");
	}

	
}