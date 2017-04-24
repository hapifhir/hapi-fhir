#!/usr/bin/fish

# Note: Element needs to implement IBaseElement

# Copy Resource Models
for i in (grep -lir ResourceDef ~/workspace/fhir/tags/stu3/build/implementations/java/org.hl7.fhir.dstu3/src/org/hl7/fhir/dstu3/model/*.java); cp $i hapi-fhir-structures-dstu3/src/main/java/org/hl7/fhir/dstu3/model/; end

# Copy Datatype Models
for i in (grep -lir DatatypeDef ~/workspace/fhir/tags/stu3/build/implementations/java/org.hl7.fhir.dstu3/src/org/hl7/fhir/dstu3/model/*.java); cp $i hapi-fhir-structures-dstu3/src/main/java/org/hl7/fhir/dstu3/model/; end

# Copy CodeSystems
rm hapi-fhir-structures-dstu3/src/main/java/org/hl7/fhir/dstu3/model/codesystems/*
cp ~/workspace/fhir/tags/stu3/build/temp/java/org.hl7.fhir.dstu3/src/org/hl7/fhir/dstu3/model/* hapi-fhir-structures-dstu3/src/main/java/org/hl7/fhir/dstu3/model/
cp ~/workspace/fhir/tags/stu3/build/temp/java/org.hl7.fhir.dstu3/src/org/hl7/fhir/dstu3/model/codesystems/* hapi-fhir-structures-dstu3/src/main/java/org/hl7/fhir/dstu3/model/codesystems/


cp ~/workspace/fhir/tags/stu3/build/implementations/java/org.hl7.fhir.dstu3/src/org/hl7/fhir/dstu3/utils/FHIRPathEngine.java hapi-fhir-structures-dstu3/src/main/java/org/hl7/fhir/dstu3/utils/
cp ~/workspace/fhir/tags/stu3/build/implementations/java/org.hl7.fhir.utilities/src/org/hl7/fhir/utilities/xhtml/HierarchicalTableGenerator.java    hapi-fhir-structures-dstu3/src/main/java/org/hl7/fhir/utilities/xhtml/
cp ~/workspace/fhir/tags/stu3/build/implementations/java/org.hl7.fhir.dstu3/src/org/hl7/fhir/dstu3/terminologies/ValueSetExpanderSimple.java   hapi-fhir-structures-dstu3/src/main/java/org/hl7/fhir/dstu3/terminologies/
cp ~/workspace/fhir/tags/stu3/build/implementations/java/org.hl7.fhir.dstu3/src/org/hl7/fhir/dstu3/terminologies/ValueSetExpander.java   hapi-fhir-structures-dstu3/src/main/java/org/hl7/fhir/dstu3/terminologies/
cp ~/workspace/fhir/tags/stu3/build/implementations/java/org.hl7.fhir.dstu3/src/org/hl7/fhir/dstu3/terminologies/ValueSetExpansionCache.java   hapi-fhir-structures-dstu3/src/main/java/org/hl7/fhir/dstu3/terminologies/
cp ~/workspace/fhir/tags/stu3/build/implementations/java/org.hl7.fhir.dstu3/src/org/hl7/fhir/dstu3/terminologies/ValueSetCheckerSimple.java   hapi-fhir-structures-dstu3/src/main/java/org/hl7/fhir/dstu3/terminologies/
cp /home/james/workspace/fhir/tags/stu3/build/implementations/java/org.hl7.fhir.dstu3/src/org/hl7/fhir/dstu3/model/ExpressionNode.java hapi-fhir-structures-dstu3/src/main/java/org/hl7/fhir/dstu3/model/
cp ~/workspace/fhir/tags/stu3/build/implementations/java/org.hl7.fhir.dstu3/src/org/hl7/fhir/dstu3/model/Base.java hapi-fhir-structures-dstu3/src/main/java/org/hl7/fhir/dstu3/model/
cp ~/workspace/fhir/tags/stu3/build/implementations/java/org.hl7.fhir.dstu3/src/org/hl7/fhir/dstu3/elementmodel/Element.java  hapi-fhir-structures-dstu3/src/main/java/org/hl7/fhir/dstu3/elementmodel/
cp ~/workspace/fhir/tags/stu3/build/implementations/java/org.hl7.fhir.dstu3/src/org/hl7/fhir/dstu3/elementmodel/Property.java   hapi-fhir-structures-dstu3/src/main/java/org/hl7/fhir/dstu3/elementmodel/
cp ~/workspace/fhir/tags/stu3/build/implementations/java/org.hl7.fhir.dstu3/src/org/hl7/fhir/dstu3/conformance/ProfileUtilities.java   hapi-fhir-structures-dstu3/src/main/java/org/hl7/fhir/dstu3/conformance/
cp ~/workspace/fhir/tags/stu3/build/implementations/java/org.hl7.fhir.dstu3/src/org/hl7/fhir/dstu3/elementmodel/ObjectConverter.java  hapi-fhir-structures-dstu3/src/main/java/org/hl7/fhir/dstu3/elementmodel/

cp ~/workspace/fhir/tags/stu3/build/implementations/java/org.hl7.fhir.dstu3/src/org/hl7/fhir/dstu3/elementmodel/XmlParser.java  hapi-fhir-structures-dstu3/src/main/java/org/hl7/fhir/dstu3/elementmodel/
cp ~/workspace/fhir/tags/stu3/build/implementations/java/org.hl7.fhir.dstu3/src/org/hl7/fhir/dstu3/elementmodel/JsonParser.java  hapi-fhir-structures-dstu3/src/main/java/org/hl7/fhir/dstu3/elementmodel/

cp /home/james/workspace/fhir/tags/stu3/build/implementations/java/org.hl7.fhir.dstu3/src/org/hl7/fhir/dstu3/utils/StructureMapUtilities.java hapi-fhir-structures-dstu3/src/main/java/org/hl7/fhir/dstu3/utils/
cp /home/james/workspace/fhir/tags/stu3/build/implementations/java/org.hl7.fhir.dstu3/src/org/hl7/fhir/dstu3/utils/formats/JsonTrackingParser.java  hapi-fhir-structures-dstu3/src/main/java/org/hl7/fhir/dstu3/utils/formats/

cp /home/james/workspace/fhir/tags/stu3/build/implementations/java/org.hl7.fhir.dstu3/src/org/hl7/fhir/dstu3/utils/formats/XmlLocationAnnotator.java   hapi-fhir-structures-dstu3/src/main/java/org/hl7/fhir/dstu3/utils/formats/
cp /home/james/workspace/fhir/tags/stu3/build/implementations/java/org.hl7.fhir.dstu3/src/org/hl7/fhir/dstu3/utils/formats/XmlLocationData.java   hapi-fhir-structures-dstu3/src/main/java/org/hl7/fhir/dstu3/utils/formats/

cp /home/james/workspace/fhir/tags/stu3/build/implementations/java/org.hl7.fhir.dstu3/src/org/hl7/fhir/dstu3/terminologies/CodeSystemUtilities.java   hapi-fhir-structures-dstu3/src/main/java/org/hl7/fhir/dstu3/terminologies/


# Validation
rm hapi-fhir-validator/src/main/java/org/hl7/fhir/dstu3/validation/*
cp ~/workspace/fhir/tags/stu3/build/implementations/java/org.hl7.fhir.validation/src/org/hl7/fhir/dstu3/validator/*.java hapi-fhir-structures-dstu3/src/main/java/org/hl7/fhir/dstu3/validation/
cp ~/workspace/fhir/tags/stu3/build/implementations/java/org.hl7.fhir.utilities/src/org/hl7/fhir/utilities/validation/ValidationMessage.java hapi-fhir-validator/src/main/java/org/hl7/fhir/utilities/validation/
 cp ~/workspace/fhir/tags/stu3/build/implementations/java/org.hl7.fhir.dstu3/src/org/hl7/fhir/dstu3/utils/IResourceValidator.java hapi-fhir-validator/src/main/java/org/hl7/fhir/dstu3/utils/
cp ~/workspace/fhir/tags/stu3/build/implementations/java/org.hl7.fhir.dstu3/src/org/hl7/fhir/dstu3/utils/ValidationProfileSet.java  hapi-fhir-validator/src/main/java/org/hl7/fhir/dstu3/utils/



 
# Copy Spreadsheets
rm hapi-tinder-plugin/src/main/resources/res/dstu3/*.xml; cp ~/workspace/fhir/tags/stu3/build/source/*/*-spreadsheet.xml hapi-tinder-plugin/src/main/resources/res/dstu3/; rm hapi-tinder-plugin/src/main/resources/res/dstu3/*-*-*.xml

# Copy Validation Resources
cp ~/workspace/fhir/tags/stu3/build/publish/*.sch hapi-fhir-validation-resources-dstu3/src/main/resources/org/hl7/fhir/instance/model/dstu3/schema/

cp ~/workspace/fhir/tags/stu3/build/publish/fhir-single.xsd  hapi-fhir-validation-resources-dstu3/src/main/resources/org/hl7/fhir/instance/model/dstu3/schema/
cp ~/workspace/fhir/tags/stu3/build/publish/fhir-xhtml.xsd   hapi-fhir-validation-resources-dstu3/src/main/resources/org/hl7/fhir/instance/model/dstu3/schema/
cp ~/workspace/fhir/tags/stu3/build/publish/xml.xsd   hapi-fhir-validation-resources-dstu3/src/main/resources/org/hl7/fhir/instance/model/dstu3/schema/

cp ~/workspace/fhir/tags/stu3/build/publish/profiles-*.xml hapi-fhir-validation-resources-dstu3/src/main/resources/org/hl7/fhir/instance/model/dstu3/profile/
cp ~/workspace/fhir/tags/stu3/build/publish/v2-tables.xml  hapi-fhir-validation-resources-dstu3/src/main/resources/org/hl7/fhir/instance/model/dstu3/valueset/
cp ~/workspace/fhir/tags/stu3/build/publish/v3-codesystems.xml   hapi-fhir-validation-resources-dstu3/src/main/resources/org/hl7/fhir/instance/model/dstu3/valueset/
cp ~/workspace/fhir/tags/stu3/build/publish/valuesets.xml    hapi-fhir-validation-resources-dstu3/src/main/resources/org/hl7/fhir/instance/model/dstu3/valueset/

