package org.hl7.fhir.r4.elementmodel;

import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.r4.conformance.ProfileUtilities;
import org.hl7.fhir.r4.context.IWorkerContext;
import org.hl7.fhir.r4.formats.FormatUtilities;
import org.hl7.fhir.r4.model.ElementDefinition;
import org.hl7.fhir.r4.model.ElementDefinition.PropertyRepresentation;
import org.hl7.fhir.r4.model.ElementDefinition.TypeRefComponent;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.StructureDefinition.StructureDefinitionKind;
import org.hl7.fhir.r4.model.TypeDetails;
import org.hl7.fhir.r4.utils.ToolingExtensions;
import org.hl7.fhir.r4.utils.TypesUtilities;
import org.hl7.fhir.utilities.Utilities;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.exceptions.DefinitionException;
import org.hl7.fhir.exceptions.FHIRException;

public class Property {

	private IWorkerContext context;
	private ElementDefinition definition;
	private StructureDefinition structure;
	private Boolean canBePrimitive; 

	public Property(IWorkerContext context, ElementDefinition definition, StructureDefinition structure) {
		this.context = context;
		this.definition = definition;
		this.structure = structure;
	}

	public String getName() {
		return definition.getPath().substring(definition.getPath().lastIndexOf(".")+1);
	}

	public ElementDefinition getDefinition() {
		return definition;
	}

	public String getType() {
		if (definition.getType().size() == 0)
			return null;
		else if (definition.getType().size() > 1) {
			String tn = definition.getType().get(0).getCode();
			for (int i = 1; i < definition.getType().size(); i++) {
				if (!tn.equals(definition.getType().get(i).getCode()))
					throw new Error("logic error, gettype when types > 1");
			}
			return tn;
		} else
			return definition.getType().get(0).getCode();
	}

	public String getType(String elementName) {
    if (!definition.getPath().contains("."))
      return definition.getPath();
    ElementDefinition ed = definition;
    if (definition.hasContentReference()) {
      if (!definition.getContentReference().startsWith("#"))
        throw new Error("not handled yet");
      boolean found = false;
      for (ElementDefinition d : structure.getSnapshot().getElement()) {
        if (d.hasId() && d.getId().equals(definition.getContentReference().substring(1))) {
          found = true;
          ed = d;
        }
      }
      if (!found)
        throw new Error("Unable to resolve "+definition.getContentReference()+" at "+definition.getPath()+" on "+structure.getUrl());
    }
    if (ed.getType().size() == 0)
			return null;
    else if (ed.getType().size() > 1) {
      String t = ed.getType().get(0).getCode();
			boolean all = true;
      for (TypeRefComponent tr : ed.getType()) {
				if (!t.equals(tr.getCode()))
					all = false;
			}
			if (all)
				return t;
      String tail = ed.getPath().substring(ed.getPath().lastIndexOf(".")+1);
      if (tail.endsWith("[x]") && elementName != null && elementName.startsWith(tail.substring(0, tail.length()-3))) {
				String name = elementName.substring(tail.length()-3);
        return isPrimitive(lowFirst(name)) ? lowFirst(name) : name;        
			} else
        throw new Error("logic error, gettype when types > 1, name mismatch for "+elementName+" on at "+ed.getPath());
    } else if (ed.getType().get(0).getCode() == null) {
      if (Utilities.existsInList(ed.getId(), "Element.id", "Extension.url"))
        return "string";
      else
        return structure.getId();
		} else
      return ed.getType().get(0).getCode();
	}

  public boolean hasType(String elementName) {
    if (definition.getType().size() == 0)
      return false;
    else if (definition.getType().size() > 1) {
      String t = definition.getType().get(0).getCode();
      boolean all = true;
      for (TypeRefComponent tr : definition.getType()) {
        if (!t.equals(tr.getCode()))
          all = false;
      }
      if (all)
        return true;
      String tail = definition.getPath().substring(definition.getPath().lastIndexOf(".")+1);
      if (tail.endsWith("[x]") && elementName.startsWith(tail.substring(0, tail.length()-3))) {
        String name = elementName.substring(tail.length()-3);
        return true;        
      } else
        return false;
    } else
      return true;
  }

	public StructureDefinition getStructure() {
		return structure;
	}

	/**
	 * Is the given name a primitive
	 * 
	 * @param E.g. "Observation.status"
	 */
	public boolean isPrimitiveName(String name) {
	  String code = getType(name);
      return isPrimitive(code);
	}

