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


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.UUID;

import org.hl7.fhir.dstu3.formats.IParser.OutputStyle;
import org.hl7.fhir.dstu3.formats.XmlParser;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Bundle.BundleType;
import org.hl7.fhir.dstu3.model.DateTimeType;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Observation.ObservationComponentComponent;
import org.hl7.fhir.dstu3.model.Observation.ObservationStatus;
import org.hl7.fhir.dstu3.model.Quantity;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.Type;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.exceptions.FHIRFormatError;

public class ObservationStatsBuilder {

  public static void main(String[] args) throws FileNotFoundException, IOException, FHIRException {
    buildVitalSignsSet();
    buildStatsSeries();
  
  }

  private static void buildVitalSignsSet() throws FileNotFoundException, IOException, FHIRFormatError {
    Calendar base = Calendar.getInstance();
    base.add(Calendar.DAY_OF_MONTH, -1);
    Bundle b = new Bundle();
    b.setType(BundleType.COLLECTION);
    b.setId(UUID.randomUUID().toString().toLowerCase());
    
    vitals(b, base, 0, 80, 120, 95, 37.1);
    vitals(b, base, 35, 85, 140, 98, 36.9);
    vitals(b, base, 53, 75, 110, 96, 36.2);
    vitals(b, base, 59, 65, 100, 94, 35.5);
    vitals(b, base, 104, 60, 90, 90, 35.9);
    vitals(b, base, 109, 65, 100, 92, 36.5);
    vitals(b, base, 114, 70, 130, 94, 37.5);
    vitals(b, base, 120, 90, 150, 97, 37.3);
    vitals(b, base, 130, 95, 133, 97, 37.2);
    vitals(b, base, 150, 85, 125, 98, 37.1);
    
    new XmlParser().setOutputStyle(OutputStyle.PRETTY).compose(new FileOutputStream("c:\\temp\\vitals.xml"), b);
  }

  private static void vitals(Bundle b,  Calendar base, int minutes, int diastolic, int systolic, int sat, double temp) throws FHIRFormatError {
    Calendar when = (Calendar) base.clone();
    when.add(Calendar.MINUTE, minutes);
    
    baseVitals(b, when, minutes, "sat", "59408-5", "O2 Saturation").setValue(makeQty(sat, "%", "%"));
    baseVitals(b, when, minutes, "temp", "8310-5", "Body temperature").setValue(makeQty(temp, "\u00b0C", "Cel"));
    Observation obs = baseVitals(b, when, minutes, "bp", "85354-9", "Blood pressure");
    component(obs, "8480-6", "Systolic").setValue(makeQty(systolic, "mmhg", "mm[Hg]"));
    component(obs, "8462-4", "Diastolic").setValue(makeQty(diastolic, "mmhg", "mm[Hg]"));

  }

    private static ObservationComponentComponent component(Observation obs, String lCode, String text) {
      ObservationComponentComponent comp = obs.addComponent();
      comp.getCode().setText(text).addCoding().setCode(lCode).setSystem("http://loinc.org");
      return comp;
    }

  private static Type makeQty(int sat, String human, String ucum) {
    Quantity q = new Quantity();
    q.setCode(ucum);
    q.setSystem("http://unitsofmeasure.org");
    q.setUnit(human);
    q.setValue(new BigDecimal(sat));
    return q;
  }


  private static Type makeQty(double val, String human, String ucum) {
    Quantity q = new Quantity();
    q.setCode(ucum);
    q.setSystem("http://unitsofmeasure.org");
    q.setUnit(human);
    q.setValue(new BigDecimal(val));
    return q;
  }
  
