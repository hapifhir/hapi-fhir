package org.hl7.fhir.r4.utils.transform;

import org.hl7.fhir.r4.elementmodel.Property;
import org.hl7.fhir.r4.model.TypeDetails;
import org.hl7.fhir.utilities.CommaSeparatedStringBuilder;

import java.util.ArrayList;
import java.util.List;

public class VariablesForProfiling {

  private List<VariableForProfiling> list = new ArrayList<VariableForProfiling>();
  private boolean optional;
  private boolean repeating;

  public VariablesForProfiling(boolean optional, boolean repeating) {
    this.optional = optional;
    this.repeating = repeating;
  }

  public void add(VariableMode mode, String name, String path, Property property, TypeDetails types) {
    add(mode, name, new PropertyWithType(path, property, null, types));
  }

  public void add(VariableMode mode, String name, String path, Property baseProperty, Property profileProperty, TypeDetails types) {
    add(mode, name, new PropertyWithType(path, baseProperty, profileProperty, types));
  }

  public void add(VariableMode mode, String name, PropertyWithType property) {
    VariableForProfiling vv = null;
    for (VariableForProfiling v : list)
      if ((v.getMode() == mode) && v.getName().equals(name))
        vv = v;
    if (vv != null)
      list.remove(vv);
    list.add(new VariableForProfiling(mode, name, property));
  }

  public VariablesForProfiling copy(boolean optional, boolean repeating) {
    VariablesForProfiling result = new VariablesForProfiling(optional, repeating);
    result.list.addAll(list);
    return result;
  }

  public VariablesForProfiling copy() {
    VariablesForProfiling result = new VariablesForProfiling(optional, repeating);
    result.list.addAll(list);
    return result;
  }

  public VariableForProfiling get(VariableMode mode, String name) {
    if (mode == null) {
      for (VariableForProfiling v : list)
        if ((v.getMode() == VariableMode.OUTPUT) && v.getName().equals(name))
          return v;
      for (VariableForProfiling v : list)
        if ((v.getMode() == VariableMode.INPUT) && v.getName().equals(name))
          return v;
    }
    for (VariableForProfiling v : list)
      if ((v.getMode() == mode) && v.getName().equals(name))
        return v;
    return null;
  }

  public String summary() {
    CommaSeparatedStringBuilder s = new CommaSeparatedStringBuilder();
    CommaSeparatedStringBuilder t = new CommaSeparatedStringBuilder();
    for (VariableForProfiling v : list)
      if (v.getMode() == VariableMode.INPUT)
        s.append(v.summary());
      else
        t.append(v.summary());
    return "source variables [" + s.toString() + "], target variables [" + t.toString() + "]";
  }
}
