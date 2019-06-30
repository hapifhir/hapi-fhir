package org.hl7.fhir.r4.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.hl7.fhir.utilities.CSFileInputStream;
import org.hl7.fhir.utilities.xml.XMLUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class Translations {

  private String[] lang;
  private Map<String, Element> messages = new HashMap<String, Element>();

  /**
   * Set a default language to use
   * 
   * @param lang
   */
  public void setLang(String lang) {
    this.lang = lang.split("[.;]");
  }
  
  /**
   * Load from the XML translations file maintained by the FHIR project
   * 
   * @param filename
   * @throws IOException 
   * @throws SAXException 
   * @throws FileNotFoundException 
   * @throws ParserConfigurationException 
   * @throws Exception
   */
  public void load(String filename) throws FileNotFoundException, SAXException, IOException, ParserConfigurationException {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    loadMessages(builder.parse(new CSFileInputStream(filename)));
  }
  
  private void loadMessages(Document doc) {
    // TODO Auto-generated method stub
    Element element = XMLUtil.getFirstChild(doc.getDocumentElement());
    while (element != null) {
      messages.put(element.getAttribute("id"), element);
      element = XMLUtil.getNextSibling(element);
    }
  }

  public boolean hasTranslation(String id) {
    return messages.containsKey(id);
  }

  /**
   * use configured language
   * 
   * @param id - the id of the message to retrieve
   * @param defaultMsg - string to use if the message is not defined or a language match is not found (if null, then will default to english)
   * @return the message
   */
  public String getMessage(String id, String defaultMsg) {
    return getMessage(id, lang, defaultMsg);
  }

  /**
   * return the message in a specified language
   * 
   * @param id - the id of the message to retrieve
   * @param lang - a language string from a browser
   * @param defaultMsg - string to use if the message is not defined or a language match is not found (if null, then will default to the english message)
   * @return the message
   */
  public String getMessage(String id, String lang, String defaultMsg) {
    return getMessage(id, lang.split("[.;]"), defaultMsg);
  }

  private String getMessage(String id, String[] lang, String defaultMsg) {
    Element msg = messages.get(id);
    if (msg == null)
      return defaultMsg;
    for (String l : lang) {
      String res = getByLang(msg, l);
      if (res != null)
        return res;
    }
    if (defaultMsg == null) {
      String res = getByLang(msg, "en");
      if (res != null)
        return res;
    }
    return defaultMsg;
  }

  private String getByLang(Element msg, String lang) {
    Element c = XMLUtil.getFirstChild(msg);
    while (c != null) {
      if (c.getAttribute("lang").equals(lang))
        return c.getTextContent();
      c = XMLUtil.getNextSibling(c);
    }
    return null;
  }
 
  // http://en.wikipedia.org/wiki/List_of_ISO_639-1_codes
  public String getLangDesc(String s) {
    if (s.equals("en"))
      return "English";
    if (s.equals("nl"))
      return "Nederlands (Dutch)";
    if (s.equals("de"))
      return "Deutsch (German)";
    if (s.equals("ru"))
      return "\u0440\u0443\u0301\u0441\u0441\u043a\u0438\u0439 (Russian)";
    return "\"" + s + "\"";
  }


}
