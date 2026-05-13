#!/bin/bash

# RI Utilities
cp -R /home/james/git/fhir/implementations/java/org.hl7.fhir.utilities/src/org hapi-fhir-utilities/src/main/java/
#cp -R ./hapi-fhir-utilities/src/main/java/org/hl7/fhir/utilities/tests/* ./hapi-fhir-utilities/src/test/java/org/hl7/fhir/utilities/tests/
#rm -R ./hapi-fhir-utilities/src/main/java/org/hl7/fhir/utilities/tests
rm hapi-fhir-utilities/src/main/java/org/hl7/fhir/utilities/xls/XLSXmlNormaliserTests.java
rm hapi-fhir-structures-r4/src/test/java/ca/uhn/fhir/parser/XmlParserR4Test.java
rm hapi-fhir-structures-r4/src/test/java/ca/uhn/fhir/rest/client/ClientServerValidationR4Test.java
rm hapi-fhir-structures-r4/src/test/java/ca/uhn/fhir/rest/server/interceptor/AuthorizationInterceptorR4Test.java
rm hapi-fhir-validation/src/main/java/org/hl7/fhir/conversion/tests/R3R4ConversionTests.java

# RI Validation 
cp -R /home/james/git/fhir/implementations/java/org.hl7.fhir.validation/src/org hapi-fhir-validation/src/main/java/
#cp -R ./hapi-fhir-validation/src/main/java/org/hl7/fhir/validation/dstu3/tests/* ./hapi-fhir-validation/src/test/java/org/hl7/fhir/validation/dstu3/tests/
#rm -R ./hapi-fhir-validation/src/main/java/org/hl7/fhir/validation/dstu3/tests
rm hapi-fhir-validation/src/main/java/org/hl7/fhir/r4/validation/ValidationEngine.java
rm hapi-fhir-validation/src/main/java/org/hl7/fhir/dstu2/validation/ValidationEngine.java
rm hapi-fhir-validation/src/main/java/org/hl7/fhir/dstu2/validation/Validator.java
rm hapi-fhir-validation/src/main/java/org/hl7/fhir/dstu3/validation/Validator.java
rm hapi-fhir-validation/src/main/java/org/hl7/fhir/r4/validation/Validator.java
rm -R hapi-fhir-validation/src/main/java/org/hl7/fhir/validation/dstu3/tests
rm -R hapi-fhir-validation/src/main/java/org/hl7/fhir/validation/r4/tests
rm -R hapi-fhir-validation/src/main/java/org/hl7/fhir/dstu2

# Converter
#cp -R ~/git/fhir/implementations/java/org.hl7.fhir.convertors/src/org ./hapi-fhir-converter/src/main/java/
cp /home/james/git/fhir/implementations/java/org.hl7.fhir.convertors/src/org/hl7/fhir/convertors/VersionConvertorConstants.java  ./hapi-fhir-converter/src/main/java/org/hl7/fhir/convertors/
cat /home/james/git/fhir/implementations/java/org.hl7.fhir.convertors/src/org/hl7/fhir/convertors/VersionConvertor_10_40.java | sed "s/org.hl7.fhir.dstu2.model/org.hl7.fhir.instance.model/g" | sed "s/org.hl7.fhir.dstu2.utils/org.hl7.fhir.instance.utils/g" > ./hapi-fhir-converter/src/main/java/org/hl7/fhir/convertors/VersionConvertor_10_40.java
cp /home/james/git/fhir/implementations/java/org.hl7.fhir.convertors/src/org/hl7/fhir/convertors/VersionConvertor_14_40.java ./hapi-fhir-converter/src/main/java/org/hl7/fhir/convertors/VersionConvertor_14_40.java
cp /home/james/git/fhir/implementations/java/org.hl7.fhir.convertors/src/org/hl7/fhir/convertors/VersionConvertor_30_40.java ./hapi-fhir-converter/src/main/java/org/hl7/fhir/convertors/VersionConvertor_30_40.java
cat /home/james/git/fhir/implementations/java/org.hl7.fhir.dstu2/src/org/hl7/fhir/dstu2/utils/ToolingExtensions.java  | sed "s/org.hl7.fhir.dstu2.model/org.hl7.fhir.instance.model/g" | sed "s/org.hl7.fhir.dstu2.utils/org.hl7.fhir.instance.utils/g" > ./hapi-fhir-structures-hl7org-dstu2/src/main/java/org/hl7/fhir/instance/utils/ToolingExtensions.java


# RI DSTU3 things
cp /home/james/git/fhir/implementations/java/org.hl7.fhir.dstu3/src/org/hl7/fhir/dstu3/utils/IResourceValidator.java hapi-fhir-structures-dstu3/src/main/java/org/hl7/fhir/dstu3/utils/

