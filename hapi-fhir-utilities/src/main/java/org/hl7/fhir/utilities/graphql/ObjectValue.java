package org.hl7.fhir.utilities.graphql;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.hl7.fhir.utilities.Utilities;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class ObjectValue extends Value {
  private List<Argument> fields = new ArrayList<Argument>();

  public ObjectValue() {
    super();
  }

  public ObjectValue(JsonObject json) throws EGraphQLException {
    super();
    for (Entry<String, JsonElement> n : json.entrySet()) 
      fields.add(new Argument(n.getKey(), n.getValue()));      
  }

  public List<Argument> getFields() {
    return fields;
  }

  public Argument addField(String name, boolean isList) {
    Argument result = null;
    for (Argument t : fields)
      if ((t.name.equals(name)))
        result = t;
    if (result == null) {
      result = new Argument();
      result.setName(name);
      result.setList(isList);
      fields.add(result);
    } else
      result.list = true;
    return result;
  }

  public void write(StringBuilder b, int indent) throws EGraphQLException, EGraphEngine {
    b.append("{");
    int ni = indent;
    String s = "";
    String se = "";
    if ((ni > -1))
    {
      se = "\r\n"+Utilities.padLeft("",' ', ni*2);
      ni++;
      s = "\r\n"+Utilities.padLeft("",' ', ni*2);
    }
    boolean first = true;
    for (Argument a : fields) {
      if (first) first = false; else b.append(",");
      b.append(s);
      a.write(b, ni);
    }
    b.append(se);
    b.append("}");

  }
}