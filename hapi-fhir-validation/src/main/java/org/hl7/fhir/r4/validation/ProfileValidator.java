package org.hl7.fhir.r4.validation;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.hl7.fhir.r4.context.IWorkerContext;
import org.hl7.fhir.r4.elementmodel.Element;
import org.hl7.fhir.r4.model.ElementDefinition;
import org.hl7.fhir.r4.model.ElementDefinition.ElementDefinitionConstraintComponent;
import org.hl7.fhir.r4.model.ElementDefinition.TypeRefComponent;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.utils.FHIRPathEngine;
import org.hl7.fhir.r4.validation.InstanceValidator.NodeStack;
import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.utilities.validation.ValidationMessage;
import org.hl7.fhir.utilities.validation.ValidationMessage.IssueType;

public class ProfileValidator extends BaseValidator {

  IWorkerContext context;
  private boolean checkAggregation = false;
  private boolean checkMustSupport = false;

  public void setContext(IWorkerContext context) {
    this.context = context;    
  }

  public boolean isCheckAggregation() {
    return checkAggregation;
  }

  public boolean isCheckMustSupport() {
    return checkMustSupport;
  }

  public void setCheckAggregation(boolean checkAggregation) {
    this.checkAggregation = checkAggregation;
  }

  public void setCheckMustSupport(boolean checkMustSupport) {
    this.checkMustSupport = checkMustSupport;
  }

  protected boolean rule(List<ValidationMessage> errors, IssueType type, String path, boolean b, String msg) {
    String rn = path.contains(".") ? path.substring(0, path.indexOf(".")) : path;
    return super.rule(errors, type, path, b, msg, "<a href=\""+(rn.toLowerCase())+".html\">"+rn+"</a>: "+Utilities.escapeXml(msg));
  }

  public List<ValidationMessage> validate(StructureDefinition profile, boolean forBuild) {
    List<ValidationMessage> errors = new ArrayList<ValidationMessage>();
    
    // first check: extensions must exist
    for (ElementDefinition ec : profile.getDifferential().getElement())
      checkExtensions(profile, errors, "differential", ec);
    rule(errors, IssueType.STRUCTURE, profile.getId(), profile.hasSnapshot(), "missing Snapshot at "+profile.getName()+"."+profile.getName());
    for (ElementDefinition ec : profile.getSnapshot().getElement()) 
      checkExtensions(profile, errors, "snapshot", ec);

    if (rule(errors, IssueType.STRUCTURE, profile.getId(), profile.hasSnapshot(), "A snapshot is required")) {
      Hashtable<String, ElementDefinition> snapshotElements = new Hashtable<String, ElementDefinition>();
      for (ElementDefinition ed : profile.getSnapshot().getElement()) {
        snapshotElements.put(ed.getId(), ed);
        checkExtensions(profile, errors, "snapshot", ed);
        for (ElementDefinitionConstraintComponent inv : ed.getConstraint()) {
          if (forBuild) {
            if (!inExemptList(inv.getKey())) {
              if (rule(errors, IssueType.BUSINESSRULE, profile.getId()+"::"+ed.getPath()+"::"+inv.getKey(), inv.hasExpression(), "The invariant has no FHIR Path expression ("+inv.getXpath()+")")) {
                try {
                  new FHIRPathEngine(context).check(null, profile.getType(), ed.getPath(), inv.getExpression()); // , inv.hasXpath() && inv.getXpath().startsWith("@value")
                } catch (Exception e) {
//                  rule(errors, IssueType.STRUCTURE, profile.getId()+"::"+ed.getPath()+"::"+inv.getId(), exprExt != null, e.getMessage());
                }
              } 
            }
          }
        }
      }
      for (ElementDefinition diffElement : profile.getDifferential().getElement()) {
        ElementDefinition snapElement = snapshotElements.get(diffElement.getId());
        if (snapElement!=null) { // Happens with profiles in the main build - should be able to fix once snapshot generation is fixed - Lloyd
          warning(errors, IssueType.BUSINESSRULE, diffElement.getId(), !checkMustSupport || snapElement.hasMustSupport(), "Elements included in the differential should declare mustSupport");
          if (checkAggregation) {
            for (TypeRefComponent type : snapElement.getType()) {
              if ("http://hl7.org/fhir/Reference".equals(type.getCode()) || "http://hl7.org/fhir/canonical".equals(type.getCode())) {
                warning(errors, IssueType.BUSINESSRULE, diffElement.getId(), type.hasAggregation(), "Elements with type Reference or canonical should declare aggregation");
              }
            }
          }
        }
      }
    }
    return errors;
  }

  // these are special cases
  private boolean inExemptList(String key) {
    return key.startsWith("txt-");
  }

  private void checkExtensions(StructureDefinition profile, List<ValidationMessage> errors, String kind, ElementDefinition ec) {
    if (!ec.getType().isEmpty() && "Extension".equals(ec.getType().get(0).getCode()) && ec.getType().get(0).hasProfile()) {
      String url = ec.getType().get(0).getProfile().get(0).getValue();
      StructureDefinition defn = context.fetchResource(StructureDefinition.class, url);
      rule(errors, IssueType.BUSINESSRULE, profile.getId(), defn != null, "Unable to find Extension '"+url+"' referenced at "+profile.getUrl()+" "+kind+" "+ec.getPath()+" ("+ec.getSliceName()+")");
    }
  }
}