  private static Observation baseVitals(Bundle b, Calendar when, int min, String name, String lCode, String text) throws FHIRFormatError {
    Observation obs = new Observation();
    obs.setId("obs-vitals-"+name+"-"+Integer.toString(min));
    obs.setSubject(new Reference().setReference("Patient/123"));
    obs.setStatus(ObservationStatus.FINAL);
    obs.setEffective(new DateTimeType(when));
    obs.addCategory().setText("Vital Signs").addCoding().setSystem("http://hl7.org/fhir/observation-category").setCode("vital-signs").setDisplay("Vital Signs");
    obs.getCode().setText(text).addCoding().setCode(lCode).setSystem("http://loinc.org");
    b.addEntry().setFullUrl("http://hl7.org/fhir/Observation/"+obs.getId()).setResource(obs);
    return obs;
  }
/**
 *     

 * @throws FHIRException
 * @throws IOException
 * @throws FileNotFoundException
 */
  public static void buildStatsSeries() throws FHIRException, IOException, FileNotFoundException {
    Bundle b = new Bundle();
    b.setType(BundleType.COLLECTION);
    b.setId(UUID.randomUUID().toString().toLowerCase());

    addAge(b, 5, 1, "18.3");
    addAge(b, 5, 2, "18.4");
    addAge(b, 5, 3, "18.6");
    addAge(b, 5, 4, "18.8");
    addAge(b, 5, 5, "19.0");
    addAge(b, 5, 6, "19.1");
    addAge(b, 5, 7, "19.3");
    addAge(b, 5, 8, "19.5");
    addAge(b, 5, 9, "19.6");
    addAge(b, 5,10, "19.8");
    addAge(b, 5,11, "20.0");
    addAge(b, 6, 0, "20.2");
    addAge(b, 6, 1, "20.3");
    addAge(b, 6, 2, "20.5");
    addAge(b, 6, 3, "20.7");
    addAge(b, 6, 4, "20.9");
    addAge(b, 6, 5, "21.0");
    addAge(b, 6, 6, "21.2");
    addAge(b, 6, 7, "21.4");
    addAge(b, 6, 8, "21.6");
    addAge(b, 6, 9, "21.8");
    addAge(b, 6,10, "22.0");
    addAge(b, 6,11, "22.2");
    addAge(b, 7, 0, "22.4");
    addAge(b, 7, 1, "22.6");
    addAge(b, 7, 2, "22.8");
    addAge(b, 7, 3, "23.0");
    addAge(b, 7, 4, "23.2");
    addAge(b, 7, 5, "23.4");
    addAge(b, 7, 6, "23.6");
    addAge(b, 7, 7, "23.9");
    addAge(b, 7, 8, "24.1");
    addAge(b, 7, 9, "24.3");
    addAge(b, 7,10, "24.5");
    addAge(b, 7,11, "24.8");
    addAge(b, 8, 0, "25.0");
    addAge(b, 8, 1, "25.3");
    addAge(b, 8, 2, "25.5");
    addAge(b, 8, 3, "25.8");
    addAge(b, 8, 4, "26.0");
    addAge(b, 8, 5, "26.3");
    addAge(b, 8, 6, "26.6");
    addAge(b, 8, 7, "26.8");
    addAge(b, 8, 8, "27.1");
    addAge(b, 8, 9, "27.4");
    addAge(b, 8,10, "27.6");
    addAge(b, 8,11, "27.9");
    addAge(b, 9, 0, "28.2");
    addAge(b, 9, 1, "28.5");
    addAge(b, 9, 2, "28.8");
    addAge(b, 9, 3, "29.1");
    addAge(b, 9, 4, "29.4");
    addAge(b, 9, 5, "29.7");
    addAge(b, 9, 6, "30.0");
    addAge(b, 9, 7, "30.3");
    addAge(b, 9, 8, "30.6");
    addAge(b, 9, 9, "30.9");
    addAge(b, 9, 10, "31.2");
    addAge(b, 9, 11, "31.5");
    addAge(b, 10, 0, "31.9");

  
    new XmlParser().setOutputStyle(OutputStyle.PRETTY).compose(new FileOutputStream("c:\\temp\\obs.xml"), b);
  }

  private static void addAge(Bundle b, int y, int m, String v) throws FHIRException {
    Observation obs = new Observation();
    obs.setId("obs-example-age-weight-"+Integer.toString(y)+"-"+Integer.toString(m));
    obs.setSubject(new Reference().setReference("Patient/123"));
    obs.setStatus(ObservationStatus.FINAL);
    Calendar when = Calendar.getInstance();
    when.add(Calendar.YEAR, -y);
    when.add(Calendar.MONTH, m);
    obs.setEffective(new DateTimeType(when));
    obs.getCode().addCoding().setCode("29463-7").setSystem("http://loinc.org");
    obs.setValue(new Quantity());
    obs.getValueQuantity().setCode("kg");
    obs.getValueQuantity().setSystem("http://unitsofmeasure.org");
    obs.getValueQuantity().setUnit("kg");
    obs.getValueQuantity().setValue(new BigDecimal(v));
    b.addEntry().setFullUrl("http://hl7.org/fhir/Observation/"+obs.getId()).setResource(obs);
  }

}
