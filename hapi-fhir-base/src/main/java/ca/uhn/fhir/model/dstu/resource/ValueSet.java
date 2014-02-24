











package ca.uhn.fhir.model.dstu.resource;

import java.util.*;
import ca.uhn.fhir.model.api.*;
import ca.uhn.fhir.model.api.annotation.*;
import ca.uhn.fhir.model.primitive.*;
import ca.uhn.fhir.model.dstu.composite.*;

/**
 * HAPI/FHIR <b>ValueSet</b> Resource
 * (A set of codes drawn from one or more code systems)
 *
 * <p>
 * <b>Definition:</b>
 * A value set specifies a set of codes drawn from one or more code systems
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 */
@ResourceDef(name="ValueSet")
public class ValueSet implements IResource {

	@Child(name="identifier", type=StringDt.class, order=0, min=0, max=1)	
	private StringDt myIdentifier;
	
	@Child(name="version", type=StringDt.class, order=1, min=0, max=1)	
	private StringDt myVersion;
	
	@Child(name="name", type=StringDt.class, order=2, min=1, max=1)	
	private StringDt myName;
	
	@Child(name="publisher", type=StringDt.class, order=3, min=0, max=1)	
	private StringDt myPublisher;
	
	@Child(name="telecom", type=ContactDt.class, order=4, min=0, max=Child.MAX_UNLIMITED)	
	private List<ContactDt> myTelecom;
	
	@Child(name="description", type=StringDt.class, order=5, min=1, max=1)	
	private StringDt myDescription;
	
	@Child(name="copyright", type=StringDt.class, order=6, min=0, max=1)	
	private StringDt myCopyright;
	
	@Child(name="status", type=CodeDt.class, order=7, min=1, max=1)	
	private CodeDt myStatus;
	
	@Child(name="experimental", type=BooleanDt.class, order=8, min=0, max=1)	
	private BooleanDt myExperimental;
	
	@Child(name="extensible", type=BooleanDt.class, order=9, min=0, max=1)	
	private BooleanDt myExtensible;
	
	@Child(name="date", type=DateTimeDt.class, order=10, min=0, max=1)	
	private DateTimeDt myDate;
	
	@Child(name="define", order=11, min=0, max=1)	
	private IDatatype myDefine;
	
	@Child(name="compose", order=12, min=0, max=1)	
	private IDatatype myCompose;
	
	@Child(name="expansion", order=13, min=0, max=1)	
	private IDatatype myExpansion;
	
