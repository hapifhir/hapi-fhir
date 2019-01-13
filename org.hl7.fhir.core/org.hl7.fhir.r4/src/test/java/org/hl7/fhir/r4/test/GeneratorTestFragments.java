package org.hl7.fhir.r4.test;

import java.io.IOException;

import org.hl7.fhir.exceptions.FHIRFormatError;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.CodeableConcept.*;
import org.hl7.fhir.r4.model.CodeableConcept.*;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Coding.*;
import org.hl7.fhir.r4.model.Coding.*;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.Enumerations.*;
import org.hl7.fhir.r4.model.Narrative;
import org.hl7.fhir.r4.model.Narrative.*;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Observation.*;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.Quantity.*;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Reference.*;
import org.hl7.fhir.r4.model.Reference.*;
import org.hl7.fhir.utilities.xhtml.XhtmlParser;

public class GeneratorTestFragments {

  private void test() throws FHIRFormatError, IOException {

      Observation res = new Observation();
      res.setId("example");
      Narrative n = new Narrative();
        res.setText(n);
        n.setStatus(NarrativeStatus.GENERATED);
        n.setDiv(new XhtmlParser().parse("<div xmlns=\"http://www.w3.org/1999/xhtml\"><p><b>Generated Narrative with Details</b></p><p><b>id</b>: example</p><p><b>status</b>: final</p><p><b>category</b>: Vital Signs <span>(Details : {http://hl7.org/fhir/observation-category code &#39;vital-signs&#39; = &#39;Vital Signs&#39;, given as &#39;Vital Signs&#39;})</span></p><p><b>code</b>: Body Weight <span>(Details : {LOINC code &#39;29463-7&#39; = &#39;Body weight&#39;, given as &#39;Body Weight&#39;}; {LOINC code &#39;3141-9&#39; = &#39;Body weight Measured&#39;, given as &#39;Body weight Measured&#39;}; {SNOMED CT code &#39;27113001&#39; = &#39;Body weight&#39;, given as &#39;Body weight&#39;}; {http://acme.org/devices/clinical-codes code &#39;body-weight&#39; = &#39;body-weight&#39;, given as &#39;Body Weight&#39;})</span></p><p><b>subject</b>: <a>Patient/example</a></p><p><b>context</b>: <a>Encounter/example</a></p><p><b>effective</b>: 28/03/2016</p><p><b>value</b>: 185 lbs<span> (Details: UCUM code [lb_av] = &#39;lb_av&#39;)</span></p></div>", "div"));
      res.setStatus(ObservationStatus.FINAL);
      CodeableConcept cc = res.addCategory();
        Coding c = cc.addCoding();
          c.setSystem("http://hl7.org/fhir/observation-category");
          c.setCode("vital-signs");
          c.setDisplay("Vital Signs");
      cc = new CodeableConcept();
        res.setCode(cc);
        c = cc.addCoding();
          c.setSystem("http://loinc.org");
          c.setCode("29463-7");
          c.setDisplay("Body Weight");
        c = cc.addCoding();
          c.setSystem("http://loinc.org");
          c.setCode("3141-9");
          c.setDisplay("Body weight Measured");
        c = cc.addCoding();
          c.setSystem("http://snomed.info/sct");
          c.setCode("27113001");
          c.setDisplay("Body weight");
        c = cc.addCoding();
          c.setSystem("http://acme.org/devices/clinical-codes");
          c.setCode("body-weight");
          c.setDisplay("Body Weight");
      Reference r = new Reference();
        res.setSubject(r);
        r.setReference("Patient/example");
      r = new Reference();
        res.setEncounter(r);
        r.setReference("Encounter/example");
      res.setEffective(new DateTimeType("2016-03-28"));
      Quantity q = new Quantity();
        res.setValue(q);
        q.setValue(185);
        q.setUnit("lbs");
        q.setSystem("http://unitsofmeasure.org");
        q.setCode("[lb_av]");

    
  }

}
