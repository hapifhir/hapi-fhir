package org.hl7.fhir.r4.utils;

import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r4.context.IWorkerContext;
import org.hl7.fhir.r4.model.ElementDefinition;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.StructureDefinition.StructureDefinitionKind;
import org.hl7.fhir.r4.model.StructureDefinition.TypeDerivationRule;
import org.hl7.fhir.utilities.Utilities;

public class ProtoBufGenerator {

  private IWorkerContext context;
  private StructureDefinition definition;
  private OutputStreamWriter destination;
  private int cursor;
  private Message message; 
  
  private class Field {
    private String name;   
    private boolean required;
    private boolean repeating;
    private String type;
  }
  
  private class Message {
    private String name;
    private List<Field> fields = new ArrayList<Field>();
    private List<Message> messages = new ArrayList<Message>();
    public Message(String name) {
      super();
      this.name = name;
    }
    
    
  }
  
  public ProtoBufGenerator(IWorkerContext context) {
    super();
    this.context = context;
  }

  public ProtoBufGenerator(IWorkerContext context, StructureDefinition definition, OutputStreamWriter destination) {
    super();
    this.context = context;
    this.definition = definition;
    this.destination = destination;
  }

  public IWorkerContext getContext() {
    return context;
  }
  
  public StructureDefinition getDefinition() {
    return definition;
  }

  public void setDefinition(StructureDefinition definition) {
    this.definition = definition;
  }

  public OutputStreamWriter getDestination() {
    return destination;
  }

  public void setDestination(OutputStreamWriter destination) {
    this.destination = destination;
  }


  public void build() throws FHIRException {
    if (definition == null)
      throw new FHIRException("A definition must be provided");
    if (destination == null)
      throw new FHIRException("A destination must be provided");
    
    if (definition.getDerivation() == TypeDerivationRule.CONSTRAINT)
      throw new FHIRException("derivation = constraint is not supported yet");
    
    message = new Message(definition.getSnapshot().getElement().get(0).getPath());
    cursor = 1;
    while (cursor < definition.getSnapshot().getElement().size()) {
      ElementDefinition ed = definition.getSnapshot().getElement().get(0);
      Field fld = new Field();
      fld.name = tail(ed.getPath());
      fld.required = (ed.getMin() == 1);
      fld.repeating = (!ed.getMax().equals("1"));
      message.fields.add(fld);
      if (ed.getType().size() != 1)
        fld.type = "Unknown";
      else {
        StructureDefinition td = context.fetchTypeDefinition(ed.getTypeFirstRep().getCode());
        if (td == null)
          fld.type = "Unresolved";
        else if (td.getKind() == StructureDefinitionKind.PRIMITIVETYPE) {
          fld.type = protoTypeForFhirType(ed.getTypeFirstRep().getCode());
          fld = new Field();
          fld.name = tail(ed.getPath())+"Extra";
          fld.repeating = (!ed.getMax().equals("1"));
          fld.type = "Primitive";
          message.fields.add(fld);
        } else
          fld.type = ed.getTypeFirstRep().getCode();
      }   
    }
  }

  private String protoTypeForFhirType(String code) {
    if (Utilities.existsInList(code, "integer", "unsignedInt", "positiveInt"))
      return "int23";
    else if (code.equals("boolean"))
      return "bool";
    else 
      return "string";
  }

  private String tail(String path) {
    return path.substring(path.lastIndexOf(".")+1);
  }
  
  
}
