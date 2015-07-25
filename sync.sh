#!/bin/sh

for i in $(find ~/workspace/fhirbuild/trunk/build/source -name *-spreadsheet.xml | egrep "/[a-z0-9]+-spreadsheet"); do cp -v $i hapi-tinder-plugin/src/main/resources/res/dstu2/; done

for i in $(find ~/workspace/fhirbuild/trunk/build/source/datatypes | grep xml | grep -v spreadsheet | grep -v -); do cp -v $i hapi-tinder-plugin/src/main/resources/dt/dstu2/; done

cp /Users/james/workspace/fhir/trunk/build/publish/valuesets.xml hapi-tinder-plugin/src/main/resources/vs/dstu2/all-valuesets-bundle.xml

# Schematron
rm hapi-fhir-structures-dstu2/src/main/resources/ca/uhn/fhir/model/dstu2/schema/*.sch; for i in $(ls ~/workspace/fhir/trunk/build/publish/*.sch | grep -v -); do cp -v $i hapi-fhir-structures-dstu2/src/main/resources/ca/uhn/fhir/model/dstu2/schema/; done

# Schema
cp ~/workspace/fhir/trunk/build/publish/fhir-single.xsd hapi-fhir-structures-dstu2/src/main/resources/ca/uhn/fhir/model/dstu2/schema/
cp ~/workspace/fhir/trunk/build/publish/fhir-xhtml.xsd hapi-fhir-structures-dstu2/src/main/resources/ca/uhn/fhir/model/dstu2/schema/
cp ~/workspace/fhir/trunk/build/publish/xml.xsd hapi-fhir-structures-dstu2/src/main/resources/ca/uhn/fhir/model/dstu2/schema/

