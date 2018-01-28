package org.hl7.fhir.r4.utils;

import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.r4.context.IWorkerContext;
import org.hl7.fhir.r4.model.Base;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Bundle.BundleLinkComponent;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.ContactDetail;
import org.hl7.fhir.r4.model.ContactPoint;
import org.hl7.fhir.r4.model.ContactPoint.ContactPointSystem;
import org.hl7.fhir.r4.model.ElementDefinition;
import org.hl7.fhir.r4.model.ElementDefinition.ElementDefinitionBindingComponent;
import org.hl7.fhir.r4.model.ElementDefinition.TypeRefComponent;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.OperationOutcome.IssueSeverity;
import org.hl7.fhir.r4.model.OperationOutcome.OperationOutcomeIssueComponent;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.ResourceType;
import org.hl7.fhir.r4.model.Type;
import org.hl7.fhir.utilities.CommaSeparatedStringBuilder;
import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.utilities.xhtml.XhtmlComposer;

/**
 * Decoration utilities for various resource types
 * @author Grahame
 *
 */
public class ResourceUtilities {

  public final static String FHIR_LANGUAGE = "urn:ietf:bcp:47";

	public static boolean isAnError(OperationOutcome error) {
		for (OperationOutcomeIssueComponent t : error.getIssue())
			if (t.getSeverity() == IssueSeverity.ERROR)
				return true;
			else if (t.getSeverity() == IssueSeverity.FATAL)
				return true;
		return false;
	}
	
	public static String getErrorDescription(OperationOutcome error) {  
		if (error.hasText() && error.getText().hasDiv())
			return new XhtmlComposer(XhtmlComposer.XML).composePlainText(error.getText().getDiv());
		
		StringBuilder b = new StringBuilder();
		for (OperationOutcomeIssueComponent t : error.getIssue())
			if (t.getSeverity() == IssueSeverity.ERROR)
				b.append("Error:" +t.getDetails()+"\r\n");
			else if (t.getSeverity() == IssueSeverity.FATAL)
				b.append("Fatal:" +t.getDetails()+"\r\n");
			else if (t.getSeverity() == IssueSeverity.WARNING)
				b.append("Warning:" +t.getDetails()+"\r\n");
			else if (t.getSeverity() == IssueSeverity.INFORMATION)
				b.append("Information:" +t.getDetails()+"\r\n");
		return b.toString();
  }

  public static Resource getById(Bundle feed, ResourceType type, String reference) {
    for (BundleEntryComponent item : feed.getEntry()) {
      if (item.getResource().getId().equals(reference) && item.getResource().getResourceType() == type)
        return item.getResource();
    }
    return null;
  }

  public static BundleEntryComponent getEntryById(Bundle feed, ResourceType type, String reference) {
    for (BundleEntryComponent item : feed.getEntry()) {
      if (item.getResource().getId().equals(reference) && item.getResource().getResourceType() == type)
        return item;
    }
    return null;
  }

	public static String getLink(Bundle feed, String rel) {
		for (BundleLinkComponent link : feed.getLink()) {
			if (link.getRelation().equals(rel))
				return link.getUrl();
		}
	  return null;
  }

