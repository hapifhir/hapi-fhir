set -euf -o pipefail

MODULES_CORE=\
hapi-deployable-pom,\
hapi-fhir-base,\
hapi-fhir-utilities,\
hapi-tinder-plugin,\
hapi-tinder-test,\
hapi-fhir-client,\
hapi-fhir-server,\
hapi-fhir-validation,\
hapi-fhir-structures-dstu2,\
hapi-fhir-structures-hl7org-dstu2,\
hapi-fhir-validation-resources-dstu2,\
hapi-fhir-structures-dstu2.1,\
hapi-fhir-validation-resources-dstu2.1,\
hapi-fhir-structures-dstu3,\
hapi-fhir-validation-resources-dstu3,\
hapi-fhir-structures-r4,\
hapi-fhir-validation-resources-r4,\
hapi-fhir-igpacks,\
restful-server-example,\
hapi-fhir-testpage-overlay,\
hapi-fhir-client-okhttp,\
hapi-fhir-android,\
hapi-fhir-converter,\
hapi-fhir-cli,\
hapi-fhir-dist,\
examples,\
example-projects/hapi-fhir-base-example-embedded-ws,\
example-projects/hapi-fhir-standalone-overlay-example,\
tests/hapi-fhir-base-test-mindeps-client,\
tests/hapi-fhir-base-test-mindeps-server,\
hapi-fhir-spring-boot,\
hapi-fhir-test-utilities

MODULES_JPASERVER=\
hapi-fhir-jpaserver-model,\
hapi-fhir-jpaserver-searchparam,\
hapi-fhir-jpaserver-subscription,\
hapi-fhir-jaxrsserver-base,\
hapi-fhir-jaxrsserver-example,\
hapi-fhir-jpaserver-base,\
hapi-fhir-jpaserver-elasticsearch,\
hapi-fhir-jpaserver-migrate,\
example-projects/hapi-fhir-jpaserver-cds-example,\
example-projects/hapi-fhir-jpaserver-dynamic,\
example-projects/hapi-fhir-jpaserver-example-postgres,\
hapi-fhir-jpaserver-uhnfhirtest

STAGE=$1

#Sanity check that all modules are in one of the module lists, and that no modules appear that are not part of HAPI FHIR
ALL_HAPI_MODULES_SORTED=`mvn help:evaluate -Dexpression=project.modules | grep "<string>" | sed 's/<\/*string>//g' | sed 's/[[:space:]]*//' | sort`
ALL_TRAVIS_MODULES_SORTED=`echo "$MODULES_CORE,$MODULES_JPASERVER" | tr , '\n' | sort`

MISCONFIGURED_MODULES=`comm -3 <(echo "$ALL_HAPI_MODULES_SORTED") <(echo "$ALL_TRAVIS_MODULES_SORTED")`
if [ -n "$MISCONFIGURED_MODULES" ]; then
	echo "One or more modules are either present in the build, and not included in a module list, or present in a module list and not present in the build"
	echo "Missing in the Travis module list: " `comm -23 <(echo "$ALL_HAPI_MODULES_SORTED") <(echo "$ALL_TRAVIS_MODULES_SORTED")`
	echo "Present in Travis module list, but not in HAPI FHIR build: " `comm -13 <(echo "$ALL_HAPI_MODULES_SORTED") <(echo "$ALL_TRAVIS_MODULES_SORTED")`
	exit 1
fi

if [ $STAGE = "compile" ]; then
	mvn -Dci=true -e -B -P ALLMODULES clean install -DskipTests -DskipITs
elif [ $STAGE = "core" ]; then
	mvn -Dci=true -e -B -P ALLMODULES clean install -DskipTests -DskipITs -pl "$MODULES_CORE" -am
	mvn -Dci=true -e -B -P ALLMODULES verify -pl "$MODULES_CORE"
elif [ $STAGE = "jpaserver" ]; then
	mvn -Dci=true -e -B -P ALLMODULES clean install -DskipTests -DskipITs -pl "$MODULES_JPASERVER" -am
	mvn -Dci=true -e -B -P ALLMODULES verify -pl "$MODULES_JPASERVER"
fi
