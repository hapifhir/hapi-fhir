package org.hl7.fhir.r4.model;

import java.io.IOException;
import java.util.List;

import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r4.formats.FormatUtilities;
import org.hl7.fhir.utilities.xhtml.NodeType;
import org.hl7.fhir.utilities.xhtml.XhtmlComposer;
import org.hl7.fhir.utilities.xhtml.XhtmlNode;

public class XhtmlType extends Element {

  private Narrative place;
  
  public XhtmlType(Narrative place) {
    super();
    this.place = place;
  }

  public XhtmlType() {
    // "<div xmlns=\""+FormatUtilities.XHTML_NS+"\"></div>"
  }

  @Override
  public String fhirType() {
    return "xhtml";
  }

  @Override
  protected void listChildren(List<Property> result) {
  }

  @Override
  public String getIdBase() {
    return null;
  }

  @Override
  public void setIdBase(String value) {
  }

  @Override
  public Element copy() {
    return null;
  }

  public XhtmlNode getValue() {
    return place == null ? new XhtmlNode(NodeType.Element, "div") : place.getDiv();
  }

  @Override
  public Base setProperty(int hash, String name, Base value) throws FHIRException {
    if ("value".equals(name)) {
      place.setDiv(castToXhtml(value));
      return value;
    } else
      return super.setProperty(hash, name, value);
  }

  @Override
  public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
    if ("value".equals(name))
      return new Base[] {this};
    return super.getProperty(hash, name, checkValid);
  }

  @Override
  public String primitiveValue() {
    try {
      return new XhtmlComposer(false).compose(getValue());
    } catch (IOException e) {
    }
    return null;
  }  
  
  @Override
  public boolean isPrimitive() {
    return true;
  }
  
  @Override
  public boolean hasPrimitiveValue() {
    return true;
  }
  

}
