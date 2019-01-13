package org.hl7.fhir.convertors.misc;

/*-
 * #%L
 * org.hl7.fhir.convertors
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


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.ContactPoint.ContactPointSystem;
import org.hl7.fhir.r4.formats.IParser.OutputStyle;
import org.hl7.fhir.r4.formats.JsonParser;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeSystem.CodeSystemContentMode;
import org.hl7.fhir.r4.model.CodeSystem.ConceptDefinitionComponent;
import org.hl7.fhir.r4.model.CodeSystem.PropertyType;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.Enumerations.PublicationStatus;
import org.hl7.fhir.r4.terminologies.CodeSystemUtilities;
import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.utilities.xml.XMLUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class CountryCodesConverter {


  public static void main(String[] args) throws Exception {
    CountryCodesConverter self = new CountryCodesConverter();
    self.source = args[0];
    self.dest = args[1];
    self.execute();
  }

  private String source;
  private String dest;
  
  
  private void execute() throws FileNotFoundException, ParserConfigurationException, SAXException, IOException {
    Document src = load();
    CodeSystem cs1 = new CodeSystem();
    CodeSystem cs2 = new CodeSystem();
    CodeSystem cs3 = new CodeSystem();
    setMetadata(src, cs1, "iso3166", "urn:iso:std:iso:3166", "", "");
    setMetadata(src, cs2, "iso3166-2", "urn:iso:std:iso:3166:-2", "Part2", " Part 2");
    cs1.addProperty().setCode("canonical").setDescription("The 2 letter code that identifies the same country (so 2/3/numeric codes can be aligned)").setType(PropertyType.CODE);
    cs2.addProperty().setCode("country").setDescription("The 2 letter code that identifies the country for the subdivision").setType(PropertyType.CODE);
    for (Element e : XMLUtil.getNamedChildren(src.getDocumentElement(), "country")) {
      System.out.println(e.getAttribute("id"));
      String c2 = XMLUtil.getNamedChildText(e, "alpha-2-code");
      String c3 = XMLUtil.getNamedChildText(e, "alpha-3-code");
      String cN = XMLUtil.getNamedChildText(e, "numeric-code");
      Element n = XMLUtil.getNamedChildByAttribute(e, "short-name", "lang3code", "eng");
      if (n == null)
        n = XMLUtil.getNamedChildByAttribute(e, "short-name-upper-case", "lang3code", "eng");
      if (n == null)
        continue;
      String name = n.getTextContent();
      n = XMLUtil.getNamedChildByAttribute(e, "full-name", "lang3code", "eng");
      if (n == null)
        n = XMLUtil.getNamedChildByAttribute(e, "full-name-upper-case", "lang3code", "eng");
      if (n == null)
        n = XMLUtil.getNamedChildByAttribute(e, "short-name", "lang3code", "eng");
      if (n == null)
        n = XMLUtil.getNamedChildByAttribute(e, "short-name-upper-case", "lang3code", "eng");
      String desc = n.getTextContent();
      ConceptDefinitionComponent cc = cs1.addConcept();
      cc.setCode(c2);
      cc.setDisplay(name);
      cc.setDefinition(desc);
      poplang(e, cc);
      if (c3 != null) {
        cc = cs1.addConcept();
        cc.setCode(c3);
        cc.setDisplay(name);
        cc.setDefinition(desc);
        cc.addProperty().setCode("canonical").setValue(new CodeType(c2));
        poplang(e, cc);
      }
      if (cN != null) {
        cc = cs1.addConcept();
        cc.setCode(cN);
        cc.setDisplay(name);
        cc.setDefinition(desc);
        cc.addProperty().setCode("canonical").setValue(new CodeType(c2));
        poplang(e, cc);
      }
      for (Element sd : XMLUtil.getNamedChildren(e, "subdivision")) {
        cc = cs2.addConcept();
        cc.setCode(XMLUtil.getNamedChildText(sd, "subdivision-code"));
        Element l = XMLUtil.getNamedChild(sd, "subdivision-locale");
        cc.setDisplay(XMLUtil.getNamedChildText(l, "subdivision-locale-name"));
        cc.addProperty().setCode("country").setValue(new CodeType(c2));            
      }
    }
    cs1.setCount(cs1.getConcept().size());
    cs2.setCount(cs2.getConcept().size());
    new JsonParser().setOutputStyle(OutputStyle.PRETTY).compose(new FileOutputStream(Utilities.path(dest, "4.0.0", "package", "CodeSstem-iso3166.json")), cs1);
    new JsonParser().setOutputStyle(OutputStyle.PRETTY).compose(new FileOutputStream(Utilities.path(dest, "3.0.1", "package", "CodeSstem-iso3166.json")), cs1); // format hasn't changed
    new JsonParser().setOutputStyle(OutputStyle.PRETTY).compose(new FileOutputStream(Utilities.path(dest, "4.0.0", "package", "CodeSstem-iso3166-2.json")), cs2);
    new JsonParser().setOutputStyle(OutputStyle.PRETTY).compose(new FileOutputStream(Utilities.path(dest, "3.0.1", "package", "CodeSstem-iso3166-2.json")), cs2); // format hasn't changed
  }

  public void setMetadata(Document src, CodeSystem cs, String id, String url, String partName, String partTitle) {
    cs.setId(id);
    cs.setUrl(url);
    cs.setName("ISOCountryCodes"+partName);
    cs.setTitle("ISO Country Codes (ISO-3166)"+partTitle);
    cs.setVersion(XMLUtil.getFirstChild(src.getDocumentElement()).getAttribute("version"));
    cs.setStatus(PublicationStatus.ACTIVE);
    cs.setExperimental(false);
    cs.addContact().setName("FHIR Project Team").addTelecom().setSystem(ContactPointSystem.URL).setValue("http://hl7.org/fhir");
    cs.setDateElement(new DateTimeType(src.getDocumentElement().getAttribute("generated")));
    cs.setCopyright("Copyright ISO. See https://www.iso.org/obp/ui/#search/code/");
    cs.setCaseSensitive(true);
    cs.setContent(CodeSystemContentMode.COMPLETE);
    cs.setLanguage("en");
  }

  public void poplang(Element e, ConceptDefinitionComponent cc) {
    for (Element el : XMLUtil.getNamedChildren(e, "short-name")) {
      if (!el.getAttribute("lang3code").equals("eng")) {
        String l2 = lang3To2(el.getAttribute("lang3code"));
        if (l2 != null)
          cc.addDesignation().setLanguage(l2).setValue(el.getTextContent());
      }
    }
  }

  private String lang3To2(String lang) {
    if ("abk".equals(lang)) return "ab";
    if ("aar".equals(lang)) return "aa";
    if ("afr".equals(lang)) return "af";
    if ("aka".equals(lang)) return "ak";
    if ("sqi".equals(lang)) return "sq";
    if ("amh".equals(lang)) return "am";
    if ("ara".equals(lang)) return "ar";
    if ("arg".equals(lang)) return "an";
    if ("hye".equals(lang)) return "hy";
    if ("asm".equals(lang)) return "as";
    if ("ava".equals(lang)) return "av";
    if ("ave".equals(lang)) return "ae";
    if ("aym".equals(lang)) return "ay";
    if ("aze".equals(lang)) return "az";
    if ("bam".equals(lang)) return "bm";
    if ("bak".equals(lang)) return "ba";
    if ("eus".equals(lang)) return "eu";
    if ("bel".equals(lang)) return "be";
    if ("ben".equals(lang)) return "bn";
    if ("bih".equals(lang)) return "bh";
    if ("bis".equals(lang)) return "bi";
    if ("bos".equals(lang)) return "bs";
    if ("bre".equals(lang)) return "br";
    if ("bul".equals(lang)) return "bg";
    if ("mya".equals(lang)) return "my";
    if ("cat".equals(lang)) return "ca";
    if ("khm".equals(lang)) return "km";
    if ("cha".equals(lang)) return "ch";
    if ("che".equals(lang)) return "ce";
    if ("nya".equals(lang)) return "ny";
    if ("zho".equals(lang)) return "zh";
    if ("chu".equals(lang)) return "cu";
    if ("chv".equals(lang)) return "cv";
    if ("cor".equals(lang)) return "kw";
    if ("cos".equals(lang)) return "co";
    if ("cre".equals(lang)) return "cr";
    if ("hrv".equals(lang)) return "hr";
    if ("ces".equals(lang)) return "cs";
    if ("dan".equals(lang)) return "da";
    if ("div".equals(lang)) return "dv";
    if ("nld".equals(lang)) return "nl";
    if ("dzo".equals(lang)) return "dz";
    if ("eng".equals(lang)) return "en";
    if ("epo".equals(lang)) return "eo";
    if ("est".equals(lang)) return "et";
    if ("ewe".equals(lang)) return "ee";
    if ("fao".equals(lang)) return "fo";
    if ("fij".equals(lang)) return "fj";
    if ("fin".equals(lang)) return "fi";
    if ("fra".equals(lang)) return "fr";
    if ("ful".equals(lang)) return "ff";
    if ("gla".equals(lang)) return "gd";
    if ("glg".equals(lang)) return "gl";
    if ("lug".equals(lang)) return "lg";
    if ("kat".equals(lang)) return "ka";
    if ("deu".equals(lang)) return "de";
    if ("ell".equals(lang)) return "el";
    if ("grn".equals(lang)) return "gn";
    if ("guj".equals(lang)) return "gu";
    if ("hat".equals(lang)) return "ht";
    if ("hau".equals(lang)) return "ha";
    if ("heb".equals(lang)) return "he";
    if ("her".equals(lang)) return "hz";
    if ("hin".equals(lang)) return "hi";
    if ("hmo".equals(lang)) return "ho";
    if ("hun".equals(lang)) return "hu";
    if ("isl".equals(lang)) return "is";
    if ("ido".equals(lang)) return "io";
    if ("ibo".equals(lang)) return "ig";
    if ("ind".equals(lang)) return "id";
    if ("ina".equals(lang)) return "ia";
    if ("ile".equals(lang)) return "ie";
    if ("iku".equals(lang)) return "iu";
    if ("ipk".equals(lang)) return "ik";
    if ("gle".equals(lang)) return "ga";
    if ("ita".equals(lang)) return "it";
    if ("jpn".equals(lang)) return "ja";
    if ("jav".equals(lang)) return "jv";
    if ("kal".equals(lang)) return "kl";
    if ("kan".equals(lang)) return "kn";
    if ("kau".equals(lang)) return "kr";
    if ("kas".equals(lang)) return "ks";
    if ("kaz".equals(lang)) return "kk";
    if ("kik".equals(lang)) return "ki";
    if ("kin".equals(lang)) return "rw";
    if ("kir".equals(lang)) return "ky";
    if ("kom".equals(lang)) return "kv";
    if ("kon".equals(lang)) return "kg";
    if ("kor".equals(lang)) return "ko";
    if ("kua".equals(lang)) return "kj";
    if ("kur".equals(lang)) return "ku";
    if ("lao".equals(lang)) return "lo";
    if ("lat".equals(lang)) return "la";
    if ("lav".equals(lang)) return "lv";
    if ("lim".equals(lang)) return "li";
    if ("lin".equals(lang)) return "ln";
    if ("lit".equals(lang)) return "lt";
    if ("lub".equals(lang)) return "lu";
    if ("ltz".equals(lang)) return "lb";
    if ("mkd".equals(lang)) return "mk";
    if ("mlg".equals(lang)) return "mg";
    if ("msa".equals(lang)) return "ms";
    if ("mal".equals(lang)) return "ml";
    if ("mlt".equals(lang)) return "mt";
    if ("glv".equals(lang)) return "gv";
    if ("mri".equals(lang)) return "mi";
    if ("mar".equals(lang)) return "mr";
    if ("mah".equals(lang)) return "mh";
    if ("mon".equals(lang)) return "mn";
    if ("nau".equals(lang)) return "na";
    if ("nav".equals(lang)) return "nv";
    if ("ndo".equals(lang)) return "ng";
    if ("nep".equals(lang)) return "ne";
    if ("nde".equals(lang)) return "nd";
    if ("sme".equals(lang)) return "se";
    if ("nor".equals(lang)) return "no";
    if ("nob".equals(lang)) return "nb";
    if ("nno".equals(lang)) return "nn";
    if ("oci".equals(lang)) return "oc";
    if ("oji".equals(lang)) return "oj";
    if ("ori".equals(lang)) return "or";
    if ("orm".equals(lang)) return "om";
    if ("oss".equals(lang)) return "os";
    if ("pli".equals(lang)) return "pi";
    if ("pan".equals(lang)) return "pa";
    if ("pus".equals(lang)) return "ps";
    if ("fas".equals(lang)) return "fa";
    if ("pol".equals(lang)) return "pl";
    if ("por".equals(lang)) return "pt";
    if ("que".equals(lang)) return "qu";
    if ("ron".equals(lang)) return "ro";
    if ("roh".equals(lang)) return "rm";
    if ("run".equals(lang)) return "rn";
    if ("rus".equals(lang)) return "ru";
    if ("smo".equals(lang)) return "sm";
    if ("sag".equals(lang)) return "sg";
    if ("san".equals(lang)) return "sa";
    if ("srd".equals(lang)) return "sc";
    if ("srp".equals(lang)) return "sr";
    if ("sna".equals(lang)) return "sn";
    if ("iii".equals(lang)) return "ii";
    if ("snd".equals(lang)) return "sd";
    if ("sin".equals(lang)) return "si";
    if ("slk".equals(lang)) return "sk";
    if ("slv".equals(lang)) return "sl";
    if ("som".equals(lang)) return "so";
    if ("nbl".equals(lang)) return "nr";
    if ("sot".equals(lang)) return "st";
    if ("spa".equals(lang)) return "es";
    if ("sun".equals(lang)) return "su";
    if ("swa".equals(lang)) return "sw";
    if ("ssw".equals(lang)) return "ss";
    if ("swe".equals(lang)) return "sv";
    if ("tgl".equals(lang)) return "tl";
    if ("tah".equals(lang)) return "ty";
    if ("tgk".equals(lang)) return "tg";
    if ("tam".equals(lang)) return "ta";
    if ("tat".equals(lang)) return "tt";
    if ("tel".equals(lang)) return "te";
    if ("tha".equals(lang)) return "th";
    if ("bod".equals(lang)) return "bo";
    if ("tir".equals(lang)) return "ti";
    if ("ton".equals(lang)) return "to";
    if ("tso".equals(lang)) return "ts";
    if ("tsn".equals(lang)) return "tn";
    if ("tur".equals(lang)) return "tr";
    if ("tuk".equals(lang)) return "tk";
    if ("twi".equals(lang)) return "tw";
    if ("uig".equals(lang)) return "ug";
    if ("ukr".equals(lang)) return "uk";
    if ("urd".equals(lang)) return "ur";
    if ("uzb".equals(lang)) return "uz";
    if ("ven".equals(lang)) return "ve";
    if ("vie".equals(lang)) return "vi";
    if ("vol".equals(lang)) return "vo";
    if ("wln".equals(lang)) return "wa";
    if ("cym".equals(lang)) return "cy";
    if ("fry".equals(lang)) return "fy";
    if ("wol".equals(lang)) return "wo";
    if ("xho".equals(lang)) return "xh";
    if ("yid".equals(lang)) return "yi";
    if ("yor".equals(lang)) return "yo";
    if ("zha".equals(lang)) return "za";
    if ("zul".equals(lang)) return "zu";
    if ("pap".equals(lang)) return "pap";
    if ("gil".equals(lang)) return "gil";
    if ("002".equals(lang)) return null;
    if ("cnr".equals(lang)) return "cnr";
    if ("niu".equals(lang)) return "niu";
    if ("tpi".equals(lang)) return "tpi";
    if ("pau".equals(lang)) return "pau";
    if ("crs".equals(lang)) return null;
    if ("tkl".equals(lang)) return "tkl";
    if ("tet".equals(lang)) return "tet";
    if ("tvl".equals(lang)) return "tvl";
    if ("nso".equals(lang)) return "nso";
    throw new Error("unknown 3 letter lang code "+lang);
  }

  private Document load() throws ParserConfigurationException, FileNotFoundException, SAXException, IOException {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setNamespaceAware(true);
    DocumentBuilder builder = factory.newDocumentBuilder();

    return builder.parse(new FileInputStream(source));
  }
  
  
}