  public static Meta meta(Resource resource) {
    if (!resource.hasMeta())
      resource.setMeta(new Meta());
    return resource.getMeta();
  }

//  public static String representDataElementCollection(IWorkerContext context, Bundle bundle, boolean profileLink, String linkBase) {
//    StringBuilder b = new StringBuilder();
//    DataElement common = showDECHeader(b, bundle);
//    b.append("<table class=\"grid\">\r\n"); 
//    List<String> cols = chooseColumns(bundle, common, b, profileLink);
//    for (BundleEntryComponent e : bundle.getEntry()) {
//      DataElement de = (DataElement) e.getResource();
//      renderDE(de, cols, b, profileLink, linkBase);
//    }
//    b.append("</table>\r\n");
//    return b.toString();
//  }
//
//  
//  private static void renderDE(DataElement de, List<String> cols, StringBuilder b, boolean profileLink, String linkBase) {
//    b.append("<tr>");
//    for (String col : cols) {
//      String v;
//      ElementDefinition dee = de.getElement().get(0);
//      if (col.equals("DataElement.name")) {
//        v = de.hasName() ? Utilities.escapeXml(de.getName()) : "";
//      } else if (col.equals("DataElement.status")) {
//        v = de.hasStatusElement() ? de.getStatusElement().asStringValue() : "";
//      } else if (col.equals("DataElement.code")) {
//        v = renderCoding(dee.getCode());
//      } else if (col.equals("DataElement.type")) {
//        v = dee.hasType() ? Utilities.escapeXml(dee.getType().get(0).getCode()) : "";
//      } else if (col.equals("DataElement.units")) {
//        v = renderDEUnits(ToolingExtensions.getAllowedUnits(dee));
//      } else if (col.equals("DataElement.binding")) {
//        v = renderBinding(dee.getBinding());
//      } else if (col.equals("DataElement.minValue")) {
//        v = ToolingExtensions.hasExtension(de, "http://hl7.org/fhir/StructureDefinition/minValue") ? Utilities.escapeXml(ToolingExtensions.readPrimitiveExtension(de, "http://hl7.org/fhir/StructureDefinition/minValue").asStringValue()) : "";
//      } else if (col.equals("DataElement.maxValue")) {
//        v = ToolingExtensions.hasExtension(de, "http://hl7.org/fhir/StructureDefinition/maxValue") ? Utilities.escapeXml(ToolingExtensions.readPrimitiveExtension(de, "http://hl7.org/fhir/StructureDefinition/maxValue").asStringValue()) : "";
//      } else if (col.equals("DataElement.maxLength")) {
//        v = ToolingExtensions.hasExtension(de, "http://hl7.org/fhir/StructureDefinition/maxLength") ? Utilities.escapeXml(ToolingExtensions.readPrimitiveExtension(de, "http://hl7.org/fhir/StructureDefinition/maxLength").asStringValue()) : "";
//      } else if (col.equals("DataElement.mask")) {
//        v = ToolingExtensions.hasExtension(de, "http://hl7.org/fhir/StructureDefinition/mask") ? Utilities.escapeXml(ToolingExtensions.readPrimitiveExtension(de, "http://hl7.org/fhir/StructureDefinition/mask").asStringValue()) : "";
//      } else 
//        throw new Error("Unknown column name: "+col);
//
//      b.append("<td>"+v+"</td>");
//    }
//    if (profileLink) {
//      b.append("<td><a href=\""+linkBase+"-"+de.getId()+".html\">Profile</a>, <a href=\"http://www.opencem.org/#/20140917/Intermountain/"+de.getId()+"\">CEM</a>");
//      if (ToolingExtensions.hasExtension(de, ToolingExtensions.EXT_CIMI_REFERENCE)) 
//        b.append(", <a href=\""+ToolingExtensions.readStringExtension(de, ToolingExtensions.EXT_CIMI_REFERENCE)+"\">CIMI</a>");
//      b.append("</td>");
//    }
//    b.append("</tr>\r\n");
//  }

  

  private static String renderBinding(ElementDefinitionBindingComponent binding) {
    // TODO Auto-generated method stub
    return null;
  }

  private static String renderDEUnits(Type units) {
    if (units == null || units.isEmpty())
      return "";
    if (units instanceof CodeableConcept)
      return renderCodeable((CodeableConcept) units);
    else
      return "<a href=\""+Utilities.escapeXml(((Reference) units).getReference())+"\">"+Utilities.escapeXml(((Reference) units).getReference())+"</a>";
      
  }

  private static String renderCodeable(CodeableConcept units) {
    if (units == null || units.isEmpty())
      return "";
    String v = renderCoding(units.getCoding());
    if (units.hasText())
      v = v + " " +Utilities.escapeXml(units.getText());
    return v;
  }

