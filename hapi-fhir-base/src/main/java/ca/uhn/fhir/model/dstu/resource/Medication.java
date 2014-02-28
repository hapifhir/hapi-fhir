















package ca.uhn.fhir.model.dstu.resource;

import java.util.*;
import ca.uhn.fhir.model.api.*;
import ca.uhn.fhir.model.api.annotation.*;
import ca.uhn.fhir.model.primitive.*;
import ca.uhn.fhir.model.dstu.composite.*;

/**
 * HAPI/FHIR <b>Medication</b> Resource
 * (Definition of a Medication)
 *
 * <p>
 * <b>Definition:</b>
 * Primarily used for identification and definition of Medication, but also covers ingredients and packaging
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 */
@ResourceDef(name="Medication")
public class Medication extends BaseElement implements IResource {

	@Child(name="name", type=StringDt.class, order=0, min=0, max=1)	
	private StringDt myName;
	
	@Child(name="code", type=CodeableConceptDt.class, order=1, min=0, max=1)	
	private CodeableConceptDt myCode;
	
	@Child(name="isBrand", type=BooleanDt.class, order=2, min=0, max=1)	
	private BooleanDt myIsBrand;
	
	@Child(name="manufacturer", order=3, min=0, max=1)
	@ChildResource(types= {
		Organization.class,
	})	
	private ResourceReference myManufacturer;
	
	@Child(name="kind", type=CodeDt.class, order=4, min=0, max=1)	
	private CodeDt myKind;
	
	@Child(name="product", order=5, min=0, max=1)	
	private Product myProduct;
	
	@Child(name="package", type=CodeDt.class, order=6, min=0, max=1)	
	private CodeDt myPackage;
	
	/**
	 * Gets the value(s) for <b>name</b> (Common / Commercial name).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The common/commercial name of the medication absent information such as strength, form, etc.  E.g. Acetaminophen, Tylenol 3, etc.  The fully coordinated name is communicated as the display of Medication.code
     * </p> 
	 */
	public StringDt getName() {  
		if (myName == null) {
			myName = new StringDt();
		}
		return myName;
	}

	/**
	 * Sets the value(s) for <b>name</b> (Common / Commercial name)
	 *
     * <p>
     * <b>Definition:</b>
     * The common/commercial name of the medication absent information such as strength, form, etc.  E.g. Acetaminophen, Tylenol 3, etc.  The fully coordinated name is communicated as the display of Medication.code
     * </p> 
	 */
	public void setName(StringDt theValue) {
		myName = theValue;
	}
	
	/**
	 * Sets the value(s) for <b>name</b> (Common / Commercial name)
	 *
     * <p>
     * <b>Definition:</b>
     * The common/commercial name of the medication absent information such as strength, form, etc.  E.g. Acetaminophen, Tylenol 3, etc.  The fully coordinated name is communicated as the display of Medication.code
     * </p> 
	 */
	public void setName( String theString) {
		myName = new StringDt(theString); 
	}
 
	/**
	 * Gets the value(s) for <b>code</b> (Codes that identify this medication).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A code (or set of codes) that identify this medication.   Usage note: This could be a standard drug code such as a drug regulator code, RxNorm code, SNOMED CT code, etc. It could also be a local formulary code, optionally with translations to the standard drug codes
     * </p> 
	 */
	public CodeableConceptDt getCode() {  
		if (myCode == null) {
			myCode = new CodeableConceptDt();
		}
		return myCode;
	}

	/**
	 * Sets the value(s) for <b>code</b> (Codes that identify this medication)
	 *
     * <p>
     * <b>Definition:</b>
     * A code (or set of codes) that identify this medication.   Usage note: This could be a standard drug code such as a drug regulator code, RxNorm code, SNOMED CT code, etc. It could also be a local formulary code, optionally with translations to the standard drug codes
     * </p> 
	 */
	public void setCode(CodeableConceptDt theValue) {
		myCode = theValue;
	}
	
 
	/**
	 * Gets the value(s) for <b>isBrand</b> (True if a brand).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Set to true if the item is attributable to a specific manufacturer (even if we don't know who that is) 
     * </p> 
	 */
	public BooleanDt getIsBrand() {  
		if (myIsBrand == null) {
			myIsBrand = new BooleanDt();
		}
		return myIsBrand;
	}

	/**
	 * Sets the value(s) for <b>isBrand</b> (True if a brand)
	 *
     * <p>
     * <b>Definition:</b>
     * Set to true if the item is attributable to a specific manufacturer (even if we don't know who that is) 
     * </p> 
	 */
	public void setIsBrand(BooleanDt theValue) {
		myIsBrand = theValue;
	}
	
 
	/**
	 * Gets the value(s) for <b>manufacturer</b> (Manufacturer of the item).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Describes the details of the manufacturer
     * </p> 
	 */
	public ResourceReference getManufacturer() {  
		if (myManufacturer == null) {
			myManufacturer = new ResourceReference();
		}
		return myManufacturer;
	}

	/**
	 * Sets the value(s) for <b>manufacturer</b> (Manufacturer of the item)
	 *
     * <p>
     * <b>Definition:</b>
     * Describes the details of the manufacturer
     * </p> 
	 */
	public void setManufacturer(ResourceReference theValue) {
		myManufacturer = theValue;
	}
	
 
	/**
	 * Gets the value(s) for <b>kind</b> (product | package).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Medications are either a single administrable product or a package that contains one or more products.
     * </p> 
	 */
	public CodeDt getKind() {  
		if (myKind == null) {
			myKind = new CodeDt();
		}
		return myKind;
	}

