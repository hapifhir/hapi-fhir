package org.hl7.fhir.r4.utils.formats;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.hl7.fhir.r4.formats.IParser.OutputStyle;
import org.hl7.fhir.r4.formats.JsonParser;
import org.hl7.fhir.r4.formats.XmlParser;
import org.hl7.fhir.r4.model.CanonicalType;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.ElementDefinition;
import org.hl7.fhir.r4.model.ElementDefinition.ElementDefinitionConstraintComponent;
import org.hl7.fhir.r4.model.ElementDefinition.ElementDefinitionMappingComponent;
import org.hl7.fhir.r4.model.ElementDefinition.ElementDefinitionSlicingDiscriminatorComponent;
import org.hl7.fhir.r4.model.ElementDefinition.TypeRefComponent;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.StructureDefinition.StructureDefinitionMappingComponent;
import org.hl7.fhir.r4.model.Type;
import org.hl7.fhir.r4.model.UriType;
import org.hl7.fhir.utilities.TextStreamWriter;


public class CSVWriter  extends TextStreamWriter  {

  private StructureDefinition def;
  private List<StructureDefinitionMappingComponent> mapKeys = new ArrayList<StructureDefinitionMappingComponent>();
  private List<CSVLine> lines = new ArrayList<CSVLine>();
  private XmlParser xml = new XmlParser();
  private JsonParser json = new JsonParser();
  private boolean asXml;

  private class CSVLine {
    private String line = "";
    
    public void addString(String s) {
      line = line + (line.equals("") ? "":",") + "\"" + csvEscape(s) + "\"";
    }
    
    public void addString(StringType s) {
      addString(s==null? "" : s.getValue());
    }

    public void addValue(String s) {
      line = line + (line.equals("") ? "":",") + s;
    }
    
    public void addValue(int s) {
      line = line + (line.equals("") ? "":",") + s;
    }

    public void addBoolean(boolean b) {
      addValue(b ? "Y" : "");
    }

    protected String csvEscape(String s) {
      if (s==null)
        return "";
      else if (s.contains("\""))
    	  return s.substring(0,s.indexOf("\"")) + "\"\"" + csvEscape(s.substring(s.indexOf("\"")+1));
      else
        return s;
    }
    
    public String toString() {
      return line;
    }
  }

  public CSVWriter(OutputStream out, StructureDefinition def, boolean asXml) throws UnsupportedEncodingException {
    super(out);
    this.asXml = asXml;
    this.def = def;
    CSVLine header = new CSVLine();
    lines.add(header);
    header.addString("Path");                 //A
    header.addString("Slice Name");           //B
    header.addString("Alias(s)");             //C
    header.addString("Label");                //D
    header.addString("Min");                  //E
    header.addString("Max");                  //F
    header.addString("Must Support?");        //G
    header.addString("Is Modifier?");         //H
    header.addString("Is Summary?");          //I
    header.addString("Type(s)");              //J
    header.addString("Short");                //K
    header.addString("Definition");           //L
    header.addString("Comments");             //M
    header.addString("Requirements");         //N
    header.addString("Default Value");        //O
    header.addString("Meaning When Missing"); //P
    header.addString("Fixed Value");          //Q
    header.addString("Pattern");              //R
    header.addString("Example");              //S
    header.addString("Minimum Value");        //T
    header.addString("Maximum Value");        //U
    header.addString("Maximum Length");       //V
    header.addString("Binding Strength");     //W
    header.addString("Binding Description");  //X
    header.addString("Binding Value Set");    //Y
    header.addString("Code");                 //Z
    header.addString("Slicing Discriminator");//AA
    header.addString("Slicing Description");  //AB
    header.addString("Slicing Ordered");      //AC
    header.addString("Slicing Rules");        //AD
    header.addString("Base Path");            //AE
    header.addString("Base Min");             //AF
    header.addString("Base Max");             //AG
    header.addString("Condition(s)");         //AH
    header.addString("Constraint(s)");        //AI
    for (StructureDefinitionMappingComponent map : def.getMapping()) {
      header.addString("Mapping: " + map.getName());
    }
  }
  
/*  private void findMapKeys(StructureDefinition def, List<StructureDefinitionMappingComponent> maps, IWorkerContext context) {
  	maps.addAll(def.getMapping());
  	if (def.getBaseDefinition()!=null) {
  	  StructureDefinition base = context.fetchResource(StructureDefinition.class, def.getBaseDefinition());
  	  findMapKeys(base, maps, context);
  	}
  }*/

