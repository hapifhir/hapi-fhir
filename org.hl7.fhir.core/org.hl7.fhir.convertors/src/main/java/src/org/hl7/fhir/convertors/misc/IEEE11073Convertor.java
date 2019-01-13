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
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.fhir.ucum.UcumEssenceService;
import org.fhir.ucum.UcumService;
import org.hl7.fhir.dstu3.formats.IParser.OutputStyle;
import org.hl7.fhir.dstu3.formats.XmlParser;
import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.CodeSystem.CodeSystemContentMode;
import org.hl7.fhir.dstu3.model.CodeSystem.ConceptDefinitionComponent;
import org.hl7.fhir.dstu3.model.CodeSystem.PropertyType;
import org.hl7.fhir.dstu3.model.CodeType;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.ConceptMap;
import org.hl7.fhir.dstu3.model.ConceptMap.ConceptMapGroupComponent;
import org.hl7.fhir.dstu3.model.ConceptMap.SourceElementComponent;
import org.hl7.fhir.dstu3.model.ConceptMap.TargetElementComponent;
import org.hl7.fhir.dstu3.model.ContactDetail;
import org.hl7.fhir.dstu3.model.ContactPoint;
import org.hl7.fhir.dstu3.model.ContactPoint.ContactPointSystem;
import org.hl7.fhir.dstu3.model.DateTimeType;
import org.hl7.fhir.dstu3.model.Enumerations.ConceptMapEquivalence;
import org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus;
import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.UriType;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.utilities.CSVReader;
import org.hl7.fhir.utilities.CommaSeparatedStringBuilder;
import org.hl7.fhir.utilities.Utilities;

public class IEEE11073Convertor {

  public static final String UCUM_PATH = "c:\\work\\org.hl7.fhir\\build\\implementations\\java\\org.hl7.fhir.convertors\\samples\\ucum-essence.xml";
  private static final String MDC_ALL_VALUES = "http://????";