  private static String renderCoding(List<Coding> codes) {
    CommaSeparatedStringBuilder b = new CommaSeparatedStringBuilder();
    for (Coding c : codes)
      b.append(renderCoding(c));
    return b.toString();
  }

  private static String renderCoding(Coding code) {
    if (code == null || code.isEmpty())
      return "";
    else
      return "<span title=\""+Utilities.escapeXml(code.getSystem())+"\">"+Utilities.escapeXml(code.getCode())+"</span>";
  }

//  private static List<String> chooseColumns(Bundle bundle, DataElement common, StringBuilder b, boolean profileLink) {
//    b.append("<tr>");
//    List<String> results = new ArrayList<String>();
//    results.add("DataElement.name");
//    b.append("<td width=\"250\"><b>Name</b></td>");
//    if (!common.hasStatus()) {
//      results.add("DataElement.status");
//      b.append("<td><b>Status</b></td>");
//    }
//    if (hasCode(bundle)) {
//      results.add("DataElement.code");
//      b.append("<td><b>Code</b></td>");
//    }
//    if (!common.getElement().get(0).hasType() && hasType(bundle)) {
//      results.add("DataElement.type");
//      b.append("<td><b>Type</b></td>");
//    }
//    if (hasUnits(bundle)) {
//      results.add("DataElement.units");
//      b.append("<td><b>Units</b></td>");
//    }
//    if (hasBinding(bundle)) {
//      results.add("DataElement.binding");
//      b.append("<td><b>Binding</b></td>");
//    }
//    if (hasExtension(bundle, "http://hl7.org/fhir/StructureDefinition/minValue")) {
//      results.add("DataElement.minValue");
//      b.append("<td><b>Min Value</b></td>");
//    }
//    if (hasExtension(bundle, "http://hl7.org/fhir/StructureDefinition/maxValue")) {
//      results.add("DataElement.maxValue");
//      b.append("<td><b>Max Value</b></td>");
//    }
//    if (hasExtension(bundle, "http://hl7.org/fhir/StructureDefinition/maxLength")) {
//      results.add("DataElement.maxLength");
//      b.append("<td><b>Max Length</b></td>");
//    }
//    if (hasExtension(bundle, "http://hl7.org/fhir/StructureDefinition/mask")) {
//      results.add("DataElement.mask");
//      b.append("<td><b>Mask</b></td>");
//    }
//    if (profileLink)
//      b.append("<td><b>Links</b></td>");
//    b.append("</tr>\r\n");
//    return results;
//  }
//
//  private static boolean hasExtension(Bundle bundle, String url) {
//    for (BundleEntryComponent e : bundle.getEntry()) {
//      DataElement de = (DataElement) e.getResource();
//      if (ToolingExtensions.hasExtension(de, url))
//        return true;
//    }
//    return false;
//  }
//
//  private static boolean hasBinding(Bundle bundle) {
//    for (BundleEntryComponent e : bundle.getEntry()) {
//      DataElement de = (DataElement) e.getResource();
//      if (de.getElement().get(0).hasBinding())
//        return true;
//    }
//    return false;
//  }
//
//  private static boolean hasCode(Bundle bundle) {
//    for (BundleEntryComponent e : bundle.getEntry()) {
//      DataElement de = (DataElement) e.getResource();
//      if (de.getElement().get(0).hasCode())
//        return true;
//    }
//    return false;
//  }
//
//  private static boolean hasType(Bundle bundle) {
//    for (BundleEntryComponent e : bundle.getEntry()) {
//      DataElement de = (DataElement) e.getResource();
//      if (de.getElement().get(0).hasType())
//        return true;
//    }
//    return false;
//  }
//
//  private static boolean hasUnits(Bundle bundle) {
//    for (BundleEntryComponent e : bundle.getEntry()) {
//      DataElement de = (DataElement) e.getResource();
//      if (ToolingExtensions.getAllowedUnits(de.getElement().get(0)) != null)
//        return true;
//    }
//    return false;
//  }
//
//  private static DataElement showDECHeader(StringBuilder b, Bundle bundle) {
//    DataElement meta = new DataElement();
//    DataElement prototype = (DataElement) bundle.getEntry().get(0).getResource();
//    meta.setPublisher(prototype.getPublisher());
//    meta.getContact().addAll(prototype.getContact());
//    meta.setStatus(prototype.getStatus());
//    meta.setDate(prototype.getDate());
//    meta.addElement().getType().addAll(prototype.getElement().get(0).getType());
//
//    for (BundleEntryComponent e : bundle.getEntry()) {
//      DataElement de = (DataElement) e.getResource();
//      if (!Base.compareDeep(de.getPublisherElement(), meta.getPublisherElement(), false))
//        meta.setPublisherElement(null);
//      if (!Base.compareDeep(de.getContact(), meta.getContact(), false))
//        meta.getContact().clear();
//      if (!Base.compareDeep(de.getStatusElement(), meta.getStatusElement(), false))
//        meta.setStatusElement(null);
//      if (!Base.compareDeep(de.getDateElement(), meta.getDateElement(), false))
//        meta.setDateElement(null);
//      if (!Base.compareDeep(de.getElement().get(0).getType(), meta.getElement().get(0).getType(), false))
//        meta.getElement().get(0).getType().clear();
//    }
//    if (meta.hasPublisher() || meta.hasContact() || meta.hasStatus() || meta.hasDate() /* || meta.hasType() */) {
//      b.append("<table class=\"grid\">\r\n"); 
//      if (meta.hasPublisher())
//        b.append("<tr><td>Publisher:</td><td>"+meta.getPublisher()+"</td></tr>\r\n");
//      if (meta.hasContact()) {
//        b.append("<tr><td>Contacts:</td><td>");
//        boolean firsti = true;
//        for (ContactDetail c : meta.getContact()) {
//          if (firsti)
//            firsti = false;
//          else
//            b.append("<br/>");
//          if (c.hasName())
//            b.append(Utilities.escapeXml(c.getName())+": ");
//          boolean first = true;
//          for (ContactPoint cp : c.getTelecom()) {
//            if (first)
//              first = false;
//            else
//              b.append(", ");
//            renderContactPoint(b, cp);
//          }
//        }
//        b.append("</td></tr>\r\n");
//      }
//      if (meta.hasStatus())
//        b.append("<tr><td>Status:</td><td>"+meta.getStatus().toString()+"</td></tr>\r\n");
//      if (meta.hasDate())
//        b.append("<tr><td>Date:</td><td>"+meta.getDateElement().asStringValue()+"</td></tr>\r\n");
//      if (meta.getElement().get(0).hasType())
//        b.append("<tr><td>Type:</td><td>"+renderType(meta.getElement().get(0).getType())+"</td></tr>\r\n");
//      b.append("</table>\r\n"); 
//    }  
//    return meta;
//  }

  private static String renderType(List<TypeRefComponent> type) {
    if (type == null || type.isEmpty())
      return "";
    CommaSeparatedStringBuilder b = new CommaSeparatedStringBuilder();
    for (TypeRefComponent c : type)
      b.append(renderType(c));
    return b.toString();
  }

  private static String renderType(TypeRefComponent type) {
    if (type == null || type.isEmpty())
      return "";
    return type.getCode();
  }

  public static void renderContactPoint(StringBuilder b, ContactPoint cp) {
    if (cp != null && !cp.isEmpty()) {
      if (cp.getSystem() == ContactPointSystem.EMAIL)
        b.append("<a href=\"mailto:"+cp.getValue()+"\">"+cp.getValue()+"</a>");
      else if (cp.getSystem() == ContactPointSystem.FAX) 
        b.append("Fax: "+cp.getValue());
      else if (cp.getSystem() == ContactPointSystem.OTHER) 
        b.append("<a href=\""+cp.getValue()+"\">"+cp.getValue()+"</a>");
      else
        b.append(cp.getValue());
    }
  }
}
