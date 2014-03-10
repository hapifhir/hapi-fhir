















package ca.uhn.fhir.model.dstu.resource;

import java.util.*;
import ca.uhn.fhir.model.api.*;
import ca.uhn.fhir.model.api.annotation.*;
import ca.uhn.fhir.model.primitive.*;
import ca.uhn.fhir.model.dstu.composite.*;
import ca.uhn.fhir.model.dstu.valueset.*;

/**
 * HAPI/FHIR <b>Substance</b> Resource
 * (A homogeneous material with a definite composition)
 *
 * <p>
 * <b>Definition:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 */
@ResourceDef(name="Substance", profile="http://hl7.org/fhir/profiles/Substance", id="substance")
public class Substance extends BaseResource implements IResource {

	/**
	 * Search parameter constant for <b>type</b>
	 * <p>
	 * Description: <b>The type of the substance</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Substance.type</b><br/>
	 * </p>
	 */
	public static final String SP_TYPE = "type";

	/**
	 * Search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Substance.instance.identifier</b><br/>
	 * </p>
	 */
	public static final String SP_IDENTIFIER = "identifier";

	/**
	 * Search parameter constant for <b>expiry</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Substance.instance.expiry</b><br/>
	 * </p>
	 */
	public static final String SP_EXPIRY = "expiry";

	/**
	 * Search parameter constant for <b>quantity</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>number</b><br/>
	 * Path: <b>Substance.instance.quantity</b><br/>
	 * </p>
	 */
	public static final String SP_QUANTITY = "quantity";

	/**
	 * Search parameter constant for <b>substance</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Substance.ingredient.substance</b><br/>
	 * </p>
	 */
	public static final String SP_SUBSTANCE = "substance";


	@Child(name="type", type=CodeableConceptDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="What kind of substance this is",
		formalDefinition="A code (or set of codes) that identify this substance"
	)
	private BoundCodeableConceptDt<SubstanceTypeEnum> myType;
	
	@Child(name="description", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Textual description of the substance, comments",
		formalDefinition="A description of the substance - its appearance, handling requirements, and other usage notes"
	)
	private StringDt myDescription;
	
	@Child(name="instance", order=2, min=0, max=1)	
	@Description(
		shortDefinition="If this describes a specific package/container of the substance",
		formalDefinition="Substance may be used to describe a kind of substance, or a specific package/container of the substance: an instance"
	)
	private Instance myInstance;
	
