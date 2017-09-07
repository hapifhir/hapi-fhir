package org.hl7.fhir.r4.utils.transform;

import org.hl7.fhir.utilities.CommaSeparatedStringBuilder;
import org.hl7.fhir.utilities.xhtml.XhtmlNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TargetWriter {
  private Map<String, String> newResources = new HashMap<String, String>();
  private List<StringPair> assignments = new ArrayList<StringPair>();
  private List<StringPair> keyProps = new ArrayList<StringPair>();
  private CommaSeparatedStringBuilder txt = new CommaSeparatedStringBuilder();

  public TargetWriter() {
  }

  public void newResource(String var, String name) {
    newResources.put(var, name);
    txt.append("new " + name);
  }

  public void valueAssignment(String context, String desc) {
    assignments.add(new StringPair(context, desc));
    txt.append(desc);
  }

  public void keyAssignment(String context, String desc) {
    keyProps.add(new StringPair(context, desc));
    txt.append(desc);
  }

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
