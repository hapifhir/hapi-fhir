#!/bin/bash

java -jar ~/Development/HL7-GForge/fhir/trunk/build/publish/org.hl7.fhir.validator.jar patient-example-a.xml -defn ~/Development/HL7-GForge/fhir/trunk/build/publish/validation-min.xml.zip -profile nl-core-patient