	@Child(name="ingredient", order=3, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Composition information about the substance",
		formalDefinition="A substance can be composed of other substances"
	)
	private List<Ingredient> myIngredient;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myType,  myDescription,  myInstance,  myIngredient);
	}

	/**
	 * Gets the value(s) for <b>type</b> (What kind of substance this is).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A code (or set of codes) that identify this substance
     * </p> 
	 */
	public BoundCodeableConceptDt<SubstanceTypeEnum> getType() {  
		if (myType == null) {
			myType = new BoundCodeableConceptDt<SubstanceTypeEnum>(SubstanceTypeEnum.VALUESET_BINDER);
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (What kind of substance this is)
	 *
     * <p>
     * <b>Definition:</b>
     * A code (or set of codes) that identify this substance
     * </p> 
	 */
	public void setType(BoundCodeableConceptDt<SubstanceTypeEnum> theValue) {
		myType = theValue;
	}


	/**
	 * Sets the value(s) for <b>type</b> (What kind of substance this is)
	 *
     * <p>
     * <b>Definition:</b>
     * A code (or set of codes) that identify this substance
     * </p> 
	 */
	public void setType(SubstanceTypeEnum theValue) {
		getType().setValueAsEnum(theValue);
	}

  
	/**
	 * Gets the value(s) for <b>description</b> (Textual description of the substance, comments).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A description of the substance - its appearance, handling requirements, and other usage notes
     * </p> 
	 */
	public StringDt getDescription() {  
		if (myDescription == null) {
			myDescription = new StringDt();
		}
		return myDescription;
	}

	/**
	 * Sets the value(s) for <b>description</b> (Textual description of the substance, comments)
	 *
     * <p>
     * <b>Definition:</b>
     * A description of the substance - its appearance, handling requirements, and other usage notes
     * </p> 
	 */
	public void setDescription(StringDt theValue) {
		myDescription = theValue;
	}


 	/**
	 * Sets the value for <b>description</b> (Textual description of the substance, comments)
	 *
     * <p>
     * <b>Definition:</b>
     * A description of the substance - its appearance, handling requirements, and other usage notes
     * </p> 
	 */
	public void setDescription( String theString) {
		myDescription = new StringDt(theString); 
	}

 
	/**
	 * Gets the value(s) for <b>instance</b> (If this describes a specific package/container of the substance).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Substance may be used to describe a kind of substance, or a specific package/container of the substance: an instance
     * </p> 
	 */
	public Instance getInstance() {  
		if (myInstance == null) {
			myInstance = new Instance();
		}
		return myInstance;
	}

	/**
	 * Sets the value(s) for <b>instance</b> (If this describes a specific package/container of the substance)
	 *
     * <p>
     * <b>Definition:</b>
     * Substance may be used to describe a kind of substance, or a specific package/container of the substance: an instance
     * </p> 
	 */
	public void setInstance(Instance theValue) {
		myInstance = theValue;
	}


  
	/**
	 * Gets the value(s) for <b>ingredient</b> (Composition information about the substance).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A substance can be composed of other substances
     * </p> 
	 */
	public List<Ingredient> getIngredient() {  
		if (myIngredient == null) {
			myIngredient = new ArrayList<Ingredient>();
		}
		return myIngredient;
	}

	/**
	 * Sets the value(s) for <b>ingredient</b> (Composition information about the substance)
	 *
     * <p>
     * <b>Definition:</b>
     * A substance can be composed of other substances
     * </p> 
	 */
	public void setIngredient(List<Ingredient> theValue) {
		myIngredient = theValue;
	}

	/**
	 * Adds and returns a new value for <b>ingredient</b> (Composition information about the substance)
	 *
     * <p>
     * <b>Definition:</b>
     * A substance can be composed of other substances
     * </p> 
	 */
	public Ingredient addIngredient() {
		Ingredient newType = new Ingredient();
		getIngredient().add(newType);
		return newType; 
	}

  
	/**
	 * Block class for child element: <b>Substance.instance</b> (If this describes a specific package/container of the substance)
	 *
     * <p>
     * <b>Definition:</b>
     * Substance may be used to describe a kind of substance, or a specific package/container of the substance: an instance
     * </p> 
	 */
	@Block(name="Substance.instance")	
	public static class Instance extends BaseElement implements IResourceBlock {
	
	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="Identifier of the package/container",
		formalDefinition="Identifier associated with the package/container (usually a label affixed directly)"
	)
	private IdentifierDt myIdentifier;
	
	@Child(name="expiry", type=DateTimeDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="When no longer valid to use",
		formalDefinition="When the substance is no longer valid to use. For some substances, a single arbitrary date is used for expiry."
	)
	private DateTimeDt myExpiry;
	
	@Child(name="quantity", type=QuantityDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="Amount of substance in the package",
		formalDefinition="The amount of the substance"
	)
	private QuantityDt myQuantity;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myExpiry,  myQuantity);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> (Identifier of the package/container).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier associated with the package/container (usually a label affixed directly)
     * </p> 
	 */
	public IdentifierDt getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new IdentifierDt();
		}
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for <b>identifier</b> (Identifier of the package/container)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier associated with the package/container (usually a label affixed directly)
     * </p> 
	 */
	public void setIdentifier(IdentifierDt theValue) {
		myIdentifier = theValue;
	}


  
	/**
	 * Gets the value(s) for <b>expiry</b> (When no longer valid to use).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * When the substance is no longer valid to use. For some substances, a single arbitrary date is used for expiry.
     * </p> 
	 */
	public DateTimeDt getExpiry() {  
		if (myExpiry == null) {
			myExpiry = new DateTimeDt();
		}
		return myExpiry;
	}

	/**
	 * Sets the value(s) for <b>expiry</b> (When no longer valid to use)
	 *
     * <p>
     * <b>Definition:</b>
     * When the substance is no longer valid to use. For some substances, a single arbitrary date is used for expiry.
     * </p> 
	 */
	public void setExpiry(DateTimeDt theValue) {
		myExpiry = theValue;
	}


 	/**
	 * Sets the value for <b>expiry</b> (When no longer valid to use)
	 *
     * <p>
     * <b>Definition:</b>
     * When the substance is no longer valid to use. For some substances, a single arbitrary date is used for expiry.
     * </p> 
	 */
	public void setExpiryWithSecondsPrecision( Date theDate) {
		myExpiry = new DateTimeDt(theDate); 
	}

	/**
	 * Sets the value for <b>expiry</b> (When no longer valid to use)
	 *
     * <p>
     * <b>Definition:</b>
     * When the substance is no longer valid to use. For some substances, a single arbitrary date is used for expiry.
     * </p> 
	 */
	public void setExpiry( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myExpiry = new DateTimeDt(theDate, thePrecision); 
	}

 
	/**
	 * Gets the value(s) for <b>quantity</b> (Amount of substance in the package).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The amount of the substance
     * </p> 
	 */
	public QuantityDt getQuantity() {  
		if (myQuantity == null) {
			myQuantity = new QuantityDt();
		}
		return myQuantity;
	}

	/**
	 * Sets the value(s) for <b>quantity</b> (Amount of substance in the package)
	 *
     * <p>
     * <b>Definition:</b>
     * The amount of the substance
     * </p> 
	 */
	public void setQuantity(QuantityDt theValue) {
		myQuantity = theValue;
	}


  

	}


	/**
	 * Block class for child element: <b>Substance.ingredient</b> (Composition information about the substance)
	 *
     * <p>
     * <b>Definition:</b>
     * A substance can be composed of other substances
     * </p> 
	 */
	@Block(name="Substance.ingredient")	
	public static class Ingredient extends BaseElement implements IResourceBlock {
	
	@Child(name="quantity", type=RatioDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="Optional amount (concentration)",
		formalDefinition="The amount of the ingredient in the substance - a concentration ratio"
	)
	private RatioDt myQuantity;
	
	@Child(name="substance", order=1, min=1, max=1, type={
		Substance.class,
	})
	@Description(
		shortDefinition="A component of the substance",
		formalDefinition="Another substance that is a component of this substance"
	)
	private ResourceReferenceDt mySubstance;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myQuantity,  mySubstance);
	}

	/**
	 * Gets the value(s) for <b>quantity</b> (Optional amount (concentration)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The amount of the ingredient in the substance - a concentration ratio
     * </p> 
	 */
	public RatioDt getQuantity() {  
		if (myQuantity == null) {
			myQuantity = new RatioDt();
		}
		return myQuantity;
	}

	/**
	 * Sets the value(s) for <b>quantity</b> (Optional amount (concentration))
	 *
     * <p>
     * <b>Definition:</b>
     * The amount of the ingredient in the substance - a concentration ratio
     * </p> 
	 */
	public void setQuantity(RatioDt theValue) {
		myQuantity = theValue;
	}


  
	/**
	 * Gets the value(s) for <b>substance</b> (A component of the substance).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Another substance that is a component of this substance
     * </p> 
	 */
	public ResourceReferenceDt getSubstance() {  
		if (mySubstance == null) {
			mySubstance = new ResourceReferenceDt();
		}
		return mySubstance;
	}

	/**
	 * Sets the value(s) for <b>substance</b> (A component of the substance)
	 *
     * <p>
     * <b>Definition:</b>
     * Another substance that is a component of this substance
     * </p> 
	 */
	public void setSubstance(ResourceReferenceDt theValue) {
		mySubstance = theValue;
	}


  

	}




}