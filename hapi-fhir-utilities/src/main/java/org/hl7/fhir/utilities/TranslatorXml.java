package org.hl7.fhir.utilities;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.hl7.fhir.utilities.xml.XMLUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class TranslatorXml implements TranslationServices {

  
  public class TranslatedTerm {
    private Set<String> props = new HashSet<String>();
    private Map<String, String> translations = new HashMap<String, String>();
  }


  private Map<String, TranslatedTerm> termsById = new HashMap<String, TranslatedTerm>();  
  private Map<String, TranslatedTerm> termsByTerm = new HashMap<String, TranslatedTerm>();  
  
  public TranslatorXml(String filename) throws ParserConfigurationException, SAXException, IOException {
    super();
    load(filename);
  }


  private void load(String filename) throws ParserConfigurationException, SAXException, IOException {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setNamespaceAware(false);
    DocumentBuilder builder = factory.newDocumentBuilder();
    Document xml = builder.parse(new File(filename)); 
    Element e = XMLUtil.getFirstChild(xml.getDocumentElement());
    while (e != null) {
      load(e);
      e = XMLUtil.getNextSibling(e);
    }
  }

  private void load(Element e) {
    TranslatedTerm t = new TranslatedTerm();
    for (int i = 0; i < e.getAttributes().getLength(); i++) {
      Node a = e.getAttributes().item(i);
      String n = a.getNodeName();
      if (n.equals("id"))
        termsById.put(a.getTextContent(), t);
      else if (a.getNodeValue().equals("true"))
        t.props.add(n);
    }
    Element c = XMLUtil.getFirstChild(e);
    while (c != null) {
      String l = c.getAttribute("lang");
      String s = c.getTextContent();
      if (l.equals("en"))
        termsByTerm.put(s, t);
      t.translations.put(l, s);
      c = XMLUtil.getNextSibling(c);
    }    
  }


  private Map<String, String> getTranslations(String code) {
    TranslatedTerm t = termsById.get(code);
    if (t == null)
      t = termsByTerm.get(code);
    return t == null ? null : t.translations;
  }

  
  @Override
  public String translate(String context, String value, String targetLang) {
    if (targetLang == null)
      return value;
    Map<String, String> t = getTranslations(value);
    if (t == null)
      return value;
    if (t.containsKey(targetLang))
      return t.get(targetLang);
    return value;
  }

  @Override
  public String translateAndFormat(String context, String lang, String value, Object... args) {
    value = translate(context, value, lang);
    return value == null ? "":  String.format(value, args);
  }

  @Override
  public String translate(String context, String value) {
    return null;    
  }

  @Override
  public String toStr(float value) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String toStr(Date value) {
    // TODO Auto-generated method stub
    return null;
  }


  @Override
  public Map<String, String> translations(String value) {
    return getTranslations(value);
  }


  @Override
  public Set<String> listTranslations(String category) {
    Set<String> res = new HashSet<String>();
    for (String s : termsById.keySet()) {
      if (termsById.get(s).props.contains(category))
        res.add(s);
    }
    return res;
  }
 
}
