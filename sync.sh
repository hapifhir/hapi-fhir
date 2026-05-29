#!/bin/sh

set -e

FHIRTRUNK=~/workspace/fhir/trunk
DIRVERSION=dstu21
PROJVERSION=dstu2.1
PACKAGEVERSION=dstu21

# Resource Definitions
rm hapi-tinder-plugin/src/main/resources/res/$DIRVERSION/*
for i in $(find $FHIRTRUNK/build/source -name *-spreadsheet.xml | egrep "/[a-z0-9]+-spreadsheet"); do cp -v $i hapi-tinder-plugin/src/main/resources/res/$DIRVERSION/; done

# Datatype Definitions
rm hapi-tinder-plugin/src/main/resources/dt/$DIRVERSION/*
for i in $(find $FHIRTRUNK/build/source/datatypes | grep xml | grep -v spreadsheet | grep -v -); do cp -v $i hapi-tinder-plugin/src/main/resources/dt/$DIRVERSION/; done

# Compartments
cp ~/workspace/fhir/trunk/build/source/compartments.xml  hapi-tinder-plugin/src/main/resources/compartment/

# ValueSets
cp $FHIRTRUNK/build/publish/valuesets.xml hapi-fhir-validation-resources-$PROJVERSION/src/main/resources/org/hl7/fhir/instance/model/$PACKAGEVERSION/valueset/
cp $FHIRTRUNK/build/publish/v3-codesystems.xml hapi-fhir-validation-resources-$PROJVERSION/src/main/resources/org/hl7/fhir/instance/model/$PACKAGEVERSION/valueset/
cp $FHIRTRUNK/build/publish/v2-tables.xml hapi-fhir-validation-resources-$PROJVERSION/src/main/resources/org/hl7/fhir/instance/model/$PACKAGEVERSION/valueset/

# Profiles
touch ./hapi-fhir-validation-resources-$PROJVERSION/src/main/resources/org/hl7/fhir/instance/model/$PACKAGEVERSION/profile/_.xml
rm ./hapi-fhir-validation-resources-$PROJVERSION/src/main/resources/org/hl7/fhir/instance/model/$PACKAGEVERSION/profile/*.xml
for i in $(find $FHIRTRUNK/build/publish | grep -E "publish\/[a-z]+\.profile.xml$"); do echo $i; cp $i hapi-fhir-validation-resources-$PROJVERSION/src/main/resources/org/hl7/fhir/instance/model/$PACKAGEVERSION/profile/; done

# Schematron
touch hapi-fhir-validation-resources-$PROJVERSION/src/main/resources/org/hl7/fhir/instance/model/$PACKAGEVERSION/schema/a.sch
rm hapi-fhir-validation-resources-$PROJVERSION/src/main/resources/org/hl7/fhir/instance/model/$PACKAGEVERSION/schema/*.sch
for i in $(ls $FHIRTRUNK/build/publish/*.sch | grep -v -); do cp -v $i ./hapi-fhir-validation-resources-$PROJVERSION/src/main/resources/org/hl7/fhir/instance/model/$PACKAGEVERSION/schema/; done

# Schema
cp $FHIRTRUNK/build/publish/fhir-single.xsd ./hapi-fhir-validation-resources-$PROJVERSION/src/main/resources/org/hl7/fhir/instance/model/$PACKAGEVERSION/schema/
cp $FHIRTRUNK/build/publish/fhir-xhtml.xsd ./hapi-fhir-validation-resources-$PROJVERSION/src/main/resources/org/hl7/fhir/instance/model/$PACKAGEVERSION/schema/
cp $FHIRTRUNK/build/publish/xml.xsd ./hapi-fhir-validation-resources-$PROJVERSION/src/main/resources/org/hl7/fhir/instance/model/$PACKAGEVERSION/schema/

#find hapi-tinder-plugin/src/main/resources/res/$DIRVERSION | sed "s|.*/|<baseResourceName>|" | sed "s/-spread.*/<\/baseResourceName>/" | sort

