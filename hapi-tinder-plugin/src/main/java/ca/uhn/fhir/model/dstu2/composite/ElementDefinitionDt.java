















package ca.uhn.fhir.model.dstu2.composite;

import java.net.URI;
import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

import ca.uhn.fhir.model.api.*;
import ca.uhn.fhir.model.primitive.*;
import ca.uhn.fhir.model.api.annotation.*;
import ca.uhn.fhir.model.base.composite.*;
import ca.uhn.fhir.model.dstu2.valueset.AddressUseEnum;
import ca.uhn.fhir.model.dstu2.valueset.AggregationModeEnum;
import ca.uhn.fhir.model.dstu2.valueset.BindingConformanceEnum;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.valueset.ConstraintSeverityEnum;
import ca.uhn.fhir.model.dstu2.valueset.ContactPointSystemEnum;
import ca.uhn.fhir.model.dstu2.valueset.ContactPointUseEnum;
import ca.uhn.fhir.model.dstu2.valueset.DataTypeEnum;
import ca.uhn.fhir.model.dstu2.valueset.EventTimingEnum;
import ca.uhn.fhir.model.dstu2.valueset.IdentifierUseEnum;
import ca.uhn.fhir.model.dstu2.valueset.NameUseEnum;
import ca.uhn.fhir.model.dstu2.resource.Organization;
import ca.uhn.fhir.model.dstu2.composite.PeriodDt;
import ca.uhn.fhir.model.dstu2.valueset.PropertyRepresentationEnum;
import ca.uhn.fhir.model.dstu2.valueset.QuantityComparatorEnum;
import ca.uhn.fhir.model.dstu2.composite.QuantityDt;
import ca.uhn.fhir.model.dstu2.valueset.SlicingRulesEnum;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.dstu2.valueset.UnitsOfTimeEnum;
import ca.uhn.fhir.model.dstu2.resource.ValueSet;
import ca.uhn.fhir.model.primitive.Base64BinaryDt;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.DecimalDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.UriDt;

