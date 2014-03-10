















package ca.uhn.fhir.model.dstu.resource;

import java.util.*;
import ca.uhn.fhir.model.api.*;
import ca.uhn.fhir.model.api.annotation.*;
import ca.uhn.fhir.model.primitive.*;
import ca.uhn.fhir.model.dstu.composite.*;
import ca.uhn.fhir.model.dstu.valueset.*;

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
@ResourceDef(name="Medication", profile="http://hl7.org/fhir/profiles/Medication", id="medication")
public class Medication extends BaseResource implements IResource {

	/**
	 * Search parameter constant for <b>code</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Medication.code</b><br/>
	 * </p>
	 */
	public static final String SP_CODE = "code";

	/**
	 * Search parameter constant for <b>name</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Medication.name</b><br/>
	 * </p>
	 */
	public static final String SP_NAME = "name";

	/**
	 * Search parameter constant for <b>manufacturer</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Medication.manufacturer</b><br/>
	 * </p>
	 */
	public static final String SP_MANUFACTURER = "manufacturer";

	/**
	 * Search parameter constant for <b>form</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Medication.product.form</b><br/>
	 * </p>
	 */
	public static final String SP_FORM = "form";

	/**
	 * Search parameter constant for <b>ingredient</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Medication.product.ingredient.item</b><br/>
	 * </p>
	 */
	public static final String SP_INGREDIENT = "ingredient";

	/**
	 * Search parameter constant for <b>container</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Medication.package.container</b><br/>
	 * </p>
	 */
	public static final String SP_CONTAINER = "container";

	/**
	 * Search parameter constant for <b>content</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Medication.package.content.item</b><br/>
	 * </p>
	 */
	public static final String SP_CONTENT = "content";


	@Child(name="name", type=StringDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="Common / Commercial name",
		formalDefinition="The common/commercial name of the medication absent information such as strength, form, etc.  E.g. Acetaminophen, Tylenol 3, etc.  The fully coordinated name is communicated as the display of Medication.code"
	)
	private StringDt myName;
	
	@Child(name="code", type=CodeableConceptDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Codes that identify this medication",
		formalDefinition="A code (or set of codes) that identify this medication.   Usage note: This could be a standard drug code such as a drug regulator code, RxNorm code, SNOMED CT code, etc. It could also be a local formulary code, optionally with translations to the standard drug codes"
	)
	private CodeableConceptDt myCode;
	
	@Child(name="isBrand", type=BooleanDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="True if a brand",
		formalDefinition="Set to true if the item is attributable to a specific manufacturer (even if we don't know who that is)"
	)
	private BooleanDt myIsBrand;
	
	@Child(name="manufacturer", order=3, min=0, max=1, type={
		Organization.class,
	})
	@Description(
		shortDefinition="Manufacturer of the item",
		formalDefinition="Describes the details of the manufacturer"
	)
	private ResourceReferenceDt myManufacturer;
	
	@Child(name="kind", type=CodeDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="product | package",
		formalDefinition="Medications are either a single administrable product or a package that contains one or more products."
	)
	private BoundCodeDt<MedicationKindEnum> myKind;
	
	@Child(name="product", order=5, min=0, max=1)	
	@Description(
		shortDefinition="Administrable medication details",
		formalDefinition="Information that only applies to products (not packages)"
	)
	private Product myProduct;
	
	@Child(name="package", type=CodeDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="Details about packaged medications",
		formalDefinition="Information that only applies to packages (not products)"
	)
	private CodeDt myPackage;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myName,  myCode,  myIsBrand,  myManufacturer,  myKind,  myProduct,  myPackage);
	}

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
	 * Sets the value for <b>name</b> (Common / Commercial name)
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
	 * Sets the value for <b>isBrand</b> (True if a brand)
	 *
     * <p>
     * <b>Definition:</b>
     * Set to true if the item is attributable to a specific manufacturer (even if we don't know who that is)
     * </p> 
	 */
	public void setIsBrand( Boolean theBoolean) {
		myIsBrand = new BooleanDt(theBoolean); 
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
	public ResourceReferenceDt getManufacturer() {  
		if (myManufacturer == null) {
			myManufacturer = new ResourceReferenceDt();
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
	public void setManufacturer(ResourceReferenceDt theValue) {
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
	public BoundCodeDt<MedicationKindEnum> getKind() {  
		if (myKind == null) {
			myKind = new BoundCodeDt<MedicationKindEnum>(MedicationKindEnum.VALUESET_BINDER);
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
	public void setKind(BoundCodeDt<MedicationKindEnum> theValue) {
		myKind = theValue;
	}


	/**
	 * Sets the value(s) for <b>kind</b> (product | package)
	 *
     * <p>
     * <b>Definition:</b>
     * Medications are either a single administrable product or a package that contains one or more products.
     * </p> 
	 */
	public void setKind(MedicationKindEnum theValue) {
		getKind().setValueAsEnum(theValue);
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
	 * Sets the value for <b>package</b> (Details about packaged medications)
	 *
     * <p>
     * <b>Definition:</b>
     * Information that only applies to packages (not products)
     * </p> 
	 */
	public void setPackage( String theCode) {
		myPackage = new CodeDt(theCode); 
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
	@Description(
		shortDefinition="powder | tablets | carton +",
		formalDefinition="Describes the form of the item.  Powder; tables; carton"
	)
	private CodeableConceptDt myForm;
	
	@Child(name="ingredient", order=1, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Active or inactive ingredient",
		formalDefinition="Identifies a particular constituent of interest in the product"
	)
	private List<ProductIngredient> myIngredient;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myForm,  myIngredient);
	}

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

	/**
	 * Adds and returns a new value for <b>ingredient</b> (Active or inactive ingredient)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a particular constituent of interest in the product
     * </p> 
	 */
	public ProductIngredient addIngredient() {
		ProductIngredient newType = new ProductIngredient();
		getIngredient().add(newType);
		return newType; 
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
	
	@Child(name="item", order=0, min=1, max=1, type={
		Substance.class,
		Medication.class,
	})
	@Description(
		shortDefinition="The product contained",
		formalDefinition="The actual ingredient - either a substance (simple ingredient) or another medication"
	)
	private ResourceReferenceDt myItem;
	
	@Child(name="amount", type=RatioDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="How much ingredient in product",
		formalDefinition="Specifies how many (or how much) of the items there are in this Medication.  E.g. 250 mg per tablet"
	)
	private RatioDt myAmount;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myItem,  myAmount);
	}

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
	public ResourceReferenceDt getItem() {  
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
	public void setItem(ResourceReferenceDt theValue) {
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