package org.hl7.fhir.dstu3.elementmodel;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.Validate;
import org.hl7.fhir.dstu3.elementmodel.Element.ElementSortComparator;
import org.hl7.fhir.dstu3.model.Base;
import org.hl7.fhir.dstu3.model.ElementDefinition;
import org.hl7.fhir.dstu3.model.ElementDefinition.TypeRefComponent;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.StructureDefinition;
import org.hl7.fhir.dstu3.model.Type;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.utilities.xhtml.XhtmlComposer;
import org.hl7.fhir.utilities.xhtml.XhtmlNode;

/**
 * This class represents the underlying reference model of FHIR
 * 
 * A resource is nothing but a set of elements, where every element has a 
 * name, maybe a stated type, maybe an id, and either a value or child elements 
 * (one or the other, but not both or neither)
 * 
 * @author Grahame Grieve
 *
 */
public class Element extends Base {


  public enum SpecialElement {
		CONTAINED, BUNDLE_ENTRY, BUNDLE_OUTCOME, PARAMETER;

    public static SpecialElement fromProperty(Property property) {
      if (property.getStructure().getIdElement().getIdPart().equals("Parameters"))
        return PARAMETER;
      if (property.getStructure().getIdElement().getIdPart().equals("Bundle") && property.getName().equals("resource"))
        return BUNDLE_ENTRY;
      if (property.getStructure().getIdElement().getIdPart().equals("Bundle") && property.getName().equals("outcome"))
        return BUNDLE_OUTCOME;
      if (property.getName().equals("contained")) 
        return CONTAINED;
      throw new Error("Unknown resource containing a native resource: "+property.getDefinition().getId());
    }
	}

	private List<String> comments;// not relevant for production, but useful in documentation
	private String name;
	private String type;
	private String value;
	private int index = -1;
	private List<Element> children;
	private Property property;
  private Property elementProperty; // this is used when special is set to true - it tracks the underlying element property which is used in a few places
	private int line;
	private int col;
	private SpecialElement special;
	private XhtmlNode xhtml; // if this is populated, then value will also hold the string representation

	public Element(String name) {
		super();
		this.name = name;
	}

  public Element(Element other) {
    super();
    name = other.name;
    type = other.type;
    property = other.property;
    elementProperty = other.elementProperty;
    special = other.special;
  }
  
  public Element(String name, Property property) {
		super();
		this.name = name;
		this.property = property;
	}

	public Element(String name, Property property, String type, String value) {
		super();
		this.name = name;
		this.property = property;
		this.type = type;
		this.value = value;
	}

	public void updateProperty(Property property, SpecialElement special, Property elementProperty) {
		this.property = property;
    this.elementProperty = elementProperty;
		this.special = special;
	}

	public SpecialElement getSpecial() {
		return special;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		if (type == null)
			return property.getType(name);
		else
		  return type;
	}

	public String getValue() {
		return value;
	}

	public boolean hasChildren() {
		return !(children == null || children.isEmpty());
	}

	public List<Element> getChildren() {
		if (children == null)
			children = new ArrayList<Element>();
		return children;
	}

	public boolean hasComments() {
		return !(comments == null || comments.isEmpty());
	}

	public List<String> getComments() {
		if (comments == null)
			comments = new ArrayList<String>();
		return comments;
	}

