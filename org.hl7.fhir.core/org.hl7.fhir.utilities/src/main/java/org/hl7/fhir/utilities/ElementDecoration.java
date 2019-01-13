package org.hl7.fhir.utilities;

/*-
 * #%L
 * org.hl7.fhir.utilities
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


public class ElementDecoration {
  public enum DecorationType { TYPE, SLICE, HINT, WARNING, ERROR };

  private DecorationType type;
  private String link;
  private String text;
  public ElementDecoration(DecorationType type, String link, String text) {
    super();
    this.type = type;
    this.link = link;
    this.text = text;
  }
  public DecorationType getType() {
    return type;
  }
  
  public boolean hasLink() {
    return !Utilities.noString(link);
  }
  
  public String getLink() {
    return link;
  }
  public String getText() {
    return text;
  }
  public String getIcon() {
    switch (type) {
    case SLICE: return "icon_slice.png";
    case TYPE: return "icon_element.gif";
    case HINT: return "icon-hint.png";
    case ERROR: return "icon-wanning.png";
    case WARNING: return "icon-error.gif";
    default: return "";
    }
  }
  
  
}