	/**
	 * Gets the value(s) for identifier (Logical id to reference this value set)
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this value set when it is referenced in a specification, model, design or an instance (should be globally unique OID, UUID, or URI)
     * </p> 
	 */
	public StringDt getIdentifier() {
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for identifier (Logical id to reference this value set)
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this value set when it is referenced in a specification, model, design or an instance (should be globally unique OID, UUID, or URI)
     * </p> 
	 */
	public void setIdentifier(StringDt theValue) {
		myIdentifier = theValue;
	}
	
	/**
	 * Gets the value(s) for version (Logical id for this version of the value set)
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this version of the value set when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp
     * </p> 
	 */
	public StringDt getVersion() {
		return myVersion;
	}

	/**
	 * Sets the value(s) for version (Logical id for this version of the value set)
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this version of the value set when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp
     * </p> 
	 */
	public void setVersion(StringDt theValue) {
		myVersion = theValue;
	}
	
	/**
	 * Gets the value(s) for name (Informal name for this value set)
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language name describing the value set
     * </p> 
	 */
	public StringDt getName() {
		return myName;
	}

	/**
	 * Sets the value(s) for name (Informal name for this value set)
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language name describing the value set
     * </p> 
	 */
	public void setName(StringDt theValue) {
		myName = theValue;
	}
	
	/**
	 * Gets the value(s) for publisher (Name of the publisher (Organization or individual))
	 *
     * <p>
     * <b>Definition:</b>
     * The name of the individual or organization that published the value set
     * </p> 
	 */
	public StringDt getPublisher() {
		return myPublisher;
	}

	/**
	 * Sets the value(s) for publisher (Name of the publisher (Organization or individual))
	 *
     * <p>
     * <b>Definition:</b>
     * The name of the individual or organization that published the value set
     * </p> 
	 */
	public void setPublisher(StringDt theValue) {
		myPublisher = theValue;
	}
	
	/**
	 * Gets the value(s) for telecom (Contact information of the publisher)
	 *
     * <p>
     * <b>Definition:</b>
     * Contacts of the publisher to assist a user in finding and communicating with the publisher
     * </p> 
	 */
	public List<ContactDt> getTelecom() {
		return myTelecom;
	}

	/**
	 * Sets the value(s) for telecom (Contact information of the publisher)
	 *
     * <p>
     * <b>Definition:</b>
     * Contacts of the publisher to assist a user in finding and communicating with the publisher
     * </p> 
	 */
	public void setTelecom(List<ContactDt> theValue) {
		myTelecom = theValue;
	}
	
	/**
	 * Gets the value(s) for description (Human language description of the value set)
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language description of the use of the value set - reason for definition, conditions of use, etc.
     * </p> 
	 */
	public StringDt getDescription() {
		return myDescription;
	}

	/**
	 * Sets the value(s) for description (Human language description of the value set)
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language description of the use of the value set - reason for definition, conditions of use, etc.
     * </p> 
	 */
	public void setDescription(StringDt theValue) {
		myDescription = theValue;
	}
	
	/**
	 * Gets the value(s) for copyright (About the value set or its content)
	 *
     * <p>
     * <b>Definition:</b>
     * A copyright statement relating to the value set and/or its contents
     * </p> 
	 */
	public StringDt getCopyright() {
		return myCopyright;
	}

	/**
	 * Sets the value(s) for copyright (About the value set or its content)
	 *
     * <p>
     * <b>Definition:</b>
     * A copyright statement relating to the value set and/or its contents
     * </p> 
	 */
	public void setCopyright(StringDt theValue) {
		myCopyright = theValue;
	}
	
	/**
	 * Gets the value(s) for status (draft | active | retired
)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the value set
     * </p> 
	 */
	public CodeDt getStatus() {
		return myStatus;
	}

	/**
	 * Sets the value(s) for status (draft | active | retired
)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the value set
     * </p> 
	 */
	public void setStatus(CodeDt theValue) {
		myStatus = theValue;
	}
	
	/**
	 * Gets the value(s) for experimental (If for testing purposes, not real usage)
	 *
     * <p>
     * <b>Definition:</b>
     * This valueset was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage
     * </p> 
	 */
	public BooleanDt getExperimental() {
		return myExperimental;
	}

	/**
	 * Sets the value(s) for experimental (If for testing purposes, not real usage)
	 *
     * <p>
     * <b>Definition:</b>
     * This valueset was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage
     * </p> 
	 */
	public void setExperimental(BooleanDt theValue) {
		myExperimental = theValue;
	}
	
	/**
	 * Gets the value(s) for extensible (Whether this is intended to be used with an extensible binding)
	 *
     * <p>
     * <b>Definition:</b>
     * Whether this is intended to be used with an extensible binding or not
     * </p> 
	 */
	public BooleanDt getExtensible() {
		return myExtensible;
	}

	/**
	 * Sets the value(s) for extensible (Whether this is intended to be used with an extensible binding)
	 *
     * <p>
     * <b>Definition:</b>
     * Whether this is intended to be used with an extensible binding or not
     * </p> 
	 */
	public void setExtensible(BooleanDt theValue) {
		myExtensible = theValue;
	}
	
	/**
	 * Gets the value(s) for date (Date for given status)
	 *
     * <p>
     * <b>Definition:</b>
     * The date that the value set status was last changed
     * </p> 
	 */
	public DateTimeDt getDate() {
		return myDate;
	}

	/**
	 * Sets the value(s) for date (Date for given status)
	 *
     * <p>
     * <b>Definition:</b>
     * The date that the value set status was last changed
     * </p> 
	 */
	public void setDate(DateTimeDt theValue) {
		myDate = theValue;
	}
	
	/**
	 * Gets the value(s) for define (When value set defines its own codes)
	 *
     * <p>
     * <b>Definition:</b>
     * ${child.definition}
     * </p> 
	 */
	public IDatatype getDefine() {
		return myDefine;
	}

	/**
	 * Sets the value(s) for define (When value set defines its own codes)
	 *
     * <p>
     * <b>Definition:</b>
     * ${child.definition}
     * </p> 
	 */
	public void setDefine(IDatatype theValue) {
		myDefine = theValue;
	}
	
	/**
	 * Gets the value(s) for compose (When value set includes codes from elsewhere)
	 *
     * <p>
     * <b>Definition:</b>
     * ${child.definition}
     * </p> 
	 */
	public IDatatype getCompose() {
		return myCompose;
	}

	/**
	 * Sets the value(s) for compose (When value set includes codes from elsewhere)
	 *
     * <p>
     * <b>Definition:</b>
     * ${child.definition}
     * </p> 
	 */
	public void setCompose(IDatatype theValue) {
		myCompose = theValue;
	}
	
	/**
	 * Gets the value(s) for expansion (When value set is an expansion)
	 *
     * <p>
     * <b>Definition:</b>
     * ${child.definition}
     * </p> 
	 */
	public IDatatype getExpansion() {
		return myExpansion;
	}

	/**
	 * Sets the value(s) for expansion (When value set is an expansion)
	 *
     * <p>
     * <b>Definition:</b>
     * ${child.definition}
     * </p> 
	 */
	public void setExpansion(IDatatype theValue) {
		myExpansion = theValue;
	}
	

	/**
	 * Block class for child element: <b>ValueSet.define</b> (When value set defines its own codes)
	 *
     * <p>
     * <b>Definition:</b>
     * ${child.definition}
     * </p> 
	 */
	@Block(name="ValueSet.define")	
	public static class Define implements IResourceBlock {
	@Child(name="identifier", type=StringDt.class, order=0, min=0, max=1)	
	private StringDt myIdentifier;
	
	@Child(name="version", type=StringDt.class, order=1, min=0, max=1)	
	private StringDt myVersion;
	
	@Child(name="name", type=StringDt.class, order=2, min=1, max=1)	
	private StringDt myName;
	
	@Child(name="publisher", type=StringDt.class, order=3, min=0, max=1)	
	private StringDt myPublisher;
	
	@Child(name="telecom", type=ContactDt.class, order=4, min=0, max=Child.MAX_UNLIMITED)	
	private List<ContactDt> myTelecom;
	
	@Child(name="description", type=StringDt.class, order=5, min=1, max=1)	
	private StringDt myDescription;
	
	@Child(name="copyright", type=StringDt.class, order=6, min=0, max=1)	
	private StringDt myCopyright;
	
	@Child(name="status", type=CodeDt.class, order=7, min=1, max=1)	
	private CodeDt myStatus;
	
	@Child(name="experimental", type=BooleanDt.class, order=8, min=0, max=1)	
	private BooleanDt myExperimental;
	
	@Child(name="extensible", type=BooleanDt.class, order=9, min=0, max=1)	
	private BooleanDt myExtensible;
	
	@Child(name="date", type=DateTimeDt.class, order=10, min=0, max=1)	
	private DateTimeDt myDate;
	
	@Child(name="define", order=11, min=0, max=1)	
	private IDatatype myDefine;
	
	@Child(name="compose", order=12, min=0, max=1)	
	private IDatatype myCompose;
	
	@Child(name="expansion", order=13, min=0, max=1)	
	private IDatatype myExpansion;
	
	/**
	 * Gets the value(s) for identifier (Logical id to reference this value set)
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this value set when it is referenced in a specification, model, design or an instance (should be globally unique OID, UUID, or URI)
     * </p> 
	 */
	public StringDt getIdentifier() {
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for identifier (Logical id to reference this value set)
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this value set when it is referenced in a specification, model, design or an instance (should be globally unique OID, UUID, or URI)
     * </p> 
	 */
	public void setIdentifier(StringDt theValue) {
		myIdentifier = theValue;
	}
	
	/**
	 * Gets the value(s) for version (Logical id for this version of the value set)
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this version of the value set when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp
     * </p> 
	 */
	public StringDt getVersion() {
		return myVersion;
	}

	/**
	 * Sets the value(s) for version (Logical id for this version of the value set)
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this version of the value set when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp
     * </p> 
	 */
	public void setVersion(StringDt theValue) {
		myVersion = theValue;
	}
	
	/**
	 * Gets the value(s) for name (Informal name for this value set)
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language name describing the value set
     * </p> 
	 */
	public StringDt getName() {
		return myName;
	}

	/**
	 * Sets the value(s) for name (Informal name for this value set)
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language name describing the value set
     * </p> 
	 */
	public void setName(StringDt theValue) {
		myName = theValue;
	}
	
	/**
	 * Gets the value(s) for publisher (Name of the publisher (Organization or individual))
	 *
     * <p>
     * <b>Definition:</b>
     * The name of the individual or organization that published the value set
     * </p> 
	 */
	public StringDt getPublisher() {
		return myPublisher;
	}

	/**
	 * Sets the value(s) for publisher (Name of the publisher (Organization or individual))
	 *
     * <p>
     * <b>Definition:</b>
     * The name of the individual or organization that published the value set
     * </p> 
	 */
	public void setPublisher(StringDt theValue) {
		myPublisher = theValue;
	}
	
	/**
	 * Gets the value(s) for telecom (Contact information of the publisher)
	 *
     * <p>
     * <b>Definition:</b>
     * Contacts of the publisher to assist a user in finding and communicating with the publisher
     * </p> 
	 */
	public List<ContactDt> getTelecom() {
		return myTelecom;
	}

	/**
	 * Sets the value(s) for telecom (Contact information of the publisher)
	 *
     * <p>
     * <b>Definition:</b>
     * Contacts of the publisher to assist a user in finding and communicating with the publisher
     * </p> 
	 */
	public void setTelecom(List<ContactDt> theValue) {
		myTelecom = theValue;
	}
	
	/**
	 * Gets the value(s) for description (Human language description of the value set)
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language description of the use of the value set - reason for definition, conditions of use, etc.
     * </p> 
	 */
	public StringDt getDescription() {
		return myDescription;
	}

	/**
	 * Sets the value(s) for description (Human language description of the value set)
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language description of the use of the value set - reason for definition, conditions of use, etc.
     * </p> 
	 */
	public void setDescription(StringDt theValue) {
		myDescription = theValue;
	}
	
	/**
	 * Gets the value(s) for copyright (About the value set or its content)
	 *
     * <p>
     * <b>Definition:</b>
     * A copyright statement relating to the value set and/or its contents
     * </p> 
	 */
	public StringDt getCopyright() {
		return myCopyright;
	}

	/**
	 * Sets the value(s) for copyright (About the value set or its content)
	 *
     * <p>
     * <b>Definition:</b>
     * A copyright statement relating to the value set and/or its contents
     * </p> 
	 */
	public void setCopyright(StringDt theValue) {
		myCopyright = theValue;
	}
	
	/**
	 * Gets the value(s) for status (draft | active | retired
)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the value set
     * </p> 
	 */
	public CodeDt getStatus() {
		return myStatus;
	}

	/**
	 * Sets the value(s) for status (draft | active | retired
)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the value set
     * </p> 
	 */
	public void setStatus(CodeDt theValue) {
		myStatus = theValue;
	}
	
	/**
	 * Gets the value(s) for experimental (If for testing purposes, not real usage)
	 *
     * <p>
     * <b>Definition:</b>
     * This valueset was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage
     * </p> 
	 */
	public BooleanDt getExperimental() {
		return myExperimental;
	}

	/**
	 * Sets the value(s) for experimental (If for testing purposes, not real usage)
	 *
     * <p>
     * <b>Definition:</b>
     * This valueset was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage
     * </p> 
	 */
	public void setExperimental(BooleanDt theValue) {
		myExperimental = theValue;
	}
	
	/**
	 * Gets the value(s) for extensible (Whether this is intended to be used with an extensible binding)
	 *
     * <p>
     * <b>Definition:</b>
     * Whether this is intended to be used with an extensible binding or not
     * </p> 
	 */
	public BooleanDt getExtensible() {
		return myExtensible;
	}

	/**
	 * Sets the value(s) for extensible (Whether this is intended to be used with an extensible binding)
	 *
     * <p>
     * <b>Definition:</b>
     * Whether this is intended to be used with an extensible binding or not
     * </p> 
	 */
	public void setExtensible(BooleanDt theValue) {
		myExtensible = theValue;
	}
	
	/**
	 * Gets the value(s) for date (Date for given status)
	 *
     * <p>
     * <b>Definition:</b>
     * The date that the value set status was last changed
     * </p> 
	 */
	public DateTimeDt getDate() {
		return myDate;
	}

	/**
	 * Sets the value(s) for date (Date for given status)
	 *
     * <p>
     * <b>Definition:</b>
     * The date that the value set status was last changed
     * </p> 
	 */
	public void setDate(DateTimeDt theValue) {
		myDate = theValue;
	}
	
	/**
	 * Gets the value(s) for define (When value set defines its own codes)
	 *
     * <p>
     * <b>Definition:</b>
     * ${child.definition}
     * </p> 
	 */
	public IDatatype getDefine() {
		return myDefine;
	}

	/**
	 * Sets the value(s) for define (When value set defines its own codes)
	 *
     * <p>
     * <b>Definition:</b>
     * ${child.definition}
     * </p> 
	 */
	public void setDefine(IDatatype theValue) {
		myDefine = theValue;
	}
	
	/**
	 * Gets the value(s) for compose (When value set includes codes from elsewhere)
	 *
     * <p>
     * <b>Definition:</b>
     * ${child.definition}
     * </p> 
	 */
	public IDatatype getCompose() {
		return myCompose;
	}

	/**
	 * Sets the value(s) for compose (When value set includes codes from elsewhere)
	 *
     * <p>
     * <b>Definition:</b>
     * ${child.definition}
     * </p> 
	 */
	public void setCompose(IDatatype theValue) {
		myCompose = theValue;
	}
	
	/**
	 * Gets the value(s) for expansion (When value set is an expansion)
	 *
     * <p>
     * <b>Definition:</b>
     * ${child.definition}
     * </p> 
	 */
	public IDatatype getExpansion() {
		return myExpansion;
	}

	/**
	 * Sets the value(s) for expansion (When value set is an expansion)
	 *
     * <p>
     * <b>Definition:</b>
     * ${child.definition}
     * </p> 
	 */
	public void setExpansion(IDatatype theValue) {
		myExpansion = theValue;
	}
	
	}

	/**
	 * Block class for child element: <b>ValueSet.compose</b> (When value set includes codes from elsewhere)
	 *
     * <p>
     * <b>Definition:</b>
     * ${child.definition}
     * </p> 
	 */
	@Block(name="ValueSet.compose")	
	public static class Compose implements IResourceBlock {
	@Child(name="identifier", type=StringDt.class, order=0, min=0, max=1)	
	private StringDt myIdentifier;
	
	@Child(name="version", type=StringDt.class, order=1, min=0, max=1)	
	private StringDt myVersion;
	
	@Child(name="name", type=StringDt.class, order=2, min=1, max=1)	
	private StringDt myName;
	
	@Child(name="publisher", type=StringDt.class, order=3, min=0, max=1)	
	private StringDt myPublisher;
	
	@Child(name="telecom", type=ContactDt.class, order=4, min=0, max=Child.MAX_UNLIMITED)	
	private List<ContactDt> myTelecom;
	
	@Child(name="description", type=StringDt.class, order=5, min=1, max=1)	
	private StringDt myDescription;
	
	@Child(name="copyright", type=StringDt.class, order=6, min=0, max=1)	
	private StringDt myCopyright;
	
	@Child(name="status", type=CodeDt.class, order=7, min=1, max=1)	
	private CodeDt myStatus;
	
	@Child(name="experimental", type=BooleanDt.class, order=8, min=0, max=1)	
	private BooleanDt myExperimental;
	
	@Child(name="extensible", type=BooleanDt.class, order=9, min=0, max=1)	
	private BooleanDt myExtensible;
	
	@Child(name="date", type=DateTimeDt.class, order=10, min=0, max=1)	
	private DateTimeDt myDate;
	
	@Child(name="define", order=11, min=0, max=1)	
	private IDatatype myDefine;
	
	@Child(name="compose", order=12, min=0, max=1)	
	private IDatatype myCompose;
	
	@Child(name="expansion", order=13, min=0, max=1)	
	private IDatatype myExpansion;
	
	/**
	 * Gets the value(s) for identifier (Logical id to reference this value set)
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this value set when it is referenced in a specification, model, design or an instance (should be globally unique OID, UUID, or URI)
     * </p> 
	 */
	public StringDt getIdentifier() {
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for identifier (Logical id to reference this value set)
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this value set when it is referenced in a specification, model, design or an instance (should be globally unique OID, UUID, or URI)
     * </p> 
	 */
	public void setIdentifier(StringDt theValue) {
		myIdentifier = theValue;
	}
	
	/**
	 * Gets the value(s) for version (Logical id for this version of the value set)
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this version of the value set when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp
     * </p> 
	 */
	public StringDt getVersion() {
		return myVersion;
	}

	/**
	 * Sets the value(s) for version (Logical id for this version of the value set)
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this version of the value set when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp
     * </p> 
	 */
	public void setVersion(StringDt theValue) {
		myVersion = theValue;
	}
	
	/**
	 * Gets the value(s) for name (Informal name for this value set)
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language name describing the value set
     * </p> 
	 */
	public StringDt getName() {
		return myName;
	}

	/**
	 * Sets the value(s) for name (Informal name for this value set)
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language name describing the value set
     * </p> 
	 */
	public void setName(StringDt theValue) {
		myName = theValue;
	}
	
	/**
	 * Gets the value(s) for publisher (Name of the publisher (Organization or individual))
	 *
     * <p>
     * <b>Definition:</b>
     * The name of the individual or organization that published the value set
     * </p> 
	 */
	public StringDt getPublisher() {
		return myPublisher;
	}

	/**
	 * Sets the value(s) for publisher (Name of the publisher (Organization or individual))
	 *
     * <p>
     * <b>Definition:</b>
     * The name of the individual or organization that published the value set
     * </p> 
	 */
	public void setPublisher(StringDt theValue) {
		myPublisher = theValue;
	}
	
	/**
	 * Gets the value(s) for telecom (Contact information of the publisher)
	 *
     * <p>
     * <b>Definition:</b>
     * Contacts of the publisher to assist a user in finding and communicating with the publisher
     * </p> 
	 */
	public List<ContactDt> getTelecom() {
		return myTelecom;
	}

	/**
	 * Sets the value(s) for telecom (Contact information of the publisher)
	 *
     * <p>
     * <b>Definition:</b>
     * Contacts of the publisher to assist a user in finding and communicating with the publisher
     * </p> 
	 */
	public void setTelecom(List<ContactDt> theValue) {
		myTelecom = theValue;
	}
	
	/**
	 * Gets the value(s) for description (Human language description of the value set)
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language description of the use of the value set - reason for definition, conditions of use, etc.
     * </p> 
	 */
	public StringDt getDescription() {
		return myDescription;
	}

	/**
	 * Sets the value(s) for description (Human language description of the value set)
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language description of the use of the value set - reason for definition, conditions of use, etc.
     * </p> 
	 */
	public void setDescription(StringDt theValue) {
		myDescription = theValue;
	}
	
	/**
	 * Gets the value(s) for copyright (About the value set or its content)
	 *
     * <p>
     * <b>Definition:</b>
     * A copyright statement relating to the value set and/or its contents
     * </p> 
	 */
	public StringDt getCopyright() {
		return myCopyright;
	}

	/**
	 * Sets the value(s) for copyright (About the value set or its content)
	 *
     * <p>
     * <b>Definition:</b>
     * A copyright statement relating to the value set and/or its contents
     * </p> 
	 */
	public void setCopyright(StringDt theValue) {
		myCopyright = theValue;
	}
	
	/**
	 * Gets the value(s) for status (draft | active | retired
)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the value set
     * </p> 
	 */
	public CodeDt getStatus() {
		return myStatus;
	}

	/**
	 * Sets the value(s) for status (draft | active | retired
)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the value set
     * </p> 
	 */
	public void setStatus(CodeDt theValue) {
		myStatus = theValue;
	}
	
	/**
	 * Gets the value(s) for experimental (If for testing purposes, not real usage)
	 *
     * <p>
     * <b>Definition:</b>
     * This valueset was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage
     * </p> 
	 */
	public BooleanDt getExperimental() {
		return myExperimental;
	}

	/**
	 * Sets the value(s) for experimental (If for testing purposes, not real usage)
	 *
     * <p>
     * <b>Definition:</b>
     * This valueset was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage
     * </p> 
	 */
	public void setExperimental(BooleanDt theValue) {
		myExperimental = theValue;
	}
	
	/**
	 * Gets the value(s) for extensible (Whether this is intended to be used with an extensible binding)
	 *
     * <p>
     * <b>Definition:</b>
     * Whether this is intended to be used with an extensible binding or not
     * </p> 
	 */
	public BooleanDt getExtensible() {
		return myExtensible;
	}

	/**
	 * Sets the value(s) for extensible (Whether this is intended to be used with an extensible binding)
	 *
     * <p>
     * <b>Definition:</b>
     * Whether this is intended to be used with an extensible binding or not
     * </p> 
	 */
	public void setExtensible(BooleanDt theValue) {
		myExtensible = theValue;
	}
	
	/**
	 * Gets the value(s) for date (Date for given status)
	 *
     * <p>
     * <b>Definition:</b>
     * The date that the value set status was last changed
     * </p> 
	 */
	public DateTimeDt getDate() {
		return myDate;
	}

	/**
	 * Sets the value(s) for date (Date for given status)
	 *
     * <p>
     * <b>Definition:</b>
     * The date that the value set status was last changed
     * </p> 
	 */
	public void setDate(DateTimeDt theValue) {
		myDate = theValue;
	}
	
	/**
	 * Gets the value(s) for define (When value set defines its own codes)
	 *
     * <p>
     * <b>Definition:</b>
     * ${child.definition}
     * </p> 
	 */
	public IDatatype getDefine() {
		return myDefine;
	}

	/**
	 * Sets the value(s) for define (When value set defines its own codes)
	 *
     * <p>
     * <b>Definition:</b>
     * ${child.definition}
     * </p> 
	 */
	public void setDefine(IDatatype theValue) {
		myDefine = theValue;
	}
	
	/**
	 * Gets the value(s) for compose (When value set includes codes from elsewhere)
	 *
     * <p>
     * <b>Definition:</b>
     * ${child.definition}
     * </p> 
	 */
	public IDatatype getCompose() {
		return myCompose;
	}

	/**
	 * Sets the value(s) for compose (When value set includes codes from elsewhere)
	 *
     * <p>
     * <b>Definition:</b>
     * ${child.definition}
     * </p> 
	 */
	public void setCompose(IDatatype theValue) {
		myCompose = theValue;
	}
	
	/**
	 * Gets the value(s) for expansion (When value set is an expansion)
	 *
     * <p>
     * <b>Definition:</b>
     * ${child.definition}
     * </p> 
	 */
	public IDatatype getExpansion() {
		return myExpansion;
	}

	/**
	 * Sets the value(s) for expansion (When value set is an expansion)
	 *
     * <p>
     * <b>Definition:</b>
     * ${child.definition}
     * </p> 
	 */
	public void setExpansion(IDatatype theValue) {
		myExpansion = theValue;
	}
	
	}

	/**
	 * Block class for child element: <b>ValueSet.expansion</b> (When value set is an expansion)
	 *
     * <p>
     * <b>Definition:</b>
     * ${child.definition}
     * </p> 
	 */
	@Block(name="ValueSet.expansion")	
	public static class Expansion implements IResourceBlock {
	@Child(name="identifier", type=StringDt.class, order=0, min=0, max=1)	
	private StringDt myIdentifier;
	
	@Child(name="version", type=StringDt.class, order=1, min=0, max=1)	
	private StringDt myVersion;
	
	@Child(name="name", type=StringDt.class, order=2, min=1, max=1)	
	private StringDt myName;
	
	@Child(name="publisher", type=StringDt.class, order=3, min=0, max=1)	
	private StringDt myPublisher;
	
	@Child(name="telecom", type=ContactDt.class, order=4, min=0, max=Child.MAX_UNLIMITED)	
	private List<ContactDt> myTelecom;
	
	@Child(name="description", type=StringDt.class, order=5, min=1, max=1)	
	private StringDt myDescription;
	
	@Child(name="copyright", type=StringDt.class, order=6, min=0, max=1)	
	private StringDt myCopyright;
	
	@Child(name="status", type=CodeDt.class, order=7, min=1, max=1)	
	private CodeDt myStatus;
	
	@Child(name="experimental", type=BooleanDt.class, order=8, min=0, max=1)	
	private BooleanDt myExperimental;
	
	@Child(name="extensible", type=BooleanDt.class, order=9, min=0, max=1)	
	private BooleanDt myExtensible;
	
	@Child(name="date", type=DateTimeDt.class, order=10, min=0, max=1)	
	private DateTimeDt myDate;
	
	@Child(name="define", order=11, min=0, max=1)	
	private IDatatype myDefine;
	
	@Child(name="compose", order=12, min=0, max=1)	
	private IDatatype myCompose;
	
	@Child(name="expansion", order=13, min=0, max=1)	
	private IDatatype myExpansion;
	
	/**
	 * Gets the value(s) for identifier (Logical id to reference this value set)
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this value set when it is referenced in a specification, model, design or an instance (should be globally unique OID, UUID, or URI)
     * </p> 
	 */
	public StringDt getIdentifier() {
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for identifier (Logical id to reference this value set)
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this value set when it is referenced in a specification, model, design or an instance (should be globally unique OID, UUID, or URI)
     * </p> 
	 */
	public void setIdentifier(StringDt theValue) {
		myIdentifier = theValue;
	}
	
	/**
	 * Gets the value(s) for version (Logical id for this version of the value set)
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this version of the value set when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp
     * </p> 
	 */
	public StringDt getVersion() {
		return myVersion;
	}

	/**
	 * Sets the value(s) for version (Logical id for this version of the value set)
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this version of the value set when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp
     * </p> 
	 */
	public void setVersion(StringDt theValue) {
		myVersion = theValue;
	}
	
	/**
	 * Gets the value(s) for name (Informal name for this value set)
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language name describing the value set
     * </p> 
	 */
	public StringDt getName() {
		return myName;
	}

	/**
	 * Sets the value(s) for name (Informal name for this value set)
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language name describing the value set
     * </p> 
	 */
	public void setName(StringDt theValue) {
		myName = theValue;
	}
	
	/**
	 * Gets the value(s) for publisher (Name of the publisher (Organization or individual))
	 *
     * <p>
     * <b>Definition:</b>
     * The name of the individual or organization that published the value set
     * </p> 
	 */
	public StringDt getPublisher() {
		return myPublisher;
	}

	/**
	 * Sets the value(s) for publisher (Name of the publisher (Organization or individual))
	 *
     * <p>
     * <b>Definition:</b>
     * The name of the individual or organization that published the value set
     * </p> 
	 */
	public void setPublisher(StringDt theValue) {
		myPublisher = theValue;
	}
	
	/**
	 * Gets the value(s) for telecom (Contact information of the publisher)
	 *
     * <p>
     * <b>Definition:</b>
     * Contacts of the publisher to assist a user in finding and communicating with the publisher
     * </p> 
	 */
	public List<ContactDt> getTelecom() {
		return myTelecom;
	}

	/**
	 * Sets the value(s) for telecom (Contact information of the publisher)
	 *
     * <p>
     * <b>Definition:</b>
     * Contacts of the publisher to assist a user in finding and communicating with the publisher
     * </p> 
	 */
	public void setTelecom(List<ContactDt> theValue) {
		myTelecom = theValue;
	}
	
	/**
	 * Gets the value(s) for description (Human language description of the value set)
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language description of the use of the value set - reason for definition, conditions of use, etc.
     * </p> 
	 */
	public StringDt getDescription() {
		return myDescription;
	}

	/**
	 * Sets the value(s) for description (Human language description of the value set)
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language description of the use of the value set - reason for definition, conditions of use, etc.
     * </p> 
	 */
	public void setDescription(StringDt theValue) {
		myDescription = theValue;
	}
	
	/**
	 * Gets the value(s) for copyright (About the value set or its content)
	 *
     * <p>
     * <b>Definition:</b>
     * A copyright statement relating to the value set and/or its contents
     * </p> 
	 */
	public StringDt getCopyright() {
		return myCopyright;
	}

	/**
	 * Sets the value(s) for copyright (About the value set or its content)
	 *
     * <p>
     * <b>Definition:</b>
     * A copyright statement relating to the value set and/or its contents
     * </p> 
	 */
	public void setCopyright(StringDt theValue) {
		myCopyright = theValue;
	}
	
	/**
	 * Gets the value(s) for status (draft | active | retired
)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the value set
     * </p> 
	 */
	public CodeDt getStatus() {
		return myStatus;
	}

	/**
	 * Sets the value(s) for status (draft | active | retired
)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the value set
     * </p> 
	 */
	public void setStatus(CodeDt theValue) {
		myStatus = theValue;
	}
	
	/**
	 * Gets the value(s) for experimental (If for testing purposes, not real usage)
	 *
     * <p>
     * <b>Definition:</b>
     * This valueset was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage
     * </p> 
	 */
	public BooleanDt getExperimental() {
		return myExperimental;
	}

	/**
	 * Sets the value(s) for experimental (If for testing purposes, not real usage)
	 *
     * <p>
     * <b>Definition:</b>
     * This valueset was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage
     * </p> 
	 */
	public void setExperimental(BooleanDt theValue) {
		myExperimental = theValue;
	}
	
	/**
	 * Gets the value(s) for extensible (Whether this is intended to be used with an extensible binding)
	 *
     * <p>
     * <b>Definition:</b>
     * Whether this is intended to be used with an extensible binding or not
     * </p> 
	 */
	public BooleanDt getExtensible() {
		return myExtensible;
	}

	/**
	 * Sets the value(s) for extensible (Whether this is intended to be used with an extensible binding)
	 *
     * <p>
     * <b>Definition:</b>
     * Whether this is intended to be used with an extensible binding or not
     * </p> 
	 */
	public void setExtensible(BooleanDt theValue) {
		myExtensible = theValue;
	}
	
	/**
	 * Gets the value(s) for date (Date for given status)
	 *
     * <p>
     * <b>Definition:</b>
     * The date that the value set status was last changed
     * </p> 
	 */
	public DateTimeDt getDate() {
		return myDate;
	}

	/**
	 * Sets the value(s) for date (Date for given status)
	 *
     * <p>
     * <b>Definition:</b>
     * The date that the value set status was last changed
     * </p> 
	 */
	public void setDate(DateTimeDt theValue) {
		myDate = theValue;
	}
	
	/**
	 * Gets the value(s) for define (When value set defines its own codes)
	 *
     * <p>
     * <b>Definition:</b>
     * ${child.definition}
     * </p> 
	 */
	public IDatatype getDefine() {
		return myDefine;
	}

	/**
	 * Sets the value(s) for define (When value set defines its own codes)
	 *
     * <p>
     * <b>Definition:</b>
     * ${child.definition}
     * </p> 
	 */
	public void setDefine(IDatatype theValue) {
		myDefine = theValue;
	}
	
	/**
	 * Gets the value(s) for compose (When value set includes codes from elsewhere)
	 *
     * <p>
     * <b>Definition:</b>
     * ${child.definition}
     * </p> 
	 */
	public IDatatype getCompose() {
		return myCompose;
	}

	/**
	 * Sets the value(s) for compose (When value set includes codes from elsewhere)
	 *
     * <p>
     * <b>Definition:</b>
     * ${child.definition}
     * </p> 
	 */
	public void setCompose(IDatatype theValue) {
		myCompose = theValue;
	}
	
	/**
	 * Gets the value(s) for expansion (When value set is an expansion)
	 *
     * <p>
     * <b>Definition:</b>
     * ${child.definition}
     * </p> 
	 */
	public IDatatype getExpansion() {
		return myExpansion;
	}

	/**
	 * Sets the value(s) for expansion (When value set is an expansion)
	 *
     * <p>
     * <b>Definition:</b>
     * ${child.definition}
     * </p> 
	 */
	public void setExpansion(IDatatype theValue) {
		myExpansion = theValue;
	}
	
	}



}