	/**
	 * Is the given type a primitive
	 * 
	 * @param E.g. "integer"
	 */
	public boolean isPrimitive(String code) {
	  return TypesUtilities.isPrimitive(code);
	 // was this... but this can be very inefficient compared to hard coding the list
//		StructureDefinition sd = context.fetchTypeDefinition(code);
//      return sd != null && sd.getKind() == StructureDefinitionKind.PRIMITIVETYPE;
	}

	private String lowFirst(String t) {
		return t.substring(0, 1).toLowerCase()+t.substring(1);
	}

	public boolean isResource() {
	  if (definition.getType().size() > 0)
	    return definition.getType().size() == 1 && ("Resource".equals(definition.getType().get(0).getCode()) || "DomainResource".equals(definition.getType().get(0).getCode()));
	  else
	    return !definition.getPath().contains(".") && structure.getKind() == StructureDefinitionKind.RESOURCE;
	}

	public boolean isList() {
	  return !"1".equals(definition.getMax());
	}

  public String getScopedPropertyName() {
    return definition.getBase().getPath();
  }

  public String getNamespace() {
    if (ToolingExtensions.hasExtension(definition, "http://hl7.org/fhir/StructureDefinition/elementdefinition-namespace"))
      return ToolingExtensions.readStringExtension(definition, "http://hl7.org/fhir/StructureDefinition/elementdefinition-namespace");
    if (ToolingExtensions.hasExtension(structure, "http://hl7.org/fhir/StructureDefinition/elementdefinition-namespace"))
      return ToolingExtensions.readStringExtension(structure, "http://hl7.org/fhir/StructureDefinition/elementdefinition-namespace");
    return FormatUtilities.FHIR_NS;
  }

  private boolean isElementWithOnlyExtension(final ElementDefinition ed, final List<ElementDefinition> children) {
    boolean result = false;
    if (!ed.getType().isEmpty()) {
      result = true;
      for (final ElementDefinition ele : children) {
        if (!ele.getPath().contains("extension")) {
          result = false;
          break;
        }
      }
    }
    return result;
  }
  
	public boolean IsLogicalAndHasPrimitiveValue(String name) {
//		if (canBePrimitive!= null)
//			return canBePrimitive;
		
		canBePrimitive = false;
  	if (structure.getKind() != StructureDefinitionKind.LOGICAL)
  		return false;
  	if (!hasType(name))
  		return false;
  	StructureDefinition sd = context.fetchResource(StructureDefinition.class, structure.getUrl().substring(0, structure.getUrl().lastIndexOf("/")+1)+getType(name));
  	if (sd == null)
  	  sd = context.fetchResource(StructureDefinition.class, ProfileUtilities.sdNs(getType(name), context.getOverrideVersionNs()));
    if (sd != null && sd.getKind() == StructureDefinitionKind.PRIMITIVETYPE)
      return true;
  	if (sd == null || sd.getKind() != StructureDefinitionKind.LOGICAL)
  		return false;
  	for (ElementDefinition ed : sd.getSnapshot().getElement()) {
  		if (ed.getPath().equals(sd.getId()+".value") && ed.getType().size() == 1 && isPrimitive(ed.getType().get(0).getCode())) {
  			canBePrimitive = true;
  			return true;
  		}
  	}
  	return false;
	}

  public boolean isChoice() {
    if (definition.getType().size() <= 1)
      return false;
    String tn = definition.getType().get(0).getCode();
    for (int i = 1; i < definition.getType().size(); i++) 
      if (!definition.getType().get(i).getCode().equals(tn))
        return true;
    return false;
  }


