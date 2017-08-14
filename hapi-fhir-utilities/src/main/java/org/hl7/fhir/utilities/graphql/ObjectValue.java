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

  /**
   * Write the output using the system default line separator (as defined in {@link System#lineSeparator}
   * @param b The StringBuilder to populate
   * @param indent The indent level, or <code>-1</code> for no indent
   */
  public void write(StringBuilder b, int indent) throws EGraphQLException, EGraphEngine {
    write(b, indent, System.lineSeparator());
  }

  @Override
  public String getValue() {
    return null;
  }

  /**
   * Write the output using the system default line separator (as defined in {@link System#lineSeparator}
   * @param b The StringBuilder to populate
   * @param indent The indent level, or <code>-1</code> for no indent
   * @param lineSeparator The line separator
   */
  public void write(StringBuilder b, Integer indent, String lineSeparator) throws EGraphQLException, EGraphEngine {
    b.append("{");
    String s = "";
    String se = "";
    if ((indent > -1))
    {
      se = lineSeparator + Utilities.padLeft("",' ', indent*2);
      indent++;
      s = lineSeparator + Utilities.padLeft("",' ', indent*2);
    }
    boolean first = true;
    for (Argument a : fields) {
      if (first) first = false; else b.append(",");
      b.append(s);
      a.write(b, indent);
    }
    b.append(se);
    b.append("}");

  }
}
