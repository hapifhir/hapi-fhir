package org.hl7.fhir.r4.utils.transform;

import org.hl7.fhir.r4.model.Base;
import org.hl7.fhir.utilities.CommaSeparatedStringBuilder;

import java.util.ArrayList;
import java.util.List;

public class Variables {
  /**
   * List of variables
   */
  private List<Variable> list = new ArrayList<>();

  /**
   * safely adds a new variable instance to the list
   *
   * @param mode   Variable mode
   * @param name   name of variable
   * @param object object of the variable
   */
  public void add(VariableMode mode, String name, Base object) {
    Variable vv = null;
    for (Variable v : list)
      if ((v.getMode() == mode) && v.getName().equals(name))
        vv = v;
    if (vv != null)
      list.remove(vv);
    list.add(new Variable(mode, name, object));
  }

  /**
   * Copies list to a new list instance of Variables
   *
   * @return new instance of Variables
   */
  public Variables copy() {
    Variables result = new Variables();
    result.list.addAll(list);
    return result;
  }

  /**
   * gets the object of a variable based on the arguments
   *
   * @param mode mode of target variable
   * @param name name of target variable
   * @return variable's base object, may be null if not present
   */
  public Base get(VariableMode mode, String name) {
    for (Variable v : list)
      if ((v.getMode() == mode) && v.getName().equals(name))
        return v.getObject();
    return null;
  }

  /**
   * Gets a summary of the variables based on whats present
   *
   * @return string summary
   */
  public String summary() {
    CommaSeparatedStringBuilder s = new CommaSeparatedStringBuilder();
    CommaSeparatedStringBuilder t = new CommaSeparatedStringBuilder();
    for (Variable v : list)
      if (v.getMode() == VariableMode.INPUT)
        s.append(v.summary());
      else
        t.append(v.summary());
    return "source variables [" + s.toString() + "], target variables [" + t.toString() + "]";
  }
}
