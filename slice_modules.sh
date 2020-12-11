#!/bin/bash

totalAgents=$SYSTEM_TOTALJOBSINPHASE
agentNumber=$SYSTEM_JOBPOSITIONINPHASE

#totalAgents=4
#agentNumber=2

if [[ $totalAgents -eq 0 ]]; then totalAgents=1; fi
if [ -z "$agentNumber" ]; then agentNumber=1; fi

echo $totalAgents
echo $agentNumber

# Find all directories that have hapi- in the name, assuming they are all modules.
modules=$(ls | grep "hapi-" | grep -v "\." | grep -v "hapi-fhir-jpaserver-base" | grep -v "hapi-fhir-android-realm" | grep -v "hapi-fhir-narrativegenerator" | grep -v "hapi-fhir-oauth2" | grep -v "hapi-fhir-testpage-interceptor" | grep -v "hapi-fhir-structures-dstu" | grep -v "hapi-fhir-jacoco")


# Every slice needs jacoco
modulesToTest="hapi-fhir-jacoco"

# Assign the
if [ $agentNumber -eq 1 ]; then modulesToTest="$modulesToTest,hapi-fhir-jpaserver-base"; fi

responsibleAgentCounter=2

# For each parallel agent that _isnt_ the first, evenly split the remaining modules between them
for i in $modules; do
   echo Counter is $responsibleAgentCounter
   if [[ $responsibleAgentCounter -eq $agentNumber ]]; then
      echo "adding $i to modules to test!";
      modulesToTest=$modulesToTest,$i;
   fi

   responsibleAgentCounter=$((responsibleAgentCounter+1))
   if [[ $responsibleAgentCounter -gt $totalAgents ]]; then responsibleAgentCounter=2; fi
done

echo This agent \[\#$agentNumber\] is responsible for these modules: $modulesToTest

# Set an Azure environment variable via this janky vso echo.
# This variable represents which projects this particular agent is responsible for testing.
echo "##vso[task.setvariable variable=slicedModules]$modulesToTest"
