package org.hl7.fhir.r4.utils.transform;

import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.utilities.xhtml.XhtmlNode;

import java.util.ArrayList;
import java.util.List;

public class StructureMapAnalysis {

  private List<StructureDefinition> profiles = new ArrayList<StructureDefinition>();
  private XhtmlNode summary;

  public List<StructureDefinition> getProfiles() {
    return profiles;
  }

  public void setProfiles(List<StructureDefinition> profiles) {
    this.profiles = profiles;
  }

  public XhtmlNode getSummary() {
    return summary;
  }

  public void setSummary(XhtmlNode summary) {
    this.summary = summary;
  }

}