  public void processElement(ElementDefinition ed) throws Exception {
    CSVLine line = new CSVLine();
    lines.add(line);
    line.addString(ed.getPath());
    line.addString(ed.getSliceName());
    line.addString(itemList(ed.getAlias()));
    line.addString(ed.getLabel());
    line.addValue(ed.getMin());
    line.addValue(ed.getMax());
    line.addString(ed.getMustSupport() ? "Y" : "");
    line.addString(ed.getIsModifier() ? "Y" : "");
    line.addString(ed.getIsSummary() ? "Y" : "");
    line.addString(itemList(ed.getType()));
    line.addString(ed.getShort());
    line.addString(ed.getDefinition());
    line.addString(ed.getComment());
    line.addString(ed.getRequirements());
    line.addString(ed.getDefaultValue()!=null ? renderType(ed.getDefaultValue()) : "");
    line.addString(ed.getMeaningWhenMissing());
    line.addString(ed.hasFixed() ? renderType(ed.getFixed()) : "");
    line.addString(ed.hasPattern() ? renderType(ed.getPattern()) : "");
    line.addString(ed.hasExample() ? renderType(ed.getExample().get(0).getValue()) : ""); // todo...?
    line.addString(ed.hasMinValue() ? renderType(ed.getMinValue()) : "");
    line.addString(ed.hasMaxValue() ? renderType(ed.getMaxValue()) : "");
    line.addValue((ed.hasMaxLength() ? Integer.toString(ed.getMaxLength()) : ""));
    if (ed.hasBinding()) {
      line.addString(ed.getBinding().getStrength()!=null ? ed.getBinding().getStrength().toCode() : "");
      line.addString(ed.getBinding().getDescription());
      if (ed.getBinding().getValueSet()==null)
        line.addString("");
      else
        line.addString(ed.getBinding().getValueSet());
    } else {
      line.addValue("");
      line.addValue("");
      line.addValue("");
    }
    line.addString(itemList(ed.getCode()));
    if (ed.hasSlicing()) {
      line.addString(itemList(ed.getSlicing().getDiscriminator()));
      line.addString(ed.getSlicing().getDescription());
      line.addBoolean(ed.getSlicing().getOrdered());
      line.addString(ed.getSlicing().getRules()!=null ? ed.getSlicing().getRules().toCode() : "");
    } else {
      line.addValue("");
      line.addValue("");
      line.addValue("");      
    }
    if (ed.getBase()!=null) {
      line.addString(ed.getBase().getPath());
      line.addValue(ed.getBase().getMin());
      line.addValue(ed.getBase().getMax());
    } else {
      line.addValue("");
      line.addValue("");
      line.addValue("");      
    }
    line.addString(itemList(ed.getCondition()));
    line.addString(itemList(ed.getConstraint()));
    for (StructureDefinitionMappingComponent mapKey : def.getMapping()) {
      for (ElementDefinitionMappingComponent map : ed.getMapping()) {
        if (map.getIdentity().equals(mapKey.getIdentity()))
        	line.addString(map.getMap());
      }
    }
  }


  private String itemList(List l) {
    StringBuilder s = new StringBuilder();
    for (int i =0; i< l.size(); i++) {
      Object o = l.get(i);
      String val = "";
      if (o instanceof StringType) {
        val = ((StringType)o).getValue();
      } else if (o instanceof UriType) {
        val = ((UriType)o).getValue();
      } else if (o instanceof IdType) {
        val = ((IdType)o).getValue();
      } else if (o instanceof Enumeration<?>) {
        val = o.toString();
      } else if (o instanceof TypeRefComponent) {
        TypeRefComponent t = (TypeRefComponent)o;
    	  val = t.getCode() + (t.getProfile() == null ? "" : " {" + t.getProfile() + "}") +(t.getTargetProfile() == null ? "" : " {" + t.getTargetProfile() + "}")  + (t.getAggregation() == null || t.getAggregation().isEmpty() ? "" : " (" + itemList(t.getAggregation()) + ")");
      } else if (o instanceof Coding) {
        Coding t = (Coding)o;
        val = (t.getSystem()==null ? "" : t.getSystem()) + (t.getCode()==null ? "" : "#" + t.getCode()) + (t.getDisplay()==null ? "" : " (" + t.getDisplay() + ")");
      } else if (o instanceof ElementDefinitionConstraintComponent) {
        ElementDefinitionConstraintComponent c = (ElementDefinitionConstraintComponent)o;
        val = c.getKey() + ":" + c.getHuman() + " {" + c.getExpression() + "}";
      } else if (o instanceof ElementDefinitionSlicingDiscriminatorComponent) {
        ElementDefinitionSlicingDiscriminatorComponent c = (ElementDefinitionSlicingDiscriminatorComponent)o;
        val = c.getType().toCode() + ":" + c.getPath() + "}";
        
      } else {
        val = o.toString();
        val = val.substring(val.indexOf("[")+1);
        val = val.substring(0, val.indexOf("]"));
      }
      s = s.append(val);
      if (i == 0)
        s.append("\n");
    }
    return s.toString();
  }
  
  private String renderType(Type value) throws Exception {
    String s = null;
    ByteArrayOutputStream bs = new ByteArrayOutputStream();
    if (asXml) {
      xml.setOutputStyle(OutputStyle.PRETTY);
      xml.compose(bs, "", value);
      bs.close();
      s = bs.toString();
      s = s.substring(s.indexOf("\n")+2);
    } else {
      json.setOutputStyle(OutputStyle.PRETTY);
      json.compose(bs, value, "");
      bs.close();
      s = bs.toString();
  	}
    return s;
  }

  public void dump() throws IOException {
    for (CSVLine l : lines)
      ln(l.toString());
    
    flush();
    close();
  }

}
