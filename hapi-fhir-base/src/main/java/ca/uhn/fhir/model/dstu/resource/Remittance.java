















package ca.uhn.fhir.model.dstu.resource;


import java.util.List;

import ca.uhn.fhir.model.api.BaseIdentifiableElement;
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
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.valueset.IdentifierUseEnum;
import ca.uhn.fhir.model.primitive.DecimalDt;
import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.rest.gclient.TokenParam;


/**
 * HAPI/FHIR <b>Remittance</b> Resource
 * (A remittance)
 *
 * <p>
 * <b>Definition:</b>
 * A remittance
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/Remittance">http://hl7.org/fhir/profiles/Remittance</a> 
 * </p>
 *
 */
@ResourceDef(name="Remittance", profile="http://hl7.org/fhir/profiles/Remittance", id="remittance")
public class Remittance extends BaseResource implements IResource {

	/**
	 * Search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Remittance.identifier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="identifier", path="Remittance.identifier", description="", type="token")
	public static final String SP_IDENTIFIER = "identifier";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Remittance.identifier</b><br/>
	 * </p>
	 */
	public static final TokenParam IDENTIFIER = new TokenParam(SP_IDENTIFIER);

	/**
	 * Search parameter constant for <b>service</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Remittance.service.code</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="service", path="Remittance.service.code", description="", type="token")
	public static final String SP_SERVICE = "service";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>service</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Remittance.service.code</b><br/>
	 * </p>
	 */
	public static final TokenParam SERVICE = new TokenParam(SP_SERVICE);


	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="Remittance id",
		formalDefinition="The remittance identifier"
	)
	private IdentifierDt myIdentifier;
	
	@Child(name="service", order=1, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="A service paid as part of remittance",
		formalDefinition="A service paid as part of remittance"
	)
	private java.util.List<Service> myService;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myService);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myService);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> (Remittance id).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The remittance identifier
     * </p> 
	 */
	public IdentifierDt getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new IdentifierDt();
		}
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for <b>identifier</b> (Remittance id)
	 *
     * <p>
     * <b>Definition:</b>
     * The remittance identifier
     * </p> 
	 */
	public Remittance setIdentifier(IdentifierDt theValue) {
		myIdentifier = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>identifier</b> (Remittance id)
	 *
     * <p>
     * <b>Definition:</b>
     * The remittance identifier
     * </p> 
	 */
	public Remittance setIdentifier( IdentifierUseEnum theUse,  String theSystem,  String theValue,  String theLabel) {
		myIdentifier = new IdentifierDt(theUse, theSystem, theValue, theLabel); 
		return this; 
	}

	/**
	 * Sets the value for <b>identifier</b> (Remittance id)
	 *
     * <p>
     * <b>Definition:</b>
     * The remittance identifier
     * </p> 
	 */
	public Remittance setIdentifier( String theSystem,  String theValue) {
		myIdentifier = new IdentifierDt(theSystem, theValue); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>service</b> (A service paid as part of remittance).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A service paid as part of remittance
     * </p> 
	 */
	public java.util.List<Service> getService() {  
		if (myService == null) {
			myService = new java.util.ArrayList<Service>();
		}
		return myService;
	}

	/**
	 * Sets the value(s) for <b>service</b> (A service paid as part of remittance)
	 *
     * <p>
     * <b>Definition:</b>
     * A service paid as part of remittance
     * </p> 
	 */
	public Remittance setService(java.util.List<Service> theValue) {
		myService = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>service</b> (A service paid as part of remittance)
	 *
     * <p>
     * <b>Definition:</b>
     * A service paid as part of remittance
     * </p> 
	 */
	public Service addService() {
		Service newType = new Service();
		getService().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>service</b> (A service paid as part of remittance),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * A service paid as part of remittance
     * </p> 
	 */
	public Service getServiceFirstRep() {
		if (getService().isEmpty()) {
			return addService();
		}
		return getService().get(0); 
	}
  
	/**
	 * Block class for child element: <b>Remittance.service</b> (A service paid as part of remittance)
	 *
     * <p>
     * <b>Definition:</b>
     * A service paid as part of remittance
     * </p> 
	 */
	@Block()	
	public static class Service extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="instance", type=IntegerDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="Service instance number",
		formalDefinition="The service instance number for the original transaction"
	)
	private IntegerDt myInstance;
	
	@Child(name="code", type=CodeableConceptDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="Professional service code",
		formalDefinition="The code for the professional service"
	)
	private CodeableConceptDt myCode;
	
	@Child(name="rate", type=DecimalDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="Benefit Rate %",
		formalDefinition="The percent of the service fee which would be elegible for coverage"
	)
	private DecimalDt myRate;
	
	@Child(name="benefit", type=DecimalDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="Benefit amount",
		formalDefinition="The amount payable for a submitted service (includes both professional and lab fees.)"
	)
	private DecimalDt myBenefit;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myInstance,  myCode,  myRate,  myBenefit);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myInstance, myCode, myRate, myBenefit);
	}

	/**
	 * Gets the value(s) for <b>instance</b> (Service instance number).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The service instance number for the original transaction
     * </p> 
	 */
	public IntegerDt getInstance() {  
		if (myInstance == null) {
			myInstance = new IntegerDt();
		}
		return myInstance;
	}

	/**
	 * Sets the value(s) for <b>instance</b> (Service instance number)
	 *
     * <p>
     * <b>Definition:</b>
     * The service instance number for the original transaction
     * </p> 
	 */
	public Service setInstance(IntegerDt theValue) {
		myInstance = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>instance</b> (Service instance number)
	 *
     * <p>
     * <b>Definition:</b>
     * The service instance number for the original transaction
     * </p> 
	 */
	public Service setInstance( int theInteger) {
		myInstance = new IntegerDt(theInteger); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>code</b> (Professional service code).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The code for the professional service
     * </p> 
	 */
	public CodeableConceptDt getCode() {  
		if (myCode == null) {
			myCode = new CodeableConceptDt();
		}
		return myCode;
	}

	/**
	 * Sets the value(s) for <b>code</b> (Professional service code)
	 *
     * <p>
     * <b>Definition:</b>
     * The code for the professional service
     * </p> 
	 */
	public Service setCode(CodeableConceptDt theValue) {
		myCode = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>rate</b> (Benefit Rate %).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The percent of the service fee which would be elegible for coverage
     * </p> 
	 */
	public DecimalDt getRate() {  
		if (myRate == null) {
			myRate = new DecimalDt();
		}
		return myRate;
	}

	/**
	 * Sets the value(s) for <b>rate</b> (Benefit Rate %)
	 *
     * <p>
     * <b>Definition:</b>
     * The percent of the service fee which would be elegible for coverage
     * </p> 
	 */
	public Service setRate(DecimalDt theValue) {
		myRate = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>rate</b> (Benefit Rate %)
	 *
     * <p>
     * <b>Definition:</b>
     * The percent of the service fee which would be elegible for coverage
     * </p> 
	 */
	public Service setRate( long theValue) {
		myRate = new DecimalDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>rate</b> (Benefit Rate %)
	 *
     * <p>
     * <b>Definition:</b>
     * The percent of the service fee which would be elegible for coverage
     * </p> 
	 */
	public Service setRate( double theValue) {
		myRate = new DecimalDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>rate</b> (Benefit Rate %)
	 *
     * <p>
     * <b>Definition:</b>
     * The percent of the service fee which would be elegible for coverage
     * </p> 
	 */
	public Service setRate( java.math.BigDecimal theValue) {
		myRate = new DecimalDt(theValue); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>benefit</b> (Benefit amount).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The amount payable for a submitted service (includes both professional and lab fees.)
     * </p> 
	 */
	public DecimalDt getBenefit() {  
		if (myBenefit == null) {
			myBenefit = new DecimalDt();
		}
		return myBenefit;
	}

	/**
	 * Sets the value(s) for <b>benefit</b> (Benefit amount)
	 *
     * <p>
     * <b>Definition:</b>
     * The amount payable for a submitted service (includes both professional and lab fees.)
     * </p> 
	 */
	public Service setBenefit(DecimalDt theValue) {
		myBenefit = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>benefit</b> (Benefit amount)
	 *
     * <p>
     * <b>Definition:</b>
     * The amount payable for a submitted service (includes both professional and lab fees.)
     * </p> 
	 */
	public Service setBenefit( long theValue) {
		myBenefit = new DecimalDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>benefit</b> (Benefit amount)
	 *
     * <p>
     * <b>Definition:</b>
     * The amount payable for a submitted service (includes both professional and lab fees.)
     * </p> 
	 */
	public Service setBenefit( double theValue) {
		myBenefit = new DecimalDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>benefit</b> (Benefit amount)
	 *
     * <p>
     * <b>Definition:</b>
     * The amount payable for a submitted service (includes both professional and lab fees.)
     * </p> 
	 */
	public Service setBenefit( java.math.BigDecimal theValue) {
		myBenefit = new DecimalDt(theValue); 
		return this; 
	}

 

	}




}