  protected List<Property> getChildProperties(String elementName, String statedType) throws FHIRException {
    ElementDefinition ed = definition;
    StructureDefinition sd = structure;
    List<ElementDefinition> children = ProfileUtilities.getChildMap(sd, ed);
    String url = null;
    if (children.isEmpty() || isElementWithOnlyExtension(ed, children)) {
      // ok, find the right definitions
      String t = null;
      if (ed.getType().size() == 1)
        t = ed.getType().get(0).getCode();
      else if (ed.getType().size() == 0)
        throw new Error("types == 0, and no children found on "+getDefinition().getPath());
      else {
        t = ed.getType().get(0).getCode();
        boolean all = true;
        for (TypeRefComponent tr : ed.getType()) {
          if (!tr.getCode().equals(t)) {
            all = false;
            break;
          }
        }
        if (!all) {
          // ok, it's polymorphic
          if (ed.hasRepresentation(PropertyRepresentation.TYPEATTR)) {
            t = statedType;
            if (t == null && ToolingExtensions.hasExtension(ed, "http://hl7.org/fhir/StructureDefinition/elementdefinition-defaulttype"))
              t = ToolingExtensions.readStringExtension(ed, "http://hl7.org/fhir/StructureDefinition/elementdefinition-defaulttype");
            boolean ok = false;
            for (TypeRefComponent tr : ed.getType()) { 
              if (tr.getCode().equals(t)) 
                ok = true;
              if (Utilities.isAbsoluteUrl(tr.getCode())) {
                StructureDefinition sdt = context.fetchResource(StructureDefinition.class, tr.getCode());
                if (sdt != null && sdt.getType().equals(t)) {
                  url = tr.getCode();
                  ok = true;
                }
              }
              if (ok)
                break;
            }
             if (!ok)
               throw new DefinitionException("Type '"+t+"' is not an acceptable type for '"+elementName+"' on property "+definition.getPath());
            
          } else {
            t = elementName.substring(tail(ed.getPath()).length() - 3);
            if (isPrimitive(lowFirst(t)))
              t = lowFirst(t);
          }
        }
      }
      if (!"xhtml".equals(t)) {
        for (TypeRefComponent aType: ed.getType()) {
          if (aType.getCode().equals(t)) {
            if (aType.hasProfile()) {
              assert aType.getProfile().size() == 1; 
              url = aType.getProfile().get(0).getValue();
            } else {
              url = ProfileUtilities.sdNs(t, context.getOverrideVersionNs());
            }
            break;
          }
        }
        if (url==null)
          throw new FHIRException("Unable to find type " + t + " for element " + elementName + " with path " + ed.getPath());
        sd = context.fetchResource(StructureDefinition.class, url);        
        if (sd == null)
          throw new DefinitionException("Unable to find type '"+t+"' for name '"+elementName+"' on property "+definition.getPath());
        children = ProfileUtilities.getChildMap(sd, sd.getSnapshot().getElement().get(0));
      }
    }
    List<Property> properties = new ArrayList<Property>();
    for (ElementDefinition child : children) {
      properties.add(new Property(context, child, sd));
    }
    return properties;
  }

  protected List<Property> getChildProperties(TypeDetails type) throws DefinitionException {
    ElementDefinition ed = definition;
    StructureDefinition sd = structure;
    List<ElementDefinition> children = ProfileUtilities.getChildMap(sd, ed);
    if (children.isEmpty()) {
      // ok, find the right definitions
      String t = null;
      if (ed.getType().size() == 1)
        t = ed.getType().get(0).getCode();
      else if (ed.getType().size() == 0)
        throw new Error("types == 0, and no children found");
      else {
        t = ed.getType().get(0).getCode();
        boolean all = true;
        for (TypeRefComponent tr : ed.getType()) {
          if (!tr.getCode().equals(t)) {
            all = false;
            break;
          }
        }
        if (!all) {
          // ok, it's polymorphic
          t = type.getType();
        }
      }
      if (!"xhtml".equals(t)) {
        sd = context.fetchResource(StructureDefinition.class, t);
        if (sd == null)
          throw new DefinitionException("Unable to find class '"+t+"' for name '"+ed.getPath()+"' on property "+definition.getPath());
        children = ProfileUtilities.getChildMap(sd, sd.getSnapshot().getElement().get(0));
      }
    }
    List<Property> properties = new ArrayList<Property>();
    for (ElementDefinition child : children) {
      properties.add(new Property(context, child, sd));
    }
    return properties;
  }

  private String tail(String path) {
    return path.contains(".") ? path.substring(path.lastIndexOf(".")+1) : path;
  }

  public Property getChild(String elementName, String childName) throws FHIRException {
    List<Property> children = getChildProperties(elementName, null);
    for (Property p : children) {
      if (p.getName().equals(childName)) {
        return p;
      }
    }
    return null;
  }

  public Property getChild(String name, TypeDetails type) throws DefinitionException {
    List<Property> children = getChildProperties(type);
    for (Property p : children) {
      if (p.getName().equals(name) || p.getName().equals(name+"[x]")) {
        return p;
      }
    }
    return null;
  }

  public Property getChild(String name) throws FHIRException {
    List<Property> children = getChildProperties(name, null);
    for (Property p : children) {
      if (p.getName().equals(name)) {
        return p;
      }
    }
    return null;
  }

  public Property getChildSimpleName(String elementName, String name) throws FHIRException {
    List<Property> children = getChildProperties(elementName, null);
    for (Property p : children) {
      if (p.getName().equals(name) || p.getName().equals(name+"[x]")) {
        return p;
      }
    }
    return null;
  }

  public IWorkerContext getContext() {
    return context;
  }

  @Override
  public String toString() {
    return definition.getPath();
  }


}
