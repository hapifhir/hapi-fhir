#!/bin/bash

# resolve-pom-versions.sh
# Resolves <version> tag conflicts in pom.xml files during merges
# Usage: ./scripts/resolve-pom-versions.sh

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}=== POM Version Conflict Resolver ===${NC}"
echo

# Find all pom.xml files with conflicts
CONFLICTED_POMS=$(git diff --name-only --diff-filter=U | grep "pom\.xml$" || true)

if [ -z "$CONFLICTED_POMS" ]; then
    echo -e "${GREEN}No pom.xml files with conflicts found.${NC}"
    exit 0
fi

POM_COUNT=$(echo "$CONFLICTED_POMS" | wc -l | tr -d ' ')
echo -e "Found ${YELLOW}$POM_COUNT${NC} pom.xml file(s) with conflicts."
echo

# Find the first pom.xml that actually has version conflicts
FIRST_POM=""
for pom in $CONFLICTED_POMS; do
    if grep -q "^<<<<<<< " "$pom" 2>/dev/null; then
        FIRST_POM="$pom"
        break
    fi
done

if [ -z "$FIRST_POM" ]; then
    echo -e "${YELLOW}No pom.xml files with actual conflict markers found.${NC}"
    echo "The files may already be resolved but not staged."
    exit 0
fi

# Extract HEAD version (the version after <<<<<<< HEAD)
HEAD_VERSION=$(grep -A1 "^<<<<<<< " "$FIRST_POM" | grep -o '<version>[^<]*</version>' | head -1 | sed 's/<version>\(.*\)<\/version>/\1/')

# Extract incoming version (the version after =======)
INCOMING_VERSION=$(grep -A1 "^=======" "$FIRST_POM" | grep -o '<version>[^<]*</version>' | head -1 | sed 's/<version>\(.*\)<\/version>/\1/')

# Extract branch name from conflict marker
BRANCH_NAME=$(grep "^>>>>>>>" "$FIRST_POM" | head -1 | sed 's/>>>>>>> //')

if [ -z "$HEAD_VERSION" ] || [ -z "$INCOMING_VERSION" ]; then
    echo -e "${RED}Error: Could not extract versions from conflicts.${NC}"
    echo "HEAD_VERSION: $HEAD_VERSION"
    echo "INCOMING_VERSION: $INCOMING_VERSION"
    exit 1
fi

echo -e "Detected version conflict:"
echo -e "  ${GREEN}[1]${NC} $HEAD_VERSION (HEAD/current branch)"
echo -e "  ${GREEN}[2]${NC} $INCOMING_VERSION ($BRANCH_NAME)"
echo
read -p "Which version do you want to use? [1/2]: " choice

case $choice in
    1)
        CHOSEN_VERSION="$HEAD_VERSION"
        echo -e "\nUsing version: ${GREEN}$CHOSEN_VERSION${NC}"
        ;;
    2)
        CHOSEN_VERSION="$INCOMING_VERSION"
        echo -e "\nUsing version: ${GREEN}$CHOSEN_VERSION${NC}"
        ;;
    *)
        echo -e "${RED}Invalid choice. Exiting.${NC}"
        exit 1
        ;;
esac

echo
echo "Resolving conflicts..."
echo

RESOLVED_COUNT=0
RESOLVED_FILES=0

# Process each pom.xml file
for pom in $CONFLICTED_POMS; do
    # Count conflicts before
    BEFORE_COUNT=$(grep -c "^<<<<<<< " "$pom" 2>/dev/null || echo "0")
    BEFORE_COUNT=${BEFORE_COUNT//[^0-9]/}

    # Use perl to replace version conflict blocks
    # Matches: <<<<<<< HEAD / <version>X</version> / ======= / <version>Y</version> / >>>>>>>
    # Replaces with just <version>CHOSEN</version> preserving indentation
    perl -i -0777 -pe 's/^<<<<<<< [^\n]*\n(\s*)<version>[^<]*<\/version>\n=======\n\s*<version>[^<]*<\/version>\n>>>>>>> [^\n]*\n/${1}<version>'"$CHOSEN_VERSION"'<\/version>\n/gm' "$pom"

    # Count conflicts after
    AFTER_COUNT=$(grep -c "^<<<<<<< " "$pom" 2>/dev/null || echo "0")
    AFTER_COUNT=${AFTER_COUNT//[^0-9]/}

    BEFORE_COUNT=${BEFORE_COUNT:-0}
    AFTER_COUNT=${AFTER_COUNT:-0}
    RESOLVED_IN_FILE=$((BEFORE_COUNT - AFTER_COUNT))
    if [ "$RESOLVED_IN_FILE" -gt 0 ]; then
        RESOLVED_COUNT=$((RESOLVED_COUNT + RESOLVED_IN_FILE))
        RESOLVED_FILES=$((RESOLVED_FILES + 1))
        echo -e "  ${GREEN}✓${NC} $pom - resolved $RESOLVED_IN_FILE conflict(s)"

        if [ "$AFTER_COUNT" -gt 0 ]; then
            echo -e "    ${YELLOW}⚠${NC} $AFTER_COUNT non-version conflict(s) remain"
        fi
    else
        echo -e "  ${YELLOW}○${NC} $pom - no version conflicts found"
    fi
done

echo
echo -e "${BLUE}=== Staging Clean Files ===${NC}"

STAGED_COUNT=0
POMS_WITH_CONFLICTS=""

# Check each pom.xml and stage if clean, otherwise track it
for pom in $CONFLICTED_POMS; do
    if grep -q "^<<<<<<< " "$pom" 2>/dev/null; then
        # Still has conflicts - don't stage
        POMS_WITH_CONFLICTS="$POMS_WITH_CONFLICTS$pom\n"
    else
        # Clean - stage it
        git add "$pom"
        STAGED_COUNT=$((STAGED_COUNT + 1))
    fi
done

echo -e "Staged ${GREEN}$STAGED_COUNT${NC} fully-resolved pom.xml file(s)"

# Report poms that still have non-version conflicts
if [ -n "$POMS_WITH_CONFLICTS" ]; then
    echo
    echo -e "${YELLOW}=== POM Files With Remaining Conflicts ===${NC}"
    echo -e "${YELLOW}The following pom.xml files have non-version conflicts that need manual resolution:${NC}"
    echo -e "$POMS_WITH_CONFLICTS" | while read pom; do
        if [ -n "$pom" ]; then
            CONFLICT_COUNT=$(grep -c "^<<<<<<< " "$pom" 2>/dev/null || echo "0")
            echo -e "  ${RED}✗${NC} $pom ($CONFLICT_COUNT conflict(s))"
        fi
    done
fi

echo
echo -e "${BLUE}=== Summary ===${NC}"
echo -e "Resolved ${GREEN}$RESOLVED_COUNT${NC} version conflict(s) in ${GREEN}$RESOLVED_FILES${NC} file(s)"
echo -e "Staged ${GREEN}$STAGED_COUNT${NC} fully-resolved pom.xml file(s)"

# Check for remaining non-pom conflicts
REMAINING_NON_POM=$(git diff --name-only --diff-filter=U | grep -v "pom\.xml$" | wc -l | tr -d ' ')
if [ "$REMAINING_NON_POM" -gt 0 ]; then
    echo
    echo -e "${YELLOW}Note:${NC} $REMAINING_NON_POM non-pom file(s) still have unresolved conflicts:"
    git diff --name-only --diff-filter=U | grep -v "pom\.xml$" | while read f; do
        echo "  - $f"
    done
fi

echo
echo -e "${GREEN}Done!${NC}"
