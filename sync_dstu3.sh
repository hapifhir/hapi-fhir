
# From /Users/james/workspace/fhirbuild/trunk/build/implementations/java
cp ./org.hl7.fhir.dstu3/src/org/hl7/fhir/dstu3/validation/InstanceValidator.java ~/workspace/hapi-fhir/hapi-fhir-structures-dstu3/src/main/java/org/hl7/fhir/dstu3/validation/InstanceValidator.java
cp ./org.hl7.fhir.dstu3/src/org/hl7/fhir/dstu3/utils/ProfileUtilities.java ~/workspace/hapi-fhir/hapi-fhir-structures-dstu3/src/main/java/org/hl7/fhir/dstu3/utils/ProfileUtilities.java
cp ./org.hl7.fhir.dstu3/src/org/hl7/fhir/dstu3/utils/IWorkerContext.java ~/workspace/hapi-fhir/hapi-fhir-structures-dstu3/src/main/java/org/hl7/fhir/dstu3/utils/IWorkerContext.java
cp ./org.hl7.fhir.dstu3/src/org/hl7/fhir/dstu3/utils/FHIRPathEngine.java ~/workspace/hapi-fhir/hapi-fhir-structures-dstu3/src/main/java/org/hl7/fhir/dstu3/utils/FHIRPathEngine.java
cp ./org.hl7.fhir.dstu3/src/org/hl7/fhir/dstu3/terminologies/ValueSet*.java ~/workspace/hapi-fhir/hapi-fhir-structures-dstu3/src/main/java/org/hl7/fhir/dstu3/terminologies/
cp ./org.hl7.fhir.dstu3/src/org/hl7/fhir/dstu3/terminologies/CodeSystemUtilities.java ~/workspace/hapi-fhir/hapi-fhir-structures-dstu3/src/main/java/org/hl7/fhir/dstu3/terminologies/

cp ./org.hl7.fhir.utilities/src/org/hl7/fhir/utilities/xml/XMLUtil.java ~/workspace/hapi-fhir/hapi-fhir-structures-dstu3/src/main/java/org/hl7/fhir/utilities/xml/XMLUtil.java

# From /Users/james/workspace/fhirbuild/trunk/build
cp ./source/*/*-spreadsheet.xml ~/workspace/hapi-fhir/hapi-tinder-plugin/src/main/resources/res/dstu3/
rm ~/workspace/hapi-fhir/hapi-tinder-plugin/src/main/resources/res/dstu3/*extensions*
rm ~/workspace/hapi-fhir/hapi-tinder-plugin/src/main/resources/res/dstu3/*-profile-*