	public Property getProperty() {
		return property;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setType(String type) {
		this.type = type;

	}

	public boolean hasValue() {
		return value != null;
	}

	public List<Element> getChildrenByName(String name) {
		List<Element> res = new ArrayList<Element>();
		if (hasChildren()) {
			for (Element child : children)
				if (name.equals(child.getName()))
					res.add(child);
		}
		return res;
	}

	public void numberChildren() {
		if (children == null)
			return;
		
		String last = "";
		int index = 0;
		for (Element child : children) {
			if (child.getProperty().isList()) {
			  if (last.equals(child.getName())) {
			  	index++;
			  } else {
			  	last = child.getName();
			  	index = 0;
			  }
		  	child.index = index;
			} else {
				child.index = -1;
			}
			child.numberChildren();
		}	
	}

	public int getIndex() {
		return index;
	}

	public boolean hasIndex() {
		return index > -1;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getChildValue(String name) {
		if (children == null)
			return null;
		for (Element child : children) {
			if (name.equals(child.getName()))
				return child.getValue();
		}
  	return null;
	}

  public void setChildValue(String name, String value) {
    if (children == null)
      children = new ArrayList<Element>();
    for (Element child : children) {
      if (name.equals(child.getName())) {
        if (!child.isPrimitive())
          throw new Error("Cannot set a value of a non-primitive type ("+name+" on "+this.getName()+")");
        child.setValue(value);
      }
    }
    try {
      setProperty(name.hashCode(), name, new StringType(value));
    } catch (FHIRException e) {
      throw new Error(e);
    }
  }

	public List<Element> getChildren(String name) {
		List<Element> res = new ArrayList<Element>(); 
		if (children != null)
		for (Element child : children) {
			if (name.equals(child.getName()))
				res.add(child);
		}
		return res;
	}

  public boolean hasType() {
    if (type == null)
      return property.hasType(name);
    else
      return true;
  }

  @Override
  public String fhirType() {
    return getType();
  }

  @Override
	public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
  	if (isPrimitive() && (hash == "value".hashCode()) && !Utilities.noString(value)) {
//  		String tn = getType();
//  		throw new Error(tn+" not done yet");
  	  Base[] b = new Base[1];
  	  b[0] = new StringType(value);
  	  return b;
  	}
  		
  	List<Base> result = new ArrayList<Base>();
  	if (children != null) {
  	for (Element child : children) {
  		if (child.getName().equals(name))
  			result.add(child);
  		if (child.getName().startsWith(name) && child.getProperty().isChoice() && child.getProperty().getName().equals(name+"[x]"))
  			result.add(child);
  	}
  	}
  	if (result.isEmpty() && checkValid) {
//  		throw new FHIRException("not determined yet");
  	}
  	return result.toArray(new Base[result.size()]);
	}

	@Override
	protected void listChildren(List<org.hl7.fhir.dstu3.model.Property> childProps) {
	  if (children != null) {
	    for (Element c : children) {
	      childProps.add(new org.hl7.fhir.dstu3.model.Property(c.getName(), c.fhirType(), c.getProperty().getDefinition().getDefinition(), c.getProperty().getDefinition().getMin(), maxToInt(c.getProperty().getDefinition().getMax()), c));
	    }
	  }
	}
	
  @Override
  public Base setProperty(int hash, String name, Base value) throws FHIRException {
    if (isPrimitive() && (hash == "value".hashCode())) {
      this.value = castToString(value).asStringValue();
      return this;
    }
    if ("xhtml".equals(getType()) && (hash == "value".hashCode())) {
      this.xhtml = castToXhtml(value);
      this.value =  castToXhtmlString(value);
      return this;
    }
    
    if (!value.isPrimitive() && !(value instanceof Element)) {
      if (isDataType(value)) 
        value = convertToElement(property.getChild(name), value);
      else
        throw new FHIRException("Cannot set property "+name+" on "+this.name+" - value is not a primitive type ("+value.fhirType()+") or an ElementModel type");
    }
    
    if (children == null)
      children = new ArrayList<Element>();
    Element childForValue = null;
    
    // look through existing children
    for (Element child : children) {
      if (child.getName().equals(name)) {
        if (!child.isList()) {
          childForValue = child;
          break;
        } else {
          Element ne = new Element(child);
          children.add(ne);
          numberChildren();
          childForValue = ne;
          break;
        }
      }
    }

    if (childForValue == null)
      for (Property p : property.getChildProperties(this.name, type)) {
        if (p.getName().equals(name) || p.getName().equals(name+"[x]")) {
          Element ne = new Element(name, p);
          children.add(ne);
          childForValue = ne;
          break;
        }
      }
    
    if (childForValue == null)
      throw new Error("Cannot set property "+name+" on "+this.name);
    else if (value.isPrimitive()) {
      if (childForValue.property.getName().endsWith("[x]"))
        childForValue.name = name+Utilities.capitalize(value.fhirType());
      childForValue.setValue(value.primitiveValue());
    } else {
      Element ve = (Element) value;
      childForValue.type = ve.getType();
      if (childForValue.property.getName().endsWith("[x]"))
        childForValue.name = name+Utilities.capitalize(childForValue.type);
      else if (value.isResource()) {
        if (childForValue.elementProperty == null)
          childForValue.elementProperty = childForValue.property;
        childForValue.property = ve.property;
        childForValue.special = SpecialElement.BUNDLE_ENTRY;
      }
      if (ve.children != null) {
        if (childForValue.children == null)
          childForValue.children = new ArrayList<Element>();
        else 
          childForValue.children.clear();
        childForValue.children.addAll(ve.children);
      }
    }
    return childForValue;
  }

  private Base convertToElement(Property prop, Base v) throws FHIRException {
    return new ObjectConverter(property.getContext()).convert(prop, (Type) v);
  }

  private boolean isDataType(Base v) {
    return v instanceof Type &&  property.getContext().getTypeNames().contains(v.fhirType());
  }

  @Override
  public Base makeProperty(int hash, String name) throws FHIRException {
    if (isPrimitive() && (hash == "value".hashCode())) {
      return new StringType(value);
    }

    if (children == null)
      children = new ArrayList<Element>();
    
    // look through existing children
    for (Element child : children) {
      if (child.getName().equals(name)) {
        if (!child.isList()) {
          return child;
        } else {
          Element ne = new Element(child);
          children.add(ne);
          numberChildren();
          return ne;
        }
      }
    }

    for (Property p : property.getChildProperties(this.name, type)) {
      if (p.getName().equals(name)) {
        Element ne = new Element(name, p);
        children.add(ne);
        return ne;
      }
    }
      
    throw new Error("Unrecognised name "+name+" on "+this.name); 
  }
  
	private int maxToInt(String max) {
    if (max.equals("*"))
      return Integer.MAX_VALUE;
    else
      return Integer.parseInt(max);
	}

	@Override
	public boolean isPrimitive() {
		return type != null ? property.isPrimitive(type) : property.isPrimitive(property.getType(name));
	}
	
  @Override
  public boolean isResource() {
    return property.isResource();
  }
  

	@Override
	public boolean hasPrimitiveValue() {
		return property.isPrimitiveName(name) || property.IsLogicalAndHasPrimitiveValue(name);
	}
	

	@Override
	public String primitiveValue() {
		if (isPrimitive())
		  return value;
		else {
			if (hasPrimitiveValue() && children != null) {
				for (Element c : children) {
					if (c.getName().equals("value"))
						return c.primitiveValue();
				}
			}
			return null;
		}
	}
	
	// for the validator
  public int line() {
    return line;
  }

  public int col() {
    return col;
  }

	public Element markLocation(int line, int col) {
		this.line = line;
		this.col = col;	
		return this;
	}

	public void markValidation(StructureDefinition profile, ElementDefinition definition) {
	}
	
  public Element getNamedChild(String name) {
	  if (children == null)
  		return null;
	  Element result = null;
	  for (Element child : children) {
	  	if (child.getName().equals(name)) {
	  		if (result == null)
	  			result = child;
	  		else 
	  			throw new Error("Attempt to read a single element when there is more than one present ("+name+")");
	  	}
	  }
	  return result;
	}

  public void getNamedChildren(String name, List<Element> list) {
  	if (children != null)
  		for (Element child : children) 
  			if (child.getName().equals(name))
  				list.add(child);
  }

  public String getNamedChildValue(String name) {
  	Element child = getNamedChild(name);
  	return child == null ? null : child.value;
  }

  public void getNamedChildrenWithWildcard(String string, List<Element> values) {
	  Validate.isTrue(string.endsWith("[x]"));
	  
	  String start = string.substring(0, string.length() - 3);
	  	if (children != null) {
	  		for (Element child : children) { 
	  			if (child.getName().startsWith(start)) {
	  				values.add(child);
	  			}
	  		}
	  	}
  }

  
	public XhtmlNode getXhtml() {
		return xhtml;
	}

	public Element setXhtml(XhtmlNode xhtml) {
		this.xhtml = xhtml;
		return this;
 	}

	@Override
	public boolean isEmpty() {
		if (isNotBlank(value)) {
			return false;
		}
		for (Element next : getChildren()) {
			if (!next.isEmpty()) {
				return false;
			}
		}
		return true;
	}

  public Property getElementProperty() {
    return elementProperty;
  }

  public boolean hasElementProperty() {
    return elementProperty != null;
  }

  public boolean hasChild(String name) {
    return getNamedChild(name) != null;
  }

  @Override
  public String toString() {
    return name+"="+fhirType() + "["+(children == null || hasValue() ? value : Integer.toString(children.size())+" children")+"]";
  }

  @Override
  public String getIdBase() {
    return getChildValue("id");
  }

  @Override
  public void setIdBase(String value) {
    setChildValue("id", value);
  }


//  @Override
//  public boolean equalsDeep(Base other) {
//    if (!super.equalsDeep(other))
//      return false;
//    
//  }
//
//  @Override
//  public boolean equalsShallow(Base other) {
//    if (!super.equalsShallow(other))
//      return false;
//  }

  public Type asType() throws FHIRException {
    return new ObjectConverter(property.getContext()).convertToType(this);
  }

  @Override
  public boolean isMetadataBased() {
    return true;
  }

  public boolean isList() {
    if (elementProperty != null)
      return elementProperty.isList();
    else
      return property.isList();
  }
  
  @Override
  public String[] getTypesForProperty(int hash, String name) throws FHIRException {
    Property p = property.getChildSimpleName(this.name, name);
    if (p != null) {
      Set<String> types = new HashSet<String>();
      for (TypeRefComponent tr : p.getDefinition().getType()) {
        types.add(tr.getCode());
      }
      return types.toArray(new String[]{});
    }
    return super.getTypesForProperty(hash, name);

  }

  public void sort() {
    if (children != null) {
      List<Element> remove = new ArrayList<Element>();
      for (Element child : children) {
        child.sort();
        if (child.isEmpty())
          remove.add(child);
      }
      children.removeAll(remove);
      Collections.sort(children, new ElementSortComparator(this, this.property));
    }
  }

  public class ElementSortComparator implements Comparator<Element> {
    private List<ElementDefinition> children;
    public ElementSortComparator(Element e, Property property) {
      String tn = e.getType();
      StructureDefinition sd = property.getContext().fetchResource(StructureDefinition.class, "http://hl7.org/fhir/StructureDefinition/"+tn);
      if (sd != null && !sd.getAbstract())
        children = sd.getSnapshot().getElement();
      else
        children = property.getStructure().getSnapshot().getElement();
    }
    
    @Override
    public int compare(Element e0, Element e1) {
      int i0 = find(e0);
      int i1 = find(e1);
      return (i0 < i1) ? -1 : ((i0 == i1) ? 0 : 1);
    }
    
    private int find(Element e0) {
      int i =  e0.elementProperty != null ? children.indexOf(e0.elementProperty.getDefinition()) :  children.indexOf(e0.property.getDefinition());
      return i; 
    }

  }

}
