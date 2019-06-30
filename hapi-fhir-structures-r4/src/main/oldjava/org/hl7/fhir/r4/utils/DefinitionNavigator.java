package org.hl7.fhir.r4.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hl7.fhir.r4.context.IWorkerContext;
import org.hl7.fhir.r4.model.ElementDefinition;
import org.hl7.fhir.r4.model.ElementDefinition.TypeRefComponent;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.exceptions.DefinitionException;

public class DefinitionNavigator {

  private IWorkerContext context;
  private StructureDefinition structure;
  private int index;
  private List<DefinitionNavigator> children;
  private List<DefinitionNavigator> typeChildren;
  private List<DefinitionNavigator> slices;
  private List<String> names = new ArrayList<String>();
  private TypeRefComponent typeOfChildren;
  private String path;
  
  public DefinitionNavigator(IWorkerContext context, StructureDefinition structure) throws DefinitionException {
    if (!structure.hasSnapshot())
      throw new DefinitionException("Snapshot required");
    this.context = context;
    this.structure = structure;
    this.index = 0;
    this.path = current().getPath();
    names.add(nameTail());
  }
  
  private DefinitionNavigator(IWorkerContext context, StructureDefinition structure, int index, String path, List<String> names, String type) {
    this.path = path;
    this.context = context;
    this.structure = structure;
    this.index = index;
    if (type == null)
      for (String name : names)
        this.names.add(name+"."+nameTail());
    else {
      this.names.addAll(names);
      this.names.add(type);
    }
  }
  
  /**
   * When you walk a tree, and you walk into a typed structure, an element can simultaineously 
   * be covered by multiple types at once. Take, for example, the string label for an identifer value.
   * It has the following paths:
   *   Patient.identifier.value.value
   *   Identifier.value.value
   *   String.value
   *   value
   * If you started in a bundle, the list might be even longer and deeper
   *   
   * Any of these names might be relevant. This function returns the names in an ordered list
   * in the order above  
   * @return
   */
  public List<String> getNames() {
    return names;
  }
  public ElementDefinition current() {
    return structure.getSnapshot().getElement().get(index);
  }
  
  public List<DefinitionNavigator> slices() throws DefinitionException {
    if (children == null) {
      loadChildren();
    }
    return slices;
  }
  
  public List<DefinitionNavigator> children() throws DefinitionException {
    if (children == null) {
      loadChildren();
    }
    return children;
  }

  private void loadChildren() throws DefinitionException {
    children = new ArrayList<DefinitionNavigator>();
    String prefix = current().getPath()+".";
    Map<String, DefinitionNavigator> nameMap = new HashMap<String, DefinitionNavigator>();

    for (int i = index + 1; i < structure.getSnapshot().getElement().size(); i++) {
      String path = structure.getSnapshot().getElement().get(i).getPath();
      if (path.startsWith(prefix) && !path.substring(prefix.length()).contains(".")) {
        DefinitionNavigator dn = new DefinitionNavigator(context, structure, i, this.path+"."+tail(path), names, null);
        
        if (nameMap.containsKey(path)) {
          DefinitionNavigator master = nameMap.get(path);
          if (!master.current().hasSlicing()) 
            throw new DefinitionException("Found slices with no slicing details at "+dn.current().getPath());
          if (master.slices == null) 
            master.slices = new ArrayList<DefinitionNavigator>();
          master.slices.add(dn);
        } else {
          nameMap.put(path, dn);
          children.add(dn);
        }
      } else if (path.length() < prefix.length())
        break;
    }
  }

  public String path() {
    return path;
  }
  
  private String tail(String p) {
    if (p.contains("."))
      return p.substring(p.lastIndexOf('.')+1);
    else
      return p;
  }

  public String nameTail() {
    return tail(path);
  }

  /**
   * if you have a typed element, the tree might end at that point.
   * And you may or may not want to walk into the tree of that type
   * It depends what you are doing. So this is a choice. You can 
   * ask for the children, and then, if you get no children, you 
   * can see if there are children defined for the type, and then 
   * get them
   * 
   * you have to provide a type if there's more than one type 
   * for current() since this library doesn't know how to choose
   * @throws DefinitionException 
   * @
   */
  public boolean hasTypeChildren(TypeRefComponent type) throws DefinitionException {
    if (typeChildren == null || typeOfChildren != type) {
      loadTypedChildren(type);
    }
    return !typeChildren.isEmpty();
  }

  private void loadTypedChildren(TypeRefComponent type) throws DefinitionException {
    typeOfChildren = null;
    StructureDefinition sd = context.fetchResource(StructureDefinition.class, /* GF#13465 : this somehow needs to be revisited type.hasProfile() ? type.getProfile() : */ type.getCode());
    if (sd != null) {
      DefinitionNavigator dn = new DefinitionNavigator(context, sd, 0, path, names, sd.getType());
      typeChildren = dn.children();
    } else
      throw new DefinitionException("Unable to find definition for "+type.getCode()+(type.hasProfile() ? "("+type.getProfile()+")" : ""));
    typeOfChildren = type;
  }

  /**
   * 
   * @return
   * @throws DefinitionException 
   * @
   */
  public List<DefinitionNavigator> childrenFromType(TypeRefComponent type) throws DefinitionException {
    if (typeChildren == null || typeOfChildren != type) {
      loadTypedChildren(type);
    }
    return typeChildren;
  }
  

}
