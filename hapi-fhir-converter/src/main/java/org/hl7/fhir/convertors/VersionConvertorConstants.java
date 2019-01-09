package org.hl7.fhir.convertors;

/*-
 * #%L
 * HAPI FHIR - Converter
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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


public class VersionConvertorConstants {

  public final static String IG_DEPENDSON_PACKAGE_EXTENSION = "http://hl7.org/fhir/4.0/StructureDefinition/extension-ImplentationGuide.dependency.packageId";
  public final static String IG_DEPENDSON_VERSION_EXTENSION = "http://hl7.org/fhir/4.0/StructureDefinition/extension-ImplentationGuide.dependency.version";
  public final static String MODIFIER_REASON_EXTENSION = "http://hl7.org/fhir/4.0/StructureDefinition/extension-ElementDefinition.isModifierReason";
  public final static String MODIFIER_TAKEN = "http://hl7.org/fhir/4.0/StructureDefinition/extension-MedicationStatment.taken";
  public final static String MODIFIER_REASON_LEGACY = "No Modifier Reason provideed in previous versions of FHIR";
  
  public static String refToVS(String url) {
    if (url == null)
      return null;
    if (url.equals("http://www.genenames.org"))
      return "http://hl7.org/fhir/ValueSet/genenames";
    else if (url.equals("http://varnomen.hgvs.org/"))
      return "http://hl7.org/fhir/ValueSet/variants";
    else if (url.equals("http://www.ncbi.nlm.nih.gov/nuccore?db=nuccore"))
      return "http://hl7.org/fhir/ValueSet/ref-sequences";
    else if (url.equals("http://www.ensembl.org/"))
      return "http://hl7.org/fhir/ValueSet/ensembl";
    else if (url.equals("http://www.ncbi.nlm.nih.gov/clinvar/variation"))
      return "http://hl7.org/fhir/ValueSet/clinvar";
    else if (url.equals("http://cancer.sanger.ac.uk/cancergenome/projects/cosmic/"))
      return "http://hl7.org/fhir/ValueSet/cosmic";
    else if (url.equals("http://www.ncbi.nlm.nih.gov/projects/SNP/"))
      return "http://hl7.org/fhir/ValueSet/bbsnp";
    else if (url.equals("http://www.sequenceontology.org/"))
      return "http://hl7.org/fhir/ValueSet/sequenceontology";
    else if (url.equals("http://www.ebi.ac.uk/"))
      return "http://hl7.org/fhir/ValueSet/allelename";
    else if (url.equals("https://www.iso.org/iso-4217-currency-codes.html"))
      return "http://hl7.org/fhir/ValueSet/currencies";
    else if (url.equals("http://www.rfc-editor.org/bcp/bcp13.txt"))
      return "http://hl7.org/fhir/ValueSet/mimetypes";
    else
      return url;
  }
    
  public static String vsToRef(String url) {
    if (url == null)
      return null;
    if (url.equals("http://hl7.org/fhir/ValueSet/genenames"))
      return "http://www.genenames.org";
    else if (url.equals("http://hl7.org/fhir/ValueSet/variants"))
      return "http://varnomen.hgvs.org/";
    else if (url.equals("http://hl7.org/fhir/ValueSet/ref-sequences"))
      return "http://www.ncbi.nlm.nih.gov/nuccore?db=nuccore";
    else if (url.equals("http://hl7.org/fhir/ValueSet/ensembl"))
      return "http://www.ensembl.org/";
    else if (url.equals("http://hl7.org/fhir/ValueSet/clinvar"))
      return "http://www.ncbi.nlm.nih.gov/clinvar/variation";
    else if (url.equals("http://hl7.org/fhir/ValueSet/cosmic"))
      return "http://cancer.sanger.ac.uk/cancergenome/projects/cosmic/";
    else if (url.equals("http://hl7.org/fhir/ValueSet/bbsnp"))
      return "http://www.ncbi.nlm.nih.gov/projects/SNP/";
    else if (url.equals("http://hl7.org/fhir/ValueSet/sequenceontology"))
      return "http://www.sequenceontology.org/";
    else if (url.equals("http://hl7.org/fhir/ValueSet/allelename"))
      return "http://www.ebi.ac.uk/";
    else if (url.equals("http://hl7.org/fhir/ValueSet/currencies"))
      return "https://www.iso.org/iso-4217-currency-codes.html";
    else if (url.equals("http://hl7.org/fhir/ValueSet/mimetypes"))
      return "http://www.rfc-editor.org/bcp/bcp13.txt";
    else
      return null;
  }
}
