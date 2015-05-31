#!/bin/sh
cp -v ~/workspace/fhir/trunk/build/implementations/java/org.hl7.fhir.instance/src/org/hl7/fhir/instance/model/*.java hapi-fhir-structures-hl7org-dstu2/src/main/java/org/hl7/fhir/instance/model/
cp -v ~/workspace/fhir/trunk/build/implementations/java/org.hl7.fhir.instance/src/org/hl7/fhir/instance/formats/FormatUtilities.java ~/workspace/hapi-fhir/hapi-fhir-structures-hl7org-dstu2/src/main/java/org/hl7/fhir/instance/formats/FormatUtilities.java
cp -v ~/workspace/fhir/trunk/build/implementations/java/org.hl7.fhir.instance/src/org/hl7/fhir/instance/validation/InstanceValidator.java ~/workspace/hapi-fhir/hapi-fhir-structures-hl7org-dstu2/src/main/java/org/hl7/fhir/instance/validation/InstanceValidator.java 
cp -v ~/workspace/fhir/trunk/build/implementations/java/org.hl7.fhir.instance/src/org/hl7/fhir/instance/validation/ValidationMessage.java ~/workspace/hapi-fhir/hapi-fhir-structures-hl7org-dstu2/src/main/java/org/hl7/fhir/instance/validation/ValidationMessage.java
