















package ca.uhn.fhir.model.dstu2.composite;

import ca.uhn.fhir.i18n.Msg;
import java.util.List;

import ca.uhn.fhir.model.api.BaseIdentifiableElement;
import ca.uhn.fhir.model.api.ICompositeDatatype;
import ca.uhn.fhir.model.api.IDatatype;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IResourceBlock;
import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.dstu2.resource.ValueSet;
import ca.uhn.fhir.model.dstu2.valueset.AggregationModeEnum;
import ca.uhn.fhir.model.dstu2.valueset.BindingStrengthEnum;
import ca.uhn.fhir.model.dstu2.valueset.ConstraintSeverityEnum;
import ca.uhn.fhir.model.dstu2.valueset.PropertyRepresentationEnum;
import ca.uhn.fhir.model.dstu2.valueset.SlicingRulesEnum;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.model.primitive.MarkdownDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.UriDt;

/**
 * HAPI/FHIR <b>ElementDefinitionDt</b> Datatype
 * ()
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
        extends  BaseIdentifiableElement         implements ICompositeDatatype{

	/**
	 * Constructor
	 */
	public ElementDefinitionDt() {
		// nothing
	}


	@Child(name="path", type=StringDt.class, order=0, min=1, max=1, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="The path identifies the element and is expressed as a \".\"-separated list of ancestor elements, beginning with the name of the resource or extension"
	)
	private StringDt myPath;
	
	@Child(name="representation", type=CodeDt.class, order=1, min=0, max=Child.MAX_UNLIMITED, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="Codes that define how this element is represented in instances, when the deviation varies from the normal case"
	)
	private java.util.List<BoundCodeDt<PropertyRepresentationEnum>> myRepresentation;
	
	@Child(name="name", type=StringDt.class, order=2, min=0, max=1, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="The name of this element definition (to refer to it from other element definitions using ElementDefinition.nameReference). This is a unique name referring to a specific set of constraints applied to this element. One use of this is to provide a name to different slices of the same element"
	)
	private StringDt myName;
	
	@Child(name="label", type=StringDt.class, order=3, min=0, max=1, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="The text to display beside the element indicating its meaning or to use to prompt for the element in a user display or form."
	)
	private StringDt myLabel;
	
	@Child(name="code", type=CodingDt.class, order=4, min=0, max=Child.MAX_UNLIMITED, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="A code that provides the meaning for the element according to a particular terminology."
	)
	private java.util.List<CodingDt> myCode;
	
	@Child(name="slicing", order=5, min=0, max=1, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="Indicates that the element is sliced into a set of alternative definitions (i.e. in a structure definition, there are multiple different constraints on a single element in the base resource). Slicing can be used in any resource that has cardinality ..* on the base resource, or any resource with a choice of types. The set of slices is any elements that come after this in the element sequence that have the same path, until a shorter path occurs (the shorter path terminates the set)"
	)
	private Slicing mySlicing;
	
	@Child(name="short", type=StringDt.class, order=6, min=0, max=1, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="A concise description of what this element means (e.g. for use in auto-generated summaries)"
	)
	private StringDt myShort;
	
	@Child(name="definition", type=MarkdownDt.class, order=7, min=0, max=1, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="Provides a complete explanation of the meaning of the data element for human readability.  For the case of elements derived from existing elements (e.g. constraints), the definition SHALL be consistent with the base definition, but convey the meaning of the element in the particular context of use of the resource"
	)
	private MarkdownDt myDefinition;
	
	@Child(name="comments", type=MarkdownDt.class, order=8, min=0, max=1, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="Explanatory notes and implementation guidance about the data element, including notes about how to use the data properly, exceptions to proper use, etc."
	)
	private MarkdownDt myComments;
	
	@Child(name="requirements", type=MarkdownDt.class, order=9, min=0, max=1, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="This element is for traceability of why the element was created and why the constraints exist as they do. This may be used to point to source materials or specifications that drove the structure of this element."
	)
	private MarkdownDt myRequirements;
	
	@Child(name="alias", type=StringDt.class, order=10, min=0, max=Child.MAX_UNLIMITED, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="Identifies additional names by which this element might also be known"
	)
	private java.util.List<StringDt> myAlias;
	
	@Child(name="min", type=IntegerDt.class, order=11, min=0, max=1, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="The minimum number of times this element SHALL appear in the instance"
	)
	private IntegerDt myMin;
	
	@Child(name="max", type=StringDt.class, order=12, min=0, max=1, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="The maximum number of times this element is permitted to appear in the instance"
	)
	private StringDt myMax;
	
	@Child(name="base", order=13, min=0, max=1, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="Information about the base definition of the element, provided to make it unncessary for tools to trace the derviation of the element through the derived and related profiles. This information is only provided where the element definition represents a constraint on another element definition, and must be present if there is a base element definition"
	)
	private Base myBase;
	
	@Child(name="type", order=14, min=0, max=Child.MAX_UNLIMITED, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="The data type or resource that the value of this element is permitted to be"
	)
	private java.util.List<Type> myType;
	
	@Child(name="nameReference", type=StringDt.class, order=15, min=0, max=1, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="Identifies the name of a slice defined elsewhere in the profile whose constraints should be applied to the current element"
	)
	private StringDt myNameReference;
	
	@Child(name="defaultValue", type=IDatatype.class, order=16, min=0, max=1, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="The value that should be used if there is no value stated in the instance (e.g. 'if not otherwise specified, the abstract is false')"
	)
	private IDatatype myDefaultValue;
	
	@Child(name="meaningWhenMissing", type=MarkdownDt.class, order=17, min=0, max=1, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="The Implicit meaning that is to be understood when this element is missing (e.g. 'when this element is missing, the period is ongoing'"
	)
	private MarkdownDt myMeaningWhenMissing;
	
	@Child(name="fixed", type=IDatatype.class, order=18, min=0, max=1, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="Specifies a value that SHALL be exactly the value  for this element in the instance. For purposes of comparison, non-signficant whitespace is ignored, and all values must be an exact match (case and accent sensitive). Missing elements/attributes must also be missing"
	)
	private IDatatype myFixed;
	
	@Child(name="pattern", type=IDatatype.class, order=19, min=0, max=1, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="Specifies a value that the value in the instance SHALL follow - that is, any value in the pattern must be found in the instance. Other additional values may be found too. This is effectively constraint by example.  The values of elements present in the pattern must match exactly (case-senstive, accent-sensitive, etc.)"
	)
	private IDatatype myPattern;
	
	@Child(name="example", type=IDatatype.class, order=20, min=0, max=1, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="A sample value for this element demonstrating the type of information that would typically be captured."
	)
	private IDatatype myExample;
	
	@Child(name="minValue", type=IDatatype.class, order=21, min=0, max=1, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="The minimum allowed value for the element. The value is inclusive. This is allowed for the types date, dateTime, instant, time, decimal, integer, and Quantity"
	)
	private IDatatype myMinValue;
	
	@Child(name="maxValue", type=IDatatype.class, order=22, min=0, max=1, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="The maximum allowed value for the element. The value is inclusive. This is allowed for the types date, dateTime, instant, time, decimal, integer, and Quantity"
	)
	private IDatatype myMaxValue;
	
	@Child(name="maxLength", type=IntegerDt.class, order=23, min=0, max=1, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="Indicates the maximum length in characters that is permitted to be present in conformant instances and which is expected to be supported by conformant consumers that support the element"
	)
	private IntegerDt myMaxLength;
	
	@Child(name="condition", type=IdDt.class, order=24, min=0, max=Child.MAX_UNLIMITED, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="A reference to an invariant that may make additional statements about the cardinality or value in the instance"
	)
	private java.util.List<IdDt> myCondition;
	
	@Child(name="constraint", order=25, min=0, max=Child.MAX_UNLIMITED, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="Formal constraints such as co-occurrence and other constraints that can be computationally evaluated within the context of the instance"
	)
	private java.util.List<Constraint> myConstraint;
	
	@Child(name="mustSupport", type=BooleanDt.class, order=26, min=0, max=1, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="If true, implementations that produce or consume resources SHALL provide \"support\" for the element in some meaningful way.  If false, the element may be ignored and not supported"
	)
	private BooleanDt myMustSupport;
	
	@Child(name="isModifier", type=BooleanDt.class, order=27, min=0, max=1, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="If true, the value of this element affects the interpretation of the element or resource that contains it, and the value of the element cannot be ignored. Typically, this is used for status, negation and qualification codes. The effect of this is that the element cannot be ignored by systems: they SHALL either recognize the element and process it, and/or a pre-determination has been made that it is not relevant to their particular system."
	)
	private BooleanDt myIsModifier;
	
	@Child(name="isSummary", type=BooleanDt.class, order=28, min=0, max=1, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="Whether the element should be included if a client requests a search with the parameter _summary=true"
	)
	private BooleanDt myIsSummary;
	
	@Child(name="binding", order=29, min=0, max=1, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="Binds to a value set if this element is coded (code, Coding, CodeableConcept)"
	)
	private Binding myBinding;
	
	@Child(name="mapping", order=30, min=0, max=Child.MAX_UNLIMITED, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="Identifies a concept from an external specification that roughly corresponds to this element"
	)
	private java.util.List<Mapping> myMapping;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myPath,  myRepresentation,  myName,  myLabel,  myCode,  mySlicing,  myShort,  myDefinition,  myComments,  myRequirements,  myAlias,  myMin,  myMax,  myBase,  myType,  myNameReference,  myDefaultValue,  myMeaningWhenMissing,  myFixed,  myPattern,  myExample,  myMinValue,  myMaxValue,  myMaxLength,  myCondition,  myConstraint,  myMustSupport,  myIsModifier,  myIsSummary,  myBinding,  myMapping);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myPath, myRepresentation, myName, myLabel, myCode, mySlicing, myShort, myDefinition, myComments, myRequirements, myAlias, myMin, myMax, myBase, myType, myNameReference, myDefaultValue, myMeaningWhenMissing, myFixed, myPattern, myExample, myMinValue, myMaxValue, myMaxLength, myCondition, myConstraint, myMustSupport, myIsModifier, myIsSummary, myBinding, myMapping);
	}

	/**
	 * Gets the value(s) for <b>path</b> ().
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
	 * Gets the value(s) for <b>path</b> ().
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
	 * Sets the value(s) for <b>path</b> ()
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
	 * Sets the value for <b>path</b> ()
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
	 * Gets the value(s) for <b>representation</b> ().
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
	 * Sets the value(s) for <b>representation</b> ()
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
	 * Add a value for <b>representation</b> () using an enumerated type. This
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
	 * Gets the first repetition for <b>representation</b> (),
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
	 * Add a value for <b>representation</b> ()
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
	 * Sets the value(s), and clears any existing value(s) for <b>representation</b> ()
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
	 * Gets the value(s) for <b>name</b> ().
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
	 * Gets the value(s) for <b>name</b> ().
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
	 * Sets the value(s) for <b>name</b> ()
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
	 * Sets the value for <b>name</b> ()
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
	 * Gets the value(s) for <b>label</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The text to display beside the element indicating its meaning or to use to prompt for the element in a user display or form.
     * </p> 
	 */
	public StringDt getLabelElement() {  
		if (myLabel == null) {
			myLabel = new StringDt();
		}
		return myLabel;
	}

	
	/**
	 * Gets the value(s) for <b>label</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The text to display beside the element indicating its meaning or to use to prompt for the element in a user display or form.
     * </p> 
	 */
	public String getLabel() {  
		return getLabelElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>label</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The text to display beside the element indicating its meaning or to use to prompt for the element in a user display or form.
     * </p> 
	 */
	public ElementDefinitionDt setLabel(StringDt theValue) {
		myLabel = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>label</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The text to display beside the element indicating its meaning or to use to prompt for the element in a user display or form.
     * </p> 
	 */
	public ElementDefinitionDt setLabel( String theString) {
		myLabel = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>code</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A code that provides the meaning for the element according to a particular terminology.
     * </p> 
	 */
	public java.util.List<CodingDt> getCode() {  
		if (myCode == null) {
			myCode = new java.util.ArrayList<CodingDt>();
		}
		return myCode;
	}

	/**
	 * Sets the value(s) for <b>code</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A code that provides the meaning for the element according to a particular terminology.
     * </p> 
	 */
	public ElementDefinitionDt setCode(java.util.List<CodingDt> theValue) {
		myCode = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>code</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A code that provides the meaning for the element according to a particular terminology.
     * </p> 
	 */
	public CodingDt addCode() {
		CodingDt newType = new CodingDt();
		getCode().add(newType);
		return newType; 
	}

	/**
	 * Adds a given new value for <b>code</b> ()
	 *
	 * <p>
	 * <b>Definition:</b>
	 * A code that provides the meaning for the element according to a particular terminology.
	 * </p>
	 * @param theValue The code to add (must not be <code>null</code>)
	 */
	public ElementDefinitionDt addCode(CodingDt theValue) {
		if (theValue == null) {
			throw new NullPointerException(Msg.code(77) + "theValue must not be null");
		}
		getCode().add(theValue);
		return this;
	}

	/**
	 * Gets the first repetition for <b>code</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * A code that provides the meaning for the element according to a particular terminology.
     * </p> 
	 */
	public CodingDt getCodeFirstRep() {
		if (getCode().isEmpty()) {
			return addCode();
		}
		return getCode().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>slicing</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates that the element is sliced into a set of alternative definitions (i.e. in a structure definition, there are multiple different constraints on a single element in the base resource). Slicing can be used in any resource that has cardinality ..* on the base resource, or any resource with a choice of types. The set of slices is any elements that come after this in the element sequence that have the same path, until a shorter path occurs (the shorter path terminates the set)
     * </p> 
	 */
	public Slicing getSlicing() {  
		if (mySlicing == null) {
			mySlicing = new Slicing();
		}
		return mySlicing;
	}

	/**
	 * Sets the value(s) for <b>slicing</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates that the element is sliced into a set of alternative definitions (i.e. in a structure definition, there are multiple different constraints on a single element in the base resource). Slicing can be used in any resource that has cardinality ..* on the base resource, or any resource with a choice of types. The set of slices is any elements that come after this in the element sequence that have the same path, until a shorter path occurs (the shorter path terminates the set)
     * </p> 
	 */
	public ElementDefinitionDt setSlicing(Slicing theValue) {
		mySlicing = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>short</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A concise description of what this element means (e.g. for use in auto-generated summaries)
     * </p> 
	 */
	public StringDt getShortElement() {  
		if (myShort == null) {
			myShort = new StringDt();
		}
		return myShort;
	}

	
	/**
	 * Gets the value(s) for <b>short</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A concise description of what this element means (e.g. for use in auto-generated summaries)
     * </p> 
	 */
	public String getShort() {  
		return getShortElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>short</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A concise description of what this element means (e.g. for use in auto-generated summaries)
     * </p> 
	 */
	public ElementDefinitionDt setShort(StringDt theValue) {
		myShort = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>short</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A concise description of what this element means (e.g. for use in auto-generated summaries)
     * </p> 
	 */
	public ElementDefinitionDt setShort( String theString) {
		myShort = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>definition</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Provides a complete explanation of the meaning of the data element for human readability.  For the case of elements derived from existing elements (e.g. constraints), the definition SHALL be consistent with the base definition, but convey the meaning of the element in the particular context of use of the resource
     * </p> 
	 */
	public MarkdownDt getDefinitionElement() {  
		if (myDefinition == null) {
			myDefinition = new MarkdownDt();
		}
		return myDefinition;
	}

	
	/**
	 * Gets the value(s) for <b>definition</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Provides a complete explanation of the meaning of the data element for human readability.  For the case of elements derived from existing elements (e.g. constraints), the definition SHALL be consistent with the base definition, but convey the meaning of the element in the particular context of use of the resource
     * </p> 
	 */
	public String getDefinition() {  
		return getDefinitionElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>definition</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Provides a complete explanation of the meaning of the data element for human readability.  For the case of elements derived from existing elements (e.g. constraints), the definition SHALL be consistent with the base definition, but convey the meaning of the element in the particular context of use of the resource
     * </p> 
	 */
	public ElementDefinitionDt setDefinition(MarkdownDt theValue) {
		myDefinition = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>comments</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Explanatory notes and implementation guidance about the data element, including notes about how to use the data properly, exceptions to proper use, etc.
     * </p> 
	 */
	public MarkdownDt getCommentsElement() {  
		if (myComments == null) {
			myComments = new MarkdownDt();
		}
		return myComments;
	}

	
	/**
	 * Gets the value(s) for <b>comments</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Explanatory notes and implementation guidance about the data element, including notes about how to use the data properly, exceptions to proper use, etc.
     * </p> 
	 */
	public String getComments() {  
		return getCommentsElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>comments</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Explanatory notes and implementation guidance about the data element, including notes about how to use the data properly, exceptions to proper use, etc.
     * </p> 
	 */
	public ElementDefinitionDt setComments(MarkdownDt theValue) {
		myComments = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>requirements</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * This element is for traceability of why the element was created and why the constraints exist as they do. This may be used to point to source materials or specifications that drove the structure of this element.
     * </p> 
	 */
	public MarkdownDt getRequirementsElement() {  
		if (myRequirements == null) {
			myRequirements = new MarkdownDt();
		}
		return myRequirements;
	}

	
	/**
	 * Gets the value(s) for <b>requirements</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * This element is for traceability of why the element was created and why the constraints exist as they do. This may be used to point to source materials or specifications that drove the structure of this element.
     * </p> 
	 */
	public String getRequirements() {  
		return getRequirementsElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>requirements</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * This element is for traceability of why the element was created and why the constraints exist as they do. This may be used to point to source materials or specifications that drove the structure of this element.
     * </p> 
	 */
	public ElementDefinitionDt setRequirements(MarkdownDt theValue) {
		myRequirements = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>alias</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies additional names by which this element might also be known
     * </p> 
	 */
	public java.util.List<StringDt> getAlias() {  
		if (myAlias == null) {
			myAlias = new java.util.ArrayList<StringDt>();
		}
		return myAlias;
	}

	/**
	 * Sets the value(s) for <b>alias</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies additional names by which this element might also be known
     * </p> 
	 */
	public ElementDefinitionDt setAlias(java.util.List<StringDt> theValue) {
		myAlias = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>alias</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies additional names by which this element might also be known
     * </p> 
	 */
	public StringDt addAlias() {
		StringDt newType = new StringDt();
		getAlias().add(newType);
		return newType; 
	}

	/**
	 * Adds a given new value for <b>alias</b> ()
	 *
	 * <p>
	 * <b>Definition:</b>
	 * Identifies additional names by which this element might also be known
	 * </p>
	 * @param theValue The alias to add (must not be <code>null</code>)
	 */
	public ElementDefinitionDt addAlias(StringDt theValue) {
		if (theValue == null) {
			throw new NullPointerException(Msg.code(78) + "theValue must not be null");
		}
		getAlias().add(theValue);
		return this;
	}

	/**
	 * Gets the first repetition for <b>alias</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies additional names by which this element might also be known
     * </p> 
	 */
	public StringDt getAliasFirstRep() {
		if (getAlias().isEmpty()) {
			return addAlias();
		}
		return getAlias().get(0); 
	}
 	/**
	 * Adds a new value for <b>alias</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies additional names by which this element might also be known
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public ElementDefinitionDt addAlias( String theString) {
		if (myAlias == null) {
			myAlias = new java.util.ArrayList<StringDt>();
		}
		myAlias.add(new StringDt(theString));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>min</b> ().
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
	 * Gets the value(s) for <b>min</b> ().
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
	 * Sets the value(s) for <b>min</b> ()
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
	 * Sets the value for <b>min</b> ()
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
	 * Gets the value(s) for <b>max</b> ().
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
	 * Gets the value(s) for <b>max</b> ().
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
	 * Sets the value(s) for <b>max</b> ()
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
	 * Sets the value for <b>max</b> ()
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
	 * Gets the value(s) for <b>base</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Information about the base definition of the element, provided to make it unncessary for tools to trace the derviation of the element through the derived and related profiles. This information is only provided where the element definition represents a constraint on another element definition, and must be present if there is a base element definition
     * </p> 
	 */
	public Base getBase() {  
		if (myBase == null) {
			myBase = new Base();
		}
		return myBase;
	}

	/**
	 * Sets the value(s) for <b>base</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Information about the base definition of the element, provided to make it unncessary for tools to trace the derviation of the element through the derived and related profiles. This information is only provided where the element definition represents a constraint on another element definition, and must be present if there is a base element definition
     * </p> 
	 */
	public ElementDefinitionDt setBase(Base theValue) {
		myBase = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>type</b> ().
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
	 * Sets the value(s) for <b>type</b> ()
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
	 * Adds and returns a new value for <b>type</b> ()
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
	 * Adds a given new value for <b>type</b> ()
	 *
	 * <p>
	 * <b>Definition:</b>
	 * The data type or resource that the value of this element is permitted to be
	 * </p>
	 * @param theValue The type to add (must not be <code>null</code>)
	 */
	public ElementDefinitionDt addType(Type theValue) {
		if (theValue == null) {
			throw new NullPointerException(Msg.code(79) + "theValue must not be null");
		}
		getType().add(theValue);
		return this;
	}

	/**
	 * Gets the first repetition for <b>type</b> (),
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
	 * Gets the value(s) for <b>nameReference</b> ().
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
	 * Gets the value(s) for <b>nameReference</b> ().
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
	 * Sets the value(s) for <b>nameReference</b> ()
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
	 * Sets the value for <b>nameReference</b> ()
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
	 * Gets the value(s) for <b>defaultValue[x]</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The value that should be used if there is no value stated in the instance (e.g. 'if not otherwise specified, the abstract is false')
     * </p> 
	 */
	public IDatatype getDefaultValue() {  
		return myDefaultValue;
	}

	/**
	 * Sets the value(s) for <b>defaultValue[x]</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The value that should be used if there is no value stated in the instance (e.g. 'if not otherwise specified, the abstract is false')
     * </p> 
	 */
	public ElementDefinitionDt setDefaultValue(IDatatype theValue) {
		myDefaultValue = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>meaningWhenMissing</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The Implicit meaning that is to be understood when this element is missing (e.g. 'when this element is missing, the period is ongoing'
     * </p> 
	 */
	public MarkdownDt getMeaningWhenMissingElement() {  
		if (myMeaningWhenMissing == null) {
			myMeaningWhenMissing = new MarkdownDt();
		}
		return myMeaningWhenMissing;
	}

	
	/**
	 * Gets the value(s) for <b>meaningWhenMissing</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The Implicit meaning that is to be understood when this element is missing (e.g. 'when this element is missing, the period is ongoing'
     * </p> 
	 */
	public String getMeaningWhenMissing() {  
		return getMeaningWhenMissingElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>meaningWhenMissing</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The Implicit meaning that is to be understood when this element is missing (e.g. 'when this element is missing, the period is ongoing'
     * </p> 
	 */
	public ElementDefinitionDt setMeaningWhenMissing(MarkdownDt theValue) {
		myMeaningWhenMissing = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>fixed[x]</b> ().
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
	 * Sets the value(s) for <b>fixed[x]</b> ()
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
	 * Gets the value(s) for <b>pattern[x]</b> ().
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
	 * Sets the value(s) for <b>pattern[x]</b> ()
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
	 * Gets the value(s) for <b>example[x]</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A sample value for this element demonstrating the type of information that would typically be captured.
     * </p> 
	 */
	public IDatatype getExample() {  
		return myExample;
	}

	/**
	 * Sets the value(s) for <b>example[x]</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A sample value for this element demonstrating the type of information that would typically be captured.
     * </p> 
	 */
	public ElementDefinitionDt setExample(IDatatype theValue) {
		myExample = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>minValue[x]</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The minimum allowed value for the element. The value is inclusive. This is allowed for the types date, dateTime, instant, time, decimal, integer, and Quantity
     * </p> 
	 */
	public IDatatype getMinValue() {  
		return myMinValue;
	}

	/**
	 * Sets the value(s) for <b>minValue[x]</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The minimum allowed value for the element. The value is inclusive. This is allowed for the types date, dateTime, instant, time, decimal, integer, and Quantity
     * </p> 
	 */
	public ElementDefinitionDt setMinValue(IDatatype theValue) {
		myMinValue = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>maxValue[x]</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The maximum allowed value for the element. The value is inclusive. This is allowed for the types date, dateTime, instant, time, decimal, integer, and Quantity
     * </p> 
	 */
	public IDatatype getMaxValue() {  
		return myMaxValue;
	}

	/**
	 * Sets the value(s) for <b>maxValue[x]</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The maximum allowed value for the element. The value is inclusive. This is allowed for the types date, dateTime, instant, time, decimal, integer, and Quantity
     * </p> 
	 */
	public ElementDefinitionDt setMaxValue(IDatatype theValue) {
		myMaxValue = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>maxLength</b> ().
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
	 * Gets the value(s) for <b>maxLength</b> ().
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
	 * Sets the value(s) for <b>maxLength</b> ()
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
	 * Sets the value for <b>maxLength</b> ()
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
	 * Gets the value(s) for <b>condition</b> ().
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
	 * Sets the value(s) for <b>condition</b> ()
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
	 * Adds and returns a new value for <b>condition</b> ()
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
	 * Adds a given new value for <b>condition</b> ()
	 *
	 * <p>
	 * <b>Definition:</b>
	 * A reference to an invariant that may make additional statements about the cardinality or value in the instance
	 * </p>
	 * @param theValue The condition to add (must not be <code>null</code>)
	 */
	public ElementDefinitionDt addCondition(IdDt theValue) {
		if (theValue == null) {
			throw new NullPointerException(Msg.code(80) + "theValue must not be null");
		}
		getCondition().add(theValue);
		return this;
	}

	/**
	 * Gets the first repetition for <b>condition</b> (),
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
	 * Adds a new value for <b>condition</b> ()
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
	 * Gets the value(s) for <b>constraint</b> ().
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
	 * Sets the value(s) for <b>constraint</b> ()
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
	 * Adds and returns a new value for <b>constraint</b> ()
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
	 * Adds a given new value for <b>constraint</b> ()
	 *
	 * <p>
	 * <b>Definition:</b>
	 * Formal constraints such as co-occurrence and other constraints that can be computationally evaluated within the context of the instance
	 * </p>
	 * @param theValue The constraint to add (must not be <code>null</code>)
	 */
	public ElementDefinitionDt addConstraint(Constraint theValue) {
		if (theValue == null) {
			throw new NullPointerException(Msg.code(81) + "theValue must not be null");
		}
		getConstraint().add(theValue);
		return this;
	}

	/**
	 * Gets the first repetition for <b>constraint</b> (),
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
	 * Gets the value(s) for <b>mustSupport</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If true, implementations that produce or consume resources SHALL provide \&quot;support\&quot; for the element in some meaningful way.  If false, the element may be ignored and not supported
     * </p> 
	 */
	public BooleanDt getMustSupportElement() {  
		if (myMustSupport == null) {
			myMustSupport = new BooleanDt();
		}
		return myMustSupport;
	}

	
	/**
	 * Gets the value(s) for <b>mustSupport</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If true, implementations that produce or consume resources SHALL provide \&quot;support\&quot; for the element in some meaningful way.  If false, the element may be ignored and not supported
     * </p> 
	 */
	public Boolean getMustSupport() {  
		return getMustSupportElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>mustSupport</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * If true, implementations that produce or consume resources SHALL provide \&quot;support\&quot; for the element in some meaningful way.  If false, the element may be ignored and not supported
     * </p> 
	 */
	public ElementDefinitionDt setMustSupport(BooleanDt theValue) {
		myMustSupport = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>mustSupport</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * If true, implementations that produce or consume resources SHALL provide \&quot;support\&quot; for the element in some meaningful way.  If false, the element may be ignored and not supported
     * </p> 
	 */
	public ElementDefinitionDt setMustSupport( boolean theBoolean) {
		myMustSupport = new BooleanDt(theBoolean); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>isModifier</b> ().
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
	 * Gets the value(s) for <b>isModifier</b> ().
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
	 * Sets the value(s) for <b>isModifier</b> ()
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
	 * Sets the value for <b>isModifier</b> ()
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
	 * Gets the value(s) for <b>isSummary</b> ().
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
	 * Gets the value(s) for <b>isSummary</b> ().
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
	 * Sets the value(s) for <b>isSummary</b> ()
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
	 * Sets the value for <b>isSummary</b> ()
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
	 * Gets the value(s) for <b>binding</b> ().
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
	 * Sets the value(s) for <b>binding</b> ()
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
	 * Gets the value(s) for <b>mapping</b> ().
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
	 * Sets the value(s) for <b>mapping</b> ()
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
	 * Adds and returns a new value for <b>mapping</b> ()
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
	 * Adds a given new value for <b>mapping</b> ()
	 *
	 * <p>
	 * <b>Definition:</b>
	 * Identifies a concept from an external specification that roughly corresponds to this element
	 * </p>
	 * @param theValue The mapping to add (must not be <code>null</code>)
	 */
	public ElementDefinitionDt addMapping(Mapping theValue) {
		if (theValue == null) {
			throw new NullPointerException(Msg.code(82) + "theValue must not be null");
		}
		getMapping().add(theValue);
		return this;
	}

	/**
	 * Gets the first repetition for <b>mapping</b> (),
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
	 * Block class for child element: <b>ElementDefinition.slicing</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates that the element is sliced into a set of alternative definitions (i.e. in a structure definition, there are multiple different constraints on a single element in the base resource). Slicing can be used in any resource that has cardinality ..* on the base resource, or any resource with a choice of types. The set of slices is any elements that come after this in the element sequence that have the same path, until a shorter path occurs (the shorter path terminates the set)
     * </p> 
	 */
	@Block()	
	public static class Slicing 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="discriminator", type=StringDt.class, order=0, min=0, max=Child.MAX_UNLIMITED, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="Designates which child elements are used to discriminate between the slices when processing an instance. If one or more discriminators are provided, the value of the child elements in the instance data SHALL completely distinguish which slice the element in the resource matches based on the allowed values for those elements in each of the slices"
	)
	private java.util.List<StringDt> myDiscriminator;
	
	@Child(name="description", type=StringDt.class, order=1, min=0, max=1, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="A human-readable text description of how the slicing works. If there is no discriminator, this is required to be present to provide whatever information is possible about how the slices can be differentiated"
	)
	private StringDt myDescription;
	
	@Child(name="ordered", type=BooleanDt.class, order=2, min=0, max=1, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="If the matching elements have to occur in the same order as defined in the profile"
	)
	private BooleanDt myOrdered;
	
	@Child(name="rules", type=CodeDt.class, order=3, min=1, max=1, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
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
	 * Gets the value(s) for <b>discriminator</b> ().
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
	 * Sets the value(s) for <b>discriminator</b> ()
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
	 * Adds and returns a new value for <b>discriminator</b> ()
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
	 * Adds a given new value for <b>discriminator</b> ()
	 *
	 * <p>
	 * <b>Definition:</b>
	 * Designates which child elements are used to discriminate between the slices when processing an instance. If one or more discriminators are provided, the value of the child elements in the instance data SHALL completely distinguish which slice the element in the resource matches based on the allowed values for those elements in each of the slices
	 * </p>
	 * @param theValue The discriminator to add (must not be <code>null</code>)
	 */
	public Slicing addDiscriminator(StringDt theValue) {
		if (theValue == null) {
			throw new NullPointerException(Msg.code(83) + "theValue must not be null");
		}
		getDiscriminator().add(theValue);
		return this;
	}

	/**
	 * Gets the first repetition for <b>discriminator</b> (),
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
	 * Adds a new value for <b>discriminator</b> ()
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
	 * Gets the value(s) for <b>description</b> ().
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
	 * Gets the value(s) for <b>description</b> ().
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
	 * Sets the value(s) for <b>description</b> ()
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
	 * Sets the value for <b>description</b> ()
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
	 * Gets the value(s) for <b>ordered</b> ().
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
	 * Gets the value(s) for <b>ordered</b> ().
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
	 * Sets the value(s) for <b>ordered</b> ()
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
	 * Sets the value for <b>ordered</b> ()
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
	 * Gets the value(s) for <b>rules</b> ().
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
	 * Gets the value(s) for <b>rules</b> ().
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
	 * Sets the value(s) for <b>rules</b> ()
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
	 * Sets the value(s) for <b>rules</b> ()
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
	 * Block class for child element: <b>ElementDefinition.base</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Information about the base definition of the element, provided to make it unncessary for tools to trace the derviation of the element through the derived and related profiles. This information is only provided where the element definition represents a constraint on another element definition, and must be present if there is a base element definition
     * </p> 
	 */
	@Block()	
	public static class Base 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="path", type=StringDt.class, order=0, min=1, max=1, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="The Path that identifies the base element - this matches the ElementDefinition.path for that element. Across FHIR, there is only one base definition of any element - that is, an element definition on a [[[StructureDefinition]]] without a StructureDefinition.base"
	)
	private StringDt myPath;
	
	@Child(name="min", type=IntegerDt.class, order=1, min=1, max=1, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="Minimum cardinality of the base element identified by the path"
	)
	private IntegerDt myMin;
	
	@Child(name="max", type=StringDt.class, order=2, min=1, max=1, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="Maximum cardinality of the base element identified by the path"
	)
	private StringDt myMax;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myPath,  myMin,  myMax);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myPath, myMin, myMax);
	}

	/**
	 * Gets the value(s) for <b>path</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The Path that identifies the base element - this matches the ElementDefinition.path for that element. Across FHIR, there is only one base definition of any element - that is, an element definition on a [[[StructureDefinition]]] without a StructureDefinition.base
     * </p> 
	 */
	public StringDt getPathElement() {  
		if (myPath == null) {
			myPath = new StringDt();
		}
		return myPath;
	}

	
	/**
	 * Gets the value(s) for <b>path</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The Path that identifies the base element - this matches the ElementDefinition.path for that element. Across FHIR, there is only one base definition of any element - that is, an element definition on a [[[StructureDefinition]]] without a StructureDefinition.base
     * </p> 
	 */
	public String getPath() {  
		return getPathElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>path</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The Path that identifies the base element - this matches the ElementDefinition.path for that element. Across FHIR, there is only one base definition of any element - that is, an element definition on a [[[StructureDefinition]]] without a StructureDefinition.base
     * </p> 
	 */
	public Base setPath(StringDt theValue) {
		myPath = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>path</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The Path that identifies the base element - this matches the ElementDefinition.path for that element. Across FHIR, there is only one base definition of any element - that is, an element definition on a [[[StructureDefinition]]] without a StructureDefinition.base
     * </p> 
	 */
	public Base setPath( String theString) {
		myPath = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>min</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Minimum cardinality of the base element identified by the path
     * </p> 
	 */
	public IntegerDt getMinElement() {  
		if (myMin == null) {
			myMin = new IntegerDt();
		}
		return myMin;
	}

	
	/**
	 * Gets the value(s) for <b>min</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Minimum cardinality of the base element identified by the path
     * </p> 
	 */
	public Integer getMin() {  
		return getMinElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>min</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Minimum cardinality of the base element identified by the path
     * </p> 
	 */
	public Base setMin(IntegerDt theValue) {
		myMin = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>min</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Minimum cardinality of the base element identified by the path
     * </p> 
	 */
	public Base setMin( int theInteger) {
		myMin = new IntegerDt(theInteger); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>max</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Maximum cardinality of the base element identified by the path
     * </p> 
	 */
	public StringDt getMaxElement() {  
		if (myMax == null) {
			myMax = new StringDt();
		}
		return myMax;
	}

	
	/**
	 * Gets the value(s) for <b>max</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Maximum cardinality of the base element identified by the path
     * </p> 
	 */
	public String getMax() {  
		return getMaxElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>max</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Maximum cardinality of the base element identified by the path
     * </p> 
	 */
	public Base setMax(StringDt theValue) {
		myMax = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>max</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Maximum cardinality of the base element identified by the path
     * </p> 
	 */
	public Base setMax( String theString) {
		myMax = new StringDt(theString); 
		return this; 
	}

 


	}


	/**
	 * Block class for child element: <b>ElementDefinition.type</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The data type or resource that the value of this element is permitted to be
     * </p> 
	 */
	@Block()	
	public static class Type 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="code", type=CodeDt.class, order=0, min=1, max=1, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="Name of Data type or Resource that is a(or the) type used for this element"
	)
	private CodeDt myCode;
	
	@Child(name="profile", type=UriDt.class, order=1, min=0, max=Child.MAX_UNLIMITED, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="Identifies a profile structure or implementation Guide that SHALL hold for resources or datatypes referenced as the type of this element. Can be a local reference - to another structure in this profile, or a reference to a structure in another profile. When more than one profile is specified, the content must conform to all of them. When an implementation guide is specified, the resource SHALL conform to at least one profile defined in the implementation guide"
	)
	private java.util.List<UriDt> myProfile;
	
	@Child(name="aggregation", type=CodeDt.class, order=2, min=0, max=Child.MAX_UNLIMITED, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
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
	 * Gets the value(s) for <b>code</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Name of Data type or Resource that is a(or the) type used for this element
     * </p> 
	 */
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
     * Name of Data type or Resource that is a(or the) type used for this element
     * </p> 
	 */
	public String getCode() {  
		return getCodeElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>code</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Name of Data type or Resource that is a(or the) type used for this element
     * </p> 
	 */
	public Type setCode(CodeDt theValue) {
		myCode = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>code</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Name of Data type or Resource that is a(or the) type used for this element
     * </p> 
	 */
	public Type setCode( String theCode) {
		myCode = new CodeDt(theCode); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>profile</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a profile structure or implementation Guide that SHALL hold for resources or datatypes referenced as the type of this element. Can be a local reference - to another structure in this profile, or a reference to a structure in another profile. When more than one profile is specified, the content must conform to all of them. When an implementation guide is specified, the resource SHALL conform to at least one profile defined in the implementation guide
     * </p> 
	 */
	public java.util.List<UriDt> getProfile() {  
		if (myProfile == null) {
			myProfile = new java.util.ArrayList<UriDt>();
		}
		return myProfile;
	}

	/**
	 * Sets the value(s) for <b>profile</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a profile structure or implementation Guide that SHALL hold for resources or datatypes referenced as the type of this element. Can be a local reference - to another structure in this profile, or a reference to a structure in another profile. When more than one profile is specified, the content must conform to all of them. When an implementation guide is specified, the resource SHALL conform to at least one profile defined in the implementation guide
     * </p> 
	 */
	public Type setProfile(java.util.List<UriDt> theValue) {
		myProfile = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>profile</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a profile structure or implementation Guide that SHALL hold for resources or datatypes referenced as the type of this element. Can be a local reference - to another structure in this profile, or a reference to a structure in another profile. When more than one profile is specified, the content must conform to all of them. When an implementation guide is specified, the resource SHALL conform to at least one profile defined in the implementation guide
     * </p> 
	 */
	public UriDt addProfile() {
		UriDt newType = new UriDt();
		getProfile().add(newType);
		return newType; 
	}

	/**
	 * Adds a given new value for <b>profile</b> ()
	 *
	 * <p>
	 * <b>Definition:</b>
	 * Identifies a profile structure or implementation Guide that SHALL hold for resources or datatypes referenced as the type of this element. Can be a local reference - to another structure in this profile, or a reference to a structure in another profile. When more than one profile is specified, the content must conform to all of them. When an implementation guide is specified, the resource SHALL conform to at least one profile defined in the implementation guide
	 * </p>
	 * @param theValue The profile to add (must not be <code>null</code>)
	 */
	public Type addProfile(UriDt theValue) {
		if (theValue == null) {
			throw new NullPointerException(Msg.code(84) + "theValue must not be null");
		}
		getProfile().add(theValue);
		return this;
	}

	/**
	 * Gets the first repetition for <b>profile</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a profile structure or implementation Guide that SHALL hold for resources or datatypes referenced as the type of this element. Can be a local reference - to another structure in this profile, or a reference to a structure in another profile. When more than one profile is specified, the content must conform to all of them. When an implementation guide is specified, the resource SHALL conform to at least one profile defined in the implementation guide
     * </p> 
	 */
	public UriDt getProfileFirstRep() {
		if (getProfile().isEmpty()) {
			return addProfile();
		}
		return getProfile().get(0); 
	}
 	/**
	 * Adds a new value for <b>profile</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a profile structure or implementation Guide that SHALL hold for resources or datatypes referenced as the type of this element. Can be a local reference - to another structure in this profile, or a reference to a structure in another profile. When more than one profile is specified, the content must conform to all of them. When an implementation guide is specified, the resource SHALL conform to at least one profile defined in the implementation guide
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Type addProfile( String theUri) {
		if (myProfile == null) {
			myProfile = new java.util.ArrayList<UriDt>();
		}
		myProfile.add(new UriDt(theUri));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>aggregation</b> ().
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
	 * Sets the value(s) for <b>aggregation</b> ()
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
	 * Add a value for <b>aggregation</b> () using an enumerated type. This
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
	 * Gets the first repetition for <b>aggregation</b> (),
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
	 * Add a value for <b>aggregation</b> ()
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
	 * Sets the value(s), and clears any existing value(s) for <b>aggregation</b> ()
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
	 * Block class for child element: <b>ElementDefinition.constraint</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Formal constraints such as co-occurrence and other constraints that can be computationally evaluated within the context of the instance
     * </p> 
	 */
	@Block()	
	public static class Constraint 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="key", type=IdDt.class, order=0, min=1, max=1, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="Allows identification of which elements have their cardinalities impacted by the constraint.  Will not be referenced for constraints that do not affect cardinality"
	)
	private IdDt myKey;
	
	@Child(name="requirements", type=StringDt.class, order=1, min=0, max=1, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="Description of why this constraint is necessary or appropriate"
	)
	private StringDt myRequirements;
	
	@Child(name="severity", type=CodeDt.class, order=2, min=1, max=1, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="Identifies the impact constraint violation has on the conformance of the instance"
	)
	private BoundCodeDt<ConstraintSeverityEnum> mySeverity;
	
	@Child(name="human", type=StringDt.class, order=3, min=1, max=1, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="Text that can be used to describe the constraint in messages identifying that the constraint has been violated"
	)
	private StringDt myHuman;
	
	@Child(name="xpath", type=StringDt.class, order=4, min=1, max=1, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="An XPath expression of constraint that can be executed to see if this constraint is met"
	)
	private StringDt myXpath;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myKey,  myRequirements,  mySeverity,  myHuman,  myXpath);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myKey, myRequirements, mySeverity, myHuman, myXpath);
	}

	/**
	 * Gets the value(s) for <b>key</b> ().
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
	 * Gets the value(s) for <b>key</b> ().
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
	 * Sets the value(s) for <b>key</b> ()
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
	 * Sets the value for <b>key</b> ()
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
	 * Gets the value(s) for <b>requirements</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Description of why this constraint is necessary or appropriate
     * </p> 
	 */
	public StringDt getRequirementsElement() {  
		if (myRequirements == null) {
			myRequirements = new StringDt();
		}
		return myRequirements;
	}

	
	/**
	 * Gets the value(s) for <b>requirements</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Description of why this constraint is necessary or appropriate
     * </p> 
	 */
	public String getRequirements() {  
		return getRequirementsElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>requirements</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Description of why this constraint is necessary or appropriate
     * </p> 
	 */
	public Constraint setRequirements(StringDt theValue) {
		myRequirements = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>requirements</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Description of why this constraint is necessary or appropriate
     * </p> 
	 */
	public Constraint setRequirements( String theString) {
		myRequirements = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>severity</b> ().
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
	 * Gets the value(s) for <b>severity</b> ().
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
	 * Sets the value(s) for <b>severity</b> ()
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
	 * Sets the value(s) for <b>severity</b> ()
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
	 * Gets the value(s) for <b>human</b> ().
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
	 * Gets the value(s) for <b>human</b> ().
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
	 * Sets the value(s) for <b>human</b> ()
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
	 * Sets the value for <b>human</b> ()
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
	 * Gets the value(s) for <b>xpath</b> ().
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
	 * Gets the value(s) for <b>xpath</b> ().
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
	 * Sets the value(s) for <b>xpath</b> ()
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
	 * Sets the value for <b>xpath</b> ()
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
	 * Block class for child element: <b>ElementDefinition.binding</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Binds to a value set if this element is coded (code, Coding, CodeableConcept)
     * </p> 
	 */
	@Block()	
	public static class Binding 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="strength", type=CodeDt.class, order=0, min=1, max=1, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="Indicates the degree of conformance expectations associated with this binding - that is, the degree to which the provided value set must be adhered to in the instances"
	)
	private BoundCodeDt<BindingStrengthEnum> myStrength;
	
	@Child(name="description", type=StringDt.class, order=1, min=0, max=1, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="Describes the intended use of this particular set of codes"
	)
	private StringDt myDescription;
	
	@Child(name="valueSet", order=2, min=0, max=1, summary=true, modifier=false, type={
		UriDt.class, 		ValueSet.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Points to the value set or external definition (e.g. implicit value set) that identifies the set of codes to be used"
	)
	private IDatatype myValueSet;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myStrength,  myDescription,  myValueSet);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myStrength, myDescription, myValueSet);
	}

	/**
	 * Gets the value(s) for <b>strength</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the degree of conformance expectations associated with this binding - that is, the degree to which the provided value set must be adhered to in the instances
     * </p> 
	 */
	public BoundCodeDt<BindingStrengthEnum> getStrengthElement() {  
		if (myStrength == null) {
			myStrength = new BoundCodeDt<BindingStrengthEnum>(BindingStrengthEnum.VALUESET_BINDER);
		}
		return myStrength;
	}

	
	/**
	 * Gets the value(s) for <b>strength</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the degree of conformance expectations associated with this binding - that is, the degree to which the provided value set must be adhered to in the instances
     * </p> 
	 */
	public String getStrength() {  
		return getStrengthElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>strength</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the degree of conformance expectations associated with this binding - that is, the degree to which the provided value set must be adhered to in the instances
     * </p> 
	 */
	public Binding setStrength(BoundCodeDt<BindingStrengthEnum> theValue) {
		myStrength = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>strength</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the degree of conformance expectations associated with this binding - that is, the degree to which the provided value set must be adhered to in the instances
     * </p> 
	 */
	public Binding setStrength(BindingStrengthEnum theValue) {
		getStrengthElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>description</b> ().
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
	 * Gets the value(s) for <b>description</b> ().
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
	 * Sets the value(s) for <b>description</b> ()
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
	 * Sets the value for <b>description</b> ()
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
	 * Gets the value(s) for <b>valueSet[x]</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Points to the value set or external definition (e.g. implicit value set) that identifies the set of codes to be used
     * </p> 
	 */
	public IDatatype getValueSet() {  
		return myValueSet;
	}

	/**
	 * Sets the value(s) for <b>valueSet[x]</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Points to the value set or external definition (e.g. implicit value set) that identifies the set of codes to be used
     * </p> 
	 */
	public Binding setValueSet(IDatatype theValue) {
		myValueSet = theValue;
		return this;
	}
	
	

  


	}


	/**
	 * Block class for child element: <b>ElementDefinition.mapping</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a concept from an external specification that roughly corresponds to this element
     * </p> 
	 */
	@Block()	
	public static class Mapping 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="identity", type=IdDt.class, order=0, min=1, max=1, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="An internal reference to the definition of a mapping"
	)
	private IdDt myIdentity;
	
	@Child(name="language", type=CodeDt.class, order=1, min=0, max=1, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="Identifies the computable language in which mapping.map is expressed."
	)
	private CodeDt myLanguage;
	
	@Child(name="map", type=StringDt.class, order=2, min=1, max=1, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="Expresses what part of the target specification corresponds to this element"
	)
	private StringDt myMap;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentity,  myLanguage,  myMap);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentity, myLanguage, myMap);
	}

	/**
	 * Gets the value(s) for <b>identity</b> ().
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
	 * Gets the value(s) for <b>identity</b> ().
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
	 * Sets the value(s) for <b>identity</b> ()
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
	 * Sets the value for <b>identity</b> ()
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
	 * Gets the value(s) for <b>language</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the computable language in which mapping.map is expressed.
     * </p> 
	 */
	public CodeDt getLanguageElement() {  
		if (myLanguage == null) {
			myLanguage = new CodeDt();
		}
		return myLanguage;
	}

	
	/**
	 * Gets the value(s) for <b>language</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the computable language in which mapping.map is expressed.
     * </p> 
	 */
	public String getLanguage() {  
		return getLanguageElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>language</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the computable language in which mapping.map is expressed.
     * </p> 
	 */
	public Mapping setLanguage(CodeDt theValue) {
		myLanguage = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>language</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the computable language in which mapping.map is expressed.
     * </p> 
	 */
	public Mapping setLanguage( String theCode) {
		myLanguage = new CodeDt(theCode); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>map</b> ().
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
	 * Gets the value(s) for <b>map</b> ().
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
	 * Sets the value(s) for <b>map</b> ()
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
	 * Sets the value for <b>map</b> ()
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