	/**
	 * Sets the value(s) for <b>kind</b> (product | package)
	 *
     * <p>
     * <b>Definition:</b>
     * Medications are either a single administrable product or a package that contains one or more products.
     * </p> 
	 */
	public void setKind(CodeDt theValue) {
		myKind = theValue;
	}
	
 
	/**
	 * Gets the value(s) for <b>product</b> (Administrable medication details).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Information that only applies to products (not packages)
     * </p> 
	 */
	public Product getProduct() {  
		if (myProduct == null) {
			myProduct = new Product();
		}
		return myProduct;
	}

	/**
	 * Sets the value(s) for <b>product</b> (Administrable medication details)
	 *
     * <p>
     * <b>Definition:</b>
     * Information that only applies to products (not packages)
     * </p> 
	 */
	public void setProduct(Product theValue) {
		myProduct = theValue;
	}
	
 
	/**
	 * Gets the value(s) for <b>package</b> (Details about packaged medications).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Information that only applies to packages (not products)
     * </p> 
	 */
	public CodeDt getPackage() {  
		if (myPackage == null) {
			myPackage = new CodeDt();
		}
		return myPackage;
	}

	/**
	 * Sets the value(s) for <b>package</b> (Details about packaged medications)
	 *
     * <p>
     * <b>Definition:</b>
     * Information that only applies to packages (not products)
     * </p> 
	 */
	public void setPackage(CodeDt theValue) {
		myPackage = theValue;
	}
	
 
	/**
	 * Block class for child element: <b>Medication.product</b> (Administrable medication details)
	 *
     * <p>
     * <b>Definition:</b>
     * Information that only applies to products (not packages)
     * </p> 
	 */
	@Block(name="Medication.product")	
	public static class Product extends BaseElement implements IResourceBlock {
	
	@Child(name="form", type=CodeableConceptDt.class, order=0, min=0, max=1)	
	private CodeableConceptDt myForm;
	
	@Child(name="ingredient", order=1, min=0, max=Child.MAX_UNLIMITED)	
	private List<ProductIngredient> myIngredient;
	
	/**
	 * Gets the value(s) for <b>form</b> (powder | tablets | carton +).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Describes the form of the item.  Powder; tables; carton
     * </p> 
	 */
	public CodeableConceptDt getForm() {  
		if (myForm == null) {
			myForm = new CodeableConceptDt();
		}
		return myForm;
	}

	/**
	 * Sets the value(s) for <b>form</b> (powder | tablets | carton +)
	 *
     * <p>
     * <b>Definition:</b>
     * Describes the form of the item.  Powder; tables; carton
     * </p> 
	 */
	public void setForm(CodeableConceptDt theValue) {
		myForm = theValue;
	}
	
 
	/**
	 * Gets the value(s) for <b>ingredient</b> (Active or inactive ingredient).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a particular constituent of interest in the product
     * </p> 
	 */
	public List<ProductIngredient> getIngredient() {  
		if (myIngredient == null) {
			myIngredient = new ArrayList<ProductIngredient>();
		}
		return myIngredient;
	}

	/**
	 * Sets the value(s) for <b>ingredient</b> (Active or inactive ingredient)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a particular constituent of interest in the product
     * </p> 
	 */
	public void setIngredient(List<ProductIngredient> theValue) {
		myIngredient = theValue;
	}
	
 

	}

	/**
	 * Block class for child element: <b>Medication.product.ingredient</b> (Active or inactive ingredient)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a particular constituent of interest in the product
     * </p> 
	 */
	@Block(name="Medication.product.ingredient")	
	public static class ProductIngredient extends BaseElement implements IResourceBlock {
	
	@Child(name="item", order=0, min=1, max=1)
	@ChildResource(types= {
		Substance.class,
		Medication.class,
	})	
	private ResourceReference myItem;
	
	@Child(name="amount", type=RatioDt.class, order=1, min=0, max=1)	
	private RatioDt myAmount;
	
	/**
	 * Gets the value(s) for <b>item</b> (The product contained).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The actual ingredient - either a substance (simple ingredient) or another medication
     * </p> 
	 */
	public ResourceReference getItem() {  
		return myItem;
	}

	/**
	 * Sets the value(s) for <b>item</b> (The product contained)
	 *
     * <p>
     * <b>Definition:</b>
     * The actual ingredient - either a substance (simple ingredient) or another medication
     * </p> 
	 */
	public void setItem(ResourceReference theValue) {
		myItem = theValue;
	}
	
 
	/**
	 * Gets the value(s) for <b>amount</b> (How much ingredient in product).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Specifies how many (or how much) of the items there are in this Medication.  E.g. 250 mg per tablet
     * </p> 
	 */
	public RatioDt getAmount() {  
		if (myAmount == null) {
			myAmount = new RatioDt();
		}
		return myAmount;
	}

	/**
	 * Sets the value(s) for <b>amount</b> (How much ingredient in product)
	 *
     * <p>
     * <b>Definition:</b>
     * Specifies how many (or how much) of the items there are in this Medication.  E.g. 250 mg per tablet
     * </p> 
	 */
	public void setAmount(RatioDt theValue) {
		myAmount = theValue;
	}
	
 

	}





}