# RI R4 Model
cp -fR /home/james/git/fhir/implementations/java/org.hl7.fhir.r4/src/org hapi-fhir-structures-r4/src/main/java/
cp -fR /home/james/git/fhir/temp/java/org.hl7.fhir.r4/src/org/hl7/fhir/r4/model/* hapi-fhir-structures-r4/src/main/java/org/hl7/fhir/r4/model/
#cp -R ./hapi-fhir-structures-r4/src/main/java/org/hl7/fhir/r4/test/* hapi-fhir-structures-r4/src/test/java/org/hl7/fhir/r4/test/
rm -R ./hapi-fhir-structures-r4/src/main/java/org/hl7/fhir/r4/test
#cp -R /home/james/git/fhir/temp/java/org.hl7.fhir.r4/src/* hapi-fhir-structures-r4/src/main/java/
cp /home/james/git/fhir/implementations/java/org.hl7.fhir.dstu3/src/org/hl7/fhir/dstu3/utils/ToolingExtensions.java ./hapi-fhir-structures-dstu3/src/main/java/org/hl7/fhir/dstu3/utils/ToolingExtensions.java
cp /home/james/git/fhir/implementations/java/org.hl7.fhir.dstu3/src/org/hl7/fhir/dstu3/utils/ExtensionHelper.java ./hapi-fhir-structures-dstu3/src/main/java/org/hl7/fhir/dstu3/utils/ExtensionHelper.java
cp /home/james/git/fhir/implementations/java/org.hl7.fhir.dstu2016may/src/org/hl7/fhir/dstu2016may/utils/ToolingExtensions.java ./hapi-fhir-structures-dstu2.1/src/main/java/org/hl7/fhir/dstu2016may/utils/ToolingExtensions.java
cp /home/james/git/fhir/implementations/java/org.hl7.fhir.r4/src/org/hl7/fhir/r4/formats/JsonParser.java ./hapi-fhir-structures-r4/src/main/java/org/hl7/fhir/r4/formats/JsonParser.java
cp /home/james/git/fhir/temp/java/org.hl7.fhir.r4/src/org/hl7/fhir/r4/formats/RdfParser.java hapi-fhir-structures-r4/src/main/java/org/hl7/fhir/r4/formats/RdfParser.java

cp /home/james/git/fhir/temp/java/org.hl7.fhir.r4/src/org/hl7/fhir/r4/formats/XmlParser.java ./hapi-fhir-structures-r4/src/main/java/org/hl7/fhir/r4/formats/XmlParser.java
cp /home/james/git/fhir/temp/java/org.hl7.fhir.r4/src/org/hl7/fhir/r4/formats/JsonParser.java ./hapi-fhir-structures-r4/src/main/java/org/hl7/fhir/r4/formats/JsonParser.java


# Resource Spreadsheets
mkdir -p hapi-tinder-plugin/src/main/resources/res/r4/
rm hapi-tinder-plugin/src/main/resources/res/r4/*.xml; cp ~/git/fhir/source/*/*-spreadsheet.xml hapi-tinder-plugin/src/main/resources/res/r4/; rm hapi-tinder-plugin/src/main/resources/res/r4/*-*-*.xml

# Copy Validation Resources
cp ~/git/fhir/publish/*.sch            hapi-fhir-validation-resources-r4/src/main/resources/org/hl7/fhir/r4/model/schema/
cp ~/git/fhir/publish/fhir-single.xsd  hapi-fhir-validation-resources-r4/src/main/resources/org/hl7/fhir/r4/model/schema/
cp ~/git/fhir/publish/fhir-xhtml.xsd   hapi-fhir-validation-resources-r4/src/main/resources/org/hl7/fhir/r4/model/schema/
cp ~/git/fhir/publish/xml.xsd          hapi-fhir-validation-resources-r4/src/main/resources/org/hl7/fhir/r4/model/schema/

cp ~/git/fhir/publish/profiles-*.xml       hapi-fhir-validation-resources-r4/src/main/resources/org/hl7/fhir/r4/model/profile/
cp ~/git/fhir/publish/v2-tables.xml        hapi-fhir-validation-resources-r4/src/main/resources/org/hl7/fhir/r4/model/valueset/
cp ~/git/fhir/publish/v3-codesystems.xml   hapi-fhir-validation-resources-r4/src/main/resources/org/hl7/fhir/r4/model/valueset/
cp ~/git/fhir/publish/valuesets.xml        hapi-fhir-validation-resources-r4/src/main/resources/org/hl7/fhir/r4/model/valueset/
cp ~/git/fhir/publish/extension-definitions.xml hapi-fhir-validation-resources-r4/src/main/resources/org/hl7/fhir/r4/model/extension/
