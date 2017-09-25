#!/bin/bash

# RI Utilities
cp -R /home/james/workspace/fhir/trunk/build/implementations/java/org.hl7.fhir.utilities/src/org hapi-fhir-utilities/src/main/java/
#cp -R ./hapi-fhir-utilities/src/main/java/org/hl7/fhir/utilities/tests/* ./hapi-fhir-utilities/src/test/java/org/hl7/fhir/utilities/tests/
#rm -R ./hapi-fhir-utilities/src/main/java/org/hl7/fhir/utilities/tests

# RI Validation 
cp -R /home/james/workspace/fhir/trunk/build/implementations/java/org.hl7.fhir.validation/src/org hapi-fhir-validation/src/main/java/
#cp -R ./hapi-fhir-validation/src/main/java/org/hl7/fhir/validation/dstu3/tests/* ./hapi-fhir-validation/src/test/java/org/hl7/fhir/validation/dstu3/tests/
#rm -R ./hapi-fhir-validation/src/main/java/org/hl7/fhir/validation/dstu3/tests

# Converter
#cp -R ~/workspace/fhir/trunk/build/implementations/java/org.hl7.fhir.convertors/src/org ./hapi-fhir-converter/src/main/java/

# RI R4 Model
cp -R /home/james/workspace/fhir/trunk/build/implementations/java/org.hl7.fhir.r4/src/org hapi-fhir-structures-r4/src/main/java/
cp -R ./hapi-fhir-structures-r4/src/main/java/org/hl7/fhir/r4/test/* hapi-fhir-structures-r4/src/test/java/org/hl7/fhir/r4/test/
rm -R ./hapi-fhir-structures-r4/src/main/java/org/hl7/fhir/r4/test
cp -R /home/james/workspace/fhir/trunk/build/temp/java/org.hl7.fhir.r4/src/* hapi-fhir-structures-r4/src/main/java/


# Resource Spreadsheets
mkdir -p hapi-tinder-plugin/src/main/resources/res/r4/
rm hapi-tinder-plugin/src/main/resources/res/r4/*.xml; cp ~/workspace/fhir/trunk/build/source/*/*-spreadsheet.xml hapi-tinder-plugin/src/main/resources/res/r4/; rm hapi-tinder-plugin/src/main/resources/res/r4/*-*-*.xml

# Copy Validation Resources
cp ~/workspace/fhir/trunk/build/publish/*.sch            hapi-fhir-validation-resources-r4/src/main/resources/org/hl7/fhir/r4/model/schema/
cp ~/workspace/fhir/trunk/build/publish/fhir-single.xsd  hapi-fhir-validation-resources-r4/src/main/resources/org/hl7/fhir/r4/model/schema/
cp ~/workspace/fhir/trunk/build/publish/fhir-xhtml.xsd   hapi-fhir-validation-resources-r4/src/main/resources/org/hl7/fhir/r4/model/schema/
cp ~/workspace/fhir/trunk/build/publish/xml.xsd          hapi-fhir-validation-resources-r4/src/main/resources/org/hl7/fhir/r4/model/schema/

cp ~/workspace/fhir/trunk/build/publish/profiles-*.xml       hapi-fhir-validation-resources-r4/src/main/resources/org/hl7/fhir/r4/model/profile/
cp ~/workspace/fhir/trunk/build/publish/v2-tables.xml        hapi-fhir-validation-resources-r4/src/main/resources/org/hl7/fhir/r4/model/valueset/
cp ~/workspace/fhir/trunk/build/publish/v3-codesystems.xml   hapi-fhir-validation-resources-r4/src/main/resources/org/hl7/fhir/r4/model/valueset/
cp ~/workspace/fhir/trunk/build/publish/valuesets.xml        hapi-fhir-validation-resources-r4/src/main/resources/org/hl7/fhir/r4/model/valueset/
