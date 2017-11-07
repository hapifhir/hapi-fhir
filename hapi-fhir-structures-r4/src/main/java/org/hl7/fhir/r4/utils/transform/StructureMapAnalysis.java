package org.hl7.fhir.r4.utils.transform;

import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.utilities.xhtml.XhtmlNode;

import java.util.ArrayList;
import java.util.List;

/**
 * StructureMap analysis
 */
public class StructureMapAnalysis {

  /**
   * List of Structure Definitions.
   */
  private List<StructureDefinition> profiles = new ArrayList<>();
  /**
   * XhtmlNode summary
   */
  private XhtmlNode summary;

  /**
   * get accessor for profiles
   *
   * @return profiles in list form
   */
  public List<StructureDefinition> getProfiles() {
    return profiles;
  }

  /**
   * set accessor for profiles
   *
   * @param profiles profiles in list form
   */
  public void setProfiles(List<StructureDefinition> profiles) {
    this.profiles = profiles;
  }

  /**
   * get accessor for summary
   *
   * @return XhtmlNode summary
   */
  public XhtmlNode getSummary() {
    return summary;
  }

  /**
   * set accessor for summary
   *
   * @param summary XhtmlNode summary
   */
  public void setSummary(XhtmlNode summary) {
    this.summary = summary;
  }

}
