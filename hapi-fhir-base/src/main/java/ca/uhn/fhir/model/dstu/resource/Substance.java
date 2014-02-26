











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
	
 
	}



}