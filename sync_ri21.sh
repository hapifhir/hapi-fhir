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

# Download Validation Resources from Official HL7 FHIR DSTU2.1 Release
# This ensures we get DSTU2.1-specific files without newer FHIR version elements
# See: https://github.com/hapifhir/hapi-fhir/issues/6546

# Configurable base URL - defaults to official HL7 FHIR DSTU2.1 release
FHIR_DSTU21_URL="${FHIR_DSTU21_URL:-https://hl7.org/fhir/DSTU2.1}"

echo "Downloading DSTU2.1 validation resources from: ${FHIR_DSTU21_URL}"

# Helper function to download a file with error handling
download_file() {
    local url="$1"
    local dest="$2"
    echo "  Downloading: ${url}"
    if ! curl -fsSL --retry 3 --retry-delay 2 -o "${dest}" "${url}"; then
        echo "ERROR: Failed to download ${url}" >&2
        exit 1
    fi
}

BASE_DIR="hapi-fhir-validation-resources-$PROJVERSION/src/main/resources/org/hl7/fhir/instance/model/$PACKAGEVERSION"

# Download valueset files
VALUESET_DIR="${BASE_DIR}/valueset"
download_file "${FHIR_DSTU21_URL}/valuesets.xml" "${VALUESET_DIR}/valuesets.xml"
download_file "${FHIR_DSTU21_URL}/v2-tables.xml" "${VALUESET_DIR}/v2-tables.xml"
download_file "${FHIR_DSTU21_URL}/v3-codesystems.xml" "${VALUESET_DIR}/v3-codesystems.xml"

# Download profile files
PROFILE_DIR="${BASE_DIR}/profile"
touch "${PROFILE_DIR}/_.xml"
rm "${PROFILE_DIR}"/*.xml 2>/dev/null || true
download_file "${FHIR_DSTU21_URL}/profiles-resources.xml" "${PROFILE_DIR}/profiles-resources.xml"
download_file "${FHIR_DSTU21_URL}/profiles-types.xml" "${PROFILE_DIR}/profiles-types.xml"
download_file "${FHIR_DSTU21_URL}/profiles-others.xml" "${PROFILE_DIR}/profiles-others.xml"

# Download schema and schematron files from ZIP
# The ZIP contains all XSD and SCH files needed for validation
SCHEMA_DIR="${BASE_DIR}/schema"
TEMP_ZIP="/tmp/fhir-dstu21-xsd-$$.zip"

echo "  Downloading: ${FHIR_DSTU21_URL}/fhir-all-xsd.zip"
if ! curl -fsSL --retry 3 --retry-delay 2 -o "${TEMP_ZIP}" "${FHIR_DSTU21_URL}/fhir-all-xsd.zip"; then
    echo "ERROR: Failed to download ${FHIR_DSTU21_URL}/fhir-all-xsd.zip" >&2
    exit 1
fi

# Extract XSD files
echo "  Extracting XSD files..."
unzip -o -j "${TEMP_ZIP}" "*.xsd" -d "${SCHEMA_DIR}/" > /dev/null

# Extract SCH (Schematron) files
echo "  Extracting SCH (Schematron) files..."
unzip -o -j "${TEMP_ZIP}" "*.sch" -d "${SCHEMA_DIR}/" > /dev/null

# Cleanup
rm -f "${TEMP_ZIP}"

echo "DSTU2.1 validation resources downloaded successfully."

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
