















package ca.uhn.fhir.model.dstu.resource;

/*
 * #%L
 * HAPI FHIR Library
 * %%
 * Copyright (C) 2014 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import java.util.List;

import ca.uhn.fhir.model.api.BaseElement;
import ca.uhn.fhir.model.api.BaseResource;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.IResourceBlock;
import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.dstu.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu.composite.RatioDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.valueset.MedicationKindEnum;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.gclient.Include;
import ca.uhn.fhir.rest.gclient.ReferenceParam;
import ca.uhn.fhir.rest.gclient.StringParam;
import ca.uhn.fhir.rest.gclient.TokenParam;


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
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/Medication">http://hl7.org/fhir/profiles/Medication</a> 
 * </p>
 *
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
	@SearchParamDefinition(name="code", path="Medication.code", description="", type="token")
	public static final String SP_CODE = "code";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>code</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Medication.code</b><br/>
	 * </p>
	 */
	public static final TokenParam CODE = new TokenParam(SP_CODE);

	/**
	 * Search parameter constant for <b>name</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Medication.name</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="name", path="Medication.name", description="", type="string")
	public static final String SP_NAME = "name";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>name</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Medication.name</b><br/>
	 * </p>
	 */
	public static final StringParam NAME = new StringParam(SP_NAME);

	/**
	 * Search parameter constant for <b>manufacturer</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Medication.manufacturer</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="manufacturer", path="Medication.manufacturer", description="", type="reference")
	public static final String SP_MANUFACTURER = "manufacturer";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>manufacturer</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Medication.manufacturer</b><br/>
	 * </p>
	 */
	public static final ReferenceParam MANUFACTURER = new ReferenceParam(SP_MANUFACTURER);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Medication.manufacturer</b>".
	 */
	public static final Include INCLUDE_MANUFACTURER = new Include("Medication.manufacturer");

	/**
	 * Search parameter constant for <b>form</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Medication.product.form</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="form", path="Medication.product.form", description="", type="token")
	public static final String SP_FORM = "form";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>form</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Medication.product.form</b><br/>
	 * </p>
	 */
	public static final TokenParam FORM = new TokenParam(SP_FORM);

	/**
	 * Search parameter constant for <b>ingredient</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Medication.product.ingredient.item</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="ingredient", path="Medication.product.ingredient.item", description="", type="reference")
	public static final String SP_INGREDIENT = "ingredient";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>ingredient</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Medication.product.ingredient.item</b><br/>
	 * </p>
	 */
	public static final ReferenceParam INGREDIENT = new ReferenceParam(SP_INGREDIENT);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Medication.product.ingredient.item</b>".
	 */
	public static final Include INCLUDE_PRODUCT_INGREDIENT_ITEM = new Include("Medication.product.ingredient.item");

	/**
	 * Search parameter constant for <b>container</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Medication.package.container</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="container", path="Medication.package.container", description="", type="token")
	public static final String SP_CONTAINER = "container";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>container</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Medication.package.container</b><br/>
	 * </p>
	 */
	public static final TokenParam CONTAINER = new TokenParam(SP_CONTAINER);

	/**
	 * Search parameter constant for <b>content</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Medication.package.content.item</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="content", path="Medication.package.content.item", description="", type="reference")
	public static final String SP_CONTENT = "content";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>content</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Medication.package.content.item</b><br/>
	 * </p>
	 */
	public static final ReferenceParam CONTENT = new ReferenceParam(SP_CONTENT);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Medication.package.content.item</b>".
	 */
	public static final Include INCLUDE_PACKAGE_CONTENT_ITEM = new Include("Medication.package.content.item");


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
		ca.uhn.fhir.model.dstu.resource.Organization.class	})
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
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myName, myCode, myIsBrand, myManufacturer, myKind, myProduct, myPackage);
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
	public Medication setName(StringDt theValue) {
		myName = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>name</b> (Common / Commercial name)
	 *
     * <p>
     * <b>Definition:</b>
     * The common/commercial name of the medication absent information such as strength, form, etc.  E.g. Acetaminophen, Tylenol 3, etc.  The fully coordinated name is communicated as the display of Medication.code
     * </p> 
	 */
	public Medication setName( String theString) {
		myName = new StringDt(theString); 
		return this; 
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
	public Medication setCode(CodeableConceptDt theValue) {
		myCode = theValue;
		return this;
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
	public Medication setIsBrand(BooleanDt theValue) {
		myIsBrand = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>isBrand</b> (True if a brand)
	 *
     * <p>
     * <b>Definition:</b>
     * Set to true if the item is attributable to a specific manufacturer (even if we don't know who that is)
     * </p> 
	 */
	public Medication setIsBrand( boolean theBoolean) {
		myIsBrand = new BooleanDt(theBoolean); 
		return this; 
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
	public Medication setManufacturer(ResourceReferenceDt theValue) {
		myManufacturer = theValue;
		return this;
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
	public Medication setKind(BoundCodeDt<MedicationKindEnum> theValue) {
		myKind = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>kind</b> (product | package)
	 *
     * <p>
     * <b>Definition:</b>
     * Medications are either a single administrable product or a package that contains one or more products.
     * </p> 
	 */
	public Medication setKind(MedicationKindEnum theValue) {
		getKind().setValueAsEnum(theValue);
		return this;
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
	public Medication setProduct(Product theValue) {
		myProduct = theValue;
		return this;
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
	public Medication setPackage(CodeDt theValue) {
		myPackage = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>package</b> (Details about packaged medications)
	 *
     * <p>
     * <b>Definition:</b>
     * Information that only applies to packages (not products)
     * </p> 
	 */
	public Medication setPackage( String theCode) {
		myPackage = new CodeDt(theCode); 
		return this; 
	}

 
	/**
	 * Block class for child element: <b>Medication.product</b> (Administrable medication details)
	 *
     * <p>
     * <b>Definition:</b>
     * Information that only applies to products (not packages)
     * </p> 
	 */
	@Block()	
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
	private java.util.List<ProductIngredient> myIngredient;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myForm,  myIngredient);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myForm, myIngredient);
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
	public Product setForm(CodeableConceptDt theValue) {
		myForm = theValue;
		return this;
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
	public java.util.List<ProductIngredient> getIngredient() {  
		if (myIngredient == null) {
			myIngredient = new java.util.ArrayList<ProductIngredient>();
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
	public Product setIngredient(java.util.List<ProductIngredient> theValue) {
		myIngredient = theValue;
		return this;
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

	/**
	 * Gets the first repetition for <b>ingredient</b> (Active or inactive ingredient),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a particular constituent of interest in the product
     * </p> 
	 */
	public ProductIngredient getIngredientFirstRep() {
		if (getIngredient().isEmpty()) {
			return addIngredient();
		}
		return getIngredient().get(0); 
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
	@Block()	
	public static class ProductIngredient extends BaseElement implements IResourceBlock {
	
	@Child(name="item", order=0, min=1, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Substance.class, 		ca.uhn.fhir.model.dstu.resource.Medication.class	})
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
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myItem, myAmount);
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
	public ProductIngredient setItem(ResourceReferenceDt theValue) {
		myItem = theValue;
		return this;
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
	public ProductIngredient setAmount(RatioDt theValue) {
		myAmount = theValue;
		return this;
	}

  

	}





}
