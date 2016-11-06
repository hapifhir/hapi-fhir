#!/usr/bin/fish

# Note: Element needs to implement IBaseElement

# Copy Resource Models
for i in (grep -lir ResourceDef ~/workspace/fhir/trunk/build/implementations/java/org.hl7.fhir.dstu3/src/org/hl7/fhir/dstu3/model/*.java); cp $i hapi-fhir-structures-dstu3/src/main/java/org/hl7/fhir/dstu3/model/; end

# Copy Datatype Models
for i in (grep -lir DatatypeDef ~/workspace/fhir/trunk/build/implementations/java/org.hl7.fhir.dstu3/src/org/hl7/fhir/dstu3/model/*.java); cp $i hapi-fhir-structures-dstu3/src/main/java/org/hl7/fhir/dstu3/model/; end

# Copy CodeSystems
cp ~/workspace/fhir/trunk/build/temp/java/org.hl7.fhir.dstu3/src/org/hl7/fhir/dstu3/model/* hapi-fhir-structures-dstu3/src/main/java/org/hl7/fhir/dstu3/model/

# Copy Spreadsheets
rm hapi-tinder-plugin/src/main/resources/res/dstu3/*.xml; cp ~/workspace/fhir/trunk/build/source/*/*-spreadsheet.xml hapi-tinder-plugin/src/main/resources/res/dstu3/; rm hapi-tinder-plugin/src/main/resources/res/dstu3/*-*-*.xml

cp ~/workspace/fhir/trunk/build/publish/*.sch hapi-fhir-validation-resources-dstu3/src/main/resources/org/hl7/fhir/instance/model/dstu3/schema/

cp ~/workspace/fhir/trunk/build/publish/fhir-single.xsd  hapi-fhir-validation-resources-dstu3/src/main/resources/org/hl7/fhir/instance/model/dstu3/schema/
cp ~/workspace/fhir/trunk/build/publish/fhir-xhtml.xsd   hapi-fhir-validation-resources-dstu3/src/main/resources/org/hl7/fhir/instance/model/dstu3/schema/
cp ~/workspace/fhir/trunk/build/publish/xml.xsd   hapi-fhir-validation-resources-dstu3/src/main/resources/org/hl7/fhir/instance/model/dstu3/schema/

cp ~/workspace/fhir/trunk/build/publish/profiles-*.xml hapi-fhir-validation-resources-dstu3/src/main/resources/org/hl7/fhir/instance/model/dstu3/profile/
cp ~/workspace/fhir/trunk/build/publish/v2-tables.xml  hapi-fhir-validation-resources-dstu3/src/main/resources/org/hl7/fhir/instance/model/dstu3/valueset/
cp ~/workspace/fhir/trunk/build/publish/v3-codesystems.xml   hapi-fhir-validation-resources-dstu3/src/main/resources/org/hl7/fhir/instance/model/dstu3/valueset/
cp ~/workspace/fhir/trunk/build/publish/valuesets.xml    hapi-fhir-validation-resources-dstu3/src/main/resources/org/hl7/fhir/instance/model/dstu3/valueset/
cp ~/workspace/fhir/trunk/build/implementations/java/org.hl7.fhir.dstu3/src/org/hl7/fhir/dstu3/utils/FluentPathEngine.java hapi-fhir-structures-dstu3/src/main/java/org/hl7/fhir/dstu3/utils/
cp ~/workspace/fhir/trunk/build/implementations/java/org.hl7.fhir.dstu3/src/org/hl7/fhir/dstu3/validation/InstanceValidator.java  hapi-fhir-structures-dstu3/src/main/java/org/hl7/fhir/dstu3/validation/
cp ~/workspace/fhir/trunk/build/implementations/java/org.hl7.fhir.utilities/src/org/hl7/fhir/utilities/xhtml/HierarchicalTableGenerator.java    hapi-fhir-structures-dstu3/src/main/java/org/hl7/fhir/utilities/xhtml/
cp ~/workspace/fhir/trunk/build/implementations/java/org.hl7.fhir.dstu3/src/org/hl7/fhir/dstu3/terminologies/ValueSetExpanderSimple.java   hapi-fhir-structures-dstu3/src/main/java/org/hl7/fhir/dstu3/terminologies/
cp ~/workspace/fhir/trunk/build/implementations/java/org.hl7.fhir.dstu3/src/org/hl7/fhir/dstu3/terminologies/ValueSetExpander.java   hapi-fhir-structures-dstu3/src/main/java/org/hl7/fhir/dstu3/terminologies/
cp ~/workspace/fhir/trunk/build/implementations/java/org.hl7.fhir.dstu3/src/org/hl7/fhir/dstu3/terminologies/ValueSetExpansionCache.java   hapi-fhir-structures-dstu3/src/main/java/org/hl7/fhir/dstu3/terminologies/
cp ~/workspace/fhir/trunk/build/implementations/java/org.hl7.fhir.dstu3/src/org/hl7/fhir/dstu3/terminologies/ValueSetCheckerSimple.java   hapi-fhir-structures-dstu3/src/main/java/org/hl7/fhir/dstu3/terminologies/
cp /home/james/workspace/fhir/trunk/build/implementations/java/org.hl7.fhir.dstu3/src/org/hl7/fhir/dstu3/model/ExpressionNode.java hapi-fhir-structures-dstu3/src/main/java/org/hl7/fhir/dstu3/model/
cp ~/workspace/fhir/trunk/build/implementations/java/org.hl7.fhir.dstu3/src/org/hl7/fhir/dstu3/model/Base.java hapi-fhir-structures-dstu3/src/main/java/org/hl7/fhir/dstu3/model/
cp ~/workspace/fhir/trunk/build/implementations/java/org.hl7.fhir.dstu3/src/org/hl7/fhir/dstu3/elementmodel/Element.java  hapi-fhir-structures-dstu3/src/main/java/org/hl7/fhir/dstu3/elementmodel/
cp ~/workspace/fhir/trunk/build/implementations/java/org.hl7.fhir.dstu3/src/org/hl7/fhir/dstu3/elementmodel/Property.java   hapi-fhir-structures-dstu3/src/main/java/org/hl7/fhir/dstu3/elementmodel/
cp ~/workspace/fhir/trunk/build/implementations/java/org.hl7.fhir.dstu3/src/org/hl7/fhir/dstu3/conformance/ProfileUtilities.java   hapi-fhir-structures-dstu3/src/main/java/org/hl7/fhir/dstu3/conformance/
cp ~/workspace/fhir/trunk/build/implementations/java/org.hl7.fhir.dstu3/src/org/hl7/fhir/dstu3/elementmodel/ObjectConverter.java  hapi-fhir-structures-dstu3/src/main/java/org/hl7/fhir/dstu3/elementmodel/

