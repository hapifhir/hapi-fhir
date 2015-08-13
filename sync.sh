#!/bin/sh

FHIRTRUNK=~/workspace/fhir/trunk

rm hapi-tinder-plugin/src/main/resources/res/dstu2/*
for i in $(find $FHIRTRUNK/build/source -name *-spreadsheet.xml | egrep "/[a-z0-9]+-spreadsheet"); do cp -v $i hapi-tinder-plugin/src/main/resources/res/dstu2/; done

rm hapi-tinder-plugin/src/main/resources/dt/dstu2/*
for i in $(find $FHIRTRUNK/build/source/datatypes | grep xml | grep -v spreadsheet | grep -v -); do cp -v $i hapi-tinder-plugin/src/main/resources/dt/dstu2/; done

cp $FHIRTRUNK/build/publish/valuesets.xml hapi-tinder-plugin/src/main/resources/vs/dstu2/all-valuesets-bundle.xml

# Schematron
rm hapi-fhir-structures-dstu2/src/main/resources/ca/uhn/fhir/model/dstu2/schema/*.sch
for i in $(ls $FHIRTRUNK/build/publish/*.sch | grep -v -); do cp -v $i hapi-fhir-structures-dstu2/src/main/resources/ca/uhn/fhir/model/dstu2/schema/; done

# Schema
cp $FHIRTRUNK/build/publish/fhir-single.xsd hapi-fhir-structures-dstu2/src/main/resources/ca/uhn/fhir/model/dstu2/schema/
cp $FHIRTRUNK/build/publish/fhir-xhtml.xsd hapi-fhir-structures-dstu2/src/main/resources/ca/uhn/fhir/model/dstu2/schema/
cp $FHIRTRUNK/build/publish/xml.xsd hapi-fhir-structures-dstu2/src/main/resources/ca/uhn/fhir/model/dstu2/schema/

