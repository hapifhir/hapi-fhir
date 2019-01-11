package org.hl7.fhir.r4.utils;

// todo:
// - generate sort order parameters
// - generate inherited search parameters

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r4.conformance.ProfileUtilities;
import org.hl7.fhir.r4.context.IWorkerContext;
import org.hl7.fhir.r4.model.Constants;
import org.hl7.fhir.r4.model.ElementDefinition;
import org.hl7.fhir.r4.model.ElementDefinition.TypeRefComponent;
import org.hl7.fhir.r4.model.Enumerations.SearchParamType;
import org.hl7.fhir.r4.model.SearchParameter;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.StructureDefinition.StructureDefinitionKind;
import org.hl7.fhir.r4.model.StructureDefinition.TypeDerivationRule;
import org.hl7.fhir.utilities.Utilities;

public class GraphQLSchemaGenerator {

  public enum FHIROperationType {READ, SEARCH, CREATE, UPDATE, DELETE};
  
  private static final String INNER_TYPE_NAME = "gql.type.name";
  IWorkerContext context;

  public GraphQLSchemaGenerator(IWorkerContext context) {
    super();
    this.context = context;
  }
  
  public void generateTypes(OutputStream stream) throws IOException, FHIRException {
    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stream));
    
    Map<String, StructureDefinition> pl = new HashMap<String, StructureDefinition>();
    Map<String, StructureDefinition> tl = new HashMap<String, StructureDefinition>();
    for (StructureDefinition sd : context.allStructures()) {
      if (sd.getKind() == StructureDefinitionKind.PRIMITIVETYPE && sd.getDerivation() == TypeDerivationRule.SPECIALIZATION) {
        pl.put(sd.getName(), sd);
      }
      if (sd.getKind() == StructureDefinitionKind.COMPLEXTYPE && sd.getDerivation() == TypeDerivationRule.SPECIALIZATION) {
        tl.put(sd.getName(), sd);
      }
    }
    writer.write("# FHIR GraphQL Schema. Version "+Constants.VERSION+"\r\n\r\n");
    writer.write("# FHIR Defined Primitive types\r\n");
    for (String n : sorted(pl.keySet()))
      generatePrimitive(writer, pl.get(n));
    writer.write("\r\n");
    writer.write("# FHIR Defined Search Parameter Types\r\n");
    for (SearchParamType dir : SearchParamType.values()) {
      if (dir != SearchParamType.NULL)
        generateSearchParamType(writer, dir.toCode());      
    }
    writer.write("\r\n");
    generateElementBase(writer);
    for (String n : sorted(tl.keySet()))
      generateType(writer, tl.get(n));
    writer.flush();
    writer.close();
  }

  public void generateResource(OutputStream stream, StructureDefinition sd, List<SearchParameter> parameters, EnumSet<FHIROperationType> operations) throws IOException, FHIRException {
    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stream));
    writer.write("# FHIR GraphQL Schema. Version "+Constants.VERSION+"\r\n\r\n");
    generateType(writer, sd);
    if (operations.contains(FHIROperationType.READ))
      generateIdAccess(writer, sd.getName());
    if (operations.contains(FHIROperationType.SEARCH)) {
      generateListAccess(writer, parameters, sd.getName());
      generateConnectionAccess(writer, parameters, sd.getName());
    }
    if (operations.contains(FHIROperationType.CREATE))
      generateCreate(writer, sd.getName());
    if (operations.contains(FHIROperationType.UPDATE))
      generateUpdate(writer, sd.getName());
    if (operations.contains(FHIROperationType.DELETE))
      generateDelete(writer, sd.getName());
    writer.flush();
    writer.close();
  }

  private void generateCreate(BufferedWriter writer, String name) throws IOException {
    writer.write("type "+name+"CreateType {\r\n");
    writer.write("  "+name+"Create(");
    param(writer, "resource", name, false, false);
    writer.write(") : "+name+"Creation\r\n");
    writer.write("}\r\n");
    writer.write("\r\n");    
    writer.write("type "+name+"Creation {\r\n");
    writer.write("  location : String\r\n");
    writer.write("  resource : "+name+"\r\n");
    writer.write("  information : OperationOutcome\r\n");
    writer.write("}\r\n");
    writer.write("\r\n");    
  }

  private void generateUpdate(BufferedWriter writer, String name) throws IOException {
    writer.write("type "+name+"UpdateType {\r\n");
    writer.write("  "+name+"Update(");
    param(writer, "id", "ID", false, false);
    param(writer, "resource", name, false, false);
    writer.write(") : "+name+"Update\r\n");
    writer.write("}\r\n");
    writer.write("\r\n");    
    writer.write("type "+name+"Update {\r\n");
    writer.write("  resource : "+name+"\r\n");
    writer.write("  information : OperationOutcome\r\n");
    writer.write("}\r\n");
    writer.write("\r\n");    
  }

  private void generateDelete(BufferedWriter writer, String name) throws IOException {
    writer.write("type "+name+"DeleteType {\r\n");
    writer.write("  "+name+"Delete(");
    param(writer, "id", "ID", false, false);
    writer.write(") : "+name+"Delete\r\n");
    writer.write("}\r\n");
    writer.write("\r\n");    
    writer.write("type "+name+"Delete {\r\n");
    writer.write("  information : OperationOutcome\r\n");
    writer.write("}\r\n");
    writer.write("\r\n");    
  }

  private void generateListAccess(BufferedWriter writer, List<SearchParameter> parameters, String name) throws IOException {
    writer.write("type "+name+"ListType {\r\n");
    writer.write("  "+name+"List(");
    param(writer, "_filter", "String", false, false);
    for (SearchParameter sp : parameters)
      param(writer, sp.getName().replace("-", "_"), getGqlname(sp.getType().toCode()), true, true);
    param(writer, "_sort", "String", false, true);
    param(writer, "_count", "Int", false, true);
    param(writer, "_cursor", "String", false, true);
    writer.write(") : ["+name+"]\r\n");
    writer.write("}\r\n");
    writer.write("\r\n");    
  }

  private void param(BufferedWriter writer, String name, String type, boolean list, boolean line) throws IOException {
    if (line)
      writer.write("\r\n    ");
    writer.write(name);
    writer.write(" : ");
    if (list)
      writer.write("[");
    writer.write(type);      
    if (list)
      writer.write("]");
  }

  private void generateConnectionAccess(BufferedWriter writer, List<SearchParameter> parameters, String name) throws IOException {
    writer.write("type "+name+"ConnectionType {\r\n");
    writer.write("  "+name+"Conection(");
    param(writer, "_filter", "String", false, false);
    for (SearchParameter sp : parameters)
      param(writer, sp.getName().replace("-", "_"), getGqlname(sp.getType().toCode()), true, true);
    param(writer, "_sort", "String", false, true);
    param(writer, "_count", "Int", false, true);
    param(writer, "_cursor", "String", false, true);
    writer.write(") : "+name+"Connection\r\n");
    writer.write("}\r\n");
    writer.write("\r\n");    
    writer.write("type "+name+"Connection {\r\n");
    writer.write("  count : Int\r\n");
    writer.write("  offset : Int\r\n");
    writer.write("  pagesize : Int\r\n");
    writer.write("  first : ID\r\n");
    writer.write("  previous : ID\r\n");
    writer.write("  next : ID\r\n");
    writer.write("  last : ID\r\n");
    writer.write("  edges : ["+name+"Edge]\r\n");
    writer.write("}\r\n");
    writer.write("\r\n");    
    writer.write("type "+name+"Edge {\r\n");
    writer.write("  mode : String\r\n");
    writer.write("  score : Float\r\n");
    writer.write("  resource : "+name+"\r\n");
    writer.write("}\r\n");
    writer.write("\r\n");    
  }

  
  private void generateIdAccess(BufferedWriter writer, String name) throws IOException {
    writer.write("type "+name+"ReadType {\r\n");
    writer.write("  "+name+"(id : ID!) : "+name+"\r\n");
    writer.write("}\r\n");
    writer.write("\r\n");    
  }

  private void generateElementBase(BufferedWriter writer) throws IOException {
    writer.write("type ElementBase {\r\n");
    writer.write("  id : ID\r\n");
    writer.write("  extension : [Extension]{\r\n");
    writer.write("}\r\n");
    writer.write("\r\n");
    
  }

  private void generateType(BufferedWriter writer, StructureDefinition sd) throws IOException {
    if (sd.getAbstract())
      return;
    
    List<StringBuilder> list = new ArrayList<StringBuilder>();
    StringBuilder b = new StringBuilder();
    list.add(b);
    b.append("type ");
    b.append(sd.getName());
    b.append(" {\r\n");
    ElementDefinition ed = sd.getSnapshot().getElementFirstRep();
    generateProperties(list, b, sd.getName(), sd, ed, "type");
    b.append("}");
    b.append("\r\n");
    b.append("\r\n");
    for (StringBuilder bs : list)
      writer.write(bs.toString());
    list.clear();
    b = new StringBuilder();
    list.add(b);
    b.append("input ");
    b.append(sd.getName());
    b.append(" {\r\n");
    ed = sd.getSnapshot().getElementFirstRep();
    generateProperties(list, b, sd.getName(), sd, ed, "input");
    b.append("}");
    b.append("\r\n");
    b.append("\r\n");
    for (StringBuilder bs : list)
      writer.write(bs.toString());
  }

  private void generateProperties(List<StringBuilder> list, StringBuilder b, String typeName, StructureDefinition sd, ElementDefinition ed, String mode) throws IOException {
    List<ElementDefinition> children = ProfileUtilities.getChildList(sd, ed);
    for (ElementDefinition child : children) {
      if (child.hasContentReference()) {
        ElementDefinition ref = resolveContentReference(sd, child.getContentReference());        
        generateProperty(list, b, typeName, sd, child, ref.getType().get(0), false, ref, mode);
      } else if (child.getType().size() == 1) {
        generateProperty(list, b, typeName, sd, child, child.getType().get(0), false, null, mode);
      } else {
        boolean ref  = false;
        for (TypeRefComponent t : child.getType()) {
          if (!t.hasTarget())
            generateProperty(list, b, typeName, sd, child, t, true, null, mode);
          else if (!ref) {
            ref = true;
            generateProperty(list, b, typeName, sd, child, t, true, null, mode);
          }
        }
      }
    }
  }

  private ElementDefinition resolveContentReference(StructureDefinition sd, String contentReference) {
    String id = contentReference.substring(1);
    for (ElementDefinition ed : sd.getSnapshot().getElement()) {
      if (id.equals(ed.getId()))
        return ed;
    }
    throw new Error("Unable to find "+id);
  }

  private void generateProperty(List<StringBuilder> list, StringBuilder b, String typeName, StructureDefinition sd, ElementDefinition child, TypeRefComponent typeDetails, boolean suffix, ElementDefinition cr, String mode) throws IOException {
    if (isPrimitive(typeDetails)) {
      String n = getGqlname(typeDetails.getCode()); 
      b.append("  ");
      b.append(tail(child.getPath(), suffix));
      if (suffix)
        b.append(Utilities.capitalize(typeDetails.getCode()));
      b.append(": ");
      b.append(n);
      if (!child.getPath().endsWith(".id")) {
        b.append("  _");
        b.append(tail(child.getPath(), suffix));
        if (suffix)
          b.append(Utilities.capitalize(typeDetails.getCode()));
        if (!child.getMax().equals("1"))
          b.append(": [ElementBase]\r\n");
        else
          b.append(": ElementBase\r\n");
      } else
        b.append("\r\n");
    } else {
      b.append("  ");
      b.append(tail(child.getPath(), suffix));
      if (suffix)
        b.append(Utilities.capitalize(typeDetails.getCode()));
      b.append(": ");
      if (!child.getMax().equals("1"))
        b.append("[");
      String type = typeDetails.getCode();
      if (cr != null)
        b.append(generateInnerType(list, sd, typeName, cr, mode));
      else if (Utilities.existsInList(type, "Element", "BackboneElement"))
        b.append(generateInnerType(list, sd, typeName, child, mode));
      else
        b.append(type);
      if (!child.getMax().equals("1"))
        b.append("]");
      if (child.getMin() != 0 && !suffix)
        b.append("!");
      b.append("\r\n");
    }
  }

  private String generateInnerType(List<StringBuilder> list, StructureDefinition sd, String name, ElementDefinition child, String mode) throws IOException {
    if (child.hasUserData(INNER_TYPE_NAME+"."+mode))
      return child.getUserString(INNER_TYPE_NAME+"."+mode);
    
    String typeName = name+Utilities.capitalize(tail(child.getPath(), false));
    child.setUserData(INNER_TYPE_NAME+"."+mode, typeName);
    StringBuilder b = new StringBuilder();
    list.add(b);
    b.append(mode);
    b.append(" ");
    b.append(typeName);
    b.append(" {\r\n");
    generateProperties(list, b, typeName, sd, child, mode);
    b.append("}");
    b.append("\r\n");
    b.append("\r\n");
    return typeName;
  }

  private String tail(String path, boolean suffix) {
    if (suffix)
      path = path.substring(0, path.length()-3);
    int i = path.lastIndexOf(".");
    return i < 0 ? path : path.substring(i + 1);
  }

  private boolean isPrimitive(TypeRefComponent type) {
    String typeName = type.getCode();
    StructureDefinition sd = context.fetchTypeDefinition(typeName);
    if (sd == null)
      return false;
    return sd.getKind() == StructureDefinitionKind.PRIMITIVETYPE;
  }

  private List<String> sorted(Set<String> keys) {
    List<String> sl = new ArrayList<>();
    sl.addAll(keys);
    Collections.sort(sl);
    return sl;
  }

  private void generatePrimitive(BufferedWriter writer, StructureDefinition sd) throws IOException, FHIRException {
    String gqlName = getGqlname(sd.getName());
    if (gqlName.equals(sd.getName())) { 
      writer.write("Scalar ");
      writer.write(sd.getName());
      writer.write(" # JSON Format: ");
      writer.write(getJsonFormat(sd));
    } else  {
      writer.write("# Type ");
      writer.write(sd.getName());
      writer.write(": use GraphQL Scalar type ");
      writer.write(gqlName);
    }
    writer.write("\r\n");
  }

  private void generateSearchParamType(BufferedWriter writer, String name) throws IOException, FHIRException {
    String gqlName = getGqlname(name);
    if (gqlName.equals(name)) { 
      writer.write("Scalar ");
      writer.write(name);
      writer.write(" # JSON Format: String");
    } else  {
      writer.write("# Search Param ");
      writer.write(name);
      writer.write(": use GraphQL Scalar type ");
      writer.write(gqlName);
    }
    writer.write("\r\n");
  }
  
  private String getJsonFormat(StructureDefinition sd) throws FHIRException {
    for (ElementDefinition ed : sd.getSnapshot().getElement()) {
      if (!ed.getType().isEmpty() &&  ed.getType().get(0).getCodeElement().hasExtension("http://hl7.org/fhir/StructureDefinition/structuredefinition-json-type"))
        return ed.getType().get(0).getCodeElement().getExtensionString("http://hl7.org/fhir/StructureDefinition/structuredefinition-json-type");
    }
    return "??";
  }

  private String getGqlname(String name) {
    if (name.equals("string"))
      return "String";
    if (name.equals("integer"))
      return "Int";
    if (name.equals("boolean"))
      return "Boolean";
    if (name.equals("id"))
      return "ID";    
    return name;
  }
}
