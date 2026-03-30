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

# Download Validation Resources from Official HL7 FHIR STU3 Release
# This ensures we get STU3-specific files without newer FHIR version elements
# See: https://github.com/hapifhir/hapi-fhir/issues/6546

# Configurable base URL - defaults to official HL7 FHIR STU3 release
set -q FHIR_STU3_URL; or set FHIR_STU3_URL "https://hl7.org/fhir/STU3"

echo "Downloading STU3 validation resources from: $FHIR_STU3_URL"

# Helper function to download a file with error handling
function download_file
    set url $argv[1]
    set dest $argv[2]
    echo "  Downloading: $url"
    if not curl -fsSL --retry 3 --retry-delay 2 -o "$dest" "$url"
        echo "ERROR: Failed to download $url" >&2
        exit 1
    end
end

set BASE_DIR "hapi-fhir-validation-resources-dstu3/src/main/resources/org/hl7/fhir/instance/model/dstu3"

# Download schema and schematron files from ZIP
# The ZIP contains all XSD and SCH files needed for validation
set SCHEMA_DIR "$BASE_DIR/schema"
set TEMP_ZIP "/tmp/fhir-stu3-xsd-"(random)".zip"

echo "  Downloading: $FHIR_STU3_URL/fhir-all-xsd.zip"
if not curl -fsSL --retry 3 --retry-delay 2 -o "$TEMP_ZIP" "$FHIR_STU3_URL/fhir-all-xsd.zip"
    echo "ERROR: Failed to download $FHIR_STU3_URL/fhir-all-xsd.zip" >&2
    exit 1
end

# Extract XSD files
echo "  Extracting XSD files..."
unzip -o -j "$TEMP_ZIP" "*.xsd" -d "$SCHEMA_DIR/" > /dev/null

# Extract SCH (Schematron) files
echo "  Extracting SCH (Schematron) files..."
unzip -o -j "$TEMP_ZIP" "*.sch" -d "$SCHEMA_DIR/" > /dev/null

# Cleanup
rm -f "$TEMP_ZIP"

# Download profile files
set PROFILE_DIR "$BASE_DIR/profile"
download_file "$FHIR_STU3_URL/profiles-resources.xml" "$PROFILE_DIR/profiles-resources.xml"
download_file "$FHIR_STU3_URL/profiles-types.xml" "$PROFILE_DIR/profiles-types.xml"
download_file "$FHIR_STU3_URL/profiles-others.xml" "$PROFILE_DIR/profiles-others.xml"

# Download valueset files
set VALUESET_DIR "$BASE_DIR/valueset"
download_file "$FHIR_STU3_URL/valuesets.xml" "$VALUESET_DIR/valuesets.xml"
download_file "$FHIR_STU3_URL/v2-tables.xml" "$VALUESET_DIR/v2-tables.xml"
download_file "$FHIR_STU3_URL/v3-codesystems.xml" "$VALUESET_DIR/v3-codesystems.xml"

echo "STU3 validation resources downloaded successfully."