/**
 * HAPI/FHIR <b>ElementDefinitionDt</b> Datatype
 * (ElementDefinition)
 *
 * <p>
 * <b>Definition:</b>
 * Captures constraints on each element within the resource, profile, or extension
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 */
@DatatypeDef(name="ElementDefinitionDt") 
public class ElementDefinitionDt
        extends  BaseIdentifiableElement         implements ICompositeDatatype
{

	/**
	 * Constructor
	 */
	public ElementDefinitionDt() {
		// nothing
	}


	@Child(name="path", type=StringDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="ElementDefinition.path",
		formalDefinition="The path identifies the element and is expressed as a \".\"-separated list of ancestor elements, beginning with the name of the resource or extension"
	)
	private StringDt myPath;
	
	@Child(name="representation", type=CodeDt.class, order=1, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="ElementDefinition.representation",
		formalDefinition="Codes that define how this element is represented in instances, when the deviation varies from the normal case"
	)
	private java.util.List<BoundCodeDt<PropertyRepresentationEnum>> myRepresentation;
	
	@Child(name="name", type=StringDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="ElementDefinition.name",
		formalDefinition="The name of this element definition (to refer to it from other element definitions using ElementDefinition.nameReference). This is a unique name referring to a specific set of constraints applied to this element. One use of this is to provide a name to different slices of the same element"
	)
	private StringDt myName;
	
	@Child(name="slicing", order=3, min=0, max=1)	
	@Description(
		shortDefinition="ElementDefinition.slicing",
		formalDefinition="Indicates that the element is sliced into a set of alternative definitions (there are multiple definitions on a single element in the base resource). The set of slices is any elements that come after this in the element sequence that have the same path, until a shorter path occurs (the shorter path terminates the set)"
	)
	private Slicing mySlicing;
	
	@Child(name="short", type=StringDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="ElementDefinition.short",
		formalDefinition="A concise definition that  is shown in the generated XML format that summarizes profiles (used throughout the specification)"
	)
	private StringDt myShort;
	
	@Child(name="formal", type=StringDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="ElementDefinition.formal",
		formalDefinition="The definition SHALL be consistent with the base definition, but convey the meaning of the element in the particular context of use of the resource"
	)
	private StringDt myFormal;
	
	@Child(name="comments", type=StringDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="ElementDefinition.comments",
		formalDefinition="Comments about the use of the element, including notes about how to use the data properly, exceptions to proper use, etc."
	)
	private StringDt myComments;
	
	@Child(name="requirements", type=StringDt.class, order=7, min=0, max=1)	
	@Description(
		shortDefinition="ElementDefinition.requirements",
		formalDefinition="Explains why this element is needed and why it's been constrained as it has"
	)
	private StringDt myRequirements;
	
	@Child(name="synonym", type=StringDt.class, order=8, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="ElementDefinition.synonym",
		formalDefinition="Identifies additional names by which this element might also be known"
	)
	private java.util.List<StringDt> mySynonym;
	
	@Child(name="min", type=IntegerDt.class, order=9, min=0, max=1)	
	@Description(
		shortDefinition="ElementDefinition.min",
		formalDefinition="The minimum number of times this element SHALL appear in the instance"
	)
	private IntegerDt myMin;
	
	@Child(name="max", type=StringDt.class, order=10, min=0, max=1)	
	@Description(
		shortDefinition="ElementDefinition.max",
		formalDefinition="The maximum number of times this element is permitted to appear in the instance"
	)
	private StringDt myMax;
	
	@Child(name="type", order=11, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="ElementDefinition.type",
		formalDefinition="The data type or resource that the value of this element is permitted to be"
	)
	private java.util.List<Type> myType;
	
	@Child(name="nameReference", type=StringDt.class, order=12, min=0, max=1)	
	@Description(
		shortDefinition="ElementDefinition.nameReference",
		formalDefinition="Identifies the name of a slice defined elsewhere in the profile whose constraints should be applied to the current element"
	)
	private StringDt myNameReference;
	
	@Child(name="defaultValue", type=IDatatype.class, order=13, min=0, max=1)	
	@Description(
		shortDefinition="ElementDefinition.defaultValue[x]",
		formalDefinition="The value that should be used if there is no value stated in the instance"
	)
	private IDatatype myDefaultValue;
	
	@Child(name="meaningWhenMissing", type=StringDt.class, order=14, min=0, max=1)	
	@Description(
		shortDefinition="ElementDefinition.meaningWhenMissing",
		formalDefinition="The Implicit meaning that is to be understood when this element is missing"
	)
	private StringDt myMeaningWhenMissing;
	
	@Child(name="fixed", type=IDatatype.class, order=15, min=0, max=1)	
	@Description(
		shortDefinition="ElementDefinition.fixed[x]",
		formalDefinition="Specifies a value that SHALL be exactly the value  for this element in the instance. For purposes of comparison, non-signficant whitespace is ignored, and all values must be an exact match (case and accent sensitive). Missing elements/attributes must also be missing"
	)
	private IDatatype myFixed;
	
	@Child(name="pattern", type=IDatatype.class, order=16, min=0, max=1)	
	@Description(
		shortDefinition="ElementDefinition.pattern[x]",
		formalDefinition="Specifies a value that the value in the instance SHALL follow - that is, any value in the pattern must be found in the instance. Other additional values may be found too. This is effectively constraint by example.  The values of elements present in the pattern must match exactly (case-senstive, accent-sensitive, etc.)"
	)
	private IDatatype myPattern;
	
	@Child(name="example", type=IDatatype.class, order=17, min=0, max=1)	
	@Description(
		shortDefinition="ElementDefinition.example[x]",
		formalDefinition="An example value for this element"
	)
	private IDatatype myExample;
	
	@Child(name="maxLength", type=IntegerDt.class, order=18, min=0, max=1)	
	@Description(
		shortDefinition="ElementDefinition.maxLength",
		formalDefinition="Indicates the maximum length in characters that is permitted to be present in conformant instances and which is expected to be supported by conformant consumers that support the element"
	)
	private IntegerDt myMaxLength;
	
	@Child(name="condition", type=IdDt.class, order=19, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="ElementDefinition.condition",
		formalDefinition="A reference to an invariant that may make additional statements about the cardinality or value in the instance"
	)
	private java.util.List<IdDt> myCondition;
	
	@Child(name="constraint", order=20, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="ElementDefinition.constraint",
		formalDefinition="Formal constraints such as co-occurrence and other constraints that can be computationally evaluated within the context of the instance"
	)
	private java.util.List<Constraint> myConstraint;
	
	@Child(name="mustSupport", type=BooleanDt.class, order=21, min=0, max=1)	
	@Description(
		shortDefinition="ElementDefinition.mustSupport",
		formalDefinition="If true, conformant resource authors SHALL be capable of providing a value for the element and resource consumers SHALL be capable of extracting and doing something useful with the data element.  If false, the element may be ignored and not supported"
	)
	private BooleanDt myMustSupport;
	
	@Child(name="isModifier", type=BooleanDt.class, order=22, min=0, max=1)	
	@Description(
		shortDefinition="ElementDefinition.isModifier",
		formalDefinition="If true, the value of this element affects the interpretation of the element or resource that contains it, and the value of the element cannot be ignored. Typically, this is used for status, negation and qualification codes. The effect of this is that the element cannot be ignored by systems: they SHALL either recognize the element and process it, and/or a pre-determination has been made that it is not relevant to their particular system."
	)
	private BooleanDt myIsModifier;
	
	@Child(name="isSummary", type=BooleanDt.class, order=23, min=0, max=1)	
	@Description(
		shortDefinition="ElementDefinition.isSummary",
		formalDefinition="Whether the element should be included if a client requests a search with the parameter _summary=true"
	)
	private BooleanDt myIsSummary;
	
	@Child(name="binding", order=24, min=0, max=1)	
	@Description(
		shortDefinition="ElementDefinition.binding",
		formalDefinition="Binds to a value set if this element is coded (code, Coding, CodeableConcept)"
	)
	private Binding myBinding;
	
	@Child(name="mapping", order=25, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="ElementDefinition.mapping",
		formalDefinition="Identifies a concept from an external specification that roughly corresponds to this element"
	)
	private java.util.List<Mapping> myMapping;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myPath,  myRepresentation,  myName,  mySlicing,  myShort,  myFormal,  myComments,  myRequirements,  mySynonym,  myMin,  myMax,  myType,  myNameReference,  myDefaultValue,  myMeaningWhenMissing,  myFixed,  myPattern,  myExample,  myMaxLength,  myCondition,  myConstraint,  myMustSupport,  myIsModifier,  myIsSummary,  myBinding,  myMapping);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myPath, myRepresentation, myName, mySlicing, myShort, myFormal, myComments, myRequirements, mySynonym, myMin, myMax, myType, myNameReference, myDefaultValue, myMeaningWhenMissing, myFixed, myPattern, myExample, myMaxLength, myCondition, myConstraint, myMustSupport, myIsModifier, myIsSummary, myBinding, myMapping);
	}

	/**
	 * Gets the value(s) for <b>path</b> (ElementDefinition.path).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The path identifies the element and is expressed as a \&quot;.\&quot;-separated list of ancestor elements, beginning with the name of the resource or extension
     * </p> 
	 */
	public StringDt getPathElement() {  
		if (myPath == null) {
			myPath = new StringDt();
		}
		return myPath;
	}

	
	/**
	 * Gets the value(s) for <b>path</b> (ElementDefinition.path).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The path identifies the element and is expressed as a \&quot;.\&quot;-separated list of ancestor elements, beginning with the name of the resource or extension
     * </p> 
	 */
	public String getPath() {  
		return getPathElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>path</b> (ElementDefinition.path)
	 *
     * <p>
     * <b>Definition:</b>
     * The path identifies the element and is expressed as a \&quot;.\&quot;-separated list of ancestor elements, beginning with the name of the resource or extension
     * </p> 
	 */
	public ElementDefinitionDt setPath(StringDt theValue) {
		myPath = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>path</b> (ElementDefinition.path)
	 *
     * <p>
     * <b>Definition:</b>
     * The path identifies the element and is expressed as a \&quot;.\&quot;-separated list of ancestor elements, beginning with the name of the resource or extension
     * </p> 
	 */
	public ElementDefinitionDt setPath( String theString) {
		myPath = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>representation</b> (ElementDefinition.representation).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Codes that define how this element is represented in instances, when the deviation varies from the normal case
     * </p> 
	 */
	public java.util.List<BoundCodeDt<PropertyRepresentationEnum>> getRepresentation() {  
		if (myRepresentation == null) {
			myRepresentation = new java.util.ArrayList<BoundCodeDt<PropertyRepresentationEnum>>();
		}
		return myRepresentation;
	}

	/**
	 * Sets the value(s) for <b>representation</b> (ElementDefinition.representation)
	 *
     * <p>
     * <b>Definition:</b>
     * Codes that define how this element is represented in instances, when the deviation varies from the normal case
     * </p> 
	 */
	public ElementDefinitionDt setRepresentation(java.util.List<BoundCodeDt<PropertyRepresentationEnum>> theValue) {
		myRepresentation = theValue;
		return this;
	}
	
	

	/**
	 * Add a value for <b>representation</b> (ElementDefinition.representation) using an enumerated type. This
	 * is intended as a convenience method for situations where the FHIR defined ValueSets are mandatory
	 * or contain the desirable codes. If you wish to use codes other than those which are built-in, 
	 * you may also use the {@link #addRepresentation()} method.
	 *
     * <p>
     * <b>Definition:</b>
     * Codes that define how this element is represented in instances, when the deviation varies from the normal case
     * </p> 
	 */
	public BoundCodeDt<PropertyRepresentationEnum> addRepresentation(PropertyRepresentationEnum theValue) {
		BoundCodeDt<PropertyRepresentationEnum> retVal = new BoundCodeDt<PropertyRepresentationEnum>(PropertyRepresentationEnum.VALUESET_BINDER, theValue);
		getRepresentation().add(retVal);
		return retVal;
	}

	/**
	 * Gets the first repetition for <b>representation</b> (ElementDefinition.representation),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Codes that define how this element is represented in instances, when the deviation varies from the normal case
     * </p> 
	 */
	public BoundCodeDt<PropertyRepresentationEnum> getRepresentationFirstRep() {
		if (getRepresentation().size() == 0) {
			addRepresentation();
		}
		return getRepresentation().get(0);
	}

	/**
	 * Add a value for <b>representation</b> (ElementDefinition.representation)
	 *
     * <p>
     * <b>Definition:</b>
     * Codes that define how this element is represented in instances, when the deviation varies from the normal case
     * </p> 
	 */
	public BoundCodeDt<PropertyRepresentationEnum> addRepresentation() {
		BoundCodeDt<PropertyRepresentationEnum> retVal = new BoundCodeDt<PropertyRepresentationEnum>(PropertyRepresentationEnum.VALUESET_BINDER);
		getRepresentation().add(retVal);
		return retVal;
	}

	/**
	 * Sets the value(s), and clears any existing value(s) for <b>representation</b> (ElementDefinition.representation)
	 *
     * <p>
     * <b>Definition:</b>
     * Codes that define how this element is represented in instances, when the deviation varies from the normal case
     * </p> 
	 */
	public ElementDefinitionDt setRepresentation(PropertyRepresentationEnum theValue) {
		getRepresentation().clear();
		addRepresentation(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>name</b> (ElementDefinition.name).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The name of this element definition (to refer to it from other element definitions using ElementDefinition.nameReference). This is a unique name referring to a specific set of constraints applied to this element. One use of this is to provide a name to different slices of the same element
     * </p> 
	 */
	public StringDt getNameElement() {  
		if (myName == null) {
			myName = new StringDt();
		}
		return myName;
	}

	
	/**
	 * Gets the value(s) for <b>name</b> (ElementDefinition.name).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The name of this element definition (to refer to it from other element definitions using ElementDefinition.nameReference). This is a unique name referring to a specific set of constraints applied to this element. One use of this is to provide a name to different slices of the same element
     * </p> 
	 */
	public String getName() {  
		return getNameElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>name</b> (ElementDefinition.name)
	 *
     * <p>
     * <b>Definition:</b>
     * The name of this element definition (to refer to it from other element definitions using ElementDefinition.nameReference). This is a unique name referring to a specific set of constraints applied to this element. One use of this is to provide a name to different slices of the same element
     * </p> 
	 */
	public ElementDefinitionDt setName(StringDt theValue) {
		myName = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>name</b> (ElementDefinition.name)
	 *
     * <p>
     * <b>Definition:</b>
     * The name of this element definition (to refer to it from other element definitions using ElementDefinition.nameReference). This is a unique name referring to a specific set of constraints applied to this element. One use of this is to provide a name to different slices of the same element
     * </p> 
	 */
	public ElementDefinitionDt setName( String theString) {
		myName = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>slicing</b> (ElementDefinition.slicing).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates that the element is sliced into a set of alternative definitions (there are multiple definitions on a single element in the base resource). The set of slices is any elements that come after this in the element sequence that have the same path, until a shorter path occurs (the shorter path terminates the set)
     * </p> 
	 */
	public Slicing getSlicing() {  
		if (mySlicing == null) {
			mySlicing = new Slicing();
		}
		return mySlicing;
	}

	/**
	 * Sets the value(s) for <b>slicing</b> (ElementDefinition.slicing)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates that the element is sliced into a set of alternative definitions (there are multiple definitions on a single element in the base resource). The set of slices is any elements that come after this in the element sequence that have the same path, until a shorter path occurs (the shorter path terminates the set)
     * </p> 
	 */
	public ElementDefinitionDt setSlicing(Slicing theValue) {
		mySlicing = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>short</b> (ElementDefinition.short).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A concise definition that  is shown in the generated XML format that summarizes profiles (used throughout the specification)
     * </p> 
	 */
	public StringDt getShortElement() {  
		if (myShort == null) {
			myShort = new StringDt();
		}
		return myShort;
	}

	
	/**
	 * Gets the value(s) for <b>short</b> (ElementDefinition.short).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A concise definition that  is shown in the generated XML format that summarizes profiles (used throughout the specification)
     * </p> 
	 */
	public String getShort() {  
		return getShortElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>short</b> (ElementDefinition.short)
	 *
     * <p>
     * <b>Definition:</b>
     * A concise definition that  is shown in the generated XML format that summarizes profiles (used throughout the specification)
     * </p> 
	 */
	public ElementDefinitionDt setShort(StringDt theValue) {
		myShort = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>short</b> (ElementDefinition.short)
	 *
     * <p>
     * <b>Definition:</b>
     * A concise definition that  is shown in the generated XML format that summarizes profiles (used throughout the specification)
     * </p> 
	 */
	public ElementDefinitionDt setShort( String theString) {
		myShort = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>formal</b> (ElementDefinition.formal).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The definition SHALL be consistent with the base definition, but convey the meaning of the element in the particular context of use of the resource
     * </p> 
	 */
	public StringDt getFormalElement() {  
		if (myFormal == null) {
			myFormal = new StringDt();
		}
		return myFormal;
	}

	
	/**
	 * Gets the value(s) for <b>formal</b> (ElementDefinition.formal).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The definition SHALL be consistent with the base definition, but convey the meaning of the element in the particular context of use of the resource
     * </p> 
	 */
	public String getFormal() {  
		return getFormalElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>formal</b> (ElementDefinition.formal)
	 *
     * <p>
     * <b>Definition:</b>
     * The definition SHALL be consistent with the base definition, but convey the meaning of the element in the particular context of use of the resource
     * </p> 
	 */
	public ElementDefinitionDt setFormal(StringDt theValue) {
		myFormal = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>formal</b> (ElementDefinition.formal)
	 *
     * <p>
     * <b>Definition:</b>
     * The definition SHALL be consistent with the base definition, but convey the meaning of the element in the particular context of use of the resource
     * </p> 
	 */
	public ElementDefinitionDt setFormal( String theString) {
		myFormal = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>comments</b> (ElementDefinition.comments).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Comments about the use of the element, including notes about how to use the data properly, exceptions to proper use, etc.
     * </p> 
	 */
	public StringDt getCommentsElement() {  
		if (myComments == null) {
			myComments = new StringDt();
		}
		return myComments;
	}

	
	/**
	 * Gets the value(s) for <b>comments</b> (ElementDefinition.comments).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Comments about the use of the element, including notes about how to use the data properly, exceptions to proper use, etc.
     * </p> 
	 */
	public String getComments() {  
		return getCommentsElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>comments</b> (ElementDefinition.comments)
	 *
     * <p>
     * <b>Definition:</b>
     * Comments about the use of the element, including notes about how to use the data properly, exceptions to proper use, etc.
     * </p> 
	 */
	public ElementDefinitionDt setComments(StringDt theValue) {
		myComments = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>comments</b> (ElementDefinition.comments)
	 *
     * <p>
     * <b>Definition:</b>
     * Comments about the use of the element, including notes about how to use the data properly, exceptions to proper use, etc.
     * </p> 
	 */
	public ElementDefinitionDt setComments( String theString) {
		myComments = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>requirements</b> (ElementDefinition.requirements).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Explains why this element is needed and why it's been constrained as it has
     * </p> 
	 */
	public StringDt getRequirementsElement() {  
		if (myRequirements == null) {
			myRequirements = new StringDt();
		}
		return myRequirements;
	}

	
	/**
	 * Gets the value(s) for <b>requirements</b> (ElementDefinition.requirements).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Explains why this element is needed and why it's been constrained as it has
     * </p> 
	 */
	public String getRequirements() {  
		return getRequirementsElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>requirements</b> (ElementDefinition.requirements)
	 *
     * <p>
     * <b>Definition:</b>
     * Explains why this element is needed and why it's been constrained as it has
     * </p> 
	 */
	public ElementDefinitionDt setRequirements(StringDt theValue) {
		myRequirements = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>requirements</b> (ElementDefinition.requirements)
	 *
     * <p>
     * <b>Definition:</b>
     * Explains why this element is needed and why it's been constrained as it has
     * </p> 
	 */
	public ElementDefinitionDt setRequirements( String theString) {
		myRequirements = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>synonym</b> (ElementDefinition.synonym).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies additional names by which this element might also be known
     * </p> 
	 */
	public java.util.List<StringDt> getSynonym() {  
		if (mySynonym == null) {
			mySynonym = new java.util.ArrayList<StringDt>();
		}
		return mySynonym;
	}

	/**
	 * Sets the value(s) for <b>synonym</b> (ElementDefinition.synonym)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies additional names by which this element might also be known
     * </p> 
	 */
	public ElementDefinitionDt setSynonym(java.util.List<StringDt> theValue) {
		mySynonym = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>synonym</b> (ElementDefinition.synonym)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies additional names by which this element might also be known
     * </p> 
	 */
	public StringDt addSynonym() {
		StringDt newType = new StringDt();
		getSynonym().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>synonym</b> (ElementDefinition.synonym),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies additional names by which this element might also be known
     * </p> 
	 */
	public StringDt getSynonymFirstRep() {
		if (getSynonym().isEmpty()) {
			return addSynonym();
		}
		return getSynonym().get(0); 
	}
 	/**
	 * Adds a new value for <b>synonym</b> (ElementDefinition.synonym)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies additional names by which this element might also be known
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public ElementDefinitionDt addSynonym( String theString) {
		if (mySynonym == null) {
			mySynonym = new java.util.ArrayList<StringDt>();
		}
		mySynonym.add(new StringDt(theString));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>min</b> (ElementDefinition.min).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The minimum number of times this element SHALL appear in the instance
     * </p> 
	 */
	public IntegerDt getMinElement() {  
		if (myMin == null) {
			myMin = new IntegerDt();
		}
		return myMin;
	}

	
	/**
	 * Gets the value(s) for <b>min</b> (ElementDefinition.min).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The minimum number of times this element SHALL appear in the instance
     * </p> 
	 */
	public Integer getMin() {  
		return getMinElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>min</b> (ElementDefinition.min)
	 *
     * <p>
     * <b>Definition:</b>
     * The minimum number of times this element SHALL appear in the instance
     * </p> 
	 */
	public ElementDefinitionDt setMin(IntegerDt theValue) {
		myMin = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>min</b> (ElementDefinition.min)
	 *
     * <p>
     * <b>Definition:</b>
     * The minimum number of times this element SHALL appear in the instance
     * </p> 
	 */
	public ElementDefinitionDt setMin( int theInteger) {
		myMin = new IntegerDt(theInteger); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>max</b> (ElementDefinition.max).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The maximum number of times this element is permitted to appear in the instance
     * </p> 
	 */
	public StringDt getMaxElement() {  
		if (myMax == null) {
			myMax = new StringDt();
		}
		return myMax;
	}

	
	/**
	 * Gets the value(s) for <b>max</b> (ElementDefinition.max).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The maximum number of times this element is permitted to appear in the instance
     * </p> 
	 */
	public String getMax() {  
		return getMaxElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>max</b> (ElementDefinition.max)
	 *
     * <p>
     * <b>Definition:</b>
     * The maximum number of times this element is permitted to appear in the instance
     * </p> 
	 */
	public ElementDefinitionDt setMax(StringDt theValue) {
		myMax = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>max</b> (ElementDefinition.max)
	 *
     * <p>
     * <b>Definition:</b>
     * The maximum number of times this element is permitted to appear in the instance
     * </p> 
	 */
	public ElementDefinitionDt setMax( String theString) {
		myMax = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>type</b> (ElementDefinition.type).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The data type or resource that the value of this element is permitted to be
     * </p> 
	 */
	public java.util.List<Type> getType() {  
		if (myType == null) {
			myType = new java.util.ArrayList<Type>();
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (ElementDefinition.type)
	 *
     * <p>
     * <b>Definition:</b>
     * The data type or resource that the value of this element is permitted to be
     * </p> 
	 */
	public ElementDefinitionDt setType(java.util.List<Type> theValue) {
		myType = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>type</b> (ElementDefinition.type)
	 *
     * <p>
     * <b>Definition:</b>
     * The data type or resource that the value of this element is permitted to be
     * </p> 
	 */
	public Type addType() {
		Type newType = new Type();
		getType().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>type</b> (ElementDefinition.type),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * The data type or resource that the value of this element is permitted to be
     * </p> 
	 */
	public Type getTypeFirstRep() {
		if (getType().isEmpty()) {
			return addType();
		}
		return getType().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>nameReference</b> (ElementDefinition.nameReference).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the name of a slice defined elsewhere in the profile whose constraints should be applied to the current element
     * </p> 
	 */
	public StringDt getNameReferenceElement() {  
		if (myNameReference == null) {
			myNameReference = new StringDt();
		}
		return myNameReference;
	}

	
	/**
	 * Gets the value(s) for <b>nameReference</b> (ElementDefinition.nameReference).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the name of a slice defined elsewhere in the profile whose constraints should be applied to the current element
     * </p> 
	 */
	public String getNameReference() {  
		return getNameReferenceElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>nameReference</b> (ElementDefinition.nameReference)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the name of a slice defined elsewhere in the profile whose constraints should be applied to the current element
     * </p> 
	 */
	public ElementDefinitionDt setNameReference(StringDt theValue) {
		myNameReference = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>nameReference</b> (ElementDefinition.nameReference)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the name of a slice defined elsewhere in the profile whose constraints should be applied to the current element
     * </p> 
	 */
	public ElementDefinitionDt setNameReference( String theString) {
		myNameReference = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>defaultValue[x]</b> (ElementDefinition.defaultValue[x]).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The value that should be used if there is no value stated in the instance
     * </p> 
	 */
	public IDatatype getDefaultValue() {  
		return myDefaultValue;
	}

	/**
	 * Sets the value(s) for <b>defaultValue[x]</b> (ElementDefinition.defaultValue[x])
	 *
     * <p>
     * <b>Definition:</b>
     * The value that should be used if there is no value stated in the instance
     * </p> 
	 */
	public ElementDefinitionDt setDefaultValue(IDatatype theValue) {
		myDefaultValue = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>meaningWhenMissing</b> (ElementDefinition.meaningWhenMissing).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The Implicit meaning that is to be understood when this element is missing
     * </p> 
	 */
	public StringDt getMeaningWhenMissingElement() {  
		if (myMeaningWhenMissing == null) {
			myMeaningWhenMissing = new StringDt();
		}
		return myMeaningWhenMissing;
	}

	
	/**
	 * Gets the value(s) for <b>meaningWhenMissing</b> (ElementDefinition.meaningWhenMissing).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The Implicit meaning that is to be understood when this element is missing
     * </p> 
	 */
	public String getMeaningWhenMissing() {  
		return getMeaningWhenMissingElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>meaningWhenMissing</b> (ElementDefinition.meaningWhenMissing)
	 *
     * <p>
     * <b>Definition:</b>
     * The Implicit meaning that is to be understood when this element is missing
     * </p> 
	 */
	public ElementDefinitionDt setMeaningWhenMissing(StringDt theValue) {
		myMeaningWhenMissing = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>meaningWhenMissing</b> (ElementDefinition.meaningWhenMissing)
	 *
     * <p>
     * <b>Definition:</b>
     * The Implicit meaning that is to be understood when this element is missing
     * </p> 
	 */
	public ElementDefinitionDt setMeaningWhenMissing( String theString) {
		myMeaningWhenMissing = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>fixed[x]</b> (ElementDefinition.fixed[x]).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Specifies a value that SHALL be exactly the value  for this element in the instance. For purposes of comparison, non-signficant whitespace is ignored, and all values must be an exact match (case and accent sensitive). Missing elements/attributes must also be missing
     * </p> 
	 */
	public IDatatype getFixed() {  
		return myFixed;
	}

	/**
	 * Sets the value(s) for <b>fixed[x]</b> (ElementDefinition.fixed[x])
	 *
     * <p>
     * <b>Definition:</b>
     * Specifies a value that SHALL be exactly the value  for this element in the instance. For purposes of comparison, non-signficant whitespace is ignored, and all values must be an exact match (case and accent sensitive). Missing elements/attributes must also be missing
     * </p> 
	 */
	public ElementDefinitionDt setFixed(IDatatype theValue) {
		myFixed = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>pattern[x]</b> (ElementDefinition.pattern[x]).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Specifies a value that the value in the instance SHALL follow - that is, any value in the pattern must be found in the instance. Other additional values may be found too. This is effectively constraint by example.  The values of elements present in the pattern must match exactly (case-senstive, accent-sensitive, etc.)
     * </p> 
	 */
	public IDatatype getPattern() {  
		return myPattern;
	}

	/**
	 * Sets the value(s) for <b>pattern[x]</b> (ElementDefinition.pattern[x])
	 *
     * <p>
     * <b>Definition:</b>
     * Specifies a value that the value in the instance SHALL follow - that is, any value in the pattern must be found in the instance. Other additional values may be found too. This is effectively constraint by example.  The values of elements present in the pattern must match exactly (case-senstive, accent-sensitive, etc.)
     * </p> 
	 */
	public ElementDefinitionDt setPattern(IDatatype theValue) {
		myPattern = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>example[x]</b> (ElementDefinition.example[x]).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An example value for this element
     * </p> 
	 */
	public IDatatype getExample() {  
		return myExample;
	}

	/**
	 * Sets the value(s) for <b>example[x]</b> (ElementDefinition.example[x])
	 *
     * <p>
     * <b>Definition:</b>
     * An example value for this element
     * </p> 
	 */
	public ElementDefinitionDt setExample(IDatatype theValue) {
		myExample = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>maxLength</b> (ElementDefinition.maxLength).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the maximum length in characters that is permitted to be present in conformant instances and which is expected to be supported by conformant consumers that support the element
     * </p> 
	 */
	public IntegerDt getMaxLengthElement() {  
		if (myMaxLength == null) {
			myMaxLength = new IntegerDt();
		}
		return myMaxLength;
	}

	
	/**
	 * Gets the value(s) for <b>maxLength</b> (ElementDefinition.maxLength).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the maximum length in characters that is permitted to be present in conformant instances and which is expected to be supported by conformant consumers that support the element
     * </p> 
	 */
	public Integer getMaxLength() {  
		return getMaxLengthElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>maxLength</b> (ElementDefinition.maxLength)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the maximum length in characters that is permitted to be present in conformant instances and which is expected to be supported by conformant consumers that support the element
     * </p> 
	 */
	public ElementDefinitionDt setMaxLength(IntegerDt theValue) {
		myMaxLength = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>maxLength</b> (ElementDefinition.maxLength)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the maximum length in characters that is permitted to be present in conformant instances and which is expected to be supported by conformant consumers that support the element
     * </p> 
	 */
	public ElementDefinitionDt setMaxLength( int theInteger) {
		myMaxLength = new IntegerDt(theInteger); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>condition</b> (ElementDefinition.condition).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A reference to an invariant that may make additional statements about the cardinality or value in the instance
     * </p> 
	 */
	public java.util.List<IdDt> getCondition() {  
		if (myCondition == null) {
			myCondition = new java.util.ArrayList<IdDt>();
		}
		return myCondition;
	}

	/**
	 * Sets the value(s) for <b>condition</b> (ElementDefinition.condition)
	 *
     * <p>
     * <b>Definition:</b>
     * A reference to an invariant that may make additional statements about the cardinality or value in the instance
     * </p> 
	 */
	public ElementDefinitionDt setCondition(java.util.List<IdDt> theValue) {
		myCondition = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>condition</b> (ElementDefinition.condition)
	 *
     * <p>
     * <b>Definition:</b>
     * A reference to an invariant that may make additional statements about the cardinality or value in the instance
     * </p> 
	 */
	public IdDt addCondition() {
		IdDt newType = new IdDt();
		getCondition().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>condition</b> (ElementDefinition.condition),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * A reference to an invariant that may make additional statements about the cardinality or value in the instance
     * </p> 
	 */
	public IdDt getConditionFirstRep() {
		if (getCondition().isEmpty()) {
			return addCondition();
		}
		return getCondition().get(0); 
	}
 	/**
	 * Adds a new value for <b>condition</b> (ElementDefinition.condition)
	 *
     * <p>
     * <b>Definition:</b>
     * A reference to an invariant that may make additional statements about the cardinality or value in the instance
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public ElementDefinitionDt addCondition( String theId) {
		if (myCondition == null) {
			myCondition = new java.util.ArrayList<IdDt>();
		}
		myCondition.add(new IdDt(theId));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>constraint</b> (ElementDefinition.constraint).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Formal constraints such as co-occurrence and other constraints that can be computationally evaluated within the context of the instance
     * </p> 
	 */
	public java.util.List<Constraint> getConstraint() {  
		if (myConstraint == null) {
			myConstraint = new java.util.ArrayList<Constraint>();
		}
		return myConstraint;
	}

	/**
	 * Sets the value(s) for <b>constraint</b> (ElementDefinition.constraint)
	 *
     * <p>
     * <b>Definition:</b>
     * Formal constraints such as co-occurrence and other constraints that can be computationally evaluated within the context of the instance
     * </p> 
	 */
	public ElementDefinitionDt setConstraint(java.util.List<Constraint> theValue) {
		myConstraint = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>constraint</b> (ElementDefinition.constraint)
	 *
     * <p>
     * <b>Definition:</b>
     * Formal constraints such as co-occurrence and other constraints that can be computationally evaluated within the context of the instance
     * </p> 
	 */
	public Constraint addConstraint() {
		Constraint newType = new Constraint();
		getConstraint().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>constraint</b> (ElementDefinition.constraint),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Formal constraints such as co-occurrence and other constraints that can be computationally evaluated within the context of the instance
     * </p> 
	 */
	public Constraint getConstraintFirstRep() {
		if (getConstraint().isEmpty()) {
			return addConstraint();
		}
		return getConstraint().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>mustSupport</b> (ElementDefinition.mustSupport).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If true, conformant resource authors SHALL be capable of providing a value for the element and resource consumers SHALL be capable of extracting and doing something useful with the data element.  If false, the element may be ignored and not supported
     * </p> 
	 */
	public BooleanDt getMustSupportElement() {  
		if (myMustSupport == null) {
			myMustSupport = new BooleanDt();
		}
		return myMustSupport;
	}

	
	/**
	 * Gets the value(s) for <b>mustSupport</b> (ElementDefinition.mustSupport).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If true, conformant resource authors SHALL be capable of providing a value for the element and resource consumers SHALL be capable of extracting and doing something useful with the data element.  If false, the element may be ignored and not supported
     * </p> 
	 */
	public Boolean getMustSupport() {  
		return getMustSupportElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>mustSupport</b> (ElementDefinition.mustSupport)
	 *
     * <p>
     * <b>Definition:</b>
     * If true, conformant resource authors SHALL be capable of providing a value for the element and resource consumers SHALL be capable of extracting and doing something useful with the data element.  If false, the element may be ignored and not supported
     * </p> 
	 */
	public ElementDefinitionDt setMustSupport(BooleanDt theValue) {
		myMustSupport = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>mustSupport</b> (ElementDefinition.mustSupport)
	 *
     * <p>
     * <b>Definition:</b>
     * If true, conformant resource authors SHALL be capable of providing a value for the element and resource consumers SHALL be capable of extracting and doing something useful with the data element.  If false, the element may be ignored and not supported
     * </p> 
	 */
	public ElementDefinitionDt setMustSupport( boolean theBoolean) {
		myMustSupport = new BooleanDt(theBoolean); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>isModifier</b> (ElementDefinition.isModifier).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If true, the value of this element affects the interpretation of the element or resource that contains it, and the value of the element cannot be ignored. Typically, this is used for status, negation and qualification codes. The effect of this is that the element cannot be ignored by systems: they SHALL either recognize the element and process it, and/or a pre-determination has been made that it is not relevant to their particular system.
     * </p> 
	 */
	public BooleanDt getIsModifierElement() {  
		if (myIsModifier == null) {
			myIsModifier = new BooleanDt();
		}
		return myIsModifier;
	}

	
	/**
	 * Gets the value(s) for <b>isModifier</b> (ElementDefinition.isModifier).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If true, the value of this element affects the interpretation of the element or resource that contains it, and the value of the element cannot be ignored. Typically, this is used for status, negation and qualification codes. The effect of this is that the element cannot be ignored by systems: they SHALL either recognize the element and process it, and/or a pre-determination has been made that it is not relevant to their particular system.
     * </p> 
	 */
	public Boolean getIsModifier() {  
		return getIsModifierElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>isModifier</b> (ElementDefinition.isModifier)
	 *
     * <p>
     * <b>Definition:</b>
     * If true, the value of this element affects the interpretation of the element or resource that contains it, and the value of the element cannot be ignored. Typically, this is used for status, negation and qualification codes. The effect of this is that the element cannot be ignored by systems: they SHALL either recognize the element and process it, and/or a pre-determination has been made that it is not relevant to their particular system.
     * </p> 
	 */
	public ElementDefinitionDt setIsModifier(BooleanDt theValue) {
		myIsModifier = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>isModifier</b> (ElementDefinition.isModifier)
	 *
     * <p>
     * <b>Definition:</b>
     * If true, the value of this element affects the interpretation of the element or resource that contains it, and the value of the element cannot be ignored. Typically, this is used for status, negation and qualification codes. The effect of this is that the element cannot be ignored by systems: they SHALL either recognize the element and process it, and/or a pre-determination has been made that it is not relevant to their particular system.
     * </p> 
	 */
	public ElementDefinitionDt setIsModifier( boolean theBoolean) {
		myIsModifier = new BooleanDt(theBoolean); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>isSummary</b> (ElementDefinition.isSummary).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Whether the element should be included if a client requests a search with the parameter _summary=true
     * </p> 
	 */
	public BooleanDt getIsSummaryElement() {  
		if (myIsSummary == null) {
			myIsSummary = new BooleanDt();
		}
		return myIsSummary;
	}

	
	/**
	 * Gets the value(s) for <b>isSummary</b> (ElementDefinition.isSummary).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Whether the element should be included if a client requests a search with the parameter _summary=true
     * </p> 
	 */
	public Boolean getIsSummary() {  
		return getIsSummaryElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>isSummary</b> (ElementDefinition.isSummary)
	 *
     * <p>
     * <b>Definition:</b>
     * Whether the element should be included if a client requests a search with the parameter _summary=true
     * </p> 
	 */
	public ElementDefinitionDt setIsSummary(BooleanDt theValue) {
		myIsSummary = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>isSummary</b> (ElementDefinition.isSummary)
	 *
     * <p>
     * <b>Definition:</b>
     * Whether the element should be included if a client requests a search with the parameter _summary=true
     * </p> 
	 */
	public ElementDefinitionDt setIsSummary( boolean theBoolean) {
		myIsSummary = new BooleanDt(theBoolean); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>binding</b> (ElementDefinition.binding).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Binds to a value set if this element is coded (code, Coding, CodeableConcept)
     * </p> 
	 */
	public Binding getBinding() {  
		if (myBinding == null) {
			myBinding = new Binding();
		}
		return myBinding;
	}

	/**
	 * Sets the value(s) for <b>binding</b> (ElementDefinition.binding)
	 *
     * <p>
     * <b>Definition:</b>
     * Binds to a value set if this element is coded (code, Coding, CodeableConcept)
     * </p> 
	 */
	public ElementDefinitionDt setBinding(Binding theValue) {
		myBinding = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>mapping</b> (ElementDefinition.mapping).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a concept from an external specification that roughly corresponds to this element
     * </p> 
	 */
	public java.util.List<Mapping> getMapping() {  
		if (myMapping == null) {
			myMapping = new java.util.ArrayList<Mapping>();
		}
		return myMapping;
	}

	/**
	 * Sets the value(s) for <b>mapping</b> (ElementDefinition.mapping)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a concept from an external specification that roughly corresponds to this element
     * </p> 
	 */
	public ElementDefinitionDt setMapping(java.util.List<Mapping> theValue) {
		myMapping = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>mapping</b> (ElementDefinition.mapping)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a concept from an external specification that roughly corresponds to this element
     * </p> 
	 */
	public Mapping addMapping() {
		Mapping newType = new Mapping();
		getMapping().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>mapping</b> (ElementDefinition.mapping),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a concept from an external specification that roughly corresponds to this element
     * </p> 
	 */
	public Mapping getMappingFirstRep() {
		if (getMapping().isEmpty()) {
			return addMapping();
		}
		return getMapping().get(0); 
	}
  
	/**
	 * Block class for child element: <b>ElementDefinition.slicing</b> (ElementDefinition.slicing)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates that the element is sliced into a set of alternative definitions (there are multiple definitions on a single element in the base resource). The set of slices is any elements that come after this in the element sequence that have the same path, until a shorter path occurs (the shorter path terminates the set)
     * </p> 
	 */
	@Block()	
	public static class Slicing 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="discriminator", type=StringDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="ElementDefinition.slicing.discriminator",
		formalDefinition="Designates which child elements are used to discriminate between the slices when processing an instance. If one or more discriminators are provided, the value of the child elements in the instance data SHALL completely distinguish which slice the element in the resource matches based on the allowed values for those elements in each of the slices"
	)
	private java.util.List<StringDt> myDiscriminator;
	
	@Child(name="description", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="ElementDefinition.slicing.description",
		formalDefinition="A human-readable text description of how the slicing works. If there is no discriminator, this is required to be present to provide whatever information is possible about how the slices can be differentiated"
	)
	private StringDt myDescription;
	
	@Child(name="ordered", type=BooleanDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="ElementDefinition.slicing.ordered",
		formalDefinition="If the matching elements have to occur in the same order as defined in the profile"
	)
	private BooleanDt myOrdered;
	
	@Child(name="rules", type=CodeDt.class, order=3, min=1, max=1)	
	@Description(
		shortDefinition="ElementDefinition.slicing.rules",
		formalDefinition="Whether additional slices are allowed or not. When the slices are ordered, profile authors can also say that additional slices are only allowed at the end"
	)
	private BoundCodeDt<SlicingRulesEnum> myRules;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myDiscriminator,  myDescription,  myOrdered,  myRules);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myDiscriminator, myDescription, myOrdered, myRules);
	}

	/**
	 * Gets the value(s) for <b>discriminator</b> (ElementDefinition.slicing.discriminator).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Designates which child elements are used to discriminate between the slices when processing an instance. If one or more discriminators are provided, the value of the child elements in the instance data SHALL completely distinguish which slice the element in the resource matches based on the allowed values for those elements in each of the slices
     * </p> 
	 */
	public java.util.List<StringDt> getDiscriminator() {  
		if (myDiscriminator == null) {
			myDiscriminator = new java.util.ArrayList<StringDt>();
		}
		return myDiscriminator;
	}

	/**
	 * Sets the value(s) for <b>discriminator</b> (ElementDefinition.slicing.discriminator)
	 *
     * <p>
     * <b>Definition:</b>
     * Designates which child elements are used to discriminate between the slices when processing an instance. If one or more discriminators are provided, the value of the child elements in the instance data SHALL completely distinguish which slice the element in the resource matches based on the allowed values for those elements in each of the slices
     * </p> 
	 */
	public Slicing setDiscriminator(java.util.List<StringDt> theValue) {
		myDiscriminator = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>discriminator</b> (ElementDefinition.slicing.discriminator)
	 *
     * <p>
     * <b>Definition:</b>
     * Designates which child elements are used to discriminate between the slices when processing an instance. If one or more discriminators are provided, the value of the child elements in the instance data SHALL completely distinguish which slice the element in the resource matches based on the allowed values for those elements in each of the slices
     * </p> 
	 */
	public StringDt addDiscriminator() {
		StringDt newType = new StringDt();
		getDiscriminator().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>discriminator</b> (ElementDefinition.slicing.discriminator),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Designates which child elements are used to discriminate between the slices when processing an instance. If one or more discriminators are provided, the value of the child elements in the instance data SHALL completely distinguish which slice the element in the resource matches based on the allowed values for those elements in each of the slices
     * </p> 
	 */
	public StringDt getDiscriminatorFirstRep() {
		if (getDiscriminator().isEmpty()) {
			return addDiscriminator();
		}
		return getDiscriminator().get(0); 
	}
 	/**
	 * Adds a new value for <b>discriminator</b> (ElementDefinition.slicing.discriminator)
	 *
     * <p>
     * <b>Definition:</b>
     * Designates which child elements are used to discriminate between the slices when processing an instance. If one or more discriminators are provided, the value of the child elements in the instance data SHALL completely distinguish which slice the element in the resource matches based on the allowed values for those elements in each of the slices
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Slicing addDiscriminator( String theString) {
		if (myDiscriminator == null) {
			myDiscriminator = new java.util.ArrayList<StringDt>();
		}
		myDiscriminator.add(new StringDt(theString));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>description</b> (ElementDefinition.slicing.description).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A human-readable text description of how the slicing works. If there is no discriminator, this is required to be present to provide whatever information is possible about how the slices can be differentiated
     * </p> 
	 */
	public StringDt getDescriptionElement() {  
		if (myDescription == null) {
			myDescription = new StringDt();
		}
		return myDescription;
	}

	
	/**
	 * Gets the value(s) for <b>description</b> (ElementDefinition.slicing.description).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A human-readable text description of how the slicing works. If there is no discriminator, this is required to be present to provide whatever information is possible about how the slices can be differentiated
     * </p> 
	 */
	public String getDescription() {  
		return getDescriptionElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>description</b> (ElementDefinition.slicing.description)
	 *
     * <p>
     * <b>Definition:</b>
     * A human-readable text description of how the slicing works. If there is no discriminator, this is required to be present to provide whatever information is possible about how the slices can be differentiated
     * </p> 
	 */
	public Slicing setDescription(StringDt theValue) {
		myDescription = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>description</b> (ElementDefinition.slicing.description)
	 *
     * <p>
     * <b>Definition:</b>
     * A human-readable text description of how the slicing works. If there is no discriminator, this is required to be present to provide whatever information is possible about how the slices can be differentiated
     * </p> 
	 */
	public Slicing setDescription( String theString) {
		myDescription = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>ordered</b> (ElementDefinition.slicing.ordered).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If the matching elements have to occur in the same order as defined in the profile
     * </p> 
	 */
	public BooleanDt getOrderedElement() {  
		if (myOrdered == null) {
			myOrdered = new BooleanDt();
		}
		return myOrdered;
	}

	
	/**
	 * Gets the value(s) for <b>ordered</b> (ElementDefinition.slicing.ordered).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If the matching elements have to occur in the same order as defined in the profile
     * </p> 
	 */
	public Boolean getOrdered() {  
		return getOrderedElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>ordered</b> (ElementDefinition.slicing.ordered)
	 *
     * <p>
     * <b>Definition:</b>
     * If the matching elements have to occur in the same order as defined in the profile
     * </p> 
	 */
	public Slicing setOrdered(BooleanDt theValue) {
		myOrdered = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>ordered</b> (ElementDefinition.slicing.ordered)
	 *
     * <p>
     * <b>Definition:</b>
     * If the matching elements have to occur in the same order as defined in the profile
     * </p> 
	 */
	public Slicing setOrdered( boolean theBoolean) {
		myOrdered = new BooleanDt(theBoolean); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>rules</b> (ElementDefinition.slicing.rules).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Whether additional slices are allowed or not. When the slices are ordered, profile authors can also say that additional slices are only allowed at the end
     * </p> 
	 */
	public BoundCodeDt<SlicingRulesEnum> getRulesElement() {  
		if (myRules == null) {
			myRules = new BoundCodeDt<SlicingRulesEnum>(SlicingRulesEnum.VALUESET_BINDER);
		}
		return myRules;
	}

	
	/**
	 * Gets the value(s) for <b>rules</b> (ElementDefinition.slicing.rules).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Whether additional slices are allowed or not. When the slices are ordered, profile authors can also say that additional slices are only allowed at the end
     * </p> 
	 */
	public String getRules() {  
		return getRulesElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>rules</b> (ElementDefinition.slicing.rules)
	 *
     * <p>
     * <b>Definition:</b>
     * Whether additional slices are allowed or not. When the slices are ordered, profile authors can also say that additional slices are only allowed at the end
     * </p> 
	 */
	public Slicing setRules(BoundCodeDt<SlicingRulesEnum> theValue) {
		myRules = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>rules</b> (ElementDefinition.slicing.rules)
	 *
     * <p>
     * <b>Definition:</b>
     * Whether additional slices are allowed or not. When the slices are ordered, profile authors can also say that additional slices are only allowed at the end
     * </p> 
	 */
	public Slicing setRules(SlicingRulesEnum theValue) {
		getRulesElement().setValueAsEnum(theValue);
		return this;
	}

  

	}


	/**
	 * Block class for child element: <b>ElementDefinition.type</b> (ElementDefinition.type)
	 *
     * <p>
     * <b>Definition:</b>
     * The data type or resource that the value of this element is permitted to be
     * </p> 
	 */
	@Block()	
	public static class Type 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="code", type=CodeDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="ElementDefinition.type.code",
		formalDefinition="Name of Data type or Resource that is a(or the) type used for this element"
	)
	private BoundCodeDt<DataTypeEnum> myCode;
	
	@Child(name="profile", type=UriDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="ElementDefinition.type.profile",
		formalDefinition="Identifies a profile structure that SHALL hold for resources or datatypes referenced as the type of this element. Can be a local reference - to another structure in this profile, or a reference to a structure in another profile"
	)
	private UriDt myProfile;
	
	@Child(name="aggregation", type=CodeDt.class, order=2, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="ElementDefinition.type.aggregation",
		formalDefinition="If the type is a reference to another resource, how the resource is or can be aggreated - is it a contained resource, or a reference, and if the context is a bundle, is it included in the bundle"
	)
	private java.util.List<BoundCodeDt<AggregationModeEnum>> myAggregation;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myCode,  myProfile,  myAggregation);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myCode, myProfile, myAggregation);
	}

	/**
	 * Gets the value(s) for <b>code</b> (ElementDefinition.type.code).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Name of Data type or Resource that is a(or the) type used for this element
     * </p> 
	 */
	public BoundCodeDt<DataTypeEnum> getCodeElement() {  
		if (myCode == null) {
			myCode = new BoundCodeDt<DataTypeEnum>(DataTypeEnum.VALUESET_BINDER);
		}
		return myCode;
	}

	
	/**
	 * Gets the value(s) for <b>code</b> (ElementDefinition.type.code).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Name of Data type or Resource that is a(or the) type used for this element
     * </p> 
	 */
	public String getCode() {  
		return getCodeElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>code</b> (ElementDefinition.type.code)
	 *
     * <p>
     * <b>Definition:</b>
     * Name of Data type or Resource that is a(or the) type used for this element
     * </p> 
	 */
	public Type setCode(BoundCodeDt<DataTypeEnum> theValue) {
		myCode = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>code</b> (ElementDefinition.type.code)
	 *
     * <p>
     * <b>Definition:</b>
     * Name of Data type or Resource that is a(or the) type used for this element
     * </p> 
	 */
	public Type setCode(DataTypeEnum theValue) {
		getCodeElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>profile</b> (ElementDefinition.type.profile).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a profile structure that SHALL hold for resources or datatypes referenced as the type of this element. Can be a local reference - to another structure in this profile, or a reference to a structure in another profile
     * </p> 
	 */
	public UriDt getProfileElement() {  
		if (myProfile == null) {
			myProfile = new UriDt();
		}
		return myProfile;
	}

	
	/**
	 * Gets the value(s) for <b>profile</b> (ElementDefinition.type.profile).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a profile structure that SHALL hold for resources or datatypes referenced as the type of this element. Can be a local reference - to another structure in this profile, or a reference to a structure in another profile
     * </p> 
	 */
	public String getProfile() {  
		return getProfileElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>profile</b> (ElementDefinition.type.profile)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a profile structure that SHALL hold for resources or datatypes referenced as the type of this element. Can be a local reference - to another structure in this profile, or a reference to a structure in another profile
     * </p> 
	 */
	public Type setProfile(UriDt theValue) {
		myProfile = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>profile</b> (ElementDefinition.type.profile)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a profile structure that SHALL hold for resources or datatypes referenced as the type of this element. Can be a local reference - to another structure in this profile, or a reference to a structure in another profile
     * </p> 
	 */
	public Type setProfile( String theUri) {
		myProfile = new UriDt(theUri); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>aggregation</b> (ElementDefinition.type.aggregation).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If the type is a reference to another resource, how the resource is or can be aggreated - is it a contained resource, or a reference, and if the context is a bundle, is it included in the bundle
     * </p> 
	 */
	public java.util.List<BoundCodeDt<AggregationModeEnum>> getAggregation() {  
		if (myAggregation == null) {
			myAggregation = new java.util.ArrayList<BoundCodeDt<AggregationModeEnum>>();
		}
		return myAggregation;
	}

	/**
	 * Sets the value(s) for <b>aggregation</b> (ElementDefinition.type.aggregation)
	 *
     * <p>
     * <b>Definition:</b>
     * If the type is a reference to another resource, how the resource is or can be aggreated - is it a contained resource, or a reference, and if the context is a bundle, is it included in the bundle
     * </p> 
	 */
	public Type setAggregation(java.util.List<BoundCodeDt<AggregationModeEnum>> theValue) {
		myAggregation = theValue;
		return this;
	}
	
	

	/**
	 * Add a value for <b>aggregation</b> (ElementDefinition.type.aggregation) using an enumerated type. This
	 * is intended as a convenience method for situations where the FHIR defined ValueSets are mandatory
	 * or contain the desirable codes. If you wish to use codes other than those which are built-in, 
	 * you may also use the {@link #addAggregation()} method.
	 *
     * <p>
     * <b>Definition:</b>
     * If the type is a reference to another resource, how the resource is or can be aggreated - is it a contained resource, or a reference, and if the context is a bundle, is it included in the bundle
     * </p> 
	 */
	public BoundCodeDt<AggregationModeEnum> addAggregation(AggregationModeEnum theValue) {
		BoundCodeDt<AggregationModeEnum> retVal = new BoundCodeDt<AggregationModeEnum>(AggregationModeEnum.VALUESET_BINDER, theValue);
		getAggregation().add(retVal);
		return retVal;
	}

	/**
	 * Gets the first repetition for <b>aggregation</b> (ElementDefinition.type.aggregation),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * If the type is a reference to another resource, how the resource is or can be aggreated - is it a contained resource, or a reference, and if the context is a bundle, is it included in the bundle
     * </p> 
	 */
	public BoundCodeDt<AggregationModeEnum> getAggregationFirstRep() {
		if (getAggregation().size() == 0) {
			addAggregation();
		}
		return getAggregation().get(0);
	}

	/**
	 * Add a value for <b>aggregation</b> (ElementDefinition.type.aggregation)
	 *
     * <p>
     * <b>Definition:</b>
     * If the type is a reference to another resource, how the resource is or can be aggreated - is it a contained resource, or a reference, and if the context is a bundle, is it included in the bundle
     * </p> 
	 */
	public BoundCodeDt<AggregationModeEnum> addAggregation() {
		BoundCodeDt<AggregationModeEnum> retVal = new BoundCodeDt<AggregationModeEnum>(AggregationModeEnum.VALUESET_BINDER);
		getAggregation().add(retVal);
		return retVal;
	}

	/**
	 * Sets the value(s), and clears any existing value(s) for <b>aggregation</b> (ElementDefinition.type.aggregation)
	 *
     * <p>
     * <b>Definition:</b>
     * If the type is a reference to another resource, how the resource is or can be aggreated - is it a contained resource, or a reference, and if the context is a bundle, is it included in the bundle
     * </p> 
	 */
	public Type setAggregation(AggregationModeEnum theValue) {
		getAggregation().clear();
		addAggregation(theValue);
		return this;
	}

  

	}


	/**
	 * Block class for child element: <b>ElementDefinition.constraint</b> (ElementDefinition.constraint)
	 *
     * <p>
     * <b>Definition:</b>
     * Formal constraints such as co-occurrence and other constraints that can be computationally evaluated within the context of the instance
     * </p> 
	 */
	@Block()	
	public static class Constraint 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="key", type=IdDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="ElementDefinition.constraint.key",
		formalDefinition="Allows identification of which elements have their cardinalities impacted by the constraint.  Will not be referenced for constraints that do not affect cardinality"
	)
	private IdDt myKey;
	
	@Child(name="name", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="ElementDefinition.constraint.name",
		formalDefinition="Used to label the constraint in OCL or in short displays incapable of displaying the full human description"
	)
	private StringDt myName;
	
	@Child(name="severity", type=CodeDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="ElementDefinition.constraint.severity",
		formalDefinition="Identifies the impact constraint violation has on the conformance of the instance"
	)
	private BoundCodeDt<ConstraintSeverityEnum> mySeverity;
	
	@Child(name="human", type=StringDt.class, order=3, min=1, max=1)	
	@Description(
		shortDefinition="ElementDefinition.constraint.human",
		formalDefinition="Text that can be used to describe the constraint in messages identifying that the constraint has been violated"
	)
	private StringDt myHuman;
	
	@Child(name="xpath", type=StringDt.class, order=4, min=1, max=1)	
	@Description(
		shortDefinition="ElementDefinition.constraint.xpath",
		formalDefinition="An XPath expression of constraint that can be executed to see if this constraint is met"
	)
	private StringDt myXpath;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myKey,  myName,  mySeverity,  myHuman,  myXpath);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myKey, myName, mySeverity, myHuman, myXpath);
	}

	/**
	 * Gets the value(s) for <b>key</b> (ElementDefinition.constraint.key).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Allows identification of which elements have their cardinalities impacted by the constraint.  Will not be referenced for constraints that do not affect cardinality
     * </p> 
	 */
	public IdDt getKeyElement() {  
		if (myKey == null) {
			myKey = new IdDt();
		}
		return myKey;
	}

	
	/**
	 * Gets the value(s) for <b>key</b> (ElementDefinition.constraint.key).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Allows identification of which elements have their cardinalities impacted by the constraint.  Will not be referenced for constraints that do not affect cardinality
     * </p> 
	 */
	public String getKey() {  
		return getKeyElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>key</b> (ElementDefinition.constraint.key)
	 *
     * <p>
     * <b>Definition:</b>
     * Allows identification of which elements have their cardinalities impacted by the constraint.  Will not be referenced for constraints that do not affect cardinality
     * </p> 
	 */
	public Constraint setKey(IdDt theValue) {
		myKey = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>key</b> (ElementDefinition.constraint.key)
	 *
     * <p>
     * <b>Definition:</b>
     * Allows identification of which elements have their cardinalities impacted by the constraint.  Will not be referenced for constraints that do not affect cardinality
     * </p> 
	 */
	public Constraint setKey( String theId) {
		myKey = new IdDt(theId); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>name</b> (ElementDefinition.constraint.name).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Used to label the constraint in OCL or in short displays incapable of displaying the full human description
     * </p> 
	 */
	public StringDt getNameElement() {  
		if (myName == null) {
			myName = new StringDt();
		}
		return myName;
	}

	
	/**
	 * Gets the value(s) for <b>name</b> (ElementDefinition.constraint.name).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Used to label the constraint in OCL or in short displays incapable of displaying the full human description
     * </p> 
	 */
	public String getName() {  
		return getNameElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>name</b> (ElementDefinition.constraint.name)
	 *
     * <p>
     * <b>Definition:</b>
     * Used to label the constraint in OCL or in short displays incapable of displaying the full human description
     * </p> 
	 */
	public Constraint setName(StringDt theValue) {
		myName = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>name</b> (ElementDefinition.constraint.name)
	 *
     * <p>
     * <b>Definition:</b>
     * Used to label the constraint in OCL or in short displays incapable of displaying the full human description
     * </p> 
	 */
	public Constraint setName( String theString) {
		myName = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>severity</b> (ElementDefinition.constraint.severity).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the impact constraint violation has on the conformance of the instance
     * </p> 
	 */
	public BoundCodeDt<ConstraintSeverityEnum> getSeverityElement() {  
		if (mySeverity == null) {
			mySeverity = new BoundCodeDt<ConstraintSeverityEnum>(ConstraintSeverityEnum.VALUESET_BINDER);
		}
		return mySeverity;
	}

	
	/**
	 * Gets the value(s) for <b>severity</b> (ElementDefinition.constraint.severity).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the impact constraint violation has on the conformance of the instance
     * </p> 
	 */
	public String getSeverity() {  
		return getSeverityElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>severity</b> (ElementDefinition.constraint.severity)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the impact constraint violation has on the conformance of the instance
     * </p> 
	 */
	public Constraint setSeverity(BoundCodeDt<ConstraintSeverityEnum> theValue) {
		mySeverity = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>severity</b> (ElementDefinition.constraint.severity)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the impact constraint violation has on the conformance of the instance
     * </p> 
	 */
	public Constraint setSeverity(ConstraintSeverityEnum theValue) {
		getSeverityElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>human</b> (ElementDefinition.constraint.human).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Text that can be used to describe the constraint in messages identifying that the constraint has been violated
     * </p> 
	 */
	public StringDt getHumanElement() {  
		if (myHuman == null) {
			myHuman = new StringDt();
		}
		return myHuman;
	}

	
	/**
	 * Gets the value(s) for <b>human</b> (ElementDefinition.constraint.human).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Text that can be used to describe the constraint in messages identifying that the constraint has been violated
     * </p> 
	 */
	public String getHuman() {  
		return getHumanElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>human</b> (ElementDefinition.constraint.human)
	 *
     * <p>
     * <b>Definition:</b>
     * Text that can be used to describe the constraint in messages identifying that the constraint has been violated
     * </p> 
	 */
	public Constraint setHuman(StringDt theValue) {
		myHuman = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>human</b> (ElementDefinition.constraint.human)
	 *
     * <p>
     * <b>Definition:</b>
     * Text that can be used to describe the constraint in messages identifying that the constraint has been violated
     * </p> 
	 */
	public Constraint setHuman( String theString) {
		myHuman = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>xpath</b> (ElementDefinition.constraint.xpath).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An XPath expression of constraint that can be executed to see if this constraint is met
     * </p> 
	 */
	public StringDt getXpathElement() {  
		if (myXpath == null) {
			myXpath = new StringDt();
		}
		return myXpath;
	}

	
	/**
	 * Gets the value(s) for <b>xpath</b> (ElementDefinition.constraint.xpath).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An XPath expression of constraint that can be executed to see if this constraint is met
     * </p> 
	 */
	public String getXpath() {  
		return getXpathElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>xpath</b> (ElementDefinition.constraint.xpath)
	 *
     * <p>
     * <b>Definition:</b>
     * An XPath expression of constraint that can be executed to see if this constraint is met
     * </p> 
	 */
	public Constraint setXpath(StringDt theValue) {
		myXpath = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>xpath</b> (ElementDefinition.constraint.xpath)
	 *
     * <p>
     * <b>Definition:</b>
     * An XPath expression of constraint that can be executed to see if this constraint is met
     * </p> 
	 */
	public Constraint setXpath( String theString) {
		myXpath = new StringDt(theString); 
		return this; 
	}

 

	}


	/**
	 * Block class for child element: <b>ElementDefinition.binding</b> (ElementDefinition.binding)
	 *
     * <p>
     * <b>Definition:</b>
     * Binds to a value set if this element is coded (code, Coding, CodeableConcept)
     * </p> 
	 */
	@Block()	
	public static class Binding 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="name", type=StringDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="ElementDefinition.binding.name",
		formalDefinition="A descriptive name for this - can be useful for generating implementation artifacts"
	)
	private StringDt myName;
	
	@Child(name="isExtensible", type=BooleanDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="ElementDefinition.binding.isExtensible",
		formalDefinition="If true, then conformant systems may use additional codes or (where the data type permits) text alone to convey concepts not covered by the set of codes identified in the binding.  If false, then conformant systems are constrained to the provided codes alone"
	)
	private BooleanDt myIsExtensible;
	
	@Child(name="conformance", type=CodeDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="ElementDefinition.binding.conformance",
		formalDefinition="Indicates the degree of conformance expectations associated with this binding"
	)
	private BoundCodeDt<BindingConformanceEnum> myConformance;
	
	@Child(name="description", type=StringDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="ElementDefinition.binding.description",
		formalDefinition="Describes the intended use of this particular set of codes"
	)
	private StringDt myDescription;
	
	@Child(name="reference", order=4, min=0, max=1, type={
		UriDt.class, 		ValueSet.class	})
	@Description(
		shortDefinition="ElementDefinition.binding.reference[x]",
		formalDefinition="Points to the value set or external definition that identifies the set of codes to be used"
	)
	private IDatatype myReference;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myName,  myIsExtensible,  myConformance,  myDescription,  myReference);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myName, myIsExtensible, myConformance, myDescription, myReference);
	}

	/**
	 * Gets the value(s) for <b>name</b> (ElementDefinition.binding.name).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A descriptive name for this - can be useful for generating implementation artifacts
     * </p> 
	 */
	public StringDt getNameElement() {  
		if (myName == null) {
			myName = new StringDt();
		}
		return myName;
	}

	
	/**
	 * Gets the value(s) for <b>name</b> (ElementDefinition.binding.name).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A descriptive name for this - can be useful for generating implementation artifacts
     * </p> 
	 */
	public String getName() {  
		return getNameElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>name</b> (ElementDefinition.binding.name)
	 *
     * <p>
     * <b>Definition:</b>
     * A descriptive name for this - can be useful for generating implementation artifacts
     * </p> 
	 */
	public Binding setName(StringDt theValue) {
		myName = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>name</b> (ElementDefinition.binding.name)
	 *
     * <p>
     * <b>Definition:</b>
     * A descriptive name for this - can be useful for generating implementation artifacts
     * </p> 
	 */
	public Binding setName( String theString) {
		myName = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>isExtensible</b> (ElementDefinition.binding.isExtensible).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If true, then conformant systems may use additional codes or (where the data type permits) text alone to convey concepts not covered by the set of codes identified in the binding.  If false, then conformant systems are constrained to the provided codes alone
     * </p> 
	 */
	public BooleanDt getIsExtensibleElement() {  
		if (myIsExtensible == null) {
			myIsExtensible = new BooleanDt();
		}
		return myIsExtensible;
	}

	
	/**
	 * Gets the value(s) for <b>isExtensible</b> (ElementDefinition.binding.isExtensible).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If true, then conformant systems may use additional codes or (where the data type permits) text alone to convey concepts not covered by the set of codes identified in the binding.  If false, then conformant systems are constrained to the provided codes alone
     * </p> 
	 */
	public Boolean getIsExtensible() {  
		return getIsExtensibleElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>isExtensible</b> (ElementDefinition.binding.isExtensible)
	 *
     * <p>
     * <b>Definition:</b>
     * If true, then conformant systems may use additional codes or (where the data type permits) text alone to convey concepts not covered by the set of codes identified in the binding.  If false, then conformant systems are constrained to the provided codes alone
     * </p> 
	 */
	public Binding setIsExtensible(BooleanDt theValue) {
		myIsExtensible = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>isExtensible</b> (ElementDefinition.binding.isExtensible)
	 *
     * <p>
     * <b>Definition:</b>
     * If true, then conformant systems may use additional codes or (where the data type permits) text alone to convey concepts not covered by the set of codes identified in the binding.  If false, then conformant systems are constrained to the provided codes alone
     * </p> 
	 */
	public Binding setIsExtensible( boolean theBoolean) {
		myIsExtensible = new BooleanDt(theBoolean); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>conformance</b> (ElementDefinition.binding.conformance).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the degree of conformance expectations associated with this binding
     * </p> 
	 */
	public BoundCodeDt<BindingConformanceEnum> getConformanceElement() {  
		if (myConformance == null) {
			myConformance = new BoundCodeDt<BindingConformanceEnum>(BindingConformanceEnum.VALUESET_BINDER);
		}
		return myConformance;
	}

	
	/**
	 * Gets the value(s) for <b>conformance</b> (ElementDefinition.binding.conformance).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the degree of conformance expectations associated with this binding
     * </p> 
	 */
	public String getConformance() {  
		return getConformanceElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>conformance</b> (ElementDefinition.binding.conformance)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the degree of conformance expectations associated with this binding
     * </p> 
	 */
	public Binding setConformance(BoundCodeDt<BindingConformanceEnum> theValue) {
		myConformance = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>conformance</b> (ElementDefinition.binding.conformance)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the degree of conformance expectations associated with this binding
     * </p> 
	 */
	public Binding setConformance(BindingConformanceEnum theValue) {
		getConformanceElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>description</b> (ElementDefinition.binding.description).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Describes the intended use of this particular set of codes
     * </p> 
	 */
	public StringDt getDescriptionElement() {  
		if (myDescription == null) {
			myDescription = new StringDt();
		}
		return myDescription;
	}

	
	/**
	 * Gets the value(s) for <b>description</b> (ElementDefinition.binding.description).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Describes the intended use of this particular set of codes
     * </p> 
	 */
	public String getDescription() {  
		return getDescriptionElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>description</b> (ElementDefinition.binding.description)
	 *
     * <p>
     * <b>Definition:</b>
     * Describes the intended use of this particular set of codes
     * </p> 
	 */
	public Binding setDescription(StringDt theValue) {
		myDescription = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>description</b> (ElementDefinition.binding.description)
	 *
     * <p>
     * <b>Definition:</b>
     * Describes the intended use of this particular set of codes
     * </p> 
	 */
	public Binding setDescription( String theString) {
		myDescription = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>reference[x]</b> (ElementDefinition.binding.reference[x]).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Points to the value set or external definition that identifies the set of codes to be used
     * </p> 
	 */
	public IDatatype getReference() {  
		return myReference;
	}

	/**
	 * Sets the value(s) for <b>reference[x]</b> (ElementDefinition.binding.reference[x])
	 *
     * <p>
     * <b>Definition:</b>
     * Points to the value set or external definition that identifies the set of codes to be used
     * </p> 
	 */
	public Binding setReference(IDatatype theValue) {
		myReference = theValue;
		return this;
	}
	
	

  

	}


	/**
	 * Block class for child element: <b>ElementDefinition.mapping</b> (ElementDefinition.mapping)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a concept from an external specification that roughly corresponds to this element
     * </p> 
	 */
	@Block()	
	public static class Mapping 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="identity", type=IdDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="ElementDefinition.mapping.identity",
		formalDefinition="An internal reference to the definition of a mapping"
	)
	private IdDt myIdentity;
	
	@Child(name="map", type=StringDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="ElementDefinition.mapping.map",
		formalDefinition="Expresses what part of the target specification corresponds to this element"
	)
	private StringDt myMap;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentity,  myMap);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentity, myMap);
	}

	/**
	 * Gets the value(s) for <b>identity</b> (ElementDefinition.mapping.identity).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An internal reference to the definition of a mapping
     * </p> 
	 */
	public IdDt getIdentityElement() {  
		if (myIdentity == null) {
			myIdentity = new IdDt();
		}
		return myIdentity;
	}

	
	/**
	 * Gets the value(s) for <b>identity</b> (ElementDefinition.mapping.identity).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An internal reference to the definition of a mapping
     * </p> 
	 */
	public String getIdentity() {  
		return getIdentityElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>identity</b> (ElementDefinition.mapping.identity)
	 *
     * <p>
     * <b>Definition:</b>
     * An internal reference to the definition of a mapping
     * </p> 
	 */
	public Mapping setIdentity(IdDt theValue) {
		myIdentity = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>identity</b> (ElementDefinition.mapping.identity)
	 *
     * <p>
     * <b>Definition:</b>
     * An internal reference to the definition of a mapping
     * </p> 
	 */
	public Mapping setIdentity( String theId) {
		myIdentity = new IdDt(theId); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>map</b> (ElementDefinition.mapping.map).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Expresses what part of the target specification corresponds to this element
     * </p> 
	 */
	public StringDt getMapElement() {  
		if (myMap == null) {
			myMap = new StringDt();
		}
		return myMap;
	}

	
	/**
	 * Gets the value(s) for <b>map</b> (ElementDefinition.mapping.map).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Expresses what part of the target specification corresponds to this element
     * </p> 
	 */
	public String getMap() {  
		return getMapElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>map</b> (ElementDefinition.mapping.map)
	 *
     * <p>
     * <b>Definition:</b>
     * Expresses what part of the target specification corresponds to this element
     * </p> 
	 */
	public Mapping setMap(StringDt theValue) {
		myMap = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>map</b> (ElementDefinition.mapping.map)
	 *
     * <p>
     * <b>Definition:</b>
     * Expresses what part of the target specification corresponds to this element
     * </p> 
	 */
	public Mapping setMap( String theString) {
		myMap = new StringDt(theString); 
		return this; 
	}

 

	}




}