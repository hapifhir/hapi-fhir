















package ca.uhn.fhir.model.dstu3.composite;

import java.util.List;

import ca.uhn.fhir.model.api.ICompositeDatatype;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.base.composite.BaseCodingDt;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.UriDt;

/**
 * HAPI/FHIR <b>CodingDt</b> Datatype
 * ()
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
        extends  BaseCodingDt         implements ICompositeDatatype, org.hl7.fhir.instance.model.api.IBaseCoding {

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
	

	@Child(name="system", type=UriDt.class, order=0, min=0, max=1, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="The identification of the code system that defines the meaning of the symbol in the code."
	)
	private UriDt mySystem;
	
	@Child(name="version", type=StringDt.class, order=1, min=0, max=1, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="The version of the code system which was used when choosing this code. Note that a well-maintained code system does not need the version reported, because the meaning of codes is consistent across versions. However this cannot consistently be assured. and when the meaning is not guaranteed to be consistent, the version SHOULD be exchanged"
	)
	private StringDt myVersion;
	
	@Child(name="code", type=CodeDt.class, order=2, min=0, max=1, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="A symbol in syntax defined by the system. The symbol may be a predefined code or an expression in a syntax defined by the coding system (e.g. post-coordination)"
	)
	private CodeDt myCode;
	
	@Child(name="display", type=StringDt.class, order=3, min=0, max=1, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="A representation of the meaning of the code in the system, following the rules of the system"
	)
	private StringDt myDisplay;
	
	@Child(name="userSelected", type=BooleanDt.class, order=4, min=0, max=1, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="Indicates that this coding was chosen by a user directly - i.e. off a pick list of available items (codes or displays)"
	)
	private BooleanDt myUserSelected;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  mySystem,  myVersion,  myCode,  myDisplay,  myUserSelected);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, mySystem, myVersion, myCode, myDisplay, myUserSelected);
	}

	/**
	 * Gets the value(s) for <b>system</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The identification of the code system that defines the meaning of the symbol in the code.
     * </p> 
	 */
	@Override
	public UriDt getSystemElement() {
		if (mySystem == null) {
			mySystem = new UriDt();
		}
		return mySystem;
	}

	
	/**
	 * Gets the value(s) for <b>system</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The identification of the code system that defines the meaning of the symbol in the code.
     * </p> 
	 */
	@Override
	public String getSystem() {
		return getSystemElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>system</b> ()
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
	 * Sets the value for <b>system</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The identification of the code system that defines the meaning of the symbol in the code.
     * </p> 
	 */
	@Override
	public CodingDt setSystem(String theUri) {
		mySystem = new UriDt(theUri); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>version</b> ().
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
	 * Gets the value(s) for <b>version</b> ().
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
	 * Sets the value(s) for <b>version</b> ()
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
	 * Sets the value for <b>version</b> ()
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
	 * Gets the value(s) for <b>code</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A symbol in syntax defined by the system. The symbol may be a predefined code or an expression in a syntax defined by the coding system (e.g. post-coordination)
     * </p> 
	 */
	@Override
	public CodeDt getCodeElement() {
		if (myCode == null) {
			myCode = new CodeDt();
		}
		return myCode;
	}

	
	/**
	 * Gets the value(s) for <b>code</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A symbol in syntax defined by the system. The symbol may be a predefined code or an expression in a syntax defined by the coding system (e.g. post-coordination)
     * </p> 
	 */
	@Override
	public String getCode() {
		return getCodeElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>code</b> ()
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
	 * Sets the value for <b>code</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A symbol in syntax defined by the system. The symbol may be a predefined code or an expression in a syntax defined by the coding system (e.g. post-coordination)
     * </p> 
	 */
	@Override
	public CodingDt setCode(String theCode) {
		myCode = new CodeDt(theCode); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>display</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A representation of the meaning of the code in the system, following the rules of the system
     * </p> 
	 */
	@Override
	public StringDt getDisplayElement() {
		if (myDisplay == null) {
			myDisplay = new StringDt();
		}
		return myDisplay;
	}

	
	/**
	 * Gets the value(s) for <b>display</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A representation of the meaning of the code in the system, following the rules of the system
     * </p> 
	 */
	@Override
	public String getDisplay() {
		return getDisplayElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>display</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A representation of the meaning of the code in the system, following the rules of the system
     * </p> 
	 */
	public CodingDt setDisplay(StringDt theValue) {
		myDisplay = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>display</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A representation of the meaning of the code in the system, following the rules of the system
     * </p> 
	 */
	@Override
	public CodingDt setDisplay(String theString) {
		myDisplay = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>userSelected</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates that this coding was chosen by a user directly - i.e. off a pick list of available items (codes or displays)
     * </p> 
	 */
	public BooleanDt getUserSelectedElement() {  
		if (myUserSelected == null) {
			myUserSelected = new BooleanDt();
		}
		return myUserSelected;
	}

	
	/**
	 * Gets the value(s) for <b>userSelected</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates that this coding was chosen by a user directly - i.e. off a pick list of available items (codes or displays)
     * </p> 
	 */
	public Boolean getUserSelected() {  
		return getUserSelectedElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>userSelected</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates that this coding was chosen by a user directly - i.e. off a pick list of available items (codes or displays)
     * </p> 
	 */
	public CodingDt setUserSelected(BooleanDt theValue) {
		myUserSelected = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>userSelected</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates that this coding was chosen by a user directly - i.e. off a pick list of available items (codes or displays)
     * </p> 
	 */
	public CodingDt setUserSelected( boolean theBoolean) {
		myUserSelected = new BooleanDt(theBoolean); 
		return this; 
	}

 


}
