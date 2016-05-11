package org.hl7.fhir.dstu3.model;

import java.util.ArrayList;
import java.util.List;

/**
 * A child element or property defined by the FHIR specification
 * This class is defined as a helper class when iterating the 
 * children of an element in a generic fashion
 * 
 * At present, iteration is only based on the specification, but 
 * this may be changed to allow profile based expression at a
 * later date
 * 
 * note: there's no point in creating one of these classes outside this package
 */
public class Property {

	/**
	 * The name of the property as found in the FHIR specification
	 */
	private String name;
	
	/**
	 * The type of the property as specified in the FHIR specification (e.g. type|type|Reference(Name|Name)
	 */
	private String typeCode;
	
	/**
	 * The formal definition of the element given in the FHIR specification
	 */
	private String definition;
	
	/**
	 * The minimum allowed cardinality - 0 or 1 when based on the specification
	 */
	private int minCardinality;
	
	/** 
	 * The maximum allowed cardinality - 1 or MAX_INT when based on the specification
	 */
	private int maxCardinality;
	
	/**
	 * The actual elements that exist on this instance
	 */
	private List<Base> values = new ArrayList<Base>();

	/**
	 * For run time, if/once a property is hooked up to it's definition
	 */
	private StructureDefinition structure; 

	/**
	 * Internal constructor
	 */
	public Property(String name, String typeCode, String definition, int minCardinality, int maxCardinality, Base value) {
	  super();
	  this.name = name;
	  this.typeCode = typeCode;
	  this.definition = definition;
	  this.minCardinality = minCardinality;
	  this.maxCardinality = maxCardinality;
	  this.values.add(value);
  }

	/**
	 * Internal constructor
	 */
	public Property(String name, String typeCode, String definition, int minCardinality, int maxCardinality, List<? extends Base> values) {
	  super();
	  this.name = name;
	  this.typeCode = typeCode;
	  this.definition = definition;
	  this.minCardinality = minCardinality;
	  this.maxCardinality = maxCardinality;
	  if (values != null)
	    this.values.addAll(values);
  }

	/**
	 * @return The name of this property in the FHIR Specification
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return The stated type in the FHIR specification
	 */
	public String getTypeCode() {
		return typeCode;
	}

	/** 
	 * @return The definition of this element in the FHIR spec
	 */
	public String getDefinition() {
		return definition;
	}

	/**
	 * @return the minimum cardinality for this element 
	 */
	public int getMinCardinality() {
		return minCardinality;
	}

	/**
	 * @return the maximum cardinality for this element 
	 */
	public int getMaxCardinality() {
		return maxCardinality;
	}

	/**
	 * @return the actual values - will only be 1 unless maximum cardinality == MAX_INT
	 */
	public List<Base> getValues() {
		return values;
	}

  public boolean hasValues() {
    for (Base e : getValues())
      if (e != null)
        return true;
    return false;
  }

  public StructureDefinition getStructure() {
    return structure;
  }

  public void setStructure(StructureDefinition structure) {
    this.structure = structure;
  }


	
}