  /**
   * argument 1: path to the rosetta csv file
   * argument 2: basePath to produce files to
   *  
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    UcumService ucum = new UcumEssenceService(UCUM_PATH);

    CodeSystem mdc = generateMDC(args[0], args[1], ucum);
    ConceptMap loinc = generateLoincMdcMap(mdc, args[1], args[2]);
  }

  private static ConceptMap generateLoincMdcMap(CodeSystem mdc, String dst, String src) throws IOException, FHIRException {
    ConceptMap cm = new ConceptMap();
    cm.setId("loinc-mdc");
    cm.setUrl("http:/???/fhir/ConceptMap/loinc-mdc");
    cm.setVersion("[todo]");
    cm.setName("LoincMdcCrossMap");
    cm.setTitle("Cross Map between LOINC and MDC");
    cm.setStatus(PublicationStatus.DRAFT);
    cm.setExperimental(true);
    cm.setDateElement(new DateTimeType());
    cm.setPublisher("HL7, Inc");
    ContactDetail cd = cm.addContact();
    cd.setName("LOINC + IEEE");
    ContactPoint cp = cd.addTelecom();
    cp.setSystem(ContactPointSystem.URL);
    cp.setValue("http://loinc.org");
    cm.setDescription("A Cross Map between the LOINC and MDC Code systems");
    cm.setPurpose("To implementers map between medical device codes and LOINC codes");
    cm.setCopyright("This content LOINC \u00ae is copyright \u00a9 1995 Regenstrief Institute, Inc. and the LOINC Committee, and available at no cost under the license at http://loinc.org/terms-of-use");
    cm.setSource(new UriType("http://loinc.org/vs"));
    cm.setTarget(new UriType(MDC_ALL_VALUES));
    ConceptMapGroupComponent g = cm.addGroup();
    g.setSource("urn:iso:std:iso:11073:10101");
    g.setTarget("http://loinc.org");

    CSVReader csv = new CSVReader(new FileInputStream(src));
    csv.readHeaders();
    while (csv.line()) {
      SourceElementComponent e = g.addElement();
      e.setCode(csv.cell("IEEE_CF_CODE10"));
      e.setDisplay(csv.cell("IEEE_DESCRIPTION"));
      TargetElementComponent t = e.addTarget();
      t.setEquivalence(ConceptMapEquivalence.EQUIVALENT);
      t.setCode(csv.cell("LOINC_NUM"));
      t.setDisplay(csv.cell("LOINC_LONG_COMMON_NAME"));
    }
    new XmlParser().setOutputStyle(OutputStyle.PRETTY).compose(new FileOutputStream(Utilities.path(dst, "conceptmap-"+cm.getId()+".xml")), cm);
    System.out.println("Done");
    return cm;
  }

  public static CodeSystem generateMDC(String src, String dst, UcumService ucum) throws UnsupportedEncodingException, FileNotFoundException, IOException, FHIRException {
    CSVReader csv = new CSVReader(new FileInputStream(src));
    csv.readHeaders();
    CodeSystem cs = new CodeSystem();
    Map<String, String> ucumIssues = new HashMap<String, String>();

    int errorCount = 0;
    cs.setId("MDC");
    cs.setUrl("urn:iso:std:iso:11073:10101");
    cs.setVersion("[todo]");
    cs.setName("11073:10101 codes for the FHIR community");
    cs.setStatus(PublicationStatus.ACTIVE);
    cs.setExperimental(false);
    cs.setDateElement(new DateTimeType());
    cs.setPublisher("HL7 (FHIR Project)");
    ContactDetail cd = cs.addContact();
    ContactPoint cp = cd.addTelecom();
    cp.setSystem(ContactPointSystem.URL);
    cp.setValue("http://ieee?");
    cs.setDescription("1073 Codes for the FHIR community (generated from the Rosetta data");
    Identifier i = new Identifier();
    cs.setIdentifier(i);
    i.setSystem("urn:ietf:rfc:3986");
    i.setValue("urn:oid:2.16.840.1.113883.6.24");
    cs.setCaseSensitive(false);
    cs.setContent(CodeSystemContentMode.COMPLETE);
    cs.addProperty().setCode("ucum").setDescription("UCUM units associated with Concept").setType(PropertyType.STRING);
    cs.addProperty().setCode("unit").setDescription("MDC units associated with Concept").setType(PropertyType.STRING);
    cs.addProperty().setCode("refid").setDescription("MDC Reference Id for Concept").setType(PropertyType.CODE);
    Set<String> codes = new HashSet<String>();
    while (csv.line()) {
      if (csv.has("CF_CODE10")) {
        String code = csv.cell("CF_CODE10");
        if (codes.contains(code))
          System.out.println("Duplicate Code "+code);
        else {
          codes.add(code);
          ConceptDefinitionComponent c = cs.addConcept();
          c.setCode(code);
          c.setDisplay(csv.cell("Common Term"));
          c.setDefinition(csv.cell("Term Description"));
          String vd = csv.cell("Vendor_Description");
          if (!c.hasDefinition())
            c.setDefinition(vd);
          if (!c.hasDisplay())
            c.setDisplay(vd);
          String refid = csv.cell("REFID");
          c.addProperty().setCode("refid").setValue(new CodeType().setValue(refid));
          if (csv.has("Synonym"))
            c.addDesignation().setValue(csv.cell("Synonym")).setUse(new Coding().setSystem("http://hl7.org/fhir/designation-use").setCode("synonym"));
          if (csv.has("Acronym"))
            c.addDesignation().setValue(csv.cell("Acronym")).setUse(new Coding().setSystem("http://hl7.org/fhir/designation-use").setDisplay("acronym"));
          if (csv.has("Systematic Name")) {
            String sysName = csv.cell("Systematic Name");
            if (!c.hasDefinition())
              c.setDefinition(sysName);
            c.addDesignation().setValue(sysName).setUse(new Coding().setSystem("http://hl7.org/fhir/designation-use").setCode("structured-name"));
          }
          if (csv.has("UOM_MDC"))
            c.addProperty().setCode("unit").setValue(new StringType().setValue(csv.cell("UOM_MDC")));
          if (csv.has("UOM_UCUM")) {
            CommaSeparatedStringBuilder ul = new CommaSeparatedStringBuilder();
            for (String u : csv.cell("UOM_UCUM").split(" ")) {
              String msg = ucum.validate(u); 
              if (msg != null) {
                errorCount++;
                ucumIssues.put(u, msg);
              } else
                ul.append(u);
            }
            if (ul.length() > 0)
              c.addProperty().setCode("ucum").setValue(new StringType().setValue(ul.toString()));
          }
        }
      }
    }
    csv.close();
    new XmlParser().setOutputStyle(OutputStyle.PRETTY).compose(new FileOutputStream(Utilities.path(dst, "codesystem-"+cs.getId()+".xml")), cs);
    System.out.println(Integer.toString(errorCount)+"UCUM errors");

    for (String u : sorted(ucumIssues.keySet()))
      System.out.println("Invalid UCUM code: "+u+" because "+ucumIssues.get(u));

    return cs;
  }

  private static List<String> sorted(Set<String> keySet) {
    List<String> names = new ArrayList<>();
    names.addAll(keySet);
    Collections.sort(names);
    return names;
  }



}
