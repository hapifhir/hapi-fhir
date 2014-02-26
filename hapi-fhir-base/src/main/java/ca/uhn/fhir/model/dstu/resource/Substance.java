















package ca.uhn.fhir.model.dstu.resource;

import java.util.*;
import ca.uhn.fhir.model.api.*;
import ca.uhn.fhir.model.api.annotation.*;
import ca.uhn.fhir.model.primitive.*;
import ca.uhn.fhir.model.dstu.composite.*;

/**
 * HAPI/FHIR <b>Substance</b> Resource
 * (A homogeneous material with a definite composition )
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
@ResourceDef(name="Substance")
public class Substance implements IResource {

	@Child(name="type", type=CodeableConceptDt.class, order=0, min=1, max=1)	
	private CodeableConceptDt myType;
	
	@Child(name="description", type=StringDt.class, order=1, min=0, max=1)	
	private StringDt myDescription;
	
	@Child(name="instance", order=2, min=0, max=1)	
	private Instance myInstance;
	
	@Child(name="ingredient", order=3, min=0, max=Child.MAX_UNLIMITED)	
	private List<Ingredient> myIngredient;
	
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
	public CodeableConceptDt getType() {  
		if (myType == null) {
			myType = new CodeableConceptDt();
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
	public void setType(CodeableConceptDt theValue) {
		myType = theValue;
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
	 * Sets the value(s) for <b>description</b> (Textual description of the substance, comments)
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
	 * Block class for child element: <b>Substance.instance</b> (If this describes a specific package/container of the substance)
	 *
     * <p>
     * <b>Definition:</b>
     * Substance may be used to describe a kind of substance, or a specific package/container of the substance: an instance
     * </p> 
	 */
	@Block(name="Substance.instance")	
	public static class Instance implements IResourceBlock {
	
	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=1)	
	private IdentifierDt myIdentifier;
	
	@Child(name="expiry", type=DateTimeDt.class, order=1, min=0, max=1)	
	private DateTimeDt myExpiry;
	
	@Child(name="quantity", type=QuantityDt.class, order=2, min=0, max=1)	
	private QuantityDt myQuantity;
	
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
	 * Sets the value(s) for <b>expiry</b> (When no longer valid to use)
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
	public static class Ingredient implements IResourceBlock {
	
	@Child(name="quantity", type=RatioDt.class, order=0, min=0, max=1)	
	private RatioDt myQuantity;
	
	@Child(name="substance", order=1, min=1, max=1)
	@ChildResource(types= {
		Substance.class,
	})	
	private ResourceReference mySubstance;
	
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
	public ResourceReference getSubstance() {  
		if (mySubstance == null) {
			mySubstance = new ResourceReference();
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
	public void setSubstance(ResourceReference theValue) {
		mySubstance = theValue;
	}
	
 

	}




}