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

# Copy Model
#TODO reenable
cp -fvR $FHIRTRUNK/build/implementations/java/org.hl7.fhir.$PACKAGEVERSION/src/org/hl7/fhir/$PACKAGEVERSION/model/*.java hapi-fhir-structures-$PROJVERSION/src/main/java/org/hl7/fhir/$PACKAGEVERSION/model/
#find hapi-fhir-structures-$PROJVERSION/src -name "*.class" | xargs rm -v

# Exception class
cp $FHIRTRUNK/build/implementations/java/org.hl7.fhir.utilities/src/org/hl7/fhir/exceptions/FHIRException.java hapi-fhir-structures-$PROJVERSION/src/main/java/org/hl7/fhir/exceptions/
cp $FHIRTRUNK/build/implementations/java/org.hl7.fhir.utilities/src/org/hl7/fhir/exceptions/FHIRFormatError.java hapi-fhir-structures-$PROJVERSION/src/main/java/org/hl7/fhir/exceptions/

cp $FHIRTRUNK/build/implementations/java/org.hl7.fhir.utilities/src/org/hl7/fhir/utilities/Utilities.java hapi-fhir-structures-$PROJVERSION/src/main/java/org/hl7/fhir/utilities
cp $FHIRTRUNK/build/implementations/java/org.hl7.fhir.utilities/src/org/hl7/fhir/utilities/CSFile.java hapi-fhir-structures-$PROJVERSION/src/main/java/org/hl7/fhir/utilities
cp $FHIRTRUNK/build/implementations/java/org.hl7.fhir.utilities/src/org/hl7/fhir/utilities/MyURIResolver.java hapi-fhir-structures-$PROJVERSION/src/main/java/org/hl7/fhir/utilities
cp $FHIRTRUNK/build/implementations/java/org.hl7.fhir.utilities/src/org/hl7/fhir/utilities/ZipURIResolver.java hapi-fhir-structures-$PROJVERSION/src/main/java/org/hl7/fhir/utilities
cp $FHIRTRUNK/build/implementations/java/org.hl7.fhir.utilities/src/org/hl7/fhir/utilities/Inflector.java hapi-fhir-structures-$PROJVERSION/src/main/java/org/hl7/fhir/utilities
cp $FHIRTRUNK/build/implementations/java/org.hl7.fhir.utilities/src/org/hl7/fhir/utilities/FileNotifier.java hapi-fhir-structures-$PROJVERSION/src/main/java/org/hl7/fhir/utilities
#cp $FHIRTRUNK/build/implementations/java/org.hl7.fhir.utilities/src/org/hl7/fhir/utilities/xhtml/XhtmlNode.java hapi-fhir-structures-$PROJVERSION/src/main/java/org/hl7/fhir/utilities/xhtml/
cp $FHIRTRUNK/build/implementations/java/org.hl7.fhir.utilities/src/org/hl7/fhir/utilities/xhtml/XhtmlDocument.java hapi-fhir-structures-$PROJVERSION/src/main/java/org/hl7/fhir/utilities/xhtml/
cp $FHIRTRUNK/build/implementations/java/org.hl7.fhir.utilities/src/org/hl7/fhir/utilities/xhtml/NodeType.java hapi-fhir-structures-$PROJVERSION/src/main/java/org/hl7/fhir/utilities/xhtml/
cp $FHIRTRUNK/build/implementations/java/org.hl7.fhir.utilities/src/org/hl7/fhir/utilities/xhtml/XhtmlParser.java hapi-fhir-structures-$PROJVERSION/src/main/java/org/hl7/fhir/utilities/xhtml/
cp $FHIRTRUNK/build/implementations/java/org.hl7.fhir.utilities/src/org/hl7/fhir/utilities/xhtml/XhtmlComposer.java hapi-fhir-structures-$PROJVERSION/src/main/java/org/hl7/fhir/utilities/xhtml/
cp $FHIRTRUNK/build/implementations/java/org.hl7.fhir.utilities/src/org/hl7/fhir/utilities/Table.java hapi-fhir-structures-$PROJVERSION/src/main/java/org/hl7/fhir/utilities/
cp $FHIRTRUNK/build/implementations/java/org.hl7.fhir.utilities/src/org/hl7/fhir/utilities/CommaSeparatedStringBuilder.java hapi-fhir-structures-$PROJVERSION/src/main/java/org/hl7/fhir/utilities/
cp $FHIRTRUNK/build/implementations/java/org.hl7.fhir.utilities/src/org/hl7/fhir/utilities/xml/XMLUtil.java hapi-fhir-structures-$PROJVERSION/src/main/java/org/hl7/fhir/utilities/xml/

# Formats
cp $FHIRTRUNK/build/implementations/java/org.hl7.fhir.$PACKAGEVERSION/src/org/hl7/fhir/$PACKAGEVERSION/formats/IParser.java hapi-fhir-structures-$PROJVERSION/src/main/java/org/hl7/fhir/$PACKAGEVERSION/formats/
cp $FHIRTRUNK/build/implementations/java/org.hl7.fhir.$PACKAGEVERSION/src/org/hl7/fhir/$PACKAGEVERSION/formats/ParserType.java hapi-fhir-structures-$PROJVERSION/src/main/java/org/hl7/fhir/$PACKAGEVERSION/formats/
cp $FHIRTRUNK/build/implementations/java/org.hl7.fhir.$PACKAGEVERSION/src/org/hl7/fhir/$PACKAGEVERSION/formats/FormatUtilities.java hapi-fhir-structures-$PROJVERSION/src/main/java/org/hl7/fhir/$PACKAGEVERSION/formats/

# Terminologies
cp $FHIRTRUNK/build/implementations/java/org.hl7.fhir.$PACKAGEVERSION/src/org/hl7/fhir/$PACKAGEVERSION/terminologies/ValueSetExpander.java hapi-fhir-structures-$PROJVERSION/src/main/java/org/hl7/fhir/$PACKAGEVERSION/terminologies/
cp $FHIRTRUNK/build/implementations/java/org.hl7.fhir.$PACKAGEVERSION/src/org/hl7/fhir/$PACKAGEVERSION/terminologies/ValueSetChecker.java hapi-fhir-structures-$PROJVERSION/src/main/java/org/hl7/fhir/$PACKAGEVERSION/terminologies/
cp $FHIRTRUNK/build/implementations/java/org.hl7.fhir.$PACKAGEVERSION/src/org/hl7/fhir/$PACKAGEVERSION/terminologies/ValueSetExpanderSimple.java hapi-fhir-structures-$PROJVERSION/src/main/java/org/hl7/fhir/$PACKAGEVERSION/terminologies/
cp $FHIRTRUNK/build/implementations/java/org.hl7.fhir.$PACKAGEVERSION/src/org/hl7/fhir/$PACKAGEVERSION/terminologies/ValueSetExpanderFactory.java hapi-fhir-structures-$PROJVERSION/src/main/java/org/hl7/fhir/$PACKAGEVERSION/terminologies/
cp $FHIRTRUNK/build/implementations/java/org.hl7.fhir.$PACKAGEVERSION/src/org/hl7/fhir/$PACKAGEVERSION/terminologies/ValueSetCheckerSimple.java hapi-fhir-structures-$PROJVERSION/src/main/java/org/hl7/fhir/$PACKAGEVERSION/terminologies/

# Utils
cp $FHIRTRUNK/build/implementations/java/org.hl7.fhir.$PACKAGEVERSION/src/org/hl7/fhir/$PACKAGEVERSION/utils/ToolingExtensions.java hapi-fhir-structures-$PROJVERSION/src/main/java/org/hl7/fhir/$PACKAGEVERSION/utils/
cp $FHIRTRUNK/build/implementations/java/org.hl7.fhir.$PACKAGEVERSION/src/org/hl7/fhir/$PACKAGEVERSION/utils/IWorkerContext.java hapi-fhir-structures-$PROJVERSION/src/main/java/org/hl7/fhir/$PACKAGEVERSION/utils/
cp $FHIRTRUNK/build/implementations/java/org.hl7.fhir.$PACKAGEVERSION/src/org/hl7/fhir/$PACKAGEVERSION/utils/INarrativeGenerator.java hapi-fhir-structures-$PROJVERSION/src/main/java/org/hl7/fhir/$PACKAGEVERSION/utils/
cp $FHIRTRUNK/build/implementations/java/org.hl7.fhir.$PACKAGEVERSION/src/org/hl7/fhir/$PACKAGEVERSION/utils/EOperationOutcome.java hapi-fhir-structures-$PROJVERSION/src/main/java/org/hl7/fhir/$PACKAGEVERSION/utils/
cp $FHIRTRUNK/build/implementations/java/org.hl7.fhir.$PACKAGEVERSION/src/org/hl7/fhir/$PACKAGEVERSION/utils/FHIRPathEngine.java hapi-fhir-structures-$PROJVERSION/src/main/java/org/hl7/fhir/$PACKAGEVERSION/utils/

# Validation
cp $FHIRTRUNK/build/implementations/java/org.hl7.fhir.$PACKAGEVERSION/src/org/hl7/fhir/$PACKAGEVERSION/validation/* hapi-fhir-structures-$PROJVERSION/src/main/java/org/hl7/fhir/$PACKAGEVERSION/validation/
cp $FHIRTRUNK/build/implementations/java/org.hl7.fhir.$PACKAGEVERSION/src/org/hl7/fhir/$PACKAGEVERSION/exceptions/* hapi-fhir-structures-$PROJVERSION/src/main/java/org/hl7/fhir/$PACKAGEVERSION/exceptions/
rm hapi-fhir-structures-$PROJVERSION/src/main/java/org/hl7/fhir/$PACKAGEVERSION/validation/Validator.java
rm hapi-fhir-structures-$PROJVERSION/src/main/java/org/hl7/fhir/$PACKAGEVERSION/validation/ValidationEngine.java
rm hapi-fhir-structures-$PROJVERSION/src/main/java/org/hl7/fhir/$PACKAGEVERSION/validation/ProfileValidator.java
rm hapi-fhir-structures-$PROJVERSION/src/main/java/org/hl7/fhir/$PACKAGEVERSION/validation/ProfileValidatorTests.java

# XML
cp $FHIRTRUNK/build/implementations/java/org.hl7.fhir.utilities/src/org/hl7/fhir/utilities/xml/IXMLWriter.java hapi-fhir-structures-$PROJVERSION/src/main/java/org/hl7/fhir/utilities/xml/
