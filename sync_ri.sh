#!/bin/sh

# Model
cp -v ~/workspace/fhir/trunk/build/implementations/java/org.hl7.fhir.instance/src/org/hl7/fhir/instance/model/*.java hapi-fhir-structures-hl7org-dstu2/src/main/java/org/hl7/fhir/instance/model/
cp -v ~/workspace/fhir/trunk/build/implementations/java/org.hl7.fhir.instance/src/org/hl7/fhir/instance/formats/FormatUtilities.java ~/workspace/hapi-fhir/hapi-fhir-structures-hl7org-dstu2/src/main/java/org/hl7/fhir/instance/formats/FormatUtilities.java
cp -v ~/workspace/fhir/trunk/build/implementations/java/org.hl7.fhir.instance/src/org/hl7/fhir/instance/validation/InstanceValidator.java ~/workspace/hapi-fhir/hapi-fhir-structures-hl7org-dstu2/src/main/java/org/hl7/fhir/instance/validation/InstanceValidator.java 
cp -v ~/workspace/fhir/trunk/build/implementations/java/org.hl7.fhir.instance/src/org/hl7/fhir/instance/validation/ValidationMessage.java ~/workspace/hapi-fhir/hapi-fhir-structures-hl7org-dstu2/src/main/java/org/hl7/fhir/instance/validation/ValidationMessage.java

# Schemas
cp ~/workspace/fhir/trunk/build/publish/fhir-single.xsd ~/workspace/hapi-fhir/hapi-fhir-structures-hl7org-dstu2/src/main/resources/org/hl7/fhir/instance/model/schema/
cp ~/workspace/fhir/trunk/build/publish/fhir-xhtml.xsd ~/workspace/hapi-fhir/hapi-fhir-structures-hl7org-dstu2/src/main/resources/org/hl7/fhir/instance/model/schema/
cp ~/workspace/fhir/trunk/build/publish/xml.xsd ~/workspace/hapi-fhir/hapi-fhir-structures-hl7org-dstu2/src/main/resources/org/hl7/fhir/instance/model/schema/
for i in $( ls ~/workspace/fhir/trunk/build/publish/*.sch | grep -v - ); do cp $i hapi-fhir-structures-hl7org-dstu2/src/main/resources/org/hl7/fhir/instance/model/schema; done
