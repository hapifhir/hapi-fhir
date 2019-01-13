package org.hl7.fhir.r4.model;

/*-
 * #%L
 * org.hl7.fhir.r4
 * %%
 * Copyright (C) 2014 - 2019 Health Level 7
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


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
