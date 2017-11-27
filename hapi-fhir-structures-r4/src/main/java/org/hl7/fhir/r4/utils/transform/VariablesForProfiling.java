package org.hl7.fhir.r4.utils.transform;

import org.hl7.fhir.r4.elementmodel.Property;
import org.hl7.fhir.r4.model.TypeDetails;
import org.hl7.fhir.utilities.CommaSeparatedStringBuilder;

import java.util.ArrayList;
import java.util.List;

public class VariablesForProfiling {

  /**
   * List of Variables for Profiling
   */
  private List<VariableForProfiling> list = new ArrayList<>();
  /**
   * true if optimal
   */
  private boolean optional;
  /**
   * true if variables repeat
   */
  private boolean repeating;

  /**
   * Constructor that sets optimal and repeating
   *
   * @param optional  initial optimal value
   * @param repeating initial repeating value
   */
  @SuppressWarnings("WeakerAccess")
  public VariablesForProfiling(boolean optional, boolean repeating) {
    this.optional = optional;
    this.repeating = repeating;
  }

  /**
   * Safely adds a new variable for profiling based on the arguments
   *
   * @param mode     Variable Mode
   * @param name     String value for name
   * @param path     String value for path
   * @param property Property value
   * @param types    Type Details value
   */
  public void add(VariableMode mode, String name, String path, Property property, TypeDetails types) {
    add(mode, name, new PropertyWithType(path, property, null, types));
  }

  /**
   * Safely adds a new variable for profiling based on the arguments
   *
   * @param mode            Variable Mode
   * @param name            Variable name
   * @param path            Variable path
   * @param baseProperty    Property of base
   * @param profileProperty Property of profile
   * @param types           Type Details
   */
  public void add(VariableMode mode, String name, String path, Property baseProperty, Property profileProperty, TypeDetails types) {
    add(mode, name, new PropertyWithType(path, baseProperty, profileProperty, types));
  }

  /**
   * Safely adds a new variable for profiling based on the arguments
   *
   * @param mode     Variable Mode
   * @param name     Variable Name
   * @param property Property with Type
   */
  public void add(VariableMode mode, String name, PropertyWithType property) {
    VariableForProfiling vv = null;
    for (VariableForProfiling v : list)
      if ((v.getMode() == mode) && v.getName().equals(name))
        vv = v;
    if (vv != null)
      list.remove(vv);
    list.add(new VariableForProfiling(mode, name, property));
  }

  /**
   * Copies values in list to a new Variables for Profiling object with the arguments
   *
   * @param optional  optional value
   * @param repeating repeating value
   * @return new Variables for Profiling object
   */
  public VariablesForProfiling copy(boolean optional, boolean repeating) {
    VariablesForProfiling result = new VariablesForProfiling(optional, repeating);
    result.list.addAll(list);
    return result;
  }

  /**
   * Copies values in list to a new Variables for Profiling object with this object's optional and repeating values.
   *
   * @return new Variables for Profiling objects
   */
  public VariablesForProfiling copy() {
    VariablesForProfiling result = new VariablesForProfiling(optional, repeating);
    result.list.addAll(list);
    return result;
  }

  /**
   * Gets a variable by the name and mode from the list
   *
   * @param mode target mode
   * @param name target name
   * @return Variable for Profiling, can be null if not found
   */
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

  /**
   * Gets a string summary of variables contained in list
   *
   * @return Concatenated string of variables
   */
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
