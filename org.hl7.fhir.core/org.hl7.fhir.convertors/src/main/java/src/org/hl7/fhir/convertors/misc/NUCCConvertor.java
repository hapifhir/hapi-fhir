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
import java.io.FileOutputStream;
import java.io.IOException;

import org.hl7.fhir.dstu3.formats.IParser.OutputStyle;
import org.hl7.fhir.dstu3.formats.XmlParser;
import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.CodeSystem.CodeSystemContentMode;
import org.hl7.fhir.dstu3.model.CodeSystem.CodeSystemHierarchyMeaning;
import org.hl7.fhir.dstu3.model.CodeSystem.ConceptDefinitionComponent;
import org.hl7.fhir.dstu3.model.DateTimeType;
import org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus;
import org.hl7.fhir.dstu3.terminologies.CodeSystemUtilities;
import org.hl7.fhir.dstu3.utils.ToolingExtensions;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.exceptions.FHIRFormatError;
import org.hl7.fhir.utilities.CSVReader;
import org.hl7.fhir.utilities.Utilities;

public class NUCCConvertor {
  private String[] last = new String[2];
  private ConceptDefinitionComponent[] concepts = new ConceptDefinitionComponent[2];
  
  public static void main(String[] args) throws Exception {
    new NUCCConvertor().execute();
  }
  
  public void execute() throws IOException, FHIRException {
    CSVReader csv = new CSVReader(new FileInputStream("c:\\temp\\nucc.csv"));
    CodeSystem cs = new CodeSystem();
    cs.setId("nucc-provider-taxonomy");
    cs.setUrl("http://nucc.org/provider-taxonomy");
    cs.setName("NUCC Provider Taxonomy");
    cs.setDateElement(new DateTimeType());
    cs.setDescription("The Health Care Provider Taxonomy code is a unique alphanumeric code, ten characters in length. The code set is structured into three distinct 'Levels' including Provider Type, Classification, and Area of Specialization");
    cs.setCopyright("See NUCC copyright statement");
    cs.setStatus(PublicationStatus.ACTIVE);
    cs.setContent(CodeSystemContentMode.COMPLETE);
    cs.setExperimental(false);
    cs.setValueSet("http://hl7.org/fhir/ValueSet/nucc-provider-taxonomy"); 
    cs.setHierarchyMeaning(CodeSystemHierarchyMeaning.CLASSIFIEDWITH);
    csv.parseLine();
    while (csv.ready())
    {
      String[] values = csv.parseLine();
      processLine(cs, values);
    }     
    csv.close();
    new XmlParser().setOutputStyle(OutputStyle.PRETTY).compose(new FileOutputStream("c:\\temp\\nucc.xml"), cs);
  }

  private void processLine(CodeSystem cs, String[] values) throws FHIRFormatError {
    if (!values[1].equals(last[0])) {
      last[1] = "";
      last[0] = values[1];
      concepts[0] = new ConceptDefinitionComponent();
      cs.getConcept().add(concepts[0]);
      concepts[0].setDisplay(values[1]);
      concepts[0].setCode("base-"+Integer.toString(cs.getConcept().size()));
      CodeSystemUtilities.setNotSelectable(cs, concepts[0]);
    }
    if (!values[2].equals(last[1])) {
      last[1] = values[2];
      concepts[1] = new ConceptDefinitionComponent();
      concepts[0].getConcept().add(concepts[1]);
      concepts[1].setCode(values[0]);
      concepts[1].setDisplay(values[2]);
      concepts[1].setDefinition(values[4]);
      if (values.length > 5 && !Utilities.noString(values[5]))
        ToolingExtensions.addCSComment(concepts[1], values[5]);
    } else if (!Utilities.noString(values[3])) {
      ConceptDefinitionComponent cc = new ConceptDefinitionComponent();
      concepts[1].getConcept().add(cc);
      cc.setCode(values[0]);
      cc.setDisplay(values[3]);
      cc.setDefinition(values[4]);
      if (values.length > 5 && !Utilities.noString(values[5]))
        ToolingExtensions.addCSComment(cc, values[5]);
    }
  }
  
}
