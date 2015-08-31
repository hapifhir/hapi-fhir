#!/bin/sh

FHIRTRUNK=~/workspace/fhir/trunk
FHIRTRUNK=~/workspace/fhir/trunk

# Model
rm hapi-fhir-structures-hl7org-dstu2/src/main/java/org/hl7/fhir/instance/model/*.java
cp -vp $FHIRTRUNK/build/implementations/java/org.hl7.fhir.instance/src/org/hl7/fhir/instance/model/*.java hapi-fhir-structures-hl7org-dstu2/src/main/java/org/hl7/fhir/instance/model/
cp -vp $FHIRTRUNK/build/implementations/java/org.hl7.fhir.instance/src/org/hl7/fhir/instance/model/valuesets/*.java hapi-fhir-structures-hl7org-dstu2/src/main/java/org/hl7/fhir/instance/model/valuesets/
cp -vp $FHIRTRUNK/build/implementations/java/org.hl7.fhir.instance/src/org/hl7/fhir/instance/formats/FormatUtilities.java hapi-fhir-structures-hl7org-dstu2/src/main/java/org/hl7/fhir/instance/formats/
cp -vp $FHIRTRUNK/build/implementations/java/org.hl7.fhir.instance/src/org/hl7/fhir/instance/formats/ParserType.java hapi-fhir-structures-hl7org-dstu2/src/main/java/org/hl7/fhir/instance/formats/
cp -vp $FHIRTRUNK/build/implementations/java/org.hl7.fhir.instance/src/org/hl7/fhir/instance/formats/IParser.java hapi-fhir-structures-hl7org-dstu2/src/main/java/org/hl7/fhir/instance/formats/
cp -vp $FHIRTRUNK/build/implementations/java/org.hl7.fhir.instance/src/org/hl7/fhir/instance/utils/EOperationOutcome.java hapi-fhir-structures-hl7org-dstu2/src/main/java/org/hl7/fhir/instance/utils/
cp -vp $FHIRTRUNK/build/implementations/java/org.hl7.fhir.instance/src/org/hl7/fhir/instance/utils/INarrativeGenerator.java hapi-fhir-structures-hl7org-dstu2/src/main/java/org/hl7/fhir/instance/utils/
cp -vp $FHIRTRUNK/build/implementations/java/org.hl7.fhir.instance/src/org/hl7/fhir/instance/utils/IWorkerContext.java hapi-fhir-structures-hl7org-dstu2/src/main/java/org/hl7/fhir/instance/utils/
cp -vp $FHIRTRUNK/build/implementations/java/org.hl7.fhir.instance/src/org/hl7/fhir/instance/utils/ProfileUtilities.java hapi-fhir-structures-hl7org-dstu2/src/main/java/org/hl7/fhir/instance/utils/

cp -vp $FHIRTRUNK/build/implementations/java/org.hl7.fhir.instance/src/org/hl7/fhir/instance/terminologies/ValueSetExpansionCache.java hapi-fhir-structures-hl7org-dstu2/src/main/java/org/hl7/fhir/instance/terminologies/
cp -vp $FHIRTRUNK/build/implementations/java/org.hl7.fhir.instance/src/org/hl7/fhir/instance/terminologies/ValueSetExpander.java hapi-fhir-structures-hl7org-dstu2/src/main/java/org/hl7/fhir/instance/terminologies/
cp -vp $FHIRTRUNK/build/implementations/java/org.hl7.fhir.instance/src/org/hl7/fhir/instance/terminologies/ValueSetExpanderFactory.java hapi-fhir-structures-hl7org-dstu2/src/main/java/org/hl7/fhir/instance/terminologies/
cp -vp $FHIRTRUNK/build/implementations/java/org.hl7.fhir.instance/src/org/hl7/fhir/instance/terminologies/ValueSetChecker.java hapi-fhir-structures-hl7org-dstu2/src/main/java/org/hl7/fhir/instance/terminologies/
cp -vp $FHIRTRUNK/build/implementations/java/org.hl7.fhir.instance/src/org/hl7/fhir/instance/terminologies/ValueSetExpanderSimple.java hapi-fhir-structures-hl7org-dstu2/src/main/java/org/hl7/fhir/instance/terminologies/
cp -vp $FHIRTRUNK/build/implementations/java/org.hl7.fhir.instance/src/org/hl7/fhir/instance/terminologies/ValueSetCheckerSimple.java hapi-fhir-structures-hl7org-dstu2/src/main/java/org/hl7/fhir/instance/terminologies/

cp -vp $FHIRTRUNK/build/implementations/java/org.hl7.fhir.instance/src/org/hl7/fhir/instance/utils/WorkerContext.java hapi-fhir-structures-hl7org-dstu2/src/main/java/org/hl7/fhir/instance/utils/
cp -vp $FHIRTRUNK/build/implementations/java/org.hl7.fhir.instance/src/org/hl7/fhir/instance/utils/NameResolver.java hapi-fhir-structures-hl7org-dstu2/src/main/java/org/hl7/fhir/instance/utils/
cp -vp $FHIRTRUNK/build/implementations/java/org.hl7.fhir.instance/src/org/hl7/fhir/instance/validation/BaseValidator.java hapi-fhir-structures-hl7org-dstu2/src/main/java/org/hl7/fhir/instance/validation/
cp -vp $FHIRTRUNK/build/implementations/java/org.hl7.fhir.instance/src/org/hl7/fhir/instance/validation/InstanceValidator.java hapi-fhir-structures-hl7org-dstu2/src/main/java/org/hl7/fhir/instance/validation/
cp -vp $FHIRTRUNK/build/implementations/java/org.hl7.fhir.instance/src/org/hl7/fhir/instance/validation/ValidationMessage.java hapi-fhir-structures-hl7org-dstu2/src/main/java/org/hl7/fhir/instance/validation/ValidationMessage.java
cp -vp $FHIRTRUNK/build/implementations/java/org.hl7.fhir.instance/src/org/hl7/fhir/instance/validation/IResourceValidator.java hapi-fhir-structures-hl7org-dstu2/src/main/java/org/hl7/fhir/instance/validation/
cp -vp $FHIRTRUNK/build/implementations/java/org.hl7.fhir.utilities/src/org/hl7/fhir/utilities/xhtml/HeirarchicalTableGenerator.java hapi-fhir-structures-hl7org-dstu2/src/main/java/org/hl7/fhir/utilities/xhtml/
cp -av $FHIRTRUNK/build/implementations/java/org.hl7.fhir.utilities/src/org/hl7/fhir/utilities/Utilities.java hapi-fhir-structures-hl7org-dstu2/src/main/java/org/hl7/fhir/utilities/
cp -av $FHIRTRUNK/build/implementations/java/org.hl7.fhir.instance/src/org/hl7/fhir/instance/utils/ToolingExtensions.java hapi-fhir-structures-hl7org-dstu2/src/main/java/org/hl7/fhir/instance/utils/
cp -av $FHIRTRUNK/build/implementations/java/org.hl7.fhir.instance/src/org/hl7/fhir/instance/client/IFHIRClient.java hapi-fhir-structures-hl7org-dstu2/src/main/java/org/hl7/fhir/instance/client/

# Schemas
cp -vp $FHIRTRUNK/build/publish/fhir-single.xsd hapi-fhir-structures-hl7org-dstu2/src/main/resources/org/hl7/fhir/instance/model/schema/
cp -vp $FHIRTRUNK/build/publish/fhir-xhtml.xsd hapi-fhir-structures-hl7org-dstu2/src/main/resources/org/hl7/fhir/instance/model/schema/
cp -vp $FHIRTRUNK/build/publish/xml.xsd hapi-fhir-structures-hl7org-dstu2/src/main/resources/org/hl7/fhir/instance/model/schema/
for i in $( ls $FHIRTRUNK/build/publish/*.sch | grep -vp - ); do cp $i hapi-fhir-structures-hl7org-dstu2/src/main/resources/org/hl7/fhir/instance/model/schema; done

# Profiles
for i in $(find $FHIRTRUNK/build/publish | grep -E "publish\/[a-z]+\.profile.xml$"); do echo $i; cp $i hapi-fhir-structures-hl7org-dstu2/src/main/resources/org/hl7/fhir/instance/model/profile/; done

