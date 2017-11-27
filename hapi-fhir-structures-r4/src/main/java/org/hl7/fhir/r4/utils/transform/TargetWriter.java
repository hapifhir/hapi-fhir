package org.hl7.fhir.r4.utils.transform;

import org.hl7.fhir.utilities.CommaSeparatedStringBuilder;
import org.hl7.fhir.utilities.xhtml.XhtmlNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Target
 */
public class TargetWriter {
  /**
   * Map containing a pair of strings
   */
  private Map<String, String> newResources = new HashMap<>();
  /**
   * List of string pairs for assignments
   */
  private List<StringPair> assignments = new ArrayList<>();
  /**
   * key properties in the form of string pairs
   */
  private List<StringPair> keyProps = new ArrayList<>();
  /**
   * Comma Separated String Builder for output
   */
  private CommaSeparatedStringBuilder txt = new CommaSeparatedStringBuilder();

  /**
   * Constructor
   */
  public TargetWriter() {
  }

  /**
   * Populates new Resources with a new entry
   *
   * @param var  variable
   * @param name variable name
   */
  public void newResource(String var, String name) {
    newResources.put(var, name);
    txt.append("new " + name);
  }

  /**
   * adds a new string pair to assignments
   *
   * @param context String value
   * @param desc    descriptor
   */
  public void valueAssignment(String context, String desc) {
    assignments.add(new StringPair(context, desc));
    txt.append(desc);
  }

  /**
   * adds a String Pair value based on the arguments to keyProps
   *
   * @param context String value
   * @param desc    String descriptor
   */
  public void keyAssignment(String context, String desc) {
    keyProps.add(new StringPair(context, desc));
    txt.append(desc);
  }

  /**
   * Commit the Lists and Map to a winter
   *
   * @param xt XHTML Node
   */
  public void commit(XhtmlNode xt) {
    if (newResources.size() == 1 && assignments.size() == 1 && newResources.containsKey(assignments.get(0).getVar()) && keyProps.size() == 1 && newResources.containsKey(keyProps.get(0).getVar())) {
      xt.addText("new " + assignments.get(0).getDesc() + " (" + keyProps.get(0).getDesc().substring(keyProps.get(0).getDesc().indexOf(".") + 1) + ")");
    } else if (newResources.size() == 1 && assignments.size() == 1 && newResources.containsKey(assignments.get(0).getVar()) && keyProps.size() == 0) {
      xt.addText("new " + assignments.get(0).getDesc());
    } else {
      xt.addText(txt.toString());
    }
  }
}
