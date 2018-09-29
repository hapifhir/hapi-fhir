package org.hl7.fhir.utilities;

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
